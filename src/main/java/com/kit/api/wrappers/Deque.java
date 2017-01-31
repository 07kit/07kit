package com.kit.api.wrappers;

import com.kit.game.engine.collection.INode;
import com.kit.game.engine.collection.IDeque;
import com.kit.game.engine.collection.INode;

import java.lang.ref.WeakReference;
import java.util.Iterator;

/**
 * @author tobiewarburton
 */
public final class Deque implements Iterator<INode> {
    private WeakReference<IDeque> deque;
    private WeakReference<INode> current;

    public Deque(IDeque deque) {
        this.deque = new WeakReference<>(deque);
        if (deque.getHead() != null) {
            this.current = new WeakReference<>(deque.getHead().getNext());
        } else {
            this.current = null;
        }
    }

    public INode current() {
        return current.get();
    }

    public IDeque deque() {
        return deque.get();
    }

    /**
     * Checks if there is another node
     *
     * @return <t>true if there is another node</t> otherwise false
     */
    @Override
    public boolean hasNext() {
        return current != null && current() != null && current().getNext() != null;
    }

    /**
     * Gets the next node
     *
     * @return the next node in the deque
     */
    @Override
    public INode next() {
        INode node = current();
        if (node == deque().getHead()) {
            current = null;
            return null;
        } else {
            current = new WeakReference<>(node.getNext());
            return node;
        }
    }

    @Override
    public void remove() {
        //not needed
    }

    /**
     * Gets the size of the deque
     *
     * @return the size of the deque
     */
    public int size() {
        int i = 0;
        INode head = deque().getHead();
        for (INode node = head.getNext(); !head.equals(node); ) {
            node = node.getNext();
            i++;
        }
        return i;
    }
}
