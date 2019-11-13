package org.karsav;

import java.io.Serializable;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class MeetUpsItem implements Serializable {
    int id;
    private int imageResource;
    private String name;
    private String topic, content, decision, creator, invitees, date, time;

    public MeetUpsItem(int imageResource, String name, String topic, String content, String decision, String creator, String invitees, String date, String time, int id) {
        this.imageResource = imageResource;
        this.name = name;
        this.topic = topic;
        this.content = content;
        this.decision = decision;
        this.creator = creator;
        this.invitees = invitees;
        this.date = date;
        this.time = time;
        this.id = id;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getName() {
        return name;
    }

    public String getTopic() {
        return topic;
    }

    public String getContent() {
        return content;
    }

    public String getDecision() {
        return decision;
    }

    public String getCreator() {
        return creator;
    }

    public String getInvitees() {
        return invitees;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public int getId() {
        return id;
    }
}
