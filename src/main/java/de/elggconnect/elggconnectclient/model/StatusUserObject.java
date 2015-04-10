/*
 * Copyright (c) 2015. , Beaufort 8
 * released under MIT License
 * http://opensource.org/licenses/MIT
 */

package de.elggconnect.elggconnectclient.model;

/**
 * StatusUserObject Model
 *
 * @author Alexander Stifel
 * @author Beaufort 8
 */
public class StatusUserObject implements Comparable<StatusUserObject> {

    private String type;
    private Long count;
    private String name;
    private String url;


    public StatusUserObject(String type, String name, Long count, String url) {
        this.type = type;
        this.name = name;
        this.count = count;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getType() {
        return type;
    }

    public Long getCount() {
        return count;
    }

    public String getName() {
        return name;
    }


    @Override
    public int compareTo(StatusUserObject o) {
        StatusUserObject statusUserObject = this;
        return statusUserObject.getType().compareTo(o.getName());
    }
}
