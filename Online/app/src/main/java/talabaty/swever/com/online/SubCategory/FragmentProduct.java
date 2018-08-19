package talabaty.swever.com.online.SubCategory;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import talabaty.swever.com.online.Fields.MostTrend.Product;
import talabaty.swever.com.online.R;

public class FragmentProduct extends Fragment {

    RecyclerView recyclerView_product;
    RecyclerView.Adapter adapter_product;
    static List<Product> product_List;


    public static FragmentProduct setList(List<Product> contact_Li){
        FragmentProduct contact = new FragmentProduct();
        product_List = new ArrayList<>();
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

        /** product_List Is Already Filled From #ContactAdapter# */
        adapter_product = new ProductAdapter(getActivity(),product_List);
        recyclerView_product.setAdapter(adapter_product);
    }
}
