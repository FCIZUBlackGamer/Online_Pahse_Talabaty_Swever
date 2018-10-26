package talabaty.swever.com.online.ProductDetails;

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

import talabaty.swever.com.online.Chart.Sanf;
import talabaty.swever.com.online.R;

public class ProductDetailsAdapter extends RecyclerView.Adapter<ProductDetailsAdapter.Vholder> {

    Context context;
    List<Sanf> talabats;

    public ProductDetailsAdapter(Context context, List<Sanf> talabats) {
        this.context = context;
        this.talabats = talabats;
    }

    @NonNull
    @Override
    public Vholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_montag_detail, parent, false);
        return new Vholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Vholder holder, final int position) {

        if (!talabats.get(position).getImage().isEmpty()) {
            Picasso.with(context).load("http://onlineapi.rivile.com/" + talabats.get(position).getImage()).into(holder.image);
        }


    }

    @Override
    public int getItemCount() {
        return talabats.size();
    }

    public class Vholder extends RecyclerView.ViewHolder {

        ImageView image;

        public Vholder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
        }

    }

}
