package talabaty.swever.com.online.OffersDetails;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import talabaty.swever.com.online.Cart.Models.OperOrderModel;
import talabaty.swever.com.online.R;

public class OfferDetailsAdapter extends RecyclerView.Adapter<OfferDetailsAdapter.Vholder> {

    Context context;
    List<OperOrderModel> talabats;

    public OfferDetailsAdapter(Context context, List<OperOrderModel> talabats) {
        this.context = context;
        this.talabats = talabats;
    }

    @NonNull
    @Override
    public Vholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.adapter_offer_details, parent, false);
        return new Vholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Vholder holder, final int position) {
        holder.name.setText(talabats.get(position).getName());
        holder.price.setText(talabats.get(position).getPrice() + "");
        holder.amount.setText(talabats.get(position).getAmount() + "");

    }

    @Override
    public int getItemCount() {
        return talabats.size();
    }

    public class Vholder extends RecyclerView.ViewHolder {

        TextView name, price, amount;

        public Vholder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.pro_price);
            amount = itemView.findViewById(R.id.amount);
        }

    }

}
