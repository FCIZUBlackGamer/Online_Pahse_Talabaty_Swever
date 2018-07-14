package com.talabaty.swever.admin.Montagat.AddMontag;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.talabaty.swever.admin.Montagat.FragmentMontag;
import com.talabaty.swever.admin.R;

import java.util.ArrayList;
import java.util.List;

import yuku.ambilwarna.AmbilWarnaDialog;

public class AddMontag extends Fragment implements CompoundButton.OnCheckedChangeListener {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<ColorCode> colorCodes;
    List<ColorCode> temp;
    Button choose_color, back;
    ToggleButton size[];
    static int color = 0xffffff00;
    FragmentManager fragmentManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_montag, container, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.color_rec);
        recyclerView.setLayoutManager(layoutManager);
        colorCodes = new ArrayList<>();
        temp = new ArrayList<>();
        size = new ToggleButton[6];
        size[0] = view.findViewById(R.id.s_size);
        size[1] = view.findViewById(R.id.l_size);
        size[2] = view.findViewById(R.id.m_size);
        size[3] = view.findViewById(R.id.xl_size);
        size[4] = view.findViewById(R.id.xxl_size);
        size[5] = view.findViewById(R.id.xxxl_size);
        back = view.findViewById(R.id.back);
        choose_color = view.findViewById(R.id.choose_color);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
//        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        fragmentManager = getFragmentManager();
        temp = colorCodes;
        for (int x=0; x<size.length; x++){
            size[x].setOnCheckedChangeListener(this);
        }
        choose_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(false);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.frame_mabi3at,new FragmentMontag()).addToBackStack("FragmentMontag").addToBackStack("FragmentMontag").commit();
            }
        });
    }


    void openDialog(boolean supportsAlpha) {
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(getActivity(), color, supportsAlpha, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                Toast.makeText(getActivity(), "ok", Toast.LENGTH_SHORT).show();
                AddMontag.color = color;
                displayColor(color);
            }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                Toast.makeText(getActivity(), "cancel", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    void displayColor(int color) {
        final int size = colorCodes.size();
//        ColorCode []x = new ColorCode[size];
        if (size > 0) {
//            temp.add(colorCodes);
            for (int i = 0; i < size; i++) {
                temp.add(colorCodes.get(0));
                colorCodes.remove(0);
            }

            colorCodes.add(new ColorCode(color));

            adapter.notifyItemRangeRemoved(0, size);
        } else {
            colorCodes.add(new ColorCode(color));
        }
//        Toast.makeText(getActivity(),String.format("Current color: 0x%08x", color),Toast.LENGTH_SHORT).show();

        adapter = new ColorAdapter(getActivity(), colorCodes);
        recyclerView.setAdapter(adapter);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.s_size:
                if (isChecked)
                    size[0].setBackgroundColor(Color.parseColor(String.valueOf("#007bff")));
                else
                    size[0].setBackgroundColor(Color.parseColor(String.valueOf("#80968b8b")));
                break;
            case R.id.m_size:
                if (isChecked)
                    size[2].setBackgroundColor(Color.parseColor(String.valueOf("#007bff")));
                else
                    size[2].setBackgroundColor(Color.parseColor(String.valueOf("#80968b8b")));
                break;
            case R.id.xxxl_size:
                if (isChecked)
                    size[5].setBackgroundColor(Color.parseColor(String.valueOf("#007bff")));
                else
                    size[5].setBackgroundColor(Color.parseColor(String.valueOf("#80968b8b")));
                break;
            case R.id.xl_size:
                if (isChecked)
                    size[3].setBackgroundColor(Color.parseColor(String.valueOf("#007bff")));
                else
                    size[3].setBackgroundColor(Color.parseColor(String.valueOf("#80968b8b")));
                break;
            case R.id.xxl_size:
                if (isChecked)
                    size[4].setBackgroundColor(Color.parseColor(String.valueOf("#007bff")));
                else
                    size[4].setBackgroundColor(Color.parseColor(String.valueOf("#80968b8b")));
                break;
            case R.id.l_size:
                if (isChecked)
                    size[1].setBackgroundColor(Color.parseColor(String.valueOf("#007bff")));
                else
                    size[1].setBackgroundColor(Color.parseColor(String.valueOf("#80968b8b")));
                break;


        }
    }
}
