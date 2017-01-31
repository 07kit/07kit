package com.kit.api.event;

import com.kit.api.plugin.Option;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Option;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Option;
import com.kit.api.plugin.Plugin;

/**
 */
public class OptionChangedEvent {
    private Class<? extends Plugin> plugin;
    private Option option;
    private Object value;

    public OptionChangedEvent(Class<? extends Plugin> plugin, Option option, Object value) {
        this.plugin = plugin;
        this.option = option;
        this.value = value;
    }

    public OptionChangedEvent() {

    }

    public Class<? extends Plugin> getPlugin() {
        return plugin;
    }

    public Option getOption() {
        return option;
    }

    public Object getValue() {
        return value;
    }
}
