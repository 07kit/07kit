package com.kit.game.transform.impl;

import com.kit.api.event.Events;
import com.kit.api.event.PlayerRegionChangeEvent;
import com.kit.core.Session;
import com.kit.game.engine.IBitBuffer;
import com.kit.game.transform.Extender;
import com.kit.game.transform.model.ClassDefinition;
import com.kit.game.transform.model.MethodDefinition;
import org.apache.log4j.Logger;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.objectweb.asm.Opcodes.INVOKESTATIC;


public class PlayerRegionChangeExtender implements Extender {

    private static Logger logger = Logger.getLogger(MessageExtender.class);
    private MethodDefinition methodDef;

    public PlayerRegionChangeExtender(Map<String, ClassDefinition> definitions) {
        classes:
        for (ClassDefinition def : definitions.values()) {
            for (MethodDefinition methodDefinition : def.methods) {
                if (methodDefinition.name.equals("onPlayerRegionChange")) {
                    methodDef = methodDefinition;
                    break classes;
                }
            }
        }
        if (methodDef == null) {
            logger.error("onPlayerRegionUpdate is null");
        }
    }

    @Override
    public boolean canRun(ClassNode clazz) {
        return methodDef != null &&
                methodDef.owner.equals(clazz.name);
    }

    @Override
    public void apply(Map<String, ClassDefinition> classes, ClassNode clazz) {
        for (MethodNode m : (List<MethodNode>) clazz.methods) {
            if (!m.name.equals(methodDef.actualName) ||
                    !m.desc.equals(methodDef.actualDesc)) {
                continue;
            }

            InsnList insnList = new InsnList();
            insnList.add(new MethodInsnNode(INVOKESTATIC, "client", "getEventBus", "()L" + Events.class.getCanonicalName().replaceAll("\\.", "/") + ";"));

            insnList.add(new IntInsnNode(Opcodes.ALOAD, 0));
            insnList.add(new IntInsnNode(Opcodes.ILOAD, 1));

                    insnList.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL,
                            Events.class.getName().replace(".", "/"),
                            "submitPlayerRegionChange",
                            "(L" + IBitBuffer.class.getName().replace(".", "/") + ";I)V"));
            m.instructions.insertBefore(m.instructions.getFirst(), insnList);

            m.visitEnd();
        }

    }
}