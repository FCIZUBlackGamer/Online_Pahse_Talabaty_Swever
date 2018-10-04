package talabaty.swever.com.online.PrepareFood;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import talabaty.swever.com.online.R;

public class AdditionsAdapter extends RecyclerView.Adapter<AdditionsAdapter.Vholder> {

    Context context;
    List<PrepareFood> talabats;
    FragmentManager fragmentManager;
    private final AdditionsAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(PrepareFood item);
    }

    public AdditionsAdapter(Context context, List<PrepareFood> talabats, AdditionsAdapter.OnItemClickListener listener) {
        this.context = context;
        this.talabats = talabats;
        this.listener = listener;

    }

    @NonNull
    @Override
    public Vholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_additions, parent, false);
        fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        return new Vholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Vholder holder, final int position) {

        holder.bind(talabats.get(position), listener);

        holder.name.setText(talabats.get(position).getName());
        holder.price.setText(talabats.get(position).getPrice()+" EG");

    }

    @Override
    public int getItemCount() {
        return talabats.size();
    }

    public class Vholder extends RecyclerView.ViewHolder {
        TextView name, price;
        CheckBox check;

        public Vholder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            check = itemView.findViewById(R.id.check);
        }
        public void bind(final PrepareFood item, final AdditionsAdapter.OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }

    }

}
