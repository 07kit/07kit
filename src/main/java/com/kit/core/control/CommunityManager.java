package com.kit.core.control;

import com.kit.api.event.LevelUpEvent;
import com.kit.api.event.MessageEvent;
import com.kit.api.event.ScreenshotEvent;
import com.kit.api.service.SocialService;
import com.kit.core.Session;
import com.kit.api.event.EventHandler;
import com.kit.api.event.LevelUpEvent;
import com.kit.api.event.MessageEvent;
import com.kit.api.event.ScreenshotEvent;
import com.kit.api.service.SocialService;
import com.kit.core.Session;
import com.kit.plugins.quickchat.QuickChatPlugin;

import java.sql.Timestamp;

/**
 */
public final class CommunityManager {
    private final SocialService socialService = new SocialService();
    private final Session session;

    public CommunityManager(Session session) {
        session.getEventBus().register(this);
        this.session = session;
    }

    @EventHandler
    public void onScreenshotEvent(ScreenshotEvent event) {
        SocialService.ScreenshotForm form = new SocialService.ScreenshotForm(
                event.getFile(),
                event.getName(),
                event.getDescription(),
                Session.get().isLoggedIn() ? Session.get().player.getX() : -1,
                Session.get().isLoggedIn() ? Session.get().player.getY() : -1,
                new Timestamp(System.currentTimeMillis()));
        /*socialService.uploadScreenshot(form, (success) -> {
            if (success) {
                QuickChatPlugin.sendThroughChatBox("<col=ff0000>Uploaded screenshot to the 07Kit Cloud.</col>",
                        "",
                        "",
                        MessageEvent.Type.MESSAGE_SERVER_FILTERED,
                        false);
            } else {
                QuickChatPlugin.sendThroughChatBox("<col=ff0000>Couldn't upload screenshot to the 07Kit Cloud - if this happens again contact support@07kit.com</col>",
                        "",
                        "",
                        MessageEvent.Type.MESSAGE_SERVER_FILTERED,
                        false);
            }
        });*/
    }

    @EventHandler
    public void onLevelUp(LevelUpEvent event) {
        SocialService.ScreenshotForm screenshotForm = new SocialService.ScreenshotForm(
                event.getScreenshot(),
                "Level up - " + event.getSkill().getName() + " is now " + event.getLevel(),
                "Level up - " + event.getSkill().getName() + " is now " + event.getLevel(),
                Session.get().isLoggedIn() ? Session.get().player.getX() : -1,
                Session.get().isLoggedIn() ? Session.get().player.getY() : -1,
                new Timestamp(System.currentTimeMillis()));

        SocialService.EventForm eventForm = new SocialService.EventForm(SocialService.EventForm.EventType.LEVEL_UP, 0,
                "Level up - " + event.getSkill().getName() + " is now " + event.getLevel(),
                "Level up - " + event.getSkill().getName() + " is now " + event.getLevel());

        socialService.uploadScreenshotEvent(eventForm, screenshotForm, (success) -> {
            if (success) {
                QuickChatPlugin.sendThroughChatBox("<col=ff0000>Uploaded level-up screenshot to the 07Kit Cloud.</col>",
                        "",
                        "",
                        MessageEvent.Type.MESSAGE_SERVER_FILTERED,
                        false);
            } else {
                QuickChatPlugin.sendThroughChatBox("<col=ff0000>Couldn't upload level-up screenshot to the 07Kit Cloud - if this happens again contact support@07kit.com</col>",
                        "",
                        "",
                        MessageEvent.Type.MESSAGE_SERVER_FILTERED,
                        false);
            }
        });
    }

}
