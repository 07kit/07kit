package com.kit.game.transform.impl;

import com.kit.api.event.Events;
import com.kit.game.transform.Extender;
import com.kit.game.transform.model.ClassDefinition;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.Map;

/**
 */
public class EventBusExtender implements Extender {
    @Override
    public boolean canRun(ClassNode clazz) {
        return clazz.name.equals("client");
    }

    @Override
    public void apply(Map<String, ClassDefinition> classes, ClassNode classNode) {
        FieldNode eventBusField = new FieldNode(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, "eventBus",
                "L" + Events.class.getCanonicalName().replaceAll("\\.", "/") + ";", "L" + Events.class.getCanonicalName().replaceAll("\\.", "/") + ";", null);
        classNode.fields.add(eventBusField);
        MethodNode getter = new MethodNode(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, "getEventBus",
                "()L" + Events.class.getCanonicalName().replaceAll("\\.", "/") + ";", null, null);
        getter.instructions.add(new FieldInsnNode(Opcodes.GETSTATIC, classNode.name,
                eventBusField.name, eventBusField.desc));
        getter.instructions.add(new InsnNode(Opcodes.ARETURN));
        getter.visitEnd();
        getter.visitMaxs(1, 1);
        classNode.methods.add(getter);
        MethodNode setter = new MethodNode(Opcodes.ACC_PUBLIC, "setEventBus",
                "(L" + Events.class.getCanonicalName().replaceAll("\\.", "/") + ";)V", null, null);
        InsnList list = new InsnList();
        list.add(new VarInsnNode(Opcodes.ALOAD, 0));
        list.add(new VarInsnNode(Opcodes.ALOAD, 1));
        list.add(new FieldInsnNode(Opcodes.PUTSTATIC, classNode.name, eventBusField.name, eventBusField.desc));
        list.add(new InsnNode(Opcodes.RETURN));
        setter.instructions.add(list);
        setter.visitEnd();
        setter.visitMaxs(1, 1);
        classNode.methods.add(setter);
    }
}
