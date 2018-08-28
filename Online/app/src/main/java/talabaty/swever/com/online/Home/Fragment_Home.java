package talabaty.swever.com.online.Home;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
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
import talabaty.swever.com.online.Fields.MostViewed.*;
import talabaty.swever.com.online.Fields.MostTrend.*;
import talabaty.swever.com.online.SubCategory.*;

public class Fragment_Home extends Fragment {

    RecyclerView recyclerViewcontact, recyclerViewproducts;
    RecyclerView.Adapter adaptercontact, adapterproducts;
    List<Contact> contactList;
    List<Product> productList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        View view = inflater.inflate(R.layout.fragment_mainhome_home, container, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewproducts = view.findViewById(R.id.rec_product);
        recyclerViewproducts.setLayoutManager(layoutManager);
        productList = new ArrayList<>();

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewcontact = view.findViewById(R.id.rec_contact);
        recyclerViewcontact.setLayoutManager(layoutManager1);
        contactList = new ArrayList<>();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        //Todo: get List Of Products
        loadProduct();

        //Todo: get List Of Contacts
        loadContact();


    }

    private void loadProduct() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("جارى تحميل البيانات ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://onlineapi.sweverteam.com/Home/ListProduct",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject object = new JSONObject(response);
                            JSONArray array = object.getJSONArray("List");
                            if (array.length() > 0) {
                                final int size = productList.size();
                                if (size > 0) {
                                    for (int i = 0; i < size; i++) {
                                        productList.remove(0);
                                    }
                                    adapterproducts.notifyDataSetChanged();
                                }

                                for (int x = 0; x < array.length(); x++) {
                                    JSONObject object1 = array.getJSONObject(x);

                                    Product r = new Product(object1.getInt("Id"),
                                            object1.getString("Name"),
                                            "http://selltlbaty.sweverteam.com" + object1.getString("Photo"),
                                            (float) object1.getDouble("Price"),
                                            (float) object1.getDouble("Sale"),
                                            (float) object1.getDouble("Rate")
                                    );
                                    productList.add(r);

                                }

                                adapterproducts = new ProductAdapter(getActivity(),productList);
                                recyclerViewproducts.setAdapter(adapterproducts);

                            } else {
                                Toast toast = Toast.makeText(getActivity(), "لا توجد منتجات جديده", Toast.LENGTH_SHORT);
                                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                                v.setTextColor(Color.GREEN);
                                toast.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof ServerError)
                    Toast.makeText(getActivity(), "خطأ إثناء الاتصال بالخادم", Toast.LENGTH_SHORT).show();
                else if (error instanceof NetworkError)
                    Toast.makeText(getActivity(), "خطأ فى شبكه الانترنت", Toast.LENGTH_SHORT).show();
                else if (error instanceof TimeoutError)
                    Toast.makeText(getActivity(), "خطأ فى مده الانتظار", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("count",80+"");
                map.put("token","?za[ZbGNz2B}MXYZ");
                return map;
            }
        };
        progressDialog.dismiss();
//        Volley.newRequestQueue(getActivity()).add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                2,  // maxNumRetries = 2 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    private void loadContact() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("جارى تحميل البيانات ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://onlineapi.sweverteam.com/Home/ShopList",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject object = new JSONObject(response);
                            JSONArray array = object.getJSONArray("List");
                            if (array.length() > 0) {
                                final int size = contactList.size();
                                if (size > 0) {
                                    for (int i = 0; i < size; i++) {
                                        contactList.remove(0);
                                    }
                                    adaptercontact.notifyDataSetChanged();
                                }

                                for (int x = 0; x < array.length(); x++) {
                                    JSONObject object1 = array.getJSONObject(x);

                                    Contact info = new Contact(
                                            object1.getInt("Id"),
                                            object1.getString("Name"),
                                            (float) object1.getDouble("Rate"),
                                            object1.getString("Name"),
                                            object1.getString("Address"),
                                            object1.getString("Phone"),
                                            "http://selltlbaty.sweverteam.com"+object1.getString("Photo"),
                                            ""
                                    );
                                    contactList.add(info);

                                }

                                adaptercontact = new ContactAdapter(getActivity(),contactList);
                                recyclerViewcontact.setAdapter(adaptercontact);

                            } else {
                                Toast toast = Toast.makeText(getActivity(), "لا توجد جهات عمل جديده", Toast.LENGTH_SHORT);
                                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                                v.setTextColor(Color.GREEN);
                                toast.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof ServerError)
                    Toast.makeText(getActivity(), "خطأ إثناء الاتصال بالخادم", Toast.LENGTH_SHORT).show();
                else if (error instanceof NetworkError)
                    Toast.makeText(getActivity(), "خطأ فى شبكه الانترنت", Toast.LENGTH_SHORT).show();
                else if (error instanceof TimeoutError)
                    Toast.makeText(getActivity(), "خطأ فى مده الانتظار", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("count",80+"");
                map.put("token","?za[ZbGNz2B}MXYZ");
                return map;
            }
        };
        progressDialog.dismiss();
//        Volley.newRequestQueue(getActivity()).add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                2,  // maxNumRetries = 2 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }
}
