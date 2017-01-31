package com.kit.game.transform.impl;

import com.kit.game.transform.model.MethodDefinition;
import com.kit.api.event.Events;
import com.kit.api.event.PlayerMenuCreatedEvent;
import com.kit.game.engine.renderable.entity.IPlayer;
import com.kit.game.transform.Extender;
import com.kit.game.transform.model.ClassDefinition;
import com.kit.game.transform.model.MethodDefinition;
import org.apache.log4j.Logger;
import com.kit.game.transform.Extender;
import com.kit.game.transform.model.ClassDefinition;
import com.kit.game.transform.model.MethodDefinition;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.objectweb.asm.Opcodes.*;

public class PlayerMenuActionsExtender implements Extender {

	private static Logger logger = Logger.getLogger(MessageExtender.class);
	private MethodDefinition methodDef;

	public PlayerMenuActionsExtender(Map<String, ClassDefinition> definitions) {
		classes:
		for (ClassDefinition def : definitions.values()) {
			for (MethodDefinition methodDefinition : def.methods) {
				if (methodDefinition.name.equals("createMenuActionsForPlayer")) {
					methodDef = methodDefinition;
					break classes;
				}
			}
		}
		if (methodDef == null) {
			logger.error("createMenuActionsForPlayer is null");
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
			if (method.name.equals(methodDef.actualName) && method.desc.equals(methodDef.actualDesc)) {InsnList inject = new InsnList();

				inject.add(new MethodInsnNode(INVOKESTATIC, "client", "getEventBus", "()L" + Events.class.getCanonicalName().replaceAll("\\.", "/") + ";"));
				inject.add(new TypeInsnNode(NEW, PlayerMenuCreatedEvent.class.getCanonicalName().replaceAll("\\.", "/")));
				inject.add(new InsnNode(Opcodes.DUP));

				VarInsnNode player = null;
				List<VarInsnNode> intInsns = new ArrayList<>();
				for (int i = 0; i < methodDef.paramLoadOpcodes.size(); i++) {
					if (methodDef.paramLoadOpcodes.get(i) == Opcodes.ALOAD) {
						player = new VarInsnNode(Opcodes.ALOAD, i);
					} else if (methodDef.paramLoadOpcodes.get(i) == Opcodes.ILOAD) {
						intInsns.add(new VarInsnNode(Opcodes.ILOAD, i));
					}
				}

				inject.add(player);
				inject.add(new TypeInsnNode(Opcodes.CHECKCAST, IPlayer.class.getCanonicalName().replaceAll("\\.", "/")));
				intInsns.forEach(inject::add);
				inject.add(new MethodInsnNode(INVOKESPECIAL, PlayerMenuCreatedEvent.class.getCanonicalName().replaceAll("\\.", "/"), "<init>", "(L" + IPlayer.class.getCanonicalName().replaceAll("\\.", "/") + ";III)V"));
				inject.add(new MethodInsnNode(INVOKEVIRTUAL, Events.class.getCanonicalName().replaceAll("\\.", "/"), "submitPlayerMenuCreatedEvent", "(L" + PlayerMenuCreatedEvent.class.getCanonicalName().replaceAll("\\.", "/") + ";)V"));
				method.instructions.insertBefore(method.instructions.get(methodDef.insnToInjectBefore), inject);
				method.visitEnd();
			}
		}
	}
}
