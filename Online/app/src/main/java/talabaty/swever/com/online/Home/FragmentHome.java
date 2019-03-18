package talabaty.swever.com.online.Home;

import androidx.fragment.app.FragmentManager;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
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
import jp.wasabeef.recyclerview.animators.FadeInDownAnimator;
import jp.wasabeef.recyclerview.animators.FadeInUpAnimator;
import talabaty.swever.com.online.Contact.FragmentHomeContacts;
import talabaty.swever.com.online.Fields.MostTrend.Product;
import talabaty.swever.com.online.Fields.MostViewed.Contact;
import talabaty.swever.com.online.R;
import talabaty.swever.com.online.SubCategory.ContactAdapter;
import talabaty.swever.com.online.SubCategory.ProductAdapter;
import talabaty.swever.com.online.Utils.AppToastUtil;

public class FragmentHome extends Fragment {
    RecyclerView recyclerViewcontact, recyclerViewproducts, recyclerViewoffers;
    RecyclerView.Adapter adaptercontact, adapterproducts, adapteroffers;
    List<Contact> contactList;
    List<Product> productList, offerList;
    View view;
    LayoutInflater inflate;
    ViewGroup containe;
    ProgressDialog progressDialog, progressDialog1, progressDialog2;
    FrameLayout frameLayout;
    FragmentManager fragmentManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        inflate = inflater;
        containe = container;
        /** Set Ui View */
        setUi(inflate, containe);
        /** display products in rec view */
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewproducts = view.findViewById(R.id.rec_product);
        frameLayout = view.findViewById(R.id.frame_pro);
        recyclerViewproducts.setLayoutManager(layoutManager);
        recyclerViewproducts.setItemAnimator(new FadeInDownAnimator(new OvershootInterpolator(1f)));
        productList = new ArrayList<>();
        /** display contacts in rec view */
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewcontact = view.findViewById(R.id.rec_contact);
        recyclerViewcontact.setLayoutManager(layoutManager1);
        recyclerViewcontact.setItemAnimator(new FadeInUpAnimator(new OvershootInterpolator(1f)));
        contactList = new ArrayList<>();
        /** display offers in rec view */
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewoffers = view.findViewById(R.id.rec_offers);
        recyclerViewoffers.setLayoutManager(layoutManager2);
        recyclerViewoffers.setItemAnimator(new FadeInDownAnimator(new OvershootInterpolator(1f)));
        offerList = new ArrayList<>();

        fragmentManager = getFragmentManager();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        //Todo: get List Of Products
        loadProduct();

        //Todo: get List Of Contacts
        loadContact();

        //Todo: get List Of Offers
        loadOffers();

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
//            AppToastUtil.makeText(getContext(), "landscape", AppToastUtil.LENGTH_SHORT).show();
//
//        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
//            AppToastUtil.makeText(getContext(), "portrait", AppToastUtil.LENGTH_SHORT).show();
//        }
//        setUi(inflate, containe);
//    }

    private void loadProduct() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("جارى تحميل البيانات ...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://onlineapi.rivile.com/Home/ListProduct",
                response -> {
                    progressDialog.dismiss();
                    try {

                        JSONObject object = new JSONObject(response);
                        final JSONArray array = object.getJSONArray("List");
                        if (array.length() > 0) {

//                                final int size = productList.size();
//                                if (size > 0) {
//                                    for (int i = 0; i < size; i++) {
//                                        productList.remove(0);
//                                        adapterproducts.notifyItemRemoved(i);
//                                    }
//                                }
                            productList = new ArrayList<>();

                            for (int x = 0; x < array.length(); x++) {
                                JSONObject object1 = array.getJSONObject(x);

                                Product r = new Product(object1.getInt("Id"),
                                        object1.getString("Name"),
                                        "http://selltlbaty.rivile.com" + object1.getString("Photo"),
                                        (float) object1.getDouble("Price"),
                                        (float) object1.getDouble("Sale"),
                                        (float) object1.getDouble("Rate")
                                );
                                r.setIsOffer(0);
                                productList.add(r);

                            }

                            adapterproducts = new ProductAdapter(getActivity(), productList);
                            AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapterproducts);
                            alphaAdapter.setDuration(3000);
                            recyclerViewproducts.setAdapter(adapterproducts);

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

            if (WarningMessage != null) AppToastUtil.showWarningToast(WarningMessage,
                    AppToastUtil.LENGTH_LONG, getContext());
        }) {
            @Override
            protected Map<String, String> getParams() {
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

    private void loadOffers() {
        progressDialog1 = new ProgressDialog(getActivity());
        progressDialog1.setMessage("جارى تحميل البيانات ...");
        progressDialog1.setCancelable(false);
        progressDialog1.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://onlineapi.rivile.com/Offers/List",
                response -> {
                    progressDialog1.dismiss();
                    try {

                        JSONObject object = new JSONObject(response);
                        final JSONArray array = object.getJSONArray("Offers");
                        if (array.length() > 0) {

//                                final int size = offerList.size();
//                                if (size > 0) {
//                                    for (int i = 0; i < size; i++) {
//                                        offerList.remove(0);
//                                        adapteroffers.notifyItemRemoved(i);
//                                    }
//                                }
                            productList = new ArrayList<>();

                            for (int x = 0; x < array.length(); x++) {
                                JSONObject object1 = array.getJSONObject(x);

                                Product r = new Product(object1.getInt("Id"),
                                        object1.getString("Name"),
                                        "http://selltlbaty.rivile.com" + object1.getString("Photo"),
                                        (float) object1.getDouble("Price"),
                                        0,
                                        (float) object1.getDouble("Rate")
                                );
                                r.setIsOffer(1);
                                offerList.add(r);

                            }

                            adapteroffers = new ProductAdapter(1, getActivity(), offerList);
                            AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapteroffers);
                            alphaAdapter.setDuration(3000);
                            recyclerViewoffers.setAdapter(adapteroffers);

                        } else {
                            AppToastUtil.showInfoToast("لا توجد بيانات",
                                    AppToastUtil.LENGTH_LONG, getContext());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            progressDialog1.dismiss();

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

    @Override
    public void onPause() {
        super.onPause();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        if (progressDialog1 != null && progressDialog1.isShowing()) {
            progressDialog1.dismiss();
        }
        if (progressDialog2 != null && progressDialog2.isShowing()) {
            progressDialog2.dismiss();
        }
    }

    private void loadContact() {
        progressDialog2 = new ProgressDialog(getActivity());
        progressDialog2.setMessage("جارى تحميل البيانات ...");
        progressDialog2.setCancelable(false);
        progressDialog2.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://onlineapi.rivile.com/Home/ShopList",
                response -> {
                    progressDialog2.dismiss();
                    try {

                        JSONObject object = new JSONObject(response);
                        JSONArray array = object.getJSONArray("List");
                        if (array.length() > 0) {
//                                final int size = contactList.size();
//                                if (size > 0) {
//                                    for (int i = 0; i < size; i++) {
//                                        contactList.remove(0);
//                                    }
//                                    adaptercontact.notifyDataSetChanged();
//                                }
                            productList = new ArrayList<>();

                            for (int x = 0; x < array.length(); x++) {
                                JSONObject object1 = array.getJSONObject(x);

                                Contact info = new Contact(
                                        object1.getInt("Id"),
                                        object1.getString("Name"),
                                        (float) object1.getDouble("Rate"),
                                        object1.getString("Name"),
                                        object1.getString("Address"),
                                        object1.getString("Phone"),
                                        "http://selltlbaty.rivile.com" + object1.getString("Photo"),
                                        ""
                                );
                                contactList.add(info);

                            }

                            adaptercontact = new ContactAdapter(getActivity(), contactList, frameLayout, item -> fragmentManager.beginTransaction()
                                    .replace(R.id.frame_home, new FragmentHomeContacts().setData(item.getId(), item.getPhone(), item.getEmail(),
                                            item.getLocation(), item.getName(), item.getCompany_logo(),
                                            item.getRate())).addToBackStack("FragmentHomeContacts").commit());
                            AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adaptercontact);
                            alphaAdapter.setDuration(3000);
                            recyclerViewcontact.setAdapter(adaptercontact);

                        } else {
                            AppToastUtil.showInfoToast("لا توجد بيانات",
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
}
