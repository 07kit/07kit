package com.kit.game.transform.impl;

import com.kit.game.engine.extension.KeyboardExtension;
import com.kit.game.engine.extension.KeyboardExtension;
import com.kit.game.transform.Extender;
import com.kit.game.transform.model.ClassDefinition;
import com.kit.game.engine.extension.KeyboardExtension;
import com.kit.game.transform.Extender;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

/**
 */
public class KeyboardExtender implements Extender {
    private static String KEYBOARD_EXT = KeyboardExtension.class
            .getCanonicalName().replaceAll("\\.", "/");
    private static List<String> redirects = newArrayList(
            "keyPressed", "keyReleased", "keyTyped"
    );

    @Override
    public boolean canRun(ClassNode clazz) {
        return clazz.interfaces.contains("java/awt/event/KeyListener");
    }

    @Override
    public void apply(Map<String, ClassDefinition> definitions, ClassNode classGen) {
        classGen.interfaces.remove("java/awt/event/KeyListener");
        classGen.superName = KEYBOARD_EXT;

        for (MethodNode method : (List<MethodNode>) classGen.methods) {
            if (redirects.contains(method.name)) {
                method.name = "_" + method.name;
            } else if (method.name.equals("<init>")) {
                InsnList insns = method.instructions;
                for (int i = 0; i < insns.size(); i++) {
                    AbstractInsnNode insn = insns.get(i);
                    if (insn.getOpcode() == Opcodes.INVOKESPECIAL) {
                        MethodInsnNode invoke = (MethodInsnNode) insn;
                        if (invoke.owner.equals("java/lang/Object")) {
                            invoke.owner = KEYBOARD_EXT;
                        }
                    }
                }
            }
        }
    }

}
