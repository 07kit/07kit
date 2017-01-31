package com.kit.game.transform.impl;

import com.kit.api.event.ClientLoopEvent;
import com.kit.api.event.Events;
import com.kit.game.transform.Extender;
import com.kit.game.transform.model.ClassDefinition;
import com.kit.game.transform.model.MethodDefinition;
import org.apache.log4j.Logger;
import com.kit.game.transform.Extender;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.List;
import java.util.Map;

import static org.objectweb.asm.Opcodes.*;

public class ClientLoopExtender implements Extender {
	private static Logger logger = Logger.getLogger(MessageExtender.class);
	private MethodDefinition methodDef;

	public ClientLoopExtender(Map<String, ClassDefinition> definitions) {
		classes:
		for (ClassDefinition def : definitions.values()) {
			for (MethodDefinition methodDefinition : def.methods) {
				if (methodDefinition.name.equals("clientLoop")) {
					methodDef = methodDefinition;
					break classes;
				}
			}
		}
		if (methodDef == null) {
			logger.error("clientLoop is null");
		}
	}

	@Override
	public boolean canRun(ClassNode clazz) {
		return methodDef != null &&
				methodDef.owner.equals(clazz.name);
	}

	@Override
	public void apply(Map<String, ClassDefinition> definitions, ClassNode clazz) {
		for (MethodNode method : (List<MethodNode>) clazz.methods) {
			if (method.name.equals(methodDef.actualName) && method.desc.equals(methodDef.actualDesc)) {
				InsnList current = method.instructions;
				InsnList inject = new InsnList();

				inject.add(new MethodInsnNode(INVOKESTATIC, "client", "getEventBus", "()L" +
						Events.class.getCanonicalName().replaceAll("\\.", "/") + ";"));
				inject.add(new TypeInsnNode(NEW, ClientLoopEvent.class.getCanonicalName().replaceAll("\\.", "/")));

				inject.add(new InsnNode(Opcodes.DUP));
				inject.add(new MethodInsnNode(INVOKESPECIAL, ClientLoopEvent.class.getCanonicalName().replaceAll("\\.", "/"), "<init>", "()V"));
				inject.add(new MethodInsnNode(INVOKEVIRTUAL, Events.class.getCanonicalName().replaceAll("\\.", "/"), "submit", "(Ljava/lang/Object;)V"));
				current.insertBefore(current.getFirst(), inject);
				method.visitEnd();
			}
		}
	}
}
