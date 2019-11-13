package org.karsav;

import java.util.Map;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class FinanceGet {
    private static Map explanation;
    private static Map creator;
    private static Map creatorId;
    private static Map responsibleId;
    private static Map responsible;
    private static Map tour;
    private static Map amount;
    private static Map id;

    public static Map getExplanation() {
        return explanation;
    }

    public static void setExplanation(Map explanation) {
        FinanceGet.explanation = explanation;
    }

    public static Map getCreator() {
        return creator;
    }

    public static void setCreator(Map creator) {
        FinanceGet.creator = creator;
    }

    public static Map getCreatorId() {
        return creatorId;
    }

    public static void setCreatorId(Map creatorId) {
        FinanceGet.creatorId = creatorId;
    }

    public static Map getResponsibleId() {
        return responsibleId;
    }

    public static void setResponsibleId(Map responsibleId) {
        FinanceGet.responsibleId = responsibleId;
    }

    public static Map getResponsible() {
        return responsible;
    }

    public static void setResponsible(Map responsible) {
        FinanceGet.responsible = responsible;
    }

    public static Map getTour() {
        return tour;
    }

    public static void setTour(Map tour) {
        FinanceGet.tour = tour;
    }

    public static Map getAmount() {
        return amount;
    }

    public static void setAmount(Map amount) {
        FinanceGet.amount = amount;
    }

    public static Map getId() {
        return id;
    }

    public static void setId(Map id) {
        FinanceGet.id = id;
    }
}
