package com.kit.game.engine;

import com.kit.game.engine.renderable.entity.INpc;
import com.kit.api.event.Events;
import com.kit.game.engine.collection.IBag;
import com.kit.game.engine.collection.ICache;
import com.kit.game.engine.collection.IDeque;
import com.kit.game.engine.input.ICanvas;
import com.kit.game.engine.input.IKeyboard;
import com.kit.game.engine.input.IMouse;
import com.kit.game.engine.media.IWidget;
import com.kit.game.engine.renderable.entity.INpc;
import com.kit.game.engine.renderable.entity.IPlayer;
import com.kit.game.engine.scene.ICollisionMap;
import com.kit.game.engine.scene.IScene;
import com.kit.api.event.Events;
import com.kit.game.engine.collection.IBag;
import com.kit.game.engine.collection.IDeque;
import com.kit.game.engine.media.IWidget;
import com.kit.game.engine.renderable.entity.INpc;
import com.kit.game.engine.scene.ICollisionMap;
import com.kit.game.engine.scene.IScene;

import java.awt.*;

public interface IClient {

    void addMenuEntry(String var0, String var1, int var2, int var3, int var4, int var5);

    void setEventBus(Events eventBus);

    void onMessageReceived(int type, String sender, String message, String clanName);

    IClanChatMember[] getClanChatMembers();

    IFriend[] getFriends();

    String getUsername();

    boolean loadWorlds();

    IBag getItemCache();

    void setUsername(String s);

    int getGameState();

    int getBaseX();

    int getBaseY();

    int getCameraPitch();

    int getCameraX();

    int getCameraY();

    int getCameraYaw();

    int getCameraZ();

    Canvas getCanvas();

    void setLoginIndex(int state);

    ICollisionMap[] getCollisionMap();

    int[] getGameSettings();

    IGrandExchangeOffer[] getGrandExchangeOffers();

    IKeyboard getKeyboard();

    IPlayer getLocalPlayer();

    int getLoginIndex();

    int getLoopCycle();

    IDeque[][][] getLoot();

    String[] getMenuActions();

    int getMenuCount();

    int getMenuHeight();

    boolean getMenuOpen();

    String[] getMenuOptions();

    int getMenuWidth();

    int getMenuX();

    int getMenuY();

    int getMinimapAngle();

    int getMinimapOffset();

    int getMinimapScale();

    IMouse getMouse();

    INpc[] getNpcs();

    int getPlane();

    IPlayer[] getPlayers();

    IDeque getProjectiles();

    int getScale();

    IScene getScene();

    int[] getSettings();

    int[] getSkillBases();

    int[] getSkillExperiences();

    int[] getSkillLevels();

    int[][][] getTileHeights();

    byte[][][] getTileSettings();

    int getViewportHeight();

    int getViewportWidth();

    int[] getWidgetBoundsHeight();

    int[] getWidgetBoundsWidth();

    int[] getWidgetBoundsX();

    int[] getWidgetBoundsY();

    IBag getWidgetNodeBag();

    IWidget[][] getWidgets();

    IWorld[] getWorlds();

    int[] getPlayerRegionInformation();

    IBitBuffer getPlayerRegionBuffer();

    boolean getFpsOn();

    void setWorldId(int id);

    void setWorldDomain(String domain);

    void setFpsOn(boolean on);

    void onPlayerRegionChange(IBitBuffer buffer, int playerId);
}
