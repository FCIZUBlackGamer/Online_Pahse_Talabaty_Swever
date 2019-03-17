package talabaty.swever.com.online.ProductDetails;

import android.graphics.Color;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import talabaty.swever.com.online.R;

public class ColorProductDetailsAdapter extends RecyclerView.Adapter<ColorProductDetailsAdapter.Vholder> {

    List<String> colorList;
    List<Boolean> state;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String item);
    }

    public ColorProductDetailsAdapter(List<String> colorList, OnItemClickListener listener) {
        this.colorList = colorList;
        state = new ArrayList<>();
        for (int x = 0; x < colorList.size(); x++) {
            state.add(false);
        }
        this.listener = listener;
    }

    @NonNull
    @Override
    public Vholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_product_details_color_item, parent, false);
        return new Vholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Vholder holder, final int position) {
        holder.bind(colorList.get(position), listener);
        Log.e("Color", colorList.get(position));
        try {
            holder.color.setBackgroundColor(Color.parseColor(colorList.get(position)));
        }catch (Exception e){

        }

//        holder.color.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                for (int x = 0; x < state.size(); x++) {
//                    state.set(x, false);
//                }
//                state.set(position, true);
//
//                for (int x = 0; x < state.size(); x++) {
////                    Log.e("S" + x, state.get(x) + "");
//                    if (state.get(x)) {
//
//                        holder.ok.setVisibility(View.VISIBLE);
//                        if (isColorDark(Color.parseColor(colorList.get(position)))) {
//                            holder.ok.setBackgroundResource(R.drawable.okw);
//                            Log.e("State" + x, "11");
//                            notifyItemChanged(position);
//                        } else {
//                            holder.ok.setBackgroundResource(R.drawable.ok);
//                            Log.e("State" + x, "10");
//                            notifyItemChanged(position);
//                        }
//                    } else {
//                        Log.e("State" + x, "0");
//                        holder.ok.setVisibility(View.INVISIBLE);
//                        notifyItemChanged(position);
//                    }
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return colorList.size();
    }

    public class Vholder extends RecyclerView.ViewHolder {

        TextView color;
        ImageView ok;

        public Vholder(View itemView) {
            super(itemView);
            color = itemView.findViewById(R.id.color_item);
            ok = itemView.findViewById(R.id.ok);
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

    public boolean isColorDark(int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        if (darkness < 0.5) {
            return false; // It's a light color
        } else {
            return true; // It's a dark color
        }
    }

}
