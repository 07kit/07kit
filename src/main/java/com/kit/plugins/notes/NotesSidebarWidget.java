package com.kit.plugins.notes;

import com.kit.Application;
import com.kit.gui.component.MateScrollPane;
import jiconfont.icons.FontAwesome;
import jiconfont.swing.IconFontSwing;
import com.kit.Application;
import com.kit.gui.component.SidebarWidget;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * Date: 26/10/2016
 * Time: 09:46
 *
 * @author Matt Collinge
 */
public class NotesSidebarWidget extends JPanel implements SidebarWidget {

    private static final Image NORMAL_ICON = IconFontSwing.buildImage(FontAwesome.FILE_TEXT, 20, Color.GRAY);
    private static final Image TOGGLED_ICON = IconFontSwing.buildImage(FontAwesome.FILE_TEXT, 20, Color.WHITE);

    private final NotesPlugin notesPlugin;
    private final JTextArea textArea;

    public NotesSidebarWidget(NotesPlugin plugin) {
        notesPlugin = plugin;

        setLayout(new BorderLayout());

        textArea = new JTextArea(plugin.getNotes());
        textArea.setForeground(Application.COLOUR_SCHEME.getText());
        textArea.setBackground(Application.COLOUR_SCHEME.getDark().brighter());
        textArea.setBorder(new EmptyBorder(1, 1, 1, 1));
        textArea.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                notesPlugin.save(textArea.getText());
            }
        });
        MateScrollPane scrollPane = new MateScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

    }

    @Override
    public Component getContent() {
        return this;
    }

    @Override
    public String getTitle() {
        return "Notes";
    }

    @Override
    public Image getIcon(boolean toggled) {
        return toggled ? TOGGLED_ICON : NORMAL_ICON;
    }
}
