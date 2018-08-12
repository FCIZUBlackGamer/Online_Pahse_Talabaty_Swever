package talabaty.swever.com.online;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManagerNonConfig;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.SubMenu;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import talabaty.swever.com.online.Chart.FragmentHomeChart;
import talabaty.swever.com.online.ContactUs.FragmentContactUs;
import talabaty.swever.com.online.Home.FragmentHome;
import talabaty.swever.com.online.Home.MostTrend.FragmentMostTrend;
import talabaty.swever.com.online.Home.MostViewed.FragmentMostViewed;
import talabaty.swever.com.online.WorkWithUs.FragmentWorkWithUs;

public class Switch_nav extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_nav);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                fragmentManager.beginTransaction()
                        .replace(R.id.frame_home,new FragmentHomeChart()).commit();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        // automatically handle clicks on the Home/Up button, so long
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
        } else if (id == R.id.nav_montag) {
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_home,new FragmentMostTrend()).commit();
        } else if (id == R.id.nav_category) {
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_home,new FragmentHome()).commit();
        } else if (id == R.id.nav_contact) {
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_home,new FragmentMostViewed()).commit();
        } else if (id == R.id.nav_work_with_us) {
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_home,new FragmentWorkWithUs()).commit();
        } else if (id == R.id.nav_call_us) {
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_home,new FragmentContactUs()).commit();
        } else if (id == R.id.nav_car_shop) {
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_home,new FragmentHomeChart()).commit();
        } else if (id == R.id.nav_logout) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "font/changamedium.ttf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }
}
