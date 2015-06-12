/*
 * Copyright (c) 2015. , Beaufort 8
 * released under MIT License
 * http://opensource.org/licenses/MIT
 */

package de.elggconnect.elggconnectclient.util;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ElggConnect BaseUrlLoader
 * This Class loads the Base URL from a Properties file
 * ß
 *
 * @author Alexander Stifel
 * @author Beaufort 8
 */
public class PropertyLoader {

    private InputStream input = null;
    private final String configFile = "config/elggconnect.properties";
    private Properties prop = new Properties();

    public PropertyLoader() {
        this.prop = loadProperties();
    }

    /**
     * load the properties file
     *
     * @return
     */
    private Properties loadProperties() {
        try {

            input = PropertyLoader.class.getClassLoader().getResourceAsStream(configFile);

            if (input == null) {
                throw new FileNotFoundException(configFile + "does not exists. Use the example property file");
            }

            //load a properties file
            Properties properties = new Properties();
            properties.load(input);
            return properties;

        } catch (IOException ex) {
            ex.printStackTrace();
            //Exit Application
            System.exit(0);
        } finally {
            //Close input Stream
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        }

        return null;
    }


    /**
     * load Base Url from ElggConnect Property File
     *
     * @return baseurl value
     */
    public String getBaseUrl() {

        //Check if baseurl value exists
        if (prop.getProperty("baseurl") == null) {
            throw new NullPointerException("baseurl property not exists : check " + configFile);
        } else {
            return prop.getProperty("baseurl");
        }

    }

    /**
     * get Application Name for the Preferences View
     *
     * @return
     */
    public String getAppname() {

        //Check if baseurl value exists
        if (prop.getProperty("appname") == null) {
            System.err.println("appname property not exists : check " + configFile);
            throw new NullPointerException("appname property not exists : check " + configFile);
        } else {
            return prop.getProperty("appname");
        }
    }

    /**
     * get Subline Name for the Preferences View
     *
     * @return
     */
    public String getSubline() {

        //Check if slug value exists
        if (prop.getProperty("subline") == null) {
            System.err.println("subline property not exists : check " + configFile);
            throw new NullPointerException("subline property not exists : check " + configFile);
        } else {
            return prop.getProperty("subline");
        }
    }

    /**
     * @return
     */
    public String getImage() {

        //Check if image value exists
        if (prop.getProperty("image") == null) {
            System.err.println("image property not exists : check " + configFile);
            throw new NullPointerException("image property not exists : check " + configFile);
        } else {
            return prop.getProperty("image");
        }
    }


    public boolean valuesNotEmpty() {
        return !(getAppname().isEmpty() || getBaseUrl().isEmpty() || getSubline().isEmpty() || getImage().isEmpty());
    }


}
