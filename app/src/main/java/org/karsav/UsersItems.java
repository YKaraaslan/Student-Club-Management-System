package org.karsav;

import java.io.Serializable;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class UsersItems implements Serializable {
    private int id;
    private String name, position, status;

    public UsersItems() {
    }

    public UsersItems(int id, String name, String position, String status) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public String getStatus() {
        return status;
    }
}

