package com.kit.api.event;

import com.kit.core.model.Property;
import com.kit.core.model.Property;

public class PropertyChangedEvent {

	private Property property;

	public PropertyChangedEvent(Property property) {
		this.property = property;
	}

	public Property getProperty() {
		return property;
	}
}
