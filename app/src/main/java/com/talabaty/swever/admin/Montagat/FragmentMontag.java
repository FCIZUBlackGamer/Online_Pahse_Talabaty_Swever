package com.talabaty.swever.admin.Montagat;

import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import com.talabaty.swever.admin.Montagat.AddMontag.AddMontag;
import com.talabaty.swever.admin.Montagat.ControlMontag.ControlMontag;
import com.talabaty.swever.admin.R;

public class FragmentMontag extends Fragment {

    Button add_montage, control_montage;
    FragmentManager fragmentManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_montagat,container,false);
        add_montage = view.findViewById(R.id.add_montage);
        control_montage = view.findViewById(R.id.control_montag);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        ((Home) getActivity())
                .setActionBarTitle("المنتجات");
        fragmentManager = getFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.home_montag_frame,new FragmentHomeMontag()).commit();
        add_montage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                fragmentManager.beginTransaction().replace(R.id.frame_mabi3at,new AddMontag()).addToBackStack("FragmentViewContacts").commit();
//                Toast.makeText(getActivity(),"Done",Toast.LENGTH_SHORT).show();
            }
        });

        control_montage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                Intent intent = new Intent(getActivity(), Mabi3atNavigator.class);
                intent.putExtra("fragment","control");
                startActivity(intent);
//                Toast.makeText(getActivity(),"Done",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
