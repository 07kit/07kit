package com.kit.api.impl.tabs;


import com.kit.api.Prayers;
import com.kit.api.wrappers.Prayer;
import com.kit.api.wrappers.Skill;
import com.kit.api.MethodContext;
import com.kit.api.Prayers;
import com.kit.api.wrappers.Prayer;
import com.kit.api.wrappers.Skill;
import com.kit.api.wrappers.Tab;
import com.kit.api.wrappers.Widget;

import java.util.ArrayList;
import java.util.List;

/**
 * Prayers interaction implementation
 *
 * @author tommo
 */
public class PrayersImpl implements Prayers {

    private static final int GROUP_ID = 271;
    private static final int PRAYER_POINTS_WIDGET = 1;

    private final MethodContext ctx;


    public PrayersImpl(MethodContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public int getPrayerPoints() {
        Widget widget = ctx.widgets.find(GROUP_ID, PRAYER_POINTS_WIDGET);
        return Integer.parseInt(widget.getText().split("/")[0].trim());
    }

    @Override
    public List<Prayer> getActivatedPrayers() {
        List<Prayer> list = new ArrayList<Prayer>();
        for (Prayer prayer : Prayer.values()) {
            if (isActivated(prayer)) {
                list.add(prayer);
            }
        }
        return list;
    }

    @Override
    public boolean isActivated(Prayer prayer) {
        return ctx.settings.getWidgetSetting(prayer.getSettingsIndex()) == 1;
    }

    @Override
    public boolean canActivate(Prayer prayer) {
        return ctx.skills.getBaseLevel(Skill.PRAYER) >= prayer.getRequiredLevel();
    }

}
