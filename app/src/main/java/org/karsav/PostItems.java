package org.karsav;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class PostItems implements Serializable {

    private static Map amount;
    private String title;
    private String text;
    private String name;
    private String time;
    private int userId;
    private String date;
    private long id;
    private boolean photo;

    public PostItems() {
    }

    public PostItems(String title, String text, String name, String time, int userId, String date, long id, boolean photo) {
        this.title = title;
        this.text = text;
        this.name = name;
        this.time = time;
        this.userId = userId;
        this.date = date;
        this.id = id;
        this.photo = photo;
    }

    public static Map getAmount() {
        return amount;
    }

    public static void setAmount(Map amount) {
        PostItems.amount = amount;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public int getUserId() {
        return userId;
    }

    public String getDate() {
        return date;
    }

    public long getId() {
        return id;
    }

    public boolean isPhoto() {
        return photo;
    }
}
