package talabaty.swever.com.online;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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

import de.hdodenhof.circleimageview.CircleImageView;
import talabaty.swever.com.online.Cart.HomeCartActivity;
import talabaty.swever.com.online.ContactUs.FragmentContactUs;
import talabaty.swever.com.online.Fields.FragmentFields;
import talabaty.swever.com.online.Fields.FragmentMotageHome;
import talabaty.swever.com.online.Fields.MostViewed.FragmentMostViewed;
import talabaty.swever.com.online.Home.FragmentHome;
import talabaty.swever.com.online.NearestContacts.ContactInfo;
import talabaty.swever.com.online.Utils.AppToastUtil;
import talabaty.swever.com.online.WorkWithUs.FragmentWorkWithUs;

public class SwitchNav extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager fragmentManager;
    NavigationView navigationView;
    List<CategoryModel> categoryModelList;
    List<ContactInfo> contactInfos;
    Bundle bundle;

    CircleImageView imageView;
    TextView user_name;
    LoginDatabase loginDatabase;
    Cursor cursor;

    ProgressDialog progressDialog;
    String AccountType;
    View parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = savedInstanceState;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        parentLayout = findViewById(android.R.id.content);
        setUi();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loginDatabase = new LoginDatabase(this);
        cursor = loginDatabase.ShowData();

        fragmentManager = getSupportFragmentManager();
        categoryModelList = new ArrayList<>();
        contactInfos = new ArrayList<>();
        final Fragment fragment = new FragmentHome();
        fragmentManager.beginTransaction()
                .add(R.id.frame_home, fragment).commit();
        setActionBarTitle("الرئيسيه");
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

//                fragmentManager.beginTransaction()
//                        .replace(R.id.frame_home,new HomeCartActivity()).addToBackStack("HomeCartActivity").commit();
                startActivity(new Intent(SwitchNav.this, HomeCartActivity.class));

            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
/** make the application rtl */
        getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View view = navigationView.getHeaderView(0);
        TextView email, job;
        imageView = view.findViewById(R.id.imageView);
        email = view.findViewById(R.id.textView);
        job = view.findViewById(R.id.job);
        user_name = view.findViewById(R.id.name);
        /** Load Navigator Date From SQLITE */
        while (cursor.moveToNext()) {
            user_name.setText(cursor.getString(1));
            email.setText(cursor.getString(7));
            Log.e("UserId", cursor.getString(2));
            AccountType = cursor.getString(6);
            if (AccountType.equals("2")) {
                job.setText("مدير");
                job.setVisibility(View.VISIBLE);
            } else {
                job.setVisibility(View.GONE);
            }
            if (!cursor.getString(5).isEmpty()) {
                Picasso.get().load(cursor.getString(5))
                        .into(imageView);
            }
        }
//        loadCategory();
        //loadContacts();

    }


    public void setUi() {
        setContentView(R.layout.activity_switch_nav);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        ((DrawerLayout) findViewById(R.id.drawer_layout)).removeAllViews();
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            AppToastUtil.showInfoToast("landscape",
                    AppToastUtil.LENGTH_SHORT, this);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            AppToastUtil.showInfoToast("portrait", AppToastUtil.LENGTH_SHORT, this);
        }
        setUi();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.switch_nav, menu);
////        for (int i=0;i<menu.size();i++) {
////            MenuItem mi = menu.getItem(i);
////
//////            //for aapplying a font to subMenu ...
//////            SubMenu subMenu = mi.getSubMenu();
//////            if (subMenu!=null && subMenu.size() >0 ) {
//////                for (int j=0; j <subMenu.size();j++) {
//////                    MenuItem subMenuItem = subMenu.getItem(j);
//////                    applyFontToMenuItem(subMenuItem);
//////                }
//////            }
////
////            //the method we have create in activity
////            applyFontToMenuItem(mi);
////        }
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Fields/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            /** home page */
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_home, new FragmentHome()).addToBackStack("FragmentHome").commit();
        }
//        else if (id == R.id.nav_new_offers) {
//            fragmentManager.beginTransaction()
//                    .replace(R.id.frame_home,new FragmentMostTrend().setType("offers")).addToBackStack("FragmentMostTrend").commit();
//        }
        else if (id == R.id.nav_new_food) {
            /** make new food جهز وجبتى */
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_home, new FragmentMostViewed().setType("prepare_food")).addToBackStack("FragmentMostViewed").commit();
        } else if (id == R.id.nav_nearest) {
            /** الاقرب*/
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_home, new FragmentMostViewed().setType("nearest")).addToBackStack("FragmentMostViewed").commit();
        } else if (id == R.id.nav_montag) {
            /** Montage page */
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_home, new FragmentMotageHome()).addToBackStack("FragmentMotageHome").commit();
        } else if (id == R.id.nav_category) {
            /** Category page */
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_home, new FragmentFields()).addToBackStack("FragmentFields").commit();
        } else if (id == R.id.nav_contact) {
            /** جهات العمل */
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_home, new FragmentMostViewed().setType("normal")).addToBackStack("FragmentMostViewed").commit();
        } else if (id == R.id.nav_work_with_us) {
            /**  اشترك معنا 0 يعني مسجلش قبل كده 1 يعني هو مسجل بالفعل ومينفعش يسجل تانى  */
            if (AccountType.equals("0")) {
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_home, new FragmentWorkWithUs()).addToBackStack("FragmentWorkWithUs").commit();
            } else {
                Snackbar.make(parentLayout, "لا يمكن انشاء حساب اخر", Snackbar.LENGTH_LONG)
                        .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                        .show();
            }
        } else if (id == R.id.nav_call_us) {
            /** تواصل معنا */
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_home, new FragmentContactUs()).addToBackStack("FragmentContactUs").commit();
        } else if (id == R.id.nav_car_shop) {
//            fragmentManager.beginTransaction()
//                    .replace(R.id.frame_home,new HomeCartActivity()).addToBackStack("HomeCartActivity").commit();
            startActivity(new Intent(SwitchNav.this, HomeCartActivity.class));
        } else if (id == R.id.nav_logout) {
            //Todo: Action Logout
            loginDatabase.UpdateData("1", "c", "c", "c", "0", "", "0", "0");
            startActivity(new Intent(SwitchNav.this, LoginActivity.class));
        }
//        else {
//            String cat_name = item.getTitle().toString();
//            int cat_id = 0;
//            for (int x=0; x<categoryModelList.size(); x++){
//                if (categoryModelList.get(x).getName().equals(cat_name)){
//                    cat_id = categoryModelList.get(x).getId();
//                    Log.e("Cat Id",cat_id+"");
//                }
//            }
//            // Todo: use cat_id to push api
//            fragmentManager.beginTransaction()
//                    .replace(R.id.frame_home,new FragmentSubCategory().setId(cat_id)).addToBackStack("FragmentSubCategory").commit();
//        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

//    private void loadCategory() {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                progressDialog = new ProgressDialog(SwitchNav.this);
//                progressDialog.setMessage("جارى تحميل البيانات ...");
//                progressDialog.setCancelable(false);
//                progressDialog.show();
//                StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://onlineapi.rivile.com/Fields/List",
//                        new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//
//                                progressDialog.dismiss();
//                                try {
//
//                                    JSONObject object = new JSONObject(response);
//                                    JSONArray array = object.getJSONArray("Fields");
//                                    if (array.length() > 0) {
//                                        Menu menu = navigationView.getMenu();
//                                        Menu submenu = menu.addSubMenu("مجالات");
//                                        for (int x = 0; x < array.length(); x++) {
//                                            JSONObject object1 = array.getJSONObject(x);
//
//                                            CategoryModel categoryModel = new CategoryModel(object1.getInt("Id"),object1.getString("Name"),"http://selltlbaty.sweverteam.com"+object1.getString("Photo"));
//                                            submenu.add(object1.getString("Name"));
//                                            categoryModelList.add(categoryModel);
//                                        }
//                                        navigationView.invalidate();
//                                    }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//                        }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                        progressDialog.dismiss();
//                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
//                        AppToastUtil toast = new AppToastUtil(SwitchNav.this);
//                        toast.setGravity(Gravity.BOTTOM, 0, 0);
//                        toast.setDuration(AppToastUtil.LENGTH_LONG);
//                        toast.setView(layout);
//                        toast.show();
//                    }
//                }){
//                    @Override
//                    protected Map<String, String> getParams() throws AuthFailureError {
//                        HashMap<String,String> map = new HashMap<>();
//                        map.put("token","?za[ZbGNz2B}MXYZ");
//                        return map;
//                    }
//                };
////        Volley.newRequestQueue(getActivity()).add(stringRequest);
//                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                        DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
//                        3,  // maxNumRetries = 2 means no retry
//                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//                Volley.newRequestQueue(SwitchNav.this).add(stringRequest);
//            }
//        });
//
//    }

    @Override
    public void onPause() {
        super.onPause();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    //Todo: For The Nearest Contacts To My Current Location
    private void loadContacts() {
        final ProgressDialog progressDialog = new ProgressDialog(SwitchNav.this);
        progressDialog.setMessage("جارى تحميل جهات العمل الأقرب ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://onlineapi.sweverteam.com/shops/list/list",
                response -> {
                    progressDialog.dismiss();
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONArray array = object.getJSONArray("List");
                        if (array.length() > 0) {
                            for (int x = 0; x < array.length(); x++) {
                                JSONObject object1 = array.getJSONObject(x);

                                ContactInfo info = new ContactInfo(
                                        object1.getInt("Id"),
                                        object1.getInt("Visitor"),
                                        object1.getInt("CatogoryId"),
                                        object1.getInt("CityId"),
                                        object1.getInt("RegionId"),
                                        (float) object1.getDouble("Rate"),
                                        object1.getString("Name"),
                                        "http://selltlbaty.sweverteam.com" + object1.getString("Photo"),
                                        object1.getString("Address"),
                                        object1.getString("Phone"),
                                        object1.getString("Descripation"),
                                        object1.getString("Descripation1"),
                                        object1.getString("Category"),
                                        object1.getString("CityName"),
                                        object1.getString("EnglishCityName"),
                                        object1.getString("RegionName")
                                );
                                contactInfos.add(info);
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    progressDialog.dismiss();
                    String errorMessage = null;
                    if (error instanceof ServerError)
                        errorMessage = "خطأ إثناء الاتصال بالخادم";
                    else if (error instanceof NetworkError)
                        errorMessage = "خطأ فى شبكه الانترنت";
                    else if (error instanceof TimeoutError)
                        errorMessage = "خطأ فى مده الانتظار";

                    if (errorMessage != null) AppToastUtil.showErrorToast(
                            errorMessage, AppToastUtil.LENGTH_LONG, SwitchNav.this);
                }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> map = new HashMap<>();
                map.put("Id", "3");
                map.put("token", "?za[ZbGNz2B}MXYZ");
                map.put("type", "1");
                map.put("x", "0");
                map.put("count", "80");
                return map;
            }
        };
//        Volley.newRequestQueue(getActivity()).add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                2,  // maxNumRetries = 2 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(SwitchNav.this).add(stringRequest);
    }
}
