package talabaty.swever.com.online.Fields;

import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import talabaty.swever.com.online.R;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.VHolder> {
    private final Context mContext;
    private final List<Category> categoryList;
//    FragmentManager fragmentManager;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Category item);
    }

    public CategoryAdapter(Context c, List<Category> categoryList, OnItemClickListener listener) {
        this.categoryList = categoryList;
        this.mContext = c;
        this.listener = listener;
    }

    @Override
    public CategoryAdapter.VHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext)
                .inflate(R.layout.adapter_subcategory, parent, false);
//        fragmentManager = ((AppCompatActivity) mContext).getSupportFragmentManager();
        return new VHolder(v);
    }

    @Override
    public void onBindViewHolder(CategoryAdapter.VHolder holder, final int position) {

        holder.bind(categoryList.get(position), listener);

        holder.name.setText(categoryList.get(position).getName());

        if (!categoryList.get(position).getImage().isEmpty()) {
            Picasso.get().load(categoryList.get(position).getImage()).into(holder.image);
        }
        /**
         * fragmentManager.beginTransaction()
         .replace(R.id.frame_home, new FragmentSubCategory().setId(categoryList.get(position).getId())).addToBackStack("FragmentSubCategory").commit();
         * */

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class VHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;

        public VHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.cat_img);
            name = (TextView) itemView.findViewById(R.id.cat_name);
        }

        public void bind(final Category item, final OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}