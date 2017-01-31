package com.kit.game.transform.impl;

import com.kit.game.transform.model.ClassDefinition;
import com.kit.game.transform.Extender;
import com.kit.game.transform.model.ClassDefinition;
import com.kit.game.transform.Extender;
import com.kit.game.transform.model.ClassDefinition;
import org.objectweb.asm.tree.*;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 */
public class UIDExtender implements Extender {
    private final Random random = new Random();

    @Override
    public boolean canRun(ClassNode clazz) {
        return true;
    }

    @Override
    public void apply(Map<String, ClassDefinition> classes, ClassNode clazz) {
        for (MethodNode method : (List<MethodNode>) clazz.methods) {
            InsnList instructions = method.instructions;
            for (int i = 0; i < instructions.size(); i++) {
                AbstractInsnNode node = instructions.get(i);
                if (node instanceof LdcInsnNode) {
                    LdcInsnNode ldc = (LdcInsnNode) node;
                    if (ldc.cst.equals("random.dat")) {
                        String fileName = "07kit/uids/hijacked_" + random.nextInt(Integer.MAX_VALUE) + ".dat";
                        while (new File(System.getProperty("user.home") + "/" + fileName).exists()) {
                            fileName = "07kit/uids/hijacked_" + random.nextInt(Integer.MAX_VALUE) + ".dat";
                        }
                        ldc.cst = fileName;
                    }
                }
            }
        }
    }
}
