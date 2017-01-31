package com.kit.api.impl.game;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.kit.Application;
import com.kit.api.MethodContext;
import com.kit.api.Worlds;
import com.kit.api.util.Utilities;
import com.kit.api.wrappers.Widget;
import com.kit.api.wrappers.World;
import com.kit.core.Session;
import com.kit.game.engine.IWorld;
import com.kit.api.MethodContext;
import com.kit.api.Worlds;
import com.kit.api.util.Internet;
import com.kit.api.wrappers.Widget;
import com.kit.api.wrappers.World;
import com.kit.game.engine.IWorld;
import com.kit.api.MethodContext;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;

import static com.google.common.collect.Lists.newArrayList;
import static com.kit.api.util.Utilities.random;
import static java.lang.Integer.parseInt;
import static java.util.Collections.sort;

/**
 * API for world selection etc.
 *
 * * @author tobiewarburton
 */
public class WorldsImpl implements Worlds {
	private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_" +
			"9_0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36";

	public static final int GAME_PORT = 43594;
	private final MethodContext context;

	public static final LoadingCache<MethodContext, List<World>> WORLDS_CACHE = CacheBuilder.newBuilder()
			.maximumSize(100)
			.expireAfterWrite(30, TimeUnit.SECONDS)
			.build(
					new CacheLoader<MethodContext, List<World>>() {
						@Override
						public List<World> load(MethodContext ctx) {
							final List<World> worlds = new ArrayList<>();

							String worldListTxt = Internet.getHtml("http://oldschool.runescape.com/slu");
							String numberString = "slu-world-";
							String playersString = "players";
							String countryString = "country";
							String typeString = "type";
							String domainString = "Old School ";

							for(String s : worldListTxt.substring(worldListTxt.indexOf("server-list__body")).split("<tr>|</tr>")) {
								if (!s.contains("server-list")) {
									continue;
								}
								String[] lines = s.split("\n");
								int worldNumber = 0;
								boolean members = false;
								int players = 0;
								String country = null;
								String activity = null;
								String domain = null;
								for (String line : lines) {
									if (!line.contains("td")) {
										continue;
									}
									if (line.contains(numberString)) {
										int start = line.indexOf(numberString);
										worldNumber = Integer.parseInt(line.substring(start + numberString.length(), line.indexOf('"', start)).trim());
										int startDomain = line.indexOf(domainString);
										String number = line.substring(startDomain + domainString.length(), line.indexOf('<', startDomain)).trim();
										domain = "oldschool" + number + ".runescape.com";
									} else if (line.contains(playersString)) {
										players = Integer.parseInt(line.substring(line.indexOf('>') + 1, line.indexOf(playersString)).trim());
									} else if (line.contains(countryString)) {
										country = line.substring(line.indexOf('>') + 1, line.lastIndexOf('<')).trim();
									} else if (line.contains(typeString)) {
										members = line.substring(line.indexOf('>') + 1, line.lastIndexOf('<')).trim().toLowerCase().contains("members");
									} else {
										activity = line.substring(line.indexOf('>') + 1, line.lastIndexOf('<')).trim();
									}
								}
								worlds.add(new World(ctx, activity, members, players, worldNumber, country, domain));
							}
							worlds.sort((o1, o2) -> o1.getId() - o2.getId());
							return worlds;
						}
					}
			);

	public static final LoadingCache<String, Integer> PING_CACHE = CacheBuilder.newBuilder()
			.maximumSize(100)
			.expireAfterWrite(10, TimeUnit.SECONDS)
			.build(
					new CacheLoader<String, Integer>() {
						@Override
						public Integer load(String domain) throws ExecutionException, InterruptedException {
							long startTime = System.nanoTime();
							try (Socket socket = new Socket(domain, GAME_PORT)) {
								byte[] payload = new byte[]{15, 0, 0, 0, 0};
								socket.getOutputStream().write(payload, 0, payload.length);
								socket.getInputStream().read();
								long endTime = System.nanoTime();
								return (int) ((endTime - startTime) / 10e6);
							} catch (IOException e) {
								e.printStackTrace();
							}
							return -1;
						}
					}
			);

	public WorldsImpl(MethodContext context) {
		this.context = context;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public World getEmptiest() {
		List<World> worlds = getAll();
        sort(worlds, (o1, o2) -> o1.getPlayers() - o2.getPlayers());
		return worlds.size() > 0 ? worlds.get(0) : null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public World getBusiest() {
		List<World> worlds = getAll();
        sort(worlds, (o1, o2) -> o2.getPlayers() - o1.getPlayers());
		return worlds.size() > 0 ? worlds.get(0) : null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public World getAny() {
		List<World> worlds = getAll();
		return (worlds.size() > 0) ? worlds.get(Utilities.random(0, worlds.size() - 1)) : null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void switchTo(World world) {
		if (!context.isLoggedIn() || getCurrent() == world.getId()) {
			return;
		}
//		for (IWorld iworld : context.client().getWorlds()) {
//			if (world.getId() == iworld.getId()) {
				Session.get().client().setWorldDomain(world.getDomain());
				Session.get().client().setWorldId(world.getId());
				Session.get().getClient().setLoginIndex(45);
				return;
//			}
//		}
//		throw new RuntimeException("Unable to find world with id: " + world.getId());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getPing(World world) {
		try {
			return PING_CACHE.get(world.getDomain());
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return -1;
	}

	@Override
	public int getCurrent() {
		try {
			Widget widget = context.widgets.find(429, 1);
			if (widget != null) {
				String text = widget.getText();
				if (text != null) {
					return Integer.parseInt(text.substring(text.trim().lastIndexOf(' ')).trim());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<World> getAll() {
		try {
			return WORLDS_CACHE.get(context);
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
	}

}
