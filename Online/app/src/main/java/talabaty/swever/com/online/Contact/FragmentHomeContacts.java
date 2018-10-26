package talabaty.swever.com.online.Contact;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
    ProgressDialog progressDialog, progressDialog2;
    int rate_num = 0;

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

        recyclerView_offer = (RecyclerView) view.findViewById(R.id.rec_offers);
        recyclerView_offer.setHasFixedSize(true);
        recyclerView_offer.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));

        next = view.findViewById(R.id.next);
        last = view.findViewById(R.id.previous);
        num = view.findViewById(R.id.item_num);
        item_num = page_num = 0;
        num.setText(1 + "");
        products = new ArrayList<>();
//        phone = view.findViewById(R.id.company_phone);
//        email = view.findViewById(R.id.company_email);
        name = view.findViewById(R.id.company_name);
//        address = view.findViewById(R.id.company_address);
        bar = view.findViewById(R.id.company_rate);
        logo = view.findViewById(R.id.company_logo);

        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onStart() {
        super.onStart();

        /** Basic Info*/
//        phone.setText(phon);
        name.setText(nam);
//        email.setText(emai);
//        address.setText(addres);
        bar.setRating(ba);
        if (!log.isEmpty()) {
            Picasso.with(getActivity()).load(log).into(logo);
        }

        temp_first = 0;
        temp_last = 10;
        page_num = 0;

        bar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                rate_num = 0;

                final LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final View rate = inflater.inflate(R.layout.rate_view, null);

                final RatingBar bar = rate.findViewById(R.id.offer_rate);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("ضع تقييم للمنتج")
                        .setView(rate)
                        .setPositiveButton("تقييم", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                /** Upload Rate */
                                progressDialog = new ProgressDialog(getActivity());
                                progressDialog.setCancelable(false);
                                progressDialog.show();
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://onlineapi.rivile.com/Shops/Rate",
                                        new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                Log.e("Response", response);
                                                progressDialog.dismiss();
                                                String result = "";
                                                float rate_num = 0;
                                                try {
                                                    JSONObject res = new JSONObject(response);
                                                    result = res.getString("success");
                                                    rate_num = (float) res.getDouble("Rate");

                                                    if (result.equals("Success")) {
                                                        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                                                        View layout = inflater.inflate(R.layout.toast_info, null);

                                                        TextView text = (TextView) layout.findViewById(R.id.txt);
                                                        text.setText("تمت العملية بنجاح");

                                                        Toast toast = new Toast(getActivity());
                                                        toast.setGravity(Gravity.BOTTOM, 0, 0);
                                                        toast.setDuration(Toast.LENGTH_LONG);
                                                        toast.setView(layout);
                                                        toast.show();
                                                        bar.setRating(rate_num);

                                                    } else {

                                                        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                                                        View layout = inflater.inflate(R.layout.toast_warning, null);

                                                        TextView text = (TextView) layout.findViewById(R.id.txt);
                                                        text.setText("عذرا حدث خطأ أثناء اجراء العملية  .. يرجي المحاوله لاحقا");

                                                        Toast toast = new Toast(getActivity());
                                                        toast.setGravity(Gravity.BOTTOM, 0, 0);
                                                        toast.setDuration(Toast.LENGTH_LONG);
                                                        toast.setView(layout);
                                                        toast.show();
                                                    }
                                                } catch (JSONException e) {

                                                }

                                            }
                                        }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        progressDialog.dismiss();
                                        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                                        View layout = inflater.inflate(R.layout.toast_warning, null);

                                        TextView text = (TextView) layout.findViewById(R.id.txt);

                                        if (error instanceof ServerError)
                                            text.setText("خطأ فى الاتصال بالخادم");
                                        else if (error instanceof TimeoutError)
                                            text.setText("خطأ فى مدة الاتصال");
                                        else if (error instanceof NetworkError)
                                            text.setText("شبكه الانترنت ضعيفه حاليا");

                                        Toast toast = new Toast(getActivity());
                                        toast.setGravity(Gravity.BOTTOM, 0, 0);
                                        toast.setDuration(Toast.LENGTH_LONG);
                                        toast.setView(layout);
                                        toast.show();
                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        HashMap<String, String> map = new HashMap<>();
                                        map.put("Id", ShopId + "");
                                        map.put("vote", (int) rate_num + "");
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

                                dialog.dismiss();
                                if (rate != null) {
                                    ViewGroup parent = (ViewGroup) rate.getParent();
                                    if (parent != null) {
                                        parent.removeAllViews();
                                    }
                                }
                            }
                        }).setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (rate != null) {
                            ViewGroup parent = (ViewGroup) rate.getParent();
                            if (parent != null) {
                                parent.removeAllViews();
                            }
                        }

                    }
                }).show();


                bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        rate_num = (int) rating;
                    }
                });

                return false;
            }
        });

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
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("جارى تحميل المنتجات ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://onlineapi.rivile.com/Products/MostVisited",
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
                                            "http://selltlbaty.rivile.com" + object1.getString("Photo"),
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
                                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                                View layout = inflater.inflate(R.layout.toast_info, null);

                                TextView text = (TextView) layout.findViewById(R.id.txt);
                                text.setText("لا توجد منتجات");

                                Toast toast = new Toast(getActivity());
                                toast.setGravity(Gravity.BOTTOM, 0, 0);
                                toast.setDuration(Toast.LENGTH_LONG);
                                toast.setView(layout);
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

                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View layout = inflater.inflate(R.layout.toast_warning, null);

                TextView text = (TextView) layout.findViewById(R.id.txt);

                if (error instanceof ServerError)
                    text.setText("خطأ فى الاتصال بالخادم");
                else if (error instanceof TimeoutError)
                    text.setText("خطأ فى مدة الاتصال");
                else if (error instanceof NetworkError)
                    text.setText("شبكه الانترنت ضعيفه حاليا");

                Toast toast = new Toast(getActivity());
                toast.setGravity(Gravity.BOTTOM, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();
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
        offerList = new ArrayList<>();
        progressDialog2 = new ProgressDialog(getActivity());
        progressDialog2.setMessage("جارى تحميل العروض ...");
        progressDialog2.setCancelable(false);
        progressDialog2.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://onlineapi.rivile.com/Offers/ListByShop",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog2.dismiss();
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
                                            "http://selltlbaty.rivile.com" + object1.getString("Photo"),
                                            object1.getInt("Price"),
                                            0,
                                            0
                                    );
                                    offerList.add(r);

                                }

                                offerAdapter = new ContactAdapter(1, getActivity(), offerList, new ContactAdapter.OnItemClickListener() {

                                    @Override
                                    public void onItemClick(Product item) {
                                        //Todo: Make Some Action
                                        Log.e("ProductId",item.getId()+"");
                                    }
                                });
                                recyclerView_offer.setAdapter(offerAdapter);

                            } else {
                                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                                View layout = inflater.inflate(R.layout.toast_info, null);

                                TextView text = (TextView) layout.findViewById(R.id.txt);
                                text.setText("لا توجد عروض");

                                Toast toast = new Toast(getActivity());
                                toast.setGravity(Gravity.BOTTOM, 0, 0);
                                toast.setDuration(Toast.LENGTH_LONG);
                                toast.setView(layout);
                                toast.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog2.dismiss();
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View layout = inflater.inflate(R.layout.toast_warning, null);

                TextView text = (TextView) layout.findViewById(R.id.txt);

                if (error instanceof ServerError)
                    text.setText("خطأ فى الاتصال بالخادم");
                else if (error instanceof TimeoutError)
                    text.setText("خطأ فى مدة الاتصال");
                else if (error instanceof NetworkError)
                    text.setText("شبكه الانترنت ضعيفه حاليا");

                Toast toast = new Toast(getActivity());
                toast.setGravity(Gravity.BOTTOM, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();
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

//        Volley.newRequestQueue(getActivity()).add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                2,  // maxNumRetries = 2 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        if (progressDialog2 != null && progressDialog2.isShowing()) {
            progressDialog2.dismiss();
        }

    }
}
