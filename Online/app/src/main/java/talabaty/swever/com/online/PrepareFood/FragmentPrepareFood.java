package talabaty.swever.com.online.PrepareFood;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.GridView;

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

import talabaty.swever.com.online.Cart.CartAdditionalDatabase;
import talabaty.swever.com.online.Cart.CartDatabase;
import talabaty.swever.com.online.Cart.Sanf;
import talabaty.swever.com.online.R;
import talabaty.swever.com.online.Utils.AppToastUtil;

public class FragmentPrepareFood extends Fragment {

    View view;
    GridView gridView;
    PrepareFoodAdapter prepareFoodAdapter;
    List<Sanf> sanfList;
    static Sanf sanf = null;
    FragmentManager fragmentManager;
    static int shopId;
    ProgressDialog progressDialog;
    int amount = 0;
    CartDatabase cartDatabase;
    CartAdditionalDatabase cartAdditionalDatabase;

    public static FragmentPrepareFood setData(int shopI) {
        FragmentPrepareFood fragmentPrepareFood = new FragmentPrepareFood();
        shopId = shopI;
        return fragmentPrepareFood;
    }

    public static FragmentPrepareFood setAdditions(Sanf s) {
        FragmentPrepareFood fragmentPrepareFood = new FragmentPrepareFood();
        sanf = s;
        Gson gson = new Gson();
        Log.e("gson", gson.toJson(s.getAdditionList()));
        return fragmentPrepareFood;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_preapare_new_food, container, false);
        gridView = (GridView) view.findViewById(R.id.gridview);
        fragmentManager = getFragmentManager();
        sanfList = new ArrayList<>();
        cartDatabase = new CartDatabase(getActivity());
        cartAdditionalDatabase = new CartAdditionalDatabase(getActivity());
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();


        loadFood(shopId + "");

        if (sanf != null) {
            // Add sanf item To Cart
            long res;
            //String[] real_price = price.getText().toString().split(" ");

            res = cartDatabase.InsertData(sanf.getName() + "",
                    sanf.getImage() + "", "", "", "", sanf.getAmount() + "",
                    "ممتازة", sanf.getPrice() + "", "", "", "",/* in this case we assign address to state*/ sanf.getState() + "", sanf.getId() + "", "2");

            Log.e("Address", sanf.getState());
            //int temp_id = sanf.getId();
            Log.e("InsertedID", sanf.getId() + "");
            Log.e("InsertedRes", res + "");
            //temp_id = sanf.getId();
            boolean s;
            s = cartAdditionalDatabase.InsertData(sanf.getAdditionList().get(0).getName(),
                    String.valueOf(sanf.getAdditionList().get(0).getPrice()),
                    String.valueOf(sanf.getAdditionList().get(0).getId()),
                    String.valueOf(sanf.getId())
            );
//            Cursor cursor = cartAdditionalDatabase.ShowData(sanf.getId()+"");
//            int x = 0;
//            while (cursor.moveToNext()){
//                Log.e("cartAdditionalDatabase"+x,cursor.getString(0));
//                Log.e("cartAdditionalDatabase"+x,cursor.getString(1));
//                Log.e("cartAdditionalDatabase"+x,cursor.getString(2));
//                Log.e("cartAdditionalDatabase"+x,cursor.getString(3));
//                Log.e("cartAdditionalDatabase"+x,cursor.getString(4));
//                x++;
//            }
//            Log.e("X=",x+"");
//            Gson gson = new Gson();
//            Log.e("InsertToDataBase", gson.toJson(cursor) + "");

            if (res == 1) {
                Snackbar.make(view, "تم اضافة المنتج لعربة التسوق", Snackbar.LENGTH_LONG).show();
            } else {

            }
        }

    }

//    private int amount() {
//        amount = 0;
//        final EditText amount_num = new EditText(getActivity());
//        amount_num.setHint("ادخل عدد القطع");
//        amount_num.setInputType(InputType.TYPE_CLASS_NUMBER);
//        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setView(amount_num).setCancelable(false)
//                .setPositiveButton("التالى", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        amount = (int) Float.parseFloat(amount_num.getText().toString());
//                        ViewGroup parent = (ViewGroup) amount_num.getParent();
//                        if (parent != null) {
//                            parent.removeAllViews();
//                        }
//
//                    }
//                })
//                .setNegativeButton("إلغاء", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        ViewGroup parent = (ViewGroup) amount_num.getParent();
//                        if (parent != null) {
//                            parent.removeAllViews();
//                        }
//                        dialog.dismiss();
//                    }
//                }).show();
//        return amount;
//    }

    private void loadFood(final String shopId) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("جارى تحميل البيانات ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://onlineapi.rivile.com/BeTheChef/BaseFoods",
                response -> {
                    progressDialog.dismiss();
                    try {

                        JSONObject object = new JSONObject(response);
                        JSONArray array = object.getJSONArray("List");
                        if (array.length() > 0) {

                            final int size = sanfList.size();
                            if (size > 0) {
                                for (int i = 0; i < size; i++) {
                                    sanfList.remove(0);

                                }
                                prepareFoodAdapter.notifyDataSetChanged();
                            }
                            try {

                                for (int x = 0; x < array.length(); x++) {
                                    JSONObject object1 = array.getJSONObject(x);

                                    Sanf food = new Sanf(object1.getInt("Id"),
                                            object1.getString("Name"),
                                            "http://www.selltlbaty.rivile.com" + object1.getString("Photo"),
                                            (float) object1.getDouble("Price")
                                    );
                                    food.setIsOffer(2);
                                    food.setState(object1.getString("Address"));
                                    sanfList.add(food);
                                }

                                prepareFoodAdapter = new PrepareFoodAdapter(getActivity(), sanfList);
                                gridView.setAdapter(prepareFoodAdapter);
                                Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.fly_in_from_center);
                                gridView.setAnimation(anim);
                                anim.start();
                                gridView.setOnItemClickListener((parent, view, position, id) -> {
                                    final Sanf book = sanfList.get(position);
                                    amount = 0;
                                    final EditText amount_num = new EditText(getActivity());
                                    amount_num.setHint("ادخل عدد القطع");
                                    amount_num.setInputType(InputType.TYPE_CLASS_NUMBER);
                                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setView(amount_num).setCancelable(false)
                                            .setPositiveButton("التالى", (dialog, which) -> {
                                                amount = (int) Float.parseFloat(amount_num.getText().toString());
                                                book.setAmount(amount);
                                                Log.e("ShopId", shopId);
                                                fragmentManager.beginTransaction().replace(R.id.frame_home, new FragmentAdditions().setAdditions(book, Integer.parseInt(shopId))).addToBackStack("FragmentAdditions").commit();
                                                ViewGroup parent12 = (ViewGroup) amount_num.getParent();
                                                if (parent12 != null) {
                                                    parent12.removeAllViews();
                                                }

                                            })
                                            .setNegativeButton("إلغاء", (dialog, which) -> {
                                                ViewGroup parent1 = (ViewGroup) amount_num.getParent();
                                                if (parent1 != null) {
                                                    parent1.removeAllViews();
                                                }
                                                dialog.dismiss();
                                            }).show();

                                });

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
            protected Map<String, String> getParams() throws AuthFailureError {
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
    }
}
