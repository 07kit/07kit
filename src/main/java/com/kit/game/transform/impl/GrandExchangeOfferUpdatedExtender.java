package com.kit.game.transform.impl;

import com.kit.api.event.Events;
import com.kit.api.event.GrandExchangeOfferUpdatedEvent;
import com.kit.game.engine.IGrandExchangeOffer;
import com.kit.game.transform.model.ClassDefinition;
import com.kit.api.event.Events;
import com.kit.api.event.GrandExchangeOfferUpdatedEvent;
import com.kit.game.engine.IGrandExchangeOffer;
import com.kit.game.transform.Extender;
import com.kit.game.transform.model.ClassDefinition;
import com.kit.game.transform.model.MethodDefinition;
import org.apache.log4j.Logger;
import com.kit.game.engine.IGrandExchangeOffer;
import com.kit.game.transform.Extender;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Map;
import java.util.List;

import static org.objectweb.asm.Opcodes.*;

public class GrandExchangeOfferUpdatedExtender implements Extender {

	public static final String OFFER_CLASS_NAME = IGrandExchangeOffer.class.getCanonicalName().replaceAll("\\.", "/");

	private Logger logger = Logger.getLogger(getClass());
	private ClassDefinition classDef;

	public GrandExchangeOfferUpdatedExtender(Map<String, ClassDefinition> definitions) {
		for (ClassDefinition def : definitions.values()) {
			if (def.identifiedName.equals(OFFER_CLASS_NAME.replaceAll("/", "."))) {
				classDef = def;
				break;
			}
		}
		if (classDef == null) {
			logger.error("GrandExchangeOffer is null");
		}
	}
	@Override
	public boolean canRun(ClassNode clazz) {
		return classDef != null && clazz.name.equals(classDef.originalName);
	}

	@Override
	public void apply(Map<String, ClassDefinition> classes, ClassNode clazz) {
		for (MethodNode node : (List<MethodNode>) clazz.methods) {
			if (node.name.equals("<init>")) {
				InsnList current = node.instructions;
				InsnList inject = new InsnList();
				inject.add(new MethodInsnNode(INVOKESTATIC, "client", "getEventBus", "()L" + Events.class.getCanonicalName().replaceAll("\\.", "/") + ";"));
				inject.add(new TypeInsnNode(NEW, GrandExchangeOfferUpdatedEvent.class.getCanonicalName().replaceAll("\\.", "/")));

				inject.add(new InsnNode(Opcodes.DUP));
				inject.add(new VarInsnNode(Opcodes.ALOAD, 0));
				inject.add(new TypeInsnNode(Opcodes.CHECKCAST, OFFER_CLASS_NAME));
				inject.add(new MethodInsnNode(INVOKESPECIAL, GrandExchangeOfferUpdatedEvent.class.getCanonicalName().replaceAll("\\.", "/"), "<init>", "(L" +
						OFFER_CLASS_NAME + ";)V"));
				inject.add(new MethodInsnNode(INVOKEVIRTUAL, Events.class.getCanonicalName().replaceAll("\\.", "/"), "submit", "(Ljava/lang/Object;)V"));
				AbstractInsnNode returnInsn = null;
				for (AbstractInsnNode ain : node.instructions.toArray()) {
					if (ain.getOpcode() == Opcodes.RETURN) {
						returnInsn = ain;
						break;
					}
				}
				current.insertBefore(returnInsn, inject);
				node.visitEnd();
			}
		}
	}
}
