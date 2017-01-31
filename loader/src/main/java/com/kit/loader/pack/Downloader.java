package com.kit.loader.pack;

import com.google.common.hash.Hashing;
import org.apache.commons.io.IOUtils;
import com.kit.loader.gui.LoadingFrame;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Downloader {

	private static final String HASH_URL = "http://download.07kit.com/current";
	private static final String DOWNLOAD_URL = "http://download.07kit.com/latest.jar";
	private static final String HOOKS_URL = "http://download.07kit.com/hooks.json";

	private LoadingFrame loadingFrame;

	public Downloader(LoadingFrame loadingFrame) {
		this.loadingFrame = loadingFrame;
	}

	private String getLatestHash() {
		loadingFrame.setLoadingText("Checking if client is up to date...");
		try (InputStream in = new URL(HASH_URL).openStream()) {
			return new String(IOUtils.toByteArray(in)).trim();
		} catch (IOException e) {
			loadingFrame.setLoadingText("Error loading client [ErrorCode: 7A]");
			throw new RuntimeException("Error loading client");
		}
	}

	public byte[] downloadHooks() {
		try {
			loadingFrame.setLoadingText("Checking if client is up to date...");
			try (InputStream in = new URL(HOOKS_URL).openStream()) {
				return IOUtils.toByteArray(in);
			}
		} catch (IOException e) {
			loadingFrame.setLoadingText("Error loading client [ErrorCode: 5D]");
			throw new RuntimeException("Error loading client");
		}
	}

	public File downloadLatestPack() {
		try {
			File dir = new File(System.getProperty("user.home") + File.separator + "07kit" + File.separator + "client");
			if (!dir.exists()) {
				dir.mkdirs();
			}
			loadingFrame.setLoadingText("Checking if client is up to date...");
			String latestHash = getLatestHash();
			File latest = new File(dir.getPath() + File.separator + latestHash + ".jar");

			if (!latest.exists() || !com.google.common.io.Files.hash(latest, Hashing.sha1()).toString().equals(latestHash)) {
				loadingFrame.setLoadingText("Doing some house keeping...");
				for (File f : dir.listFiles()) {
					if (f.getName().endsWith(".jar") && !f.getName().equals(latest.getName())) {
						f.delete();
					}
				}
				loadingFrame.setLoadingText("Downloading latest client...");
				latest.createNewFile();
				try (InputStream in = new URL(DOWNLOAD_URL).openStream()) {
					Files.copy(in, latest.toPath(), StandardCopyOption.REPLACE_EXISTING);
				}
			} else {
				loadingFrame.setLoadingText("Client is up to date!");
			}

			return latest;
		} catch (IOException e) {
			loadingFrame.setLoadingText("Error loading client [ErrorCode: 6B]");
			throw new RuntimeException("Error loading client");
		}
	}
}
