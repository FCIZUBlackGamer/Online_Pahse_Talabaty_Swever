package talabaty.swever.com.online.FinalBell;

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

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import talabaty.swever.com.online.Chart.ChartDatabase;
import talabaty.swever.com.online.Chart.Models.Bell;
import talabaty.swever.com.online.Chart.Sanf;
import talabaty.swever.com.online.R;

public class FinalBellAdapter extends RecyclerView.Adapter<FinalBellAdapter.Vholder> {

    Context context;
    List<Bell> bellList;

    public FinalBellAdapter(Context context, List<Bell> bellList) {
        this.context = context;
        this.bellList = bellList;
    }

    @NonNull
    @Override
    public Vholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_final_bell, parent, false);
        return new Vholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Vholder holder, final int position) {

        holder.contact_name.setText(bellList.get(position).getContact_name());
        holder.item_name.setText(bellList.get(position).getName());
        holder.num_item.setText(bellList.get(position).getAmount()+"");
        holder.unit_price.setText(bellList.get(position).getPrice()+"");

        Log.e("getSaleType",bellList.get(position).getSaleType().get(0)+"");
        Log.e("getAmountValues",bellList.get(position).getAmountValues().get(0).toString()+"");
        Log.e("getPriceValues",bellList.get(position).getPriceValues().get(0).toString()+"");
//        Log.e("getSale",bellList.get(position).getSale().get(0)+"");
        if (bellList.get(position).getSaleType().get(0) == "1"){
            holder.each_total.setText(
                            Double.parseDouble(bellList.get(position).getAmountValues().get(0).toString()) *
                            Double.parseDouble(bellList.get(position).getPriceValues().get(1).toString()) +
                            Double.parseDouble(bellList.get(position).getSale().get(0).toString()) + "");
        }else if (bellList.get(position).getSaleType().get(0) == "0"){
            holder.each_total.setText(
                    Double.parseDouble(bellList.get(position).getAmountValues().get(0).toString()) *
                    Double.parseDouble(bellList.get(position).getPriceValues().get(1).toString()) +
                    (Float.parseFloat(bellList.get(position).getSale().get(0).toString()) * Double.parseDouble(bellList.get(position).getPriceValues().get(1).toString()) / 100 ) + "");
        }else if (bellList.get(position).getSaleType().get(0) == null){
            holder.each_total.setText(
                    Double.parseDouble(bellList.get(position).getAmountValues().get(0).toString()) *
                    Double.parseDouble(bellList.get(position).getPriceValues().get(1).toString()) + "");
        }else {

        }

        holder.all_total.setText(bellList.get(position).getTotalPrice()+"");

    }

    @Override
    public int getItemCount() {
        return bellList.size();
    }

    public class Vholder extends RecyclerView.ViewHolder {
        TextView contact_name, item_name, num_item, unit_price, each_total, all_total;

        public Vholder(View itemView) {
            super(itemView);
            unit_price = itemView.findViewById(R.id.unit_price);
            each_total = itemView.findViewById(R.id.each_total);
            all_total = itemView.findViewById(R.id.all_total);
            contact_name = itemView.findViewById(R.id.contact_name);
            item_name = itemView.findViewById(R.id.item_name);
            num_item = itemView.findViewById(R.id.num_item);
        }

    }

}
