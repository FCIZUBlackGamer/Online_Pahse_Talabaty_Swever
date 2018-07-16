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
import com.talabaty.swever.admin.Managment.Employees.EmployeesHome;
import com.talabaty.swever.admin.Managment.ManagmentHome;
import com.talabaty.swever.admin.R;

public class AddtionalInfo extends Fragment {
    Button first, second;
    Button save, ignore;
    FragmentManager fragmentManager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addemployee3,container,false);
//        first = view.findViewById(R.id.first);
        second = view.findViewById(R.id.third);
        save = view.findViewById(R.id.save);
        ignore = view.findViewById(R.id.ignore);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((Home) getActivity())
                .setActionBarTitle("إضافه موظف");
        fragmentManager = getFragmentManager();
//        first.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fragmentManager.beginTransaction().replace(R.id.frame_mabi3at, new PersonalInfo()).addToBackStack("PersonalInfo").commit();
//            }
//        });

        second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.frame_mabi3at, new MoneyStaff()).addToBackStack("MoneyStaff").commit();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.frame_mabi3at, new EmployeesHome()).addToBackStack("EmployeesHome").commit();
            }
        });
    }
}
