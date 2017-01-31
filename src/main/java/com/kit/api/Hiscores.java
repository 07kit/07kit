package com.kit.api;

import com.kit.api.wrappers.hiscores.HiscoreLookup;
import com.kit.api.wrappers.hiscores.HiscoreType;
import com.kit.api.wrappers.hiscores.HiscoreLookup;

public interface Hiscores {

	HiscoreLookup lookup(String username, HiscoreType type);

}
