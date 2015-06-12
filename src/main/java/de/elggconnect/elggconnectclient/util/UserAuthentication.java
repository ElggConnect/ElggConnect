/*
 * Copyright (c) 2015. , Beaufort 8
 * released under MIT License
 * http://opensource.org/licenses/MIT
 */

package de.elggconnect.elggconnectclient.util;


import de.elggconnect.elggconnectclient.Main;
import de.elggconnect.elggconnectclient.webservice.AuthGetToken;
import de.elggconnect.elggconnectclient.webservice.WebService;

import java.util.prefs.Preferences;

/**
 * UserAuthentication Singleton for handle the User Authentication
 *
 * @author Alexander Stifel
 * @author Beaufort 8
 */
public class UserAuthentication {
    private static final String ID1 = "Username";
    private static final String ID2 = "Password";
    private static final String ID3 = "Notification";
    private static UserAuthentication ourInstance = new UserAuthentication();
    private String username, password, authToken, baseURL, notification;
    private Boolean isLogged;
    private Preferences pref;

    //region Getter and Setter

    /**
     * Constructor
     * Load saved username or password
     */
    private UserAuthentication() {
        loadUserPreferences();
        this.isLogged = false;
        this.baseURL = Main.BASE_API_URL;
    }

    //region Singleton method and private constructor
    public static UserAuthentication getInstance() {
        return ourInstance;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return password;
    }

    public boolean getNotification() {
        if (this.notification.equals("true")) {
            return true;
        } else {
            return false;
        }

    }


    public String getAuthToken() {
        return authToken;
    }


    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getBaseURL() {
        return this.baseURL;
    }

    //endregion

    /**
     * load User Preferences and decode the password
     * load empty String if there is nothing to load
     */
    private void loadUserPreferences() {
        pref = Preferences.userRoot().node(this.getClass().getName());
        this.username = pref.get(ID1, "");
        this.password = EncryptUtils.base64decode(pref.get(ID2, ""));
        this.notification = pref.get(ID3, "false");


    }

    /**
     * save user preferences and encode the password
     */
    public void saveUserPreferences(String username, String password, boolean notification) {
        // This will define a node in which the preferences can be stored
        pref = Preferences.userRoot().node(this.getClass().getName());
        pref.put(ID1, username);
        pref.put(ID2, EncryptUtils.base64encode(password));
        pref.put(ID3, String.valueOf(notification));
        loadUserPreferences();

    }

    /**
     * check if the user is connected to the Elgg Network
     *
     * @return
     */
    public Boolean connect() {
        WebService webService = new WebService();
        AuthGetToken authGetToken;
        Long result;

        //try to get Token
        result = webService.executeAPIMethod(authGetToken = new AuthGetToken(username, password));
        if (result == 0) {
            setAuthToken(authGetToken.getAuthToken());
            this.isLogged = true;
            return true;
        }


        this.isLogged = false;
        return false;

    }


    /**
     * Check if username and password not empty
     *
     * @return
     */
    public boolean isEmpty() {
        return this.username.isEmpty() || this.password.isEmpty();
    }


    /**
     * Overwrite username and password with empty string
     */
    public void deleteUserPreferences() {
        pref = Preferences.userRoot().node(this.getClass().getName());
        pref.put(ID1, "");
        pref.put(ID2, "");
        pref.put(ID3, "false");
        this.username = "";
        this.password = "";
        this.authToken = "";
        this.isLogged = false;
        this.notification = "false";
    }

}
