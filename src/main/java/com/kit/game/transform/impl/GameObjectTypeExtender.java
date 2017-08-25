package com.kit.game.transform.impl;

import com.kit.api.wrappers.GameObject;
import com.kit.api.wrappers.GameObjectType;
import com.kit.game.engine.scene.tile.IBoundaryObject;
import com.kit.game.engine.scene.tile.IFloorObject;
import com.kit.game.engine.scene.tile.IInteractableObject;
import com.kit.game.engine.scene.tile.IWallObject;
import com.kit.game.transform.Extender;
import com.kit.game.transform.model.ClassDefinition;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.List;
import java.util.Map;

public class GameObjectTypeExtender implements Extender {
    @Override
    public boolean canRun(ClassNode classNode) {
        return classNode.interfaces.contains(IInteractableObject.class.getCanonicalName().replaceAll("\\.", "/"))
                || classNode.interfaces.contains(IBoundaryObject.class.getCanonicalName().replaceAll("\\.", "/"))
                || classNode.interfaces.contains(IFloorObject.class.getCanonicalName().replaceAll("\\.", "/"))
                || classNode.interfaces.contains(IWallObject.class.getCanonicalName().replaceAll("\\.", "/"));
    }

    @Override
    public void apply(Map<String, ClassDefinition> classes, ClassNode clazz) {
        String identified = classes.get(clazz.name).identifiedName;

        FieldNode field = injectFieldAndGetter(clazz);

        for (MethodNode mn : (List<MethodNode>) clazz.methods) {
            if (mn.name.equals("<init>")) {
                InsnList insnList = new InsnList();
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));

                String enumValue = null;
                if (identified.equals(IInteractableObject.class.getCanonicalName())) {
                    enumValue = GameObjectType.INTERACTABLE.name();
                } else if (identified.equals(IBoundaryObject.class.getCanonicalName())) {
                    enumValue = GameObjectType.BOUNDARY.name();
                } else if (identified.equals(IFloorObject.class.getCanonicalName())) {
                    enumValue = GameObjectType.FLOOR.name();
                } else if (identified.equals(IWallObject.class.getCanonicalName())) {
                    enumValue = GameObjectType.WALL.name();
                }

                insnList.add(new FieldInsnNode(
                        Opcodes.GETSTATIC,
                        GameObjectType.class.getCanonicalName().replaceAll("\\.", "/"),
                        enumValue,
                        field.desc
                ));

                insnList.add(new FieldInsnNode(Opcodes.PUTFIELD, clazz.name, field.name, field.desc));

                AbstractInsnNode returnInsn = null;
                for (AbstractInsnNode ain : mn.instructions.toArray()) {
                    if (ain.getOpcode() == Opcodes.RETURN) {
                        returnInsn = ain;
                        break;
                    }
                }
                if (returnInsn != null) {
                    mn.instructions.insertBefore(returnInsn, insnList);
                    mn.visitEnd();
                } else {
                    throw new RuntimeException("Unable to find instruction to inject before");
                }
            }
        }
    }

    private FieldNode injectFieldAndGetter(ClassNode classNode) {
        FieldNode field = new FieldNode(Opcodes.ACC_PUBLIC, "type",
                "L" + GameObjectType.class.getCanonicalName().replaceAll("\\.", "/") + ";", "L" + GameObjectType.class.getCanonicalName().replaceAll("\\.", "/") + ";", null);
        classNode.fields.add(field);

        MethodNode getter = new MethodNode(Opcodes.ACC_PUBLIC, "getType",
                "()L" + GameObjectType.class.getCanonicalName().replaceAll("\\.", "/") + ";", null, null);
        getter.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        getter.instructions.add(new FieldInsnNode(Opcodes.GETFIELD, classNode.name,
                field.name, field.desc));
        getter.instructions.add(new InsnNode(Opcodes.ARETURN));
        getter.visitEnd();
        getter.visitMaxs(1, 1);
        classNode.methods.add(getter);

        return field;
    }
}
