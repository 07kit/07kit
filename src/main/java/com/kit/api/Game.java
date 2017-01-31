package com.kit.api;

import com.kit.api.event.MessageEvent;
import com.kit.api.event.MessageEvent;

public interface Game {

	void sendChatboxMessage(String message, String sender, String clan, MessageEvent.Type type);

	void sendChatboxMessage(String message, MessageEvent.Type type);

	void sendChatboxMessage(String message);

	void sendChatboxMessage(MessageEvent evt);

	void sendChatboxMessage(String message, String sender);

}
