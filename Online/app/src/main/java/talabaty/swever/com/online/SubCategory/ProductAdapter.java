package talabaty.swever.com.online.SubCategory;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import talabaty.swever.com.online.Fields.MostTrend.Product;
import talabaty.swever.com.online.OffersDetails.FragmentOfferDetails;
import talabaty.swever.com.online.ProductDetails.FragmentProductDetails;
import talabaty.swever.com.online.R;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.Vholder> {

    private final Context mContext;
    private final List<Product> product;
    FragmentManager fragmentManager;

    private int lastPosition = -1;
    int type = 0;

    public ProductAdapter(Context context, List<Product> products) {
        this.mContext = context;
        this.product = products;
    }

    public ProductAdapter(int Type, Context context, List<Product> products) {
        this.mContext = context;
        this.product = products;
        type = Type;
    }

    @NonNull
    @Override
    public Vholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_subcategory_product, parent, false);
        fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
        return new Vholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Vholder holder, @SuppressLint("RecyclerView") final int position) {

        setAnimation(holder.itemView, position);

        holder.name.setText(product.get(position).getName());
        holder.ratingBar.setNumStars(5);
        holder.ratingBar.setRating(product.get(position).getRate());
        holder.price.setText(String.valueOf(product.get(position).getPrice()) + " EG");
        if (product.get(position).getSell() != 0) {
            holder.sell.setText("%" + String.valueOf(product.get(position).getSell()));
            holder.sell_image.setVisibility(View.VISIBLE);
        }else {
            holder.sell_image.setVisibility(View.INVISIBLE);
        }
        if (!product.get(position).getImage_url().isEmpty()) {
            Picasso.with(mContext).load(product.get(position).getImage_url()).into(holder.image);
        }

        holder.action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type == 1) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_home, new FragmentOfferDetails().setId(product.get(position).getId(), product.get(position).getIsOffer())).addToBackStack("FragmentOfferDetails").commit();
                } else {
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_home, new FragmentProductDetails().setId(product.get(position).getId(), product.get(position).getIsOffer())).addToBackStack("FragmentOfferDetails").commit();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return product.size();
    }

    public class Vholder extends RecyclerView.ViewHolder {
        TextView name;
        TextView price;
        TextView sell;
        RatingBar ratingBar;
        ImageView image;
        ImageView sell_image;
        Button action;

        public Vholder(View view) {
            super(view);
            name = view.findViewById(R.id.product_name);
            price = view.findViewById(R.id.product_price);
            image = view.findViewById(R.id.product_image);
            sell = view.findViewById(R.id.sell);
            ratingBar = view.findViewById(R.id.product_rat);
            sell_image = view.findViewById(R.id.sell_image);
            action = view.findViewById(R.id.action);
        }

    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

}
