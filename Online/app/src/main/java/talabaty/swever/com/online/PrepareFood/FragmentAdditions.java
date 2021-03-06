package talabaty.swever.com.online.PrepareFood;

import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
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

import jp.wasabeef.recyclerview.adapters.SlideInBottomAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInDownAnimator;
import talabaty.swever.com.online.Cart.AdditionalModel;
import talabaty.swever.com.online.Cart.Sanf;
import talabaty.swever.com.online.R;
import talabaty.swever.com.online.Utils.AppToastUtil;

public class FragmentAdditions extends Fragment {

    View view;
    Button next, previous;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    static Sanf sanf;
    List<AdditionalModel> AdditionList, modelList;
    FragmentManager fragmentManager;
    static int ShopId;
    ProgressDialog progressDialog;
    TextView total;

    public static FragmentAdditions setAdditions(Sanf san, int shopI) {
        FragmentAdditions additions = new FragmentAdditions();
        sanf = san;
        ShopId = shopI;
        Log.e("ShopId", ShopId + "");
        return additions;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_additions, container, false);
        next = view.findViewById(R.id.next);
        previous = view.findViewById(R.id.previous);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerView = view.findViewById(R.id.rec_food);
        total = view.findViewById(R.id.total);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new FadeInDownAnimator(new OvershootInterpolator(1f)));
        fragmentManager = getFragmentManager();
        modelList = AdditionList = new ArrayList<>();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        loadFood(ShopId+"");
        total.setText(String.valueOf(sanf.getAmount() * sanf.getPrice()));

        previous.setOnClickListener(v -> fragmentManager.beginTransaction()
                .replace(R.id.frame_home, new FragmentPrepareFood().setData(ShopId)).commit());

        next.setOnClickListener(v -> {
            sanf.setAdditionList(modelList);
            Gson gson = new Gson();
            Log.e("ID", sanf.getId()+"");
            Log.e("GSON", gson.toJson(sanf));
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_home, new FragmentPrepareFood().setAdditions(sanf)).addToBackStack("FragmentPrepareFood").commit();
        });
    }


    @Override
    public void onPause() {
        super.onPause();
        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }

    }

    private void loadFood(final String shopId) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("جارى تحميل البيانات ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://onlineapi.rivile.com/BeTheChef/Addations",
                response -> {

                    try {

                        JSONObject object = new JSONObject(response);
                        JSONArray array = object.getJSONArray("List");
                        if (array.length() > 0) {

                            final int size = AdditionList.size();
                            if (size > 0) {
                                for (int i = 0; i < size; i++) {
                                    AdditionList.remove(0);

                                }
                                adapter.notifyDataSetChanged();
                            }
                            try {

                                for (int x = 0; x < array.length(); x++) {
                                    JSONObject object1 = array.getJSONObject(x);

                                    AdditionalModel food = new AdditionalModel(object1.getInt("Id"),
                                            object1.getString("Name"),
                                            object1.getDouble("Price")
                                    );
                                    AdditionList.add(food);

                                }

                                SlideInBottomAnimationAdapter alphaAdapter = new SlideInBottomAnimationAdapter(adapter);
                                alphaAdapter.setDuration(3000);
                                modelList = new ArrayList<>();
                                adapter = new AdditionsAdapter(getActivity(), AdditionList, item -> {
                                    // get item and add to list
                                    Log.e("Item", item.getChecked()+"");
                                    if (!item.getChecked()) {

                                        item.setChecked(true);

                                        adapter.notifyDataSetChanged();
                                        modelList.add(item);
                                        total.setText(String.valueOf(Float.parseFloat(total.getText().toString()) + (sanf.getAmount() *  item.getPrice())));

                                    } else {

                                        item.setChecked(false);

                                        adapter.notifyDataSetChanged();
                                        modelList.remove(item);
                                        total.setText(String.valueOf(Float.parseFloat(total.getText().toString()) - (sanf.getAmount() *  item.getPrice())));
                                    }
                                });

                                recyclerView.setAdapter(adapter);
                            } catch (Exception e) {

                            }
                        } else {
                            AppToastUtil.showInfoToast("لا توجد بيانات",
                                    AppToastUtil.LENGTH_LONG, getContext());
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {

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
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("ShopId", shopId + "");
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
