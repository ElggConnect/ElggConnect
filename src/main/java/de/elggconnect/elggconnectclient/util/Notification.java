/*
 * Copyright (c) 2015. , Beaufort 8
 * released under MIT License
 * http://opensource.org/licenses/MIT
 */
package de.elggconnect.elggconnectclient.util;

import de.elggconnect.elggconnectclient.Main;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;


/**
 * Custom Notification for ElggConnect
 * <p>
 *
 * @author Alexander Stifel
 * @author Beaufort 8
 */
public class Notification {

    public final String TITLE;
    public final String MESSAGE;
    public final String URL;
    public static final Image ICON = new Image(Notification.class.getResourceAsStream("/images/png/logo.png"));
    public final Image IMAGE;


    /**
     * Constructor
     *
     * @param TITLE
     * @param MESSAGE
     */
    public Notification(final String TITLE, final String MESSAGE, final String URL) {
        this.TITLE = TITLE;
        this.MESSAGE = MESSAGE;
        this.IMAGE = ICON;
        this.URL = URL;

    }


    /**
     * Inner Classes for Notification
     */
    public enum Notifier {
        INSTANCE;
        private static final double ICON_WIDTH = 24;
        private static final double ICON_HEIGHT = 24;
        private static double width = 325;
        private static double height = 80;
        private static double offsetX = 10;
        private static double offsetY = 45;
        private static double spacingY = 10;
        private static Pos popupLocation = Pos.TOP_RIGHT;
        private static Stage stageRef = null;
        private Duration popupLifetime;
        private Stage stage;
        private Scene scene;
        private ObservableList<Popup> popups;


        /**
         * Constructor for inner class
         */
        Notifier() {
            init();
            initGraphics();
        }


        /**
         * Initialization
         */
        private void init() {
            popupLifetime = Duration.millis(6000);
            popups = FXCollections.observableArrayList();
        }

        /**
         * Initialization
         */
        private void initGraphics() {
            scene = new Scene(new Region());
            scene.setFill(null);
            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());

            stage = new Stage();
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.setScene(scene);
        }


        /**
         * Method to stop and close the notification
         */
        public void stop() {
            popups.clear();
            stage.close();
        }


        /**
         * Show the given Notification on the screen
         *
         * @param NOTIFICATION
         */
        public void notify(final Notification NOTIFICATION) {
            preOrder();
            showPopup(NOTIFICATION);
        }

        /**
         * Show a Notification with the given parameters on the screen
         *
         * @param TITLE
         * @param MESSAGE
         */
        public void notify(final String TITLE, final String MESSAGE, final String URL) {
            notify(new Notification(TITLE, MESSAGE, URL));
        }

        /**
         * Reorder the popup Notifications on screen so that the latest Notification will stay on top
         */
        private void preOrder() {
            if (popups.isEmpty()) return;
            for (int i = 0; i < popups.size(); i++) {
                switch (popupLocation) {
                    case TOP_LEFT:
                    case TOP_CENTER:
                    case TOP_RIGHT:
                        popups.get(i).setY(popups.get(i).getY() + height + spacingY);
                        break;
                    default:
                        popups.get(i).setY(popups.get(i).getY() - height - spacingY);
                }
            }
        }

        /**
         * Creates and shows a popup with the data from the given Notification object
         *
         * @param NOTIFICATION
         */
        private void showPopup(final Notification NOTIFICATION) {


            ImageView icon = new ImageView(NOTIFICATION.IMAGE);
            icon.setFitWidth(ICON_WIDTH);
            icon.setFitHeight(ICON_HEIGHT);

            Hyperlink link = new Hyperlink(NOTIFICATION.MESSAGE, icon);
            link.getStyleClass().add("message");

            Label title = new Label(NOTIFICATION.TITLE);
            title.getStyleClass().add("title");


            VBox popupLayout = new VBox();
            popupLayout.setSpacing(10);
            popupLayout.setPadding(new Insets(10, 10, 10, 10));
            popupLayout.getChildren().addAll(title, link);

            StackPane popupContent = new StackPane();
            popupContent.setPrefSize(width, height);
            popupContent.getStyleClass().add("notification");
            popupContent.getChildren().addAll(popupLayout);


            final Popup POPUP = new Popup();
            POPUP.setX(getX());
            POPUP.setY(getY());
            POPUP.getContent().add(popupContent);


            popups.add(POPUP);

            // Add a timeline for popup fade out
            KeyValue fadeOutBegin = new KeyValue(POPUP.opacityProperty(), 1.0);
            KeyValue fadeOutEnd = new KeyValue(POPUP.opacityProperty(), 0.0);

            KeyFrame kfBegin = new KeyFrame(Duration.ZERO, fadeOutBegin);
            KeyFrame kfEnd = new KeyFrame(Duration.millis(500), fadeOutEnd);


            //timeline
            Timeline timeline = new Timeline(kfBegin, kfEnd);
            timeline.setDelay(popupLifetime);

            //Timeline for clicking notification
            Timeline timelineClose = new Timeline(kfBegin, kfEnd);
            timelineClose.setDelay(Duration.millis(500));

            //Close Notification on click
            popupContent.setOnMouseClicked(evt ->
                            timelineClose.play()
            );

            // Handle Hyperlink event.
            link.setOnAction((event) -> {
                URLHandler.openURL(Main.BASE_URL
                        + NOTIFICATION.URL);
                timelineClose.play();

            });

            timelineClose.setOnFinished(actionEvent -> Platform.runLater(() -> {
                POPUP.hide();
                popups.remove(POPUP);
            }));


            timeline.setOnFinished(actionEvent -> Platform.runLater(() -> {
                POPUP.hide();
                popups.remove(POPUP);
            }));

            if (stage.isShowing()) {
                stage.toFront();
            } else {
                stage.show();
            }

            POPUP.show(stage);
            timeline.play();
        }

        private double getX() {
            if (null == stageRef) return calcX(0.0, Screen.getPrimary().getBounds().getWidth());

            return calcX(stageRef.getX(), stageRef.getWidth());
        }

        private double getY() {
            if (null == stageRef) return calcY(0.0, Screen.getPrimary().getBounds().getHeight());

            return calcY(stageRef.getY(), stageRef.getHeight());
        }

        private double calcX(final double LEFT, final double TOTAL_WIDTH) {
            switch (popupLocation) {
                case TOP_LEFT:
                case CENTER_LEFT:
                case BOTTOM_LEFT:
                    return LEFT + offsetX;
                case TOP_CENTER:
                case CENTER:
                case BOTTOM_CENTER:
                    return LEFT + (TOTAL_WIDTH - width) * 0.5 - offsetX;
                case TOP_RIGHT:
                case CENTER_RIGHT:
                case BOTTOM_RIGHT:
                    return LEFT + TOTAL_WIDTH - width - offsetX;
                default:
                    return 0.0;
            }
        }

        private double calcY(final double TOP, final double TOTAL_HEIGHT) {
            switch (popupLocation) {
                case TOP_LEFT:
                case TOP_CENTER:
                case TOP_RIGHT:
                    return TOP + offsetY;
                case CENTER_LEFT:
                case CENTER:
                case CENTER_RIGHT:
                    return TOP + (TOTAL_HEIGHT - height) / 2 - offsetY;
                case BOTTOM_LEFT:
                case BOTTOM_CENTER:
                case BOTTOM_RIGHT:
                    return TOP + TOTAL_HEIGHT - height - offsetY;
                default:
                    return 0.0;
            }
        }
    }

}