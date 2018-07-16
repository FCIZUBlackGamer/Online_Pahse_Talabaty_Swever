package com.talabaty.swever.admin.Managment.Privilages.ControlPrivilege;

import android.content.Intent;
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

import com.talabaty.swever.admin.Home;
import com.talabaty.swever.admin.Mabi3at.Mabi3atNavigator;
import com.talabaty.swever.admin.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentControlPrivilege extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<PrivilegeItem> privilegeItems;
    Button save, ignore;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_control_privilege, container, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.priv_rec);
        recyclerView.setLayoutManager(layoutManager);
        privilegeItems = new ArrayList<>();
        save = view.findViewById(R.id.save);
        ignore = view.findViewById(R.id.ignore);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((Mabi3atNavigator) getActivity())
                .setActionBarTitle("عمليات الصلاحيه");

        for (int x=0; x<10; x++){
            PrivilegeItem item = new PrivilegeItem(""+x,"admin"+x,true,true,false,true);
            privilegeItems.add(item);
        }

        adapter = new ControlPrivilegeAdapter(privilegeItems,getActivity());
        recyclerView.setAdapter(adapter);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        ignore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

}
