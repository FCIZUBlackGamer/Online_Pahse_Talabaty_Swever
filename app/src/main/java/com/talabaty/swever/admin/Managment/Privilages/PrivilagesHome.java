package com.talabaty.swever.admin.Managment.Privilages;

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

public class PrivilagesHome extends Fragment {
    Button control, add;
    Intent intent;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_privilege,container,false);
        control = view.findViewById(R.id.control);
        add = view.findViewById(R.id.add);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((Home) getActivity())
                .setActionBarTitle("إداره الصلاحيات");
        intent = new Intent(getActivity(), Mabi3atNavigator.class);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("fragment","priv_add");
                startActivity(intent);
            }
        });

        control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("fragment","priv_control");
                startActivity(intent);
            }
        });

    }
}
