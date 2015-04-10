/*
 * Copyright (c) 2015. , Beaufort 8
 * released under MIT License
 * http://opensource.org/licenses/MIT
 */

package de.elggconnect.elggconnectclient.controller;

import de.elggconnect.elggconnectclient.Main;
import de.elggconnect.elggconnectclient.util.UserAuthentication;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * TrayIcon Elgg Application
 * Controller for the Settings / Login Stage
 *
 * @author Alexander Stifel
 * @author Beaufort 8
 */
public class LogoutController {

    @FXML
    private Label lblLoggedUsername;
    @FXML
    private Button btnLogout;


    @FXML
    /**
     * Set up the Logout View
     * Show the logged in username
     */
    void initialize() {
        this.lblLoggedUsername.setText(UserAuthentication.getInstance().getUsername());
        //Bind Enter key to Logout Button
        btnLogout.defaultButtonProperty().bind(btnLogout.focusedProperty());
    }

    /**
     * User clicked on Button Logout
     */
    public void btnLogout() {
        //Delete stored user data
        UserAuthentication.getInstance().deleteUserPreferences();
        Main.timerTask.run();
    }

    /**
     * initialize wrapper
     */
    public void init() {
        initialize();
    }
}
