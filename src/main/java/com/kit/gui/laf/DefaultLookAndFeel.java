package com.kit.gui.laf;

import javax.swing.*;
import javax.swing.plaf.basic.BasicLookAndFeel;

/**
 */
public class DefaultLookAndFeel extends BasicLookAndFeel {
    @Override
    public String getName() {
        return "07Kit";
    }

    @Override
    public String getID() {
        return getName();
    }

    @Override
    public String getDescription() {
        return "07Kit look and feel";
    }

    @Override
    public boolean isNativeLookAndFeel() {
        return false;
    }

    @Override
    public boolean isSupportedLookAndFeel() {
        return true;
    }


    @Override
    protected void initClassDefaults(final UIDefaults defaults) {
        super.initClassDefaults(defaults);
        final String PREFIX = "com.kit.gui.laf.component.";
        defaults.keySet().stream().filter(o -> o instanceof String).forEach(o -> {
            final String key = (String) o;
            if (key.endsWith("UI")) {
                defaults.put(key, PREFIX + key);
            }
        });

    }

    @Override
    protected void initComponentDefaults(final UIDefaults table) {
        super.initComponentDefaults(table);
    }
}
