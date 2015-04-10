/*
 * Copyright (c) 2015. , Beaufort 8
 * released under MIT License
 * http://opensource.org/licenses/MIT
 */

package de.elggconnect.elggconnectclient.controller;

import de.elggconnect.elggconnectclient.util.PropertyLoader;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;


/**
 * MainController - Class
 * <p>
 * Controller for the Main Stage Layout. The sceneHolder can displaying different Scenes.
 *
 * @author Alexander Stifel
 * @author Beaufort 8
 */
public class MainController {

    @FXML
    private StackPane sceneHolder;
    @FXML
    private Text appname;
    @FXML
    private ImageView logo;
    @FXML
    private Text subline;


    @FXML
    /**
     * Set up the Main View with configured Values
     */
    void initialize() {
        PropertyLoader propertyLoader = new PropertyLoader();
        if (propertyLoader.valuesNotEmpty()) {
            this.appname.setText(propertyLoader.getAppname());
            this.subline.setText(propertyLoader.getSubline());
            this.logo.setImage(new Image(propertyLoader.getImage()));
        }

    }

    /**
     * Replaces the Scene displayed in the Scene holder with a new Scene.
     *
     * @param node the Scene node to be swapped in.
     */
    public void setScene(Node node) {
        this.sceneHolder.getChildren().setAll(node);
    }


}




