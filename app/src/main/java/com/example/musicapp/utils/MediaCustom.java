package com.example.musicapp.utils;

import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.musicapp.R;
import com.example.musicapp.activities.ChiTietNhacActivity;
import com.example.musicapp.activities.MainActivity;
import com.example.musicapp.modal.anhxajson.BaiHat;
import com.example.musicapp.modal.anhxajson.DanhSachPhat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MediaCustom {

    public static MediaPlayer mediaPlayer = new MediaPlayer();

    public static ArrayList<BaiHat> danhSachPhats = null;

    public static int typeDanhSachPhat;

    public static String tenLoai;

    public static int position = -1;

    public static Boolean loading = false;
    public static Boolean isPlay = false;

    public static Boolean isData = false;

    public static String strTotalTime = "";
    public static int totalTime = 0;

    public static Boolean isRandom = false;
    public static Boolean isLoop = false;
    public static int typeLoop = 1;
    public static ArrayList<Integer> positionRandom = new ArrayList<>();

    public static Boolean phatNhac(String url) {
        try {
            Log.e(url, "");

            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            mediaPlayer.start();

            //su kiện kết thúc
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                    if (position == danhSachPhats.size() - 1 && !isLoop && !isRandom) {
                        //Xử lý khi dừng phát
                        isPlay = false;
                        if (MainActivity.dungNhac != null) {
                            MainActivity.dungNhac.setImageResource(R.drawable.baseline_play_arrow_24);
                        }

                        if (ChiTietNhacActivity.btnPausePlay != null) {
                            ChiTietNhacActivity.btnPausePlay.setImageResource(R.drawable.baseline_play_arrow_white);
                        }
                        return;
                    }


                    if (typeDanhSachPhat == 2) {
                        if (isLoop && typeLoop == 2) {
                            mediaPlayer.start();
                            return;
                        }
                    }

                    if (ChiTietNhacActivity.isChiTietNhac) {
                        ChiTietNhacActivity.btnNext.callOnClick();
                    } else {
                        next();
                    }


                }
            });

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    Log.e("vao", "");
                    loading = false;
                }
            });

            //lay tg nhac
            int duration = mediaPlayer.getDuration() / 1000;
            totalTime = duration;
            int phut = duration / 60;
            duration = duration - (60 * phut);
            strTotalTime = String.valueOf(phut) + ":";
            if (duration < 10) strTotalTime += "0" + String.valueOf(duration);
            else strTotalTime += String.valueOf(duration);

            isData = true;

            MainActivity.dungNhac.setImageResource(R.drawable.baseline_pause_24);


            isPlay = true;

            return true;

        } catch (IOException e) {
            Log.e("error phat am thanh", e.getMessage());
            return false;

        }
    }

    public static void play() {
        if (isData) {
            mediaPlayer.start();
            isPlay = true;
        }
    }

    public static void pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPlay = false;
        }
    }

    public static Boolean next() {

        int size = danhSachPhats.size();

        Boolean statusPhatNhac = false;

        if (typeDanhSachPhat == 1) {
            //kham pha
            int newPosition = (position + 1) % size;
            statusPhatNhac = phatNhac(danhSachPhats.get(newPosition).getLinkBaiHat());

            if (statusPhatNhac) {
                position = newPosition;

                if (newPosition > 0) {
                    MainActivity.btnPrev.setVisibility(View.VISIBLE);
                } else {
                    MainActivity.btnPrev.setVisibility(View.GONE);
                }

            } else {
                Toast.makeText(MainActivity.dungNhac.getContext(), "Lỗi", Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (typeDanhSachPhat == 2) {
            if (isRandom) {
                Log.e("random", "");
                if (positionRandom.size() == 0) {
                    taoListRandom();
                }
                for (int i = 0; i < positionRandom.size(); i++) {
                    Log.e(String.valueOf(positionRandom.get(i)), "");
                }

                int newNumber = (positionRandom.indexOf(position) + 1) % size;
                int newPositionRandom = positionRandom.get(newNumber);

                String url = danhSachPhats.get(newPositionRandom).getLinkBaiHat();

                statusPhatNhac = phatNhac(url);
                if (statusPhatNhac) {
                    position = newPositionRandom;
                } else {
                    Toast.makeText(MainActivity.dungNhac.getContext(), "Lỗi", Toast.LENGTH_SHORT)
                            .show();
                }

            } else {
                int newPosition = (position + 1) % size;
                statusPhatNhac = phatNhac(danhSachPhats.get(newPosition).getLinkBaiHat());
                if (statusPhatNhac) {
                    position = newPosition;
                } else {
                    Toast.makeText(MainActivity.dungNhac.getContext(), "Lỗi", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        }


        //update giao dien
        String anhBia = MediaCustom.danhSachPhats.get(position)
                .getAnhBia();
        Glide.with(MainActivity.imgNhac.getContext()).load(anhBia)
                .into(MainActivity.imgNhac);
        MainActivity.txtTenBaiHat.setText(danhSachPhats.get(position).getTenBaiHat());
        MainActivity.txtTenCaSi.setText(danhSachPhats.get(position).getCasi().getTenCaSi());
        MainActivity.dungNhac.setImageResource(R.drawable.baseline_pause_24);

        return statusPhatNhac;
    }

    public static Boolean prev() {
//        if (loading) {
//            return false;
//        }
//        loading = true;
        Boolean statusPhatNhac = false;

        int size = danhSachPhats.size();

        if (typeDanhSachPhat == 1) {
            //kham pha
            int newPosition = position - 1;
            statusPhatNhac = phatNhac(danhSachPhats.get(newPosition).getLinkBaiHat());

            if (statusPhatNhac) {
                position = newPosition;

            } else {
                Toast.makeText(MainActivity.dungNhac.getContext(), "sfsdf", Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (typeDanhSachPhat == 2) {
            if (isRandom) {
                if (positionRandom.size() == 0) {
                    taoListRandom();
                }

                int newNumber = (positionRandom.indexOf(position) - 1 + size) % size;
                int newPositionRandom = positionRandom.get(newNumber);

                String url = danhSachPhats.get(newPositionRandom).getLinkBaiHat();

                statusPhatNhac = phatNhac(url);
                if (statusPhatNhac) {
                    position = newPositionRandom;
                } else {
                    Toast.makeText(MainActivity.dungNhac.getContext(), "Lỗi", Toast.LENGTH_SHORT)
                            .show();
                }

            } else {
                int newPosition = (position - 1 + size) % size;
                statusPhatNhac = phatNhac(danhSachPhats.get(newPosition).getLinkBaiHat());
                if (statusPhatNhac) {
                    position = newPosition;
                } else {
                    Toast.makeText(MainActivity.dungNhac.getContext(), "Lỗi", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        }

        //update giao dien
        String anhBia = MediaCustom.danhSachPhats.get(position)
                .getAnhBia();
        Glide.with(MainActivity.imgNhac.getContext()).load(anhBia)
                .into(MainActivity.imgNhac);

        MainActivity.txtTenBaiHat.setText(danhSachPhats.get(position).getTenBaiHat());
        MainActivity.txtTenCaSi.setText(danhSachPhats.get(position).getCasi().getTenCaSi());
        MainActivity.dungNhac.setImageResource(R.drawable.baseline_pause_24);

        if (position > 0) {
            MainActivity.btnPrev.setVisibility(View.VISIBLE);
        } else {
            MainActivity.btnPrev.setVisibility(View.GONE);
        }

        return statusPhatNhac;
    }

    public static String getStrCurrentTime() {
        int currentTime = mediaPlayer.getCurrentPosition() / 1000;
        int phut = currentTime / 60;
        currentTime = currentTime - (60 * phut);
        String strCurrentTime = String.valueOf(phut) + ":";
        if (currentTime < 10)
            strCurrentTime += "0" + String.valueOf(currentTime);
        else strCurrentTime += String.valueOf(currentTime);

        return strCurrentTime;
    }

    public static float getFloatCurrentTime() {
        float currentTime = mediaPlayer.getCurrentPosition() / 1000;
        return currentTime;
    }

    public static void setCurrentTime(int newTime) {
        if (isData) {
            newTime = newTime * 1000;
            mediaPlayer.seekTo(newTime);
        }
    }

    public static void taoListRandom() {
        positionRandom = new ArrayList<>();
        positionRandom.add(position);
        Random random = new Random();
        while (positionRandom.size() < danhSachPhats.size()) {
            int number = random.nextInt(danhSachPhats.size());
            if (!positionRandom.contains(number))
                positionRandom.add(number);
        }
    }
}
