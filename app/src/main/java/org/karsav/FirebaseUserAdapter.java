package org.karsav;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class FirebaseUserAdapter {
    String name, status, position;
    int id;

    public FirebaseUserAdapter(String name, String status, String position, int id) {
        this.name = name;
        this.status = status;
        this.position = position;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getPosition() {
        return position;
    }

    public int getId() {
        return id;
    }
}
