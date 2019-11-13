package org.karsav;

import java.io.Serializable;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
class RegisterReferenceItem implements Serializable {
    private int imageResource;
    private String id, name, surname, institution, profession, phone, mail, explanation, reference;

    public RegisterReferenceItem(int imageResource, String id, String name, String surname, String institution, String profession, String phone, String mail, String explanation, String reference) {
        this.imageResource = imageResource;
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.institution = institution;
        this.profession = profession;
        this.phone = phone;
        this.mail = mail;
        this.explanation = explanation;
        this.reference = reference;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getInstitution() {
        return institution;
    }

    public String getProfession() {
        return profession;
    }

    public String getPhone() {
        return phone;
    }

    public String getMail() {
        return mail;
    }

    public String getExplanation() {
        return explanation;
    }

    public String getReference() {
        return reference;
    }
}