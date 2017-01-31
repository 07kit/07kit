package com.kit.gui.controller;

import com.kit.Application;
import com.kit.core.Session;
import com.kit.core.model.Property;
import com.kit.gui.Controller;
import com.kit.gui.view.LoginView;
import org.apache.http.client.fluent.Executor;
import com.kit.Application;
import com.kit.http.CreateTokenRequest;
import com.kit.core.Session;
import com.kit.core.model.Property;
import com.kit.gui.Controller;
import com.kit.gui.ControllerManager;
import com.kit.gui.view.LoginView;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import com.kit.http.UserAccount;
import com.kit.http.UserToken;
import com.kit.util.HttpUtil;
import com.kit.util.JacksonUtil;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class LoginController extends Controller<LoginView> {

    public static final String API_URL = "https://api.07kit.com/user";

    private final Logger logger = Logger.getLogger(LoginController.class);
    private LoginView view;

    public LoginController() {
        ControllerManager.add(LoginController.class, this);
    }


    public void show() {
        try {
            if (Session.get().getApiToken() != null) {
                logger.info("Existing API token found - trying to retrieve account info...");
                if (loadAccount(Session.get().getApiToken(), true, Session.get().getEmail().getValue())) {
                    logger.info("Logged in with pre-existing key.");
                    return;
                }
            }
        } catch (Exception e) {
            logger.error("Failed to authenticate.", e);
        }

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int centerX = (toolkit.getScreenSize().width / 2) - (getComponent().getWidth() / 2);
        int centerY = (toolkit.getScreenSize().height / 2) - (getComponent().getHeight() / 2);
        getComponent().setLocation(centerX, centerY);
        getComponent().setIconImage(Application.ICON_IMAGE);
        getComponent().setVisible(true);
    }

    public void login(String email, String password, boolean rememberMe) {
        try {
            SwingWorker worker = new SwingWorker() {
                @Override
                protected Object doInBackground() throws Exception {
                    HttpResponse response = Executor.newInstance(HttpUtil.getClient()).execute(Request.Post(API_URL + "/token")
                            .bodyString(JacksonUtil.serialize(new CreateTokenRequest(email, password)), ContentType.APPLICATION_JSON)).returnResponse();
                    if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                        UserToken tokenResponse = JacksonUtil.deserialize(
                                EntityUtils.toString(response.getEntity()),
                                UserToken.class);
                        if (loadAccount(tokenResponse.getUuid(), rememberMe, email)) {
                            logger.info("Logged in.");
                            return null;
                        }
                    }
                    logger.error("Invalid login, response: [" + response.toString() + "]");
                    getComponent().getStatusLbl().setText("Status: Invalid login");
                    return null;
                }
            };
            worker.execute();
        } catch (Exception e) {
            logger.error("Oops.", e);
            getComponent().getStatusLbl().setText("Status: Error logging in");
        }
    }

    private boolean loadAccount(String uuid, boolean rememberMe, String email) throws IOException {
        HttpResponse getAccountResponse = Executor.newInstance(HttpUtil.getClient()).execute(Request.Get(API_URL)
                .addHeader("Authorization", "Bearer " + uuid)).returnResponse();
        if (getAccountResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            logger.info("Processed login [" + getAccountResponse.toString() + "]");

            UserAccount account = JacksonUtil.deserialize(
                    EntityUtils.toString(getAccountResponse.getEntity()),
                    UserAccount.class);
            if (account != null && account.getStatus() == UserAccount.Status.ACTIVE &&
                    account.getType() != null) {
                getComponent().getStatusLbl().setText("Status: Logged in");
                getComponent().dispose();

                Session.get().setUserAccount(account);

                Session.get().setApiToken(uuid);
                Property emailProperty = Session.get().getEmail();
                Property apiKeyProperty = Session.get().getApiKey();
                if (rememberMe) {
                    if (emailProperty == null) {
                        emailProperty = new Property(Session.EMAIL_PROPERTY_KEY, email);
                        emailProperty.save();
                    } else {
                        emailProperty.setValue(email);
                        emailProperty.save();
                    }

                    if (apiKeyProperty == null) {
                        apiKeyProperty = new Property(Session.API_KEY_PROPERTY_KEY, uuid);
                        apiKeyProperty.save();
                    } else {
                        apiKeyProperty.setValue(uuid);
                        apiKeyProperty.save();
                    }
                } else {
                    if (emailProperty != null) {
                        emailProperty.remove();
                    }

                    if (apiKeyProperty != null) {
                        apiKeyProperty.remove();
                    }
                }
                ControllerManager.get(MainController.class).show();
                return true;
            } else {
                getComponent().getStatusLbl().setText("Status: Only BETA users can login");
                return false;
            }
        }
        return false;
    }

    @Override
    public LoginView getComponent() {
        if (view == null) {
            view = new LoginView(this);
        }
        return view;
    }
}
