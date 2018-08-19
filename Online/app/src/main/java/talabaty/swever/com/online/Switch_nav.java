package talabaty.swever.com.online;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

import talabaty.swever.com.online.Chart.FragmentHomeChart;
import talabaty.swever.com.online.ContactUs.FragmentContactUs;
import talabaty.swever.com.online.Fields.FragmentFields;
import talabaty.swever.com.online.Fields.MostTrend.FragmentMostTrend;
import talabaty.swever.com.online.Fields.MostViewed.FragmentMostViewed;
import talabaty.swever.com.online.WorkWithUs.FragmentWorkWithUs;
import talabaty.swever.com.online.NearestContacts.*;
import talabaty.swever.com.online.SubCategory.*;
import talabaty.swever.com.online.Home.*;

public class Switch_nav extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager fragmentManager;
    NavigationView navigationView;
    List<CategoryModel> categoryModelList;
    List<ContactInfo> contactInfos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();
        categoryModelList = new ArrayList<>();
        contactInfos = new ArrayList<>();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_home,new Fragment_Home()).addToBackStack("Fragment_Home").commit();
        setActionBarTitle("الرئيسيه");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_home,new FragmentHomeChart()).addToBackStack("FragmentHomeChart").commit();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        loadCategory();
        //loadContacts();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.switch_nav, menu);
//        for (int i=0;i<menu.size();i++) {
//            MenuItem mi = menu.getItem(i);
//
////            //for aapplying a font to subMenu ...
////            SubMenu subMenu = mi.getSubMenu();
////            if (subMenu!=null && subMenu.size() >0 ) {
////                for (int j=0; j <subMenu.size();j++) {
////                    MenuItem subMenuItem = subMenu.getItem(j);
////                    applyFontToMenuItem(subMenuItem);
////                }
////            }
//
//            //the method we have create in activity
//            applyFontToMenuItem(mi);
//        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Fields/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            //todo: Custom Activity
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_home,new Fragment_Home()).addToBackStack("Fragment_Home").commit();
        } else if (id == R.id.nav_nearest) {

        } else if (id == R.id.nav_montag) {
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_home,new FragmentMostTrend()).addToBackStack("FragmentMostTrend").commit();
        } else if (id == R.id.nav_category) {
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_home,new FragmentFields()).addToBackStack("FragmentFields").commit();
        } else if (id == R.id.nav_contact) {
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_home,new FragmentMostViewed()).addToBackStack("FragmentMostViewed").commit();
        } else if (id == R.id.nav_work_with_us) {
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_home,new FragmentWorkWithUs()).addToBackStack("FragmentWorkWithUs").commit();
        } else if (id == R.id.nav_call_us) {
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_home,new FragmentContactUs()).addToBackStack("FragmentContactUs").commit();
        } else if (id == R.id.nav_car_shop) {
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_home,new FragmentHomeChart()).addToBackStack("FragmentHomeChart").commit();
        } else if (id == R.id.nav_logout) {
            //Todo: Action Logout
        }else {
            String cat_name = item.getTitle().toString();
            int cat_id = 0;
            for (int x=0; x<categoryModelList.size(); x++){
                if (categoryModelList.get(x).getName().equals(cat_name)){
                    cat_id = categoryModelList.get(x).getId();
                    Log.e("Cat Id",cat_id+"");
                }
            }
            // Todo: use cat_id to push api
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_home,new FragmentSubCategory().setId(cat_id)).addToBackStack("FragmentSubCategory").commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    private void loadCategory() {
        final ProgressDialog progressDialog = new ProgressDialog(Switch_nav.this);
        progressDialog.setMessage("جارى تحميل البيانات ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://onlineapi.sweverteam.com/Fields/List",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        try {

                            JSONObject object = new JSONObject(response);
                            JSONArray array = object.getJSONArray("Fields");
                            if (array.length() > 0) {
                                Menu menu = navigationView.getMenu();
                                Menu submenu = menu.addSubMenu("مجالات");
                                for (int x = 0; x < array.length(); x++) {
                                    JSONObject object1 = array.getJSONObject(x);

                                    CategoryModel categoryModel = new CategoryModel(object1.getInt("Id"),object1.getString("Name"),"http://selltlbaty.sweverteam.com"+object1.getString("Photo"));
                                    submenu.add(object1.getString("Name"));
                                    categoryModelList.add(categoryModel);
                                }
                                navigationView.invalidate();
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
                    Toast.makeText(Switch_nav.this, "خطأ إثناء الاتصال بالخادم", Toast.LENGTH_SHORT).show();
                else if (error instanceof NetworkError)
                    Toast.makeText(Switch_nav.this, "خطأ فى شبكه الانترنت", Toast.LENGTH_SHORT).show();
                else if (error instanceof TimeoutError)
                    Toast.makeText(Switch_nav.this, "خطأ فى مده الانتظار", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("token","?za[ZbGNz2B}MXYZ");
                return map;
            }
        };
//        Volley.newRequestQueue(getActivity()).add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                2,  // maxNumRetries = 2 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(Switch_nav.this).add(stringRequest);
    }



    private void loadContacts() {
        final ProgressDialog progressDialog = new ProgressDialog(Switch_nav.this);
        progressDialog.setMessage("جارى تحميل جهات العمل الأقرب ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://onlineapi.sweverteam.com/shops/list/list",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

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
                                            "http://selltlbaty.sweverteam.com"+object1.getString("Photo"),
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

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                if (error instanceof ServerError)
                    Toast.makeText(Switch_nav.this, "خطأ إثناء الاتصال بالخادم", Toast.LENGTH_SHORT).show();
                else if (error instanceof NetworkError)
                    Toast.makeText(Switch_nav.this, "خطأ فى شبكه الانترنت", Toast.LENGTH_SHORT).show();
                else if (error instanceof TimeoutError)
                    Toast.makeText(Switch_nav.this, "خطأ فى مده الانتظار", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("Id","3");
                map.put("token","?za[ZbGNz2B}MXYZ");
                map.put("type","1");
                map.put("x","0");
                map.put("count","80");
                return map;
            }
        };
//        Volley.newRequestQueue(getActivity()).add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                2,  // maxNumRetries = 2 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(Switch_nav.this).add(stringRequest);
    }
}
