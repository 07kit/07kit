package com.kit.game.transform.impl;

import com.kit.api.event.Events;
import com.kit.api.event.SpawnGroundItemEvent;
import com.kit.game.transform.model.MethodDefinition;
import com.kit.api.event.Events;
import com.kit.api.event.SpawnGroundItemEvent;
import com.kit.game.transform.Extender;
import com.kit.game.transform.model.ClassDefinition;
import com.kit.game.transform.model.MethodDefinition;
import org.apache.log4j.Logger;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.objectweb.asm.Opcodes.*;

public class SpawnGroundItemExtender implements Extender {
	private static Logger logger = Logger.getLogger(MessageExtender.class);
	private MethodDefinition methodDef;

	public SpawnGroundItemExtender(Map<String, ClassDefinition> definitions) {
		classes:
		for (ClassDefinition def : definitions.values()) {
			for (MethodDefinition methodDefinition : def.methods) {
				if (methodDefinition.name.equals("spawnGroundItem")) {
					methodDef = methodDefinition;
					break classes;
				}
			}
		}
		if (methodDef == null) {
			logger.error("spawnGroundItem is null");
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
				inject.add(new TypeInsnNode(NEW, SpawnGroundItemEvent.class.getCanonicalName().replaceAll("\\.", "/")));
				inject.add(new InsnNode(Opcodes.DUP));

				List<VarInsnNode> iloads = new ArrayList<>();

				for (int i = 0; i < methodDef.paramDescs.size(); i++) {
					if (methodDef.paramDescs.get(i).split("~")[0].equals("I") && i < 2) {
						iloads.add(new VarInsnNode(Opcodes.ILOAD, i));
					}
				}

				iloads.forEach(inject::add);

				inject.add(new MethodInsnNode(INVOKESPECIAL, SpawnGroundItemEvent.class.getCanonicalName().replaceAll("\\.", "/"), "<init>", "(II)V"));
				inject.add(new MethodInsnNode(INVOKEVIRTUAL, Events.class.getCanonicalName().replaceAll("\\.", "/"), "submitSpawnGroundItemEvent", "(L" + SpawnGroundItemEvent.class.getCanonicalName().replaceAll("\\.", "/") + ";)V"));
				current.insertBefore(method.instructions.get(methodDef.insnToInjectBefore), inject);
				method.visitEnd();
			}
		}
	}
}
