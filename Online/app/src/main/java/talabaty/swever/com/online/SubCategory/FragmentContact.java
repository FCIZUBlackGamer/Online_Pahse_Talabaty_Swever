package talabaty.swever.com.online.SubCategory;

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
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInDownAnimator;
import talabaty.swever.com.online.Fields.MostViewed.Contact;
import talabaty.swever.com.online.PrepareFood.FragmentPrepareFood;
import talabaty.swever.com.online.R;

public class FragmentContact extends Fragment {

    RecyclerView recyclerView_contact;
    RecyclerView.Adapter adapter_contact;
    static List<Contact> contact_List;
    FragmentManager fragmentManager;

    public static FragmentContact setList(List<Contact> contact_Lis){
        FragmentContact contact = new FragmentContact();
        contact_List = new ArrayList<>();
        contact_List = contact_Lis;
        return contact;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subcategory_recycleview_contact, container, false);
        /** Contact Rec*/
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView_contact = (RecyclerView) view.findViewById(R.id.rec_contact);
        recyclerView_contact.setItemAnimator(new FadeInDownAnimator(new OvershootInterpolator(1f)));
        recyclerView_contact.setLayoutManager(layoutManager);
        fragmentManager = getFragmentManager();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        /** contact_List Is Already Filled From FragmentSubCategory() */
        adapter_contact = new ContactAdapter(getActivity(), contact_List, null, new ContactAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Contact item) {
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_home,new FragmentPrepareFood().setData(item.getId())).addToBackStack("FragmentPrepareFood").commit();
            }
        });
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter_contact);
        alphaAdapter.setDuration(3000);
        recyclerView_contact.setAdapter(adapter_contact);

    }
}
