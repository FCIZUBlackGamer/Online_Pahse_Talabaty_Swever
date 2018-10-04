package talabaty.swever.com.online.Contact;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import talabaty.swever.com.online.Fields.MostTrend.Product;
import talabaty.swever.com.online.R;
public class FragmentHomeContacts extends Fragment {


    RecyclerView gridView;
    ContactAdapter booksAdapter;
    List<Product> products;

    int temp_first, temp_last;
    TextView next, num, last;
    int item_num, page_num;
    static String phon, emai, addres, nam, log;
    static float ba;

    RecyclerView recyclerView_offer;
    RecyclerView.Adapter offerAdapter;
    List<Product> offerList;

    TextView phone, email, address, name;
    RatingBar bar;
    ImageView logo;
    static int ShopId;

    public static FragmentHomeContacts setData(int Id, String phone, String email, String address, String name, String logo, float bar) {
        FragmentHomeContacts fragmentHomeContacts = new FragmentHomeContacts();
        phon = phone;
        emai = email;
        addres = address;
        nam = name;
        log = logo;
        ba = bar;
        ShopId = Id;
        return fragmentHomeContacts;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        View view = inflater.inflate(R.layout.fragment_contacts_home, container, false);
        gridView = (RecyclerView) view.findViewById(R.id.gridview);
        gridView.setHasFixedSize(true);
        gridView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));

        recyclerView_offer = (RecyclerView) view.findViewById(R.id.gridview);
        recyclerView_offer.setHasFixedSize(true);
        recyclerView_offer.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));

        next = view.findViewById(R.id.next);
        last = view.findViewById(R.id.previous);
        num = view.findViewById(R.id.item_num);
        item_num = page_num = 0;
        num.setText(1 + "");
        products = new ArrayList<>();
        phone = view.findViewById(R.id.company_phone);
        email = view.findViewById(R.id.company_email);
        name = view.findViewById(R.id.company_name);
        address = view.findViewById(R.id.company_address);
        bar = view.findViewById(R.id.company_rate);
        logo = view.findViewById(R.id.company_logo);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        /** Basic Info*/
        phone.setText(phon);
        name.setText(nam);
        email.setText(emai);
        address.setText(addres);
        bar.setRating(ba);
        if (!log.isEmpty()) {
            Picasso.with(getActivity()).load(log).into(logo);
        }

        temp_first = 0;
        temp_last = 10;
        page_num = 0;

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Item Num", item_num + "");
                if (products.size() == 80) {
                    loadData(item_num, "1");
                } else {
                    Snackbar.make(v, "نهايه المنتجات", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (page_num > 1) {
                    Log.e("Item Num", item_num + "");
                    loadData(item_num, "0");
                } else {
                    Snackbar.make(v, "بدايه المنتجات", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });


        /** Adapter Montag*/

        loadData(0, "1");
        loadContactOffersPrepareFood(ShopId);

    }

    private void loadData(final int x, final String type) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("جارى تحميل البيانات ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://onlineapi.sweverteam.com/Products/MostVisited",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int temp = 0;
                        progressDialog.dismiss();
                        try {

                            JSONObject object = new JSONObject(response);
                            JSONArray array = object.getJSONArray("List");
                            if (array.length() > 0) {
                                final int size = products.size();
                                if (size > 0) {
                                    for (int i = 0; i < size; i++) {
                                        products.remove(0);
                                    }
                                    booksAdapter.notifyDataSetChanged();
                                }

                                for (int x = 0; x < array.length(); x++) {
                                    JSONObject object1 = array.getJSONObject(x);
                                    if (x == 0) {
                                        temp_first = object1.getInt("Id");
                                    } else if (x == array.length() - 1) {
                                        temp_last = object1.getInt("Id");
                                    }


                                    Product r = new Product(object1.getInt("Id"),
                                            object1.getString("Name"),
                                            "http://selltlbaty.sweverteam.com" + object1.getString("Photo"),
                                            object1.getInt("Price"),
                                            object1.getInt("Sale"),
                                            object1.getInt("Rate")
                                    );
                                    products.add(r);
                                    temp = object1.getInt("Id");


                                }

                                if (type.equals("1")) {
                                    page_num++;
                                } else if (type.equals("0")) {
                                    page_num--;
                                }

                                booksAdapter = new ContactAdapter(getActivity(), products, new ContactAdapter.OnItemClickListener(){

                                    @Override
                                    public void onItemClick(Product item) {
                                        //Todo: Make Some Action
                                        Log.e("ProductId",item.getId()+"");
                                    }
                                });
                                gridView.setAdapter(booksAdapter);

                                item_num = temp;

                                num.setText(page_num + "");
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
                progressDialog.dismiss();

                if (error instanceof ServerError)
                    Toast.makeText(getActivity(), "خطأ إثناء الاتصال بالخادم", Toast.LENGTH_SHORT).show();
                else if (error instanceof NetworkError)
                    Toast.makeText(getActivity(), "خطأ فى شبكه الانترنت", Toast.LENGTH_SHORT).show();
                else if (error instanceof TimeoutError)
                    Toast.makeText(getActivity(), "خطأ فى مده الانتظار", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("type", type + "");
                map.put("x", x + "");
                map.put("count", 80 + "");
                map.put("token", "?za[ZbGNz2B}MXYZ");
                return map;
            }
        };
//        Volley.newRequestQueue(getActivity()).add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                2,  // maxNumRetries = 2 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    private void loadContactOffersPrepareFood(final int shopId) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("جارى تحميل البيانات ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://onlineapi.sweverteam.com/Offers/ListByShop",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject object = new JSONObject(response);
                            JSONArray array = object.getJSONArray("Offers");
                            if (array.length() > 0) {

                                final int size = offerList.size();
                                if (size > 0) {
                                    for (int i = 0; i < size; i++) {
                                        offerList.remove(0);

                                    }
                                    offerAdapter.notifyDataSetChanged();
                                }


                                for (int x = 0; x < array.length(); x++) {
                                    JSONObject object1 = array.getJSONObject(x);

                                    Product r = new Product(object1.getInt("Id"),
                                            object1.getString("Name"),
                                            "http://selltlbaty.sweverteam.com" + object1.getString("Photo"),
                                            object1.getInt("Price"),
                                            0,
                                            0
                                    );
                                    offerList.add(r);

                                }

                                offerAdapter = new ContactAdapter(getActivity(), offerList, new ContactAdapter.OnItemClickListener(){

                                    @Override
                                    public void onItemClick(Product item) {
                                        //Todo: Make Some Action
                                        Log.e("ProductId",item.getId()+"");
                                    }
                                });
                                recyclerView_offer.setAdapter(offerAdapter);

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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("ShopId", shopId+ "");
                map.put("token", "?za[ZbGNz2B}MXYZ");
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
