package com.kit.plugins.clan.backend;

import com.google.gson.Gson;
import com.kit.api.util.NotificationsUtil;
import com.kit.core.Session;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import com.kit.api.util.NotificationsUtil;
import com.kit.core.Session;
import com.kit.http.SuccessResponse;
import com.kit.util.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ClanService {

    public static final Gson GSON = new Gson();

    public static final String API_URL = "https://api.07kit.com/clan/";

    public static final String AUTH_HEADER_KEY = "Authorization";

    private static final Logger logger = LoggerFactory.getLogger(ClanService.class);

    public static ClanInfo create(String loginName, String ingameName, String clanName, ClanRank.Status status, int world) {
        try {
            Request request = Request.Post(API_URL + "create");
            request.addHeader(AUTH_HEADER_KEY, "Bearer " + Session.get().getApiToken());
            request.bodyString(GSON.toJson(new CreateUpdateClanRequest(loginName, ingameName, clanName, status, world)),
                    ContentType.APPLICATION_JSON);

            HttpResponse response = Executor.newInstance(HttpUtil.getClient()).execute(request).returnResponse();
            if (response != null) {
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    byte[] bytes = EntityUtils.toByteArray(response.getEntity());
                    return GSON.fromJson(new String(bytes), ClanInfo.class);
                } else if (response.getStatusLine().getStatusCode() == 302) {
                    NotificationsUtil.showNotification("Clan", "That clan name is taken");
                } else {
                    NotificationsUtil.showNotification("Clan", "Error creating clan");
                }
            }
            return null;
        } catch (IOException e) {
            logger.error("Error creating clan", e);
            return null;
        }
    }

    public static ClanInfo join(String loginName, String ingameName, String clanName, long clanId, boolean useId, ClanRank.Status status, int world) {
        try {
            Request request = Request.Post(API_URL + "rank/update/" + (useId ? "id" : "name"));
            request.addHeader(AUTH_HEADER_KEY, "Bearer " + Session.get().getApiToken());
            request.bodyString(GSON.toJson(new GetOrJoinClanRequest(
                    clanId,
                    loginName,
                    ingameName,
                    clanName,
                    status,
                    world
            )), ContentType.APPLICATION_JSON);
            HttpResponse response = Executor.newInstance(HttpUtil.getClient()).execute(request).returnResponse();

            if (response != null) {
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    byte[] bytes = EntityUtils.toByteArray(response.getEntity());
                    return GSON.fromJson(new String(bytes), ClanInfo.class);
                } else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_FAILED_DEPENDENCY) {
                    NotificationsUtil.showNotification("Clan", "You have not yet been accepted into this clan");
                } else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
                    NotificationsUtil.showNotification("Clan", "Unable to find clan");
                } else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_FORBIDDEN) {
                    NotificationsUtil.showNotification("Clan", "You have been banned from this clan");
                } else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_EXPECTATION_FAILED) {
                    NotificationsUtil.showNotification("Clan", "You have been denied access into this clan");
                } else {
                    NotificationsUtil.showNotification("Clan", "Error joining clan");
                }
            }
            return null;
        } catch (IOException e) {
            logger.error("Error joining clan", e);
            return null;
        }
    }

    public static ClanInfo updateRank(String loginName, String ingameName, ClanInfo clan, ClanRank.Status status, int world) {
        try {
            Request request = Request.Post(API_URL + "rank/update/id");
            request.addHeader(AUTH_HEADER_KEY, "Bearer " + Session.get().getApiToken());
            request.bodyString(GSON.toJson(new GetOrJoinClanRequest(
                    clan.getClanId(),
                    loginName,
                    ingameName,
                    clan.getName(),
                    status,
                    world
            )), ContentType.APPLICATION_JSON);
            HttpResponse response = Executor.newInstance(HttpUtil.getClient()).execute(request).returnResponse();

            if (response != null) {
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    byte[] bytes = EntityUtils.toByteArray(response.getEntity());
                    return GSON.fromJson(new String(bytes), ClanInfo.class);
                } else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_FAILED_DEPENDENCY) {
                    NotificationsUtil.showNotification("Clan", "You have not yet been accepted into this clan");
                } else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
                    NotificationsUtil.showNotification("Clan", "Unable to find clan");
                } else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_FORBIDDEN) {
                    NotificationsUtil.showNotification("Clan", "You have been banned from this clan");
                } else {
                    NotificationsUtil.showNotification("Clan", "Error updating clan");
                }
            }
            return null;
        } catch (IOException e) {
            logger.error("Error updating clan rank", e);
            return null;
        }
    }

    public static SuccessResponse updateMemberRank(UpdateRankRequest updateRankRequest) {
        try {
            Request request = Request.Post(API_URL + "rank/update");
            request.addHeader(AUTH_HEADER_KEY, "Bearer " + Session.get().getApiToken());
            request.bodyString(GSON.toJson(updateRankRequest), ContentType.APPLICATION_JSON);
            HttpResponse response = Executor.newInstance(HttpUtil.getClient()).execute(request).returnResponse();

            if (response != null) {
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    byte[] bytes = EntityUtils.toByteArray(response.getEntity());
                    return GSON.fromJson(new String(bytes), SuccessResponse.class);
                } else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_FAILED_DEPENDENCY) {
                    NotificationsUtil.showNotification("Clan", "You aren't allowed to update ranks");
                } else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
                    NotificationsUtil.showNotification("Clan", "Unable to find clan/rank");
                } else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_FORBIDDEN) {
                    NotificationsUtil.showNotification("Clan", "Unable to find clan rank");
                } else {
                    NotificationsUtil.showNotification("Clan", "Error updating clan");
                }
            }
            return null;
        } catch (IOException e) {
            logger.error("Error updating member clan rank", e);
            return null;
        }
    }
}
