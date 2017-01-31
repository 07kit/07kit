package com.kit.game.engine.combat;

import com.kit.game.engine.collection.INode;
import com.kit.game.engine.collection.INode;
import com.kit.game.engine.collection.INode;

public interface ICombatInfoNode extends Iterable<INode> {

	INode getNext();

	INode getPrevious();

}
