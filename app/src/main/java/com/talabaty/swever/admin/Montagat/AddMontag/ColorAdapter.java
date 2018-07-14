package com.talabaty.swever.admin.Montagat.AddMontag;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.talabaty.swever.admin.R;

import java.util.List;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.Vholder> {

    Context context;
    List<ColorCode> colorCodes;

    public ColorAdapter(Context context, List<ColorCode> colorCodes) {
        this.context = context;
        this.colorCodes = colorCodes;
    }

    @NonNull
    @Override
    public Vholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.button_color,parent,false);
        return new Vholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Vholder holder, final int position) {

        holder.color.setBackgroundColor(colorCodes.get(position).getColor());
        holder.delete_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete Color From List And Redisplay the List
                try {
                    if (colorCodes.size()> 0) {
                        colorCodes.remove(position);
                        notifyItemRemoved(position);
                    }else {
                        Toast.makeText(context,"Can't Remove",Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception s){
//                    Toast.makeText(context,"Can't Remove",Toast.LENGTH_SHORT).show();

                    for (int x=0; x<colorCodes.size(); x++){
                        colorCodes.remove(x);
                        notifyItemRemoved(x);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return colorCodes.size();
    }

    public class Vholder extends RecyclerView.ViewHolder{
        Button color, delete_color;
        public Vholder(View itemView) {
            super(itemView);
            color = itemView.findViewById(R.id.color_button);
            delete_color = itemView.findViewById(R.id.delete_color);
        }
    }
}
