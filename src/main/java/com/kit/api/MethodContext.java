package com.kit.api;


import com.kit.api.impl.HiscoresImpl;
import com.kit.api.impl.LocalPlayer;
import com.kit.api.impl.game.*;
import com.kit.api.impl.scene.ObjectsImpl;
import com.kit.api.impl.tabs.*;
import com.kit.core.Session;
import com.kit.game.engine.IClient;
import com.kit.api.impl.HiscoresImpl;
import com.kit.api.impl.LocalPlayer;
import com.kit.api.impl.LoggerImpl;
import com.kit.api.impl.UserInterfaceImpl;
import com.kit.api.impl.scene.LootsImpl;
import com.kit.api.impl.scene.NpcsImpl;
import com.kit.api.impl.scene.ObjectsImpl;
import com.kit.api.impl.scene.PlayersImpl;
import com.kit.core.Session;
import com.kit.core.input.impl.MouseImpl;
import com.kit.game.engine.IClient;
import com.kit.api.impl.HiscoresImpl;
import com.kit.core.input.impl.MouseImpl;

/**
 * A collection class for everything that is made available
 * to the script in terms of methods and object instances.
 *
 */
public class MethodContext {

    public static final int FIXED_VIEWPORT_WIDTH = 512;
    public static final int FIXED_VIEWPORT_HEIGHT = 334;


    public Logger logger = new LoggerImpl();


    public LocalPlayer player = new LocalPlayer(this);


    public Viewport viewport = new ViewportImpl(this);


    public Widgets widgets = new WidgetsImpl(this);


    public Players players = new PlayersImpl(this);


    public Npcs npcs = new NpcsImpl(this);


    public Objects objects = new ObjectsImpl(this);


    public Loots loot = new LootsImpl(this);


    public Menu menu = new MenuImpl(this);


    public Worlds worlds = new WorldsImpl(this);


    public Inventory inventory = new InventoryImpl(this);


    public Equipment equipment = new EquipmentImpl(this);


    public Skills skills = new SkillsImpl(this);


    public Tabs tabs = new TabsImpl(this);


    public Camera camera = new CameraImpl(this);


    public Bank bank = new BankImpl(this);


    public Magic magic = new MagicImpl(this);


    public Minimap minimap = new MinimapImpl(this);


    public Mouse mouse = new MouseImpl(this);


    public Settings settings = new SettingsImpl(this);

    public Shops shops = new ShopsImpl(this);


    public Prayers prayers = new PrayersImpl(this);


    public Screen screen = new ScreenImpl(this);

    public Hiscores hiscores = new HiscoresImpl(this);

    public Composites composites = new CompositesImpl(this);

    public UserInterface ui = new UserInterfaceImpl();

    public Game game = new GameImpl(this);

    public Friends friends = new FriendsImpl(this);

    public ClanChat clanChat = new ClanChatImpl(this);

    public IClient client() {
        return Session.get().getClient();
    }

    public boolean inResizableMode() {
        return client().getViewportWidth() != FIXED_VIEWPORT_WIDTH ||
                client().getViewportHeight() != FIXED_VIEWPORT_HEIGHT;
    }

    public boolean isLoggedIn() {
        return client() != null &&
                client().getLoginIndex() >= 30 &&
                widgets.find(378, 6) == null;
    }

}
