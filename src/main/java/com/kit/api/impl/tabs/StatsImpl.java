package com.kit.api.impl.tabs;

import com.kit.api.MethodContext;
import com.kit.api.wrappers.Skill;
import com.kit.api.wrappers.Tab;
import com.kit.api.wrappers.Widget;
import com.kit.api.MethodContext;
import com.kit.api.wrappers.Skill;

/**
 */
public class StatsImpl {
    private final MethodContext ctx;

    public StatsImpl(MethodContext ctx) {
        this.ctx = ctx;
    }

    private int determineWidgetID(Skill skill) {
        switch (skill) {

            case ATTACK:
                return 48;

            case HITPOINTS:
                return 49;

            case MINING:
                return 50;

            case STRENGTH:
                return 51;

            case AGILITY:
                return 52;

            case SMITHING:
                return 53;

            case DEFENCE:
                return 54;

            case HERBLORE:
                return 55;

            case FISHING:
                return 55;
        }
        return -1;
    }


}
