package com.kit.plugins.cluescrolls;

import com.kit.api.wrappers.Tile;
import com.kit.api.wrappers.Area;
import com.kit.api.wrappers.Tile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public enum ClueScroll {

	SCROLL_2678(2678, ClueScrollLevel.EASY, ClueScrollType.COORDINATE, new Tile(3228, 3212, 1), new Area(new Tile(3227, 3217, 0),
			new Tile(3230, 3211, 0)), new String[]{"Ladder"}, null),
	SCROLL_2681(2681, ClueScrollLevel.EASY, ClueScrollType.SPEECH, null, new Area(new Tile(3202, 3236, 0),
			new Tile(3226, 3203, 0)), null, null, "Talk to Hans", "Lumbridge castle courtyard"),
	SCROLL_2694(2694, ClueScrollLevel.EASY, ClueScrollType.OBJECT_INTERACTION, new Tile(2969, 3311, 0)),
	SCROLL_2696(2696, ClueScrollLevel.EASY, ClueScrollType.SPEECH, null, new Area(new Tile(3043, 3259, 0), new Tile(3056, 3255, 0)),
			null, null, "Talk to Port Sarim bartender"),
	SCROLL_2698(2698, ClueScrollLevel.EASY, ClueScrollType.SPEECH, null, new Area(new Tile(2950, 3452, 0), new Tile(2953, 3449, 0)),
			null, null, "Talk to Doric", "North of Falador"),
	SCROLL_2699(2699, ClueScrollLevel.EASY, ClueScrollType.SPEECH, null, new Area(new Tile(2881, 3453, 0), new Tile(2888, 3446, 0)),
			null, null, "Talk to Gaius", "Taverly 2h sword store"),
	SCROLL_2703(2703, ClueScrollLevel.EASY, ClueScrollType.COORDINATE, new Tile(2748, 3495, 2), new Area(new Tile(2746, 3495, 0),
			new Tile(2751, 2490, 0)), null, null, "Second floor of Camelot castle"),
	SCROLL_2711(2711, ClueScrollLevel.EASY, ClueScrollType.COORDINATE, new Tile(2612, 3304, 1), new Area(new Tile(2609, 3307, 0),
			new Tile(2612, 3304, 0)), null, null, "Second floor of Ardougne church"),
	SCROLL_2713(2713, ClueScrollLevel.EASY, ClueScrollType.COORDINATE, new Tile(3166, 3360, 0)),
	SCROLL_2716(2716, ClueScrollLevel.EASY, ClueScrollType.COORDINATE, new Tile(3290, 3374, 0)),
	SCROLL_2719(2719, ClueScrollLevel.EASY, ClueScrollType.COORDINATE, new Tile(3043, 3399, 0)),
	SCROLL_2722(2722, ClueScrollLevel.HARD, ClueScrollType.COORDINATE, new Tile(3309, 3503, 0), ClueScrollPlugin.SPADE_CHECK),
	SCROLL_2725(2725, ClueScrollLevel.HARD, ClueScrollType.COORDINATE, new Tile(2988, 3963, 0)),
	SCROLL_2727(2727, ClueScrollLevel.HARD, ClueScrollType.COORDINATE, new Tile(3158, 3958, 0)),
	SCROLL_2729(2729, ClueScrollLevel.HARD, ClueScrollType.COORDINATE, new Tile(3190, 3963, 0)),
	SCROLL_2731(2731, ClueScrollLevel.HARD, ClueScrollType.COORDINATE, new Tile(3290, 3889, 0)),
	SCROLL_2733(2733, ClueScrollLevel.HARD, ClueScrollType.COORDINATE, new Tile(3141, 3804, 0)),
	SCROLL_2735(2735, ClueScrollLevel.HARD, ClueScrollType.COORDINATE, new Tile(2946, 3818, 0)),
	SCROLL_2737(2737, ClueScrollLevel.HARD, ClueScrollType.COORDINATE, new Tile(3013, 3846, 0)),
	SCROLL_2739(2739, ClueScrollLevel.HARD, ClueScrollType.COORDINATE, new Tile(3039, 3960, 0)),
	SCROLL_2743(2743, ClueScrollLevel.HARD, ClueScrollType.COORDINATE, new Tile(3168, 3678, 0), ClueScrollPlugin.SPADE_CHECK),
	SCROLL_2745(2745, ClueScrollLevel.HARD, ClueScrollType.COORDINATE, new Tile(3055, 3696, 0), ClueScrollPlugin.SPADE_CHECK),
	SCROLL_2773(2773, ClueScrollLevel.HARD, ClueScrollType.COORDINATE, new Tile(3219, 9617, 0), new Area(new Tile(3206, 3213, 0),
			new Tile(3212, 3216, 0)), null, null, "Lumbridge kitchen basement"),
	SCROLL_2774(2774, ClueScrollLevel.HARD, ClueScrollType.COORDINATE, new Tile(3088, 3470, 0)),
	SCROLL_2776(2776, ClueScrollLevel.HARD, ClueScrollType.COORDINATE, new Tile(3191, 9825, 0), new Area(new Tile(3185, 3435, 0),
			new Tile(3191, 3431, 0)), ClueScrollPlugin.SPADE_CHECK, null, null, "Varrock west bank basement"),
	SCROLL_2778(2778, ClueScrollLevel.HARD, ClueScrollType.SPEECH, null, new Area(new Tile(3009, 3231, 0), new Tile(3019, 3218, 0)),
			null, null, "Talk to Gerrant"),
	SCROLL_2780(2780, ClueScrollLevel.HARD, ClueScrollType.COORDINATE, new Tile(3085, 3255, 0)),
	SCROLL_2783(2783, ClueScrollLevel.HARD, ClueScrollType.COORDINATE, new Tile(2598, 3268, 0), ClueScrollPlugin.SPADE_CHECK),
	SCROLL_2786(2786, ClueScrollLevel.HARD, ClueScrollType.COORDINATE, new Tile(3174, 3663, 0)),
	SCROLL_2790(2790, ClueScrollLevel.HARD, ClueScrollType.OBJECT_INTERACTION, new Tile(3162, 9905, 0), null, null, null,
			"Varrock Sewers, Cauldron near Moss giants"),
	SCROLL_2792(2792, ClueScrollLevel.HARD, ClueScrollType.SPEECH, null, new Area(new Tile(3204, 3234, 0), new Tile(3222, 3205, 0)),
			null, null, "Talk to Hans"),
	SCROLL_2801(2801, ClueScrollLevel.MEDIUM, ClueScrollType.COORDINATE, new Tile(3161, 3251, 0), ClueScrollPlugin.SPADE_CHECK),
	SCROLL_2803(2803, ClueScrollLevel.MEDIUM, ClueScrollType.COORDINATE, new Tile(2680, 3111, 0), ClueScrollPlugin.SPADE_CHECK),
	SCROLL_2807(2807, ClueScrollLevel.MEDIUM, ClueScrollType.COORDINATE, new Tile(2383, 3369, 0), ClueScrollPlugin.SPADE_CHECK),
	SCROLL_2813(2813, ClueScrollLevel.MEDIUM, ClueScrollType.COORDINATE, new Tile(2642, 3253, 0)),
	SCROLL_2815(2815, ClueScrollLevel.MEDIUM, ClueScrollType.COORDINATE, new Tile(2849, 3297, 0), ClueScrollPlugin.SPADE_CHECK),
	SCROLL_2819(2819, ClueScrollLevel.MEDIUM, ClueScrollType.COORDINATE, new Tile(3007, 3143, 0)),
	SCROLL_2821(2821, ClueScrollLevel.MEDIUM, ClueScrollType.COORDINATE, new Tile(2920, 3403, 0)),
	SCROLL_2827(2827, ClueScrollLevel.MEDIUM, ClueScrollType.COORDINATE, new Tile(3093, 3225, 0)),
	SCROLL_2831(2831, ClueScrollLevel.MEDIUM, ClueScrollType.KEY, new Tile(3256, 3487, 0), null, null, null,
			"Kill monk for key at Ardougne Monastery"),
	SCROLL_2833(2833, ClueScrollLevel.MEDIUM, ClueScrollType.KEY, new Tile(2574, 3326, 1), new Area(new Tile(2572, 3325, 0),
			new Tile(2577, 3317, 0)), null, null, "Kill a dog at Handelmort Mansion for key",
			"Drawers upstairs Ardougne bar"),
	SCROLL_2839(2839, ClueScrollLevel.MEDIUM, ClueScrollType.KEY, null, new Area(new Tile(2587, 3110, 0), new Tile(2600, 3100, 0)),
			new String[]{"Ladder", "Closed chest"}, null, "Kill a man for the key"),
	SCROLL_2841(2841, ClueScrollLevel.MEDIUM, ClueScrollType.SPEECH, null, new Area(new Tile(2674, 3089, 0), new Tile(2680, 3084, 0)),
			null, null, "Talk to Hazelmere", "Peninsula east of Yanille", "Answer: 6859"),
	SCROLL_2848(2848, ClueScrollLevel.MEDIUM, ClueScrollType.SPEECH, null, new Area(new Tile(2776, 3215, 0), new Tile(2780, 3209, 0)),
			null, null, "Talk to Hajedy", "Brimhaven cart operator"),
	SCROLL_2849(2849, ClueScrollLevel.MEDIUM, ClueScrollType.SPEECH, null, new Area(new Tile(3271, 3183, 0), new Tile(3275, 3179, 0)),
			null, null, "Talk to Karim", "Al-Kharid kebab stand", "Answer: 5"),
	SCROLL_2851(2851, ClueScrollLevel.MEDIUM, ClueScrollType.SPEECH, null, new Area(new Tile(3009, 3504, 0), new Tile(3018, 3499, 0)),
			null, null, "Talk to Oracle", "Ice Mountain summit", "Answer: 48"),
	SCROLL_2853(2853, ClueScrollLevel.MEDIUM, ClueScrollType.SPEECH, null, new Area(new Tile(2381, 3496, 0), new Tile(2409, 3478, 0)),
			null, null, "Talk to a Referee", "Gnome Stronghold gnomeball arena", "Answer: 5096"),
	SCROLL_2858(2858, ClueScrollLevel.MEDIUM, ClueScrollType.SPEECH, null, new Area(new Tile(2935, 3155, 0), new Tile(2941, 3151, 0)),
			null, null, "Talk to Luthas", "Karamja banana plantation owner", "Answer: 33"),
	SCROLL_2873(2873, ClueScrollLevel.MEDIUM, ClueScrollType.KEY, new Tile(2709, 3478, 0), null, null, null,
			"Kill a chicken for the key"),
	SCROLL_3490(3490, ClueScrollLevel.EASY, ClueScrollType.OBJECT_INTERACTION, new Tile(2929, 3570, 0)),
	SCROLL_3501(3501, ClueScrollLevel.EASY, ClueScrollType.COORDINATE, new Tile(3308, 3206, 0)),
	SCROLL_3503(3503, ClueScrollLevel.EASY, ClueScrollType.OBJECT_INTERACTION, new Tile(2885, 3540, 0)),
	SCROLL_3506(3506, ClueScrollLevel.EASY, ClueScrollType.COORDINATE, new Tile(2636, 3453, 0)),
	SCROLL_3508(3508, ClueScrollLevel.EASY, ClueScrollType.OBJECT_INTERACTION, new Tile(2716, 3471, 1), new Area(new Tile(2710, 3473, 0),
			new Tile(3715, 3470, 0)), null, null, "Search the drawers upstairs"),
	SCROLL_3509(3509, ClueScrollLevel.EASY, ClueScrollType.COORDINATE, new Tile(2699, 3470, 0)),
	SCROLL_3516(3516, ClueScrollLevel.EASY, ClueScrollType.COORDINATE, new Tile(2612, 3482, 0)),
	SCROLL_3518(3518, ClueScrollLevel.EASY, ClueScrollType.COORDINATE, new Tile(3110, 3151, 0)),
	SCROLL_3520(3520, ClueScrollLevel.HARD, ClueScrollType.COORDINATE, new Tile(2617, 3076, 0)),
	SCROLL_3522(3522, ClueScrollLevel.HARD, ClueScrollType.COORDINATE, new Tile(2488, 3308, 0), ClueScrollPlugin.SPADE_CHECK),
	SCROLL_3524(3524, ClueScrollLevel.HARD, ClueScrollType.COORDINATE, new Tile(2457, 3182, 0)),
	SCROLL_3525(3525, ClueScrollLevel.HARD, ClueScrollType.OBJECT_INTERACTION, new Tile(3026, 3628, 0)),
	SCROLL_3526(3526, ClueScrollLevel.HARD, ClueScrollType.COORDINATE, new Tile(2892, 3674, 0)),
	SCROLL_3532(3532, ClueScrollLevel.HARD, ClueScrollType.COORDINATE, new Tile(2775, 2891, 0), null, Arrays.stream(new int[][]{ClueScrollPlugin.MACHETES, ClueScrollPlugin.AXES, ClueScrollPlugin.SPADE_CHECK})
			.flatMapToInt(IntStream::of).toArray(),
			null, null, "Southwest Karamja through Kharazi Jungle", "Requires Legends' Quest started"),
	SCROLL_3534(3534, ClueScrollLevel.HARD, ClueScrollType.COORDINATE, new Tile(2839, 2915, 0)),
	SCROLL_3536(3536, ClueScrollLevel.HARD, ClueScrollType.COORDINATE, new Tile(2950, 2902, 0)),
	SCROLL_3548(3548, ClueScrollLevel.HARD, ClueScrollType.COORDINATE, new Tile(2580, 3029, 0), new Area(new Tile(2497, 2992, 0),
			new Tile(2502, 2986, 0)), null, null),
	SCROLL_3554(3554, ClueScrollLevel.HARD, ClueScrollType.COORDINATE, new Tile(3374, 3250, 0), null, ClueScrollPlugin.SPADE_CHECK, null, null, "Duel with obstacles enabled"),
	SCROLL_3556(3556, ClueScrollLevel.HARD, ClueScrollType.COORDINATE, new Tile(3095, 3765, 0)),
	SCROLL_3558(3558, ClueScrollLevel.HARD, ClueScrollType.COORDINATE, new Tile(3285, 3942, 0), ClueScrollPlugin.SPADE_CHECK),
	SCROLL_3562(3562, ClueScrollLevel.HARD, ClueScrollType.COORDINATE, new Tile(2182, 3205, 0)),
	SCROLL_3566(3566, ClueScrollLevel.HARD, ClueScrollType.SPEECH, null, new Area(new Tile(3358, 3345, 0), new Tile(3366, 3337, 0)),
			null, null, "Talk to a digsite Examiner"),
	SCROLL_3575(3575, ClueScrollLevel.HARD, ClueScrollType.SPEECH, null, new Area(new Tile(2488, 3489, 1), new Tile(2493, 3485, 1)),
			null, null, "Talk to Heckel Funch", "Second floor of Grand Tree"),
	SCROLL_3577(3577, ClueScrollLevel.HARD, ClueScrollType.SPEECH, null, new Area(new Tile(2470, 3431, 0), new Tile(2478, 3424, 0)),
			null, null, "Talk to Gnome trainer", "Gnome Stronghold agility course"),
	SCROLL_3580(3580, ClueScrollLevel.HARD, ClueScrollType.COORDINATE, new Tile(2832, 9586, 0), new Area(new Tile(2855, 3170, 0),
			new Tile(2858, 3166, 0)), null, null, "Inside Karamja Volcano", "Red Spider's egg spawn"),
	SCROLL_3584(3584, ClueScrollLevel.MEDIUM, ClueScrollType.COORDINATE, new Tile(3430, 3389, 0)),
	SCROLL_3586(3586, ClueScrollLevel.MEDIUM, ClueScrollType.COORDINATE, new Tile(2920, 3535, 0)),
	SCROLL_3588(3588, ClueScrollLevel.MEDIUM, ClueScrollType.COORDINATE, new Tile(2888, 3153, 0)),
	SCROLL_3596(3596, ClueScrollLevel.MEDIUM, ClueScrollType.COORDINATE, new Tile(2906, 3294, 0)),
	SCROLL_3598(3598, ClueScrollLevel.MEDIUM, ClueScrollType.OBJECT_INTERACTION, new Tile(2658, 3488, 0)),
	SCROLL_3599(3599, ClueScrollLevel.MEDIUM, ClueScrollType.COORDINATE, new Tile(2650, 3230, 0)),
	SCROLL_3601(3601, ClueScrollLevel.MEDIUM, ClueScrollType.COORDINATE, new Tile(2565, 3248, 0)),
	SCROLL_3602(3602, ClueScrollLevel.MEDIUM, ClueScrollType.COORDINATE, new Tile(2924, 3209, 0), ClueScrollPlugin.SPADE_CHECK),
	SCROLL_3604(3604, ClueScrollLevel.MEDIUM, ClueScrollType.COORDINATE, new Tile(2800, 3074, 0)),
	SCROLL_3609(3609, ClueScrollLevel.MEDIUM, ClueScrollType.OBJECT_INTERACTION, new Tile(3498, 3507, 0)),
	SCROLL_3612(3612, ClueScrollLevel.MEDIUM, ClueScrollType.SPEECH, null, new Area(new Tile(2402, 3421, 0), new Tile(2406, 3417, 0)),
			null, null, "Talk to Brimstail", "Cave in southwest Gnome Stronghold"),
	SCROLL_3615(3615, ClueScrollLevel.MEDIUM, ClueScrollType.SPEECH, null, new Area(new Tile(3498, 3473, 0), new Tile(3496, 3471, 0)),
			null, null, "Talk to Roavar", "Canafis pub"),
	SCROLL_3616(3616, ClueScrollLevel.MEDIUM, ClueScrollType.SPEECH, null, new Area(new Tile(3355, 3278, 0), new Tile(3362, 3271, 0)),
			null, null, "Talk to Jaraah", "Duel Arena surgeon"),
	SCROLL_3617(3617, ClueScrollLevel.MEDIUM, ClueScrollType.SPEECH, null, new Area(new Tile(2789, 3187, 0), new Tile(2798, 3182, 0)),
			null, null, "Talk to Kangai Mau", "Brimhaven pub"),
	SCROLL_3618(3618, ClueScrollLevel.MEDIUM, ClueScrollType.SPEECH, null, new Area(new Tile(2627, 3000, 0), new Tile(2633, 2996, 0)),
			null, null, "Talk to Fycie", "Rantz' cave in Feldip Hills"),
	SCROLL_7239(7239, ClueScrollLevel.HARD, ClueScrollType.COORDINATE, new Tile(3022, 3912, 0)),
	SCROLL_7241(7241, ClueScrollLevel.HARD, ClueScrollType.COORDINATE, new Tile(2723, 3339, 0), ClueScrollPlugin.SPADE_CHECK),
	SCROLL_7248(7248, ClueScrollLevel.HARD, ClueScrollType.OBJECT_INTERACTION, new Tile(3178, 2987, 0)),
	SCROLL_7251(7251, ClueScrollLevel.HARD, ClueScrollType.OBJECT_INTERACTION, new Tile(3041, 9820, 0), null, null, null, "Dwarven mines"),
	SCROLL_7254(7254, ClueScrollLevel.HARD, ClueScrollType.COORDINATE, new Tile(3489, 2389, 0)),
	SCROLL_7262(7262, ClueScrollLevel.HARD, ClueScrollType.COORDINATE, new Tile(3113, 3602, 0)),
	SCROLL_7266(7266, ClueScrollLevel.HARD, ClueScrollType.COORDINATE, new Tile(2712, 3733, 0)),
	SCROLL_7268(7268, ClueScrollLevel.HARD, ClueScrollType.SPEECH, null, new Area(new Tile(2381, 3498, 0), new Tile(2411, 3480, 0)),
			null, null, "Talk to Gnome Coach", "Gnome Stronghold gnomeball arena", "Answer: 6"),
	SCROLL_7270(7270, ClueScrollLevel.HARD, ClueScrollType.SPEECH, null, new Area(new Tile(2523, 3164, 1),
			new Tile(2530, 3158, 1)), new String[]{"Ladder"}, null, "Talk to Bolkoy", "Tree Gnome Village general store", "Answer: 13"),
	SCROLL_7272(7272, ClueScrollLevel.HARD, ClueScrollType.SPEECH, null, new Area(new Tile(2804, 3197, 0), new Tile(2813, 3188, 0)),
			null, null, "Talk to Cap'n Izzy No Beard", "Brimhaven Agility Arena entrance", "Answer: 33"),
	SCROLL_7274(7274, ClueScrollLevel.MEDIUM, ClueScrollType.SPEECH, null, new Area(new Tile(2654, 3686, 0), new Tile(2663, 3664)),
			null, null, "Talk to Brundt the Chieftain", "Relekka longhall", "Answer: 4"),
	SCROLL_7276(7276, ClueScrollLevel.MEDIUM, ClueScrollType.SPEECH, null, new Area(new Tile(2788, 3069, 0), new Tile(2803, 3062, 0)),
			null, null, "Talk to Gabooty", "Tai Bwo Wannai Village", "Answer: 11"),
	SCROLL_7278(7278, ClueScrollLevel.MEDIUM, ClueScrollType.SPEECH, null, new Area(new Tile(2533, 3310, 0), new Tile(2544, 3300, 0)),
			null, null, "Talk to Recruiter", "West Ardougne city square", "Answer: 20"),
	SCROLL_7288(7288, ClueScrollLevel.MEDIUM, ClueScrollType.COORDINATE, new Tile(3434, 3266, 0)),
	SCROLL_7294(7294, ClueScrollLevel.MEDIUM, ClueScrollType.COORDINATE, new Tile(2667, 3561, 0)),
	SCROLL_7296(7296, ClueScrollLevel.MEDIUM, ClueScrollType.KEY, new Tile(3353, 3332, 0), null, null, null,
			"Kill a barbarian for the key"),
	SCROLL_7303(7303, ClueScrollLevel.MEDIUM, ClueScrollType.OBJECT_INTERACTION, new Tile(3289, 3022, 0)),
	SCROLL_7307(7307, ClueScrollLevel.MEDIUM, ClueScrollType.COORDINATE, new Tile(2583, 2990, 0)),
	SCROLL_7317(7317, ClueScrollLevel.MEDIUM, ClueScrollType.COORDINATE, new Tile(2874, 3047, 0)),
	SCROLL_10192(10192, ClueScrollLevel.EASY, ClueScrollType.EMOTE, new Tile(3044, 3236, 0), new String[]{"Cheer"}),
	SCROLL_10200(10200, ClueScrollLevel.EASY, ClueScrollType.EMOTE, new Tile(3110, 3294, 0), new String[]{"Dance"}),
	SCROLL_10218(10218, ClueScrollLevel.EASY, ClueScrollType.EMOTE, new Tile(2982, 3276, 0), new String[]{"Spin"}),
	SCROLL_10228(10228, ClueScrollLevel.EASY, ClueScrollType.EMOTE, new Tile(3362, 3341, 0), new String[]{"Clap"}),
	SCROLL_10234(10234, ClueScrollLevel.HARD, ClueScrollType.EMOTE, new Tile(3240, 3610, 0), new String[]{"Shrug"}),
	SCROLL_10236(10236, ClueScrollLevel.HARD, ClueScrollType.EMOTE, new Tile(2588, 3419, 0), new String[]{"Raspberry"}),
	SCROLL_10238(10238, ClueScrollLevel.HARD, ClueScrollType.EMOTE, null, new Area(new Tile(2503, 3645, 0), new Tile(2514, 3634, 0)),
			new String[]{"Staircase"}, new String[]{"Bow"}),
	SCROLL_10240(10240, ClueScrollLevel.HARD, ClueScrollType.EMOTE, new Tile(3614, 3490, 0), new String[]{"Panic"}),
	SCROLL_10242(10242, ClueScrollLevel.HARD, ClueScrollType.EMOTE, new Tile(3294, 2781, 0), new String[]{"Dance"}), // addy 2h, ring of life, uncharged glory
	SCROLL_10244(10244, ClueScrollLevel.HARD, ClueScrollType.EMOTE, new Tile(3026, 3701, 0), new String[]{"Yawn"}),
	SCROLL_10246(10246, ClueScrollLevel.HARD, ClueScrollType.EMOTE, new Tile(2920, 3163, 0), new String[]{"Salute"}),
	SCROLL_10248(10248, ClueScrollLevel.HARD, ClueScrollType.EMOTE, new Tile(2812, 3680, 0), new String[]{"Laugh"}),
	SCROLL_10250(10250, ClueScrollLevel.HARD, ClueScrollType.EMOTE, new Tile(2848, 3498, 0), new String[]{"Panic"}),
	SCROLL_10252(10252, ClueScrollLevel.HARD, ClueScrollType.EMOTE, new Tile(2852, 2953, 0), new String[]{"Blow Kiss"}),
	SCROLL_10254(10254, ClueScrollLevel.MEDIUM, ClueScrollType.EMOTE, new Tile(3495, 3490, 0), new String[]{"Dance", "Bow"}),
	SCROLL_10262(10262, ClueScrollLevel.MEDIUM, ClueScrollType.EMOTE, new Tile(2547, 3554, 0), new String[]{"Yawn", "Shrug"}),
	SCROLL_10264(10264, ClueScrollLevel.MEDIUM, ClueScrollType.EMOTE, new Tile(2547, 3554, 0), new String[]{"Cheer", "Headbang"}),
	SCROLL_10268(10268, ClueScrollLevel.MEDIUM, ClueScrollType.EMOTE, new Tile(2611, 3092, 0), new String[]{"Jump for Joy", "Jig"}),
	SCROLL_10270(10270, ClueScrollLevel.MEDIUM, ClueScrollType.EMOTE, new Tile(2439, 3161, 0), new String[]{"Think", "Spin"}),
	SCROLL_10272(10272, ClueScrollLevel.MEDIUM, ClueScrollType.EMOTE, new Tile(2528, 3376, 0), new String[]{"Cheer", "Angry"}),
	SCROLL_10276(10276, ClueScrollLevel.MEDIUM, ClueScrollType.EMOTE, new Tile(2823, 3443, 0), new String[]{"Cry", "Bow"}),
	SCROLL_10278(10278, ClueScrollLevel.MEDIUM, ClueScrollType.EMOTE, new Tile(3304, 3124, 0), new String[]{"Jig", "Bow"}),
	SCROLL_12021(12021, ClueScrollLevel.MEDIUM, ClueScrollType.EMOTE, new Tile(3169, 9571, 0), new Area(new Tile(3166, 3175, 0),
			new Tile(3174, 3170, 0)), null, new String[]{"Dance", "Blow Kiss"}),
	SCROLL_12027(12027, ClueScrollLevel.MEDIUM, ClueScrollType.EMOTE, new Tile(2849, 3430, 0), new String[]{"Cry", "Laugh"}),
	SCROLL_12033(12033, ClueScrollLevel.MEDIUM, ClueScrollType.COORDINATE, new Tile(2593, 2899, 0), ClueScrollPlugin.SPADE_CHECK),
	SCROLL_12039(12039, ClueScrollLevel.MEDIUM, ClueScrollType.COORDINATE, new Tile(2681, 3653, 0), ClueScrollPlugin.SPADE_CHECK),
	SCROLL_12041(12041, ClueScrollLevel.MEDIUM, ClueScrollType.COORDINATE, new Tile(2322, 3060, 0), ClueScrollPlugin.SPADE_CHECK),
	SCROLL_12047(12047, ClueScrollLevel.MEDIUM, ClueScrollType.COORDINATE, new Tile(2363, 3531, 0)),
	SCROLL_12073(12073, ClueScrollLevel.VERY_HARD, ClueScrollType.EMOTE, null, new Area(new Tile(2527, 3295, 0), new Tile(2532, 3285, 0)),
			null, new String[]{"Spin"}),
	SCROLL_12074(12074, ClueScrollLevel.VERY_HARD, ClueScrollType.EMOTE, new Tile(2375, 3851, 0), new String[]{"Jump for Joy"}),
	SCROLL_12075(12075, ClueScrollLevel.VERY_HARD, ClueScrollType.EMOTE, new Tile(3068, 3858, 0), new String[]{"Blow Kiss"}),
	SCROLL_12076(12076, ClueScrollLevel.VERY_HARD, ClueScrollType.EMOTE, new Tile(2630, 5069, 0), new Area(new Tile(2544, 3424, 0), new Tile(2550, 3417, 0)),
			new String[]{"Ladder"}, new String[]{"Cheer"}, "Bring a ring of visibility"),
	SCROLL_12078(12078, ClueScrollLevel.VERY_HARD, ClueScrollType.EMOTE, null, new Area(new Tile(3056, 3484, 0), new Tile(3058, 3482, 0)),
			new String[]{"Ladder"}, new String[]{"Bow"}),
	SCROLL_12079(12079, ClueScrollLevel.VERY_HARD, ClueScrollType.EMOTE, new Tile(2783, 3275, 0), new Area(new Tile(2718, 3305, 0), new Tile(2723, 3300, 0)), null,
			new String[]{"Dance"}, "Talk to Jeb to travel"),
	SCROLL_12080(12080, ClueScrollLevel.VERY_HARD, ClueScrollType.EMOTE, null, new Area(new Tile(3418, 3542, 2), new Tile(3422, 3539, 2)), null,
			new String[]{"Headbang"}, "Wear nose peg / earmuffs for protection"),
	SCROLL_12082(12082, ClueScrollLevel.VERY_HARD, ClueScrollType.EMOTE, new Tile(2916, 9894, 0), new Area(new Tile(2891, 3508, 0), new Tile(2893, 3506, 0)),
			null, new String[]{"Laugh"}),
	SCROLL_12083(12083, ClueScrollLevel.VERY_HARD, ClueScrollType.EMOTE, new Tile(3419, 3540, 2), new String[]{"Headbang"}),
	SCROLL_12088(12088, ClueScrollLevel.VERY_HARD, ClueScrollType.COORDINATE, new Tile(3828, 2848, 0)),
	SCROLL_12094(12094, ClueScrollLevel.VERY_HARD, ClueScrollType.COORDINATE, new Tile(2359, 3799, 0), ClueScrollPlugin.SPADE_CHECK),
	SCROLL_12095(12095, ClueScrollLevel.VERY_HARD, ClueScrollType.COORDINATE, new Tile(3188, 3933, 0), ClueScrollPlugin.SPADE_CHECK),
	SCROLL_12098(12098, ClueScrollLevel.VERY_HARD, ClueScrollType.COORDINATE, new Tile(2511, 2980, 0), ClueScrollPlugin.SPADE_CHECK),
	SCROLL_12099(12099, ClueScrollLevel.VERY_HARD, ClueScrollType.COORDINATE, new Tile(3225, 2838, 0), ClueScrollPlugin.SPADE_CHECK),
	SCROLL_12100(12100, ClueScrollLevel.VERY_HARD, ClueScrollType.COORDINATE, new Tile(3302, 2988, 0), ClueScrollPlugin.SPADE_CHECK),
	SCROLL_12103(12103, ClueScrollLevel.VERY_HARD, ClueScrollType.COORDINATE, new Tile(3573, 3425, 0)),
	SCROLL_12104(12104, ClueScrollLevel.VERY_HARD, ClueScrollType.COORDINATE, new Tile(2778, 3678, 0), ClueScrollPlugin.SPADE_CHECK),
	SCROLL_12106(12106, ClueScrollLevel.VERY_HARD, ClueScrollType.COORDINATE, new Tile(2936, 2721, 0), null, ClueScrollPlugin.SPADE_CHECK, null, null,
			"Travel via gnome glider hanger", "Talk to Daero in the Grand Tree bar"),
	SCROLL_12107(12107, ClueScrollLevel.VERY_HARD, ClueScrollType.COORDINATE, new Tile(2870, 2997, 0), ClueScrollPlugin.SPADE_CHECK),
	SCROLL_12108(12108, ClueScrollLevel.VERY_HARD, ClueScrollType.COORDINATE, new Tile(3603, 3564, 0)),
	SCROLL_12109(12109, ClueScrollLevel.VERY_HARD, ClueScrollType.COORDINATE, new Tile(3215, 3835, 0), null, ClueScrollPlugin.SPADE_CHECK, null, null,
			"Bring anti-fire"),
	SCROLL_12110(12110, ClueScrollLevel.VERY_HARD, ClueScrollType.COORDINATE, new Tile(2820, 3078, 0), null, null, null,
			"Tai Bwo Wannai hardwood grove", "100 trading sticks"),
	SCROLL_12111(12111, ClueScrollLevel.VERY_HARD, ClueScrollType.COORDINATE, new Tile(2180, 3282, 0), ClueScrollPlugin.SPADE_CHECK),
	SCROLL_12113(12113, ClueScrollLevel.VERY_HARD, ClueScrollType.SPEECH, null, ClueScrollPlugin.SHERLOCK_AREA, null, null, ClueScrollPlugin.SHERLOCK_MESSAGE),
	SCROLL_12114(12114, ClueScrollLevel.VERY_HARD, ClueScrollType.SPEECH, null, ClueScrollPlugin.SHERLOCK_AREA, null, null, ClueScrollPlugin.SHERLOCK_MESSAGE),
	SCROLL_12117(12117, ClueScrollLevel.VERY_HARD, ClueScrollType.SPEECH, null, ClueScrollPlugin.SHERLOCK_AREA, null, null, ClueScrollPlugin.SHERLOCK_MESSAGE),
	SCROLL_12118(12118, ClueScrollLevel.VERY_HARD, ClueScrollType.SPEECH, null, ClueScrollPlugin.SHERLOCK_AREA, null, null, ClueScrollPlugin.SHERLOCK_MESSAGE),
	SCROLL_12119(12119, ClueScrollLevel.VERY_HARD, ClueScrollType.SPEECH, null, ClueScrollPlugin.SHERLOCK_AREA, null, null, ClueScrollPlugin.SHERLOCK_MESSAGE),
	SCROLL_12120(12120, ClueScrollLevel.VERY_HARD, ClueScrollType.SPEECH, null, ClueScrollPlugin.SHERLOCK_AREA, null, null, ClueScrollPlugin.SHERLOCK_MESSAGE),
	SCROLL_12121(12121, ClueScrollLevel.VERY_HARD, ClueScrollType.SPEECH, null, ClueScrollPlugin.SHERLOCK_AREA, null, null, ClueScrollPlugin.SHERLOCK_MESSAGE),
	SCROLL_12123(12123, ClueScrollLevel.VERY_HARD, ClueScrollType.SPEECH, null, ClueScrollPlugin.SHERLOCK_AREA, null, null, ClueScrollPlugin.SHERLOCK_MESSAGE),
	SCROLL_12126(12126, ClueScrollLevel.VERY_HARD, ClueScrollType.SPEECH, null, ClueScrollPlugin.SHERLOCK_AREA, null, null, ClueScrollPlugin.SHERLOCK_MESSAGE),
	SCROLL_12127(12127, ClueScrollLevel.VERY_HARD, ClueScrollType.SPEECH, null, ClueScrollPlugin.SHERLOCK_AREA, null, null, ClueScrollPlugin.SHERLOCK_MESSAGE),
	SCROLL_12130(12130, ClueScrollLevel.VERY_HARD, ClueScrollType.COORDINATE, new Tile(2449, 3130, 0)),
	SCROLL_12133(12133, ClueScrollLevel.VERY_HARD, ClueScrollType.SPEECH, new Tile(2736, 5352, 1), new Area(new Tile(2733, 5353, 0), new Tile(2739, 5350, 0)),
			null, null, "Talk to Ambassador Alvijar", "Northeast Dorgesh-Kaan", "Answer: 2505"),
	SCROLL_12134(12134, ClueScrollLevel.VERY_HARD, ClueScrollType.SPEECH, null, new Area(new Tile(3182, 3946, 0), new Tile(3184, 3945, 0)),
			null, null, "Talk to Mandrith", "Wilderness Resource Arena", "Answer: 28"),
	SCROLL_12135(12135, ClueScrollLevel.VERY_HARD, ClueScrollType.SPEECH, null, new Area(new Tile(2320, 3183, 0), new Tile(2331, 3172, 0)),
			null, null, "Talk to Oronwen", "Lletya clothing store", "Answer: 20"),
	SCROLL_12136(12136, ClueScrollLevel.VERY_HARD, ClueScrollType.SPEECH, null, new Area(new Tile(3293, 3234, 0), new Tile(3302, 3224, 0)),
			null, null, "Talk to Cam the Camel", "Outside Duel Arena", "Answer: 6"),
	SCROLL_12138(12138, ClueScrollLevel.VERY_HARD, ClueScrollType.SPEECH, null, new Area(new Tile(3460, 3560, 0), new Tile(3463, 3556, 0)),
			null, null, "Talk to the Old Crone", "Shack east of Slayer Tower", "Answer: 619"),
	SCROLL_12140(12140, ClueScrollLevel.VERY_HARD, ClueScrollType.SPEECH, null, new Area(new Tile(2539, 3327, 0), new Tile(2556, 3317, 0)),
			null, null, "Bring mourner gear", "Mourner household basement", "Talk to Head Mourner"),
	SCROLL_14146(14146, ClueScrollLevel.VERY_HARD, ClueScrollType.COORDINATE, new Tile(3410, 3324, 0), null, ClueScrollPlugin.SPADE_CHECK, null, null, "Fairy Ring: B I P"),
	SCROLL_12147(12147, ClueScrollLevel.VERY_HARD, ClueScrollType.SPEECH, null, new Area(new Tile(3094, 3470, 0), new Tile(3099, 3466, 0)),
			null, null, "Talk to Vannaka", "Edgeville dungeon"),
	SCROLL_12150(12150, ClueScrollLevel.VERY_HARD, ClueScrollType.COORDINATE, new Tile(3049, 4839, 0), null, ClueScrollPlugin.SPADE_CHECK, null, null, "Law rift in abyss"),
	SCROLL_12151(12151, ClueScrollLevel.VERY_HARD, ClueScrollType.SPEECH, null, new Area(new Tile(3240, 3210, 0), new Tile(3247, 3204, 0)),
			null, null, "Talk to Father Aereck", "Lumbridge Church"),
	SCROLL_12152(12152, ClueScrollLevel.VERY_HARD, ClueScrollType.SPEECH, null, new Area(new Tile(2795, 3442, 0), new Tile(2803, 3435, 0)),
			null, null, "Talk to Catherby Candle Maker"),
	SCROLL_12153(12153, ClueScrollLevel.VERY_HARD, ClueScrollType.SPEECH, new Tile(3371, 9320, 0), new Area(new Tile(3370, 2909, 0), new Tile(3376, 2900, 0)),
			null, null, "Talk to the Genie west of Nardah", "Bring a rope and a light source"),
	SCROLL_12154(12154, ClueScrollLevel.VERY_HARD, ClueScrollType.SPEECH, null, new Area(new Tile(2631, 3314, 0), new Tile(2639, 3310, 0)),
			null, null, "Talk to Horacio", "East Ardougne gardener"),
	SCROLL_12159(12159, ClueScrollLevel.VERY_HARD, ClueScrollType.SPEECH, null, new Area(new Tile(3023, 3383, 0), new Tile(3031, 3375, 0)),
			null, null, "Talk to Wyson", "Falador garden gardener"),
	SCROLL_12162(12162, ClueScrollLevel.EASY, ClueScrollType.EMOTE, new Tile(3213, 3463, 0), new String[]{"Spin"}),
	SCROLL_12168(12168, ClueScrollLevel.EASY, ClueScrollType.COORDINATE, new Tile(3083, 3261, 0), new Area(new Tile(3083, 3261, 0),
			new Tile(3088, 3256, 0)), null, null, "Search Aggie's wardrobe", "Draynor Village"),
	SCROLL_12176(12176, ClueScrollLevel.EASY, ClueScrollType.COORDINATE, new Tile(3187, 9825, 0), new Area(new Tile(3186, 3434, 0),
			new Tile(3190, 3432, 0)), null, null, (String[]) null),
	SCROLL_12179(12179, ClueScrollLevel.EASY, ClueScrollType.COORDINATE, new Tile(3299, 3290, 0)),
	SCROLL_12186(12186, ClueScrollLevel.EASY, ClueScrollType.SPEECH, new Area(new Tile(3030, 3296, 0), new Tile(3040, 3290, 0))),
	SCROLL_12190(12190, ClueScrollLevel.EASY, ClueScrollType.SPEECH, null, new Area(new Tile(2921, 3409, 0), new Tile(2929, 3401, 0)),
			null, null, "Talk to the Lady of the Lake", "Outside Taverly"),
	SCROLL_12542(12542, ClueScrollLevel.HARD, ClueScrollType.EMOTE, new Tile(3362, 3341, 0), new String[]{"Headbang"}),
	SCROLL_12554(12554, ClueScrollLevel.HARD, ClueScrollType.COORDINATE, new Tile(3081, 3209, 0), null, null, null,
			"Fairy ring: C L P"),
	SCROLL_12556(12556, ClueScrollLevel.HARD, ClueScrollType.COORDINATE, new Tile(2504, 3899, 0), null, null, null,
			"Fairy ring: A J S"),
	SCROLL_12564(12564, ClueScrollLevel.HARD, ClueScrollType.COORDINATE, new Tile(3301, 3697, 0)),
	SCROLL_12566(12566, ClueScrollLevel.HARD, ClueScrollType.SPEECH, null, new Area(new Tile(3752, 5669, 0), new Tile(3758, 5662, 0)),
			null, null, "Talk to Prospector Percy", "Motherlode Mine", "Answer: 12"),
	SCROLL_12568(12568, ClueScrollLevel.HARD, ClueScrollType.SPEECH, null, new Area(new Tile(3564, 3312, 0), new Tile(3566, 3307, 0)),
			null, null, "Talk to Strange Old Man", "Gravedigger at Barrows", "Answer: 40"),
	SCROLL_12570(12570, ClueScrollLevel.HARD, ClueScrollType.SPEECH, null, new Area(new Tile(2904, 3542, 0), new Tile(2915, 3535, 0)),
			null, null, "Talk to Martin Thwait", "Rogue's Den", "Answer: 2"),
	SCROLL_12572(12572, ClueScrollLevel.HARD, ClueScrollType.SPEECH, null, new Area(new Tile(3087, 3254, 0), new Tile(3094, 3250, 0)),
			null, null, "Talk to Wise Old Man", "Answer: 28"),
	SCROLL_12574(12574, ClueScrollLevel.HARD, ClueScrollType.SPEECH, null, new Area(new Tile(2600, 3211, 0), new Tile(2612, 3204, 0)),
			null, null, "Talk to Brother Omad", "Monastery south of Ardougne", "Answer: 129"),
	SCROLL_12576(12576, ClueScrollLevel.HARD, ClueScrollType.SPEECH, null, new Area(new Tile(3223, 3236, 0), new Tile(3238, 3222, 0)),
			null, null, "Talk to Doomslayer", "In front of Lumbridge castle", "Answer: 95"),
	SCROLL_12578(12578, ClueScrollLevel.HARD, ClueScrollType.SPEECH, null, new Area(new Tile(2741, 3157, 0), new Tile(2749, 3150, 0)),
			null, null, "Talk to Saniboch", "Brimhaven Dungeon entrance"),
	SCROLL_12581(12581, ClueScrollLevel.HARD, ClueScrollType.SPEECH, null, new Area(new Tile(2855, 3437, 0), new Tile(2865, 3429, 0)),
			null, null, "Talk to Elena", "Northwest East Ardougne"),
	SCROLL_12584(12584, ClueScrollLevel.HARD, ClueScrollType.EMOTE, new Tile(2480, 3046, 0), new String[]{"Jig"}),
	SCROLL_12587(12587, ClueScrollLevel.HARD, ClueScrollType.SPEECH, null, new Area(new Tile(3281, 2801, 0), new Tile(3296, 2786, 0)),
			null, null, "Talk to Guardian mummy", "Pyramid Plunder minigame");

	protected int id;
	private ClueScrollLevel level;
	private ClueScrollType type;
	private Tile location;
	private Area area;
	private int[] itemWarnings;
	private String[] objects;
	private String[] emotes;
	private String[] messages;

	private static final Map<Integer, ClueScroll> ID_MAP = new HashMap<>();

	static {
		for (ClueScroll s : values()) {
			ID_MAP.put(s.id, s);
		}
	}

	public static ClueScroll forId(int id) {
		return ID_MAP.get(id);
	}

	ClueScroll(int id, ClueScrollLevel level, ClueScrollType type, Tile location, Area area, int[] itemWarnings, String[] objects,
			   String[] emotes, String... messages) {
		this.id = id;
		this.level = level;
		this.type = type;
		this.location = location;
		this.area = area;
		this.itemWarnings = itemWarnings;
		this.objects = objects;
		this.emotes = emotes;
		if (Objects.equals(type, ClueScrollType.COORDINATE) && messages != null) {
			this.messages = new String[messages.length + 1];
			System.arraycopy(messages, 0, this.messages, 0, messages.length);
			this.messages[messages.length] = "Bring a spade"; // TODO remove this in favor of visual warnings
		} else {
			this.messages = messages;
		}
	}

	ClueScroll(int id, ClueScrollLevel level, ClueScrollType type, Tile location, Area area, String[] objects, String[] emotes, String... messages) {
		this(id, level, type, location, area, null, objects, emotes, messages);
	}

	ClueScroll(int id, ClueScrollLevel level, ClueScrollType type, Tile location, int[] itemWarnings) {
		this(id, level, type, location, null, itemWarnings, null, null, (String[]) null);
	}

	ClueScroll(int id, ClueScrollLevel level, ClueScrollType type, Tile location) {
		this(id, level, type, location, null, null, null, (String[]) null);
	}

	ClueScroll(int id, ClueScrollLevel level, ClueScrollType type, Tile location, String[] emotes) {
		this(id, level, type, location, null, null, emotes, (String[]) null);
	}

	ClueScroll(int id, ClueScrollLevel level, ClueScrollType type, Area area) {
		this(id, level, type, null, area, null, null, (String[]) null);
	}

	public int getId() {
		return id;
	}

	public ClueScrollLevel getLevel() {
		return level;
	}

	public ClueScrollType getType() {
		return type;
	}

	public Tile getLocation() {
		return location;
	}

	public Area getArea() {
		return area;
	}

	public int[] getItemWarnings() {
		return itemWarnings;
	}

	public String[] getObjects() {
		return objects;
	}

	public String[] getEmotes() {
		return emotes;
	}

	public String[] getMessages() {
		return messages;
	}

	public static Map<Integer, ClueScroll> getIdMap() {
		return ID_MAP;
	}
}
