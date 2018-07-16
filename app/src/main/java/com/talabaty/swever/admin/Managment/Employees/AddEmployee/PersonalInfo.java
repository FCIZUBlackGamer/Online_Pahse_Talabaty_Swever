package com.talabaty.swever.admin.Managment.Employees.AddEmployee;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.talabaty.swever.admin.Home;
import com.talabaty.swever.admin.R;

public class PersonalInfo extends Fragment {
    Button second;
    FragmentManager fragmentManager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addemployee1,container,false);
        second = view.findViewById(R.id.first);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((Home) getActivity())
                .setActionBarTitle("إضافه موظف");
        fragmentManager = getFragmentManager();
        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.frame_mabi3at, new MoneyStaff()).addToBackStack("MoneyStaff").commit();
            }
        });
    }
}
