package com.talabaty.swever.admin.Managment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.talabaty.swever.admin.Communication.Contacts.FragmentViewContacts;
import com.talabaty.swever.admin.Communication.FriendRequests.FragmentViewFriendRequests;
import com.talabaty.swever.admin.Home;
import com.talabaty.swever.admin.Managment.Employees.EmployeesHome;
import com.talabaty.swever.admin.Managment.Privilages.PrivilagesHome;
import com.talabaty.swever.admin.R;

public class ManagmentHome extends Fragment {
    Button employee, privilege; //frame_mabi3at
    FragmentManager fragmentManager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_managment,container,false);
        privilege = view.findViewById(R.id.privilege);
        employee = view.findViewById(R.id.employee);
        fragmentManager = getFragmentManager();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((Home) getActivity())
                .setActionBarTitle("الإداره");
        employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.frame_mabi3at,new EmployeesHome()).addToBackStack("EmployeesHome").commit();
            }
        });

        privilege.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.frame_mabi3at,new PrivilagesHome()).addToBackStack("PrivilagesHome").commit();
            }
        });
    }
}
