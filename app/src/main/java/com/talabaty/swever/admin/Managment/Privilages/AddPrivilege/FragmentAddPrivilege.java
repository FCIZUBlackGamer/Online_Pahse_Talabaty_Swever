package com.talabaty.swever.admin.Managment.Privilages.AddPrivilege;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.talabaty.swever.admin.Home;
import com.talabaty.swever.admin.Mabi3at.Mabi3atNavigator;
import com.talabaty.swever.admin.R;

public class FragmentAddPrivilege extends Fragment {

    FragmentManager fragmentManager;
    Button save, ignor;
    Intent intent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_privelage, container, false);
        save = view.findViewById(R.id.save);
        ignor = view.findViewById(R.id.ignore);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        fragmentManager = getFragmentManager();
        ((Mabi3atNavigator) getActivity())
                .setActionBarTitle("إضافه صلاحيه");
        intent = new Intent(getActivity(), Home.class);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("fragment","save");
                startActivity(intent);
            }
        });

        ignor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("fragment","save");
                startActivity(intent);
            }
        });
    }

}
