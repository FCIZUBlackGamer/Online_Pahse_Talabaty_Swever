package talabaty.swever.com.online.ProductDetails;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import talabaty.swever.com.online.R;

public class ColorProductDetailsAdapter extends RecyclerView.Adapter<ColorProductDetailsAdapter.Vholder> {

    List<String> colorList;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String item);
    }

    public ColorProductDetailsAdapter(List<String> colorList, OnItemClickListener listener) {
        this.colorList = colorList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public Vholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_product_details_color_item, parent, false);
        return new Vholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Vholder holder, final int position) {
        holder.bind(colorList.get(position), listener);
        Log.e("Color",colorList.get(position));
        holder.color.setBackgroundColor(Color.parseColor(colorList.get(position)));

    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    public class Vholder extends RecyclerView.ViewHolder {

        TextView color;

        public Vholder(View itemView) {
            super(itemView);
            color = itemView.findViewById(R.id.color_item);
        }

        public void bind(final String item, final OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }


}
