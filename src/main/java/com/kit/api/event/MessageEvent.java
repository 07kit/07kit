package com.kit.api.event;

import java.util.HashMap;
import java.util.Map;

/**
 */
public class MessageEvent {

    public enum Type {
        MESSAGE_SERVER(0),
        MESSAGE_CHAT(2),
        MESSAGE_PRIVATE_INCOMING(3),
        MESSAGE_TRADE_INCOMING(4),
        MESSAGE_PRIVATE_INFO(5),
        MESSAGE_PRIVATE_SENT(6),
        MESSAGE_CLANCHAT(9),
        MESSAGE_CLIENT(11),
        MESSAGE_TRADE_SENT(12),
        MESSAGE_EXAMINE_ITEM(27),
        MESSAGE_EXAMINE_NPC(28),
        MESSAGE_EXAMINE_OBJECT(29),
        MESSAGE_AUTOCHAT(90),
        MESSAGE_SERVER_FILTERED(105),
        MESSAGE_ACTION(109);

        private int type;

        private static final Map<Integer, Type> TYPE_MAP = new HashMap<>();

        static {
            for (Type t : values()) {
                TYPE_MAP.put(t.getType(), t);
            }
        }
        public static Type forType(int type) {
            return TYPE_MAP.get(type);
        }

        Type(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }
    }
    private String message;
    private int rawType;
    private Type type;
    private String sender;
    private String clanName;

    public MessageEvent(String sender, String message, String clanName, int type) {
        this.sender = sender;
        this.message = message;
        this.clanName = clanName;
        this.type = Type.forType(type);
        this.rawType = type;
    }

    public Type getType() {
        return type;
    }

    public String getSender() {
        return sender;
    }

    public String getClanName() {
        return clanName;
    }

    public String getMessage() {
        return message;
    }

    public int getRawType() {
        return rawType;
    }

    @Override
    public String toString() {
        return sender + ":" + message + ":" + clanName + ":" + ":" + rawType +" [" + type + "]";
    }
}
