package com.example.musicapp.modal.anhxajson;

import java.util.Comparator;

public class BaiHatComparator implements Comparator<BaiHat> {

    @Override
    public int compare(BaiHat bh1, BaiHat bh2) {
        return bh1.getTenBaiHat().compareTo(bh2.getTenBaiHat());
    }
}
