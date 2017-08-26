package com.kit.game.transform.impl;

import com.kit.game.transform.Extender;
import com.kit.game.transform.model.ClassDefinition;
import com.kit.game.transform.Extender;
import com.kit.game.transform.model.ClassDefinition;
import org.objectweb.asm.tree.*;

import java.util.List;
import java.util.Map;

public class GCRemoverExtender implements Extender {

    @Override
    public boolean canRun(ClassNode clazz) {
        return true;
    }

    @Override
    public void apply(Map<String, ClassDefinition> classes, ClassNode clazz) {
        for (final MethodNode mn : (List<MethodNode>) clazz.methods) {
            for (AbstractInsnNode ain : mn.instructions.toArray()) {
                if (ain instanceof MethodInsnNode) {
                    MethodInsnNode gc = (MethodInsnNode) ain;
                    if (gc.name.equals("gc") && gc.owner.equals(System.class.getCanonicalName().replaceAll("\\.", "/"))) {
                        mn.instructions.remove(gc);
                    }
                }
            }
            mn.visitMaxs(1, 1);
            mn.visitEnd();
        }
    }
}
