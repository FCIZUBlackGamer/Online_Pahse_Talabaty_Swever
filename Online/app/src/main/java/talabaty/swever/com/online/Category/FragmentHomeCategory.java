package talabaty.swever.com.online.Category;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import talabaty.swever.com.online.Fields.MostTrend.MontagAdapter;
import talabaty.swever.com.online.Fields.MostTrend.Product;
import talabaty.swever.com.online.R;
import talabaty.swever.com.online.Utils.AppToastUtil;

public class FragmentHomeCategory extends Fragment {

    GridView gridView;
    MontagAdapter booksAdapter;
    List<Product> products;
    int temp_first, temp_last;
    TextView next, num, last;
    int item_num, page_num;
    static String phon, emai, addres, nam, log;
    static float ba;

    TextView phone, email, address, name;
    RatingBar bar;
    ImageView logo;
    ProgressDialog progressDialog;

    public static FragmentHomeCategory setData(String phone, String email, String address, String name, String logo, float bar) {
        FragmentHomeCategory fragmentHomeContacts = new FragmentHomeCategory();
        phon = phone;
        emai = email;
        addres = address;
        nam = name;
        log = logo;
        ba = bar;
        return fragmentHomeContacts;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        View view = inflater.inflate(R.layout.fragment_contacts_home, container, false);
        gridView = (GridView) view.findViewById(R.id.gridview);

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

    @Override
    public void onStart() {
        super.onStart();

        /** Basic Info*/
//        phone.setText(phon);
//        name.setText(nam);
//        email.setText(emai);
//        address.setText(addres);
//        bar.setRating(ba);
//        if (!log.isEmpty()) {
//            Picasso.get().load(log).into(logo);
//        }

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

        loadData(0, "1");

        /** Adapter Montag*/
//        for (int x=0; x<20; x++){
//            Product r = new Product(0, "IPhone", "",12000, 20,3);
//            products[x] = r;
//        }
//        booksAdapter = new MontagAdapter(getActivity(), products);
//        gridView.setAdapter(booksAdapter);
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Product book = products[position];
//                //Todo: Make Some Action
//                book.getId();
//            }
//        });

    }

    @Override
    public void onPause() {
        super.onPause();
        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }

    }

    private void loadData(final int x, final String type) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("جارى تحميل البيانات ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://onlineapi.rivile.com/Products/MostVisited?type=" + type + "&x=" + x + "&count=80",
                response -> {
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

                                if (r.equals("1")) {
                                    page_num++;
                                } else if (r.equals("0")) {
                                    page_num--;
                                }

                            }
                            booksAdapter = new MontagAdapter(getActivity(), products);
                            gridView.setAdapter(booksAdapter);

                            Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.fly_in_from_center);
                            gridView.setAnimation(anim);
                            anim.start();

                            item_num = temp;
                            gridView.setOnItemClickListener((parent, view, position, id) -> {
                                Product book = products.get(position);
                                //Todo: Make Some Action
                                book.getId();
                            });
                            num.setText(page_num + "");
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
                });
//        Volley.newRequestQueue(getActivity()).add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                2,  // maxNumRetries = 2 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }
}
