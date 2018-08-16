package talabaty.swever.com.online.Fields.MostViewed;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import talabaty.swever.com.online.Contact.FragmentHomeContacts;
import talabaty.swever.com.online.R;

public class MostViewedAdapter extends RecyclerView.Adapter<MostViewedAdapter.Vholder> {

    Context context;
    List<Contact> contacts;
    FragmentManager fragmentManager;

    public MostViewedAdapter(Context context, List<Contact> contacts) {
        this.context = context;
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public Vholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_home_most_viewed, parent, false);
        fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        return new Vholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Vholder holder, final int position) {

        holder.phone.setText(contacts.get(position).getPhone());
        holder.name.setText(contacts.get(position).getName());
        holder.email.setText(contacts.get(position).getEmail());
        holder.address.setText(contacts.get(position).getLocation());
        holder.bar.setRating(contacts.get(position).getRate());
        if (!contacts.get(position).getCompany_logo().isEmpty()) {
            Picasso.with(context).load(contacts.get(position).getCompany_logo()).into(holder.logo);
        }

        holder.move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_home,new FragmentHomeContacts().setData(contacts.get(position).getPhone(),contacts.get(position).getEmail(),
                                contacts.get(position).getLocation(), contacts.get(position).getName(), contacts.get(position).getCompany_logo(),
                                contacts.get(position).getRate())).addToBackStack("FragmentHomeContacts").commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class Vholder extends RecyclerView.ViewHolder {
        TextView phone, email, address, name;
        RatingBar bar;
        ImageView logo;
        Button move;

        public Vholder(View itemView) {
            super(itemView);
            phone = itemView.findViewById(R.id.company_phone);
            email = itemView.findViewById(R.id.company_email);
            name = itemView.findViewById(R.id.company_name);
            address = itemView.findViewById(R.id.company_address);
            bar = itemView.findViewById(R.id.company_rate);
            logo = itemView.findViewById(R.id.company_logo);
            move = itemView.findViewById(R.id.move);
        }

    }

}
