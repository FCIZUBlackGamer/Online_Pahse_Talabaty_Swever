package talabaty.swever.com.online.Contact;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import talabaty.swever.com.online.R;
import talabaty.swever.com.online.Fields.MostTrend.Product;
import talabaty.swever.com.online.ProductDetails.FragmentProductDetails;


public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.VHolder> {
    private final Context mContext;
    private final List<Product> products;
    FragmentManager fragmentManager;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Product item);
    }

    public ContactAdapter(Context c, List<Product> row_items, OnItemClickListener listener) {
        this.products = row_items;
        this.mContext = c;
        this.listener = listener;
    }

    @Override
    public ContactAdapter.VHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_home_most_trend,parent,false);
        fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
        return new VHolder(v);
    }

    @Override
    public void onBindViewHolder(ContactAdapter.VHolder holder, final int position) {

        holder.bind(products.get(position), listener);

        holder.name.setText(products.get(position).getName());
        holder.rate.setNumStars(5);
        holder.rate.setRating(products.get(position).getRate());
        holder.price.setText(String.valueOf(products.get(position).getPrice()) + " EG");
        if (products.get(position).getSell() != 0) {
            holder.sell.setText("%" + String.valueOf(products.get(position).getSell()));
            holder.sell_image.setVisibility(View.VISIBLE);
        }else {
            holder.sell_image.setVisibility(View.INVISIBLE);
        }
        if (!products.get(position).getImage_url().isEmpty()) {
            Picasso.with(mContext).load(products.get(position).getImage_url()).into(holder.image);
        }

        holder.action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_home,new FragmentProductDetails().setId(products.get(position).getId())).addToBackStack("FragmentProductDetails").commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class VHolder extends RecyclerView.ViewHolder{
        ImageView image;
        ImageView sell_image;
        TextView name;
        TextView price;
        TextView sell;
        RatingBar rate;
        Button action;

        public VHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.product_image);
            sell_image = (ImageView) itemView.findViewById(R.id.sell_image);
            name = (TextView) itemView.findViewById(R.id.product_name);
            price = (TextView) itemView.findViewById(R.id.product_price);
            sell = (TextView) itemView.findViewById(R.id.sell);
            rate = (RatingBar) itemView.findViewById(R.id.product_rat);
            action = (Button) itemView.findViewById(R.id.action);
        }

        public void bind(final Product item, final OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}