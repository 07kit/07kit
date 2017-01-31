package com.kit.plugins.grandexchange;

import com.kit.api.event.ActionEvent;
import com.kit.api.event.ActionEvent;
import com.kit.api.event.EventHandler;
import com.kit.api.event.MessageEvent;
import com.kit.api.plugin.Plugin;
import com.kit.api.util.PriceLookup;
import com.kit.api.util.Utilities;
import com.kit.core.Session;
import com.kit.core.control.PluginManager;

public class ItemExaminePricePlugin extends Plugin {

	public ItemExaminePricePlugin(PluginManager manager) {
		super(manager);
	}

	@Override
	public String getName() {
		return "Item Examine Price";
	}

	@Override
	public void start() {

	}

	@Override
	public void stop() {

	}

	@EventHandler
	public void onExamine(ActionEvent event) {
		if (event.getInteraction() != null && event.getInteraction().equals("Examine")) {
			int itemId = event.getId();
			if (itemId > -1) {
				int price = PriceLookup.getPrice(itemId);
				if (price > 0) {
					int stack = Session.get().inventory.getCount(true, itemId);
					Session.get().game.sendChatboxMessage(
							"A " + event.getEntityName() + "</col> is worth " + Utilities.simpleFormat(price, 0) +
							". You have " + Utilities.simpleFormat(price * stack, 0) + " worth in your inventory.",
							"LC", "", MessageEvent.Type.MESSAGE_CHAT);
				}
			}
		}
	}
}
