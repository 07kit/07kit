package com.kit.plugins.skills.mining;

/**
 */
public enum RockType {

    CLAY(new short[]{ 6705 }, new int[]{ 7454, 7487 }),
    COPPER(new short[]{ 4645 }, new int[]{ 7484, 7453 }),
    TIN(new short[]{ 53 }, new int[]{ 7485, 7486 }),
    IRON(new short[]{ 2576 }, new int[]{ 7455 }),
    SILVER(new short[]{ 74 }, new int[]{ 7490 }),
    COAL(new short[]{ 10508 }, new int[]{ 7456, 7489 }),
    GOLD(new short[]{ 8885 }, new int[]{ 7458, 7491 }),
    MITHRIL(new short[]{ -22239 }, new int[]{ 7459, 7492 }),
    ADAMANTITE(new short[]{ 21662 }, new int[]{ 7460 }),
    RUNITE(new short[]{ -31437 }, new int[]{});

    private short[] colors;
    private int[] ids;
    private boolean show = false;

    RockType(short[] colors, int[] ids) {
        this.colors = colors;
        this.ids = ids;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public short[] getColors() {
        return colors;
    }

    public int[] getIds() {
        return ids;
    }

    public String getName() {
        return name().charAt(0) + name().substring(1).toLowerCase().replaceAll("_", " ");
    }
}
