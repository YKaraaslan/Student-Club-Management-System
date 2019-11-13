package org.karsav;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class LikeItems {
    private String name;
    private String title;
    private int id;
    private String date;
    private String time;

    public LikeItems() {
    }

    public LikeItems(String name, String title, int id, String date, String time) {
        this.name = name;
        this.title = title;
        this.id = id;
        this.date = date;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}

