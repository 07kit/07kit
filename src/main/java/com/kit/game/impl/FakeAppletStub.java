package com.kit.game.impl;


import com.kit.api.event.AppletResizeEvent;
import com.kit.core.Session;
import com.kit.game.AppletConfiguration;

import java.applet.Applet;
import java.applet.AppletStub;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * An implementation of AppletStub that
 * simulates the one provided by the browser
 * plugin.
 *
 */
public final class FakeAppletStub implements AppletStub {
    private final AppletConfiguration configuration;
    private final FakeAppletContext appletContext;
    private boolean active;

    public FakeAppletStub(AppletConfiguration configuration) {
        this.configuration = configuration;
        this.appletContext = new FakeAppletContext();
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public URL getDocumentBase() {
        try {
            return new URL(configuration.getDocumentBase());
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid document base URL!");
        }
    }

    @Override
    public URL getCodeBase() {
        try {
            return new URL(configuration.getDocumentBase());
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Invalid code base URL!");
        }
    }

    @Override
    public String getParameter(String name) {
        return configuration.getParameters().get(name);
    }

    @Override
    public FakeAppletContext getAppletContext() {
        return appletContext;
    }

    @Override
    public void appletResize(int width, int height) {
        Applet applet = appletContext.getApplet("main");
        if (applet != null) {
            applet.setSize(width, height);
        }
    }
}
