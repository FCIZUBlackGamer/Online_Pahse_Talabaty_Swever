package talabaty.swever.com.online.Contact;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import talabaty.swever.com.online.Home.MostTrend.MontagAdapter;
import talabaty.swever.com.online.Home.MostTrend.Product;
import talabaty.swever.com.online.R;

public class FragmentHomeContacts extends Fragment {

    GridView gridView;
    MontagAdapter booksAdapter;
    private Product[] products = new Product[20];
    static String phon, emai, addres, nam, log;
    static float ba;

    TextView phone, email, address, name;
    RatingBar bar;
    ImageView logo;

    public static FragmentHomeContacts setData(String phone, String email, String address, String name, String logo, float bar){
        FragmentHomeContacts fragmentHomeContacts = new FragmentHomeContacts();
        phon =phone;
        emai =email;
        addres =address;
        nam =name;
        log =logo;
        ba =bar;
        return fragmentHomeContacts;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts_home, container,false);
        gridView = (GridView)view.findViewById(R.id.gridview);
        phone = view.findViewById(R.id.company_phone);
        email = view.findViewById(R.id.company_email);
        name = view.findViewById(R.id.company_name);
        address = view.findViewById(R.id.company_address);
        bar = view.findViewById(R.id.company_rate);
        logo = view.findViewById(R.id.company_logo);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        /** Basic Info*/
        phone.setText(phon);
        name.setText(nam);
        email.setText(emai);
        address.setText(addres);
        bar.setRating(ba);
        if (!log.isEmpty()) {
            Picasso.with(getActivity()).load(log).into(logo);
        }

        /** Adapter Montag*/
        for (int x=0; x<20; x++){
            Product r = new Product(0, "IPhone", "",12000, 20,3);
            products[x] = r;
        }
        booksAdapter = new MontagAdapter(getActivity(), products);
        gridView.setAdapter(booksAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product book = products[position];
                //Todo: Make Some Action
                book.getId();
            }
        });

    }
}
