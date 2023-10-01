package com.example.musicapp.modal.anhxajson;

public class CommentBaiHatCon {
    private String id, idCommentCha, noiDung, nameUserReply;
    private double thoiGian, countLike;
    private TaiKhoan User;

    public String getId() {
        return id;
    }

    public TaiKhoan getUser() {
        return User;
    }

    public void setUser(TaiKhoan user) {
        User = user;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdCommentCha() {
        return idCommentCha;
    }

    public void setIdCommentCha(String idCommentCha) {
        this.idCommentCha = idCommentCha;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public String getNameUserReply() {
        return nameUserReply;
    }

    public void setNameUserReply(String nameUserReply) {
        this.nameUserReply = nameUserReply;
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
}
