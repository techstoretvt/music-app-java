package com.example.musicapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.musicapp.R;
import com.example.musicapp.activities.MainActivity;
import com.google.android.material.textfield.TextInputEditText;

import io.socket.emitter.Emitter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DKTuXaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DKTuXaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private boolean isPlay = true;

    public DKTuXaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DKTuXaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DKTuXaFragment newInstance(String param1, String param2) {
        DKTuXaFragment fragment = new DKTuXaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_d_k_tu_xa, container, false);

        ImageView btnNext = view.findViewById(R.id.btnNext);
        ImageView btnPlayPause = view.findViewById(R.id.btnPlayPause);
        ImageView btnPrev = view.findViewById(R.id.btnPrev);
        TextInputEditText ipId = view.findViewById(R.id.ipId);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = ipId.getText().toString();
                MainActivity.webSocketClient.socket.emit("next-music-mobile", id);
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = ipId.getText().toString();
                MainActivity.webSocketClient.socket.emit("prev-music-mobile", id);
            }
        });

        btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlay) {
                    String id = ipId.getText().toString();
                    btnPlayPause.setImageResource(R.drawable.baseline_play_arrow_24);
                    MainActivity.webSocketClient.socket.emit("pause-music-mobile", id);
                } else {
                    String id = ipId.getText().toString();
                    btnPlayPause.setImageResource(R.drawable.baseline_pause_24);
                    MainActivity.webSocketClient.socket.emit("resume-music-mobile", id);
                }
                isPlay = !isPlay;
            }
        });


        return view;
    }
}