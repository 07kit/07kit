package com.kit.plugins;

import com.kit.Application;
import com.kit.api.event.EventHandler;
import com.kit.api.event.PaintEvent;
import com.kit.api.event.PlayerMenuCreatedEvent;
import com.kit.api.plugin.Plugin;
import com.kit.api.util.PaintUtils;
import com.kit.core.Session;
import com.kit.core.control.PluginManager;
import com.kit.game.engine.renderable.entity.IPlayer;
import com.kit.Application;
import com.kit.api.event.EventHandler;
import com.kit.api.event.PaintEvent;
import com.kit.api.plugin.Plugin;
import com.kit.core.control.PluginManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class FirstActionOverlayPlugin extends Plugin {

    public FirstActionOverlayPlugin(PluginManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "First Action Overlay";
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    private String getColor(String action) {
        String firstIdxStr = "(level-";
        int firstIdx = action.indexOf(firstIdxStr);
        int combatLevelRemote = Integer.parseInt(action.substring(firstIdx + firstIdxStr.length(),
                action.indexOf(")", firstIdx)).trim());
        int combatLevelLocal = Session.get().player.getCombatLevel();

        if (combatLevelLocal > combatLevelRemote) {
            return "#00ff00";
        } else if (combatLevelRemote > combatLevelLocal) {
            return "#ff0000";
        } else {
            return "#ffff00";
        }
    }

    @EventHandler
    public void onPaint(PaintEvent evt) {
        if (!Session.get().isLoggedIn() || Session.get().menu.isOpen()) {
            return;
        }
        List<String> options = Session.get().menu.getOptions();
        List<String> actions = Session.get().menu.getActions();

        String menuOption = options.size() > 0 ? options.get(0) : null;
        String menuAction = actions.size() > 0 ? actions.get(0) : null;
        String action = menuOption != null && menuAction != null ? menuOption + " " + menuAction : null;
        if (action != null && !action.toLowerCase().trim().equals("walk here") && !action.toLowerCase().trim().equals("cancel")) {
            Graphics2D g = (Graphics2D) evt.getGraphics().create();
            Point mousePos = Session.get().mouse.getPosition();

            g.setFont(g.getFont().deriveFont(Font.BOLD));
            int actionWidth = g.getFontMetrics().stringWidth(action);

            Rectangle box = new Rectangle(mousePos.x, (mousePos.y - g.getFontMetrics().getHeight()), actionWidth + 10, 18);


            g.setColor(new Color(75, 67, 54, 255));
            g.fillRect(box.x, box.y, box.width, box.height);

            g.setColor(Color.BLACK);
            g.drawRect(box.x, box.y, box.width, box.height);

            g.setColor(Application.COLOUR_SCHEME.getText());

            String levelStr = null;
            Color levelColor = null;
            if (menuAction.contains("(level-")) {
                levelColor = Color.decode(getColor(menuAction));
                String firstIdxStr = "(level-";
                int firstIdx = action.indexOf(firstIdxStr);
                levelStr = action.substring(firstIdx,
                        action.indexOf(")", firstIdx) + 1).trim();
                menuAction = menuAction.replaceAll(levelStr, "")
                        .replaceAll("\\(\\)", "");
            }

            PaintUtils.drawString(g, menuOption + " ", box.x + 5, box.y + 14);

            actionWidth = g.getFontMetrics().stringWidth(menuOption + " ");

            g.setColor(Color.WHITE.darker());
            PaintUtils.drawString(g, menuAction, box.x + actionWidth + 5, box.y + 14);

            actionWidth = g.getFontMetrics().stringWidth(menuOption + " " + menuAction);

            if (levelStr != null) {
                g.setColor(levelColor);
                PaintUtils.drawString(g, levelStr, box.x + actionWidth + 5, box.y + 14);
            }

            g.dispose();
        }
    }
}
