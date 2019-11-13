package org.karsav;

import java.io.Serializable;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class ProjectsItem implements Serializable {
    private int imageResource;
    private String name;
    private String explanation;
    private String official;
    private String admin;
    private String id;

    public ProjectsItem(int imageResource, String name, String explanation, String official, String admin, String id) {
        this.imageResource = imageResource;
        this.name = name;
        this.explanation = explanation;
        this.official = official;
        this.admin = admin;
        this.id = id;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getName() {
        return name;
    }

    public String getExplanation() {
        return explanation;
    }

    public String getOfficial() {
        return official;
    }

    public String getAdmin() {
        return admin;
    }

    public String getId() {
        return id;
    }
}

