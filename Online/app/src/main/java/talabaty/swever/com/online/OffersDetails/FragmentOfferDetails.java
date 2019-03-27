package talabaty.swever.com.online.OffersDetails;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
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

import talabaty.swever.com.online.Cart.CartDatabase;
import talabaty.swever.com.online.Cart.Models.OperOrderModel;
import talabaty.swever.com.online.Cart.Sanf;
import talabaty.swever.com.online.R;
import talabaty.swever.com.online.Utils.AppToastUtil;

public class FragmentOfferDetails extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<Sanf> sanfList;

    ImageView OperOffer_image;

    Button add;

    CartDatabase cartDatabase;

    TextView title, price, desc;
    RatingBar ratingBar;
    Spinner amount;
    //    RatingBar company_rate;
    View view;
    String contact_name, address, image_string;

    ProgressDialog progressDialog;

    static int id, offer_type;
    String Link = null;
    List<OperOrderModel> orderModels;
    int rate_num = 0;

    public FragmentOfferDetails setId(int Id, int Offer_type) {
        FragmentOfferDetails details = new FragmentOfferDetails();
        id = Id;
        offer_type = Offer_type;
        return details;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        view = inflater.inflate(R.layout.fragment_offer_detail, container, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView = view.findViewById(R.id.rec_products);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        add = view.findViewById(R.id.add);
        title = view.findViewById(R.id.offer_name);
        price = view.findViewById(R.id.price);
        desc = view.findViewById(R.id.offer_desc);
        ratingBar = view.findViewById(R.id.offer_rate);
        OperOffer_image = view.findViewById(R.id.OperOffer_image);
        amount = view.findViewById(R.id.amount);


        sanfList = new ArrayList<>();
        cartDatabase = new CartDatabase(getActivity());
        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onStart() {
        super.onStart();

        Link = "http://onlineapi.rivile.com/Offers/OffersDetails";

        loadDetails(id); /** Offer Id*/

        ratingBar.setNumStars(5);

        ratingBar.setOnTouchListener((v, event) -> {
            rate_num = 0;

            final LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            final View rate = inflater.inflate(R.layout.rate_view, null);

            final RatingBar bar = rate.findViewById(R.id.offer_rate);

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("ضع تقييم للمنتج")
                    .setView(rate)
                    .setPositiveButton("تقييم", (dialog, which) -> {

                        /** Upload Rate */
                        progressDialog = new ProgressDialog(getActivity());
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://onlineapi.rivile.com/Offers/Rate",
                                response -> {
                                    Log.e("Response", response);
                                    progressDialog.dismiss();
                                    String result = "";
                                    float rate_num = 0;
                                    try {
                                        JSONObject res = new JSONObject(response);
                                        result = res.getString("success");
                                        rate_num = (float) res.getDouble("Rate");

                                        if (result.equals("Success")) {
                                            AppToastUtil.showInfoToast("تمت العملية بنجاح",
                                                    AppToastUtil.LENGTH_LONG, getContext());
                                            ratingBar.setRating(rate_num);

                                        } else {
                                            AppToastUtil.showWarningToast("عذرا حدث خطأ أثناء اجراء العملية  .. يرجي المحاوله لاحقا",
                                                    AppToastUtil.LENGTH_LONG, getContext());
                                        }
                                    } catch (JSONException e) {

                                    }
                                }, error -> {
                            progressDialog.dismiss();

                            String WarningMessage = null;
                            if (error instanceof ServerError)
                                WarningMessage = "خطأ فى الاتصال بالخادم";
                            else if (error instanceof TimeoutError)
                                WarningMessage = "خطأ فى مدة الاتصال";
                            else if (error instanceof NetworkError)
                                WarningMessage = "شبكه الانترنت ضعيفه حاليا";

                            if (WarningMessage != null)
                                AppToastUtil.showWarningToast(WarningMessage,
                                        AppToastUtil.LENGTH_LONG, getContext());
                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                HashMap<String, String> map = new HashMap<>();
                                map.put("Id", id + "");
                                map.put("vote", rate_num + "");
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
                    }).setNegativeButton("إلغاء", (dialog, which) -> {
                        dialog.dismiss();
                        if (rate != null) {
                            ViewGroup parent = (ViewGroup) rate.getParent();
                            if (parent != null) {
                                parent.removeAllViews();
                            }
                        }

                    }).show();


            bar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> rate_num = (int) rating);

            return false;
        });

//        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//            @Override
//            public void onRatingChanged(RatingBar ratingBar, final float rating, boolean fromUser) {
//
//                progressDialog = new ProgressDialog(getActivity());
//                progressDialog.setCancelable(false);
//                progressDialog.show();
//                StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://onlineapi.rivile.com/Offers/Rate",
//                        new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//
//                                progressDialog.dismiss();
//                                if (response.equals("\"Success\"")) {
//                                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//                                    View layout = inflater.inflate(R.layout.toast_info, null);
//
//                                    TextView text = (TextView) layout.findViewById(R.id.txt);
//                                    text.setText("تمت العملية بنجاح");
//
//                                    AppToastUtil toast = new AppToastUtil(getActivity());
//                                    toast.setGravity(Gravity.BOTTOM, 0, 0);
//                                    toast.setDuration(AppToastUtil.LENGTH_LONG);
//                                    toast.setView(layout);
//                                    toast.show();
//
//                                } else {
//
//                                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//                                    View layout = inflater.inflate(R.layout.toast_warning, null);
//
//                                    TextView text = (TextView) layout.findViewById(R.id.txt);
//                                    text.setText("عذرا حدث خطأ أثناء اجراء العملية  .. يرجي المحاوله لاحقا");
//
//                                    AppToastUtil toast = new AppToastUtil(getActivity());
//                                    toast.setGravity(Gravity.BOTTOM, 0, 0);
//                                    toast.setDuration(AppToastUtil.LENGTH_LONG);
//                                    toast.setView(layout);
//                                    toast.show();
//                                }
//
//                            }
//                        }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        progressDialog.dismiss();
//                        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//                        View layout = inflater.inflate(R.layout.toast_warning, null);
//
//                        TextView text = (TextView) layout.findViewById(R.id.txt);
//
//                        if (error instanceof ServerError)
//                            text.setText("خطأ فى الاتصال بالخادم");
//                        else if (error instanceof TimeoutError)
//                            text.setText("خطأ فى مدة الاتصال");
//                        else if (error instanceof NetworkError)
//                            text.setText("شبكه الانترنت ضعيفه حاليا");
//
//                        AppToastUtil toast = new AppToastUtil(getActivity());
//                        toast.setGravity(Gravity.BOTTOM, 0, 0);
//                        toast.setDuration(AppToastUtil.LENGTH_LONG);
//                        toast.setView(layout);
//                        toast.show();
//                    }
//                }) {
//                    @Override
//                    protected Map<String, String> createNearestShopsParams() throws AuthFailureError {
//                        HashMap<String, String> map = new HashMap<>();
//                        map.put("Id", id + "");
//                        map.put("vote", (int) rating + "");
//                        map.put("token", "?za[ZbGNz2B}MXYZ");
//                        return map;
//                    }
//                };
////        Volley.newRequestQueue(getActivity()).add(stringRequest);
//                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
//                        2,  // maxNumRetries = 2 means no retry
//                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//                Volley.newRequestQueue(getActivity()).add(stringRequest);
//
//            }
//        });

    }

    @Override
    public void onPause() {
        super.onPause();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

    }

    private void loadDetails(final int id) {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("جارى تحميل البيانات ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Link,
                response -> {
                    progressDialog.dismiss();
                    try {

                        JSONObject object = new JSONObject(response);
                        JSONObject array = object.getJSONObject("Offers");
                        if (array.length() > 0) {
                            for (int x = 0; x < array.length(); x++) {

                                title.setText(array.getString("Name"));
                                if (!TextUtils.isEmpty(array.getString("Description"))) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        desc.setText(Html.fromHtml(array.getString("Description"), Html.FROM_HTML_MODE_COMPACT));
                                    } else {
                                        desc.setText(Html.fromHtml(array.getString("Description")));
                                    }
                                }

                                ratingBar.setRating((float) array.getDouble("Rate"));
                                price.setText(array.getDouble("Price") + " LE");

                                if (array.getString("Photo").isEmpty() || array.getString("Photo") == null) {
                                    image_string = "https://vignette.wikia.nocookie.net/simpsons/images/6/60/No_Image_Available.png";
                                    Picasso.get().load("https://vignette.wikia.nocookie.net/simpsons/images/6/60/No_Image_Available.png").into(OperOffer_image);
                                } else {
                                    image_string = "http://selltlbaty.rivile.com" + array.getString("Photo");
                                    Picasso.get().load("http://selltlbaty.rivile.com" + array.getString("Photo")).into(OperOffer_image);
                                }


                                /** OperOffer List*/
                                JSONArray OperOffer = new JSONArray(array.getString("OperOffer"));
                                if (OperOffer.length() > 0) {
                                    orderModels = new ArrayList<>();
                                    orderModels.clear();
                                    for (int i = 0; i < OperOffer.length(); i++) {
                                        JSONObject object2 = OperOffer.getJSONObject(i);
                                        int Amount = object2.getInt("Amount");
                                        JSONObject Product = object2.getJSONObject("Product");
                                        String name = Product.getString("Name");
                                        contact_name = Product.getString("ShopName");
                                        address = Product.getString("Address");
                                        double Price = Product.getDouble("Price");
                                        orderModels.add(new OperOrderModel(name, Amount, Price));

                                    }

                                    adapter = new OfferDetailsAdapter(getActivity(), orderModels);
                                    adapter.notifyDataSetChanged();
                                    recyclerView.setAdapter(adapter);


                                    add.setOnClickListener(v -> {
                                        long res = 0;
                                        try {
                                            String[] real_price = price.getText().toString().split(" ");
                                            res = cartDatabase.InsertData(title.getText().toString(),
                                                    image_string, "", "", "", amount.getSelectedItem().toString() + "",
                                                    "ممتازة", real_price[0], "", "", contact_name, address, id + "", "1");

                                            Log.e("ResultInserted", res + "");
                                        } catch (Exception e) {

                                        }
                                        if (res >= 1) {
                                            Snackbar.make(v, "تم اضافة المنتج لعربة التسوق", Snackbar.LENGTH_LONG).show();
                                        } else {

                                        }
                                    });

                                } else {
                                    AppToastUtil.showInfoToast("لا توجد بيانات تفصيليه للعرض ",
                                            AppToastUtil.LENGTH_LONG, getContext());
                                }
                            }
//                                incremwntView();
                        } else {
                            AppToastUtil.showInfoToast("لا توجد بيانات",
                                    AppToastUtil.LENGTH_LONG, getContext());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {
                    progressDialog.dismiss();

                    String WarningMessage = null;
                    if (error instanceof ServerError)
                        WarningMessage = "خطأ فى الاتصال بالخادم";
                    else if (error instanceof TimeoutError)
                        WarningMessage = "خطأ فى مدة الاتصال";
                    else if (error instanceof NetworkError)
                        WarningMessage = "شبكه الانترنت ضعيفه حاليا";

                    if (WarningMessage != null)
                        AppToastUtil.showWarningToast(WarningMessage,
                                AppToastUtil.LENGTH_LONG, getContext());
                }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> map = new HashMap<>();
                Log.e("OfferId", id + "");
                map.put("OfferId", id + "");
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

    private void incremwntView() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://onlineapi.rivile.com/Products/Visit",
                response -> Log.e("Response For Views", response), error -> {

                    String WarningMessage = null;
                    if (error instanceof ServerError)
                        WarningMessage = "خطأ فى الاتصال بالخادم";
                    else if (error instanceof TimeoutError)
                        WarningMessage = "خطأ فى مدة الاتصال";
                    else if (error instanceof NetworkError)
                        WarningMessage = "شبكه الانترنت ضعيفه حاليا";

                    if (WarningMessage != null)
                        AppToastUtil.showWarningToast(WarningMessage,
                                AppToastUtil.LENGTH_LONG, getContext());
                }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> map = new HashMap<>();
                map.put("Id", id + "");
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
