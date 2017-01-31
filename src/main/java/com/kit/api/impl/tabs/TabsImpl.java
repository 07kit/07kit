package com.kit.api.impl.tabs;


import com.kit.api.MethodContext;
import com.kit.api.Tabs;
import com.kit.api.wrappers.Tab;
import com.kit.api.wrappers.Widget;
import com.kit.api.MethodContext;
import com.kit.api.Tabs;
import com.kit.api.wrappers.Tab;
import com.kit.api.wrappers.Widget;
import com.kit.api.MethodContext;
import com.kit.api.Tabs;
import com.kit.api.wrappers.Widget;

import java.util.HashMap;
import java.util.Map;

/**
 * @author const_
 */
public class TabsImpl implements Tabs {

    private final MethodContext ctx;
    private static final int GROUP_FIXED_ID = 548;
    private static final int GROUP_RESIZABLE_ID = 161;


    public TabsImpl(MethodContext ctx) {
        this.ctx = ctx;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOpen(Tab tab) {
        return getCurrent() == tab;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Tab getCurrent() {
        return getOpen();
    }

    private Widget[] getAll() {
        Widget[] tabs = new Widget[Tab.values().length];
        for (int i = 0; i < tabs.length; i++) {
            int id = Tab.values()[i].getId();
            tabs[i] = ctx.widgets.find(ctx.inResizableMode() ? GROUP_RESIZABLE_ID : GROUP_FIXED_ID,
                    ctx.inResizableMode()  && id > 40 ? id - 1 : id);
        }
        return tabs;
    }

    private Tab getOpen() {
        Widget[] all = getAll();
        int targetTextureID = getOpenTextureID(all);
        if (targetTextureID != -1) {
            for (Widget widget : all) {
                if (widget.getSpriteId() == targetTextureID) {
                    return Tab.byId(ctx.inResizableMode() ? widget.getId() + 1 : widget.getId());
                }
            }
        }
        return null;
    }

    private int getOpenTextureID(Widget[] widgets) {
        int[] textureIDs = new int[widgets.length];
        Map<Integer, Integer> frequency = new HashMap<Integer, Integer>();
        for (int i = 0; i < widgets.length; i++) {
            if (widgets[i] == null) {
                continue;
            }
            textureIDs[i] = widgets[i].getSpriteId();
            if (frequency.containsKey(textureIDs[i])) {
                int freq = frequency.get(textureIDs[i]);
                frequency.remove(textureIDs[i]);
                frequency.put(textureIDs[i], freq + 1);
            } else {
                frequency.put(textureIDs[i], 1);
            }
        }
        Integer result = extractSingleton(frequency);
        if (result == null) {
            return -1;
        }
        return result;
    }

    private <T> T extractSingleton(Map<T, Integer> items) {
        for (Map.Entry<T, Integer> entries : items.entrySet()) {
            if (entries.getValue() == 1) {
                return entries.getKey();
            }
        }
        return null;
    }
}
