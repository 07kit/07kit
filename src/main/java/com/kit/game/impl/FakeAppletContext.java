package com.kit.game.impl;

import javax.imageio.ImageIO;
import java.applet.Applet;
import java.applet.AppletContext;
import java.applet.AudioClip;
import java.awt.Desktop;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

/**
 * An implementation of AppletContext that
 * tries to simulate the applet context as
 * provided by the browser plugin.
 *
 */
public final class FakeAppletContext implements AppletContext {
    private final Map<String, InputStream> streams = new HashMap<>();
    private Applet applet;
    private String status;

    @Override
    public AudioClip getAudioClip(URL url) {
        return Applet.newAudioClip(url);
    }

    @Override
    public Image getImage(URL url) {
        try {
            return ImageIO.read(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Applet getApplet(String name) {
        return applet;
    }

    @Override
    public Enumeration<Applet> getApplets() {
        Vector<Applet> applets = new Vector<>();
        applets.add(applet);
        return applets.elements();
    }

    @Override
    public void showDocument(URL url) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(url.toURI());
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void showDocument(URL url, String target) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(url.toURI());
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void showStatus(String status) {
        this.status = status;
    }

    @Override
    public void setStream(String key, InputStream stream) throws IOException {
        if (streams.containsKey(key)) {
            streams.remove(key);
        }
        streams.put(key, stream);
    }

    @Override
    public InputStream getStream(String key) {
        return streams.get(key);
    }

    @Override
    public Iterator<String> getStreamKeys() {
        return streams.keySet().iterator();
    }

    public void setApplet(Applet applet) {
        this.applet = applet;
    }

    public String getStatus() {
        return status;
    }
}
