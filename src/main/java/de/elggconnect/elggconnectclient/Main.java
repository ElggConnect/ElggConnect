/*
 * Copyright (c) 2015. , Beaufort 8
 * released under MIT License
 * http://opensource.org/licenses/MIT
 */

package de.elggconnect.elggconnectclient;

import de.elggconnect.elggconnectclient.controller.MainController;
import de.elggconnect.elggconnectclient.manager.StatusUserManager;
import de.elggconnect.elggconnectclient.model.StatusUserObject;
import de.elggconnect.elggconnectclient.util.*;
import de.elggconnect.elggconnectclient.webservice.AuthGetToken;
import de.elggconnect.elggconnectclient.webservice.StatusUser;
import de.elggconnect.elggconnectclient.webservice.WebService;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.util.*;

/**
 * ElggConnect Main Application Class
 * This Class stars the Application and create a TrayIcon.
 *
 * @author Alexander Stifel
 * @author Beaufort 8
 */
public class Main extends Application {

    // region Attributes
    // BASE URL and BASE_API_URL to a Elgg instance
    public static String BASE_URL;
    public static String BASE_API_URL;

    //PopupMenu for the TrayIcon
    private static final PopupMenu popup = new PopupMenu();
    //Logger
    public static TimerTask timerTask;
    private static ResourceBundle messages;
    // ImageHandler for loading the right images
    private ImageHandler imageHandler = new ImageHandler();
    // Application stage is stored so that it can be shown and hidden
    private Stage stage;
    // a timer allowing the tray icon to provide a periodic notification event.
    private Timer notificationTimer = new Timer();
    // TrayIcon
    private TrayIcon trayIcon;
    // UserAuthentication Instance
    private UserAuthentication userAuthentication = UserAuthentication
            .getInstance();
    // StatusUserManager Instance
    private StatusUserManager statusUserManager = StatusUserManager
            .getInstance();
    // Standard menu items for the PopupMenu
    private MenuItem miExit;
    private MenuItem miPreference;
    public static Locale locale;

    private Notification.Notifier notifier;


// endregion

    /**
     * Start Application
     *
     * @param args
     * @throws IOException
     * @throws AWTException
     */
    public static void main(String[] args) {


        //Load Application Properties
        PropertyLoader propertyLoader = new PropertyLoader();
        BASE_URL = propertyLoader.getBaseUrl();
        BASE_API_URL = BASE_URL + "services/api/rest/json/";
        //Get System Language
        String systemLanguage = System.getProperty("user.language");

        //Check if System Language is supported or use en as default
        if (!Objects.equals(systemLanguage, "de") || !Objects.equals(systemLanguage, "en")) {
            systemLanguage = "en";
        }
        locale = new Locale(systemLanguage);
        //Load Strings
        messages = ResourceBundle.getBundle("config.elggconnect", locale);

        //launches the JavaFX application.
        launch(args);
    }

    /**
     * sets up the javafx application and load tray icons
     *
     * @param stage
     */
    @Override
    public void start(final Stage stage) {

        // stores a reference to the stage.
        this.stage = stage;
        // Set Title
        this.stage.setTitle(messages.getString("key.preferences"));
        // Set Icon the Application Icon
        this.stage.getIcons().add(imageHandler.getApplicationIcon());
        // Windows is not resizable
        this.stage.setResizable(false);

        // Load trayIcon for each status
        try {
            // Load Preferences FXML and set up the tray icon
            this.stage.setScene(new Scene(loadApplicationPanes()));
            // load and set the css to Scene
            this.stage.getScene().getStylesheets().setAll(getClass().getResource("/css/style.css").toExternalForm());

            // instructs the javafx system not to exit implicitly when the
            // Preferences window is closed.
            Platform.setImplicitExit(false);

            // sets up the tray icon (using awt code run on the swing thread).
            javax.swing.SwingUtilities.invokeLater(this::addAppToTray);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        //init notifier
        notifier = Notification.Notifier.INSTANCE;
    }

    /**
     * Loads the mainPane fxml layout.
     * Sets up the scene switching SceneNavigator.
     * Loads the login and logout scene into the scene navigator.
     *
     * @return the main pane with default view.
     * @throws IOException if the pane could not be loaded.
     */
    private Pane loadApplicationPanes() throws IOException {


        FXMLLoader loader = new FXMLLoader();
        //setResource Bundle
        loader.setResources(ResourceBundle.getBundle("config.elggconnect", locale));


        //Load main fxml layout
        Pane mainPane = loader.load(getClass().getResource(SceneNavigator.MAIN).openStream());
        MainController mainController = loader.getController();
        SceneNavigator.setController(mainController);

        //Load Login and Logout Scenes
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource(SceneNavigator.LOGIN));
        loginLoader.setResources(ResourceBundle.getBundle("config.elggconnect", locale));
        FXMLLoader logoutLoader = new FXMLLoader(getClass().getResource(SceneNavigator.LOGOUT));
        logoutLoader.setResources(ResourceBundle.getBundle("config.elggconnect", locale));
        //Store the Scenes and Controller into the SceneNavigator
        SceneNavigator.setLoginScene(loginLoader.load(), loginLoader.getController());
        SceneNavigator.setLogoutScene(logoutLoader.load(), logoutLoader.getController());
        //show login view as default
        SceneNavigator.loadLoginScene();

        return mainPane;
    }

    /**
     * Sets up a system tray icon for the application.
     */
    private void addAppToTray() {

        try {
            // ensure awt toolkit is initialized.
            Toolkit.getDefaultToolkit();

            // app requires system tray support, just exit if there is no
            // support.
            if (!SystemTray.isSupported()) {
                System.out.println("No system tray support, application exiting.");
                Platform.exit();
            }

            // set up a system tray icon.
            SystemTray tray = SystemTray.getSystemTray();
            this.trayIcon = new TrayIcon(imageHandler.getTrayiconInactive(),
                    "ElggConnect");

            //Sets the auto-size property to fit the space on the tray
            this.trayIcon.setImageAutoSize(true);

            // if the user double-clicks on the tray icon, show the preferences stage.
            this.trayIcon.addActionListener(event -> Platform
                    .runLater(this::showPreferences));

            //Set up the  Standard menu items for the PopupMenu
            this.miPreference = new MenuItem(messages.getString("key.preferences"));
            this.miPreference.addActionListener(event -> Platform
                    .runLater(this::showPreferences));

            // to really exit the application, the user must go to the system
            // tray icon and select the exit option, this will shutdown JavaFX and remove
            // the tray icon (removing the tray icon will also shut down AWT).
            this.miExit = new MenuItem(messages.getString("key.quit"));
            this.miExit.addActionListener(event -> {
                this.notificationTimer.cancel();
                Platform.exit();
                tray.remove(this.trayIcon);
            });

            // setup the popup menu for the application.
            popup.add(this.miPreference);
            popup.addSeparator();
            popup.add(this.miExit);
            this.trayIcon.setPopupMenu(popup);

            // create a timer which periodically displays a notification
            // message.
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(() -> {
                        //Check if Username and Password exist
                        if (userAuthentication.isEmpty()) {
                            SceneNavigator.loadLoginScene();

                            //Build Default PopupMenu
                            popup.removeAll();
                            popup.addSeparator();
                            popup.add(miPreference);
                            popup.addSeparator();
                            popup.add(miExit);
                            trayIcon.setPopupMenu(Main.popup);
                            trayIcon.setImage(imageHandler.getTrayiconInactive());

                            showPreferences();
                        } else {
                            SceneNavigator.loadLogoutScene();
                            userAuthentication.connect();
                            checkUserStatusNow();
                        }

                    });
                }
            };

            notificationTimer.schedule(
                    timerTask, 0, // 0 seconds after Application start
                    180_000 // every 3 minutes

            );
            // add the application tray icon to the system tray.
            tray.add(trayIcon);

        } catch (AWTException e) {
            System.err.println("Unable to init system tray" + e.getMessage());

        }
    }

    private void checkUserStatusNow() {
        WebService webService = new WebService();
        //Run StatusUser Web API Method
        Long result = webService.executeAPIMethod(new StatusUser(
                userAuthentication.getAuthToken()));

        if (result == 0) { // OK
            updateTrayIcon();
        } else if (result == -20) { // Token is not valid get a new one
            UserAuthentication userAuthentication = UserAuthentication.getInstance();
            webService.executeAPIMethod(new AuthGetToken(userAuthentication.getUsername(), userAuthentication.getPassword()));
            updateTrayIcon();
        } else if (result == -30) { //Problem
            System.out.println("No Internet Connection");
            this.trayIcon.setImage(imageHandler.getTrayiconError());
        } else {
            System.out.println("Problem while executing WebService UserStatus");
            this.trayIcon.setImage(imageHandler.getTrayiconError());

        }

    }

    /**
     * Shows the application stage and ensures that it is brought ot the front
     * of all stages.
     */
    private void showPreferences() {
        if (stage != null) {
            stage.show();
            stage.toFront();
        }
    }

    /**
     * Updates the TrayIcon if there is new content
     */
    private void updateTrayIcon() {
        // Check if there are unread Content
        if (StatusUserManager.getInstance().unreadStatusUserObjects()) {
            this.trayIcon.setImage(imageHandler.getTrayiconNotification());
            updateTrayIconMenu();
        } else {
            updateTrayIconMenu();
            this.trayIcon.setImage(imageHandler.getTrayiconActive());
        }

    }

    /**
     * Updates the Tray icon Menu depends on the UserStatus Request
     */
    private void updateTrayIconMenu() {

        java.util.List<MenuItem> statusUserMenuItems = new ArrayList<>();
        Main.popup.removeAll(); // Remove all MenuItems

        // Add all StatusObjects to a list
        for (StatusUserObject statusUserObject : statusUserManager
                .getStatusUserObjects()) {

            //Check notifications
            checkNotification(statusUserObject);


            if (statusUserObject.getCount() == 0) {
                MenuItem menuItem = new MenuItem(statusUserObject.getName());
                menuItem.addActionListener(event -> URLHandler.openURL(BASE_URL
                        + statusUserObject.getUrl()));
                menuItem.setName(statusUserObject.getType());
                statusUserMenuItems.add(menuItem);

            } else {

                MenuItem menuItem = new MenuItem(statusUserObject.getName() + ": "
                        + statusUserObject.getCount());
                menuItem.addActionListener(event -> URLHandler.openURL(BASE_URL
                        + statusUserObject.getUrl()));
                menuItem.setName(statusUserObject.getType());
                statusUserMenuItems.add(menuItem);
            }

        }

        //Add a Separator if Item type changed
        for (int i = 0; i < statusUserMenuItems.size(); i++) {
            Main.popup.add(statusUserMenuItems.get(i));
            if (i + 1 < statusUserMenuItems.size()) {
                if (!statusUserMenuItems.get(i).getName().equals(statusUserMenuItems.get(i + 1).getName())) {
                    Main.popup.addSeparator();
                }
            }
        }

        //Add Standard MenuItems
        Main.popup.addSeparator();
        Main.popup.add(miPreference);
        Main.popup.addSeparator();
        Main.popup.add(this.miExit);

        this.trayIcon.setPopupMenu(Main.popup);

    }

    /**
     * Notifies User if there is unread content
     * check notification settings
     */
    private void checkNotification(StatusUserObject so) {

        if (userAuthentication.getNotification()){
            if (so.getType().equals("notification") && so.getNewCount() > 0) {
                notifier.notify(new Notification(so.getName(), so.getText(), so.getUrl()));
            }
        }


    }


}
