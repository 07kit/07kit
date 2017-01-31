package com.kit.game.transform.impl;

import com.kit.api.event.ActionEvent;
import com.kit.api.event.Events;
import com.kit.game.transform.Extender;
import com.kit.game.transform.model.ClassDefinition;
import com.kit.game.transform.model.MethodDefinition;
import com.kit.api.event.ActionEvent;
import com.kit.api.event.Events;
import com.kit.game.transform.Extender;
import com.kit.game.transform.model.ClassDefinition;
import com.kit.game.transform.model.MethodDefinition;
import org.apache.log4j.Logger;
import org.apache.log4j.net.SyslogAppender;
import com.kit.game.transform.Extender;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.objectweb.asm.Opcodes.*;


/**
 * @author : const_
 */
public class DoActionExtender implements Extender {
	private static Logger logger = Logger.getLogger(MessageExtender.class);
	private MethodDefinition methodDefinition;

	public DoActionExtender(Map<String, ClassDefinition> definitions) {
		classes:
		for (ClassDefinition def : definitions.values()) {
			for (MethodDefinition methodDefinition : def.methods) {
				if (methodDefinition.name.equals("doAction")) {
					this.methodDefinition = methodDefinition;
					break classes;
				}
			}
		}
		if (methodDefinition == null) {
			logger.error("doAction is null");
		}
	}

	@Override
	public boolean canRun(ClassNode clazz) {
		return methodDefinition != null &&
				methodDefinition.owner.equals(clazz.name);
	}

	@Override
	public void apply(Map<String, ClassDefinition> definitions, ClassNode clazz) {
		for (MethodNode method : (List<MethodNode>) clazz.methods) {
			if (method.name.equals(methodDefinition.actualName) && method.desc.equals(methodDefinition.actualDesc)) {
				InsnList current = method.instructions;
				InsnList inject = new InsnList();
				inject.add(new MethodInsnNode(INVOKESTATIC, "client", "getEventBus", "()L" +
						Events.class.getCanonicalName().replaceAll("\\.", "/") + ";"));
				inject.add(new TypeInsnNode(NEW, ActionEvent.class.getCanonicalName().replaceAll("\\.", "/")));

				InsnList integerInsns = new InsnList();
				InsnList stringInsns = new InsnList();
				List<Integer> paramLoadOpcodes = methodDefinition.paramLoadOpcodes;
				for (int i = 0; i < paramLoadOpcodes.size(); i++) {
					int paramLoadOpcode = paramLoadOpcodes.get(i);
					if (paramLoadOpcode == Opcodes.ILOAD && integerInsns.size() < 4) {
						integerInsns.add(new VarInsnNode(paramLoadOpcode, i));
					} else if (paramLoadOpcode == Opcodes.ALOAD) {
						stringInsns.add(new VarInsnNode(paramLoadOpcode, i));
					}
				}

				inject.add(new InsnNode(Opcodes.DUP));
				inject.add(integerInsns);
				inject.add(stringInsns);
				inject.add(new MethodInsnNode(INVOKESPECIAL, ActionEvent.class.getCanonicalName().replaceAll("\\.", "/"), "<init>", "(IIIILjava/lang/String;Ljava/lang/String;)V"));
				inject.add(new MethodInsnNode(INVOKEVIRTUAL, Events.class.getCanonicalName().replaceAll("\\.", "/"), "submit", "(Ljava/lang/Object;)V"));
				AbstractInsnNode before = current.getFirst();
				current.insertBefore(before, inject);
				method.visitEnd();
			}
		}
	}
}
