package com.kit.plugins.skills.agility;

import com.kit.api.wrappers.Tile;
import com.kit.api.wrappers.Area;
import com.kit.api.wrappers.Tile;

/**
 * Created by Matt on 05/09/2016.
 */
enum AgilityCourse {
    //Normal courses
    GNOME_STRONGHOLD("Gnome Stronghold", false, 1, 46.5D, 86.5D,
            new Area[]{
                    new Area(new Tile(2493, 3445), new Tile(2461, 3409))
            },
            new Tile[]{
                    new Tile(2484, 3437), new Tile(2487, 3437)
            }, 23145, 23134, 23559, 23557, 23560, 23135, 23138, 23139
    ),
    AGILITY_PYRAMID("Agility Pyramid", false, 30, 300, 506.8D,
            new Area[]{
                    new Area(new Tile(3331, 2871), new Tile(3387, 2818)),
                    new Area(new Tile(3039, 4703), new Tile(3050, 4692))
            },
            new Tile[]{
                    new Tile(3364, 2830)
            }, 10857, 10865, 10860, 10868, 10882, 10886, 10884, 10859, 10861, 10888, 10855
    ),
    BARBARIAN_OUTPOST("Barbarian Outpost", false, 35, 60, 153.5D,
            new Area[]{
                    new Area(new Tile(2553, 3557), new Tile(2530, 3543)),
                    new Area(new Tile(2546, 9955), new Tile(2555, 9948))
            },
            new Tile[]{
                    new Tile(2542, 3553)
            },
            23131, 23144, 20211, 23547, 16682, 1948
    ),
    WILDERNESS_COURSE("Wilderness Course", false, 52, 498.9D, 571.4D,
            new Area[]{
                    new Area(new Tile(3010, 3970), new Tile(2990, 3930)),
                    new Area(new Tile(2992, 10339), new Tile(3007, 10365))
            },
            new Tile[]{
                    new Tile(2993, 3933), new Tile(2994, 3933), new Tile(2995, 3933)
            },
            23137, 23132, 23556, 23542, 23640
    ),
    //Rooftop courses
    DRAYNOR_VILLAGE("Draynor Village", true, 10, 79, 120,
            new Area[]{
                    new Area(new Tile(3114, 3289, 0), new Tile(3073, 3243, 3)),
            },
            new Tile[]{
                    new Tile(3103, 3261)
            },
            10073, 10074, 10075, 10077, 10084, 10085, 10086
    ),
    AL_KHARID("Al Kharid", true, 20, 30, 180,
            new Area[]{
                    new Area(new Tile(3265, 3199, 0), new Tile(3326, 3152, 3))
            },
            new Tile[]{
                    new Tile(3299, 3194)
            },
            10093, 10284, 10355, 10356, 10357, 10094, 10583, 10352
    ),
    VARROCK("Varrock", true, 30, 125, 238,
            new Area[]{
                    new Area(new Tile(3248, 3435, 0), new Tile(3178, 3377, 3))
            },
            new Tile[]{
                    new Tile(3236, 3417)
            },
            10586, 10587, 10642, 10777, 10778, 10779, 10780, 10781, 10817
    ),
    CANIFIS("Canifis", true, 40, 175, 240,
            new Area[]{
                    new Area(new Tile(3472, 3472, 0), new Tile(3515, 3515, 3))
            },
            new Tile[]{
                    new Tile(3510, 3485)
            },
            10819, 10820, 10821, 10828, 10822, 10831, 10823, 10832
    ),
    FALADOR("Falador", true, 50, 180, 440,
            new Area[]{
                    new Area(3009, 3330, 46, 38, 0),
                    new Area(3009, 3330, 46, 38, 1),
                    new Area(3009, 3330, 46, 38, 2),
                    new Area(3009, 3330, 46, 38, 3)
            },
            new Tile[]{
                    new Tile(3029, 3334),
                    new Tile(3029, 3333)
            },
            10833, 10834, 10836, 11161, 11360, 11361, 11364, 11365, 11366, 11367, 11368, 11369, 11370, 11371
    ),
    SEERS("Seers' Village", true, 60, 435, 635,
            new Area[]{
                    new Area(2694, 3459, 40, 40, 0),
                    new Area(2694, 3459, 40, 40, 1),
                    new Area(2694, 3459, 40, 40, 2),
                    new Area(2694, 3459, 40, 40, 3)
            },
            new Tile[]{
                    new Tile(2704, 3464)
            },
            11373, 11374, 11375, 11378, 11376, 11377
    ),
    POLLNIVNEACH("Pollnivneach", true, 70, 540, 890,
            new Area[]{
                    new Area(3346, 2960, 25, 45, 0),
                    new Area(3346, 2960, 25, 45, 1),
                    new Area(3346, 2960, 25, 45, 2),
                    new Area(3346, 2960, 25, 45, 3)
            },
            new Tile[]{
                    new Tile(3363, 2998)
            },
            11380, 11381, 11382, 11383, 11384, 11385, 11386, 11389, 11390
    ),
    RELLEKKA("Rellekka", true, 80, 475, 780,
            new Area[]{
                    new Area(new Tile(2608, 3684, 0), new Tile(2671, 3649, 3))
            },
            new Tile[]{
                    new Tile(2653, 3676)
            },
            11391, 11392, 11393, 11395, 11396, 11397, 11404
    ),
    ARDOUGNE("Ardougne", true, 90, 529, 793,
            new Area[]{
                    new Area(new Tile(2645, 3292, 0), new Tile(2675, 3323, 3))
            },
            new Tile[]{
                    new Tile(2668, 3297)
            },
            11405, 11406, 11631, 11429, 11430, 11633, 11630
    );


    private final String name;
    private final boolean rooftop;
    private final int level;
    private final double lastObstacleXp;
    private final double totalXp;
    private final Area[] areas;
    private final Tile[] finalTiles;
    private final int[] objects;

    AgilityCourse(String name, boolean rooftop, int level, double lastObstacleXp, double totalXp,
                          Area[] areas, Tile[] finalTiles, int... objects) {
        this.name = name;
        this.rooftop = rooftop;
        this.level = level;
        this.lastObstacleXp = lastObstacleXp;
        this.totalXp = totalXp;
        this.areas = areas;
        this.finalTiles = finalTiles;
        this.objects = objects;
    }

    @Override
    public String toString() {
        return name;
    }

    public boolean rooftop() {
        return rooftop;
    }

    public int level() {
        return level;
    }

    public double lastObstacleXp() {
        return lastObstacleXp;
    }

    public double totalXp() {
        return totalXp;
    }

    public Area[] areas() {
        return areas;
    }

    public Tile[] finalTiles() {
        return finalTiles;
    }

    public int[] objectIds() {
        return objects;
    }

}
