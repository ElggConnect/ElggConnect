/*
 * Copyright (c) 2015. , Beaufort 8
 * released under MIT License
 * http://opensource.org/licenses/MIT
 */

package de.elggconnect.elggconnectclient.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * TrayIcon Elgg Application
 * <p>
 * Encrypt Utility encode and decode strings
 *
 * @author Alexander Stifel
 * @author Beaufort 8
 */

class EncryptUtils {
    private static final String DEFAULT_ENCODING = "UTF-8";
    private static BASE64Encoder enc = new BASE64Encoder();
    private static BASE64Decoder dec = new BASE64Decoder();

    /**
     * base64encode
     *
     * @param text
     * @return
     */
    public static String base64encode(String text) {
        try {
            return enc.encode(text.getBytes(DEFAULT_ENCODING));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * base64decode
     *
     * @param text
     * @return
     */
    public static String base64decode(String text) {

        try {
            return new String(dec.decodeBuffer(text), DEFAULT_ENCODING);
        } catch (IOException e) {
            return null;
        }
    }
}