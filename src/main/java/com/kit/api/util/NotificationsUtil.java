package com.kit.api.util;

import ch.swingfx.twinkle.NotificationBuilder;
import com.kit.Application;
import org.apache.log4j.Logger;
import com.kit.Application;


/**
 */
public class NotificationsUtil {

    public static void showNotification(String title, String message) {
        new NotificationBuilder()
                .withStyle(Application.COLOUR_SCHEME.getNotificationStyle())
                .withTitle(title).withMessage(message).showNotification();
    }

}
