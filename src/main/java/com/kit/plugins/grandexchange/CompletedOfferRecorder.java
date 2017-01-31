package com.kit.plugins.grandexchange;

import com.kit.api.event.EventHandler;
import com.kit.api.event.GrandExchangeOfferUpdatedEvent;
import com.kit.api.event.LoginEvent;
import com.kit.util.HttpUtil;
import org.apache.http.client.fluent.Executor;
import com.kit.api.event.EventHandler;
import com.kit.api.event.GrandExchangeOfferUpdatedEvent;
import com.kit.api.event.LoginEvent;
import com.kit.api.plugin.Schedule;
import com.kit.api.wrappers.PriceInfo;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Request;
import org.apache.log4j.Logger;
import com.kit.api.event.EventHandler;
import com.kit.api.event.GrandExchangeOfferUpdatedEvent;
import com.kit.api.event.LoginEvent;
import com.kit.util.HttpUtil;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class CompletedOfferRecorder implements Runnable {

	public static final int BUY_COMPLETE_STATE = 5;
	public static final int SELL_COMPLETE_STATE = 13;
	public static final int MIN_TIME_SINCE_LOGIN = 1000 * 10;

	public static final String API_URL = "https://api.07kit.com/price/";

	public static final String SECRET_HEADER_KEY = "auth_token";
	public static final String SECRET = "KBcEDGBmBzVpPFZqdXvGsQeWMgxDK66P";

	private Queue<Request> requestQueue = new ConcurrentLinkedQueue<>();

	private final Logger logger = Logger.getLogger(getClass());

	private long lastLoginTime = -1;

	private void processQueue() {
		Request next = requestQueue.poll();
		while (next != null) {
			try {
				HttpResponse response = Executor.newInstance(HttpUtil.getClient()).execute(next).returnResponse();
				if (response != null) {
					if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
						logger.info("Processed request [" + next.toString() + "]");
					} else {
						logger.error("Error processing request queue got rsponse [" + response.toString() + "]");
					}
				}
			} catch (IOException e) {
				logger.error("Error processing request queue", e);
			}
			next = requestQueue.poll();
		}
	}

	@EventHandler
	public void onOfferUpdate(GrandExchangeOfferUpdatedEvent evt) {
		if (lastLoginTime == -1 || (System.currentTimeMillis() - lastLoginTime) < MIN_TIME_SINCE_LOGIN) {
			return;
		}

		//TODO be better at life 2k19
		if (evt.getOffer() != null && evt.getOffer().getTransferred() == evt.getOffer().getQuantity()) {
			String path = "";
			if (evt.getOffer().getState() == BUY_COMPLETE_STATE) {
				path += "buy/";
			} else if (evt.getOffer().getState() == SELL_COMPLETE_STATE) {
				path += "sell/";
			} else {
				return;
			}
			path += "insert/" + evt.getOffer().getItemId() + "/" + evt.getOffer().getTotalSpent() + "/" + evt.getOffer().getQuantity();

			Request request = Request.Post(API_URL + path);
			request.addHeader(SECRET_HEADER_KEY, SECRET);

			requestQueue.add(request);
		}
	}

	@EventHandler
	public void onPlayerLogin(LoginEvent evt) {
		lastLoginTime = evt.getTime();
	}

	@Override
	public void run() {
		while (true) {
			processQueue();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
