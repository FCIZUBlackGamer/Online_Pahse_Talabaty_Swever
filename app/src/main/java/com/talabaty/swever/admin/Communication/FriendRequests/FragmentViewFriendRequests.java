package com.talabaty.swever.admin.Communication.FriendRequests;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.talabaty.swever.admin.Communication.Contacts.ContactItem;
import com.talabaty.swever.admin.Communication.Contacts.FragmentContactItem;
import com.talabaty.swever.admin.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentViewFriendRequests extends Fragment implements SearchView.OnQueryTextListener {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<ContactItem> contactItems;
    FragmentManager fragmentManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_contact, container, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.contact_rec);
        recyclerView.setLayoutManager(layoutManager);
        contactItems = new ArrayList<>();
        fragmentManager = getFragmentManager();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        fragmentManager = getFragmentManager();

        for (int x=0; x<10; x++){
            ContactItem item = new ContactItem("1","مؤمن شاهين","fff","ffff","01020118856","ffff");
            contactItems.add(item);
        }

        adapter = new ContactAdapter(contactItems,getActivity());
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}