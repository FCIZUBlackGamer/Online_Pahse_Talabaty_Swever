package talabaty.swever.com.online.Chart;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import talabaty.swever.com.online.R;

public class FragmentHomeChart extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<Sanf> sanfList;
    Button buy;

    View confirm;
    TextView total;
    Spinner delivery_options, delivery_places;
    ArrayList<String> delivery_optionsList, indexOfdelivery_optionsList;
    ArrayList<String> delivery_placesList, indexOfdelivery_placesList;
    Button conf;
    ImageButton ignore;

    ChartDatabase chartDatabase;
    Cursor cursor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        View view = inflater.inflate(R.layout.fragment_home_chart, container, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rec);
        buy = view.findViewById(R.id.buy);
        recyclerView.setLayoutManager(layoutManager);
        sanfList = new ArrayList<>();
        chartDatabase = new ChartDatabase(getActivity());
        cursor = chartDatabase.ShowData();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadChart();
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openConfirmation();
            }
        });
    }

    private void openConfirmation() {
        final LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        confirm = inflater.inflate(R.layout.dialog_chart_buy,null);
        total = confirm.findViewById(R.id.total);
        delivery_options = confirm.findViewById(R.id.options);
        delivery_places = confirm.findViewById(R.id.places);
        conf = confirm.findViewById(R.id.send);
        ignore = confirm.findViewById(R.id.close);
        delivery_placesList = delivery_optionsList = indexOfdelivery_placesList = indexOfdelivery_optionsList = new ArrayList<>();

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("عربة التسوق")
                .setCancelable(false)
                .setView(confirm)
                .setNegativeButton("اغلاق", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do Nothing
                        clearMessageView();
                        dialog.dismiss();
                    }
                });
        final AlertDialog dialog2 = builder.create();
        dialog2.show();
        dialog2.getWindow().setLayout(1200, 800);

        closeMessage(dialog2);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo: Buy Products
            }
        });
    }

    private void clearMessageView() {
        if (confirm != null) {
            ViewGroup parent = (ViewGroup) confirm.getParent();
            if (parent != null) {
                parent.removeAllViews();
            }
        }
    }

    private void closeMessage(final Dialog dialog) {
        ignore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearMessageView();
                dialog.dismiss();
            }
        });
    }

    private void loadChart() {

        final int size = sanfList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                sanfList.remove(0);
            }
            adapter.notifyItemRangeRemoved(0, size);
        }

        int x=1;
        while (cursor.moveToNext()){

            Sanf s = new Sanf(x,cursor.getString(1), cursor.getString(2),cursor.getString(5),cursor.getString(3),Float.parseFloat(cursor.getString(4)));
            x++;
            sanfList.add(s);
        }


        adapter = new ChartAdapter(getActivity(), sanfList);
        recyclerView.setAdapter(adapter);
    }

}