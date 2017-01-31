package com.kit.api.wrappers;

import com.kit.api.MethodContext;
import com.kit.api.MethodContext;

/**
 * A simple class which represents a World ingame.
 *
 * * @author tobiewarburton
 */
public class World {

    private String country;
    private int worldNumber;
    private int players;
    private boolean members;
    private String activity;
    private String domain;

    private final MethodContext ctx;

    public World(MethodContext ctx, String activity, boolean members, int players, int worldNumber, String country, String domain) {
        this.ctx = ctx;
        this.activity = activity;
        this.members = members;
        this.players = players;
        this.worldNumber = worldNumber;
        this.country = country;
        this.domain = domain;
    }

    public String getDomain() {
        return domain;
    }

    public String getActivity() {
        return activity;
    }

    public int getId() {
        return worldNumber;
    }

    public boolean isMembers() {
        return members;
    }

    public int getPlayers() {
        return players;
    }

    public String getCountry() {
        return country;
    }

    @Override
    public String toString() {
        return "World{" +
                "country='" + country + '\'' +
                ", worldNumber=" + worldNumber +
                ", players=" + players +
                ", members=" + members +
                ", activity='" + activity + '\'' +
                ", domain='" + domain + '\'' +
                ", ctx=" + ctx +
                '}';
    }
}
