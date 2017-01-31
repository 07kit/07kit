package com.kit.plugins;

import com.kit.api.event.EventHandler;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.api.util.PriceLookup;
import com.kit.api.util.Utilities;
import com.kit.api.wrappers.Widget;
import com.kit.core.Session;
import com.kit.api.event.EventHandler;
import com.kit.api.event.PaintEvent;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.api.util.PriceLookup;
import com.kit.api.util.Utilities;
import com.kit.api.wrappers.Widget;
import com.kit.core.Session;
import com.kit.core.control.PluginManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class TradeOverlayPlugin extends Plugin {

    public static final int TRADE_SCREEN_PARENT = 335;
    public static final int TRADE_SCREEN_CHILD_MY_ITEMS = 25;
    public static final int TRADE_SCREEN_CHILD_THEIR_ITEMS = 28;

    private Map<Point, Integer> pricesToPaint = new HashMap<>();

    private boolean render = false;

    public TradeOverlayPlugin(PluginManager manager) {
        super(manager);
    }

    @Schedule(1000)
    public void updatePrices() {
        Map<Point, Integer> newPricesToPaint = new HashMap<>();
        if (Session.get().isLoggedIn()) {
            Widget myItemsWidget = Session.get().widgets.find(TRADE_SCREEN_PARENT, TRADE_SCREEN_CHILD_MY_ITEMS);
            Widget theirItemsWidget = Session.get().widgets.find(TRADE_SCREEN_PARENT, TRADE_SCREEN_CHILD_THEIR_ITEMS);
            if (myItemsWidget != null && theirItemsWidget != null) {
                Widget[] myItemsWidgetChildren = myItemsWidget.getChildren();
                Widget[] theirItemsWidgetChildren = theirItemsWidget.getChildren();
                if (myItemsWidgetChildren != null) {
                    for (Widget myItemsWidgetChild : myItemsWidgetChildren) {
                        int id = myItemsWidgetChild.getItemId();
                        int stack = myItemsWidgetChild.getItemStackSize();
                        if (id == -1 || stack == 0) {
                            continue;
                        }

                        Point point = myItemsWidgetChild.getBasePoint();
                        int price = PriceLookup.getPrice(id) * stack;
                        if (point != null) {
                            newPricesToPaint.put(point, price);
                        }
                    }
                }
                if (theirItemsWidgetChildren != null) {
                    for (Widget theirItemsWidgetChild : theirItemsWidgetChildren) {
                        int id = theirItemsWidgetChild.getItemId();
                        int stack = theirItemsWidgetChild.getItemStackSize();
                        if (id == -1 || stack == 0) {
                            continue;
                        }

                        Point point = theirItemsWidgetChild.getBasePoint();
                        int price = PriceLookup.getPrice(id) * stack;
                        if (point != null) {
                            newPricesToPaint.put(point, price);
                        }
                    }
                }
                render = newPricesToPaint.size() > 0;
                pricesToPaint = newPricesToPaint;
                return;
            }
        }
        render = false;
    }

    @EventHandler
    public void onPaint(PaintEvent event) {
        if (render) {
            event.getGraphics().setColor(Color.ORANGE);
            for (Map.Entry<Point, Integer> priceToPaint : pricesToPaint.entrySet()) {
                event.getGraphics().drawString(Utilities.prettyFormat(priceToPaint.getValue()),
                        priceToPaint.getKey().x, priceToPaint.getKey().y + 24);
            }
        }
    }

    @Override
    public String getName() {
        return "Trade Overlay";
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }
}
