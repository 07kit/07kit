package com.kit.gui.view;

import com.kit.gui.component.MateScrollPane;
import com.kit.gui.util.RelativeLayout;
import com.kit.Application;
import com.kit.api.event.OptionChangedEvent;
import com.kit.api.plugin.Option;
import com.kit.api.plugin.Plugin;
import com.kit.gui.component.MateCheckBox;
import com.kit.gui.util.RelativeLayout;
import net.miginfocom.swing.MigLayout;
import com.kit.Application;
import com.kit.api.event.OptionChangedEvent;
import com.kit.api.plugin.Option;
import com.kit.api.plugin.Plugin;
import com.kit.gui.util.RelativeLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 */
public class PluginSettingsView extends JPanel {

	private static final Pattern LINK_PATTERN = Pattern.compile("href=\"(.*)\"");

    private final Plugin plugin;

    public PluginSettingsView(Plugin plugin) {
        this.plugin = plugin;
        build();
    }

    private void build() {
        setLayout(new BorderLayout());

        JPanel container = new JPanel(new MigLayout("insets 0, gap rel 0"));
        container.setBackground(Application.COLOUR_SCHEME.getDark());

        JLabel title = new JLabel(plugin.getName());
        title.setPreferredSize(new Dimension(635, 40));
        title.setFont(title.getFont().deriveFont(Font.BOLD, 14f));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setBackground(Application.COLOUR_SCHEME.getDark().darker().darker());
        title.setBorder(new LineBorder(Color.WHITE, 1));
        title.setForeground(Application.COLOUR_SCHEME.getText());
        title.setOpaque(true);
        container.add(title, "growx, span, push, wrap, dock north");

        int index = 0;
        for (Option option : plugin.getOptions()) {
            if (option.type() == Option.Type.HIDDEN)
                continue;
            RelativeLayout layout = new RelativeLayout(RelativeLayout.X_AXIS);
            JPanel settingsPanel = new JPanel(layout);
            settingsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
            Color background = null;
            if (index % 2 == 0) {
                background = Application.COLOUR_SCHEME.getDark();
            } else {
                background = Application.COLOUR_SCHEME.getBright();
            }
            settingsPanel.setBackground(background);
            settingsPanel.setPreferredSize(new Dimension(635, 50));

            JLabel optionLabel = new JLabel(option.label());
			Matcher m = LINK_PATTERN.matcher(option.label());
			if (m.find()) {
				optionLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
				optionLabel.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						try {
							Desktop.getDesktop().browse(new URL(m.group(1)).toURI());
						} catch (IOException | URISyntaxException e1) {
							e1.printStackTrace();
						}
					}
				});
			}
            optionLabel.setOpaque(false);
            optionLabel.setForeground(Application.COLOUR_SCHEME.getText());
            settingsPanel.add(optionLabel, 2F);

            Component valueComp = null;

            if (option.type() == Option.Type.TEXT) {
                JTextField txtField = new JTextField();
                txtField.setPreferredSize(new Dimension(250, 40));
                txtField.setBackground(background);
                txtField.setForeground(Application.COLOUR_SCHEME.getText());
                txtField.setText(plugin.getOptionValue(option).toString());
				txtField.addFocusListener(new FocusAdapter() {
					@Override
					public void focusLost(FocusEvent e) {
						plugin.handleOptionChanged(
							new OptionChangedEvent(plugin.getClass(), option, txtField.getText()));
					}
				});
                valueComp = txtField;
            } else if (option.type() == Option.Type.TOGGLE) {
                MateCheckBox checkBox = new MateCheckBox();
                checkBox.setBackground(background);
                checkBox.setSelected(Boolean.valueOf(plugin.getOptionValue(option).toString()));
                checkBox.addActionListener(e -> plugin.handleOptionChanged(
                        new OptionChangedEvent(plugin.getClass(), option, checkBox.isSelected())));
                valueComp = checkBox;
            } else if (option.type() == Option.Type.NUMBER) {
                JTextField txtField = new JTextField();
                txtField.setPreferredSize(new Dimension(250, 40));
                txtField.setBackground(background);
                txtField.setForeground(Application.COLOUR_SCHEME.getText());
                txtField.setText(plugin.getOptionValue(option).toString());
				txtField.addFocusListener(new FocusAdapter() {
					@Override
					public void focusLost(FocusEvent e) {
						if (txtField.getText().length() == 0) {
							plugin.handleOptionChanged(
									new OptionChangedEvent(plugin.getClass(), option, option.value()));
						} else {
							plugin.handleOptionChanged(
									new OptionChangedEvent(plugin.getClass(), option, Integer.valueOf(txtField.getText())));
						}
					}
				});
                valueComp = txtField;
            }

            settingsPanel.add(valueComp, 1F);

            index++;
            container.add(settingsPanel, "span, growx, push, wrap, dock north");
        }

        MateScrollPane scrollPane = new MateScrollPane(container);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(75);
        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        add(scrollPane, BorderLayout.CENTER);
    }

}
