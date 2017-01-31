package com.kit.core.control;

import com.kit.api.debug.impl.*;
import com.kit.api.debug.AbstractDebug;
import com.kit.core.Session;
import com.kit.api.debug.AbstractDebug;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * A control class for managing debugging instances.
 *
 */
public final class DebugManager {
    private final List<AbstractDebug> debugs = newArrayList();
    private final Session session;

    public DebugManager(Session session) {
        this.session = session;
        this.initDefaults();
    }

    private void initDefaults() {
        debugs.add(new MouseDebug());
        debugs.add(new CameraDebug());
        debugs.add(new PositionDebug());
        debugs.add(new InventoryDebug());
        debugs.add(new LoginDebug());
        debugs.add(new PlayerDebug());
        debugs.add(new NpcDebug());
        debugs.add(new WallObjectDebug());
        debugs.add(new FloorObjectDebug());
        debugs.add(new InteractableObjectDebug());
        debugs.add(new BoundaryObjectDebug());
        debugs.add(new LootDebug());
        debugs.add(new MenuDebug());
        debugs.add(new WidgetDebug());
    }

    public boolean isEnabled(String code) {
        for (AbstractDebug debug : debugs) {
            if (debug.isEnabled() && debug.getShortcode().equals(code)) {
                return true;
            }
        }
        return false;
    }

    public void toggle(String code, boolean enable) {
        for (AbstractDebug debug : debugs) {
            if (debug.getShortcode().equals(code)) {
                debug.setEnabled(enable);
                break;
            }
        }
    }

    public List<AbstractDebug> getDebugs() {
        return debugs;
    }

}
