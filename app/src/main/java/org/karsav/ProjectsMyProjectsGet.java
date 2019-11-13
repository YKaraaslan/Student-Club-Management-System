package org.karsav;

import java.util.Map;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
class ProjectsMyProjectsGet {
    private static Map explanation;
    private static Map name, admin, members, id;

    public static Map getExplanation() {
        return explanation;
    }

    public static void setExplanation(Map explanation) {
        ProjectsMyProjectsGet.explanation = explanation;
    }

    public static Map getName() {
        return name;
    }

    public static void setName(Map name) {
        ProjectsMyProjectsGet.name = name;
    }

    public static Map getAdmin() {
        return admin;
    }

    public static void setAdmin(Map admin) {
        ProjectsMyProjectsGet.admin = admin;
    }

    public static Map getMembers() {
        return members;
    }

    public static void setMembers(Map members) {
        ProjectsMyProjectsGet.members = members;
    }

    public static Map getId() {
        return id;
    }

    public static void setId(Map id) {
        ProjectsMyProjectsGet.id = id;
    }
}

