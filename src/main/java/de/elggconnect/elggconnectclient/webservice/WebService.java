/*
 * Copyright (c) 2015. , Beaufort 8
 * released under MIT License
 * http://opensource.org/licenses/MIT
 */

package de.elggconnect.elggconnectclient.webservice;

import de.elggconnect.elggconnectclient.Main;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * WebService executes WebServicesMethods
 *
 * @author Alexander Stifel
 * @author Beaufort 8
 */
public class WebService {

    /**
     * Excute API Method if there is a network connection
     *
     * @param webServiceMethod
     * @return
     */
    public Long executeAPIMethod(IWebServiceMethod webServiceMethod) {

        try {
            URL url = new URL(Main.BASE_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.connect();

            if (con.getResponseCode() == 200) { //Check for network connection
                return webServiceMethod.execute();
            }

        } catch (Exception exception) {
            System.out.println("No network connection");

        }
        return -30L;

    }


}
