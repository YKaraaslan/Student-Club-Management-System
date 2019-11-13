package org.karsav;

import java.util.Map;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class AssignmentsNewGet {
    private static Map responsibleNameSurname;
    private static Map responsibleId;
    private static Map content;
    private static Map name;
    private static Map category;
    private static Map fromWhom;
    private static Map dueDate;
    private static Map dateOfIssue;
    private static Map id;

    public static Map getId() {
        return id;
    }

    public static void setId(Map id) {
        AssignmentsNewGet.id = id;
    }

    public static Map getResponsibleNameSurname() {
        return responsibleNameSurname;
    }

    public static void setResponsibleNameSurname(Map responsibleNameSurname) {
        AssignmentsNewGet.responsibleNameSurname = responsibleNameSurname;
    }

    public static Map getResponsibleId() {
        return responsibleId;
    }

    public static void setResponsibleId(Map responsibleId) {
        AssignmentsNewGet.responsibleId = responsibleId;
    }

    public static Map getContent() {
        return content;
    }

    public static void setContent(Map content) {
        AssignmentsNewGet.content = content;
    }

    public static Map getName() {
        return name;
    }

    public static void setName(Map name) {
        AssignmentsNewGet.name = name;
    }

    public static Map getCategory() {
        return category;
    }

    public static void setCategory(Map category) {
        AssignmentsNewGet.category = category;
    }

    public static Map getFromWhom() {
        return fromWhom;
    }

    public static void setFromWhom(Map fromWhom) {
        AssignmentsNewGet.fromWhom = fromWhom;
    }

    public static Map getDueDate() {
        return dueDate;
    }

    public static void setDueDate(Map dueDate) {
        AssignmentsNewGet.dueDate = dueDate;
    }

    public static Map getDateOfIssue() {
        return dateOfIssue;
    }

    public static void setDateOfIssue(Map dateOfIssue) {
        AssignmentsNewGet.dateOfIssue = dateOfIssue;
    }
}
