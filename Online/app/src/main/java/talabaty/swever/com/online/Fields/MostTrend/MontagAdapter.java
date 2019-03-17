package talabaty.swever.com.online.Fields.MostTrend;

import android.content.Context;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import talabaty.swever.com.online.ProductDetails.FragmentProductDetails;
import talabaty.swever.com.online.R;

public class MontagAdapter extends BaseAdapter {

    private final Context mContext;
    private final List<Product> products;
    FragmentManager fragmentManager;

    public MontagAdapter(Context context, List<Product> products) {
        this.mContext = context;
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Product product = products.get(position);

        // view holder pattern
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
            convertView = layoutInflater.inflate(R.layout.adapter_home_most_trend, null);

            final ImageView image = (ImageView) convertView.findViewById(R.id.product_image);
            final ImageView sell_image = (ImageView) convertView.findViewById(R.id.sell_image);
            final TextView name = (TextView) convertView.findViewById(R.id.product_name);
            final TextView price = (TextView) convertView.findViewById(R.id.product_price);
            final TextView sell = (TextView) convertView.findViewById(R.id.sell);
            final RatingBar rate = (RatingBar) convertView.findViewById(R.id.product_rat);
            final Button action = (Button) convertView.findViewById(R.id.action);

            final ViewHolder viewHolder = new ViewHolder(name, price, image, sell, rate, sell_image, action);
            convertView.setTag(viewHolder);
        }

        final ViewHolder viewHolder = (ViewHolder) convertView.getTag();
//    viewHolder.imageViewCoverArt.setImageResource(book.getImageResource());
        viewHolder.name.setText(product.getName());
        viewHolder.ratingBar.setNumStars(5);
        viewHolder.ratingBar.setRating(product.getRate());
        viewHolder.price.setText(String.valueOf(product.getPrice()) + " LE");
        if (product.getSell() != 0) {
            viewHolder.sell.setText("%" + String.valueOf(product.getSell()));
            viewHolder.sell_image.setVisibility(View.VISIBLE);
        }else {
            viewHolder.sell_image.setVisibility(View.INVISIBLE);
        }
        if (!product.getImage_url().isEmpty()) {
            Picasso.get().load(product.getImage_url()).into(viewHolder.image);
        }

        viewHolder.action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_home,new FragmentProductDetails().setId(products.get(position).getId(),products.get(position).getIsOffer())).addToBackStack("FragmentOfferDetails").commit();
            }
        });
        return convertView;
    }

    private class ViewHolder {
        TextView name;
        TextView price;
        TextView sell;
        RatingBar ratingBar;
        ImageView image;
        ImageView sell_image;
        Button action;

        public ViewHolder(TextView name, TextView price, ImageView image, TextView sell, RatingBar bar, ImageView sell_image, Button action) {
            this.name = name;
            this.price = price;
            this.image = image;
            this.sell = sell;
            this.ratingBar = bar;
            this.sell_image = sell_image;
            this.action = action;
        }
    }

}
