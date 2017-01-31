package com.kit.gui.component;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.spi.LoggingEvent;

/**
 * @author : const_
 */
public class Appender extends ConsoleAppender {


    public Appender(Layout layout) {
        super(layout, "System.out");
    }

    @Override
    public void append(LoggingEvent event) {
        if (event.getLevel() == Level.DEBUG) {
            return;
        }
        super.append(event);
    }
}
