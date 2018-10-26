package talabaty.swever.com.online.Chart;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import talabaty.swever.com.online.R;

public class ChartAdapter extends RecyclerView.Adapter<ChartAdapter.Vholder> {

    Context context;
    List<Sanf> talabats;
    ChartDatabase chartDatabase;
    Cursor cursor;
    ChartAdditionalDatabase chartAdditionalDatabase;
    List<String> colors, sizes;

    public ChartAdapter(Context context, List<Sanf> talabats, List<String> color, List<String> size) {
        this.context = context;
        this.talabats = talabats;
        this.sizes = size;
        this.colors = color;
        chartDatabase = new ChartDatabase(context);
        chartAdditionalDatabase = new ChartAdditionalDatabase(context);

    }

    public ChartAdapter(Context context, List<Sanf> talabats) {
        this.context = context;
        this.talabats = talabats;
        chartDatabase = new ChartDatabase(context);
        chartAdditionalDatabase = new ChartAdditionalDatabase(context);

    }

    @NonNull
    @Override
    public Vholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chart_row_item, parent, false);
        return new Vholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Vholder holder, final int position) {

        holder.name.setText(talabats.get(position).getName());
        Log.e("IsOffer",talabats.get(position).getIsOffer()+"");
        if (talabats.get(position).getIsOffer() == 0) {
            if (talabats.get(position).getColor().length() > 0) {
                Log.e("Color Index "+position , colors.get(position));
                Log.e("Size Index "+position , sizes.get(position));
                holder.color.setBackgroundColor(Color.parseColor(colors.get(position)));
            }

            holder.state.setText(sizes.get(position));
        }else {
            holder.color.setText("----");
            holder.state.setText("----");
        }
        try{
            if (!talabats.get(position).getImage().isEmpty()) {
                Picasso.with(context).load("http://selltlbaty.rivile.com/" + talabats.get(position).getImage()).into(holder.image);
            }
        }catch (Exception e){

        }
        holder.amount.setText(talabats.get(position).getAmount() + "");

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("IDDD",talabats.get(position).getId()+"");
                cursor = chartDatabase.getID(talabats.get(position).getId()+"");
                String id = null;
                while (cursor.moveToNext()) {
                    Log.e("ID", cursor.getString(0));
                    id = cursor.getString(0);
                }
                if (id != null) {
                    if(chartDatabase.DeleteData(id + "")>0) {
                        talabats.remove(position);
                        sizes.remove(position);
                        colors.remove(position);
                        try {
                            if (talabats.get(position).getIsOffer() == 2) {
                                chartAdditionalDatabase.DeleteData(id);
                            }
                        }catch (Exception e){

                        }
                        Log.e("Id", id + "");
                    }
                }
                notifyDataSetChanged();

            }
        });

    }

    @Override
    public int getItemCount() {
        return talabats.size();
    }

    public class Vholder extends RecyclerView.ViewHolder {
        TextView name, color, amount, state;
        ImageView delete;
        ImageView image;

        public Vholder(View itemView) {
            super(itemView);
            color = itemView.findViewById(R.id.color);
            name = itemView.findViewById(R.id.name);
            amount = itemView.findViewById(R.id.amount);
            state = itemView.findViewById(R.id.state);
            delete = itemView.findViewById(R.id.ignore);
            image = itemView.findViewById(R.id.image);
        }

    }

}
