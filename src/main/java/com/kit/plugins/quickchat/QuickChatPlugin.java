package com.kit.plugins.quickchat;

import com.kit.api.MethodContext;
import com.kit.api.event.MessageEvent;
import com.kit.api.plugin.Plugin;
import com.kit.api.util.PriceLookup;
import com.kit.api.util.Utilities;
import com.kit.api.wrappers.Skill;
import com.kit.api.wrappers.hiscores.HiscoreLookup;
import com.kit.api.wrappers.hiscores.HiscoreType;
import com.kit.core.Session;
import com.kit.api.MethodContext;
import com.kit.api.event.EventHandler;
import com.kit.api.event.MessageEvent;
import com.kit.api.plugin.Plugin;
import com.kit.api.util.PriceLookup;
import com.kit.api.util.Utilities;
import com.kit.api.wrappers.Skill;
import com.kit.api.wrappers.hiscores.HiscoreLookup;
import com.kit.api.wrappers.hiscores.HiscoreType;
import com.kit.core.Session;
import com.kit.core.control.PluginManager;
import com.kit.game.cache.io.Utils;
import com.kit.api.MethodContext;

import java.awt.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class QuickChatPlugin extends Plugin {

    public static final Skill[] COMBAT_SKILLS =
            {Skill.ATTACK, Skill.STRENGTH, Skill.DEFENCE, Skill.HITPOINTS, Skill.MAGIC, Skill.RANGED, Skill.PRAYER};
    public static final MessageEvent.Type[] RESPOND_TYPES = {MessageEvent.Type.MESSAGE_CHAT, MessageEvent.Type.MESSAGE_CLANCHAT};

    public QuickChatPlugin(PluginManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "Quick Chat";
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @EventHandler
    public void onMessage(MessageEvent event) {
        Arrays.stream(RESPOND_TYPES).filter(t -> t == event.getType()).findFirst()
                .map(type -> {
                    for (Command command : Command.values()) {
                        CommandWrapper wrapper = command.matches(event.getMessage().toLowerCase());
                        if (wrapper != null) {
                            new Thread(() -> {
                                String output = command.getResult(Session.get(), event, wrapper.matches(event.getMessage().toLowerCase()), wrapper);
                                if (output != null) {
                                    sendThroughChatBox(output, event.getSender(), event.getClanName(), event.getType(), false);
                                }
                            }).start();
                            break;
                        }
                    }
                    return null;//this is hack 2k16 because java8 fp sucks
                });
    }

    public static void sendThroughChatBox(String msg, String sender, String clanName, MessageEvent.Type type, boolean threaded) {
        if (threaded) {
            Thread thread = new Thread(() -> {
                Session.get().game.sendChatboxMessage(msg, sender, clanName, type);
            });
            thread.start();
        } else {
            Session.get().game.sendChatboxMessage(msg, sender, clanName, type);
        }
    }

    private static String getTotal(MethodContext ctx, MessageEvent e, String prefix, String[] args) {
        ChatMessageBuilder output = new ChatMessageBuilder();
        String usernameToLookup = e.getType() == MessageEvent.Type.MESSAGE_PRIVATE_SENT ?
                ctx.player.getName() :
                e.getSender();
        if (usernameToLookup == null) {
            output.appendColored(Color.RED, "Unable to find username to lookup");
            return output.toString();
        }
        Skill skill = Skill.OVERALL;
        HiscoreLookup lookup = ctx.hiscores.lookup(usernameToLookup, HiscoreType.STANDARD);//TODO get curr game mode
        if (lookup != null) {
            output.startColor(Color.RED)
                    .append(skill.getName())
                    .endColor()
                    .append(":")
                    .appendColored(Color.GRAY, "Exp: [")
                    .appendColored(Color.BLUE, lookup.getSkill(skill).getExperience())
                    .appendColored(Color.GRAY, "] ")
                    .appendColored(Color.GRAY, "Level: [")
                    .appendColored(Color.BLUE, lookup.getSkill(skill).getLevel())
                    .appendColored(Color.GRAY, "] ")
                    .appendColored(Color.GRAY, "Rank: [")
                    .appendColored(Color.BLUE, lookup.getSkill(skill).getRank())
                    .appendColored(Color.GRAY, "] ");
        } else {
            output.appendColored(Color.RED, "Error occurred retrieving hiscores, please try again");
        }
        return output.toString();

    }

    private static String getPrice(MethodContext ctx, MessageEvent e, String prefix, String[] args) {
        ChatMessageBuilder output = new ChatMessageBuilder();
        if (args == null || args.length == 0) {
            output.append("No item name provided.");
            return output.toString();
        }
        String item = Arrays.stream(args).collect(Collectors.joining(" "));
        int price = PriceLookup.getPrice(item);

        if (price > -1) {
            output.append("Price for ")
                    .appendColored(Color.BLUE, item)
                    .append(" -> ")
                    .appendColored(Color.RED, Utilities.prettyFormat(price) + "gp");
        } else {
            output.append("Error occurred retrieving price for " + item + ", please try again");
        }
        return output.toString();
    }

    public static String getAllStats(MethodContext ctx, String toLookup) {
        ChatMessageBuilder output = new ChatMessageBuilder();
        HiscoreLookup lookup = ctx.hiscores.lookup(toLookup, HiscoreType.STANDARD);//TODO get curr game mode
        if (lookup != null) {
            String skikllsRaw = Arrays.stream(Skill.values()).map(skill -> {
                ChatMessageBuilder skillOutput = new ChatMessageBuilder();
                return skillOutput.startColor(Color.RED)
                        .append(skill.getName())
                        .endColor()
                        .append(":")
                        .startColor(Color.BLUE)
                        .append(lookup.getSkill(skill).getLevel())
                        .endColor()
                        .toString();
            }).collect(Collectors.joining(" | "));
            output.appendRaw(skikllsRaw);
        } else {
            output.appendColored(Color.RED, "Error occurred retrieving hiscores, please try again");
        }
        return output.toString();
    }

    private static String getAllStats(MethodContext ctx, MessageEvent e, String prefix, String[] args) {
        String toLookup = e.getType() == MessageEvent.Type.MESSAGE_PRIVATE_SENT ? ctx.player.getName() : e.getSender();
        return getAllStats(ctx, toLookup);
    }


    private static String getCombatStats(MethodContext ctx, MessageEvent e, String prefix, String[] args) {
        ChatMessageBuilder output = new ChatMessageBuilder();
        String toLookup = e.getType() == MessageEvent.Type.MESSAGE_PRIVATE_SENT ? ctx.player.getName() : e.getSender();
        HiscoreLookup lookup = ctx.hiscores.lookup(toLookup, HiscoreType.STANDARD);//TODO get curr game mode
        if (lookup != null) {
            String skikllsRaw = Arrays.stream(COMBAT_SKILLS).map(skill -> {
                ChatMessageBuilder skillOutput = new ChatMessageBuilder();
                return skillOutput.startColor(Color.RED)
                        .append(skill.getName())
                        .endColor()
                        .append(":")
                        .startColor(Color.BLUE)
                        .append(lookup.getSkill(skill).getLevel())
                        .endColor()
                        .toString();
            }).collect(Collectors.joining(" | "));
            output.appendRaw(skikllsRaw);
        } else {
            output.appendColored(Color.RED, "Error occurred retrieving hiscores, please try again");
        }
        return output.toString();
    }

    private static String getLevel(MethodContext ctx, MessageEvent e, String prefix, String[] args) {
        ChatMessageBuilder output = new ChatMessageBuilder();
        String skillShort = prefix != null ? prefix : args.length > 0 ? args[0] : null;
        String usernameToLookup = e.getType() == MessageEvent.Type.MESSAGE_PRIVATE_SENT ?
                ctx.player.getName() :
                e.getSender();
        Skill skill = getSkillForString(skillShort);
        if (skill == null || usernameToLookup == null) {
            output.appendColored(Color.RED, "Unable to find skill/username to lookup");
            return output.toString();
        }
        HiscoreLookup lookup = ctx.hiscores.lookup(usernameToLookup, HiscoreType.STANDARD);//TODO get curr game mode
        if (lookup != null) {
            output.startColor(Color.RED)
                    .append(skill.getName())
                    .endColor()
                    .append(":")
                    .appendColored(Color.GRAY, "Exp: [")
                    .appendColored(Color.BLUE, lookup.getSkill(skill).getExperience())
                    .appendColored(Color.GRAY, "] ")
                    .appendColored(Color.GRAY, "Level: [")
                    .appendColored(Color.BLUE, lookup.getSkill(skill).getLevel())
                    .appendColored(Color.GRAY, "] ")
                    .appendColored(Color.GRAY, "Rank: [")
                    .appendColored(Color.BLUE, lookup.getSkill(skill).getRank())
                    .appendColored(Color.GRAY, "] ");
        } else {
            output.appendColored(Color.RED, "Error occurred retrieving hiscores, please try again");
        }
        return output.toString();
    }

    @Override
    public boolean hasOptions() {
        return false;
    }

    @FunctionalInterface
    public interface QuadFunction<T, U, V, W, R> {

        R apply(T t,
                U u,
                V v,
                W w);

        default <X> QuadFunction<T, U, V, W, X> andThen(Function<? super R, ? extends X> after) {
            Objects.requireNonNull(after);
            return (T t, U u, V v, W w) -> after.apply(apply(t, u, v, w));
        }
    }

    private static class CommandWrapper {

        private Pattern pattern;
        private String key;
        private boolean hasPrefix;

        public CommandWrapper(Pattern pattern, String key, boolean hasPrefix) {
            this.pattern = pattern;
            this.key = key;
            this.hasPrefix = hasPrefix;
        }

        public String getPrefixValue(Matcher matcher, String value) {
            if (value.startsWith("!" + key)) {
                return null;
            }
            if (matcher.groupCount() == 0 || !matcher.matches()) {
                return null;
            }
            String s = value.substring(0, value.indexOf(key)).replace("!", "");
            return s.length() > 0 ? s : null;
        }

        public Matcher matches(String value) {
            return pattern.matcher(value);
        }
    }

    public enum Command {
        LEVEL(QuickChatPlugin::getLevel,
                new CommandWrapper(Pattern.compile("!([A-z]+)vl.*"), "lvl", true),
                new CommandWrapper(Pattern.compile("!([A-z]+)evel.*"), "level", true)),
        TOTAL(QuickChatPlugin::getTotal,
                new CommandWrapper(Pattern.compile("!total"), "total", false),
                new CommandWrapper(Pattern.compile("!ttl"), "ttl", false),
                new CommandWrapper(Pattern.compile("!tl"), "tl", false)),
        COMBAT(QuickChatPlugin::getCombatStats,
                new CommandWrapper(Pattern.compile("!cmb"), "cmb", false),
                new CommandWrapper(Pattern.compile("!combat"), "combat", false),
                new CommandWrapper(Pattern.compile("!cb"), "cb", false)),
        PRICE(QuickChatPlugin::getPrice,
                new CommandWrapper(Pattern.compile("!price\\s.*"), "price", false),
                new CommandWrapper(Pattern.compile("!pricecheck\\s.*"), "pricecheck", false),
                new CommandWrapper(Pattern.compile("!pc\\s.*"), "pc", false),
                new CommandWrapper(Pattern.compile("!pr\\s.*"), "pr", false));

        private CommandWrapper[] commands;
        private QuadFunction<MethodContext, MessageEvent, String, String[], String> action;

        Command(QuadFunction<MethodContext, MessageEvent, String, String[], String> action, CommandWrapper... commands) {
            this.commands = commands;
            this.action = action;
        }

        public CommandWrapper matches(String msg) {
            for (CommandWrapper command : commands) {
                Matcher m = command.matches(msg);
                if (m.matches()) {
                    return command;
                }
            }
            return null;
        }

        public String getResult(MethodContext ctx, MessageEvent e, Matcher m, CommandWrapper w) {
            String message = e.getMessage().trim().toLowerCase();
            String prefix = w.hasPrefix ? w.getPrefixValue(m, message) : null;
            String argsRaw = message.contains(w.key + " ") ? message.substring(message.indexOf(' ') + 1) : "";
            return action.apply(ctx, e, prefix, argsRaw.trim().length() > 0 ? argsRaw.split(" ") : new String[]{});
        }
    }

    private static Skill getSkillForString(String s) {
        if (s == null) {
            return null;
        }
        switch (s.toLowerCase()) {
            case "at":
            case "atk":
            case "att":
            case "attk":
            case "attack":
                return Skill.ATTACK;
            case "st":
            case "str":
            case "stren":
            case "strenth":
            case "strength":
                return Skill.STRENGTH;
            case "de":
            case "def":
            case "dfc":
            case "defn":
            case "defence":
                return Skill.DEFENCE;
            case "ra":
            case "rng":
            case "rngd":
            case "range":
            case "ranged":
                return Skill.RANGED;
            case "pr":
            case "pra":
            case "pry":
            case "pryr":
            case "pray":
            case "prayer":
                return Skill.PRAYER;
            case "mg":
            case "mag":
            case "magic":
            case "mage":
            case "maged":
                return Skill.MAGIC;
            case "rc":
            case "runec":
            case "rcing":
            case "rcrft":
            case "rcfting":
            case "runecraft":
            case "runecrafting":
                return Skill.RUNECRAFTING;
            case "ct":
            case "con":
            case "csrt":
            case "construction":
            case "construct":
            case "building":
                return Skill.CONSTRUCTION;
            case "hp":
            case "health":
            case "constitution":
            case "hitpoints":
            case "hitpoint":
            case "hps":
            case "hitp":
            case "hitps":
                return Skill.HITPOINTS;
            case "ay":
            case "agile":
            case "agility":
            case "agil":
            case "agy":
            case "fitness":
                return Skill.AGILITY;
            case "hbl":
            case "hl":
            case "herblore":
            case "gardening":
            case "heblore":
            case "hrbl":
            case "herb":
            case "herbs":
            case "planting":
                return Skill.HERBLORE;
            case "tv":
            case "tvg":
            case "tvng":
            case "thief":
            case "thieving":
            case "theiving":
            case "theif":
            case "theiv":
            case "thiev":
            case "thieve":
            case "theive":
            case "robbing":
            case "thng":
                return Skill.THIEVING;
            case "crafting":
            case "craft":
            case "crft":
            case "cft":
            case "craftg":
            case "crt":
                return Skill.CRAFTING;
            case "fletch":
            case "ftch":
            case "fth":
            case "fletching":
            case "fletchg":
                return Skill.FLETCHING;
            case "sl":
            case "slyr":
            case "slr":
            case "slay":
            case "slayer":
            case "07kit":
            case "slayr":
                return Skill.SLAYER;
            case "hunt":
            case "hu":
            case "hr":
            case "huntr":
            case "hntr":
            case "hnt":
            case "hunter":
                return Skill.HUNTER;
            case "mn":
            case "mng":
            case "mine":
            case "mining":
            case "ming":
                return Skill.MINING;
            case "sh":
            case "smith":
            case "smithing":
            case "smth":
            case "smthng":
                return Skill.SMITHING;
            case "fhn":
            case "fish":
            case "fishing":
            case "fsh":
                return Skill.FISHING;
            case "cook":
            case "chef":
            case "cooking":
            case "ck":
            case "ckng":
            case "ckg":
                return Skill.COOKING;
            case "fm":
            case "fire":
            case "fmkng":
            case "fmkg":
            case "firemaking":
            case "fmaking":
                return Skill.FIREMAKING;
            case "wc":
            case "wood":
            case "woodcut":
            case "woodcutting":
            case "wdc":
            case "wdcng":
                return Skill.WOODCUTTING;
            case "farm":
            case "farming":
            case "fmg":
            case "frm":
            case "frmng":
                return Skill.FARMING;
        }
        return null;
    }
}
