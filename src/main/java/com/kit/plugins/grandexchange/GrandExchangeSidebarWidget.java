package com.kit.plugins.grandexchange;

import com.kit.Application;
import com.kit.game.engine.IGrandExchangeOffer;
import com.kit.Application;
import com.kit.game.engine.IGrandExchangeOffer;
import com.kit.gui.component.MateScrollPane;
import com.kit.gui.component.SidebarWidget;
import jiconfont.icons.FontAwesome;
import jiconfont.swing.IconFontSwing;
import net.miginfocom.swing.MigLayout;
import com.kit.Application;
import com.kit.game.engine.IGrandExchangeOffer;
import com.kit.gui.component.SidebarWidget;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 */
public class GrandExchangeSidebarWidget extends MateScrollPane implements SidebarWidget {
    private final Image NORMAL_ICON = IconFontSwing.buildImage(FontAwesome.MONEY, 20, Color.GRAY);
    private final Image TOGGLED_ICON = IconFontSwing.buildImage(FontAwesome.MONEY, 20, Color.WHITE);
    private final Map<IGrandExchangeOffer, GrandExchangeOfferWidget> widgets = new HashMap<>();
    private final JPanel container;

    public GrandExchangeSidebarWidget() {
        container = new JPanel();
        setViewportView(container);
        setBorder(null);

        container.setLayout(new MigLayout("insets 0, gap rel 0"));
        container.setBackground(Application.COLOUR_SCHEME.getDark().brighter());
    }

    @Override
    public Component getContent() {
        return this;
    }

    @Override
    public String getTitle() {
        return "Grand Exchange";
    }

    @Override
    public Image getIcon(boolean toggled) {
        return toggled ? TOGGLED_ICON : NORMAL_ICON;
    }

    public void addGrandExchangeOffer(IGrandExchangeOffer offer) {
        GrandExchangeOfferWidget widget = new GrandExchangeOfferWidget(offer, widgets.size() + 1);
        widgets.put(offer, widget);
        render();
    }

    private void render() {
        int[] counter = {0};
        container.removeAll();
        widgets.entrySet().forEach(x -> {
            x.getValue().setIndex(counter[0]);
            container.remove(x.getValue());
            container.add(x.getValue(), "push, growx, span");
            counter[0]++;
        });

        container.add(Box.createVerticalBox(), "span, grow, pushy, wrap push");
        container.revalidate();
        container.repaint();
    }

    public void removeGrandExchangeOffer(IGrandExchangeOffer offer) {
        GrandExchangeOfferWidget widget = widgets.remove(offer);
        if (widget != null) {
            container.remove(widget);
        }
        render();
    }

}
