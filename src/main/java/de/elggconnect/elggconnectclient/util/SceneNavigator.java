/*
 * Copyright (c) 2015. , Beaufort 8
 * released under MIT License
 * http://opensource.org/licenses/MIT
 */

package de.elggconnect.elggconnectclient.util;

import de.elggconnect.elggconnectclient.controller.LoginController;
import de.elggconnect.elggconnectclient.controller.LogoutController;
import de.elggconnect.elggconnectclient.controller.MainController;
import javafx.scene.Node;

/**
 * Utility class for controlling navigation between scenes.
 * <p>
 * All methods on the navigator are static to facilitate
 * simple access from anywhere in the application.
 */
public class SceneNavigator {

    //  constants for fxml layouts managed by the navigator.
    public static final String MAIN = "/fxml/main.fxml";
    public static final String LOGIN = "/fxml/login.fxml";
    public static final String LOGOUT = "/fxml/logout.fxml";

    //Store scenes
    private static Node loginScene;
    private static Node logoutScene;

    private static LoginController loginController;
    private static LogoutController logoutController;

    /**
     * The main application layout controller.
     */
    private static MainController mainController;

    public static void setLoginScene(Node node, LoginController controller) {
        loginScene = node;
        loginController = controller;
    }


    //endregion

    public static void setLogoutScene(Node node, LogoutController controller) {
        logoutScene = node;
        logoutController = controller;
    }

    /**
     * Stores the main controller for later use in navigation tasks.
     *
     * @param mainController the main application layout controller.
     */
    public static void setController(MainController mainController) {
        SceneNavigator.mainController = mainController;
    }


    /**
     * Loads the Login Scene
     */
    public static void loadLoginScene() {
        mainController.setScene(loginScene);
        loginController.init();

    }

    /**
     * Load the Logout Scene
     */
    public static void loadLogoutScene() {
        mainController.setScene(logoutScene);
        logoutController.init();
    }

}