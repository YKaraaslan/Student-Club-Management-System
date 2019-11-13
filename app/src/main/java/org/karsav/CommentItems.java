package org.karsav;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class CommentItems {
    private String name, text, date, time;
    private int userId;
    private long id;

    public CommentItems() {
    }

    public CommentItems(String name, String text, String date, String time, int userId, long id) {
        this.name = name;
        this.text = text;
        this.date = date;
        this.time = time;
        this.userId = userId;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public int getUserId() {
        return userId;
    }

    public long getId() {
        return id;
    }
}

