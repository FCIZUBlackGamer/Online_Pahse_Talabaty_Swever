package talabaty.swever.com.online.Chart;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.google.gson.Gson;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import talabaty.swever.com.online.R;
import talabaty.swever.com.online.Chart.Models.*;

public class FragmentHomeChart extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<Sanf> sanfList;
    Button buy;

    FragmentManager fragmentManager;

//    View confirm;
//    TextView total;
//    Spinner capital, city, region;
//    ArrayList<String> delivery_optionsList, indexOfdelivery_optionsList;
//    ArrayList<String> delivery_placesList, indexOfdelivery_placesList;
//    Button conf;
//    EditText place;
//    ImageButton ignore;

    ChartDatabase chartDatabase;
    Cursor cursor;
    List<ChartModel> modelList;
//    List<State> states;
//    List<String> stateNames;
//    List<Cities> cities;
//    List<String> cityNames;
//    List<Regions> regions;
//    List<Integer> regionsId;
//    List<String> regionNames;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        View view = inflater.inflate(R.layout.fragment_home_chart, container, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rec);
        buy = view.findViewById(R.id.buy);
//        place = view.findViewById(R.id.place);
        recyclerView.setLayoutManager(layoutManager);
        sanfList = new ArrayList<>();
        modelList = new ArrayList<>();
        chartDatabase = new ChartDatabase(getActivity());
        cursor = chartDatabase.ShowData();
        fragmentManager = getFragmentManager();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadChart();
//        capital.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Log.e("Capital",stateNames.get(position));
//                fillCitys(stateNames.get(position));
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Log.e("City",cityNames.get(position));
//                fillRegions(cityNames.get(position));
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               fragmentManager.beginTransaction()
                       .replace(R.id.frame_home,new FinishChart()).addToBackStack("FinishChart").commit();
            }
        });
    }

//    private void openConfirmation() {
//        final LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        confirm = inflater.inflate(R.layout.dialog_chart_buy,null);
//        total = confirm.findViewById(R.id.total);
//        conf = confirm.findViewById(R.id.send);
//        capital = confirm.findViewById(R.id.capital);
//        city = confirm.findViewById(R.id.city);
//        region = confirm.findViewById(R.id.region);
//        ignore = confirm.findViewById(R.id.close);
//        delivery_placesList = delivery_optionsList = indexOfdelivery_placesList = indexOfdelivery_optionsList = new ArrayList<>();
//        cursor = chartDatabase.ShowData();
//        int tota = 0;
//        while (cursor.moveToNext()) {
//            tota += Integer.parseInt(cursor.getString(6));
//        }
//        total.setText(tota+"");
////        loadDelvry();
//        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle("عربة التسوق")
//                .setCancelable(false)
//                .setView(confirm)
//                .setNegativeButton("اغلاق", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // Do Nothing
//                        clearMessageView();
//                        dialog.dismiss();
//                    }
//                });
//        final AlertDialog dialog2 = builder.create();
//        dialog2.show();
//        dialog2.getWindow().setLayout(1200, 800);
//
//        closeMessage(dialog2);
//        confirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Todo: Buy Products
//                cursor = chartDatabase.ShowData();
//                while (cursor.moveToNext()) {
//                    ChartModel model = new ChartModel();
//                    model.setId(Integer.parseInt(cursor.getString(0)));
//                    model.setID(Integer.parseInt(cursor.getString(1)));
//                    model.setAmount(Integer.parseInt(cursor.getString(4)));
//                    model.setColor(Color.parseColor(cursor.getString(3)));
//                    model.setImage(cursor.getString(2));
//                    model.setName(cursor.getString(1));
//                    model.setPrice(Double.parseDouble(cursor.getString(6)));
//                    modelList.add(model);
//                }
//                Gson gson = new Gson();
//                String mod = gson.toJson(modelList);
//                uploadChart(mod,place.getText().toString(),regionsId.get(regionNames.indexOf(region.getSelectedItem().toString())));
//            }
//        });
//    }

//    private void uploadChart(final String mod, final String addres, final int regionId) {
//        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
//        progressDialog.setMessage("جارى تحميل البيانات ...");
//        progressDialog.setCancelable(false);
//        progressDialog.show();
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://onlineapi.sweverteam.com/Order/Order",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                        progressDialog.dismiss();
//                        try {
//                            //Todo: Still Just Test And Retrieve Data
//                            JSONObject object = new JSONObject(response);
//                            JSONArray array = object.getJSONArray("Fields");
//                            if (array.length() > 0) {
//
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                progressDialog.dismiss();
//                if (error instanceof ServerError)
//                    Toast.makeText(getActivity(), "خطأ إثناء الاتصال بالخادم", Toast.LENGTH_SHORT).show();
//                else if (error instanceof NetworkError)
//                    Toast.makeText(getActivity(), "خطأ فى شبكه الانترنت", Toast.LENGTH_SHORT).show();
//                else if (error instanceof TimeoutError)
//                    Toast.makeText(getActivity(), "خطأ فى مده الانتظار", Toast.LENGTH_SHORT).show();
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                HashMap<String,String> map = new HashMap<>();
//                map.put("token","?za[ZbGNz2B}MXYZ");
//                map.put("Order",mod);
//                map.put("Address",addres);
//                map.put("RegionId",regionId+"");
//                return map;
//            }
//        };
////        Volley.newRequestQueue(getActivity()).add(stringRequest);
//        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
//                2,  // maxNumRetries = 2 means no retry
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        Volley.newRequestQueue(getActivity()).add(stringRequest);
//    }
//
//    private void clearMessageView() {
//        if (confirm != null) {
//            ViewGroup parent = (ViewGroup) confirm.getParent();
//            if (parent != null) {
//                parent.removeAllViews();
//            }
//        }
//    }
//
//    private void closeMessage(final Dialog dialog) {
//        ignore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                clearMessageView();
//                dialog.dismiss();
//            }
//        });
//    }

    private void loadChart() {

        final int size = sanfList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                sanfList.remove(0);
            }
            adapter.notifyItemRangeRemoved(0, size);
        }

        int x=1;
        if (cursor != null) {
            while (cursor.moveToNext()) {

                Sanf s = new Sanf(Float.parseFloat(cursor.getString(6)), cursor.getString(1), cursor.getString(2), cursor.getString(5), cursor.getString(3), Float.parseFloat(cursor.getString(4)));
                x++;
                sanfList.add(s);
            }
        }

        adapter = new ChartAdapter(getActivity(), sanfList);
        recyclerView.setAdapter(adapter);
    }

//    private void fillCitys(String capital) {
//        List<String> filteredCitys = new ArrayList<>();
//        for (int x= 0; x<stateNames.size(); x++){
//            if (states.get(x).getName().equals(capital)){
//                for (int y=0; y<cities.size(); y++){
//                    if (cities.get(y).getStateId() == states.get(x).getId()){
//                        filteredCitys.add(cities.get(y).getName());
//                    }
//                }
//            }
//        }
//        city.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, filteredCitys));
//    }
//
//    private void fillRegions(String regio) {
//        List<String> filteredRegions = new ArrayList<>();
//        for (int x= 0; x<cityNames.size(); x++){
//            if (cities.get(x).getName().equals(regio)){
//                for (int y=0; y<regions.size(); y++){
//                    if (regions.get(y).getCityId() == cities.get(x).getId()){
//                        filteredRegions.add(regions.get(y).getName());
//                    }
//                }
//            }
//        }
//        region.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, filteredRegions));
//    }
//
//    private void loadDelvry() {
//        states = new ArrayList<>();
//        regions = new ArrayList<>();
//        cities = new ArrayList<>();
//        stateNames = new ArrayList<>();
//        regionNames = new ArrayList<>();
//        cityNames = new ArrayList<>();
//        regionsId = new ArrayList<>();
//
//        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://sellsapi.sweverteam.com/States/Select", new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    JSONArray jsonArray = jsonObject.getJSONArray("State");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
//                        String fname = jsonObject1.getString("Name");
//                        int id = jsonObject1.getInt("Id");
//                        State state = new State(id,fname);
//                        stateNames.add(fname);
//                        states.add(state);
//                    }
//
//                    JSONArray jsonArray1 = jsonObject.getJSONArray("Cities");
//                    for (int i = 0; i < jsonArray1.length(); i++) {
//                        JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
//                        String fname = jsonObject1.getString("Name");
//                        int id = jsonObject1.getInt("Id");
//                        int StateId = jsonObject1.getInt("StateId");
//                        Cities citie = new Cities(id,StateId,fname);
//                        cityNames.add(fname);
//                        cities.add(citie);
//                    }
//
//                    JSONArray jsonArray2 = jsonObject.getJSONArray("Regions");
//                    for (int i = 0; i < jsonArray2.length(); i++) {
//                        JSONObject jsonObject1 = jsonArray2.getJSONObject(i);
//                        String fname = jsonObject1.getString("Name");
//                        int id = jsonObject1.getInt("Id");
//                        int CityId = jsonObject1.getInt("CityId");
//                        Regions region = new Regions(id,CityId,fname);
//                        regionNames.add(fname);
//                        regions.add(region);
//                        regionsId.add(id);
//                    }
//
//                    capital.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, stateNames));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                if (error instanceof ServerError)
//                    Toast.makeText(getActivity(), "خطأ إثناء الاتصال بالخادم", Toast.LENGTH_SHORT).show();
//                else if (error instanceof NetworkError)
//                    Toast.makeText(getActivity(), "خطأ فى شبكه الانترنت", Toast.LENGTH_SHORT).show();
//                else if (error instanceof TimeoutError)
//                    Toast.makeText(getActivity(), "خطأ فى مده الانتظار", Toast.LENGTH_SHORT).show();
//            }
//        });
//        int socketTimeout = 30000;
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 2, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        stringRequest.setRetryPolicy(policy);
//        requestQueue.add(stringRequest);
//    }
}