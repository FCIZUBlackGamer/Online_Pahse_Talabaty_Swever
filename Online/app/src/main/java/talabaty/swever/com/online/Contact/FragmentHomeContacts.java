package talabaty.swever.com.online.Contact;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
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

import talabaty.swever.com.online.Fields.MostTrend.Product;
import talabaty.swever.com.online.R;
import talabaty.swever.com.online.Utils.AppToastUtil;

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
        gridView = view.findViewById(R.id.gridview);
        gridView.setHasFixedSize(true);
        gridView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true));

        recyclerView_offer = view.findViewById(R.id.rec_offers);
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
            Picasso.get().load(log).into(logo);
        }

        temp_first = 0;
        temp_last = 10;
        page_num = 0;

        bar.setOnTouchListener((v, event) -> {
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
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://onlineapi.rivile.com/Shops/Rate",
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
                                            AppToastUtil.showInfoToast("تمت العملية بنجاح", AppToastUtil.LENGTH_LONG, getContext());
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

                            if (WarningMessage != null) AppToastUtil.showWarningToast(WarningMessage,
                                    AppToastUtil.LENGTH_LONG, getContext());
                        }) {
                            @Override
                            protected Map<String, String> getParams() {
                                HashMap<String, String> map = new HashMap<>();
                                map.put("Id", ShopId + "");
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

        next.setOnClickListener(v -> {
            Log.e("Item Num", item_num + "");
            if (products.size() == 80) {
                loadData(item_num, "1");
            } else {
                Snackbar.make(v, "نهايه المنتجات", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        last.setOnClickListener(v -> {
            if (page_num > 1) {
                Log.e("Item Num", item_num + "");
                loadData(item_num, "0");
            } else {
                Snackbar.make(v, "بدايه المنتجات", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
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
                response -> {
                    int temp = 0;
                    progressDialog.dismiss();
                    try {

                        JSONObject object = new JSONObject(response);
                        JSONArray array = object.getJSONArray("List");
                        if (array.length() > 0) {
                            final int size = products.size();
                            if (size > 0) {
                                products.subList(0, size).clear();
                                booksAdapter.notifyDataSetChanged();
                            }

                            for (int x1 = 0; x1 < array.length(); x1++) {
                                JSONObject object1 = array.getJSONObject(x1);
                                if (x1 == 0) {
                                    temp_first = object1.getInt("Id");
                                } else if (x1 == array.length() - 1) {
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

                            booksAdapter = new ContactAdapter(getActivity(), products, item -> {
                                //Todo: Make Some Action
                                Log.e("ProductId", item.getId() + "");
                            });
                            gridView.setAdapter(booksAdapter);

                            item_num = temp;

                            num.setText(page_num + "");
                        } else {
                            AppToastUtil.showInfoToast("لا توجد منتجات",
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

            if (WarningMessage != null) AppToastUtil.showWarningToast(WarningMessage,
                    AppToastUtil.LENGTH_LONG, getContext());
        }) {
            @Override
            protected Map<String, String> getParams() {
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
                response -> {
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

                            offerAdapter = new ContactAdapter(1, getActivity(), offerList, item -> {
                                //Todo: Make Some Action
                                Log.e("ProductId", item.getId() + "");
                            });
                            recyclerView_offer.setAdapter(offerAdapter);

                        } else {
                            AppToastUtil.showInfoToast("لا توجد عروض",
                                    AppToastUtil.LENGTH_LONG, getContext());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {
            progressDialog2.dismiss();

            String WarningMessage = null;
            if (error instanceof ServerError)
                WarningMessage = "خطأ فى الاتصال بالخادم";
            else if (error instanceof TimeoutError)
                WarningMessage = "خطأ فى مدة الاتصال";
            else if (error instanceof NetworkError)
                WarningMessage = "شبكه الانترنت ضعيفه حاليا";

            if (WarningMessage != null) AppToastUtil.showWarningToast(WarningMessage,
                    AppToastUtil.LENGTH_LONG, getContext());
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> map = new HashMap<>();
                map.put("ShopId", shopId + "");
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
