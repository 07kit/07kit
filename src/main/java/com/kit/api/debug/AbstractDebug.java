package com.kit.api.debug;

import com.kit.api.MethodContext;
import com.kit.core.Session;
import com.kit.api.MethodContext;

import java.lang.reflect.Method;
import java.util.logging.Level;

/**
 * Base class for debuggers.
 *
 */
public abstract class AbstractDebug {

    private boolean enabled;

    public AbstractDebug() {
    }

    public MethodContext ctx() {
        return Session.get();
    }


    public abstract String getName();

    public abstract String getShortcode();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        if (enabled) {
            this.enabled = true;
            Session.get().getEventBus().register(this);
        } else {
            this.enabled = false;
            Session.get().getEventBus().deregister(this);
        }
    }

}
