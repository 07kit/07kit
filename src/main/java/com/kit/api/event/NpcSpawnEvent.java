package com.kit.api.event;


import com.kit.game.engine.renderable.entity.INpc;
import com.kit.game.engine.renderable.entity.INpc;
import com.kit.game.engine.renderable.entity.INpc;

/**
 */
public class NpcSpawnEvent {
    private INpc npc;

    public NpcSpawnEvent(INpc npc) {
        this.npc = npc;
    }

    public INpc getNpc() {
        return npc;
    }

}
