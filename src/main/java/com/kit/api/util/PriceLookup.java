package com.kit.api.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.kit.api.wrappers.PriceInfo;
import com.kit.core.Session;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import com.kit.Application;
import com.kit.api.Constants;
import com.kit.api.wrappers.PriceInfo;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import com.kit.core.Session;
import com.kit.util.HttpUtil;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PriceLookup {

	public static final Gson GSON = new Gson();

	public static final String API_URL = "https://api.07kit.com/price/lookup/";

	public static final String AUTH_HEADER_KEY = "Authorization";

	public static final Type PRICE_INFO_LIST_TYPE = new com.google.common.reflect.TypeToken<ArrayList<PriceInfo>>() {
	}.getType();

	private static final LoadingCache<String, PriceInfo> PRICE_INFO_CACHE = CacheBuilder.newBuilder()
			.maximumSize(250)
			.expireAfterWrite(30, TimeUnit.MINUTES)
			.build(
					new CacheLoader<String, PriceInfo>() {

						@Override
						public PriceInfo load(String id) throws Exception {
							String path = id.startsWith("name-") ? "name/" : "id/";
							Request request = Request.Get(API_URL + path +
									(id.startsWith("name-") ?
									id.substring(id.indexOf('-') + 1)
									: id.replace("name-", "")));
							request.addHeader(AUTH_HEADER_KEY, "Bearer " + Session.get().getApiToken());

							HttpResponse response = Executor.newInstance(HttpUtil.getClient()).execute(request).returnResponse();
							if (response != null) {
								if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
									byte[] bytes = EntityUtils.toByteArray(response.getEntity());
									return GSON.fromJson(new String(bytes), PriceInfo.class);
								}
							}
							return null;
						}
					}
			);

	public static int getPrice(String name) {
		if (name != null && name.equals("Coins"))
			return 1;
		try {
			return PRICE_INFO_CACHE.get("name-" + name.replaceAll(" ", "%20")).getBuyAverage();
		} catch (ExecutionException e) {
			e.printStackTrace();
			return 0;
		} catch (CacheLoader.InvalidCacheLoadException e) {
			return 0;
		}
	}

	public static List<PriceInfo> search(String term, int limit) {
		try {
			Request request = Request.Get(API_URL + "search?term=" + URLEncoder.encode(term, "UTF-8") + "&limit=" + limit);
			request.addHeader(AUTH_HEADER_KEY, "Bearer " + Session.get().getApiToken());

			HttpResponse response = Executor.newInstance(HttpUtil.getClient()).execute(request).returnResponse();
			if (response != null) {
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					byte[] bytes = EntityUtils.toByteArray(response.getEntity());
					return GSON.fromJson(new String(bytes), PRICE_INFO_LIST_TYPE);
				}
			}
			return new ArrayList<>();
		} catch (IOException | CacheLoader.InvalidCacheLoadException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public static Map<Integer, Integer> getPrices(Collection<Integer> ids) {
		try {
			Map<Integer, Integer> prices = new HashMap<>();
			List<Integer> idsClone = new ArrayList<>(ids);

			idsClone.forEach(id -> {
				if (id == 995) {
					prices.put(id, 1);
				} else {
					PriceInfo info = PRICE_INFO_CACHE.getIfPresent(String.valueOf(id));
					if (info != null) {
						prices.put(info.getItemId(), info.getBuyAverage());
					}
				}
			});

			idsClone.removeAll(prices.keySet());

			if (idsClone.size() == 0) {
				return prices;
			}

			Request request = Request.Post(API_URL + "ids");
			request.addHeader(AUTH_HEADER_KEY, "Bearer " + Session.get().getApiToken());
			request.bodyString(GSON.toJson(idsClone), ContentType.APPLICATION_JSON);

			HttpResponse response = Executor.newInstance(HttpUtil.getClient()).execute(request).returnResponse();
			if (response != null) {
				if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
					byte[] bytes = EntityUtils.toByteArray(response.getEntity());
					List<PriceInfo> infos = GSON.fromJson(new String(bytes), PRICE_INFO_LIST_TYPE);
					infos.forEach(i -> {
						PRICE_INFO_CACHE.put(String.valueOf(i.getItemId()), i);
						prices.put(i.getItemId(), i.getBuyAverage());
					});
				}
			}

			return prices;
		} catch (IOException | CacheLoader.InvalidCacheLoadException e) {
			e.printStackTrace();
			return new HashMap<>();
		}
	}

	public static int getPrice(int id) {
		if (id == 995)//coins
			return 1;
		try {
			return PRICE_INFO_CACHE.get(String.valueOf(id)).getBuyAverage();
		} catch (ExecutionException e) {
			e.printStackTrace();
			return 0;
		} catch (CacheLoader.InvalidCacheLoadException e) {
			return 0;
		}
	}

}
