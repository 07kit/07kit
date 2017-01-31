package com.kit.plugins.twitch;

import com.kit.core.Session;
import com.kit.core.Session;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class TwitchChatMessageHandler extends ListenerAdapter {

	@Override
	public void onMessage(MessageEvent event) throws Exception {
		//TODO this should go through our event bus???
		if (Session.get().isLoggedIn() && event.getUser() != null && event.getChannel() != null) {
			String color = "DB7C00";
			if (event.getUser().getNick() != null &&
					event.getChannel().getName() != null &&
					event.getUser().getNick().toLowerCase().equals(event.getChannel().getName().toLowerCase())) {
				color = "D60000";
			}

			Session.get().game.sendChatboxMessage("<col=" + color + ">" + event.getUser().getNick() + "</col>"
					+ ": " + event.getMessage(), "[" + event.getChannel().getName() + "]");
		}
	}
}
