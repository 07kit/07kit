package com.kit.plugins.combat;

import com.kit.api.event.EventHandler;
import com.kit.api.event.PaintEvent;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.core.control.PluginManager;
import com.kit.api.event.EventHandler;
import com.kit.api.event.PaintEvent;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.core.control.PluginManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 */
public class TimersPlugin extends Plugin {
    private final Logger logger = Logger.getLogger(TimersPlugin.class);
    private final List<GameTimer> timers = new ArrayList<>();

    public TimersPlugin(PluginManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "Timers";
    }

    @Override
    public String getGroup() {
        return "Combat";
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @EventHandler
    public void paint(PaintEvent event) {
        if (!isLoggedIn()) {
            return;
        }

        for (GameTimer timer : timers) {
            long timeRemaining = timer.duration - (System.currentTimeMillis() - timer.toggled);
            if (timeRemaining > 0) {

            }
        }
    }

    @Schedule(600)
    public void pollTimers() {
        for (GameTimer timer : timers) {
            boolean active = System.currentTimeMillis() - timer.toggled < timer.duration;
            try {
                if (!active && timer.eval.call()) {
                    timer.toggled = System.currentTimeMillis();
                }
            } catch (Exception e) {
                logger.error("Error whilst evaluating timer.", e);
            }
        }
    }

    private class GameTimer {
        public final Callable<Boolean> eval;
        public String name;
        public String icon;
        public long toggled;
        public int duration;

        private GameTimer(Callable<Boolean> eval, String name, String icon, int duration) {
            this.eval = eval;
            this.name = name;
            this.icon = icon;
            this.duration = duration;
        }
    }

}
