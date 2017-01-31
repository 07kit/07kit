package com.kit.api.impl.game;


import com.kit.api.MethodContext;
import com.kit.api.Settings;
import com.kit.api.MethodContext;

/**
 * Author: const_
 */
public class SettingsImpl implements Settings {

    private final MethodContext ctx;


    public SettingsImpl(MethodContext ctx) {
        this.ctx = ctx;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int[] getWidgetSettings() {
        int[] widgetSettings = ctx.client().getSettings();
        return widgetSettings != null ? widgetSettings : new int[0];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getWidgetSetting(int index) {
        int[] widgetSettings = getWidgetSettings();
        return widgetSettings != null && index < widgetSettings.length ? widgetSettings[index] : -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int[] getPlayerSettings() {
        // TODO: May have mixed up settings and widget settings here?
        int[] playerSettings = ctx.client().getGameSettings();
        return playerSettings != null ? playerSettings : new int[0];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPlayerSetting(int index) {
        int[] settings = ctx.client().getGameSettings();
        return settings != null && index < settings.length ? settings[index] : -1;
    }
}
