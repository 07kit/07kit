package com.kit.plugins.notes;

import com.kit.api.plugin.Option;
import com.kit.api.plugin.Plugin;
import com.kit.core.control.PluginManager;

/**
 * @author Matt Collinge
 */
public class NotesPlugin extends Plugin {

    @Option(label = "Notes", value = "", type = Option.Type.HIDDEN)
    private String notes;

    private final NotesSidebarWidget widget;

    public NotesPlugin(PluginManager manager) {
        super(manager);
        widget = new NotesSidebarWidget(this);
    }

    @Override
    public String getName() {
        return "Notes";
    }

    @Override
    public void start() {
        ui.registerSidebarWidget(widget);
    }

    @Override
    public void stop() {
        ui.deregisterSidebarWidget(widget);
    }

    public void save(String text) {
        notes = text;
        persistOptions();
    }

    public String getNotes() {
        return notes;
    }
}
