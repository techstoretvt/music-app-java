package com.example.musicapp.utils;

import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.musicapp.R;
import com.example.musicapp.activities.ChiTietNhacActivity;
import com.example.musicapp.activities.MainActivity;
import com.example.musicapp.api.ApiServiceV1;
import com.example.musicapp.fragments.fragment_chi_tiet_bh.BaiHatFragment;
import com.example.musicapp.fragments.fragment_chi_tiet_bh.LoiBaiHatFragment;
import com.example.musicapp.fragments.fragment_chi_tiet_bh.ThongTinBaiHatFragment;
import com.example.musicapp.fragments.fragment_mini_nhac.CurrentMiniNhacFragment;
import com.example.musicapp.fragments.fragment_mini_nhac.NextMiniNhacFragment;
import com.example.musicapp.modal.anhxajson.BaiHat;
import com.example.musicapp.modal.anhxajson.ResponseDefault;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MediaCustom {

    public static MediaPlayer mediaPlayer = new MediaPlayer();

    public static ArrayList<BaiHat> danhSachPhats = null;
    public static ArrayList<BaiHat> danhSachPhatsRanDom = null;

    public static int typeDanhSachPhat;

    public static String tenLoai = "";

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

            if (ChiTietNhacActivity.isChiTietNhac) {
                ChiTietNhacActivity.btnPausePlay.setImageResource(R.drawable.baseline_play_arrow_white);
            }

            if (MainActivity.dungNhac != null) {
                MainActivity.dungNhac.setImageResource(R.drawable.baseline_play_arrow_24);
            }
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
        if (MainActivity.dungNhac != null) {
            //current
            String anhBia = MediaCustom.danhSachPhats.get(position)
                    .getAnhBia();
            Glide.with(MainActivity.layoutAudio.getContext()).load(anhBia)
                    .into(CurrentMiniNhacFragment.imgNhac);
            CurrentMiniNhacFragment.tenBaiHat.setText(danhSachPhats.get(position).getTenBaiHat());


            String strTenCaSi = "";
            for (int i = 0; i < danhSachPhats.get(position).getBaiHat_caSis().size(); i++) {
                if (i == 0)
                    strTenCaSi = danhSachPhats.get(position).getBaiHat_caSis().
                            get(i).getCasi().getTenCaSi();
                else
                    strTenCaSi += ", " + danhSachPhats.get(position).getBaiHat_caSis().
                            get(i).getCasi().getTenCaSi();
            }
            CurrentMiniNhacFragment.tenCaSi.setText(strTenCaSi);

            MainActivity.dungNhac.setImageResource(R.drawable.baseline_pause_24);

            //next
            int nextPosition = (position + 1) % danhSachPhats.size();
            Log.e("nextPosition", String.valueOf(nextPosition));
            String anhBiaNext = MediaCustom.danhSachPhats.get(nextPosition)
                    .getAnhBia();
            Glide.with(MainActivity.layoutAudio.getContext()).load(anhBiaNext)
                    .into(NextMiniNhacFragment.imgNhac);
            NextMiniNhacFragment.tenBaiHat.setText(danhSachPhats.get(nextPosition).getTenBaiHat());

            for (int i = 0; i < danhSachPhats.get(nextPosition).getBaiHat_caSis().size(); i++) {
                if (i == 0)
                    strTenCaSi = danhSachPhats.get(nextPosition).getBaiHat_caSis().
                            get(i).getCasi().getTenCaSi();
                else
                    strTenCaSi += ", " + danhSachPhats.get(nextPosition).getBaiHat_caSis().
                            get(i).getCasi().getTenCaSi();
            }

            NextMiniNhacFragment.tenCaSi.setText(strTenCaSi);


            MainActivity.btnPrev.setImageResource(R.drawable.baseline_skip_previous_24);
        }

        if (ChiTietNhacActivity.tgHienTai != null) {
            ChiTietNhacActivity.tgHienTai.setText(MediaCustom.getStrCurrentTime());
            ChiTietNhacActivity.totalTime.setText(MediaCustom.strTotalTime);
            ChiTietNhacActivity.sliderProgress.setValueTo(MediaCustom.totalTime);
        }

        if (BaiHatFragment.btnThaTim != null) {
            BaiHatFragment.checkLike();
        }

        if (ThongTinBaiHatFragment.tenBaiHat != null) {
            ThongTinBaiHatFragment.getData();
        }

        if (LoiBaiHatFragment.isLoiBaiHat) {
            LoiBaiHatFragment.getLoiBaiHat();
        }

        //tang view
        tangView(danhSachPhats.get(position).getId());


        return statusPhatNhac;
    }

    public static Boolean prev() {


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
        if (MainActivity.dungNhac != null) {
            //current
            String anhBia = MediaCustom.danhSachPhats.get(position)
                    .getAnhBia();
            Glide.with(MainActivity.layoutAudio.getContext()).load(anhBia)
                    .into(CurrentMiniNhacFragment.imgNhac);
            CurrentMiniNhacFragment.tenBaiHat.setText(danhSachPhats.get(position).getTenBaiHat());

            String strTenCaSi = "";
            for (int i = 0; i < danhSachPhats.get(position).getBaiHat_caSis().size(); i++) {
                if (i == 0)
                    strTenCaSi = danhSachPhats.get(position).getBaiHat_caSis().
                            get(i).getCasi().getTenCaSi();
                else
                    strTenCaSi += ", " + danhSachPhats.get(position).getBaiHat_caSis().
                            get(i).getCasi().getTenCaSi();
            }
            CurrentMiniNhacFragment.tenCaSi.setText(strTenCaSi);

            MainActivity.dungNhac.setImageResource(R.drawable.baseline_pause_24);

            //next
            int nextPosition = (position + 1) % danhSachPhats.size();
            String anhBiaNext = MediaCustom.danhSachPhats.get(nextPosition)
                    .getAnhBia();
            Glide.with(MainActivity.layoutAudio.getContext()).load(anhBiaNext)
                    .into(NextMiniNhacFragment.imgNhac);
            NextMiniNhacFragment.tenBaiHat.setText(danhSachPhats.get(nextPosition).getTenBaiHat());

            for (int i = 0; i < danhSachPhats.get(nextPosition).getBaiHat_caSis().size(); i++) {
                if (i == 0)
                    strTenCaSi = danhSachPhats.get(nextPosition).getBaiHat_caSis().
                            get(i).getCasi().getTenCaSi();
                else
                    strTenCaSi += ", " + danhSachPhats.get(nextPosition).getBaiHat_caSis().
                            get(i).getCasi().getTenCaSi();
            }
            NextMiniNhacFragment.tenCaSi.setText(strTenCaSi);
        }


        if (position > 0) {
            MainActivity.btnPrev.setVisibility(View.VISIBLE);
        } else {
            MainActivity.btnPrev.setVisibility(View.GONE);
        }

        if (ChiTietNhacActivity.tgHienTai != null) {
            ChiTietNhacActivity.tgHienTai.setText(MediaCustom.getStrCurrentTime());
            ChiTietNhacActivity.totalTime.setText(MediaCustom.strTotalTime);
            ChiTietNhacActivity.sliderProgress.setValueTo(MediaCustom.totalTime);
        }

        if (BaiHatFragment.btnThaTim != null) {
            BaiHatFragment.checkLike();
        }

        if (ThongTinBaiHatFragment.tenBaiHat != null) {
            ThongTinBaiHatFragment.getData();
        }

        if (LoiBaiHatFragment.isLoiBaiHat) {
            LoiBaiHatFragment.getLoiBaiHat();
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

    public static void setCurrentTimeDouble(double newTime) {
        if (isData) {
            int t1 = (int) Math.floor(newTime * 1000);
            Log.e("t1", String.valueOf(t1));
            mediaPlayer.seekTo(t1);
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

    public static void tangView(String idBaiHat) {
        String header = Common.getHeader();

        ApiServiceV1.apiServiceV1.tangViewBaiHat(idBaiHat, header).enqueue(new Callback<ResponseDefault>() {
            @Override
            public void onResponse(Call<ResponseDefault> call, Response<ResponseDefault> response) {

            }

            @Override
            public void onFailure(Call<ResponseDefault> call, Throwable t) {

            }
        });
    }

    public static void ranDomDanhSach() {
        taoListRandom();

        danhSachPhatsRanDom = new ArrayList<>();
        for (int i : positionRandom) {
            danhSachPhatsRanDom.add(danhSachPhats.get(i));
        }

    }
}
