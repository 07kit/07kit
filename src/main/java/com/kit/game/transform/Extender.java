package com.kit.game.transform;

import com.kit.game.transform.model.ClassDefinition;
import com.kit.game.transform.model.ClassDefinition;
import org.objectweb.asm.tree.ClassNode;

import java.util.Map;

/**
 * An interface to an extender implementation.
 * Extenders are used for extending existing
 * behaviours by swapping the superclass.
 *
 */
public interface Extender {

    /**
     * Check to see if the extender can run on
     * the specified class.
     *
     * @param clazz class to test
     * @return true if runnable.
     */
    boolean canRun(ClassNode clazz);

    /**
     * Applies the extender
     *
     * @param clazz class to apply on
     */
    void apply(Map<String, ClassDefinition> classes, ClassNode clazz);
}
