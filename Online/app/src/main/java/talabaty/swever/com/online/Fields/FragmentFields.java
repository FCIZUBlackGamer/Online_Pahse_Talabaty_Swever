package talabaty.swever.com.online.Fields;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import talabaty.swever.com.online.R;
import talabaty.swever.com.online.SubCategory.FragmentSubCategory;
import talabaty.swever.com.online.SwitchNav;
import talabaty.swever.com.online.Utils.AppToastUtil;

public class FragmentFields extends Fragment {

    //    private TabLayout tabLayout;
//    ViewPager viewPager = null;
    FragmentManager fragmentManager;
    RecyclerView recyclerView_cat;
    RecyclerView.Adapter catAdapter;
    List<Category> categoryList;
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
//        viewPager = (ViewPager) view.findViewById(R.id.pager);
        fragmentManager = getFragmentManager();
        recyclerView_cat = view.findViewById(R.id.cat_rec);
        recyclerView_cat.setHasFixedSize(true);
        recyclerView_cat.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, true));

//        viewPager.setAdapter(new pager(fragmentManager));
//        tabLayout = (TabLayout) view.findViewById(R.id.tablayout);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((SwitchNav) getActivity())
                .setActionBarTitle("مجالات ");
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition(), false);
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                tabLayout.getTabAt(position).select();
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });

        /** load category form API */
        loadCategory();
    }

    private void loadCategory() {
        categoryList = new ArrayList<>();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("جارى تحميل المجالات ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://onlineapi.rivile.com/Fields/List",
                response -> {
                    progressDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONArray array = object.getJSONArray("Fields");
                        if (array.length() > 0) {
                            for (int x = 0; x < array.length(); x++) {
                                JSONObject object1 = array.getJSONObject(x);
                                Category categoryModel = new Category(object1.getInt("Id"),
                                        object1.getString("Name"),
                                        "http://selltlbaty.rivile.com" + object1.getString("Photo")
                                );
                                categoryList.add(categoryModel);
                            }

                            catAdapter = new CategoryAdapter(getActivity(), categoryList, item -> fragmentManager.beginTransaction()
                                    .replace(R.id.frame_home, new FragmentSubCategory().setId(item.getId())).addToBackStack("FragmentSubCategory").commit());
                            recyclerView_cat.setAdapter(catAdapter);

                        } else {
                            AppToastUtil.showWarningToast("لا توجد اقسام حاليا",
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
                map.put("token", "?za[ZbGNz2B}MXYZ");
                return map;
            }
        };
//        Volley.newRequestQueue(getActivity()).add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                3,  // maxNumRetries = 2 means no retry
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

//    class pager extends FragmentPagerAdapter {
//
//        public pager(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            Fragment fragment = null;
//            if (position == 0) {
//                fragment = new FragmentMostTrend().setType("latest");
//            } else if (position == 1) {
//                fragment = new FragmentMostViewed().setType("trend");
//            } else if (position == 2) {
//                fragment = new FragmentMostTrend().setType("trend");
//            }
//            return fragment;
//        }
//
//        @Override
//        public int getCount() {
//            return 3;
//        }
//    }
}
