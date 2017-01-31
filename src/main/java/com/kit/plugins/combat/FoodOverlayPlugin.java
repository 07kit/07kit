package com.kit.plugins.combat;

import com.kit.api.plugin.Plugin;
import com.kit.api.util.PaintUtils;
import com.kit.api.wrappers.Widget;
import com.kit.api.event.EventHandler;
import com.kit.api.event.PaintEvent;
import com.kit.api.plugin.Plugin;
import com.kit.api.util.PaintUtils;
import com.kit.api.wrappers.Tab;
import com.kit.api.wrappers.Widget;
import com.kit.api.wrappers.WidgetItem;
import com.kit.core.control.PluginManager;
import com.kit.api.event.EventHandler;
import com.kit.api.event.PaintEvent;
import com.kit.api.plugin.Plugin;
import com.kit.api.wrappers.Widget;
import com.kit.api.wrappers.WidgetItem;
import com.kit.core.control.PluginManager;

import javax.swing.colorchooser.ColorChooserComponentFactory;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

/**
 */
public class FoodOverlayPlugin extends Plugin {
    private FoodItem currentFoodItem;
    private String[] foodItemNames;

    public FoodOverlayPlugin(PluginManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "Food";
    }

    @Override
    public String getGroup() {
        return "Combat";
    }

    @Override
    public void start() {
        foodItemNames = new String[FoodItem.values().length];
        for (int i = 0; i < foodItemNames.length; i++) {
            FoodItem item = FoodItem.values()[i];
            foodItemNames[i] = item.name;
        }
    }

    @Override
    public void stop() {

    }

    @EventHandler
    public void onPaintEvent(PaintEvent event) {
        if (!isLoggedIn())
            return;

        Widget inv = widgets.find(149, 0);
        if (!inv.isValid())
            return;

        if (currentFoodItem == null)
            return;

        Graphics2D gfx = (Graphics2D) event.getGraphics().create();

        String healthBoost = String.format("+%d", currentFoodItem.healthBoost);
        gfx.setFont(gfx.getFont().deriveFont(Font.BOLD));
        int width = gfx.getFontMetrics().stringWidth(healthBoost);

        Rectangle box = new Rectangle(mouse.getPosition().x - (width + 10), (mouse.getPosition().y - gfx.getFontMetrics().getHeight()), width + 10, 18);

        gfx.setColor(new Color(75, 67, 54, 255));
        gfx.fillRect(box.x, box.y, box.width, box.height);

        gfx.setColor(Color.BLACK);
        gfx.drawRect(box.x, box.y, box.width, box.height);

        Color textColor = Color.GREEN;
        if (player != null && (currentFoodItem.healthBoost + player.getCurrentHealth()) > player.getMaxHealth())
            textColor = Color.RED;

        gfx.setColor(textColor);

        PaintUtils.drawString(gfx, String.format("+%d", currentFoodItem.healthBoost), box.x + 5, box.y + 14);
        gfx.dispose();
    }

    @EventHandler
    public void onMouseEvent(MouseEvent event) {
        if (!isLoggedIn()) {
            return;
        }

        Widget bounds = inventory.getWidget();
        if (bounds != null && bounds.getArea().contains(event.getX(), event.getY())) {
            Optional<WidgetItem> hoverTarget = inventory.find().nameContains(foodItemNames)
                    .asList()
                    .stream()
                    .filter(x -> x.getArea().contains(event.getX(), event.getY()))
                    .findFirst();

            if (hoverTarget.isPresent()) {
                WidgetItem widget = hoverTarget.get();
                currentFoodItem = FoodItem.forName(widget.getName());
            } else {
                currentFoodItem = null;
            }
        } else { // aint got nuffin'
            currentFoodItem = null;
        }
    }


    private enum FoodItem {

        CABBAGE("Cabbage", 1),
        CAKE("Cake|2/3 cake|Slice of cake", 4),
        SARDINE("Sardine", 4),
        SHRIMPS("Shrimps", 3),
        ANCHOVIES("Anchovies", 1),
        COOKED_CHICKEN("Cooked chicken", 4),
        COOKED_MEAT("Cooked meat", 4),
        CHOCOLATE_CAKE("Chocolate cake|2/3 chocolate cake|Chocolate slice", 5),
        BREAD("Bread", 5),
        HERRING("Herring", 5),
        TROUT("Trout", 7),
        SALMON("Salmon", 9),
        TUNA("Tuna", 10),
        LOBSTER("Lobster", 12),
        SWORDFISH("Swordfish", 14),
        MONKFISH("Monkfish", 16),
        SHARK("Shark", 20),
        MANTA_RAY("Manta Ray", 22),
        TUNA_POTATO("Tuna potato", 22),
        DARK_CRAB("Dark crab", 22),
        PLAIN_PIZZA("Plain pizza", 7),
        MEAT_PIZZA("Meat pizza", 8),
        ANCHOVY_PIZZA("Anchovy pizza", 9),
        PINEAPPLE_PIZZA("Pineapple pizza", 11),
        REDBERRY_PIE("Redberry pie", 5),
        MEAT_PIE("Meat pie", 6),
        APPLE_PIE("Apple pie", 7),
        GARDEN_PIE("Garden pie", 6),
        FISH_PIE("Fish pie", 6),
        ADMIRAL_PIE("Admiral pie", 8),
        WILD_PIE("Wild pie", 11),
        SUMMER_PIE("Summer pie", 11),
        COOKED_KARAMBWAN("Cooked karambwan", 18),
        PEACH("Peach", 8),
        POTATO_WITH_CHEESE("Potato with cheese", 16),
        BASS("Bass", 13),
        TRIANGLE_SANDWICH("Triangle sandwich", 6),
        PINEAPPLE_RING("Pineapple ring", 2),
        UGHTHANKI_KEBAB("Ughthanki kebab", 19),
        FRIED_MUSHROOMS("Fried mushrooms", 5),
        PIKE("Pike", 8),
        CURRY("Curry", 19),
        EDIBLE_SEAWEED("Edible seaweed", 4);

        String name;
        int healthBoost;

        FoodItem(String name, int healthBoost) {
            this.name = name;
            this.healthBoost = healthBoost;
        }

        public static FoodItem forName(String name) {
            for (FoodItem item : FoodItem.values()) {
                if (name.startsWith(item.name)) {
                    return item;
                }
            }
            return null;
        }
    }
}
