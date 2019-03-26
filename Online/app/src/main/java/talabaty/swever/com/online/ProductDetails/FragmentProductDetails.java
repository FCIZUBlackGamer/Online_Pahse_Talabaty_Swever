package talabaty.swever.com.online.ProductDetails;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import talabaty.swever.com.online.Cart.CartDatabase;
import talabaty.swever.com.online.Cart.Sanf;
import talabaty.swever.com.online.R;
import talabaty.swever.com.online.Utils.AppToastUtil;

public class FragmentProductDetails extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<Sanf> sanfList;

    RecyclerView recyclerView_color;
    RecyclerView.Adapter colorAdapter;
    List<String> colorList;
    List<Integer> indexListcolor;
    String final_color = "";

    RecyclerView recyclerView_size;
    RecyclerView.Adapter sizeAdapter;
    List<String> sizeList;
    List<Integer> indexListsize;
    String final_size = "";
    String image, imageid;

    Button add;

    CartDatabase cartDatabase;

    TextView title, price;
    Spinner amount;
    RatingBar company_rate;
    View view;
    String contact_name, address;

    static int id, offer_type;
    String Link = null;

    ProgressDialog progressDialog, progressDialog2;
    Context context;
    int finalcolindex = 0, finalsizeindex = 0;
    int rate_num = 0;

    public FragmentProductDetails setId(int Id, int Offer_type) {
        FragmentProductDetails details = new FragmentProductDetails();
        id = Id;
        offer_type = Offer_type;
        return details;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        view = inflater.inflate(R.layout.fragment_montag_detail, container, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView = view.findViewById(R.id.rec);
        recyclerView_color = view.findViewById(R.id.rec_color);
        recyclerView_size = view.findViewById(R.id.rec_size);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView_color.setLayoutManager(layoutManager1);
        recyclerView_size.setLayoutManager(layoutManager2);


        add = view.findViewById(R.id.add);
        title = view.findViewById(R.id.title);
        price = view.findViewById(R.id.price);
        company_rate = view.findViewById(R.id.company_rate);
        amount = view.findViewById(R.id.spin_amount);


        sanfList = new ArrayList<>();
        colorList = new ArrayList<>();
        sizeList = new ArrayList<>();
        indexListcolor = indexListsize = new ArrayList<>();
        cartDatabase = new CartDatabase(getActivity());
        context = getContext();
        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onStart() {
        super.onStart();
        Log.e("Offer Type", offer_type + "");
        Log.e("Product Id", id + "");
        if (offer_type == 0) {
            Link = "http://onlineapi.rivile.com/Products/ShowMore";
        } else {
            Link = "http://onlineapi.rivile.com/Offers/OffersDetails";
        }
        loadDetails(id); /** Product Id*/ //http://onlineapi.rivile.com/Products/rate

        company_rate.setOnTouchListener((v, event) -> {
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
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://onlineapi.rivile.com/Products/rate",
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

                                            company_rate.setRating(rate_num);

                                        } else {
                                            AppToastUtil.showInfoToast("عذرا حدث خطأ أثناء اجراء العملية  .. يرجي المحاوله لاحقا",
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

        add.setOnClickListener(v -> {
            long res;

            res = cartDatabase.InsertData(title.getText().toString(),
                    image,
                    imageid,
                    finalcolindex + "",
                    finalsizeindex + "",
                    amount.getSelectedItem().toString(),
                    "ممتازة",
                    price.getText().toString().replace("LE", ""),
                    final_color + "",
                    "" + final_size,
                    contact_name,
                    address,
                    id + "",
                    "0");

            Log.e("finalcolindex", finalcolindex + "");
            Log.e("finalsizeindex", finalsizeindex + "");
            Log.e("final_color", final_color + "");
            Log.e("final_size", final_size + "");
            Log.e("ResultInserted", res + "");
            if (res >= 1) {
                Snackbar.make(v, "تم اضافة المنتج لعربة التسوق", Snackbar.LENGTH_LONG).show();
            } else {

            }
        });
    }

    private void loadDetails(final int id) {
        progressDialog2 = new ProgressDialog(getActivity());
        progressDialog2.setMessage("جارى تحميل البيانات ...");
        progressDialog2.setCancelable(false);
        progressDialog2.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Link,
                response -> {
                    progressDialog2.dismiss();
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONObject array = object.getJSONObject("details");
                        if (array.length() > 0) {
                            for (int x = 0; x < array.length(); x++) {

                                List<String> codeList = new ArrayList<>();
                                List<String> sourceList = new ArrayList<>();
                                List<String> sizeList = new ArrayList<>();

                                title.setText(array.getString("Name"));
                                company_rate.setRating((float) array.getDouble("Rate"));
                                price.setText(array.getDouble("Price") + " LE");

                                contact_name = array.getString("ShopName");
                                address = array.getString("Address");

                                /** Color List*/
                                indexListcolor = new ArrayList<>();
                                JSONArray color = array.getString("Color").equals("null") ? new JSONArray() : new JSONArray(array.getString("Color"));
                                if (color.length() > 0) {
                                    for (int i = 0; i < color.length(); i++) {
                                        JSONObject object2 = color.getJSONObject(i);
                                        codeList.add(object2.getString("Photo"));
                                        indexListcolor.add(object2.getInt("Id"));
//                                            Log.e("ColorId",object2.getInt("Id")+"");
                                    }
                                    loadColor(codeList, indexListcolor);
                                }
                                /** Size List*/
                                indexListsize = new ArrayList<>();
                                JSONArray size = array.getString("Size").equals("null") ? new JSONArray() : new JSONArray(array.getString("Size"));
                                if (size.length() > 0) {
                                    for (int i = 0; i < size.length(); i++) {
                                        JSONObject object2 = size.getJSONObject(i);
                                        sizeList.add(object2.getString("Photo"));
                                        indexListsize.add(object2.getInt("Id"));
//                                            Log.e("SizeId",object2.getInt("Id")+"");
                                    }
                                    loadSize(sizeList, indexListsize);
                                }
                                /** Image List*/
                                JSONArray image = new JSONArray(array.getString("Gallery"));
                                if (image.length() > 0) {
                                    Log.e("Images", image.length() + "");
                                    List<Integer> ids = new ArrayList<>();
                                    for (int i = 0; i < image.length(); i++) {
                                        JSONObject object2 = image.getJSONObject(i);
                                        ids.add(object2.getInt("Id"));
//                                            Log.e("ImageId",object2.getInt("Id")+"");
                                        sourceList.add("http://www.selltlbaty.rivile.com" + object2.getString("Photo"));
                                    }
                                    loadImages(sourceList, ids);
                                } else {
                                    List<Integer> ids = new ArrayList<>();
                                    ids.add(0);
                                    sourceList.add("https://vignette.wikia.nocookie.net/simpsons/images/6/60/No_Image_Available.png");

                                    loadImages(sourceList, ids);
                                }
                            }
                            incremwntView();
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

    private void incremwntView() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://onlineapi.rivile.com/Products/Visit",
                response -> Log.e("Response For Views", response),
                error -> {
                    String errorMessage = null;
                    if (error instanceof ServerError)
                        errorMessage = "خطأ إثناء الاتصال بالخادم";
                    else if (error instanceof NetworkError)
                        errorMessage = "خطأ فى شبكه الانترنت";
                    else if (error instanceof TimeoutError)
                        errorMessage = "خطأ فى مده الانتظار";

                    if (errorMessage != null) AppToastUtil.showErrorToast(errorMessage,
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
        Volley.newRequestQueue(context).add(stringRequest);

    }


    private void loadImages(List<String> list, List<Integer> ids) {

//        final int size = sanfList.size();
//        if (size > 0) {
//            for (int i = 0; i < size; i++) {
//                sanfList.remove(0);
//            }
//            adapter.notifyItemRangeRemoved(0, size);
//
//        }
        sanfList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            Sanf sanf = new Sanf();
            sanf.setId(ids.get(i));
            sanf.setImage(list.get(i));
            // Log.e("Image",sanf.getImage());
            sanfList.add(sanf);
        }

        image = list.get(0);
        imageid = ids.get(0) + "";
        adapter = new ProductDetailsAdapter(getActivity(), sanfList);
        recyclerView.setAdapter(adapter);

    }

    private void loadColor(List<String> list, final List<Integer> colorIndex) {

        final int size = colorList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                colorList.remove(0);
            }
            colorAdapter.notifyItemRangeRemoved(0, size);
        }

        colorList = list;
        final_color = colorList.get(0);

        colorAdapter = new ColorProductDetailsAdapter(colorList, item -> {
//                final_color = String.format("#%06X", (0xFFFFFF & Integer.parseInt(item)));
            final_color = item;
            for (int x = 0; x < colorList.size(); x++) {
//                    Log.e("color In List",colorList.get(x));
                if (colorList.get(x).equals(final_color)) {
                    finalcolindex = colorIndex.get(colorList.indexOf(final_color));
                    Log.e("color Index List", finalcolindex + "");
                    Log.e("final_color", final_color);
                }
            }

            Snackbar.make(view, "تم تحديد اللون " + final_color, Snackbar.LENGTH_LONG).show();
        });
        recyclerView_color.setAdapter(colorAdapter);
    }

    private void loadSize(List<String> list, final List<Integer> sizeIndex) {

        final int size = sizeList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                sizeList.remove(0);
            }
            sizeAdapter.notifyItemRangeRemoved(0, size);
        }

        sizeList = list;
        final_size = sizeList.get(0);

        sizeAdapter = new SizeProductDetailsAdapter(sizeList, item -> {
            final_size = item;
            for (int x = 0; x < sizeList.size(); x++) {
//                    Log.e("size In List",sizeList.get(x));
//                    Log.e("size Index ######",indexListsize.get(x)+"");
                if (sizeList.get(x).equals(final_size)) {
                    finalsizeindex = sizeIndex.get(sizeList.indexOf(final_size));
                    Log.e("size Index List", finalsizeindex + "");
                    Log.e("final_size", final_size);
                }
            }
            Snackbar.make(view, "تم تحديد الحجم " + final_size, Snackbar.LENGTH_LONG).show();
        });
        recyclerView_size.setAdapter(sizeAdapter);
    }
}
