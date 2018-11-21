package talabaty.swever.com.online.Fields.MostTrend;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
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
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import talabaty.swever.com.online.ProductDetails.FragmentProductDetails;
import talabaty.swever.com.online.R;
import talabaty.swever.com.online.Switch_nav;

public class FragmentMostTrend extends Fragment {

    GridView gridView;
    MontagAdapter booksAdapter;
    List<Product> products;
    int temp_first, temp_last;
    TextView next, num, last;
    int item_num, page_num;

    FragmentManager fragmentManager;
    LinearLayout linearLayout, layout;

    ProgressDialog progressDialog, progressDialog2;

    // http://onlineapi.sweverteam.com/Products/MostVisited/list?type=1&x=0&count=10&token=?za[ZbGNz2B}MXYZ
    static String Type = "null";
    String Link;
    int amount = 0;

    public FragmentMostTrend setType(String type) {
        FragmentMostTrend trend = new FragmentMostTrend();
        Type = type;
        return trend;
    }

    static List<Product> product_List = null;

    public static FragmentMostTrend setList(List<Product> contact_Li){
        FragmentMostTrend contact = new FragmentMostTrend();
        product_List = contact_Li;
        return contact;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        View view = inflater.inflate(R.layout.fragment_home_most_trend, container, false);
        gridView = (GridView) view.findViewById(R.id.gridview);
        next = view.findViewById(R.id.next);
        last = view.findViewById(R.id.previous);
        num = view.findViewById(R.id.item_num);
        layout = view.findViewById(R.id.layout);
        linearLayout = view.findViewById(R.id.d);
        item_num = page_num = 0;
        num.setText(1 + "");
        products = new ArrayList<>();
        fragmentManager = getFragmentManager();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        temp_first = 0;
        temp_last = 10;
        page_num = 0;

        if (product_List!= null){
//            for (Fragment fragment:getActivity().getSupportFragmentManager().getFragments()) {
//                getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
//            }

            Gson gson = new Gson();
            Log.e("Product",gson.toJson(product_List));
            layout.setVisibility(View.GONE);

            booksAdapter = new MontagAdapter(getActivity(), product_List);
            gridView.setAdapter(booksAdapter);
            Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.fly_in_from_center);
            gridView.setAnimation(anim);
            anim.start();
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Product book = product_List.get(position);

                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_home, new FragmentProductDetails().setId(book.getId(),0)).addToBackStack("FragmentOfferDetails").commit();
                }
            });

        }else {
            if (Type.equals("trend")) {
/** الاكثر زياره لجهات العمل*/
                Link = "http://onlineapi.rivile.com/Products/MostVisited/list";
                amount = 0;
                linearLayout.setVisibility(View.VISIBLE);
                loadData(0, "1");
            } else if (Type.equals("latest")) {
/** الاجدد من المنتجات*/
                Link = "http://onlineapi.rivile.com/Products/List";
                amount = 1;
                linearLayout.setVisibility(View.VISIBLE);
                layout.setVisibility(View.GONE);
                loadData(0, "1");
            } else if (Type.equals("offers")) {
/** العروض*/
                Link = "http://onlineapi.rivile.com/Offers/List";
                amount = 1;
                linearLayout.setVisibility(View.VISIBLE);
                loadOffers();
            } else {

                Link = "http://onlineapi.rivile.com/Products/List";
                amount = 0;
                linearLayout.setVisibility(View.VISIBLE);
                loadData(0, "1");
            }

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


        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }if(progressDialog2 != null && progressDialog2.isShowing()){
            progressDialog2.dismiss();
        }

    }

    private void loadData(final int x, final String type) {
        progressDialog2 = new ProgressDialog(getActivity());
        progressDialog2.setMessage("جارى تحميل البيانات ...");
        progressDialog2.setCancelable(false);
        progressDialog2.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Link,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int temp = 0;

                        progressDialog2.dismiss();

                        try {

                            JSONObject object = new JSONObject(response);
                            JSONArray array = object.getJSONArray("List");
                            if (array.length() > 0) {
//                                final int size = products.size();
//                                if (size > 0) {
//                                    for (int i = 0; i < size; i++) {
//                                        products.remove(0);
//                                    }
//                                    booksAdapter.notifyDataSetChanged();
//                                }
                                products = new ArrayList<>();

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
                                    r.setIsOffer(0);
                                    products.add(r);
                                    temp = object1.getInt("Id");

                                }
                                if (type.equals("1")) {
                                    page_num++;
                                } else if (type.equals("0")) {
                                    page_num--;
                                }
                                booksAdapter = new MontagAdapter(getActivity(), products);
                                gridView.setAdapter(booksAdapter);
                                Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.fly_in_from_center);
                                gridView.setAnimation(anim);
                                anim.start();
                                item_num = temp;
                                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Product book = products.get(position);
/** عرض تفاصيل المنتج*/
                                        fragmentManager.beginTransaction()
                                                .replace(R.id.frame_home, new FragmentProductDetails().setId(book.getId(),0)).addToBackStack("FragmentOfferDetails").commit();
                                    }
                                });
                                num.setText(page_num + "");
                            } else {
                                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                                View layout = inflater.inflate(R.layout.toast_info,null);

                                TextView text = (TextView) layout.findViewById(R.id.txt);
                                text.setText("لا توجد بيانات");

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

                View layout = inflater.inflate(R.layout.toast_warning,null);

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
                if (amount == 1){
                    map.put("count", 10 + "");
                }else {
                    map.put("count", 80 + "");
                }
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

    private void loadOffers() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("جارى تحميل البيانات ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Link,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int temp = 0;

                        progressDialog.dismiss();

                        try {

                            JSONObject object = new JSONObject(response);
                            JSONArray array = object.getJSONArray("List");
                            if (array.length() > 0) {
//                                final int size = products.size();
//                                if (size > 0) {
//                                    for (int i = 0; i < size; i++) {
//                                        products.remove(0);
//                                    }
//                                    booksAdapter.notifyDataSetChanged();
//                                }
                                products = new ArrayList<>();

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
                                    r.setIsOffer(1);
                                    products.add(r);
                                    temp = object1.getInt("Id");


                                }
                                booksAdapter = new MontagAdapter(getActivity(), products);
                                gridView.setAdapter(booksAdapter);
                                Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.fly_in_from_center);
                                gridView.setAnimation(anim);
                                anim.start();
                                item_num = temp;
                                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Product book = products.get(position);

                                        fragmentManager.beginTransaction()
                                                .replace(R.id.frame_home, new FragmentProductDetails().setId(book.getId(),1)).addToBackStack("FragmentOfferDetails").commit();
                                    }
                                });
                                num.setText(page_num + "");
                            } else {
                                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                                View layout = inflater.inflate(R.layout.toast_info,null);

                                TextView text = (TextView) layout.findViewById(R.id.txt);
                                text.setText("لا توجد بيانات");

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

                View layout = inflater.inflate(R.layout.toast_warning,null);

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
}
