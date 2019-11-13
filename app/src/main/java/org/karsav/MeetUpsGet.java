package org.karsav;

import java.util.Map;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class MeetUpsGet {
    private static Map explanation;
    private static Map name;
    private static Map date;
    private static Map time, content, decisions, creator, id, invitees;

    public static Map getInvitees() {
        return invitees;
    }

    public static void setInvitees(Map invitees) {
        MeetUpsGet.invitees = invitees;
    }

    public static Map getId() {
        return id;
    }

    public static void setId(Map id) {
        MeetUpsGet.id = id;
    }

    public static Map getContent() {
        return content;
    }

    public static void setContent(Map content) {
        MeetUpsGet.content = content;
    }

    public static Map getDecisions() {
        return decisions;
    }

    public static void setDecisions(Map decisions) {
        MeetUpsGet.decisions = decisions;
    }

    public static Map getCreator() {
        return creator;
    }

    public static void setCreator(Map creator) {
        MeetUpsGet.creator = creator;
    }

    public static Map getExplanation() {
        return explanation;
    }

    public static void setExplanation(Map explanation) {
        MeetUpsGet.explanation = explanation;
    }

    public static Map getName() {
        return name;
    }

    public static void setName(Map name) {
        MeetUpsGet.name = name;
    }

    public static Map getDate() {
        return date;
    }

    public static void setDate(Map date) {
        MeetUpsGet.date = date;
    }

    public static Map getTime() {
        return time;
    }

    public static void setTime(Map time) {
        MeetUpsGet.time = time;
    }
}
