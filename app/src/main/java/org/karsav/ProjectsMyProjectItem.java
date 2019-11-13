package org.karsav;

import java.io.Serializable;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class ProjectsMyProjectItem implements Serializable {
    private int imageResource;
    private String name, description, admin, members, id;

    public ProjectsMyProjectItem(int imageResource, String name, String description, String admin, String members, String id) {
        this.imageResource = imageResource;
        this.name = name;
        this.description = description;
        this.admin = admin;
        this.members = members;
        this.id = id;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getAdmin() {
        return admin;
    }

    public String getMembers() {
        return members;
    }

    public String getId() {
        return id;
    }
}

