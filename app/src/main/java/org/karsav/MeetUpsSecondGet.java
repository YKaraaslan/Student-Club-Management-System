package org.karsav;

import java.util.Map;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class MeetUpsSecondGet {
    private static Map explanation;
    private static Map name;
    private static Map date;
    private static Map time, content, decisions, creator, id, invitees;

    public static Map getInvitees() {
        return invitees;
    }

    public static void setInvitees(Map invitees) {
        MeetUpsSecondGet.invitees = invitees;
    }

    public static Map getId() {
        return id;
    }

    public static void setId(Map id) {
        MeetUpsSecondGet.id = id;
    }

    public static Map getContent() {
        return content;
    }

    public static void setContent(Map content) {
        MeetUpsSecondGet.content = content;
    }

    public static Map getDecisions() {
        return decisions;
    }

    public static void setDecisions(Map decisions) {
        MeetUpsSecondGet.decisions = decisions;
    }

    public static Map getCreator() {
        return creator;
    }

    public static void setCreator(Map creator) {
        MeetUpsSecondGet.creator = creator;
    }

    public static Map getExplanation() {
        return explanation;
    }

    public static void setExplanation(Map explanation) {
        MeetUpsSecondGet.explanation = explanation;
    }

    public static Map getName() {
        return name;
    }

    public static void setName(Map name) {
        MeetUpsSecondGet.name = name;
    }

    public static Map getDate() {
        return date;
    }

    public static void setDate(Map date) {
        MeetUpsSecondGet.date = date;
    }

    public static Map getTime() {
        return time;
    }

    public static void setTime(Map time) {
        MeetUpsSecondGet.time = time;
    }
}
