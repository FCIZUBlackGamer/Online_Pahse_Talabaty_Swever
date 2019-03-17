package talabaty.swever.com.online.ProductDetails;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import talabaty.swever.com.online.R;

public class SizeProductDetailsAdapter extends RecyclerView.Adapter<SizeProductDetailsAdapter.Vholder> {

    List<String> sizeList;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String item);
    }

    public SizeProductDetailsAdapter(List<String> sizeList, OnItemClickListener listener) {
        this.listener = listener;
        this.sizeList = sizeList;
    }

    @NonNull
    @Override
    public Vholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.button_size,parent,false);
        return new Vholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Vholder holder, final int position) {

        holder.bind(sizeList.get(position), listener);
        holder.size_button.setText(sizeList.get(position));
//        holder.size_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                holder.size_button.setBackgroundResource(R.drawable.background_edittext);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return sizeList.size();
    }

    public class Vholder extends RecyclerView.ViewHolder{
        TextView size_button;
        public Vholder(View itemView) {
            super(itemView);
            size_button = itemView.findViewById(R.id.size_button);
        }
        public void bind(final String item, final OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
