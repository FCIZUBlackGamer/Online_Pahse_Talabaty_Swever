package com.talabaty.swever.admin.Managment.Employees.ControlEmployee;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.talabaty.swever.admin.Home;
import com.talabaty.swever.admin.Mabi3at.Mabi3atNavigator;
import com.talabaty.swever.admin.R;

public class FragmentControlEmployee extends Fragment {
    ImageButton[]edit, delete;
    FragmentManager fragmentManager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_employee_control,container,false);
        edit = new ImageButton[10];
        delete = new ImageButton[10];

        edit[0] = view.findViewById(R.id.edit1);
        edit[1] = view.findViewById(R.id.edit2);
        edit[2] = view.findViewById(R.id.edit3);
        edit[3] = view.findViewById(R.id.edit4);
        edit[4] = view.findViewById(R.id.edit5);
        edit[5] = view.findViewById(R.id.edit6);
        edit[6] = view.findViewById(R.id.edit7);
        edit[7] = view.findViewById(R.id.edit8);
        edit[8] = view.findViewById(R.id.edit9);
        edit[9] = view.findViewById(R.id.edit10);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((Mabi3atNavigator) getActivity())
                .setActionBarTitle("عمليات الموظف");
        fragmentManager = getFragmentManager();
        for (int x=0; x<10; x++){
            edit[x].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), Home.class);
                    intent.putExtra("fragment","edit_emp");
                    startActivity(intent);
                }
            });

        }
    }
}
