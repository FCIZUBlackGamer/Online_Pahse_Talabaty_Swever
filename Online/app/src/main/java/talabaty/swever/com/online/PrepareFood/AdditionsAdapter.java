package talabaty.swever.com.online.PrepareFood;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import talabaty.swever.com.online.R;
import talabaty.swever.com.online.Cart.*;
public class AdditionsAdapter extends RecyclerView.Adapter<AdditionsAdapter.Vholder> {

    Context context;
    List<AdditionalModel> talabats;
    FragmentManager fragmentManager;
    private final AdditionsAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(AdditionalModel item);
    }

    public AdditionsAdapter(Context context, List<AdditionalModel> talabats, AdditionsAdapter.OnItemClickListener listener) {
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
        holder.price.setText(talabats.get(position).getPrice()+" LE");

        holder.check.setChecked(talabats.get(position).getChecked());
//        holder.check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                talabats.get(position).setChecked(isChecked);
//
//            }
//        });

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

        public void bind(final AdditionalModel item, final AdditionsAdapter.OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }

    }

}
