package org.karsav;

import java.io.Serializable;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class FinanceItem implements Serializable {
    private int imageResource;
    private String name;
    private String description, responsible, id, amount, tour;

    public FinanceItem(int imageResource, String name, String description, String responsible, String id, String amount, String tour) {
        this.imageResource = imageResource;
        this.name = name;
        this.description = description;
        this.responsible = responsible;
        this.id = id;
        this.amount = amount;
        this.tour = tour;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getResponsible() {
        return responsible;
    }

    public String getId() {
        return id;
    }

    public String getAmount() {
        return amount;
    }

    public String getTour() {
        return tour;
    }
}
