package com.kit.api.wrappers.hiscores;

public enum HiscoreType {

	STANDARD("http://services.runescape.com/m=hiscore_oldschool/index_lite.ws?player="),
	IRON_MAN("http://services.runescape.com/m=hiscore_oldschool_ironman/index_lite.ws?player="),
	ULTIMATE_IRON_MAN("http://services.runescape.com/m=hiscore_oldschool_ultimate/index_lite.ws?player=");

	private String url;

	private HiscoreType(String url) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}
}
