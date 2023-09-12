package com.example.musicapp.modal.anhxajson;

public class BaiHat {
    private String id;
    private String tenBaiHat;
    private String loiBaiHat;
    private String anhBia;
    private String linkBaiHat;
    private Casi casi;

    public BaiHat(String id, String tenBaiHat, String loiBaiHat, String anhBia, String linkBaiHat, Casi casi) {
        this.id = id;
        this.tenBaiHat = tenBaiHat;
        this.loiBaiHat = loiBaiHat;
        this.anhBia = anhBia;
        this.linkBaiHat = linkBaiHat;
        this.casi = casi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTenBaiHat() {
        return tenBaiHat;
    }

    public void setTenBaiHat(String tenBaiHat) {
        this.tenBaiHat = tenBaiHat;
    }

    public String getLoiBaiHat() {
        return loiBaiHat;
    }

    public void setLoiBaiHat(String loiBaiHat) {
        this.loiBaiHat = loiBaiHat;
    }

    public String getAnhBia() {
        return anhBia;
    }

    public void setAnhBia(String anhBia) {
        this.anhBia = anhBia;
    }

    public String getLinkBaiHat() {
        return linkBaiHat;
    }

    public void setLinkBaiHat(String linkBaiHat) {
        this.linkBaiHat = linkBaiHat;
    }

    public Casi getCasi() {
        return casi;
    }

    public void setCasi(Casi casi) {
        this.casi = casi;
    }
}
