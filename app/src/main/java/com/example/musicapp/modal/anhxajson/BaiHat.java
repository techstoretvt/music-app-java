package com.example.musicapp.modal.anhxajson;

import java.util.ArrayList;

public class BaiHat {
    private String id;
    private String tenBaiHat;
    private String loiBaiHat;
    private String anhBia;
    private String linkBaiHat;
    private String linkMV;
    private ArrayList<BaiHat_CaSi> baiHat_caSis;

    private double thoiGian;

    public BaiHat(String id, String tenBaiHat, String loiBaiHat, String anhBia, String linkBaiHat,
                  ArrayList<BaiHat_CaSi> baiHat_caSis) {
        this.id = id;
        this.tenBaiHat = tenBaiHat;
        this.loiBaiHat = loiBaiHat;
        this.anhBia = anhBia;
        this.linkBaiHat = linkBaiHat;
        this.linkMV = linkMV;
        this.baiHat_caSis = baiHat_caSis;
        this.thoiGian = thoiGian;
    }

    public ArrayList<BaiHat_CaSi> getBaiHat_caSis() {
        return baiHat_caSis;
    }

    public void setBaiHat_caSis(ArrayList<BaiHat_CaSi> baiHat_caSis) {
        this.baiHat_caSis = baiHat_caSis;
    }

    public String getLinkMV() {
        return linkMV;
    }

    public void setLinkMV(String linkMV) {
        this.linkMV = linkMV;
    }

    public double getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(double thoiGian) {
        this.thoiGian = thoiGian;
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


}
