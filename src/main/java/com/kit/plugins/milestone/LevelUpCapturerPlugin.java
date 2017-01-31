package com.kit.plugins.milestone;

import com.kit.api.event.EventHandler;
import com.kit.api.event.LevelUpEvent;
import com.kit.api.event.MessageEvent;
import com.kit.api.plugin.Plugin;
import com.kit.api.service.SocialService;
import com.kit.api.wrappers.Skill;
import com.kit.core.Session;
import com.kit.core.control.PluginManager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Timestamp;

public class LevelUpCapturerPlugin extends Plugin {

    public LevelUpCapturerPlugin(PluginManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "Level-up Capturer";
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean hasOptions() {
        return false;
    }

    private Skill getSkill(String partial) {
        for (Skill skill : Skill.values()) {
            if (skill.getName().toLowerCase().contains(partial.toLowerCase())) {
                return skill;
            }
        }
        return null;
    }

    @EventHandler
    public void onMessage(MessageEvent evt) {
        if (evt.getSender().length() == 0 && evt.getType() == MessageEvent.Type.MESSAGE_SERVER &&
                evt.getMessage().contains("Congratulations, you just advanced a")) {
            String skillRaw = evt.getMessage().substring(0, evt.getMessage().indexOf(" level")).trim();
            skillRaw = skillRaw.substring(skillRaw.lastIndexOf(' ') + 1).trim();
            Skill skill = getSkill(skillRaw);
            if (skill == null) {
                logger.error("Unable to find skill for partial [" + skillRaw + "]");
            } else {
                //TODO we have to use Session.get() here, fix pls
                int level = Session.get().getClient().getSkillBases()[skill.getIndex()];
                Path outputFile = Session.get().screen.capture(skill.getName() + "_" + "level_up_to_" + level, false);
                Session.get().getEventBus().submit(new LevelUpEvent(outputFile.toFile(), skill, level));
            }
        }

    }

}
