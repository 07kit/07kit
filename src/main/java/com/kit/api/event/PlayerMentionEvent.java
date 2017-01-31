package com.kit.api.event;

/**
 * @author : const_
 */
public class PlayerMentionEvent {

    private MessageEvent message;

    public PlayerMentionEvent(MessageEvent message) {
        this.message = message;
    }

    public MessageEvent getMessage() {
        return message;
    }
}
