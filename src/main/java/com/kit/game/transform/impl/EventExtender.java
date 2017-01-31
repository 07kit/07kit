package com.kit.game.transform.impl;

import com.kit.game.engine.extension.EventExtension;
import com.kit.game.transform.Extender;
import com.kit.game.transform.model.ClassDefinition;
import com.kit.game.transform.model.GetterDefinition;
import org.apache.log4j.Logger;
import com.kit.game.engine.extension.EventExtension;
import com.kit.game.transform.Extender;
import com.kit.game.transform.model.ClassDefinition;
import com.kit.game.transform.model.GetterDefinition;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.awt.event.MouseWheelEvent;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

/**
 */
public class EventExtender implements Extender {

    private final Logger logger = Logger.getLogger(getClass());

    private static String MOUSE_EXT = EventExtension.class
            .getCanonicalName().replaceAll("\\.", "/");
    private static List<String> redirects = newArrayList(
            "mouseEntered", "mouseExited", "mouseClicked",
            "mousePressed", "mouseReleased", "mouseMoved",
            "mouseDragged", "focusGained", "focusLost"
    );

    private GetterDefinition mouseDef;

    public EventExtender(Map<String, ClassDefinition> definitions) {
        mouseDef = definitions.get("client").getters.stream()
                .filter(g -> g.name.equals("getMouse"))
                .findFirst().orElseGet(null);

        if (mouseDef == null) {
            logger.error("getMouse is null");
        }
    }

    @Override
    public boolean canRun(ClassNode clazz) {
        return clazz.interfaces.contains("java/awt/event/MouseListener") ||
                clazz.interfaces.contains("java/awt/event/MouseWheelListener");
    }

    @Override
    public void apply(Map<String, ClassDefinition> definitions, ClassNode classGen) {
        if (classGen.interfaces.contains("java/awt/event/MouseWheelListener")) {
            for (MethodNode method : (List<MethodNode>) classGen.methods) {
                if (method.name.equals("mouseWheelMoved")) {
                    InsnList list = new InsnList();
                    list.add(new FieldInsnNode(Opcodes.GETSTATIC, mouseDef.fieldClass, mouseDef.fieldName, mouseDef.actualSig));
                    list.add(new TypeInsnNode(Opcodes.CHECKCAST, MOUSE_EXT));
                    list.add(new VarInsnNode(Opcodes.ALOAD, 1));
                    list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, MOUSE_EXT, "canMouseWheelMove","" +
                            "(L" + MouseWheelEvent.class.getCanonicalName().replaceAll("\\.", "/") + ";)Z"));
                    LabelNode label = new LabelNode();
                    list.add(new JumpInsnNode(Opcodes.IFNE, label));
                    list.add(new InsnNode(Opcodes.RETURN));
                    list.add(label);

                    method.instructions.insertBefore(method.instructions.getFirst(), list);
                }
            }
            return;
        }

        classGen.interfaces.remove("java/awt/event/FocusListener");
        classGen.interfaces.remove("java/awt/event/MouseListener");
        classGen.interfaces.remove("java/awt/event/MouseMotionListener");
        classGen.superName = MOUSE_EXT;

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
                            invoke.owner = MOUSE_EXT;
                        }
                    }
                }
            }
        }
    }

}
