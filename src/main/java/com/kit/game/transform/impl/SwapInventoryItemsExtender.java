package com.kit.game.transform.impl;

import com.kit.api.event.Events;
import com.kit.api.event.SwapInventoryItemsEvent;
import com.kit.game.transform.Extender;
import com.kit.game.transform.model.ClassDefinition;
import com.kit.game.transform.model.MethodDefinition;
import com.kit.api.event.Events;
import com.kit.api.event.SwapInventoryItemsEvent;
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


/**
 * @author : const_
 */
public class SwapInventoryItemsExtender implements Extender {
	private static Logger logger = Logger.getLogger(MessageExtender.class);
	private MethodDefinition methodDef;

	public SwapInventoryItemsExtender(Map<String, ClassDefinition> definitions) {
		classes:
		for (ClassDefinition def : definitions.values()) {
			for (MethodDefinition methodDefinition : def.methods) {
				if (methodDefinition.name.equals("swapInventoryItems")) {
					methodDef = methodDefinition;
					break classes;
				}
			}
		}
		if (methodDef == null) {
			logger.error("swapInventoryItems is null");
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

				inject.add(new MethodInsnNode(INVOKESTATIC, "client", "getEventBus", "()L" + Events.class.getCanonicalName().replaceAll("\\.", "/") + ";"));
				inject.add(new TypeInsnNode(NEW, SwapInventoryItemsEvent.class.getCanonicalName().replaceAll("\\.", "/")));

				inject.add(new InsnNode(Opcodes.DUP));
				inject.add(new VarInsnNode(Opcodes.ILOAD, 1));
				inject.add(new VarInsnNode(Opcodes.ILOAD, 2));
				inject.add(new MethodInsnNode(INVOKESPECIAL, SwapInventoryItemsEvent.class.getCanonicalName().replaceAll("\\.", "/"), "<init>", "(II)V"));
				inject.add(new MethodInsnNode(INVOKEVIRTUAL, Events.class.getCanonicalName().replaceAll("\\.", "/"), "submit", "(Ljava/lang/Object;)V"));
				current.insertBefore(current.getFirst(), inject);
				method.visitEnd();
			}
		}
	}
}
