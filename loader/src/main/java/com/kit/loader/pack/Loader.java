package com.kit.loader.pack;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Loader extends ClassLoader {

	private final Map<String, Class<?>> classes = new HashMap<>();
	private final Map<String, byte[]> resources;


	public Loader(Map<String, byte[]> resources) throws IOException {
		this.resources = resources;
	}

	@SuppressWarnings("unchecked")
	private Class<?> defineSelf(String name, byte[] data) throws ClassFormatError {
		if (data != null && data.length > 0) {
			return defineClass(name, data, 0, data.length);
		}
		return null;
	}

	@Override
	public InputStream getResourceAsStream(String name) {
		if (resources.containsKey(name)) {
			return new ByteArrayInputStream(resources.get(name));
		}
		return super.getResourceAsStream(name);
	}

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		Class<?> result = classes.get(name);
		if (result != null) {
			return result;
		}
		result = defineSelf(name, resources.get(name));
		if (result != null) {
			classes.put(name, result);
			return result;
		}
		result = findLoadedClass(name);
		if (result != null) {
			classes.put(name, result);
			return result;
		}
		result = Thread.currentThread().getContextClassLoader().loadClass(name);
		if (result != null) {
			classes.put(name, result);
		}
		return result;
	}

}