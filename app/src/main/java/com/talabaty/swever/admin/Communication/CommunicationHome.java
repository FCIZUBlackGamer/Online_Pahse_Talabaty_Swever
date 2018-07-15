package com.talabaty.swever.admin.Communication;

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
import com.talabaty.swever.admin.R;

public class CommunicationHome extends Fragment {
    Button friendship_request, contact_view; //frame_mabi3at
    FragmentManager fragmentManager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_communication,container,false);
        contact_view = view.findViewById(R.id.contact_view);
        friendship_request = view.findViewById(R.id.friendship_request);
        fragmentManager = getFragmentManager();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        friendship_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.frame_mabi3at,new FragmentViewFriendRequests()).addToBackStack("FragmentViewFriendRequests").commit();
            }
        });

        contact_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.frame_mabi3at,new FragmentViewContacts()).addToBackStack("FragmentViewContacts").commit();
            }
        });
    }
}
