package com.example.musicapp.modal.anhxajson;

import java.util.ArrayList;

public class CommentBaiHat {
    private String id, idBaiHat, noiDung;
    private double thoiGian, countLike;
    private ArrayList<CommentBaiHatCon> commentBHCons;

    private TaiKhoan User;

    public TaiKhoan getUser() {
        return User;
    }

    public void setUser(TaiKhoan user) {
        User = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdBaiHat() {
        return idBaiHat;
    }

    public void setIdBaiHat(String idBaiHat) {
        this.idBaiHat = idBaiHat;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public double getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(double thoiGian) {
        this.thoiGian = thoiGian;
    }

    public double getCountLike() {
        return countLike;
    }

    public void setCountLike(double countLike) {
        this.countLike = countLike;
    }

    public ArrayList<CommentBaiHatCon> getCommentBHCons() {
        return commentBHCons;
    }

    public void setCommentBHCons(ArrayList<CommentBaiHatCon> commentBHCons) {
        this.commentBHCons = commentBHCons;
    }
}
