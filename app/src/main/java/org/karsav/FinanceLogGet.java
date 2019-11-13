package org.karsav;

import java.util.Map;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
class FinanceLogGet {
    private static Map explanation;
    private static Map responsible;
    private static Map tour;
    private static Map amount, id;

    public static Map getExplanation() {
        return explanation;
    }

    public static void setExplanation(Map explanation) {
        FinanceLogGet.explanation = explanation;
    }

    public static Map getResponsible() {
        return responsible;
    }

    public static void setResponsible(Map responsible) {
        FinanceLogGet.responsible = responsible;
    }

    public static Map getTour() {
        return tour;
    }

    public static void setTour(Map tour) {
        FinanceLogGet.tour = tour;
    }

    public static Map getAmount() {
        return amount;
    }

    public static void setAmount(Map amount) {
        FinanceLogGet.amount = amount;
    }

    public static Map getId() {
        return id;
    }

    public static void setId(Map id) {
        FinanceLogGet.id = id;
    }
}
