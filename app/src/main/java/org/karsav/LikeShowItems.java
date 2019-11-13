package org.karsav;

import java.io.Serializable;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class LikeShowItems implements Serializable {

    long id;
    private String name, title;

    public LikeShowItems() {
    }

    public LikeShowItems(String name, String title, long id) {
        this.name = name;
        this.title = title;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public long getId() {
        return id;
    }
}
