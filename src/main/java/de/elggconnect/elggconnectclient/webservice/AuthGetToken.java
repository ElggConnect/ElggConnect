/*
 * Copyright (c) 2015. , Beaufort 8
 * released under MIT License
 * http://opensource.org/licenses/MIT
 */

package de.elggconnect.elggconnectclient.webservice;


import de.elggconnect.elggconnectclient.util.UserAuthentication;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

/**
 * WebServiceMethod for the AuthGetToken HTTP API Method
 *
 * @author Alexander Stifel
 * @author Beaufort 8
 */

public class AuthGetToken implements IWebServiceMethod {

    private static final String APIMETHOD = "method=auth.gettoken";
    private static final String USER_AGENT = "ELGGCONNECT";
    private String authToken;
    private UserAuthentication userAuthentication = UserAuthentication.getInstance();
    private Long status = Long.valueOf(-1);
    private String username;
    private String password;


    public AuthGetToken(String username, String password) {

        this.password = password;
        this.username = username;
    }

    public String getAuthToken() {
        return authToken;
    }

    @Override
    /**
     * Run the AuthGetTokeb Web API Method
     */
    public Long execute() {

        //Build url Parameter String
        String urlParameters = APIMETHOD + "&username=" + this.username + "&password=" + this.password;

        //Try to execute the API Method
        try {

            URL url = new URL(userAuthentication.getBaseURL());
            URLConnection conn = url.openConnection();

            //add user agent to the request header
            conn.setRequestProperty("User-Agent", USER_AGENT);

            conn.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            writer.write(urlParameters);
            writer.flush();

            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            //Read the response JSON
            StringBuilder text = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                text.append(line).append("\n");
            }

            JSONObject json = (JSONObject) new JSONParser().parse(text.toString());

            this.status = (Long) json.get("status");
            if (this.status != -1L) {
                this.authToken = (String) json.get("result");

                //Save the AuthToken
                userAuthentication.setAuthToken((String) json.get("result"));
            }

            writer.close();
            reader.close();

        } catch (Exception e) {
            System.err.println(e.getMessage());

        }

        return this.status;
    }


}
