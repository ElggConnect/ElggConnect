/*
 * Copyright (c) 2015. , Beaufort 8
 * released under MIT License
 * http://opensource.org/licenses/MIT
 */

package de.elggconnect.elggconnectclient.webservice;


import de.elggconnect.elggconnectclient.manager.StatusUserManager;
import de.elggconnect.elggconnectclient.model.StatusUserObject;
import de.elggconnect.elggconnectclient.util.UserAuthentication;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

/**
 * WebServiceMethod for the StatusUser HTTP API Method
 *
 * @author Alexander Stifel
 * @author Beaufort 8
 */
public class StatusUser implements IWebServiceMethod {

    private static final String APIMETHOD = "?method=status.user";
    private static final String USER_AGENT = "ELGGCONNECT";
    private String authToken;
    private UserAuthentication userAuthentication = UserAuthentication.getInstance();
    private StatusUserManager statusUserManager = StatusUserManager.getInstance();
    private Long status = Long.valueOf(-1);


    /**
     * Constructor
     *
     * @param authToken
     */
    public StatusUser(String authToken) {
        this.authToken = authToken;
    }


    /**
     * Run the StatusUser Web API Method
     */
    @Override
    public Long execute() {

        //Build URL for API Method
        String urlParameters = APIMETHOD + "&auth.token=" + this.authToken;
        String url = userAuthentication.getBaseURL() + urlParameters;

        //Try to execute the API Method

        try {

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");
            //add user agent to the request header
            con.setRequestProperty("User-Agent", USER_AGENT);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            //Read the response JSON
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JSONObject json = (JSONObject) new JSONParser().parse(response.toString());
            this.status = (Long) json.get("status");

            if (this.status != -1L) {
                //Handle the JSON Response
                handleStatusUserResponse((JSONArray) json.get("result"));
            }


        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return this.status;
    }


    /**
     * handle the StatusUser JSON Response and store it into the StatusManager
     * <p>
     * Remove the old Data and fill it with new one
     *
     * @param result
     */
    private void handleStatusUserResponse(JSONArray result) {

        LinkedList<JSONObject> statusUserObjectList = new LinkedList<>();

        //Save JSON Objects from result array
        for (Object aResult : result) {
            JSONObject object = (JSONObject) aResult;
            statusUserObjectList.add((JSONObject) object.get("object"));
        }

        //Clear StatusUserObjects
        StatusUserManager.getInstance().getStatusUserObjects().clear();

        //save all UserStatus objects
        statusUserObjectList.forEach(this::saveStatusUserObject);

    }

    /**
     * Helper Method to convert JSON Response to StatusUserObject
     *
     * @param object
     */
    private void saveStatusUserObject(JSONObject object) {

        //get Object information from JSONObject
        String type = (String) object.get("type");
        String name = (String) object.get("name");
        String url = (String) object.get("url");
        Long count = (Long) object.get("count");

        String text = (String) object.get("text");
        Long newCount = (Long) object.get("new");

        if (type.equals("notification")) {
            //Save StatusObjects into Singleton List
            this.statusUserManager.addStatusUserObject(new StatusUserObject(type, name, count, url, text, newCount));
        } else {
            //Save StatusObjects into Singleton List
            this.statusUserManager.addStatusUserObject(new StatusUserObject(type, name, count, url));
        }

    }
}
