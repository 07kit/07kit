package com.kit.api.impl.game;


import com.kit.api.MethodContext;
import com.kit.api.Models;
import com.kit.api.wrappers.Model;
import com.kit.api.wrappers.Npc;
import com.kit.game.engine.cache.media.IModel;
import com.kit.game.engine.collection.ICache;
import com.kit.game.engine.collection.INode;
import com.kit.api.MethodContext;
import com.kit.api.wrappers.Model;

/**
 * @author : const_
 */
public class ModelsImpl implements Models {

    private final MethodContext context;


    public ModelsImpl(MethodContext context) {
        this.context = context;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Model getNpcModel(Npc npc) {
        // TODO: wat
//        ICache cache = context.client.getNpcModelCache();
//        for (INode chain : cache.getBag().getCache()) {
//            for (INode in = chain.getNext(); in != null && !in.equals(chain); in = in.getNext()) {
//                if (in.getUid() == npc.getId()) {
//                    return new Model(context, (IModel) in,
//                            npc.getLocalX(), npc.getLocalY(), npc.getOrientation());
//                }
//            }
//        }
        return null;
    }
}
