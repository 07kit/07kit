package com.kit.plugins.map;

import com.kit.api.event.EventHandler;
import com.kit.api.event.LoginEvent;
import com.kit.api.event.PlayerRegionChangeEvent;
import com.kit.api.wrappers.Region;
import com.kit.api.wrappers.Tile;
import com.kit.core.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//TODO load all regions on start
// Only adjacent/local regions work
//Optimize this
public class RegionTracker {

    private List<Region> regions;

    public RegionTracker() {
        regions = new ArrayList<>();
        for (int x = 0; x < 100; x ++) {
            for (int y = 0; y < 175; y ++) {
                Region region = new Region(
                        x,
                        y
                );
                regions.add(region);
            }
        }
    }

//    @EventHandler
    public void onPlayerRegionChange(PlayerRegionChangeEvent evt) {
        if (Session.get().getClient().getLoginIndex() < 30) {
            return;
        }
        Tile loc = Session.get().player.getTile();
        if (loc == null) {
            return;
        }

        Region newRegion = null;

        switch (evt.getType()) {
            case ADDED_TO_LOCAL:
                newRegion = regionForTile(Math.round(loc.getX() / Region.WIDTH), Math.round(loc.getY() / Region.HEIGHT));
                break;
            case ADJACENT_REGION:
                newRegion = regionForTile(Math.round(loc.getX() / Region.WIDTH) + evt.getNewX(),
                        Math.round(loc.getY() / Region.HEIGHT) + evt.getNewY());
                break;
            case PLANE_CHANGE:
                return;
            case NONADJACENT_REGION:
                newRegion =regionForTile(evt.getNewX(), evt.getNewY());
                break;
        }

        Region oldRegion = regionForTile(evt.getOldX(), evt.getOldY());

        if (newRegion == oldRegion) {
            return;
        }

        if (oldRegion != null) {
            oldRegion.decrement();
        }

        if (newRegion == null) {
            return;
        }

        newRegion.increment();
    }

    private Region regionForTile(int x, int y) {
        for (Region region : regions) {
            if (region.isInRegion(x, y)) {
                return region;
            }
        }
        return null;
    }

    public List<Region> getRegions() {
        return regions;
    }
}
