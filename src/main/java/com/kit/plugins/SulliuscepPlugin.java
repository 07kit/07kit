package com.kit.plugins;

import com.kit.api.event.EventHandler;
import com.kit.api.event.MessageEvent;
import com.kit.api.event.PaintEvent;
import com.kit.api.plugin.Plugin;
import com.kit.api.plugin.Schedule;
import com.kit.api.util.NotificationsUtil;
import com.kit.api.wrappers.Area;
import com.kit.api.wrappers.GameObject;
import com.kit.api.wrappers.Npc;
import com.kit.api.wrappers.Tile;
import com.kit.core.Session;
import com.kit.core.control.PluginManager;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

import static com.kit.api.wrappers.GameObjectType.INTERACTABLE;

public class SulliuscepPlugin extends Plugin{
    static int TAR_BUBBLES=7801;
    static int TAR_MONSTER=7804;
    //static int[] CHOPPABLE_OBJECTS=new int[]{30644,30645,30646,30648};    //Some objects are useless to chop and some objects are 2-3 tiles big so searching by id is not the best
    private static final List<Tile> CHOPPABLE_TILES=Arrays.asList(  //There are only 10 important objects
            new Tile(Session.get(),3679,3743,0),
            new Tile(Session.get(),3678,3743,0),

            new Tile(Session.get(),3670,3746,0),
            new Tile(Session.get(),3670,3747,0),
            new Tile(Session.get(),3669,3746,0),
            new Tile(Session.get(),3669,3747,0),

            new Tile(Session.get(),3671,3760,0),
            new Tile(Session.get(),3671,3761,0),
            new Tile(Session.get(),3672,3760,0),
            new Tile(Session.get(),3672,3761,0),

            new Tile(Session.get(),3672,3764,0),
            new Tile(Session.get(),3672,3765,0),
            new Tile(Session.get(),3673,3764,0),
            new Tile(Session.get(),3673,3765,0),

            new Tile(Session.get(),3674,3771,0),
            new Tile(Session.get(),3675,3771,0),

            new Tile(Session.get(),3672,3780,0),
            new Tile(Session.get(),3672,3781,0),
            new Tile(Session.get(),3673,3780,0),
            new Tile(Session.get(),3673,3781,0),

            new Tile(Session.get(),3666,3788,0),
            new Tile(Session.get(),3666,3789,0),
            new Tile(Session.get(),3667,3788,0),
            new Tile(Session.get(),3667,3789,0),

            new Tile(Session.get(),3670,3792,0),
            new Tile(Session.get(),3670,3793,0),
            new Tile(Session.get(),3671,3792,0),
            new Tile(Session.get(),3671,3793,0),

            new Tile(Session.get(),3670,3792,0),
            new Tile(Session.get(),3670,3793,0),
            new Tile(Session.get(),3671,3792,0),
            new Tile(Session.get(),3671,3793,0),

            new Tile(Session.get(),3672,3802,0),
            new Tile(Session.get(),3672,3801,0)
            );


    private boolean active;
    private static final Area PLUGIN_AREA=new Area(3661,3728,3692-3661+1,3814-3728+1,0);

    public SulliuscepPlugin(PluginManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "Sulliuscep";
    }
    @Override
    public void start() {
        active=false;
    }
    @Override
    public void stop() {

    }

    @Schedule(1500)
    public void checkIdleState() {
        Tile pos=player.getTile();
        if (isLoggedIn() && PLUGIN_AREA.contains(pos)) {
            active=true;
        } else {
            active=false;
        }

    }

    @EventHandler
    public void onPaintEvent(PaintEvent event) {
        Graphics2D g2d = (Graphics2D) event.getGraphics().create();
        //Debug(g2d);
        if (!active&&!isLoggedIn()) {
            return;
        }
        g2d.setColor(new Color(255,255,255,64));
        for(Tile tile: CHOPPABLE_TILES){
            g2d.fill(tile.getPolygon());
        }
        g2d.setColor(new Color(255,0,0,128));
        for(Npc npc: npcs.find().distance(20).id(TAR_BUBBLES,TAR_MONSTER).asList()) {
            g2d.fill(npc.getTile().getPolygon());
        }
    }

    /*void Debug(Graphics2D g2d){
        int x=10;
        int y=100;
        g2d.setFont(g2d.getFont().deriveFont(10.0f));
        g2d.setColor(Color.GREEN);
        g2d.drawString("Debug",x,y);
        y+=12;
        g2d.drawString("active: "+active,x,y);
    }*/
}
