package com.kit.api.event;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.kit.api.wrappers.Loot;
import com.kit.game.engine.IBitBuffer;
import com.kit.game.engine.extension.CanvasExtension;
import org.apache.log4j.Logger;
import org.apache.log4j.net.SyslogAppender;
import com.kit.api.event.exeption.HandlerInvocationException;
import com.kit.api.wrappers.Loot;
import com.kit.core.Session;

import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.reflect.Modifier.isStatic;

/**
 * A self-mapping Events.
 *
 */
public class Events {

    private final ExecutorService asyncExecutorService = Executors.newSingleThreadExecutor();
    private final Multimap<Class<?>, EventHandlerBridge> typedMappings = ArrayListMultimap.create();

    /**
     * Register methods of an object that have the EventHandler annotation
     *
     * @param object object to be registered.
     */
    public void register(Object object) {
        Class<?> clazz = object.getClass();
        Set<Method> methods = new HashSet<>();
        methods.addAll(Arrays.asList(clazz.getMethods()));
        methods.addAll(Arrays.asList(clazz.getDeclaredMethods()));
        methods.forEach(x -> {
            handleMethod(object, x);
        });
    }

    private void handleMethod(Object object, Method method) {
        EventHandler annotation = method.getAnnotation(EventHandler.class);
        if (annotation != null) {
            EventHandlerBridge bridge = new EventHandlerBridge(object, method);
            if (bridge.validate()) {
                typedMappings.put(bridge.getType(), bridge);
            } else {
                throw new IllegalArgumentException("Invalid EventHandler: " + method.getName());
            }
        }
    }

    /**
     * On login all items are loaded, this takes a long time if we do something for each one.
     *
     * @param event
     */
    public void submitSpawnGroundItemEvent(SpawnGroundItemEvent event) {
        if (!Session.get().isLoggedIn()) {
            return;// or we never log in
        }
        List<Loot> loot = Session.get().loot.find().at(event.getX(), event.getY()).asList();
        if (loot != null) {
            event.setLoot(loot);
        }
        submit(event);
    }

    // Credit goes to UniquePassive, refered from https://github.com/Wingman/wingman
    public void submitPlayerRegionChange(IBitBuffer bitBuffer, int playerId) {
        try {
//            int startPosition = bitBuffer.getOffsetPos();
//
//            int type = bitBuffer.getBits(2);
//
//            if (type == 0) {
//                if (bitBuffer.getBits(1) != 0) {
//                    submitPlayerRegionChange(bitBuffer, playerId);
//                }
//
//                int deltaX = bitBuffer.getBits(6);
//                int deltaY = bitBuffer.getBits(6);
//
//                int currentPos = Session.get().getClient().getPlayerRegionInformation()[playerId];
//
//                int oldPlane = currentPos >> 28;
//                int oldX = currentPos >> 14 & 255;
//                int oldY =  currentPos & 255;
//
//                int newX = deltaX + oldX & 255;
//                int newY = deltaY + oldY & 255;
//
//                Session.get().getEventBus().submit(new PlayerRegionChangeEvent(
//                        playerId,
//                        PlayerRegionChangeEvent.Type.ADDED_TO_LOCAL,
//                        oldX, oldY, oldPlane,
//                        newX, newY, oldPlane)
//                );
//            } else if (type == 1) {
//                int deltaPlane = bitBuffer.getBits(2);
//
//                int currentPos = Session.get().getClient().getPlayerRegionInformation()[playerId];
//
//                int oldPlane = currentPos >> 28;
//                int oldX = currentPos >> 14 & 255;
//                int oldY =  currentPos & 255;
//
//                int newPlane = deltaPlane + oldPlane & 3;
//
//                Session.get().getEventBus().submit(new PlayerRegionChangeEvent(
//                        playerId,
//                        PlayerRegionChangeEvent.Type.PLANE_CHANGE,
//                        oldX, oldY, oldPlane,
//                        oldX, oldY, newPlane)
//                );
//            } else if (type == 2) {
//                int data = bitBuffer.getBits(5);
//
//                int deltaPlane = data >> 3;
//                int direction = data & 7;
//
//                int currentPos = Session.get().getClient().getPlayerRegionInformation()[playerId];
//
//                int oldPlane = currentPos >> 28;
//                int oldX = currentPos >> 14 & 255;
//                int oldY =  currentPos & 255;
//
//                int newPlane = deltaPlane + oldPlane & 3;
//                int newX = oldX;
//                int newY = oldY;
//
//                if(direction == 0) {
//                    --newX;
//                    --newY;
//                }
//
//                if(direction == 1) {
//                    --newY;
//                }
//
//                if(direction == 2) {
//                    ++newX;
//                    --newY;
//                }
//
//                if(direction == 3) {
//                    --newX;
//                }
//
//                if(direction == 4) {
//                    ++newX;
//                }
//
//                if(direction == 5) {
//                    --newX;
//                    ++newY;
//                }
//
//                if(direction == 6) {
//                    ++newY;
//                }
//
//                if(direction == 7) {
//                    ++newX;
//                    ++newY;
//                }
//
//                Session.get().getEventBus().submit(new PlayerRegionChangeEvent(
//                        playerId,
//                        PlayerRegionChangeEvent.Type.ADJACENT_REGION,
//                        oldX, oldY, oldPlane,
//                        newX, newY, newPlane)
//                );
//            } else {
//                int data = bitBuffer.getBits(18);
//
//                int deltaPlane = data >> 16;
//                int deltaX = data >> 8 & 255;
//                int deltaY = data & 255;
//
//                int currentPos = Session.get().getClient().getPlayerRegionInformation()[playerId];
//
//                int oldPlane = currentPos >> 28;
//                int oldX = currentPos >> 14;
//                int oldY =  currentPos & 255;
//
//                int newPlane = deltaPlane + oldPlane & 3;
//                int newX = deltaX + oldX & 255;
//                int newY = deltaY + currentPos & 255;
//
//                Session.get().getEventBus().submit(new PlayerRegionChangeEvent(
//                        playerId,
//                        PlayerRegionChangeEvent.Type.NONADJACENT_REGION,
//                        oldX, oldY, oldPlane,
//                        newX, newY, newPlane)
//                );
//            }
//
//            bitBuffer.setOffsetPos(startPosition);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * On login all items are loaded, this takes a long time if we do something for each one.
     *
     * @param event
     */
    public void submitSpawnInteractableObjectEvent(SpawnInteractableObjectEvent event) {
        if (!Session.get().isLoggedIn()) {
            return;// or we never log in
        }
        submit(event);
    }

    public void submitMessageEvent(MessageEvent event) {
        if (event.getSender().trim().length() == 0 && event.getType() == MessageEvent.Type.MESSAGE_SERVER
                && event.getMessage().equals("Welcome to RuneScape.")) {
            submit(new LoginEvent(System.currentTimeMillis()));
            return;
        } else if (Session.get().isLoggedIn() &&
                event.getSender() != null &&
                event.getSender().length() > 0 &&
                event.getMessage().toLowerCase().contains(Session.get().player.getName().toLowerCase())) {
            submit(new PlayerMentionEvent(event));
            return;
        }

        submit(event);
    }

    //Paint and buffer flips need to exec instantly
    public void submitPaintEvent(PaintEvent event) {
        fireEvent(event);
    }

    public void submitBufferFlipEvent(BufferFlipEvent event) {
        fireEvent(event);
    }

    public void submitPlayerMenuCreatedEvent(PlayerMenuCreatedEvent event) {
        //fireEvent(event);
    }

    public void submitNpcMenuCreatedEvent(NpcMenuCreatedEvent event) {
        //fireEvent(event);
    }

    /**
     * Submit an object to be handled by the appropriate EventHandler.
     *
     * @param object object to be handled.
     */
    public void submit(final Object object) {
        asyncExecutorService.submit(() -> fireEvent(object));
    }


    private void fireEvent(Object object) {
        typedMappings.get(object.getClass()).forEach(bridge -> bridge.handle(object));
    }

    /**
     * Deregister any EventHandlers of the object.
     *
     * @param object object to remove the listeners of.
     */
    public void deregister(Object object) {
        // TODO: Optimize this.
        typedMappings.values().removeIf(bridge -> bridge.method.getDeclaringClass().equals(object.getClass()));
    }

    public void submitMouseEvent(MouseEvent e) {
        fireEvent(e);
    }

    /**
     * Internal wrapper class to help map EventHandler methods to the correct event.
     */
    private static class EventHandlerBridge {
        private Object object;
        private Method method;

        public EventHandlerBridge(Object object, Method method) {
            this.object = object;
            this.method = method;
        }

        public Class<?> getType() {
            return method.getParameterTypes()[0];
        }

        /**
         * Checks if the method is a valid EventHandler
         *
         * @return true if it is a valid EventHandler else false.
         */
        public boolean validate() {
            return method.getParameterTypes().length == 1 && !isStatic(method.getModifiers());
        }

        /**
         * Calls the EventHandler method and passes the object as the argument.
         *
         * @param arg object to pass into the method.
         */
        public void handle(Object arg) {
            try {
                method.invoke(this.object, arg);
            } catch (Exception e) {
                throw new HandlerInvocationException("Handler invocation failed.", e);
            }
        }

        @Override
        public String toString() {
            return "Object: " + object.getClass() + " Method: " + method.toString();
        }
    }
}