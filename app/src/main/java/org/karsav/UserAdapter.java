package org.karsav;

import android.graphics.Bitmap;

import com.google.gson.annotations.SerializedName;

/**
 * Created by YUNUS KARAASLAN
 * Mail: karaaslan.21@hotmail.com
 */
public class UserAdapter {
    private static String gorevGo;
    private static String gorevKay;
    private static String grvGo;
    private static String grvKay;
    private static String kayitGo;
    private static String kullaniciAd;
    private static String maliyeGo;
    private static String maliyeKay;
    private static String pozisyon;
    private static String projeGo;
    private static String projeKay;
    private static String refGo;
    private static String refKay;
    private static String sifre;
    private static String topGo;
    private static String topKay;
    private static String tumGorevGo;
    private static String tumProjeGo;
    private static String tumTopGo;
    @SerializedName("userId")
    private static int userId;
    private static String userName;
    private static String userSurName;
    private static String uyeGo;
    private static String uyeKay;
    private static String mail;
    private static String studentNo, phone, department, aviation, ground, marine, cyber;
    private static Bitmap userImage;

    UserAdapter() {
    }

    public static String getGorevGo() {
        return gorevGo;
    }

    public static void setGorevGo(String gorevGo) {
        UserAdapter.gorevGo = gorevGo;
    }

    public static String getGorevKay() {
        return gorevKay;
    }

    public static void setGorevKay(String gorevKay) {
        UserAdapter.gorevKay = gorevKay;
    }

    public static String getGrvGo() {
        return grvGo;
    }

    public static void setGrvGo(String grvGo) {
        UserAdapter.grvGo = grvGo;
    }

    public static String getGrvKay() {
        return grvKay;
    }

    public static void setGrvKay(String grvKay) {
        UserAdapter.grvKay = grvKay;
    }

    public static String getKayitGo() {
        return kayitGo;
    }

    public static void setKayitGo(String kayitGo) {
        UserAdapter.kayitGo = kayitGo;
    }

    public static String getKullaniciAd() {
        return kullaniciAd;
    }

    public static void setKullaniciAd(String kullaniciAd) {
        UserAdapter.kullaniciAd = kullaniciAd;
    }

    public static String getMaliyeGo() {
        return maliyeGo;
    }

    public static void setMaliyeGo(String maliyeGo) {
        UserAdapter.maliyeGo = maliyeGo;
    }

    public static String getMaliyeKay() {
        return maliyeKay;
    }

    public static void setMaliyeKay(String maliyeKay) {
        UserAdapter.maliyeKay = maliyeKay;
    }

    public static String getPozisyon() {
        return pozisyon;
    }

    public static void setPozisyon(String pozisyon) {
        UserAdapter.pozisyon = pozisyon;
    }

    public static String getProjeGo() {
        return projeGo;
    }

    public static void setProjeGo(String projeGo) {
        UserAdapter.projeGo = projeGo;
    }

    public static String getProjeKay() {
        return projeKay;
    }

    public static void setProjeKay(String projeKay) {
        UserAdapter.projeKay = projeKay;
    }

    public static String getRefGo() {
        return refGo;
    }

    public static void setRefGo(String refGo) {
        UserAdapter.refGo = refGo;
    }

    public static String getRefKay() {
        return refKay;
    }

    public static void setRefKay(String refKay) {
        UserAdapter.refKay = refKay;
    }

    public static String getSifre() {
        return sifre;
    }

    public static void setSifre(String sifre) {
        UserAdapter.sifre = sifre;
    }

    public static String getTopGo() {
        return topGo;
    }

    public static void setTopGo(String topGo) {
        UserAdapter.topGo = topGo;
    }

    public static String getTopKay() {
        return topKay;
    }

    public static void setTopKay(String topKay) {
        UserAdapter.topKay = topKay;
    }

    public static String getTumGorevGo() {
        return tumGorevGo;
    }

    public static void setTumGorevGo(String tumGorevGo) {
        UserAdapter.tumGorevGo = tumGorevGo;
    }

    public static String getTumProjeGo() {
        return tumProjeGo;
    }

    public static void setTumProjeGo(String tumProjeGo) {
        UserAdapter.tumProjeGo = tumProjeGo;
    }

    public static String getTumTopGo() {
        return tumTopGo;
    }

    public static void setTumTopGo(String tumTopGo) {
        UserAdapter.tumTopGo = tumTopGo;
    }

    public static int getUserId() {
        return userId;
    }

    public static void setUserId(int userId) {
        UserAdapter.userId = userId;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        UserAdapter.userName = userName;
    }

    public static String getUserSurName() {
        return userSurName;
    }

    public static void setUserSurName(String userSurName) {
        UserAdapter.userSurName = userSurName;
    }

    public static String getUyeGo() {
        return uyeGo;
    }

    public static void setUyeGo(String uyeGo) {
        UserAdapter.uyeGo = uyeGo;
    }

    public static String getUyeKay() {
        return uyeKay;
    }

    public static void setUyeKay(String uyeKay) {
        UserAdapter.uyeKay = uyeKay;
    }

    public static String getMail() {
        return mail;
    }

    public static void setMail(String mail) {
        UserAdapter.mail = mail;
    }

    public static String getStudentNo() {
        return studentNo;
    }

    public static void setStudentNo(String studentNo) {
        UserAdapter.studentNo = studentNo;
    }

    public static String getPhone() {
        return phone;
    }

    public static void setPhone(String phone) {
        UserAdapter.phone = phone;
    }

    public static String getDepartment() {
        return department;
    }

    public static void setDepartment(String department) {
        UserAdapter.department = department;
    }

    public static String getAviation() {
        return aviation;
    }

    public static void setAviation(String aviation) {
        UserAdapter.aviation = aviation;
    }

    public static String getGround() {
        return ground;
    }

    public static void setGround(String ground) {
        UserAdapter.ground = ground;
    }

    public static String getMarine() {
        return marine;
    }

    public static void setMarine(String marine) {
        UserAdapter.marine = marine;
    }

    public static String getCyber() {
        return cyber;
    }

    public static void setCyber(String cyber) {
        UserAdapter.cyber = cyber;
    }

    public static Bitmap getUserImage() {
        return userImage;
    }

    public static void setUserImage(Bitmap userImage) {
        UserAdapter.userImage = userImage;
    }
}
