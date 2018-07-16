package com.talabaty.swever.admin.Managment.Privilages.ControlPrivilege;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import com.talabaty.swever.admin.Mabi3at.Mabi3atNavigator;
import com.talabaty.swever.admin.Managment.Privilages.AddPrivilege.FragmentAddPrivilege;
import com.talabaty.swever.admin.R;

import java.util.List;

public class ControlPrivilegeAdapter extends RecyclerView.Adapter<ControlPrivilegeAdapter.Vholder> {

    List<PrivilegeItem> privilegeItems;
    Intent intent;

    Context context;
    FragmentManager fragmentManager;

    public ControlPrivilegeAdapter(List<PrivilegeItem> privilegeItems, Context context) {
        this.privilegeItems = privilegeItems;
        this.context = context;
        intent = new Intent(context, Mabi3atNavigator.class);
    }

    @NonNull
    @Override
    public Vholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_control_fragment, parent, false);
        fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        return new Vholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Vholder holder, final int position) {

        holder.name.setText(privilegeItems.get(position).getName());

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("fragment","edit_priv");
                context.startActivity(intent);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return privilegeItems.size();
    }

    public class Vholder extends RecyclerView.ViewHolder {

        TextView name;
        ImageButton edit, delete;

        public Vholder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);
        }

    }
}
