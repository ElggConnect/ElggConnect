/*
 * Copyright (c) 2015. , Beaufort 8
 * released under MIT License
 * http://opensource.org/licenses/MIT
 */

package de.elggconnect.elggconnectclient.controller;

import de.elggconnect.elggconnectclient.Main;
import de.elggconnect.elggconnectclient.util.SceneNavigator;
import de.elggconnect.elggconnectclient.util.UserAuthentication;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.ResourceBundle;

/**
 * Controller for the Login View
 * <p>
 * The Label lblStatus can used to inform the application user.
 *
 * @author Alexander Stifel
 * @author Beaufort 8
 */
public class LoginController   {

    @FXML
    private PasswordField tfPassword;
    @FXML
    private TextField tfUsername;
    @FXML
    private Label lblStatus;
    @FXML
    private Button btnLogin;

    private ResourceBundle resources;

    @FXML
/**
 * Set up the Login View
 */
    public void initialize(ResourceBundle resources) {
        this.resources = resources;
        //Set default status label text and style
        this.lblStatus.setText(this.resources.getString("key.loginMessages"));
        lblStatus.setStyle("");


        //Activate Login Button if there is a username and password
        btnLogin.disableProperty().bind(
                Bindings.isEmpty(tfUsername.textProperty())
                        .or(Bindings.isEmpty(tfPassword.textProperty())
                        ));

        //Bind Enter key to tfPassword
        btnLogin.defaultButtonProperty().bind(tfPassword.focusedProperty());


        //Show saved username and password
        if (!UserAuthentication.getInstance().isEmpty()) {
            this.tfUsername.setText(UserAuthentication.getInstance().getUsername());
            this.tfPassword.setText(UserAuthentication.getInstance().getPassword());
        }
    }


    /**
     * User clicked on Button Login
     */
    public void btnLogin() {
        //Store username and password
        UserAuthentication.getInstance().saveUserPreferences(tfUsername.getText(), tfPassword.getText());
        //Check if username and password is valid
        if (UserAuthentication.getInstance().connect()) {
            //Load Logout Scene and run main task
            SceneNavigator.loadLogoutScene();
            Main.timerTask.run();

        } else {
            //Show wrong input
            tfUsername.clear();
            tfPassword.clear();
            lblStatus.setStyle(" -fx-text-fill: red;");
            lblStatus.setText(this.resources.getString("key.loginProblem"));
            tfUsername.requestFocus();

            UserAuthentication.getInstance().deleteUserPreferences();
        }

    }

    /**
     * initialize wrapper
     */
    public void init() {
        initialize(ResourceBundle.getBundle("config.elggconnect" , Main.locale));
    }


}
