package talabaty.swever.com.online.Home.MostViewed;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import talabaty.swever.com.online.R;

public class FragmentMostViewed extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<Contact> contacts;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_most_viewed,container,false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rec);
        recyclerView.setLayoutManager(layoutManager);
        contacts = new ArrayList<>();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        for (int x=0; x<20; x++){
            Contact contact = new Contact(x,"Apple",4,"Zagazig El-Zraa","zozo_fofa@yahoo.com","123456789","","");
            contacts.add(contact);
        }
        adapter = new MostViewedAdapter(getActivity(),contacts);
        recyclerView.setAdapter(adapter);
    }
}
