package talabaty.swever.com.online.SubCategory;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;

import java.util.List;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import talabaty.swever.com.online.Fields.MostTrend.Product;
import talabaty.swever.com.online.R;

public class FragmentProduct extends Fragment {

    RecyclerView recyclerView_product;
    RecyclerView.Adapter adapter_product;
    static List<Product> product_List;


    public static FragmentProduct setList(List<Product> contact_Li){
        FragmentProduct contact = new FragmentProduct();
        product_List = contact_Li;
        return contact;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subcategory_recycleview_product, container, false);
        /** Product Rec*/
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView_product = (RecyclerView) view.findViewById(R.id.rec_product);
        recyclerView_product.setLayoutManager(layoutManager2);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Gson gson = new Gson();
        Log.e("Product",gson.toJson(product_List));
        /** product_List Is Already Filled From #ContactAdapter# */
        adapter_product = new ProductAdapter(getActivity(),product_List);
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter_product);
        alphaAdapter.setDuration(3000);
        recyclerView_product.setAdapter(adapter_product);
    }
}
