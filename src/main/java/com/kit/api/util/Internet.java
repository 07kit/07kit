package com.kit.api.util;

import org.jsoup.Jsoup;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

/**
 * @author : constt
 */
public class Internet {

	public static String getText(String url) {
		try {
			return Jsoup.connect(url)
					.ignoreContentType(true)
					.timeout(15000).get().text();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String getHtml(String url) {
		try {
			return Jsoup.connect(url)
					.ignoreContentType(true)
					.timeout(15000).get().html();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}
}

