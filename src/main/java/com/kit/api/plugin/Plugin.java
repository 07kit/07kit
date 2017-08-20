package com.kit.api.plugin;


import com.kit.api.MethodContext;
import com.kit.api.event.EventHandler;
import com.kit.api.event.OptionChangedEvent;
import com.kit.api.plugin.PersistentOptions.OptionProxy;
import com.kit.api.util.ReflectionUtils;
import com.kit.core.control.PluginManager;
import com.kit.api.MethodContext;
import com.kit.api.event.EventHandler;
import com.kit.api.util.ReflectionUtils;
import com.kit.core.control.PluginManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.List;

/**
 * Plugin base class
 */
public abstract class Plugin extends MethodContext {

    private static final String ENABLED_LBL = "Enable this plugin";

    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final PersistentOptions persistentOptions = new PersistentOptions(getName().replaceAll(" ", "_").toLowerCase() + "_options");
    private final List<SchedulableRunnable> runnables = new ArrayList<>();
    private final Map<Field, OptionProxy> proxyMapping = new HashMap<>();
    private final Map<Option, Field> optionMapping = new HashMap<>();
    private final Map<String, Option> options = new HashMap<>();
    private final PluginManager manager;

    @Option(label = ENABLED_LBL, type = Option.Type.TOGGLE, value = "true")
    private boolean enabled;
    @Option(label = "OverlayX", type = Option.Type.HIDDEN, value = "-1")
    public int overlayX;
    @Option(label = "OverlayY", type = Option.Type.HIDDEN, value = "-1")
    public int overlayY;

    public Plugin(PluginManager manager) {
        this.manager = manager;
        extractOptions();
        extractRunnables();
        logger.info("Loading persisted options.");
        persistentOptions.load();

        outer:
        for (Map.Entry<String, Option> labelOptionEntry : options.entrySet()) {
            Field field = optionMapping.get(labelOptionEntry.getValue());
            if (labelOptionEntry.getValue().value() == null ||
                    labelOptionEntry.getValue().value().length() == 0 ||
                    field == null) {
                continue;
            }
            for (OptionProxy proxy : persistentOptions.getAll()) {
                if (proxy.label.equals(labelOptionEntry.getKey())) {
                    continue outer;
                }
            }

            Option option = labelOptionEntry.getValue();
            OptionProxy proxy = new OptionProxy();
            proxy.field = field.getName();
            proxy.type = option.type();
            proxy.label = option.label();
            if (option.label().equals(ENABLED_LBL)) {
                proxy.value = String.valueOf(isEnabledByDefault());
            } else {
                proxy.value = labelOptionEntry.getValue().value();
            }
            proxyMapping.put(field, proxy);
            persistentOptions.add(proxy);
        }
        persistentOptions.save();

        for (OptionProxy proxy : persistentOptions.getAll()) {
            try {
                Field field = getField(proxy.field);
                if (field != null) {
                    field.setAccessible(true);
                    field.set(this, ReflectionUtils.stringToValue(field, proxy.value));
                    proxyMapping.put(field, proxy);
                } else {
                    persistentOptions.remove(proxy);
                    persistentOptions.save();
                }
            } catch (IllegalAccessException e) {
                logger.error(String.format("Failed to assign option on %s", proxy.field), e);
            }
        }
        onOptionsChanged();
    }

    public boolean isEnabledByDefault() {
        return true;
    }

    private Field getField(String name) {
        try {
            return getClass().getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            logger.debug("Couldn't find field " + name + " in " + getClass().getSimpleName(), e);
        }

        logger.debug("Trying to obtain field " + name + " from super class.");
        try {
            return Plugin.class.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            logger.warn("Couldn't find field " + name + " in " + Plugin.class.getSimpleName(), e);
            return null;
        }
    }

    private List<Field> getFields() {
        List<Field> fields = new ArrayList<>();
        fields.addAll(Arrays.asList(Plugin.class.getDeclaredFields()));
        fields.addAll(Arrays.asList(getClass().getDeclaredFields()));
        return fields;
    }

    private void extractRunnables() {
        logger.info("Preparing schedulable runnables.");
        for (Method method : getClass().getMethods()) {
            Schedule schedule = method.getAnnotation(Schedule.class);
            if (schedule != null) {
                runnables.add(new SchedulableRunnable(method, schedule.value()));
            }
        }
        logger.info(String.format("Prepared schedulable runnables (%d).", runnables.size()));
    }

    private void extractOptions() {
        logger.info("Extracting plugin options.");
        for (Field field : getFields()) {
            Option option = field.getAnnotation(Option.class);
            if (option != null) {
                try {
                    field.setAccessible(true);
                    field.set(this, ReflectionUtils.stringToValue(field, option.value()));
                } catch (IllegalAccessException e) {
                    logger.error(String.format("Failed to assign option on %s", field.getName()), e);
                }
                optionMapping.put(option, field);
                options.put(option.label(), option);
            }
        }
        logger.info(String.format("Extracted %d plugin options.", options.size()));
    }

    public abstract String getName();

    public String getGroup() {
        return "Generic";
    }

    public abstract void start();

    public abstract void stop();

    public List<SchedulableRunnable> getRunnables() {
        return runnables;
    }

    public List<Option> getOptions() {
        return new ArrayList<>(options.values());
    }

    public Option getOption(String label) {
        return options.get(label);
    }

    public Object getOptionValue(Option option) {
        try {
            return optionMapping.get(option).get(this);
        } catch (IllegalAccessException e) {
            logger.error("Failed to access option mapping.");
            return null;
        }
    }

    @EventHandler
    public void handleOptionChanged(OptionChangedEvent event) {
        if (!event.getPlugin().equals(getClass())) {
            return;
        }

        Field affectedField = optionMapping.get(event.getOption());
        try {
            affectedField.set(this, event.getValue());

            OptionProxy proxy = proxyMapping.get(affectedField);
            if (proxy != null) {
                proxy.value = event.getValue().toString();
                persistentOptions.save();
            } else {
                Option option = event.getOption();
                proxy = new OptionProxy();
                proxy.field = affectedField.getName();
                proxy.type = option.type();
                proxy.label = option.label();
                proxy.value = event.getValue().toString();
                proxyMapping.put(affectedField, proxy);
                persistentOptions.add(proxy);
                persistentOptions.save();
            }
            onOptionsChanged();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void persistOptions() {
        for (Map.Entry<Field, OptionProxy> proxyEntry : proxyMapping.entrySet()) {
            OptionProxy proxy = proxyEntry.getValue();
            if (proxy != null) {
                try {
                    proxy.value = proxyEntry.getKey().get(this).toString();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        persistentOptions.save();
    }

    public void onOptionsChanged() {

    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean hasOptions() {
        return true;
    }

    public class SchedulableRunnable implements Runnable {
        private final Method method;
        private final long millis;
        private long lastRan;

        private SchedulableRunnable(Method method, long millis) {
            this.method = method;
            this.millis = millis;
        }

        @Override
        public void run() {
            try {
                if (manager.isReady()) {
                    method.invoke(Plugin.this);
                }
                lastRan = System.currentTimeMillis();
            } catch (IllegalAccessException | InvocationTargetException e) {
                logger.error("Failed to invoke scheduled runnable.", e);
            }
        }

        public long getMillis() {
            return millis;
        }

        public long getLastRan() {
            return lastRan;
        }
    }

    public PluginManager getManager() {
        return manager;
    }
}
