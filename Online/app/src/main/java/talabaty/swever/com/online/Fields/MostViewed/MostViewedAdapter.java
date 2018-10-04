package talabaty.swever.com.online.Fields.MostViewed;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
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

import talabaty.swever.com.online.Contact.FragmentHomeContacts;
import talabaty.swever.com.online.R;
import talabaty.swever.com.online.PrepareFood.*;

public class MostViewedAdapter extends BaseAdapter {

    Context context;
    List<Contact> contacts;
    FragmentManager fragmentManager;
    int shopId = -1;

    public MostViewedAdapter(Context context, List<Contact> contacts) {
        this.context = context;
        this.contacts = contacts;
    }

    public MostViewedAdapter(Context context, List<Contact> contacts, int shopId) {
        this.context = context;
        this.contacts = contacts;
        this.shopId = shopId;
    }

    @Override
    public int getCount() {
        return contacts.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Contact product = contacts.get(position);

        // view holder pattern
        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
            convertView = layoutInflater.inflate(R.layout.adapter_home_most_viewed, null);

            final ImageView image = (ImageView) convertView.findViewById(R.id.company_logo);
            final TextView name = (TextView) convertView.findViewById(R.id.company_name);
            final RatingBar rate = (RatingBar) convertView.findViewById(R.id.company_rate);
            final Button action = (Button) convertView.findViewById(R.id.move);

            final Vholder viewHolder = new Vholder(name, rate, image, action);
            convertView.setTag(viewHolder);
        }

        final Vholder viewHolder = (Vholder) convertView.getTag();
//    viewHolder.imageViewCoverArt.setImageResource(book.getImageResource());
        viewHolder.name.setText(product.getName());
        viewHolder.bar.setNumStars(5);
        viewHolder.bar.setRating(product.getRate());

        if (!product.getCompany_logo().isEmpty()) {
            Picasso.with(context).load(product.getCompany_logo()).into(viewHolder.logo);
        }

        viewHolder.move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (shopId == -1) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_home, new FragmentHomeContacts().setData(contacts.get(position).getPhone(), contacts.get(position).getEmail(),
                                    contacts.get(position).getLocation(), contacts.get(position).getName(), contacts.get(position).getCompany_logo(),
                                    contacts.get(position).getRate())).addToBackStack("FragmentHomeContacts").commit();
//                } else {
//                    fragmentManager.beginTransaction()
//                            .replace(R.id.frame_home,new FragmentPrepareFood().setData(contacts.get(position).getId())).addToBackStack("FragmentPrepareFood").commit();
//                }
            }
        });
        return convertView;
    }

    public class Vholder {
        TextView name;
        RatingBar bar;
        ImageView logo;
        Button move;

        public Vholder(TextView namee, RatingBar bare, ImageView sell_image, Button action) {
            name = namee;
            bar = bare;
            logo = sell_image;
            move = action;

        }

    }

}
