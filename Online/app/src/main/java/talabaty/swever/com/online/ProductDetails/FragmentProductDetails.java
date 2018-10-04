package talabaty.swever.com.online.ProductDetails;

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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
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

import talabaty.swever.com.online.Chart.ChartDatabase;
import talabaty.swever.com.online.Chart.Sanf;
import talabaty.swever.com.online.R;

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

    ChartDatabase chartDatabase;

    TextView title, price;
    Spinner amount;
    RatingBar company_rate;
    View view;
    String contact_name, address;

    static int id, offer_type;
    String Link = null;

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
        recyclerView = (RecyclerView) view.findViewById(R.id.rec);
        recyclerView_color = (RecyclerView) view.findViewById(R.id.rec_color);
        recyclerView_size = (RecyclerView) view.findViewById(R.id.rec_size);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView_color.setLayoutManager(layoutManager1);
        recyclerView_size.setLayoutManager(layoutManager2);


        add = (Button) view.findViewById(R.id.add);
        title = (TextView) view.findViewById(R.id.title);
        price = (TextView) view.findViewById(R.id.price);
        company_rate = (RatingBar) view.findViewById(R.id.company_rate);
        amount = (Spinner) view.findViewById(R.id.spin_amount);


        sanfList = new ArrayList<>();
        colorList = new ArrayList<>();
        sizeList = new ArrayList<>();
        indexListcolor = indexListsize = new ArrayList<>();
        chartDatabase = new ChartDatabase(getActivity());
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (offer_type == 0)
        {
            Link = "http://onlineapi.sweverteam.com/Products/ShowMore";
        }else {
            Link = "http://onlineapi.sweverteam.com/Offers/OffersDetails";
        }
        loadDetails(id); /** Product Id*/

        company_rate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, final float rating, boolean fromUser) {
                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setCancelable(false);
                progressDialog.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://onlineapi.sweverteam.com/Products/rate",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                progressDialog.dismiss();
                                if (response.equals("\"Success\"")) {
                                    Toast toast = Toast.makeText(getActivity(), "تمت العملية بنجاح", Toast.LENGTH_SHORT);
                                    TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                                    v.setTextColor(Color.GREEN);
                                    toast.show();
                                } else {
                                    Toast toast = Toast.makeText(getActivity(), "عذرا حدث خطأ أثناء اجراء العملية  .. يرجي المحاوله لاحقا", Toast.LENGTH_SHORT);
                                    TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                                    v.setTextColor(Color.RED);
                                    toast.show();
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
                        map.put("Id", id + "");
                        map.put("vote", (int) rating + "");
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
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean res;
                int finalcolindex = 0, finalsizeindex = 0;

                for (int x = 0; x < colorList.size(); x++) {
                    Log.e("color In List",colorList.get(x));
                    if (colorList.get(x).equals(final_color)) {
                        finalcolindex = indexListcolor.get(colorList.indexOf(final_color));
                        Log.e("color Index List",finalcolindex+"");
                    }
                }
                Log.e("color",final_color);
                for (int x = 0; x < sizeList.size(); x++) {
                    Log.e("size In List",sizeList.get(x));
                    if (sizeList.get(x).equals(final_size)) {
                        finalsizeindex = indexListsize.get(sizeList.indexOf(final_size));
                        Log.e("size Index List",finalsizeindex+"");
                    }
                }
                Log.e("size",final_size);
                res = chartDatabase.InsertData(title.getText().toString(),
                        image,imageid, finalcolindex + "", finalsizeindex + "", amount.getSelectedItem().toString(),
                        "ممتازة", price.getText().toString(), final_color, final_size, contact_name,address,id+"");

                if (res) {
                    Snackbar.make(v, "تم اضافة المنتج لعربة التسوق", Snackbar.LENGTH_LONG).show();
                } else {

                }
            }
        });
    }

    private void loadDetails(final int id) {

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("جارى تحميل البيانات ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Link,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
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
                                    if (offer_type == 1)
                                    {

                                    }else {
                                        contact_name = array.getString("ShopName");
                                        address = array.getString("Address");
                                    }


                                    /** Color List*/
                                    JSONArray color = new JSONArray(array.getString("Color"));
                                    if (color.length() > 0) {
                                        for (int i = 0; i < color.length(); i++) {
                                            JSONObject object2 = color.getJSONObject(i);
                                            codeList.add(object2.getString("Photo"));
                                            indexListcolor.add(object2.getInt("Id"));
                                        }
                                        loadColor(codeList);
                                    }
                                    /** Size List*/
                                    JSONArray size = new JSONArray(array.getString("Size"));
                                    if (size.length() > 0) {
                                        for (int i = 0; i < size.length(); i++) {
                                            JSONObject object2 = size.getJSONObject(i);
                                            sizeList.add(object2.getString("Photo"));
                                            indexListsize.add(object2.getInt("Id"));
                                        }
                                        loadSize(sizeList);
                                    }
                                    /** Image List*/
                                    JSONArray image = new JSONArray(array.getString("Gallery"));
                                    if (image.length() > 0) {
                                        List<Integer> ids = new ArrayList<>();
                                        for (int i = 0; i < image.length(); i++) {
                                            JSONObject object2 = image.getJSONObject(i);
                                            ids.add(object2.getInt("Id"));
                                            sourceList.add(object2.getString("Photo"));
                                        }
                                        loadImages(sourceList, ids);
                                    }else {
                                        List<Integer> ids = new ArrayList<>();
                                            ids.add(0);
                                            sourceList.add("https://vignette.wikia.nocookie.net/simpsons/images/6/60/No_Image_Available.png");

                                        loadImages(sourceList, ids);
                                    }

                                }
                                incremwntView();
                            } else {
                                Toast toast = Toast.makeText(getActivity(), "لا توجد تفاصيل للمنتج", Toast.LENGTH_SHORT);
                                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                                v.setTextColor(Color.RED);
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

    private void incremwntView() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://onlineapi.sweverteam.com/Products/Visit",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response For Views", response);
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

    private void loadImages(List<String> list, List<Integer> ids) {

        final int size = sanfList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                sanfList.remove(0);
            }
            adapter.notifyItemRangeRemoved(0, size);

        }

        for (int i = 0; i < list.size(); i++) {
            Sanf sanf = new Sanf();
            sanf.setId(ids.get(i));
            sanf.setImage(list.get(i));
            // Log.e("Image",sanf.getImage());
            sanfList.add(sanf);
        }

        image = list.get(0);
        imageid = ids.get(0)+"";
        adapter = new ProductDetailsAdapter(getActivity(), sanfList);
        recyclerView.setAdapter(adapter);

    }

    private void loadColor(List<String> list) {

        final int size = colorList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                colorList.remove(0);
            }
            colorAdapter.notifyItemRangeRemoved(0, size);

        }

        colorList = list;
        final_color = colorList.get(0);

        colorAdapter = new ColorProductDetailsAdapter(colorList, new ColorProductDetailsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String item) {
//                final_color = String.format("#%06X", (0xFFFFFF & Integer.parseInt(item)));
                final_color = item;
                Snackbar.make(view, "تم تحديد اللون " + final_color, Snackbar.LENGTH_LONG).show();
            }
        });
        recyclerView_color.setAdapter(colorAdapter);

    }

    private void loadSize(List<String> list) {

        final int size = sizeList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                sizeList.remove(0);
            }
            sizeAdapter.notifyItemRangeRemoved(0, size);

        }

        sizeList = list;
        final_size = sizeList.get(0);

        sizeAdapter = new SizeProductDetailsAdapter(sizeList, new SizeProductDetailsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String item) {
                final_size = item;
                Snackbar.make(view, "تم تحديد الحجم " + final_size, Snackbar.LENGTH_LONG).show();
            }
        });
        recyclerView_size.setAdapter(sizeAdapter);

    }
}
