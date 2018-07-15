package com.talabaty.swever.admin.Communication.FriendRequests;

import android.app.Activity;
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
import android.widget.TextView;

import com.talabaty.swever.admin.Communication.Contacts.ContactItem;
import com.talabaty.swever.admin.Communication.Contacts.FragmentContactItem;
import com.talabaty.swever.admin.R;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.Vholder> {

    List<ContactItem> contactItems;

    Context context;
    FragmentManager fragmentManager;

    public ContactAdapter(List<ContactItem> contactItems, Context context) {
        this.contactItems = contactItems;
        this.context = context;
    }

    @NonNull
    @Override
    public Vholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.freindrequest_contact_item,parent,false);
        fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
        return new Vholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Vholder holder, final int position) {

        holder.contact_name.setText(contactItems.get(position).getName());
        holder.contact_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.frame_mabi3at,new FragmentContactItem()).addToBackStack("FragmentContactItem").commit();
            }
        });

        holder.contact_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.frame_mabi3at,new FragmentContactItem()).addToBackStack("FragmentContactItem").commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return contactItems.size();
    }

    public class Vholder extends RecyclerView.ViewHolder{
        ImageView contact_image;
        TextView contact_name;
        Button accept, reject;
        public Vholder(View itemView) {
            super(itemView);
            contact_image = itemView.findViewById(R.id.contact_image);
            contact_name = itemView.findViewById(R.id.contact_name);
            accept = itemView.findViewById(R.id.accept);
            reject = itemView.findViewById(R.id.reject);
        }

    }
}
