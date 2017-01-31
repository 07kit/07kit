package com.kit.api.impl.game;

import com.kit.api.Game;
import com.kit.api.event.MessageEvent;
import com.kit.api.Game;
import com.kit.api.MethodContext;
import com.kit.api.event.MessageEvent;
import com.kit.api.MethodContext;
import com.kit.api.event.MessageEvent;

public class GameImpl implements Game {

	private final MethodContext ctx;

	public GameImpl(MethodContext ctx) {
		this.ctx = ctx;
	}

	@Override
	public void sendChatboxMessage(String message, String sender, String clan, MessageEvent.Type type) {
		ctx.client().onMessageReceived(type.getType(), sender, message, clan);
	}

	@Override
	public void sendChatboxMessage(String message, MessageEvent.Type type) {
		sendChatboxMessage(message, ctx.player.getName(), null, type);
	}

	@Override
	public void sendChatboxMessage(String message) {
		sendChatboxMessage(message, ctx.player.getName(), null, MessageEvent.Type.MESSAGE_CHAT);
	}

	@Override
	public void sendChatboxMessage(String message, String sender) {
		sendChatboxMessage(message, sender, null, MessageEvent.Type.MESSAGE_CHAT);
	}

	@Override
	public void sendChatboxMessage(MessageEvent evt) {
		sendChatboxMessage(evt.getMessage(), evt.getSender(), evt.getClanName(), evt.getType());
	}
}
