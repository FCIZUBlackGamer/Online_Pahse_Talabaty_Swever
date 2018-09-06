package talabaty.swever.com.online.Home;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInDownAnimator;
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;
import pl.droidsonroids.gif.GifImageView;
import talabaty.swever.com.online.R;
import talabaty.swever.com.online.Fields.MostViewed.*;
import talabaty.swever.com.online.Fields.MostTrend.*;
import talabaty.swever.com.online.SubCategory.*;

public class Fragment_Home extends Fragment {

    RecyclerView recyclerViewcontact, recyclerViewproducts;
    RecyclerView.Adapter adaptercontact, adapterproducts;
    List<Contact> contactList;
    List<Product> productList;
    View view;
    LayoutInflater inflate;
    ViewGroup containe;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        inflate = inflater;
        containe = container;
        setUi(inflate, containe);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewproducts = view.findViewById(R.id.rec_product);
        recyclerViewproducts.setLayoutManager(layoutManager);
        recyclerViewproducts.setItemAnimator(new FadeInDownAnimator(new OvershootInterpolator(1f)));
        productList = new ArrayList<>();

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewcontact = view.findViewById(R.id.rec_contact);
        recyclerViewcontact.setLayoutManager(layoutManager1);
        recyclerViewcontact.setItemAnimator(new FadeInUpAnimator(new OvershootInterpolator(1f)));
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

    public void setUi(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_mainhome_home, container, false);
    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        ((ScrollView)getActivity().findViewById(R.id.ss)).removeAllViews();
//        super.onConfigurationChanged(newConfig);
//
//        // Checks the orientation of the screen
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            Toast.makeText(getContext(), "landscape", Toast.LENGTH_SHORT).show();
//
//        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
//            Toast.makeText(getContext(), "portrait", Toast.LENGTH_SHORT).show();
//        }
//        setUi(inflate, containe);
//    }

    private void loadProduct() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        GifImageView gifImageView = new GifImageView(getActivity());
        gifImageView.setImageResource(R.drawable.load);
        builder.setCancelable(false);
        builder.setView(gifImageView);
        final AlertDialog dlg = builder.create();
        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dlg.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://onlineapi.sweverteam.com/Home/ListProduct",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                dlg.dismiss();
                                try {

                                    JSONObject object = new JSONObject(response);
                                    final JSONArray array = object.getJSONArray("List");
                                    if (array.length() > 0) {

                                        final int size = productList.size();
                                        if (size > 0) {
                                            for (int i = 0; i < size; i++) {
                                                productList.remove(0);
                                                adapterproducts.notifyItemRemoved(i);
                                            }
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

                                        adapterproducts = new ProductAdapter(getActivity(), productList);
                                        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapterproducts);
                                        alphaAdapter.setDuration(3000);
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
                        }, 5000);   //5 seconds
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        dlg.dismiss();
                    }
                }, 2500);   //5 seconds

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
                                            "http://selltlbaty.sweverteam.com" + object1.getString("Photo"),
                                            ""
                                    );
                                    contactList.add(info);

                                }

                                adaptercontact = new ContactAdapter(getActivity(), contactList);
                                AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adaptercontact);
                                alphaAdapter.setDuration(3000);
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("count", 80 + "");
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
