package com.kit.game.transform.impl;

import com.kit.game.transform.model.MethodDefinition;
import com.kit.api.event.Events;
import com.kit.api.event.MessageEvent;
import com.kit.game.transform.Extender;
import com.kit.game.transform.model.ClassDefinition;
import com.kit.game.transform.model.MethodDefinition;
import org.apache.log4j.Logger;
import com.kit.game.transform.Extender;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.List;
import java.util.Map;

import static org.objectweb.asm.Opcodes.*;


/**
 * @author : const_
 */
public class MessageExtender implements Extender {
    private static Logger logger = Logger.getLogger(MessageExtender.class);
    private MethodDefinition messageReceiveDef;

    public MessageExtender(Map<String, ClassDefinition> definitions) {
        classes:
        for (ClassDefinition def : definitions.values()) {
            for (MethodDefinition methodDefinition : def.methods) {
                if (methodDefinition.name.equals("onMessageReceived")) {
                    messageReceiveDef = methodDefinition;
                    break classes;
                }
            }
        }
        if (messageReceiveDef == null) {
            logger.error("onMessageReceived is null");
        }
    }

    @Override
    public boolean canRun(ClassNode clazz) {
        return messageReceiveDef != null &&
                messageReceiveDef.owner.equals(clazz.name);
    }

    @Override
    public void apply(Map<String, ClassDefinition> definitions, ClassNode clazz) {
        for (MethodNode method : (List<MethodNode>) clazz.methods) {
            if (method.name.equals(messageReceiveDef.actualName) && method.desc.equals(messageReceiveDef.actualDesc)) {
                InsnList current = method.instructions;
                InsnList inject = new InsnList();

                inject.add(new MethodInsnNode(INVOKESTATIC, "client", "getEventBus", "()L" + Events.class.getCanonicalName().replaceAll("\\.", "/") + ";"));
                inject.add(new TypeInsnNode(NEW, MessageEvent.class.getCanonicalName().replaceAll("\\.", "/")));
                VarInsnNode integerInsn = null;
                InsnList stringInsns = new InsnList();
                List<Integer> paramLoadOpcodes = messageReceiveDef.paramLoadOpcodes;
                for (int i = 0; i < paramLoadOpcodes.size(); i++) {
                    int paramLoadOpcode = paramLoadOpcodes.get(i);
                    if (paramLoadOpcode == Opcodes.ILOAD && integerInsn == null) {
                        integerInsn = new VarInsnNode(paramLoadOpcode, i);
                    } else if (paramLoadOpcode == Opcodes.ALOAD && stringInsns.size() < 3) {
                        stringInsns.add(new VarInsnNode(paramLoadOpcode, i));
                    }
                }

                inject.add(new InsnNode(Opcodes.DUP));
                inject.add(stringInsns);
                inject.add(integerInsn);
                inject.add(new MethodInsnNode(INVOKESPECIAL, MessageEvent.class.getCanonicalName().replaceAll("\\.", "/"), "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)V"));
                inject.add(new MethodInsnNode(INVOKEVIRTUAL, Events.class.getCanonicalName().replaceAll("\\.", "/"), "submitMessageEvent", "(L" + MessageEvent.class.getCanonicalName().replaceAll("\\.", "/") + ";)V"));
                current.insertBefore(current.getFirst(), inject);
                method.visitEnd();
            }
        }
    }
}
