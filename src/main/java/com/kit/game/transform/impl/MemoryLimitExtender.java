package com.kit.game.transform.impl;

import com.kit.game.transform.Extender;
import com.kit.game.transform.model.ClassDefinition;
import com.kit.game.transform.Extender;
import com.kit.game.transform.model.ClassDefinition;
import org.objectweb.asm.tree.*;

import java.util.List;
import java.util.Map;

public class MemoryLimitExtender implements Extender {

    @Override
    public boolean canRun(ClassNode clazz) {
        return true;
    }

    @Override
    public void apply(Map<String, ClassDefinition> classes, ClassNode clazz) {
        for (final MethodNode mn : (List<MethodNode>) clazz.methods) {
            for (AbstractInsnNode ain : mn.instructions.toArray()) {
                if (ain instanceof MethodInsnNode) {
                    MethodInsnNode maxMemory = (MethodInsnNode) ain;
                    if (maxMemory.name.equals("maxMemory")) {
                        AbstractInsnNode runtime = maxMemory.getPrevious();
                        AbstractInsnNode ldc = maxMemory.getNext();
                        mn.instructions.remove(runtime);
                        mn.instructions.remove(maxMemory);
                        mn.instructions.insertBefore(ldc, new LdcInsnNode((long)(Runtime.getRuntime().maxMemory() / 1.25)));
                    }
                }
            }
        }
    }
}
