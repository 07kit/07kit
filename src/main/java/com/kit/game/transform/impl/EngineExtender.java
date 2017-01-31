package com.kit.game.transform.impl;

import com.kit.game.engine.IClient;
import com.kit.game.transform.model.ClassDefinition;
import com.kit.game.transform.model.MethodDefinition;
import com.kit.game.engine.IClient;
import com.kit.game.transform.Extender;
import com.kit.game.transform.model.ClassDefinition;
import com.kit.game.transform.model.GetterDefinition;
import com.kit.game.transform.model.MethodDefinition;
import org.apache.log4j.Logger;
import com.kit.game.transform.Extender;
import com.kit.game.transform.model.GetterDefinition;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 */
public class EngineExtender implements Extender {

    private static Logger logger = Logger.getLogger(EngineExtender.class);

    @Override
    public boolean canRun(ClassNode clazz) {
        return true;
    }

    private String stripDesc(String desc) {
        return (desc.startsWith("L") ? desc.substring(1) : desc).replaceAll(";", "").replaceAll("\\[", "");
    }

    @Override
    public void apply(Map<String, ClassDefinition> definitions, ClassNode clazz) {
        if (!definitions.containsKey(clazz.name)) {
            return;
        }
        ClassDefinition definition = definitions.get(clazz.name);
        clazz.interfaces.add(definition.identifiedName.replaceAll("\\.", "/"));

        try {
            Class.forName(definition.identifiedName);
        } catch (ClassNotFoundException e) {
            logger.error("No interface class " + definition.identifiedName);
        }


        for (MethodDefinition methodDef : definitions.values().stream().
                map(cd -> cd.methods).flatMap(Collection::stream).filter(md -> md.injectInvoker
                && md.opcode == Opcodes.INVOKEVIRTUAL && md.owner.equals(clazz.name))
                .collect(Collectors.toList())) {
            MethodNode method = new MethodNode(Opcodes.ACC_PUBLIC, methodDef.name, methodDef.modifiedDesc,
                    null, null);

            if (methodDef.opcode == Opcodes.INVOKEVIRTUAL) {
                method.visitVarInsn(Opcodes.ALOAD, 0);
            }

            List<Integer> paramLoadOpcodes = methodDef.paramLoadOpcodes;
            for (int i = 0; i < paramLoadOpcodes.size(); i++) {
                int opcode = paramLoadOpcodes.get(i);
                String paramDesc = methodDef.paramDescs.get(i);
                String[] split = paramDesc.split("~");
                method.visitVarInsn(opcode, i + 1);
                if (!split[0].equals(split[1])) {
                    method.visitTypeInsn(Opcodes.CHECKCAST, stripDesc(split[0]));
                }
            }
            method.visitLdcInsn(methodDef.opaque != Integer.MAX_VALUE ? methodDef.opaque : 0);
            method.visitMethodInsn(methodDef.opcode, methodDef.owner, methodDef.actualName, methodDef.actualDesc);
            method.visitInsn(methodDef.actualDesc.endsWith("V") ? Opcodes.RETURN :
                    Type.getType(methodDef.actualDesc.substring(methodDef.actualDesc.indexOf(')') + 1)).getOpcode(Opcodes.IRETURN));
            method.visitMaxs(1, 1);
            method.visitEnd();
            clazz.methods.add(method);
        }

        if (definition.identifiedName.equals(IClient.class.getName())) {
            for (MethodDefinition methodDef : definitions.values().stream().
                    map(cd -> cd.methods).flatMap(Collection::stream).filter(md -> md.injectInvoker && md.opcode == Opcodes.INVOKESTATIC)
                    .collect(Collectors.toList())) {

                MethodNode method = new MethodNode(Opcodes.ACC_PUBLIC, methodDef.name,
                        methodDef.modifiedDesc, null, null);
                List<Integer> paramLoadOpcodes = methodDef.paramLoadOpcodes;
                for (int i = 0; i < paramLoadOpcodes.size(); i++) {
                    int opcode = paramLoadOpcodes.get(i);
                    String paramDesc = methodDef.paramDescs.get(i);
                    String[] split = paramDesc.split("~");
                    method.visitVarInsn(opcode, i + 1);
                    if (!split[0].equals(split[1])) {
                        method.visitTypeInsn(Opcodes.CHECKCAST, stripDesc(split[0]));
                    }
                }
                if (methodDef.opaque != Integer.MAX_VALUE) {
                    method.visitLdcInsn(methodDef.opaque);
                }
                method.visitMethodInsn(methodDef.opcode, methodDef.owner, methodDef.actualName, methodDef.actualDesc);
                method.visitInsn(methodDef.actualDesc.endsWith("V") ? Opcodes.RETURN :
                        Type.getType(methodDef.actualDesc.substring(methodDef.actualDesc.indexOf(')') + 1)).getOpcode(Opcodes.IRETURN));
                method.visitMaxs(1, 1);
                method.visitEnd();
                clazz.methods.add(method);
            }
        }
        for (GetterDefinition getter : definition.getters) {
            clazz.methods.add(createGetter(getter));
            if (getter.injectSetter) {
                clazz.methods.add(createSetter(getter));
            }
        }
        clazz.visitEnd();
    }

    public MethodNode createGetter(GetterDefinition getter) {
        MethodNode method = new MethodNode(Opcodes.ACC_PUBLIC, getter.name,
                "()" + getter.signature, null, null);
        if (getter.member) {
            method.visitVarInsn(Opcodes.ALOAD, 0);
            method.visitFieldInsn(Opcodes.GETFIELD, getter.fieldClass,
                    getter.fieldName, getter.actualSig);
        } else {
            method.visitFieldInsn(Opcodes.GETSTATIC, getter.fieldClass,
                    getter.fieldName, getter.actualSig);
        }

        if (getter.multiplier != 0) {
            method.visitLdcInsn((int) getter.multiplier);
            method.visitInsn(Opcodes.IMUL);
        }

        method.visitInsn(Type.getType(getter.actualSig).getOpcode(Opcodes.IRETURN));
        method.visitMaxs(1, 1);
        method.visitEnd();
        return method;
    }

    public static long modInverse(long multi) {
        return new BigInteger(String.valueOf(multi)).modInverse(new BigInteger(String.valueOf(4294967296L))).longValue();
    }

    public MethodNode createSetter(GetterDefinition getter) {
        MethodNode method = new MethodNode(Opcodes.ACC_PUBLIC, getter.name.replaceFirst("get", "set"),
                "(" + getter.signature + ")V", null, null);
        method.visitVarInsn(Opcodes.ALOAD, 0);
        method.visitVarInsn(Type.getType(getter.actualSig).getOpcode(Opcodes.ILOAD), 1);

        if (getter.actualSig.equals("I") && getter.multiplier != 0) {
            method.visitLdcInsn((int) modInverse(getter.multiplier));
            method.visitInsn(Opcodes.IMUL);
        }
        if (getter.member) {
            method.visitFieldInsn(Opcodes.PUTFIELD, getter.fieldClass,
                    getter.fieldName, getter.actualSig);
        } else {
            method.visitFieldInsn(Opcodes.PUTSTATIC, getter.fieldClass,
                    getter.fieldName, getter.actualSig);
        }
        method.visitInsn(Opcodes.RETURN);

        method.visitMaxs(1, 1);
        method.visitEnd();
        return method;
    }
}
