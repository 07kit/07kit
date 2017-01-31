package com.kit.http;

import com.google.gson.annotations.SerializedName;

public class TwitchStream {

	@SerializedName("name")
	private final String name;
	@SerializedName("logoUrl")
	private final String logoUrl;
	@SerializedName("bannerUrl")
	private final String bannerUrl;
	@SerializedName("status")
	private final String status;
	@SerializedName("streamUrl")
	private final String streamUrl;
//	private final DateTime lastUpdated;ignore this for now
	@SerializedName("language")
	private final String language;

	public TwitchStream(String name, String logoUrl, String bannerUrl, String status, String streamUrl, String language) {
		this.name = name;
		this.logoUrl = logoUrl;
		this.bannerUrl = bannerUrl;
		this.status = status;
		this.streamUrl = streamUrl;
//		this.lastUpdated = lastUpdated;
		this.language = language;
	}

	public String getName() {
		return name;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public String getBannerUrl() {
		return bannerUrl;
	}

	public String getStatus() {
		return status;
	}

	public String getStreamUrl() {
		return streamUrl;
	}

//	public DateTime getlastUpdated() {
//		return lastUpdated;
//	}

	public String getLanguage() {
		return language;
	}

	@Override
	public String toString() {
		return "TwitchStream{" +
				"name='" + name + '\'' +
				", logoUrl='" + logoUrl + '\'' +
				", bannerUrl='" + bannerUrl + '\'' +
				", status='" + status + '\'' +
				", streamUrl='" + streamUrl + '\'' +
				", language='" + language + '\'' +
				'}';
	}
}
