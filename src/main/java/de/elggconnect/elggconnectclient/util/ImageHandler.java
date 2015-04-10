/*
 * Copyright (c) 2015. , Beaufort 8
 * released under MIT License
 * http://opensource.org/licenses/MIT
 */

package de.elggconnect.elggconnectclient.util;

import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * ImageHandler is a helper class for getting the right Images based on OS.
 *
 * @author Alexander Stifel
 * @author Beaufort 8
 */
public class ImageHandler {

    private final static String TRAYICON_INACTIVE = "menu-inactive.png";
    private final static String TRAYICON_ACTIVE = "menu-active.png";
    private final static String TRAYICON_NOTIFICATION = "menu-notification.png";
    private final static String TRAYICON_ERROR = "menu-error.png";
    // location for the application icon
    private static final String DOCKICON = "/images/png/dock.png";
    //Store the os
    private final String OS;

    /**
     * Constructor saves the right Operation System String in OS
     */
    public ImageHandler() {
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            this.OS = "windows/";
        } else {
            this.OS = "mac/";
        }
    }

    /**
     * Returns the inactive trayIcon
     *
     * @return
     * @throws IOException
     */
    public BufferedImage getTrayiconInactive() {
        return loadTrayIcon("/images/" + OS + TRAYICON_INACTIVE);
    }

    /**
     * Returns the active trayIcon
     *
     * @return
     * @throws IOException
     */
    public BufferedImage getTrayiconActive() {
        return loadTrayIcon("/images/" + OS + TRAYICON_ACTIVE);
    }

    /**
     * Returns the notification trayIcon
     *
     * @return
     * @throws IOException
     */
    public BufferedImage getTrayiconNotification() {
        return loadTrayIcon("/images/" + OS + TRAYICON_NOTIFICATION);
    }

    /**
     * Returns the error trayIcon
     *
     * @return
     * @throws IOException
     */
    public BufferedImage getTrayiconError() {
        return loadTrayIcon("/images/" + OS + TRAYICON_ERROR);
    }

    /**
     * Loads the TrayIcon
     *
     * @param path
     * @return
     */
    private BufferedImage loadTrayIcon(String path) {
        try {
            return ImageIO.read(this.getClass()
                    .getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns the Application Icon
     *
     * @return
     * @throws IOException
     */
    public Image getApplicationIcon() {
        return new javafx.scene.image.Image(this.getClass()
                .getResourceAsStream(DOCKICON));
    }
}
