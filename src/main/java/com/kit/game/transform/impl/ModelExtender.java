package com.kit.game.transform.impl;

import com.kit.api.wrappers.Model;
import com.kit.game.engine.cache.media.IModel;
import com.kit.game.engine.renderable.IRenderable;
import com.kit.game.transform.Extender;
import com.kit.game.transform.model.ClassDefinition;
import com.kit.api.wrappers.Model;
import com.kit.game.engine.cache.media.IModel;
import com.kit.game.engine.renderable.IRenderable;
import com.kit.game.transform.Extender;
import com.kit.game.transform.model.ClassDefinition;
import org.apache.log4j.Logger;
import com.kit.game.transform.Extender;
import com.kit.game.transform.model.ClassDefinition;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.List;
import java.util.Map;

/**
 * * @author const_
 */
public class ModelExtender implements Extender {
    private final Logger logger = Logger.getLogger(ModelExtender.class);

    @Override
    public boolean canRun(ClassNode classNode) {
        return classNode.interfaces.contains(IRenderable.class.getCanonicalName().replaceAll("\\.", "/"));
    }

    @Override
    public void apply(Map<String, ClassDefinition> definitions, ClassNode node) {
        FieldNode model = new FieldNode(Opcodes.ACC_PUBLIC, "model", "L" + Model.class.getCanonicalName().replaceAll("\\.", "/")+ ";", null, null);
        node.fields.add(model);
        MethodNode getter = new MethodNode(Opcodes.ACC_PUBLIC, "getModel", "()" + model.desc, null, null);
        getter.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
        getter.instructions.add(new FieldInsnNode(Opcodes.GETFIELD, node.name, model.name, model.desc));
        getter.instructions.add(new InsnNode(Opcodes.ARETURN));
        getter.visitEnd();
        getter.visitMaxs(1, 1);
        node.methods.add(getter);

        methods:
        for (final MethodNode mn : (List<MethodNode>) node.methods) {
            if ((mn.access & Opcodes.ACC_STATIC) != 0 || !mn.desc.endsWith("V")) {
                continue;
            }
            InsnList list = mn.instructions;
            for (int i = 0; i < list.size(); i++) {
                AbstractInsnNode curr = list.get(i);
                if (curr.getOpcode() == Opcodes.INVOKEVIRTUAL && i + 1 < list.size() &&
                        curr.getNext().getOpcode() == Opcodes.ASTORE) {
                    MethodInsnNode min = (MethodInsnNode) curr;
                    if (!min.desc.endsWith(")V")) {
                        InsnList insns = new InsnList();
                        VarInsnNode astore = (VarInsnNode) curr.getNext();
                        insns.add(new VarInsnNode(Opcodes.ALOAD, 0));
                        insns.add(new TypeInsnNode(Opcodes.NEW, Model.class.getCanonicalName().replaceAll("\\.", "/")));
                        insns.add(new InsnNode(Opcodes.DUP));
                        insns.add(new VarInsnNode(Opcodes.ALOAD, astore.var));
                        insns.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, Model.class.getCanonicalName().replaceAll("\\.", "/"), "<init>",
                                "(L" + IModel.class.getCanonicalName().replaceAll("\\.", "/") + ";)V"));
                        insns.add(new FieldInsnNode(Opcodes.PUTFIELD, node.name, "model", "L" + Model.class.getCanonicalName().replaceAll("\\.", "/") + ";"));
                        AbstractInsnNode insn = null;
                        for (int x = i; x < list.size(); x++) {
                            if (list.get(x).getOpcode() == Opcodes.ALOAD && ((VarInsnNode) list.get(x)).var == astore.var) {
                                insn = list.get(x);
                            }
                        }
                        list.insert(insn, insns);
                        mn.visitEnd();
                        mn.visitMaxs(1, 1);
                        continue methods;
                    }
                }
            }
        }
    }
}
