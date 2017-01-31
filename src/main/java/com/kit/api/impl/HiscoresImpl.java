package com.kit.api.impl;

import com.google.common.base.CharMatcher;
import com.kit.api.Hiscores;
import com.kit.api.MethodContext;
import com.kit.api.util.Internet;
import com.kit.api.wrappers.Skill;
import com.kit.api.wrappers.hiscores.HiscoreLookup;
import com.kit.api.wrappers.hiscores.HiscoreSkill;
import com.kit.api.wrappers.hiscores.HiscoreType;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class HiscoresImpl implements Hiscores {
    private final Logger logger = Logger.getLogger(HiscoresImpl.class);
    private final MethodContext context;

    public HiscoresImpl(MethodContext context) {
        this.context = context;
    }

    public static class Wrapper {

        private Skill skill;
        private HiscoreSkill hiscoreSkill;

        public Wrapper(Skill skill, HiscoreSkill hiscoreSkill) {
            this.skill = skill;
            this.hiscoreSkill = hiscoreSkill;
        }

        public Skill getSkill() {
            return skill;
        }

        public HiscoreSkill getHiscoreSkill() {
            return hiscoreSkill;
        }
    }

    @Override
    public HiscoreLookup lookup(String username, HiscoreType type) {
        try {
            String normalisedUsername = CharMatcher.WHITESPACE
                    .or(CharMatcher.BREAKING_WHITESPACE)
                    .replaceFrom(username, "_");
            String response = Internet.getText(type.getUrl() + normalisedUsername.replace(" ", "%20"));
            String[] data = response.split(" ");
            Map<Skill, HiscoreSkill> skillMapping = new HashMap<>();
            for (int i = 0; i < data.length; i++) {
                String[] values = data[i].split(",");
                HiscoreSkill skill = new HiscoreSkill(toInt(values[0]),
                        toInt(values[1]),
                        toInt(values.length == 3 ? values[2] : "0"));

                if (i == 0) {
                    skillMapping.put(Skill.OVERALL, skill);
                } else {
                    skillMapping.put(Skill.forIndex(i - 1), skill);
                }
            }
            return new HiscoreLookup(skillMapping);
        } catch (Exception e) {
            logger.error("Failed to retrieve hiscores for " + username, e);
            return null;
        }
    }

    private int toInt(String s) {
        return Integer.parseInt(s);
    }
}
