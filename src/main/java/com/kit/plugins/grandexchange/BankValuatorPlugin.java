package com.kit.plugins.grandexchange;

import com.kit.api.event.ActionEvent;
import com.kit.api.impl.tabs.BankImpl;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.api.util.Utilities;
import com.kit.api.wrappers.Widget;
import com.kit.core.Session;
import com.kit.Application;
import com.kit.api.event.ActionEvent;
import com.kit.api.event.EventHandler;
import com.kit.api.event.PaintEvent;
import com.kit.api.impl.tabs.BankImpl;
import com.kit.api.overlay.BoxOverlay;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.api.util.ColorUtils;
import com.kit.api.util.PaintUtils;
import com.kit.api.util.PriceLookup;
import com.kit.api.util.Utilities;
import com.kit.api.wrappers.Widget;
import com.kit.api.wrappers.WidgetItem;
import com.kit.core.Session;
import com.kit.core.control.PluginManager;
import com.kit.game.cache.io.Utils;
import com.kit.api.event.EventHandler;
import com.kit.api.plugin.Plugin;
import com.kit.api.util.PriceLookup;
import com.kit.api.util.Utilities;
import com.kit.api.wrappers.Widget;
import com.kit.api.wrappers.WidgetItem;
import com.kit.core.control.PluginManager;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BankValuatorPlugin extends Plugin {

	private BankImpl.Tab current;
	private long totalValue = 0;

	public BankValuatorPlugin(PluginManager manager) {
		super(manager);
	}

	@Override
	public String getName() {
		return "Bank Value";
	}

	@Override
	public void start() {
	}

	@Override
	public void stop() {
	}

	@Schedule(800)
	public void update() {
		if (Session.get().bank.isOpen()) {
			updateText();
			int newTotal = 0;
			Map<Integer, Integer> idsAndStacks = new HashMap<>();
			for (WidgetItem item : Session.get().bank.getItemsForTab(Session.get().bank.getCurrent())) {
				if (item.getId() == 995) {
					newTotal += item.getStackSize();
				} else {
					idsAndStacks.put(item.getId(), item.getStackSize());
				}
			}
			Map<Integer, Integer> idsAndPrices = PriceLookup.getPrices(idsAndStacks.keySet());
			for (Map.Entry<Integer, Integer> entry : idsAndPrices.entrySet()) {
				if (idsAndStacks.containsKey(entry.getKey()) && idsAndStacks.get(entry.getKey()) != null || entry.getValue() != null) {
					newTotal += entry.getValue() * idsAndStacks.get(entry.getKey());
				}
			}
			totalValue = newTotal;
		}
	}

	@EventHandler
	public void onAction(ActionEvent e) {
		if (current == null || !current.equals(Session.get().bank.getCurrent())) {
			current = Session.get().bank.getCurrent();
			totalValue = 0;
		}
	}

	@Schedule(1500)
	private void updateText() {
		Widget titleWidget = Session.get().bank.getTitleWidget();
		if (titleWidget != null) {
			String text;
			switch (Session.get().bank.getCurrent()) {
				case ONE:
					text = "Tab 1";
					break;
				case TWO:
					text = "Tab 2";
					break;
				case THREE:
					text = "Tab 3";
					break;
				case FOUR:
					text = "Tab 4";
					break;
				case FIVE:
					text = "Tab 5";
					break;
				case SIX:
					text = "Tab 6";
					break;
				case SEVEN:
					text = "Tab 7";
					break;
				case EIGHT:
					text = "Tab 8";
					break;
				case NINE:
					text = "Tab 9";
					break;
					default:
						text = "The Bank of RuneScape";
						break;
			}
			titleWidget.unwrap().setText(text + " ~ " + (totalValue == 0 ? "Working..." : Utilities.simpleFormat(totalValue, 0)));
		}
	}
}
