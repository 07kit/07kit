package com.kit.api.wrappers;

import com.kit.game.engine.scene.tile.IGroundLayer;
import com.kit.game.engine.renderable.IRenderable;
import com.kit.game.engine.scene.tile.IGroundLayer;

import java.lang.ref.WeakReference;


/**
 * @author : const_
 */
public class GroundLayer {

    private WeakReference<IGroundLayer> wrapped;

    public GroundLayer(IGroundLayer wrapped) {
        this.wrapped = new WeakReference<>(wrapped);
    }

    public IGroundLayer unwrap() {
        return wrapped.get();
    }

    public IRenderable getRenderable(int layer) {
        IRenderable renderable = null;
        switch (layer) {
            case 1:
                renderable = unwrap().getRenderable1();
                break;
            case 2:
                renderable = unwrap().getRenderable2();
                break;
            case 3:
                renderable = unwrap().getRenderable3();
                break;
        }
        return renderable;
    }
}
