package org.karsav;

import java.io.Serializable;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class AssignmentsItem implements Serializable {
    private int imageResource;
    private String name;
    private String content, creator, dateOfIssue, dueDate, dateSaved, returns;
    private int id;
    private String responsibleName;
    private int responsibleId;
    private String category;

    public AssignmentsItem(int imageResource, String name, String content, String creator, String dateOfIssue, String dueDate, String dateSaved, String returns, int id, String responsibleName, int responsibleId, String category) {
        this.imageResource = imageResource;
        this.name = name;
        this.content = content;
        this.creator = creator;
        this.dateOfIssue = dateOfIssue;
        this.dueDate = dueDate;
        this.dateSaved = dateSaved;
        this.returns = returns;
        this.id = id;
        this.responsibleName = responsibleName;
        this.responsibleId = responsibleId;
        this.category = category;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public String getCreator() {
        return creator;
    }

    public String getDateOfIssue() {
        return dateOfIssue;
    }

    public String getDueDate() {
        return dueDate;
    }

    public String getDateSaved() {
        return dateSaved;
    }

    public String getReturns() {
        return returns;
    }

    public int getId() {
        return id;
    }

    public String getResponsibleName() {
        return responsibleName;
    }

    public int getResponsibleId() {
        return responsibleId;
    }

    public String getCategory() {
        return category;
    }
}