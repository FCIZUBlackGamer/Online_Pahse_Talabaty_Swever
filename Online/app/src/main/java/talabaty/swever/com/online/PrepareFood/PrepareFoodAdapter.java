package talabaty.swever.com.online.PrepareFood;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import talabaty.swever.com.online.R;

public class PrepareFoodAdapter extends BaseAdapter {

    Context context;
    List<PrepareFood> talabats;
    FragmentManager fragmentManager;

    @Override
    public int getCount() {
        return  talabats.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final PrepareFood product = talabats.get(position);
        // view holder pattern
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
            convertView = layoutInflater.inflate(R.layout.adapter_prepare_food, null);

            final ImageView image = (ImageView) convertView.findViewById(R.id.image);
            final TextView name = (TextView) convertView.findViewById(R.id.name);
            final TextView price = (TextView) convertView.findViewById(R.id.price);
            final Button action = (Button) convertView.findViewById(R.id.action);

            final Vholder viewHolder = new Vholder(name, price, image, action);
            convertView.setTag(viewHolder);
        }

        final Vholder viewHolder = (Vholder) convertView.getTag();
        viewHolder.namee.setText(product.getName());
        viewHolder.pricee.setText(String.valueOf(product.getPrice()) + " EG");

        if (!product.getImage().isEmpty()) {
            Picasso.with(context).load(product.getImage()).into(viewHolder.imagee);
        }

        viewHolder.action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction().replace(R.id.frame_home, new FragmentAdditions()).addToBackStack("FragmentAdditions").commit();
            }
        });
        return convertView;
    }

    public PrepareFoodAdapter(Context context, List<PrepareFood> talabats) {
        this.context = context;
        this.talabats = talabats;

    }

    public class Vholder {
        TextView namee, pricee;
        ImageView imagee;
        Button action;

        public Vholder(TextView name, TextView price, ImageView image, Button actione) {
            namee = name;
            pricee= price;
            imagee= image;
            action= actione;
        }

    }

}
