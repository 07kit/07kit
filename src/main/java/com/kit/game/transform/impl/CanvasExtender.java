package com.kit.game.transform.impl;

import com.kit.game.engine.extension.CanvasExtension;
import com.kit.game.engine.extension.CanvasExtension;
import com.kit.game.transform.Extender;
import com.kit.game.transform.model.ClassDefinition;
import com.kit.game.engine.extension.CanvasExtension;
import com.kit.game.transform.Extender;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;
import java.util.Map;

/**
 * A class that extends the canvas superclass used by
 * the client with our custom one.
 *
 */
public class CanvasExtender implements Extender {
    private static String CANVAS_EXT = CanvasExtension.class
            .getCanonicalName().replaceAll("\\.", "/");

    @Override
    public boolean canRun(ClassNode classNode) {
        return classNode.superName.equals("java/awt/Canvas");
    }

    @Override
    public void apply(Map<String, ClassDefinition> definitions, ClassNode node) {
        node.superName = CANVAS_EXT;
        for (final MethodNode mn : (List<MethodNode>) node.methods) {
            if (!mn.name.equals("<init>")) {
                continue;
            }
            for (final AbstractInsnNode ain : mn.instructions.toArray()) {
                if (ain.getOpcode() == Opcodes.INVOKESPECIAL
                        && ((MethodInsnNode) ain).owner.equals("java/awt/Canvas")) {
                    ((MethodInsnNode) ain).owner = CANVAS_EXT;
                }
            }
        }
    }

}
