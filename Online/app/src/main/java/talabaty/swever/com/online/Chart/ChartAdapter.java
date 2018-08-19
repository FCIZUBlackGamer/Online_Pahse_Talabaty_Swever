package talabaty.swever.com.online.Chart;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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

    public ChartAdapter(Context context, List<Sanf> talabats) {
        this.context = context;
        this.talabats = talabats;
        chartDatabase = new ChartDatabase(context);
    }

    @NonNull
    @Override
    public Vholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chart_row_item, parent, false);
        return new Vholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Vholder holder, final int position) {


        holder.id.setText(talabats.get(position).getId()+"");
        holder.name.setText(talabats.get(position).getName());
        if (talabats.get(position).getColor().length()>0) {
            holder.color.setBackgroundColor(Color.parseColor(talabats.get(position).getColor()));
        }
        holder.amount.setText(talabats.get(position).getAmount()+"");
        holder.state.setText(talabats.get(position).getState());
        if (!talabats.get(position).getImage().isEmpty()) {
            Picasso.with(context).load(talabats.get(position).getImage()).into(holder.image);
        }

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                talabats.remove(position);
                chartDatabase.DeleteData(talabats.get(position).getId()+"");
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return talabats.size();
    }

    public class Vholder extends RecyclerView.ViewHolder {
        TextView id, name, color, amount, state;
        ImageView delete;
        ImageView image;

        public Vholder(View itemView) {
            super(itemView);
            color = itemView.findViewById(R.id.color);
            id = itemView.findViewById(R.id.id);
            name = itemView.findViewById(R.id.name);
            amount = itemView.findViewById(R.id.amount);
            state = itemView.findViewById(R.id.state);
            delete = itemView.findViewById(R.id.ignore);
            image = itemView.findViewById(R.id.image);
        }

    }

}
