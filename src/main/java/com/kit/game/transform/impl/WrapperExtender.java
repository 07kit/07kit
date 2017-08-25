package com.kit.game.transform.impl;

import com.kit.game.transform.Extender;
import com.kit.game.transform.model.ClassDefinition;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.List;
import java.util.Map;

public class WrapperExtender implements Extender {

    private Class<?> wrapperClass;
    private Class<?> interfaceClass;
    private Class<?> castClass;

    public WrapperExtender(Class<?> wrapperClass, Class<?> interfaceClass, Class<?> castClass) {
        this.wrapperClass = wrapperClass;
        this.interfaceClass = interfaceClass;
        this.castClass = castClass;
    }

    @Override
    public boolean canRun(ClassNode classNode) {
        return classNode.interfaces.contains(interfaceClass.getCanonicalName().replaceAll("\\.", "/"));
    }

    @Override
    public void apply(Map<String, ClassDefinition> classes, ClassNode clazz) {
        FieldNode wrapperField = injectFieldAndGetter(clazz);
        for (MethodNode mn : (List<MethodNode>) clazz.methods) {
            if (mn.name.equals("<init>")) {
                InsnList insnList = new InsnList();
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));

                insnList.add(new TypeInsnNode(Opcodes.NEW, getWrapperName()));
                insnList.add(new InsnNode(Opcodes.DUP));
                insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                insnList.add(new TypeInsnNode(Opcodes.CHECKCAST, getInterfaceName()));
                insnList.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, getWrapperName(), "<init>", "(L" + getInterfaceName() + ";)V"));


                insnList.add(new FieldInsnNode(Opcodes.PUTFIELD, clazz.name, wrapperField.name, wrapperField.desc));

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

    private String getInterfaceName() {
        return  (castClass != null ? castClass : interfaceClass).getCanonicalName().replaceAll("\\.", "/");
    }

    private String getWrapperName() {
        return wrapperClass.getCanonicalName().replaceAll("\\.", "/");
    }

    private FieldNode injectFieldAndGetter(ClassNode classNode) {
        FieldNode field = new FieldNode(Opcodes.ACC_PUBLIC, "wrapper",
                "L" + getWrapperName() + ";", "L" + getWrapperName() + ";", null);
        classNode.fields.add(field);

        MethodNode getter = new MethodNode(Opcodes.ACC_PUBLIC, "getWrapper",
                "()L" + getWrapperName() + ";", null, null);
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
