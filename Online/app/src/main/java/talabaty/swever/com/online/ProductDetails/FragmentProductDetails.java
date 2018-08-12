package talabaty.swever.com.online.ProductDetails;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import talabaty.swever.com.online.Chart.Sanf;
import talabaty.swever.com.online.R;

public class FragmentProductDetails extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<Sanf> sanfList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_montag_detail, container,false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rec);
        recyclerView.setLayoutManager(layoutManager);
        sanfList = new ArrayList<>();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadChart();
    }
    private void loadChart() {

        final int size = sanfList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                sanfList.remove(0);
            }
            adapter.notifyItemRangeRemoved(0, size);
        }

        for (int x=0; x<10; x++) {
            Sanf s = new Sanf();
            s.setId(x+1);
            s.setImage("");
            sanfList.add(s);
        }


        adapter = new ProductDetailsAdapter(getActivity(), sanfList);
        recyclerView.setAdapter(adapter);
    }
}
