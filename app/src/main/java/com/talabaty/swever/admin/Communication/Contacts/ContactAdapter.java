package com.talabaty.swever.admin.Communication.Contacts;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.talabaty.swever.admin.R;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.Vholder> {

    OnItemClickListener listener;
    List<ContactItem> contactItems;
    public interface OnItemClickListener {
        void onItemClick(ContactItem item);
    }


    public ContactAdapter(List<ContactItem> contactItems, OnItemClickListener onItemClickListener) {
        this.contactItems = contactItems;
        this.listener = onItemClickListener;
    }

    @NonNull
    @Override
    public Vholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item,parent,false);
        return new Vholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Vholder holder, final int position) {

        holder.bind(contactItems.get(position), listener);
        holder.contact_name.setText(contactItems.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return contactItems.size();
    }

    public class Vholder extends RecyclerView.ViewHolder{
        ImageView contact_image;
        TextView contact_name;
        public Vholder(View itemView) {
            super(itemView);
            contact_image = itemView.findViewById(R.id.contact_image);
            contact_name = itemView.findViewById(R.id.contact_name);
        }
        public void bind(final ContactItem item, final OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
