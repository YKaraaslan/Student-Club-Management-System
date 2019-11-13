package org.karsav;

import java.io.Serializable;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class RegisterOfficialItem implements Serializable {
    int id;
    private int imageResource;
    private String name, surname;
    private String row;
    private String department, phone, mail, studentNo, aviation, ground, marine, cyber, explanation;

    public RegisterOfficialItem(int imageResource, String name, String surname, String row, String department, String phone, String mail, String studentNo, String aviation, String ground, String marine, String cyber, String explanation, int id) {
        this.imageResource = imageResource;
        this.name = name;
        this.surname = surname;
        this.row = row;
        this.department = department;
        this.phone = phone;
        this.mail = mail;
        this.studentNo = studentNo;
        this.aviation = aviation;
        this.ground = ground;
        this.marine = marine;
        this.cyber = cyber;
        this.explanation = explanation;
        this.id = id;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getRow() {
        return row;
    }

    public String getDepartment() {
        return department;
    }

    public String getPhone() {
        return phone;
    }

    public String getMail() {
        return mail;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public String getAviation() {
        return aviation;
    }

    public String getGround() {
        return ground;
    }

    public String getMarine() {
        return marine;
    }

    public String getCyber() {
        return cyber;
    }

    public String getExplanation() {
        return explanation;
    }

    public int getId() {
        return id;
    }
}
