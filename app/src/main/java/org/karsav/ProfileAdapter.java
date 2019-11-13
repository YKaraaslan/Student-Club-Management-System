package org.karsav;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
class ProfileAdapter {

    private static int postAmount, likeAmount, meetupsAmount, projectsAmount, assignmentsAmount;
    private static String status;

    public static int getPostAmount() {
        return postAmount;
    }

    public static void setPostAmount(int postAmount) {
        ProfileAdapter.postAmount = postAmount;
    }

    public static int getLikeAmount() {
        return likeAmount;
    }

    public static void setLikeAmount(int likeAmount) {
        ProfileAdapter.likeAmount = likeAmount;
    }

    public static String getStatus() {
        return status;
    }

    public static void setStatus(String status) {
        ProfileAdapter.status = status;
    }

    public static int getMeetupsAmount() {
        return meetupsAmount;
    }

    public static void setMeetupsAmount(int meetupsAmount) {
        ProfileAdapter.meetupsAmount = meetupsAmount;
    }

    public static int getProjectsAmount() {
        return projectsAmount;
    }

    public static void setProjectsAmount(int projectsAmount) {
        ProfileAdapter.projectsAmount = projectsAmount;
    }

    public static int getAssignmentsAmount() {
        return assignmentsAmount;
    }

    public static void setAssignmentsAmount(int assignmentsAmount) {
        ProfileAdapter.assignmentsAmount = assignmentsAmount;
    }
}