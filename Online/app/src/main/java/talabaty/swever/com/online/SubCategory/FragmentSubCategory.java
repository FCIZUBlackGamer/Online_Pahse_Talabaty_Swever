package talabaty.swever.com.online.SubCategory;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import pl.droidsonroids.gif.GifImageView;
import talabaty.swever.com.online.Fields.MostViewed.Contact;
import talabaty.swever.com.online.R;

public class FragmentSubCategory extends Fragment {

    static int ID = -1; // CategoryID
    FragmentManager fragmentManager;
    List<Contact> contact_List;
    ProgressDialog progressDialog;

    // Todo: use ID to push api
    public static FragmentSubCategory setId(int id) {
        FragmentSubCategory category = new FragmentSubCategory();
        ID = id;
        return category;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        View view = inflater.inflate(R.layout.fragment_load_product_category_contact, container, false);
        fragmentManager = getFragmentManager();
        contact_List = new ArrayList<>();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadSubCategory();

    }

    private void loadSubCategory() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("جارى تحميل البيانات ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://onlineapi.sweverteam.com/shops/ListByCategory",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        try {

                            JSONObject object = new JSONObject(response);
                            JSONArray array = object.getJSONArray("List");
                            if (array.length() > 0) {
                                //Todo: Fill Contact List From Api And Pass To FragmentContact()
                                for (int x = 0; x < array.length(); x++) {
                                    JSONObject object1 = array.getJSONObject(x);
                                    Contact contact = new Contact(object1.getInt("Id"),
                                            object1.getString("Name"),
                                            "http://selltlbaty.sweverteam.com/" + object1.getString("Photo")
                                    );
                                    contact_List.add(contact);
                                }

                                fragmentManager.beginTransaction()
                                        .replace(R.id.rec_contact, new FragmentContact().setList(contact_List)).commit();

                            }else {
                                Toast.makeText(getActivity(),"عذرا لا يوجد جهات عمل حاليا فى ذلك القسم",Toast.LENGTH_SHORT).show();
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
                map.put("CategoryId", ID + "");
                map.put("x", "0");
                map.put("count", "10000000");
                map.put("type", "1");
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
