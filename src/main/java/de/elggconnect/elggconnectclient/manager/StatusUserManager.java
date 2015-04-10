/*
 * Copyright (c) 2015. , Beaufort 8
 * released under MIT License
 * http://opensource.org/licenses/MIT
 */

package de.elggconnect.elggconnectclient.manager;


import de.elggconnect.elggconnectclient.model.StatusUserObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TrayIcon Elgg Application
 *
 * @author Alexander Stifel
 * @author Beaufort 8
 */
public class StatusUserManager {

    private static StatusUserManager ourInstance = new StatusUserManager();
    private List<StatusUserObject> statusUserObjects;

    private StatusUserManager() {
        statusUserObjects = new ArrayList<>();
    }

    //Singleton
    public static StatusUserManager getInstance() {
        return ourInstance;
    }

    public List<StatusUserObject> getStatusUserObjects() {
        return statusUserObjects;
    }

    public void setStatusUserObjects(List<StatusUserObject> statusUserObjects) {
        this.statusUserObjects = statusUserObjects;
    }

    public void addStatusUserObject(StatusUserObject statusUserObject) {
        this.statusUserObjects.add(statusUserObject);
        Collections.sort(this.statusUserObjects);
    }

    /**
     * Check for unread content
     *
     * @return
     */
    public boolean unreadStatusUserObjects() {

        for (StatusUserObject statusUserObject : this.statusUserObjects) {
            if (statusUserObject.getCount() > 0) {
                return true;
            }
        }
        return false;
    }

}
