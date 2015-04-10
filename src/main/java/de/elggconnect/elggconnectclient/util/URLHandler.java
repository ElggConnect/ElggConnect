/*
 * Copyright (c) 2015. , Beaufort 8
 * released under MIT License
 * http://opensource.org/licenses/MIT
 */

package de.elggconnect.elggconnectclient.util;

import java.awt.*;
import java.net.URI;


/**
 * Helper Class for open URL with OS default Browser
 *
 * @author Alexander Stifel
 * @author Beaufort 8
 */
public class URLHandler {

    /**
     * Try to open Url with default browser
     *
     * @param url
     */
    public static void openURL(String url) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
