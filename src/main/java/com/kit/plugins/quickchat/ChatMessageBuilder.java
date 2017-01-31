package com.kit.plugins.quickchat;

import java.awt.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatMessageBuilder {

	private final StringBuilder builder = new StringBuilder(128);
	private static final String COLOR_MARKUP_REGEX = "(<col=[a-f0-9]{6}>)|(</col>)";
	private static final Pattern MARKUP_PATTERN = Pattern.compile("(<col=[0-9a-f]{6}>)|(</col>)|(<lt>)|(<gt>)");

	public ChatMessageBuilder appendRaw(Object message) {
		builder.append(message);
		return this;
	}

	public ChatMessageBuilder append(Object message) {
		builder.append(toMessageString(message));
		return this;
	}

	public ChatMessageBuilder appendColored(Color color, Object message) {
		startColor(color);
		builder.append(toMessageString(message));
		endColor();
		return this;
	}

	public ChatMessageBuilder startColor(Color color) {
		builder.append("<col=").append(colorToHex(color)).append(">");
		return this;
	}

	public ChatMessageBuilder endColor() {
		builder.append("</col>");
		return this;
	}

	private String toMessageString(Object message) {
		return message.toString().replaceAll("<", "<lt>").replaceAll(">", "<gt>");
	}

	public int length() {
		return builder.length();
	}

	@Override
	public String toString() {
		return builder.toString();
	}

	private String colorToHex(Color color) {
		return String.format("%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
	}
}
