package com.example.musicapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.musicapp.R;
import com.example.musicapp.adapters.AdapterKhamPha;
import com.example.musicapp.modal.anhxajson.BaiHat;
import com.example.musicapp.modal.anhxajson.Casi;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChiTietThuVienFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChiTietThuVienFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;
    LinearLayoutManager manager;
    AdapterKhamPha adapter;

    public ChiTietThuVienFragment() {
        // Required empty public constructor
    }

    public static ChiTietThuVienFragment newInstance(String param1, String param2) {
        ChiTietThuVienFragment fragment = new ChiTietThuVienFragment();
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
        View view = inflater.inflate(R.layout.fragment_chi_tiet_thu_vien, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
//        manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        manager = new GridLayoutManager(getActivity(), 1);


        ArrayList<BaiHat> list = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            BaiHat bh = new BaiHat("id", "Ten bai hat", "loi",
                    "https://i.ytimg.com/vi/ygGFdo2oLqc/hqdefault.jpg?sqp=-oaymwE2CNACELwBSFXyq4qpAygIARUAAIhCGAFwAcABBvABAfgB_gmAAtAFigIMCAAQARgrIFMocjAP&rs=AOn4CLBQ6W_nMz7X9FLZihXgVO1CNCriZQ",
                    "sdfsdf",
                    new Casi("id", "ten casi", "mota", "https://i.ytimg.com/vi/ygGFdo2oLqc/hqdefault.jpg?sqp=-oaymwE2CNACELwBSFXyq4qpAygIARUAAIhCGAFwAcABBvABAfgB_gmAAtAFigIMCAAQARgrIFMocjAP&rs=AOn4CLBQ6W_nMz7X9FLZihXgVO1CNCriZQ")

            );
            list.add(bh);


        }


        adapter = new AdapterKhamPha(list, getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);

        int desiredHeightInDp = list.size() * 84;
        int desiredHeightInPixels = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, desiredHeightInDp, getResources().getDisplayMetrics()
        );
        ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
        layoutParams.height = desiredHeightInPixels;
        recyclerView.setLayoutParams(layoutParams);

        return view;
    }
}