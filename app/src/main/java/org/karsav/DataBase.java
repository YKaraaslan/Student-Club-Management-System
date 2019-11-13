package org.karsav;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy.Builder;
import android.util.Log;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mysql.cj.jdbc.Blob;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
class DataBase {

    private Connection con = null;
    private Connection connect = null;
    private String url;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReference();

    boolean Begin() {
        String db = "databaseName";
        String host = "hostIp";
        int port = 0;
        url = "jdbc:mysql://" + host + ":" + port + "/" + db + "?useUnicode=yes&characterEncoding=UTF-8";
        String userId = "";
        String password = "";
        this.connect = startConnecting(userId, password);
        return this.connect == null;
    }

    String Query(String mail, String pass) throws SQLException {
        String sorgu = "SELECT * FROM gorevliler where KullaniciAd = '" + mail + "' AND Sifre = '" + pass + "' ";
        ResultSet rs = this.con.createStatement().executeQuery(sorgu);
        if (!rs.next()) {
            Disconnect();
            return "false";
        } else if (!rs.getString("Sifre").equals(pass) || !rs.getString("KullaniciAd").equals(mail)) {
            return "wrong";
        } else {
            int id = rs.getInt("ID");
            String ad = rs.getString("Isim");
            String soyad = rs.getString("Soyisim");
            UserAdapter.setUserId(id);
            UserAdapter.setUserName(ad);
            UserAdapter.setUserSurName(soyad);
            isAdmin();
            if (UserAdapter.getUyeGo() != null || UserAdapter.getKayitGo() != null || UserAdapter.getGrvGo() != null)
                RegisterUserDb();
            if (UserAdapter.getRefGo() != null)
                RegisterReferenceDb();
            if (UserAdapter.getTopGo() != null)
                MeetUpsMine_db();
            if (UserAdapter.getTumTopGo() != null)
                MeetUps_db();
            if (UserAdapter.getProjeGo() != null) {
                Projects_db();
                ProjectsMyProjectDb();
            }
            if (UserAdapter.getMaliyeGo() != null) {
                FinanceList();
                FinanceLogList();
            }
            if (UserAdapter.getGrvGo() != null)
                AssignmentsAll_db();
            if (UserAdapter.getTumGorevGo() != null)
                AssignmentsDone_db();
            if (ad != null && soyad != null)
                AssignmentsMine_db();
            setUser();
            setProfilePicture();
            return "true";
        }
    }

    private void setProfilePicture() {
        StorageReference reference = storageReference.child("ProfilePhotos/" + UserAdapter.getUserId() + ".jpg");
        reference.getBytes(Long.MAX_VALUE).addOnSuccessListener(bytes -> {
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            UserAdapter.setUserImage(bmp);
        });
    }

    private void setUser() throws SQLException {
        int id = UserAdapter.getUserId();
        String sorgu = "SELECT * FROM uyeList where ID = '" + id + "' ";
        ResultSet rs = this.con.createStatement().executeQuery(sorgu);
        String mail = "";
        String studentNo = "";
        String phone = "";
        String department = "";
        String aviation = "";
        String ground = "";
        String marine = "";
        String cyber = "";
        if (rs.next()) {
            mail = rs.getString("Mail");
            studentNo = rs.getString("OgrenciNO");
            phone = rs.getString("Telefon");
            department = rs.getString("Bolum");
            aviation = rs.getString("Hava");
            ground = rs.getString("Kara");
            marine = rs.getString("Deniz");
            cyber = rs.getString("Siber");
        }
        UserAdapter.setMail(mail);
        UserAdapter.setStudentNo(studentNo);
        UserAdapter.setPhone(phone);
        UserAdapter.setDepartment(department);
        UserAdapter.setAviation(aviation);
        UserAdapter.setGround(ground);
        UserAdapter.setMarine(marine);
        UserAdapter.setCyber(cyber);
    }

    private void isAdmin() throws SQLException {
        String pozisyon, kayitGo = null, projeGo = null, topGo = null, gorevGo = null, uyeGo = null, uyeKay = null;
        String grvGo = null, grvKay = null, refGo = null, refKay = null, tumProjeGo = null, projeKay = null, tumTopGo = null;
        String topKay = null, tumGorevGo = null, gorevKay = null, maliyeGo = null, maliyeKay = null, user = null, pass = null;
        int id = UserAdapter.getUserId();
        String sorgu = "SELECT * FROM gorevliler where ID = '" + id + "' ";
        ResultSet rs = this.con.createStatement().executeQuery(sorgu);
        if (rs.next()) {
            pozisyon = rs.getString("Pozisyon");
            kayitGo = rs.getString("KayitGo");
            projeGo = rs.getString("ProjeGo");
            topGo = rs.getString("TopGo");
            gorevGo = rs.getString("GorevGo");
            uyeGo = rs.getString("UyeGo");
            uyeKay = rs.getString("UyeKay");
            grvGo = rs.getString("GrvGo");
            grvKay = rs.getString("GrvKay");
            refGo = rs.getString("RefGo");
            refKay = rs.getString("RefKay");
            tumProjeGo = rs.getString("TumProjeGor");
            projeKay = rs.getString("ProjeKay");
            tumTopGo = rs.getString("TumTopGor");
            topKay = rs.getString("TopKay");
            tumGorevGo = rs.getString("TumGorevGor");
            gorevKay = rs.getString("GorevKay");
            maliyeGo = rs.getString("MaliyeGo");
            maliyeKay = rs.getString("MaliyeKay");
            user = rs.getString("KullaniciAd");
            pass = rs.getString("Sifre");
        } else {
            pozisyon = "Üye";
        }
        UserAdapter.setPozisyon(pozisyon);
        UserAdapter.setKayitGo(kayitGo);
        UserAdapter.setProjeGo(projeGo);
        UserAdapter.setTopGo(topGo);
        UserAdapter.setGorevGo(gorevGo);
        UserAdapter.setUyeGo(uyeGo);
        UserAdapter.setUyeKay(uyeKay);
        UserAdapter.setGrvGo(grvGo);
        UserAdapter.setGrvKay(grvKay);
        UserAdapter.setRefGo(refGo);
        UserAdapter.setRefKay(refKay);
        UserAdapter.setTumProjeGo(tumProjeGo);
        UserAdapter.setProjeKay(projeKay);
        UserAdapter.setTumTopGo(tumTopGo);
        UserAdapter.setTopKay(topKay);
        UserAdapter.setTumGorevGo(tumGorevGo);
        UserAdapter.setGorevKay(gorevKay);
        UserAdapter.setMaliyeGo(maliyeGo);
        UserAdapter.setMaliyeKay(maliyeKay);
        UserAdapter.setKullaniciAd(user);
        UserAdapter.setSifre(pass);
    }

    @SuppressLint("UseSparseArrays")
    void RegisterUserDb() throws SQLException {
        Map<Integer, Integer> id = new HashMap<>();
        Map<Integer, String> name = new HashMap<>();
        Map<Integer, String> surname = new HashMap<>();
        Map<Integer, String> fullName = new HashMap<>();
        Map<Integer, String> department = new HashMap<>();
        Map<Integer, String> telephone = new HashMap<>();
        Map<Integer, String> mail = new HashMap<>();
        Map<Integer, String> studentNo = new HashMap<>();
        Map<Integer, String> aviation = new HashMap<>();
        Map<Integer, String> ground = new HashMap<>();
        Map<Integer, String> marine = new HashMap<>();
        Map<Integer, String> cyber = new HashMap<>();
        Map<Integer, String> explanation = new HashMap<>();

        Map<Integer, String> departmentSecond = new HashMap<>();
        Map<Integer, String> phoneSecond = new HashMap<>();
        Map<Integer, String> mailSecond = new HashMap<>();
        Map<Integer, String> studentNoSecond = new HashMap<>();
        Map<Integer, String> aviationSecond = new HashMap<>();
        Map<Integer, String> groundSecond = new HashMap<>();
        Map<Integer, String> marineSecond = new HashMap<>();
        Map<Integer, String> cyberSecond = new HashMap<>();
        Map<Integer, String> explanationSecond = new HashMap<>();

        int counter = 1;
        String sorgu = "SELECT * FROM uyeList ORDER BY ID ASC";
        ResultSet rs_registers = this.con.createStatement().executeQuery(sorgu);
        while (rs_registers.next()) {
            int ourId = rs_registers.getInt("ID");
            String nameString = rs_registers.getString("Isim");
            String surnameString = rs_registers.getString("Soyisim");
            String departmentString = rs_registers.getString("Bolum");
            String phones = rs_registers.getString("Telefon");
            String mails = rs_registers.getString("Mail");
            String student_nos = rs_registers.getString("OgrenciNO");
            String aviations = rs_registers.getString("Hava");
            String grounds = rs_registers.getString("Kara");
            String marines = rs_registers.getString("Deniz");
            String cybers = rs_registers.getString("Siber");
            String exp = rs_registers.getString("Aciklama");
            String nameSurname = nameString + " " + surnameString;
            if (nameString != null) {
                id.put(counter, ourId);
                name.put(counter, nameString);
                surname.put(counter, surnameString);
                fullName.put(counter, nameSurname);
                department.put(counter, departmentString);
                telephone.put(counter, phones);
                mail.put(counter, mails);
                studentNo.put(counter, student_nos);
                aviation.put(counter, aviations);
                ground.put(counter, grounds);
                marine.put(counter, marines);
                cyber.put(counter, cybers);
                explanation.put(counter, exp);
                counter++;

                departmentSecond.put(ourId, departmentString);
                phoneSecond.put(ourId, phones);
                mailSecond.put(ourId, mails);
                studentNoSecond.put(ourId, student_nos);
                aviationSecond.put(ourId, aviations);
                groundSecond.put(ourId, grounds);
                marineSecond.put(ourId, marines);
                cyberSecond.put(ourId, cybers);
                explanationSecond.put(ourId, exp);
            }
        }
        RegisterMemberGet.setId(id);
        RegisterMemberGet.setName(name);
        RegisterMemberGet.setSurname(surname);
        RegisterMemberGet.setDepartment(department);
        RegisterMemberGet.setPhone(telephone);
        RegisterMemberGet.setMail(mail);
        RegisterMemberGet.setStudentNo(studentNo);
        RegisterMemberGet.setAviation(aviation);
        RegisterMemberGet.setGround(ground);
        RegisterMemberGet.setMarine(marine);
        RegisterMemberGet.setCyber(cyber);
        RegisterMemberGet.setExplanation(explanation);
        RegisterMemberGet.setFullName(fullName);

        RegisterOfficialGet.setDepartment(departmentSecond);
        RegisterOfficialGet.setPhone(phoneSecond);
        RegisterOfficialGet.setMail(mailSecond);
        RegisterOfficialGet.setStudentNo(studentNoSecond);
        RegisterOfficialGet.setAviation(aviationSecond);
        RegisterOfficialGet.setGround(groundSecond);
        RegisterOfficialGet.setMarine(marineSecond);
        RegisterOfficialGet.setCyber(cyberSecond);
        RegisterOfficialGet.setExplanation(explanationSecond);

        Map<Integer, Integer> idOfficial = new HashMap<>();
        Map<Integer, String> nameMap = new HashMap<>();
        Map<Integer, String> surnameMap = new HashMap<>();
        Map<Integer, String> fullNameMap = new HashMap<>();
        Map<Integer, String> register_position = new HashMap<>();
        counter = 1;
        ResultSet rs_registers2 = this.con.createStatement().executeQuery("SELECT * FROM gorevliler ORDER BY ID ASC");
        while (rs_registers2.next()) {
            int userId = rs_registers2.getInt("ID");
            String dutyName = rs_registers2.getString("Isim");
            String dutySurname = rs_registers2.getString("Soyisim");
            String full_name = dutyName + " " + dutySurname;
            String positions = rs_registers2.getString("Pozisyon");
            idOfficial.put(counter, userId);
            nameMap.put(counter, dutyName);
            surnameMap.put(counter, dutySurname);
            fullNameMap.put(counter, full_name);
            register_position.put(userId, positions);
            counter++;
        }
        RegisterOfficialGet.setPosition(register_position);
        RegisterOfficialGet.setId(idOfficial);
        RegisterOfficialGet.setNameSurname(name);
        RegisterOfficialGet.setName(nameMap);
        RegisterOfficialGet.setSurname(surnameMap);
        RegisterOfficialGet.setNameSurname(fullNameMap);
    }

    @SuppressLint("UseSparseArrays")
    void RegisterReferenceDb() throws SQLException {
        Map<Integer, String> idRef = new HashMap<>();
        Map<Integer, String> nameRef = new HashMap<>();
        Map<Integer, String> surnameRef = new HashMap<>();
        Map<Integer, String> institution = new HashMap<>();
        Map<Integer, String> profession = new HashMap<>();
        Map<Integer, String> phone = new HashMap<>();
        Map<Integer, String> mailRef = new HashMap<>();
        Map<Integer, String> explanationRef = new HashMap<>();
        Map<Integer, String> refRef = new HashMap<>();
        int counter = 1;
        ResultSet rs_registers3 = this.con.createStatement().executeQuery("SELECT * FROM referansList ORDER BY ID ASC");
        while (rs_registers3.next()) {
            idRef.put(counter, rs_registers3.getString("ID"));
            nameRef.put(counter, rs_registers3.getString("Isim"));
            surnameRef.put(counter, rs_registers3.getString("Soyisim"));
            institution.put(counter, rs_registers3.getString("Kurum"));
            profession.put(counter, rs_registers3.getString("Isi"));
            phone.put(counter, rs_registers3.getString("Telefon"));
            mailRef.put(counter, rs_registers3.getString("Mail"));
            explanationRef.put(counter, rs_registers3.getString("Aciklama"));
            refRef.put(counter, rs_registers3.getString("RfrKisi"));
            counter++;
        }
        RegisterReferenceGet.setId(idRef);
        RegisterReferenceGet.setName(nameRef);
        RegisterReferenceGet.setSurname(surnameRef);
        RegisterReferenceGet.setInstitution(institution);
        RegisterReferenceGet.setProfession(profession);
        RegisterReferenceGet.setPhone(phone);
        RegisterReferenceGet.setMail(mailRef);
        RegisterReferenceGet.setExplanation(explanationRef);
        RegisterReferenceGet.setReference(refRef);
    }

    @SuppressLint("UseSparseArrays")
    void Projects_db() throws SQLException {
        Map<Integer, String> nameForProjects = new HashMap<>();
        Map<Integer, String> explanation = new HashMap<>();
        Map<Integer, String> admin = new HashMap<>();
        Map<Integer, String> member = new HashMap<>();
        Map<Integer, String> idMap = new HashMap<>();
        int counter = 1;
        ResultSet rs_projects = this.con.createStatement().executeQuery("SELECT * FROM projeList");
        while (rs_projects.next()) {
            int id = rs_projects.getInt("ID");
            String projectName = rs_projects.getString("ProjeAd");
            String projectExplanation = rs_projects.getString("Aciklama");
            String admins = rs_projects.getString("ProjeAdmin");
            String members = rs_projects.getString("Uyeler");
            admin.put(counter, admins);
            member.put(counter, members);
            nameForProjects.put(counter, projectName);
            explanation.put(counter, projectExplanation);
            idMap.put(counter, String.valueOf(id));
            counter++;
        }
        ProjectsGet.setName(nameForProjects);
        ProjectsGet.setExplanation(explanation);
        ProjectsGet.setAdmin(admin);
        ProjectsGet.setMembers(member);
        ProjectsGet.setId(idMap);
    }

    @SuppressLint("UseSparseArrays")
    void ProjectsMyProjectDb() throws SQLException {
        Map<Integer, String> myprojects_name = new HashMap<>();
        Map<Integer, String> myprojects_exp = new HashMap<>();
        Map<Integer, String> myprojects_admin = new HashMap<>();
        Map<Integer, String> myprojects_member = new HashMap<>();
        Map<Integer, String> myprojects_id = new HashMap<>();

        int userId = UserAdapter.getUserId();
        ResultSet rs_projects2 = this.con.createStatement().executeQuery("SELECT * FROM prjUyeList WHERE ID = '" + userId + "' ");
        int counter = 0;
        while (rs_projects2.next()) {
            int id = rs_projects2.getInt("ProjeID");
            String project_name = rs_projects2.getString("ProjeAd");
            String explanationString = rs_projects2.getString("Aciklama");
            String adminString = rs_projects2.getString("ProjeAdmin");
            String membersString = rs_projects2.getString("Uyeler");
            counter++;
            myprojects_name.put(counter, project_name);
            myprojects_exp.put(counter, explanationString);
            myprojects_admin.put(counter, adminString);
            myprojects_member.put(counter, membersString);
            myprojects_id.put(counter, String.valueOf(id));
        }
        ProjectsMyProjectsGet.setName(myprojects_name);
        ProjectsMyProjectsGet.setExplanation(myprojects_exp);
        ProjectsMyProjectsGet.setAdmin(myprojects_admin);
        ProjectsMyProjectsGet.setMembers(myprojects_member);
        ProjectsMyProjectsGet.setId(myprojects_id);
        ProfileAdapter.setProjectsAmount(counter);
    }

    @SuppressLint("UseSparseArrays")
    void MeetUps_db() throws SQLException {
        Map<Integer, Integer> id = new HashMap<>();
        Map<Integer, String> nameForMeetups = new HashMap<>();
        Map<Integer, String> date = new HashMap<>();
        Map<Integer, String> topic = new HashMap<>();
        Map<Integer, String> time = new HashMap<>();
        Map<Integer, String> content = new HashMap<>();
        Map<Integer, String> decisions = new HashMap<>();
        Map<Integer, String> creator = new HashMap<>();
        Map<Integer, String> invitees = new HashMap<>();
        ResultSet rs_meetups = this.con.createStatement().executeQuery("SELECT * FROM toplantiList ORDER BY ID ASC");
        int counter = 1;
        while (rs_meetups.next()) {

            int ids = rs_meetups.getInt("ID");
            String name = rs_meetups.getString("Isim");
            String dateString = rs_meetups.getString("Tarih");
            String subject = rs_meetups.getString("Konu");
            String timeString = rs_meetups.getString("Saat");
            String contentString = rs_meetups.getString("Icerik");
            String decisionsString = rs_meetups.getString("Kararlar");
            String creatorString = rs_meetups.getString("Olusturan");
            String invited = rs_meetups.getString("Davetliler");
            id.put(counter, ids);
            nameForMeetups.put(counter, name);
            date.put(counter, dateString);
            topic.put(counter, subject);
            time.put(counter, timeString);
            content.put(counter, contentString);
            decisions.put(counter, decisionsString);
            creator.put(counter, creatorString);
            invitees.put(counter, invited);
            counter++;
        }
        MeetUpsGet.setId(id);
        MeetUpsGet.setName(nameForMeetups);
        MeetUpsGet.setDate(date);
        MeetUpsGet.setExplanation(topic);
        MeetUpsGet.setTime(time);
        MeetUpsGet.setContent(content);
        MeetUpsGet.setDecisions(decisions);
        MeetUpsGet.setCreator(creator);
        MeetUpsGet.setInvitees(invitees);
    }

    @SuppressLint("UseSparseArrays")
    void MeetUpsMine_db() throws SQLException {
        Map<Integer, Integer> id = new HashMap<>();
        Map<Integer, String> nameForMeetups = new HashMap<>();
        Map<Integer, String> date = new HashMap<>();
        Map<Integer, String> topic = new HashMap<>();
        Map<Integer, String> time = new HashMap<>();
        Map<Integer, String> content = new HashMap<>();
        Map<Integer, String> decisions = new HashMap<>();
        Map<Integer, String> creator = new HashMap<>();
        Map<Integer, String> invited = new HashMap<>();
        int userId = UserAdapter.getUserId();
        String katilmama = "";
        ResultSet rs_meetups = this.con.createStatement().executeQuery("SELECT * FROM topUyeList WHERE UyeID = '" + userId + "' AND Katilmama = '" + katilmama + "' ");
        int counter = 0;
        while (rs_meetups.next()) {
            int ids = rs_meetups.getInt("ID");
            String name = rs_meetups.getString("Isim");
            String dateString = rs_meetups.getString("Tarih");
            String subject = rs_meetups.getString("Konu");
            String timeString = rs_meetups.getString("Saat");
            String iceriks = rs_meetups.getString("Icerik");
            String kararlars = rs_meetups.getString("Kararlar");
            String olusturans = rs_meetups.getString("Olusturan");
            String memberName = rs_meetups.getString("UyeIsim");
            String memberSurname = rs_meetups.getString("UyeSoyisim");
            String memberFullName = memberName + " " + memberSurname;

            counter++;

            id.put(counter, ids);
            nameForMeetups.put(counter, name);
            date.put(counter, dateString);
            topic.put(counter, subject);
            time.put(counter, timeString);
            content.put(counter, iceriks);
            decisions.put(counter, kararlars);
            creator.put(counter, olusturans);
            invited.put(counter, memberFullName);
        }
        MeetUpsSecondGet.setId(id);
        MeetUpsSecondGet.setName(nameForMeetups);
        MeetUpsSecondGet.setDate(date);
        MeetUpsSecondGet.setExplanation(topic);
        MeetUpsSecondGet.setTime(time);
        MeetUpsSecondGet.setContent(content);
        MeetUpsSecondGet.setDecisions(decisions);
        MeetUpsSecondGet.setCreator(creator);
        MeetUpsSecondGet.setInvitees(invited);
        ProfileAdapter.setMeetupsAmount(counter);
    }

    @SuppressLint("UseSparseArrays")
    void AssignmentsAll_db() throws SQLException {
        Map<Integer, String> name = new HashMap<>();
        Map<Integer, String> category = new HashMap<>();
        Map<Integer, String> content = new HashMap<>();
        Map<Integer, String> responsibleNameSurname = new HashMap<>();
        Map<Integer, String> fromWhom = new HashMap<>();
        Map<Integer, String> dueDate = new HashMap<>();
        Map<Integer, String> responsibleId = new HashMap<>();
        Map<Integer, String> dateOfIssue = new HashMap<>();
        Map<Integer, Integer> id = new HashMap<>();

        int counter = 1;
        ResultSet rs_assignments = this.con.createStatement().executeQuery("SELECT * FROM yeniGorevList ORDER BY ID ASC");
        while (rs_assignments.next()) {
            int idInt = rs_assignments.getInt("ID");
            String nameString = rs_assignments.getString("Isim");
            String categoryString = rs_assignments.getString("Kategori");
            String contentString = rs_assignments.getString("Icerik");
            String fullName = rs_assignments.getString("GorevliAdSoyad");
            String fromWhomString = rs_assignments.getString("Kimden");
            String dueDateString = rs_assignments.getString("SonTarih");
            String responsibleIdString = rs_assignments.getString("GorevliID");
            String dateOfIssueString = rs_assignments.getString("VerildigiTarih");
            name.put(counter, nameString);
            category.put(counter, categoryString);
            content.put(counter, contentString);
            responsibleNameSurname.put(counter, fullName);
            fromWhom.put(counter, fromWhomString);
            dueDate.put(counter, dueDateString);
            dateOfIssue.put(counter, dateOfIssueString);
            responsibleId.put(counter, responsibleIdString);
            id.put(counter, idInt);
            counter++;
        }
        AssignmentsNewGet.setName(name);
        AssignmentsNewGet.setCategory(category);
        AssignmentsNewGet.setContent(content);
        AssignmentsNewGet.setResponsibleNameSurname(responsibleNameSurname);
        AssignmentsNewGet.setFromWhom(fromWhom);
        AssignmentsNewGet.setDueDate(dueDate);
        AssignmentsNewGet.setResponsibleId(responsibleId);
        AssignmentsNewGet.setDateOfIssue(dateOfIssue);
        AssignmentsNewGet.setId(id);
    }

    @SuppressLint("UseSparseArrays")
    void AssignmentsDone_db() throws SQLException {
        Map<Integer, String> name = new HashMap<>();
        Map<Integer, String> category = new HashMap<>();
        Map<Integer, String> content = new HashMap<>();
        Map<Integer, String> responsibleNameSurname = new HashMap<>();
        Map<Integer, String> fromWhom = new HashMap<>();
        Map<Integer, String> dueDate = new HashMap<>();
        Map<Integer, String> responsibleId = new HashMap<>();
        Map<Integer, String> dateOfIssue = new HashMap<>();
        Map<Integer, String> dateSaved = new HashMap<>();
        Map<Integer, String> report = new HashMap<>();
        Map<Integer, Integer> id = new HashMap<>();

        int counter = 1;
        ResultSet rs_assignmentsdone = this.con.createStatement().executeQuery("SELECT * FROM gecmisGorevList ORDER BY ID ASC");
        while (rs_assignmentsdone.next()) {
            int idInt = rs_assignmentsdone.getInt("ID");
            String nameString = rs_assignmentsdone.getString("Isim");
            String categoryString = rs_assignmentsdone.getString("Kategori");
            String contentString = rs_assignmentsdone.getString("Icerik");
            String responsibleNameSurnameString = rs_assignmentsdone.getString("GorevliAdSoyad");
            String fromWhomString = rs_assignmentsdone.getString("Kimden");
            String dueDateString = rs_assignmentsdone.getString("SonTarih");
            String repsonsibleId = rs_assignmentsdone.getString("GorevliID");
            String dateOfIssueString = rs_assignmentsdone.getString("VerildigiTarih");
            String dateSavedString = rs_assignmentsdone.getString("YapildigiTarih");
            String reportString = rs_assignmentsdone.getString("Rapor");
            name.put(counter, nameString);
            category.put(counter, categoryString);
            content.put(counter, contentString);
            responsibleNameSurname.put(counter, responsibleNameSurnameString);
            responsibleId.put(counter, repsonsibleId);
            fromWhom.put(counter, fromWhomString);
            dueDate.put(counter, dueDateString);
            dateSaved.put(counter, dateSavedString);
            dateOfIssue.put(counter, dateOfIssueString);
            report.put(counter, reportString);
            id.put(counter, idInt);
            counter++;
        }
        AssignmentsDoneGet.setName(name);
        AssignmentsDoneGet.setCategory(category);
        AssignmentsDoneGet.setContent(content);
        AssignmentsDoneGet.setResponsibleNameSurname(responsibleNameSurname);
        AssignmentsDoneGet.setFromWhom(fromWhom);
        AssignmentsDoneGet.setDueDate(dueDate);
        AssignmentsDoneGet.setResponsibleId(responsibleId);
        AssignmentsDoneGet.setDateOfIssue(dateOfIssue);
        AssignmentsDoneGet.setDateSaved(dateSaved);
        AssignmentsDoneGet.setReport(report);
        AssignmentsDoneGet.setId(id);
    }

    @SuppressLint("UseSparseArrays")
    void AssignmentsMine_db() throws SQLException {
        Map<Integer, String> name = new HashMap<>();
        Map<Integer, String> category = new HashMap<>();
        Map<Integer, String> content = new HashMap<>();
        Map<Integer, String> responsibleNameSurname = new HashMap<>();
        Map<Integer, String> fromWhom = new HashMap<>();
        Map<Integer, String> dueDate = new HashMap<>();
        Map<Integer, String> responsibleId = new HashMap<>();
        Map<Integer, String> dateOfIssue = new HashMap<>();
        Map<Integer, String> dateSaved = new HashMap<>();
        Map<Integer, String> report = new HashMap<>();
        int userId = UserAdapter.getUserId();
        Map<Integer, Integer> id = new HashMap<>();

        int counter = 0;
        ResultSet rs_assignmentmine = this.con.createStatement().executeQuery("SELECT * FROM gecmisGorevList WHERE GorevliID = '" + userId + "' ");
        while (rs_assignmentmine.next()) {
            int idInt = rs_assignmentmine.getInt("ID");
            String nameString = rs_assignmentmine.getString("Isim");
            String categoryString = rs_assignmentmine.getString("Kategori");
            String contentString = rs_assignmentmine.getString("Icerik");
            String responsibleNameSurnameString = rs_assignmentmine.getString("GorevliAdSoyad");
            String fromWhomString = rs_assignmentmine.getString("Kimden");
            String dueDateString = rs_assignmentmine.getString("SonTarih");
            String responsibleIdString = rs_assignmentmine.getString("GorevliID");
            String dateOfIssueString = rs_assignmentmine.getString("VerildigiTarih");
            String dateSavedString = rs_assignmentmine.getString("YapildigiTarih");
            String reportString = rs_assignmentmine.getString("Rapor");
            counter++;
            name.put(counter, nameString);
            category.put(counter, categoryString);
            content.put(counter, contentString);
            responsibleNameSurname.put(counter, responsibleNameSurnameString);
            responsibleId.put(counter, responsibleIdString);
            fromWhom.put(counter, fromWhomString);
            dueDate.put(counter, dueDateString);
            dateSaved.put(counter, dateSavedString);
            dateOfIssue.put(counter, dateOfIssueString);
            report.put(counter, reportString);
            id.put(counter, idInt);
        }
        AssignmentsMineGet.setName(name);
        AssignmentsMineGet.setCategory(category);
        AssignmentsMineGet.setContent(content);
        AssignmentsMineGet.setResponsibleNameSurname(responsibleNameSurname);
        AssignmentsMineGet.setFromWhom(fromWhom);
        AssignmentsMineGet.setDueDate(dueDate);
        AssignmentsMineGet.setResponsibleId(responsibleId);
        AssignmentsMineGet.setDateOfIssue(dateOfIssue);
        AssignmentsMineGet.setDateSaved(dateSaved);
        AssignmentsMineGet.setReport(report);
        AssignmentsMineGet.setId(id);
        ProfileAdapter.setAssignmentsAmount(counter);
    }

    @SuppressLint("UseSparseArrays")
    void FinanceList() throws SQLException {
        Map<Integer, String> responsible = new HashMap<>();
        Map<Integer, String> responsibleId = new HashMap<>();
        Map<Integer, String> tour = new HashMap<>();
        Map<Integer, String> explanation = new HashMap<>();
        Map<Integer, String> amount = new HashMap<>();
        Map<Integer, String> creator = new HashMap<>();
        Map<Integer, String> creatorId = new HashMap<>();
        Map<Integer, String> id = new HashMap<>();

        int counter = 1;
        ResultSet rs_finance = this.con.createStatement().executeQuery("SELECT * FROM maliyeList ORDER BY ID ASC");
        while (rs_finance.next()) {
            String idString = rs_finance.getString("ID");
            String responsibleString = rs_finance.getString("SorumluKisi");
            String responsibleIdString = rs_finance.getString("SorumluID");
            String tourString = rs_finance.getString("Tur");
            String explanationString = rs_finance.getString("Aciklama");
            String amountString = rs_finance.getString("Tutar");
            String creatorString = rs_finance.getString("Olusturan");
            String creatorIdString = rs_finance.getString("OlusturanID");

            id.put(counter, idString);
            responsible.put(counter, responsibleString);
            responsibleId.put(counter, responsibleIdString);
            tour.put(counter, tourString);
            explanation.put(counter, explanationString);
            amount.put(counter, amountString);
            creator.put(counter, creatorString);
            creatorId.put(counter, creatorIdString);
            counter++;
        }
        FinanceGet.setResponsible(responsible);
        FinanceGet.setResponsibleId(responsibleId);
        FinanceGet.setTour(tour);
        FinanceGet.setExplanation(explanation);
        FinanceGet.setAmount(amount);
        FinanceGet.setCreator(creator);
        FinanceGet.setCreatorId(creatorId);
        FinanceGet.setId(id);
    }

    @SuppressLint("UseSparseArrays")
    void FinanceLogList() throws SQLException {
        Map<Integer, String> responsible = new HashMap<>();
        Map<Integer, String> tour = new HashMap<>();
        Map<Integer, String> explanation = new HashMap<>();
        Map<Integer, String> amount = new HashMap<>();
        Map<Integer, String> id = new HashMap<>();
        int counter = 1;
        ResultSet rs_financelog = this.con.createStatement().executeQuery("SELECT * FROM maliyeLogList ORDER BY ID ASC");
        while (rs_financelog.next()) {
            String idString = rs_financelog.getString("ID");
            String responsibleString = rs_financelog.getString("SorumluKisi");
            String tourString = rs_financelog.getString("Tur");
            String explanationString = rs_financelog.getString("Aciklama");
            String amountString = rs_financelog.getString("Tutar");

            id.put(counter, idString);
            responsible.put(counter, responsibleString);
            tour.put(counter, tourString);
            explanation.put(counter, explanationString);
            amount.put(counter, amountString);
            counter++;
        }
        FinanceLogGet.setId(id);
        FinanceLogGet.setResponsible(responsible);
        FinanceLogGet.setExplanation(explanation);
        FinanceLogGet.setTour(tour);
        FinanceLogGet.setAmount(amount);
    }

    void RegisterReferenceAddToDatabase(String nameString, String surnameString, String phoneString,
                                        String mailString, String institutionString, String professionString, String reference_personString,
                                        String explanationString, byte[] blob) throws SQLException {
        int id = 1;
        ResultSet rs_reference = this.con.createStatement().executeQuery("SELECT * FROM referansList WHERE ID = (SELECT MAX(ID) FROM referansList)");
        if (rs_reference.next()) {
            id = rs_reference.getInt("ID");
            id++;
        }
        String query = "INSERT INTO referansList(ID, Isim, Soyisim, Kurum, Isi, Telefon, Mail, Aciklama, RfrKisi) " +
                "VALUES ('" + id + "', '" + nameString + "', '" + surnameString + "', '" + institutionString + "', '"
                + professionString + "', '" + phoneString + "', '" + mailString + "', '" + explanationString + "', '" + reference_personString + "') ";
        Statement statement = this.con.createStatement();
        statement.executeUpdate(query);

        Blob blob1 = (Blob) this.con.createBlob();
        blob1.setBytes(1, blob);

        PreparedStatement ps = this.con.prepareStatement("INSERT INTO referansResim (ID, Kart) VALUES(?, ?)");
        ps.setInt(1, id);
        ps.setBlob(2, blob1);
        ps.executeUpdate();
    }

    void RegisterReferenceUpdate(String idString, String nameString, String surnameString, String phoneString,
                                 String mailString, String institutionString, String professionString,
                                 String reference_personString, String explanationString, byte[] blob) throws SQLException {

        String query = "UPDATE referansList SET Isim = '" + nameString + "', Soyisim = '" + surnameString + "', Kurum = '" + institutionString + "', " +
                "Isi = '" + professionString + "', Telefon = '" + phoneString + "', Mail = '" + mailString + "', Aciklama = '" + explanationString + "', " +
                "RfrKisi = '" + reference_personString + "' WHERE ID = '" + idString + "' ";
        Statement statement = this.con.createStatement();
        statement.executeUpdate(query);

        Blob blob1 = (Blob) this.con.createBlob();
        blob1.setBytes(1, blob);

        PreparedStatement ps = this.con.prepareStatement("UPDATE referansResim set Kart = ? WHERE ID = '" + idString + "' ");
        ps.setBlob(1, blob1);
        ps.executeUpdate();
    }

    void RegisterReferenceDeleteFromDatabase(String idString) throws SQLException {
        int id = Integer.parseInt(idString);
        String query = "DELETE FROM referansList WHERE ID = '" + id + "' ";
        Statement statement = this.con.createStatement();
        statement.executeUpdate(query);

        String queryTwo = "DELETE FROM referansResim WHERE ID = '" + id + "' ";
        Statement statementTwo = this.con.createStatement();
        statementTwo.executeUpdate(queryTwo);
    }

    void RegisterMemberAddToDatabase(String nameString, String surnameString, String phoneString, String mailString, String departmentString,
                                     String studentNoString, String explanationString, String aviation, String ground, String marine, String cyber) throws SQLException {
        int id = 1;
        ResultSet rs_reference = this.con.createStatement().executeQuery("SELECT * FROM uyeList WHERE ID = (SELECT MAX(ID) FROM uyeList)");
        if (rs_reference.next()) {
            id = rs_reference.getInt("ID");
            id++;
        }
        String query = "INSERT INTO uyeList(ID, Isim, Soyisim, Bolum, Telefon, Mail, OgrenciNo, Hava, Kara, Deniz, Siber, Aciklama) " +
                "VALUES ('" + id + "', '" + nameString + "', '" + surnameString + "', '" + departmentString + "', '"
                + phoneString + "', '" + mailString + "', '" + studentNoString + "', '" + aviation + "', '" + ground + "', '" + marine + "', '" + cyber + "', '" + explanationString + "') ";
        Statement statement = this.con.createStatement();
        statement.executeUpdate(query);
    }

    void RegisterMemberDeleteFromDatabase(String idString) throws SQLException {
        int id = Integer.parseInt(idString);
        String query = "DELETE FROM uyeList WHERE ID = '" + id + "' ";
        String queried = "DELETE FROM gorevliler WHERE ID = '" + id + "' ";
        Statement statement = this.con.createStatement();
        statement.executeUpdate(query);
        statement.executeUpdate(queried);
    }

    void RegisterOfficialDeleteFromDatabase(String idString) throws SQLException {
        int id = Integer.parseInt(idString);
        String queried = "DELETE FROM gorevliler WHERE ID = '" + id + "' ";
        Statement statement = this.con.createStatement();
        statement.executeUpdate(queried);
    }

    void RegisterMemberUpdate(String idString, String nameString, String surnameString, String phoneString, String mailString, String departmentString,
                              String studentNoString, String explanationString, String aviation, String ground, String marine, String cyber) throws SQLException {

        String query = "UPDATE uyeList SET Isim = '" + nameString + "', Soyisim = '" + surnameString + "', Bolum = '" + departmentString + "', Telefon = '" + phoneString + "'," +
                "Mail = '" + mailString + "', OgrenciNo = '" + studentNoString + "', Hava = '" + aviation + "', Kara = '" + ground + "', " +
                "Deniz = '" + marine + "', Siber = '" + cyber + "', Aciklama = '" + explanationString + "' WHERE ID = '" + idString + "' ";
        Statement statement = this.con.createStatement();
        statement.executeUpdate(query);
    }

    void RegisterOfficialUpdate(String idString, String nameString, String surnameString, String positionString, String kayit_goString, String proje_goString, String top_goString, String gorev_goString, String uye_goString, String user_idString, String passwordString, String uye_kayString, String grv_goString, String grv_kayString, String ref_goString, String ref_kayString, String tum_proje_goString, String proje_kayString, String tum_top_goString, String top_kayString, String tum_gorev_goString, String gorev_kayString, String maliye_goString, String maliye_kayString) throws SQLException {

        String query = "UPDATE gorevliler SET Isim = '" + nameString + "', Soyisim = '" + surnameString + "', Pozisyon = '" + positionString + "', KayitGo = '" + kayit_goString + "', " +
                "ProjeGo = '" + proje_goString + "', TopGo = '" + top_goString + "', GorevGo = '" + gorev_goString + "', UyeGo = '" + uye_goString + "', " +
                "UyeKay = '" + uye_kayString + "', GrvGo = '" + grv_goString + "', GrvKay = '" + grv_kayString + "', UyeKay = '" + uye_kayString + "', GrvGo = '" + grv_goString + "', GrvKay = '" + grv_kayString + "', " +
                "RefGo = '" + ref_goString + "', RefKay = '" + ref_kayString + "', TumProjeGor = '" + tum_proje_goString + "', ProjeKay = '" + proje_kayString + "', TumTopGor = '" + tum_top_goString + "', TopKay = '" + top_kayString + "', " +
                "TumGorevGor = '" + tum_gorev_goString + "', GorevKay = '" + gorev_kayString + "', maliyeGo = '" + maliye_goString + "', maliyeKay = '" + maliye_kayString + "', KullaniciAd = '" + user_idString + "', Sifre = '" + passwordString + "' WHERE ID = '" + idString + "' ";
        Statement statement = this.con.createStatement();
        statement.executeUpdate(query);
    }

    void RegisterOfficialAddToDatabase(String nameString, String surnameString, String kayit_goString, String proje_goString, String top_goString, String gorev_goString, String uye_goString, String user_idString, String passwordString,
                                       String uye_kayString, String grv_goString, String grv_kayString, String ref_goString, String ref_kayString, String tum_proje_goString, String proje_kayString,
                                       String tum_top_goString, String top_kayString, String tum_gorev_goString, String gorev_kayString, String maliye_goString,
                                       String maliye_kayString, String positionString) throws SQLException {
        int id = 1;
        ResultSet rs_reference = this.con.createStatement().executeQuery("SELECT * FROM uyeList WHERE ID = (SELECT MAX(ID) FROM uyeList)");
        if (rs_reference.next()) {
            id = rs_reference.getInt("ID");
        }
        String query = "INSERT INTO gorevliler(ID, Isim, Soyisim, Pozisyon, KayitGo, ProjeGo, TopGo, GorevGo, UyeGo, UyeKay, GrvGo, GrvKay, RefGo, RefKay, TumProjeGor, ProjeKay, TumTopGor, TopKay, TumGorevGor, GorevKay, maliyeGo, maliyeKay, KullaniciAd, Sifre) " +
                "VALUES ('" + id + "', '" + nameString + "', '" + surnameString + "', '" + positionString + "', '"
                + kayit_goString + "', '" + proje_goString + "', '" + top_goString + "', '" + gorev_goString + "', '" + uye_goString + "', '" + uye_kayString + "', '" + grv_goString + "', '" + grv_kayString + "', '"
                + ref_goString + "', '" + ref_kayString + "', '" + tum_proje_goString + "', '" + proje_kayString + "', '" + tum_top_goString + "', '" + top_kayString + "', '" + tum_gorev_goString + "', '" + gorev_kayString + "', '"
                + maliye_goString + "', '" + maliye_kayString + "', '" + user_idString + "', '" + passwordString + "') ";
        Statement statement = this.con.createStatement();
        statement.executeUpdate(query);
    }

    void RegisterOfficialAddToDatabaseWithId(String idString, String nameString, String surnameString, String positionString, String kayit_goString, String proje_goString, String top_goString, String gorev_goString, String uye_goString, String user_idString, String passwordString, String uye_kayString, String grv_goString, String grv_kayString, String ref_goString, String ref_kayString, String tum_proje_goString, String proje_kayString, String tum_top_goString, String top_kayString, String tum_gorev_goString, String gorev_kayString, String maliye_goString, String maliye_kayString) throws SQLException {
        String query = "INSERT INTO gorevliler(ID, Isim, Soyisim, Pozisyon, KayitGo, ProjeGo, TopGo, GorevGo, UyeGo, UyeKay, GrvGo, GrvKay, RefGo, RefKay, TumProjeGor, ProjeKay, TumTopGor, TopKay, TumGorevGor, GorevKay, maliyeGo, maliyeKay, KullaniciAd, Sifre) " +
                "VALUES ('" + idString + "', '" + nameString + "', '" + surnameString + "', '" + positionString + "', '"
                + kayit_goString + "', '" + proje_goString + "', '" + top_goString + "', '" + gorev_goString + "', '" + uye_goString + "', '" + uye_kayString + "', '" + grv_goString + "', '" + grv_kayString + "', '"
                + ref_goString + "', '" + ref_kayString + "', '" + tum_proje_goString + "', '" + proje_kayString + "', '" + tum_top_goString + "', '" + top_kayString + "', '" + tum_gorev_goString + "', '" + gorev_kayString + "', '"
                + maliye_goString + "', '" + maliye_kayString + "', '" + user_idString + "', '" + passwordString + "') ";
        Statement statement = this.con.createStatement();
        statement.executeUpdate(query);
    }

    void RegisterMemberLookForOfficial(String idString) throws SQLException {
        String pozisyon, kayitGo = null, projeGo = null, topGo = null, gorevGo = null, uyeGo = null, uyeKay = null;
        String grvGo = null, grvKay = null, refGo = null, refKay = null, tumProjeGo = null, projeKay = null, tumTopGo = null;
        String topKay = null, tumGorevGo = null, gorevKay = null, maliyeGo = null, maliyeKay = null, user = null, pass = null;
        String name = null, surname = null;
        String sorgu = "SELECT * FROM gorevliler where ID = '" + idString + "' ";
        ResultSet rs = this.con.createStatement().executeQuery(sorgu);
        if (rs.next()) {
            name = rs.getString("Isim");
            surname = rs.getString("Soyisim");
            pozisyon = rs.getString("Pozisyon");
            kayitGo = rs.getString("KayitGo");
            projeGo = rs.getString("ProjeGo");
            topGo = rs.getString("TopGo");
            gorevGo = rs.getString("GorevGo");
            uyeGo = rs.getString("UyeGo");
            uyeKay = rs.getString("UyeKay");
            grvGo = rs.getString("GrvGo");
            grvKay = rs.getString("GrvKay");
            refGo = rs.getString("RefGo");
            refKay = rs.getString("RefKay");
            tumProjeGo = rs.getString("TumProjeGor");
            projeKay = rs.getString("ProjeKay");
            tumTopGo = rs.getString("TumTopGor");
            topKay = rs.getString("TopKay");
            tumGorevGo = rs.getString("TumGorevGor");
            gorevKay = rs.getString("GorevKay");
            maliyeGo = rs.getString("MaliyeGo");
            maliyeKay = rs.getString("MaliyeKay");
            user = rs.getString("KullaniciAd");
            pass = rs.getString("Sifre");
        } else {
            pozisyon = "Üye";
        }
        UserMemberAdapter.setName(name);
        UserMemberAdapter.setSurName(surname);
        UserMemberAdapter.setPozisyon(pozisyon);
        UserMemberAdapter.setKayitGo(kayitGo);
        UserMemberAdapter.setProjeGo(projeGo);
        UserMemberAdapter.setTopGo(topGo);
        UserMemberAdapter.setGorevGo(gorevGo);
        UserMemberAdapter.setUyeGo(uyeGo);
        UserMemberAdapter.setUyeKay(uyeKay);
        UserMemberAdapter.setGrvGo(grvGo);
        UserMemberAdapter.setGrvKay(grvKay);
        UserMemberAdapter.setRefGo(refGo);
        UserMemberAdapter.setRefKay(refKay);
        UserMemberAdapter.setTumProjeGo(tumProjeGo);
        UserMemberAdapter.setProjeKay(projeKay);
        UserMemberAdapter.setTumTopGo(tumTopGo);
        UserMemberAdapter.setTopKay(topKay);
        UserMemberAdapter.setTumGorevGo(tumGorevGo);
        UserMemberAdapter.setGorevKay(gorevKay);
        UserMemberAdapter.setMaliyeGo(maliyeGo);
        UserMemberAdapter.setMaliyeKay(maliyeKay);
        UserMemberAdapter.setKullaniciAd(user);
        UserMemberAdapter.setSifre(pass);
    }

    void ProjectsAddToDatabase(String nameString, String explanationString, String membersString, String managerString) throws SQLException {
        int id = 1;
        ResultSet rs_reference = this.con.createStatement().executeQuery("SELECT * FROM projeList WHERE ID = (SELECT MAX(ID) FROM projeList)");
        if (rs_reference.next()) {
            id = rs_reference.getInt("ID");
            id++;
        }
        String query = "INSERT INTO projeList(ID, ProjeAd, Aciklama, Uyeler, ProjeAdmin) " +
                "VALUES ('" + id + "', '" + nameString + "', '" + explanationString + "', '" + membersString + "', '" + managerString + "') ";
        Statement statement = this.con.createStatement();
        statement.executeUpdate(query);
    }

    void ProjectsDeleteFromDatabase(String idString) throws SQLException {
        int id = Integer.parseInt(idString);
        String queried = "DELETE FROM projeList WHERE ID = '" + id + "' ";
        Statement statement = this.con.createStatement();
        statement.executeUpdate(queried);
        String query = "DELETE FROM prjUyeList WHERE ProjeID = '" + id + "' ";
        Statement statements = this.con.createStatement();
        statements.executeUpdate(query);
    }

    void ProjectsUpdate(String nameString, String explanationString, String membersString, String managerString, String idString) throws SQLException {
        String query = "UPDATE projeList SET ProjeAd = '" + nameString + "', Aciklama = '" + explanationString + "', Uyeler = '" + membersString + "', ProjeAdmin = '" + managerString + "' WHERE ID = '" + idString + "' ";
        Statement statement = this.con.createStatement();
        statement.executeUpdate(query);
    }

    void ProjectsAddUserList(String[] nameSurname, String nameString, String explanationString, String membersString, String managerString) {
        String name = null;
        String surname = null;
        int id = 0;
        String studentNo = null;
        if (nameSurname.length == 2) {
            name = nameSurname[0].trim();
            surname = nameSurname[1].trim();
        } else if (nameSurname.length == 3) {
            name = nameSurname[0].trim() + " " + nameSurname[1].trim();
            surname = nameSurname[2].trim();
        }
        try {
            ResultSet resultSet = this.con.createStatement().executeQuery("SELECT * FROM uyeList WHERE Isim = '" + name + "' AND Soyisim = '" + surname + "' ");
            if (resultSet.next()) {
                id = resultSet.getInt("ID");
                studentNo = resultSet.getString("OgrenciNO");
            }
            int ids = 1;
            ResultSet rs_reference = this.con.createStatement().executeQuery("SELECT * FROM projeList WHERE ID = (SELECT MAX(ID) FROM projeList)");
            if (rs_reference.next()) {
                ids = rs_reference.getInt("ID");
            }
            if (id > 0) {
                String query = "INSERT INTO prjUyeList(ID, Isim, Soyisim, ogrenciNO, ProjeAd, projeID, ProjeAdmin, Aciklama, Uyeler) " +
                        "VALUES ('" + id + "', '" + name + "', '" + surname + "', '" + studentNo + "', '" + nameString + "', '" + ids + "', '" + managerString + "', '" + explanationString + "', '" + membersString + "') ";
                Statement statement = this.con.createStatement();
                statement.executeUpdate(query);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void ProjectsDeleteUserList(String idString) throws SQLException {
        int id = Integer.parseInt(idString);
        String queried = "DELETE FROM prjUyeList WHERE ProjeID = '" + id + "' ";
        Statement statement = this.con.createStatement();
        statement.executeUpdate(queried);
    }

    void ProjectsAddManagerToTheUserList(String nameString, String explanationString, String membersString, String managerString) {
        try {
            String[] nameSurname = managerString.split(" ");
            String name = null, surname = null;
            String studentNo = null;
            int id = 0;
            if (nameSurname.length == 2) {
                name = nameSurname[0].trim();
                surname = nameSurname[1].trim();
            } else if (nameSurname.length == 3) {
                name = nameSurname[0].trim() + " " + nameSurname[1].trim();
                surname = nameSurname[2].trim();
            }
            ResultSet resultSet = this.con.createStatement().executeQuery("SELECT * FROM uyeList WHERE Isim = '" + name + "' AND Soyisim = '" + surname + "' ");
            if (resultSet.next()) {
                id = resultSet.getInt("ID");
                studentNo = resultSet.getString("OgrenciNO");
            }
            int ids = 1;
            ResultSet rs_reference = this.con.createStatement().executeQuery("SELECT * FROM projeList WHERE ID = (SELECT MAX(ID) FROM projeList)");
            if (rs_reference.next()) {
                ids = rs_reference.getInt("ID");
            }
            if (id > 0) {
                String query = "INSERT INTO prjUyeList(ID, Isim, Soyisim, ogrenciNO, ProjeAd, projeID, ProjeAdmin, Aciklama, Uyeler) " +
                        "VALUES ('" + id + "', '" + name + "', '" + surname + "', '" + studentNo + "', '" + nameString + "', '" + ids + "', '" + managerString + "', '" + explanationString + "', '" + membersString + "') ";
                Statement statement = this.con.createStatement();
                statement.executeUpdate(query);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void MeetUpsAddToDatabase(String nameString, String topicString, String contentString, String decisionsString, String inviteesString, String userName, String dateString, String timeString) throws SQLException {
        int id = 1;
        ResultSet rs_reference = this.con.createStatement().executeQuery("SELECT * FROM toplantiList WHERE ID = (SELECT MAX(ID) FROM toplantiList)");
        if (rs_reference.next()) {
            id = rs_reference.getInt("ID");
            id++;
        }
        String query = "INSERT INTO toplantiList(ID, Isim, Konu, Icerik, Kararlar, Davetliler, Olusturan, Tarih, Saat) " +
                "VALUES ('" + id + "', '" + nameString + "', '" + topicString + "', '" + contentString + "', '" + decisionsString + "', '"
                + inviteesString + "', '" + userName + "', '" + dateString + "', '" + timeString + "') ";
        Statement statement = this.con.createStatement();
        statement.executeUpdate(query);
    }

    void MeetUpsAddInviteesToDatabase(String[] nameSurname, String nameString, String topicString, String contentString, String decisionsString, String userName, String dateString, String timeString) {
        try {
            String name = null, surname = null;
            int id = 0;
            if (nameSurname.length == 2) {
                name = nameSurname[0].trim();
                surname = nameSurname[1].trim();
            } else if (nameSurname.length == 3) {
                name = nameSurname[0].trim() + " " + nameSurname[1].trim();
                surname = nameSurname[2].trim();
            }
            ResultSet resultSet = this.con.createStatement().executeQuery("SELECT * FROM uyeList WHERE Isim = '" + name + "' AND Soyisim = '" + surname + "' ");
            if (resultSet.next()) {
                id = resultSet.getInt("ID");
            }
            int ids = 1;
            ResultSet rs_reference = this.con.createStatement().executeQuery("SELECT * FROM toplantiList WHERE ID = (SELECT MAX(ID) FROM toplantiList)");
            if (rs_reference.next()) {
                ids = rs_reference.getInt("ID");
            }
            if (id > 0) {
                String query = "INSERT INTO topUyeList(UyeID, UyeIsim, UyeSoyisim, Katilmama, ID, Isim, Konu, Icerik, Kararlar, Olusturan, Tarih, Saat, OkuDurum) " +
                        "VALUES ('" + id + "', '" + name + "', '" + surname + "', '" + "" + "', '" + ids + "', '" + nameString + "', '" + topicString + "', '" + contentString + "', '" + decisionsString + "', '"
                        + userName + "', '" + dateString + "', '" + timeString + "', '" + 1 + "') ";
                Statement statement = this.con.createStatement();
                statement.executeUpdate(query);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    void MeetUpDeleteFromDatabase(int id) throws SQLException {
        String queried = "DELETE FROM toplantiList WHERE ID = '" + id + "' ";
        Statement statement = this.con.createStatement();
        statement.executeUpdate(queried);
        String querieds = "DELETE FROM topUyeList WHERE ID = '" + id + "' ";
        Statement statements = this.con.createStatement();
        statements.executeUpdate(querieds);
    }

    void MeetUpsUpdate(String nameString, String topicString, String contentString, String decisionsString, String inviteesString, String userName, String dateString, String timeString, int id) throws SQLException {
        String query = "UPDATE toplantiList SET ID = '" + id + "', Isim = '" + nameString + "', Konu = '" + topicString + "', Icerik = '" + contentString
                + "', Kararlar = '" + decisionsString + "', Davetliler = '" + inviteesString + "', Olusturan = '" + userName + "', Tarih = '" + dateString + "', Saat = '" + timeString + "' WHERE ID = '" + id + "' ";
        Statement statement = this.con.createStatement();
        statement.executeUpdate(query);
    }

    void MeetUpsDeleteUserList(int id) throws SQLException {
        String queried = "DELETE FROM topUyeList WHERE ID = '" + id + "' ";
        Statement statement = this.con.createStatement();
        statement.executeUpdate(queried);
    }

    void Disconnect() {
        try {
            this.connect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection startConnecting(String user, String password) {
        StrictMode.setThreadPolicy(new Builder().permitAll().build());
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.con = DriverManager.getConnection(this.url, user, password);
        } catch (ClassNotFoundException se) {
            Log.e("Error2: ", se.getMessage());
        } catch (SQLException se2) {
            Log.e("Error1: ", se2.getMessage());
        } catch (Exception e) {
            Log.e("Error3: ", e.getMessage());
        }
        return this.con;
    }

    void AssignmentDeleteFromDatabase(int id) throws SQLException {
        String query = "DELETE FROM gecmisGorevList WHERE ID = '" + id + "' ";
        Statement statement = this.con.createStatement();
        statement.executeUpdate(query);
    }

    void updateAssignment(int id, String assignment_nameString, String categoryString, String contentString, String official_name_surnameString,
                          String creator, String assignments_date_dueString, int responsibleId, String assignments_date_of_issueString,
                          String assignments_date_savedString, String assignments_returnsString) throws SQLException {
        String query = "UPDATE gecmisGorevList SET ID = '" + id + "', Isim = '" + assignment_nameString + "', Kategori = '" + categoryString + "', Icerik = '" + contentString
                + "', GorevliAdSoyad = '" + official_name_surnameString + "', Kimden = '" + creator + "', SonTarih = '" + assignments_date_dueString
                + "', GorevliID = '" + responsibleId + "', VerildigiTarih = '" + assignments_date_of_issueString + "', YapildigiTarih = '" + assignments_date_savedString + "', Rapor = '" + assignments_returnsString + "' WHERE ID = '" + id + "' ";
        Statement statement = this.con.createStatement();
        statement.executeUpdate(query);
    }

    void AssignmentNewDeleteFromDatabase(int id) throws SQLException {
        String query = "DELETE FROM yeniGorevList WHERE ID = '" + id + "' ";
        Statement statement = this.con.createStatement();
        statement.executeUpdate(query);
    }

    void updateNewAssignment(int id, String assignment_nameString, String categoryString, String contentString,
                             String official_name_surnameString, String creator, String assignments_date_dueString, int responsibleId,
                             String assignments_date_of_issueString) throws SQLException {
        String query = "UPDATE yeniGorevList SET ID = '" + id + "', Isim = '" + assignment_nameString + "', Kategori = '" + categoryString + "', Icerik = '" + contentString
                + "', GorevliAdSoyad = '" + official_name_surnameString + "', Kimden = '" + creator + "', SonTarih = '" + assignments_date_dueString
                + "', GorevliID = '" + responsibleId + "', VerildigiTarih = '" + assignments_date_of_issueString + "' WHERE ID = '" + id + "' ";
        Statement statement = this.con.createStatement();
        statement.executeUpdate(query);
    }

    void AssignmentNewFinish(String name, String category, String content, String responsibleNameSurname, String creator,
                             String date_due, int responsibleID, String date_of_issue, String dateSaved, String ourReturns) throws SQLException {
        int id = 1;
        ResultSet rs_reference = this.con.createStatement().executeQuery("SELECT * FROM gecmisGorevList WHERE ID = (SELECT MAX(ID) FROM gecmisGorevList)");
        if (rs_reference.next()) {
            id = rs_reference.getInt("ID");
            id++;
        }
        if (id > 0) {
            String query = "INSERT INTO gecmisGorevList(ID, Isim, Kategori, Icerik, GorevliAdSoyad, Kimden, SonTarih, GorevliID, VerildigiTarih, YapildigiTarih, Rapor) " +
                    "VALUES ('" + id + "', '" + name + "', '" + category + "', '" + content + "', '" + responsibleNameSurname + "', '" + creator + "', '" + date_due + "', '" + responsibleID + "', '"
                    + date_of_issue + "', '" + dateSaved + "', '" + ourReturns + "') ";
            Statement statement = this.con.createStatement();
            statement.executeUpdate(query);
        }
    }

    void addAssignment(String assignment_nameString, String categoryString, String contentString, String name, String surname,
                       String fromWhom, String finalTime, String currentTime) throws SQLException {

        int id = 1;
        ResultSet rs_reference = this.con.createStatement().executeQuery("SELECT * FROM yeniGorevList WHERE ID = (SELECT MAX(ID) FROM yeniGorevList)");
        if (rs_reference.next()) {
            id = rs_reference.getInt("ID");
            id++;
        }
        int officialID = 0;
        ResultSet resultSet = this.con.createStatement().executeQuery("SELECT * FROM uyeList WHERE Isim = '" + name + "' AND Soyisim = '" + surname + "' ");
        if (resultSet.next()) {
            officialID = resultSet.getInt("ID");
        }
        String fullname = name + " " + surname;
        String query = "INSERT INTO yeniGorevList(ID, Isim, Kategori, Icerik, GorevliAdSoyad, Kimden, SonTarih, GorevliID, VerildigiTarih) " +
                "VALUES ('" + id + "', '" + assignment_nameString + "', '" + categoryString + "', '" + contentString + "', '" + fullname + "', '" + fromWhom + "', '" + finalTime + "', '" + officialID + "', '"
                + currentTime + "') ";
        Statement statement = this.con.createStatement();
        statement.executeUpdate(query);
    }

    boolean checkPersonFromDatabase(String name, String surname, String mailString, String phoneString,
                                    String studentNoString, String userId) throws SQLException {
        ResultSet rs_check = this.con.createStatement().executeQuery("SELECT * FROM uyeList WHERE Isim = '" + name + "' AND Soyisim = '" + surname + "' ");
        if (rs_check.next()) {
            boolean correct = false;
            String mail = rs_check.getString("Mail");
            String phone = rs_check.getString("Telefon");
            String studentNo = rs_check.getString("OgrenciNO");

            if (mailString.equalsIgnoreCase(mail) && phone.equalsIgnoreCase(phoneString) && studentNo.equalsIgnoreCase(studentNoString)) {
                correct = true;
                UserAdapterAnonymous.setId(rs_check.getInt("ID"));
            }

            if (correct) {
                ResultSet rs_check_second = this.con.createStatement().executeQuery("SELECT * FROM gorevliler WHERE Isim = '" + name + "' AND Soyisim = '" + surname + "' ");
                if (rs_check_second.next()) {
                    String userIdString = rs_check_second.getString("KullaniciAd");
                    return userId.equalsIgnoreCase(userIdString);
                }
            }
        }
        return false;
    }

    boolean changePassword(int id, String userIdString, String passwordString) throws SQLException {
        String query = "UPDATE gorevliler SET KullaniciAd = '" + userIdString + "', Sifre = '" + passwordString + "' WHERE ID = '" + id + "' ";
        Statement statement = this.con.createStatement();
        statement.executeUpdate(query);
        return true;
    }

    void signUp(String nameString, String surnameString, String studentNoString, String departmentString, String mailString,
                String phoneString, String aviationString, String groundString, String marineString, String cyberString,
                String explanationString, String userString, String passwordString) throws SQLException {
        int id = 1;
        ResultSet rs_reference = this.con.createStatement().executeQuery("SELECT * FROM uyeList WHERE ID = (SELECT MAX(ID) FROM uyeList)");
        if (rs_reference.next()) {
            id = rs_reference.getInt("ID");
            id++;
        }
        String query = "INSERT INTO uyeList(ID, Isim, Soyisim, Bolum, Telefon, Mail, OgrenciNO, Hava, Kara, Deniz, Siber, Aciklama) " +
                "VALUES ('" + id + "', '" + nameString + "', '" + surnameString + "', '" + departmentString + "', '" + phoneString + "', '"
                + mailString + "', '" + studentNoString + "', '" + aviationString + "', '" + groundString + "', '" + marineString + "', '" + cyberString + "', '" + explanationString + "') ";
        Statement statement = this.con.createStatement();
        statement.executeUpdate(query);

        String go = "var";

        String query_two = "INSERT INTO gorevliler(ID, Isim, Soyisim, Pozisyon, ProjeGo, TopGo, GorevGo, KullaniciAd, Sifre) " +
                "VALUES ('" + id + "', '" + nameString + "', '" + surnameString + "', '" + explanationString + "', '" + go + "', '"
                + go + "', '" + go + "', '" + userString + "', '" + passwordString + "') ";
        Statement statement_two = this.con.createStatement();
        statement_two.executeUpdate(query_two);
    }

    Blob referenceImage(String id) throws SQLException {
        ResultSet resultSet = this.con.createStatement().executeQuery("SELECT * FROM referansResim WHERE ID = '" + id + "' ");
        if (resultSet.next()) {
            return (Blob) resultSet.getBlob("Kart");
        }
        return null;
    }

    Blob financeImage(String id) throws SQLException {
        ResultSet resultSet = this.con.createStatement().executeQuery("SELECT * FROM maliyeResim WHERE ID = '" + id + "' ");
        if (resultSet.next()) {
            return (Blob) resultSet.getBlob("Makbuz");
        }
        return null;
    }

    Blob financeLogImage(String id) throws SQLException {
        ResultSet resultSet = this.con.createStatement().executeQuery("SELECT * FROM maliyeLogList WHERE ID = '" + id + "' ");
        if (resultSet.next()) {
            return (Blob) resultSet.getBlob("Makbuz");
        }
        return null;
    }

    void FinanceLogDeleteFromDatabase(String id) throws SQLException {
        String query = "DELETE FROM maliyeList WHERE ID = '" + id + "' ";
        Statement statement = this.con.createStatement();
        statement.executeUpdate(query);

        String querySecond = "DELETE FROM maliyeResim WHERE ID = '" + id + "' ";
        Statement statementSecond = this.con.createStatement();
        statementSecond.executeUpdate(querySecond);

    }

    void updateFinance(String idString, String nameString, String explanationString, String amountString, String spinner, String names, String surnames, byte[] blob) throws SQLException {
        ResultSet rs_reference = this.con.createStatement().executeQuery("SELECT * FROM uyeList WHERE Isim = '" + names + "' AND Soyisim = '" + surnames + "' ");
        int id = 0;
        if (rs_reference.next()) {
            id = rs_reference.getInt("ID");
        }
        String ourName = UserAdapter.getUserName() + " " + UserAdapter.getUserSurName();
        int ourId = UserAdapter.getUserId();
        String query = "UPDATE maliyeList SET SorumluKisi = '" + nameString + "', SorumluID = '" + id + "', Tur = '" + spinner + "', Aciklama = '" + explanationString
                + "', Tutar = '" + amountString + "', Olusturan = '" + ourName + "', OlusturanID = '" + ourId + "' WHERE ID = '" + idString + "' ";
        Statement statement = this.con.createStatement();
        statement.executeUpdate(query);

        Blob blob1 = (Blob) this.con.createBlob();
        blob1.setBytes(1, blob);

        PreparedStatement ps = this.con.prepareStatement("UPDATE maliyeResim set Makbuz = ? WHERE ID = '" + idString + "' ");
        ps.setBlob(1, blob1);
        ps.executeUpdate();
    }

    void addFinance(String nameString, String explanationString, String amountString,
                    String spinnerString, byte[] blob, String names, String surnames) throws SQLException {
        ResultSet rs_finance = this.con.createStatement().executeQuery("SELECT * FROM uyeList WHERE Isim = '" + names + "' AND Soyisim = '" + surnames + "' ");
        int id = 0;
        if (rs_finance.next()) {
            id = rs_finance.getInt("ID");
        }
        String ourName = UserAdapter.getUserName() + " " + UserAdapter.getUserSurName();
        int ourId = UserAdapter.getUserId();

        int idForFinance = 1;
        ResultSet rs_financeId = this.con.createStatement().executeQuery("SELECT * FROM maliyeList WHERE ID = (SELECT MAX(ID) FROM maliyeList)");
        if (rs_financeId.next()) {
            idForFinance = rs_financeId.getInt("ID");
            idForFinance++;
        }
        String query_two = "INSERT INTO maliyeList(ID, SorumluKisi, SorumluID, Tur, Aciklama, Tutar, Olusturan, OlusturanID) " +
                "VALUES ('" + idForFinance + "', '" + nameString + "', '" + id + "', '" + spinnerString + "', '" + explanationString + "', '"
                + amountString + "', '" + ourName + "', '" + ourId + "') ";
        Statement statement_two = this.con.createStatement();
        statement_two.executeUpdate(query_two);

        Blob blob1 = (Blob) this.con.createBlob();
        blob1.setBytes(1, blob);

        PreparedStatement ps = this.con.prepareStatement("INSERT INTO maliyeResim (ID, Makbuz) VALUES(?, ?)");
        ps.setInt(1, idForFinance);
        ps.setBlob(2, blob1);
        ps.executeUpdate();
    }

    void updateUser(int userId, String nameString, String surnameString, String departmentString,
                    String phone_numberString, String mailString, String student_noString, String userString,
                    String passwordString, String aviation, String ground, String marine, String cyber) throws SQLException {
        String query = "UPDATE uyeList SET Isim = '" + nameString + "', Soyisim = '" + surnameString + "', Bolum = '" + departmentString
                + "', Telefon = '" + phone_numberString + "', Mail = '" + mailString + "', OgrenciNO = '" + student_noString + "', Hava = '" + aviation
                + "', Kara = '" + ground + "', Deniz = '" + marine + "', Siber = '" + cyber + "' WHERE ID = '" + userId + "' ";
        Statement statement = this.con.createStatement();
        statement.executeUpdate(query);

        String querySecond = "UPDATE gorevliler SET Isim = '" + nameString + "', Soyisim = '" + surnameString + "', KullaniciAd = '" + userString
                + "', Sifre = '" + passwordString + "' WHERE ID = '" + userId + "' ";
        Statement statementSecond = this.con.createStatement();
        statementSecond.executeUpdate(querySecond);
    }

    void setUserProfileFromDatabase(int id) throws SQLException {
        String query = "SELECT * FROM gecmisGorevList where GorevliID = '" + id + "' ";
        ResultSet rs = this.con.createStatement().executeQuery(query);
        int counterForAssignments = 0;
        while (rs.next()) {
            counterForAssignments++;
        }
        String queryForProjects = "SELECT * FROM prjUyeList where ID = '" + id + "' ";
        ResultSet resultSet = this.con.createStatement().executeQuery(queryForProjects);
        int counterForProjects = 0;
        while (resultSet.next()) {
            counterForProjects++;
        }
        String queryForMeetups = "SELECT * FROM topUyeList where UyeID = '" + id + "' ";
        ResultSet resultSetForMeetups = this.con.createStatement().executeQuery(queryForMeetups);
        int counterForMeetups = 0;
        while (resultSetForMeetups.next()) {
            counterForMeetups++;
        }
        UserProfileAdapter.setAssignmentsAmount(counterForAssignments);
        UserProfileAdapter.setProjectsAmount(counterForProjects);
        UserProfileAdapter.setMeetupsAmount(counterForMeetups);
    }
}