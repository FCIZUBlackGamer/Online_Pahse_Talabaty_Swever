package talabaty.swever.com.online.Home.MostTrend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import talabaty.swever.com.online.R;

public class MontagAdapter extends BaseAdapter {

  private final Context mContext;
  private final Product[] products;

  public MontagAdapter(Context context, Product[] products) {
    this.mContext = context;
    this.products = products;
  }

  @Override
  public int getCount() {
    return products.length;
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
  public View getView(int position, View convertView, ViewGroup parent) {
    final Product product = products[position];

    // view holder pattern
    if (convertView == null) {
      final LayoutInflater layoutInflater = LayoutInflater.from(mContext);
      convertView = layoutInflater.inflate(R.layout.adapter_home_most_trend, null);

      final ImageView image = (ImageView)convertView.findViewById(R.id.product_image);
      final ImageView sell_image = (ImageView)convertView.findViewById(R.id.sell_image);
      final TextView name = (TextView)convertView.findViewById(R.id.product_name);
      final TextView price = (TextView)convertView.findViewById(R.id.product_price);
      final TextView sell = (TextView)convertView.findViewById(R.id.sell);
      final RatingBar rate = (RatingBar)convertView.findViewById(R.id.product_rat);

      final ViewHolder viewHolder = new ViewHolder(name, price, image, sell, rate, sell_image);
      convertView.setTag(viewHolder);
    }

    final ViewHolder viewHolder = (ViewHolder)convertView.getTag();
//    viewHolder.imageViewCoverArt.setImageResource(book.getImageResource());
    viewHolder.name.setText(product.getName());
    viewHolder.ratingBar.setNumStars(5);
    viewHolder.ratingBar.setRating(product.getRate());
    viewHolder.price.setText(String.valueOf(product.getPrice())+" EG");
//    if (!String.valueOf(product.getSell()).isEmpty()) {
      viewHolder.sell.setText("%"+String.valueOf(product.getSell()));
      viewHolder.sell_image.setVisibility(View.VISIBLE);
//    }
    if (!product.getImage_url().isEmpty()) {
      Picasso.with(mContext).load(product.getImage_url()).into(viewHolder.image);
    }
    return convertView;
  }

  private class ViewHolder {
    TextView name;
    TextView price;
    TextView sell;
    RatingBar ratingBar;
    ImageView image;
    ImageView sell_image;

    public ViewHolder(TextView name, TextView price, ImageView image, TextView sell, RatingBar bar, ImageView sell_image) {
      this.name = name;
      this.price = price;
      this.image = image;
      this.sell = sell;
      this.ratingBar = bar;
      this.sell_image = sell_image;
    }
  }

}
