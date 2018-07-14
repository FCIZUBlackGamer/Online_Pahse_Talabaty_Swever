package com.talabaty.swever.admin;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.talabaty.swever.admin.Mabi3at.Mabi3atNavigator;
import com.talabaty.swever.admin.Mabi3at.MainHome;
import com.talabaty.swever.admin.Montagat.FragmentMontag;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    NavigationView navigationView;
    //    Fragment fragment;
    FragmentManager fragmentManager;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        intent = getIntent();
        fragmentManager = getSupportFragmentManager();
//        fragment = new MainHome();
//        fragmentManager.beginTransaction().replace(R.id.frame_mabi3at, new MainHome()).commit();

        if (intent.getStringExtra("fragment").equals("montag")) {
            fragmentManager.beginTransaction().replace(R.id.frame_mabi3at, new FragmentMontag()).commit();
        } else if (intent.getStringExtra("fragment").equals("mabi3at")) {
            fragmentManager.beginTransaction().replace(R.id.frame_mabi3at, new MainHome()).commit();
        }else {

        }

//        getSupportActionBar().setTitle("المبيعات");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        int count = getFragmentManager().getBackStackEntryCount();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (count == 0) {
                super.onBackPressed();
                //additional code
            } else {
                getFragmentManager().popBackStack();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
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
        Menu nav_Menu = navigationView.getMenu();

        if (id == R.id.nav_mabe3at) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            nav_Menu.findItem(R.id.nav_mabe3at).setIcon(R.drawable.ic_shopping_basket_on_24dp);
            nav_Menu.findItem(R.id.nav_montagat).setIcon(R.drawable.ic_shopping_basket_off_24dp);
            nav_Menu.findItem(R.id.nav_trendmontag).setIcon(R.drawable.ic_trending_up_off_24dp);
            nav_Menu.findItem(R.id.nav_customer).setIcon(R.drawable.ic_people_off_24dp);
            nav_Menu.findItem(R.id.nav_contact).setIcon(R.drawable.ic_message_off_24dp);
            nav_Menu.findItem(R.id.nav_management).setIcon(R.drawable.ic_assistant_photo_off_24dp);

//            fragment = new MainHome();
            fragmentManager.beginTransaction().replace(R.id.frame_mabi3at, new MainHome()).addToBackStack("MainHome").commit();
            getSupportActionBar().setTitle("المبيعات");
//            startActivity(new Intent(Home.this, Mabi3atNavigator.class));
        } else if (id == R.id.nav_montagat) {
            nav_Menu.findItem(R.id.nav_mabe3at).setIcon(R.drawable.ic_shopping_basket_off_24dp);
            nav_Menu.findItem(R.id.nav_montagat).setIcon(R.drawable.ic_shopping_basket_on_24dp);
            nav_Menu.findItem(R.id.nav_trendmontag).setIcon(R.drawable.ic_trending_up_off_24dp);
            nav_Menu.findItem(R.id.nav_customer).setIcon(R.drawable.ic_people_off_24dp);
            nav_Menu.findItem(R.id.nav_contact).setIcon(R.drawable.ic_message_off_24dp);
            nav_Menu.findItem(R.id.nav_management).setIcon(R.drawable.ic_assistant_photo_off_24dp);
//            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            fragmentManager.beginTransaction().replace(R.id.frame_mabi3at, new FragmentMontag()).addToBackStack("FragmentMontag").commit();
            getSupportActionBar().setTitle("المنتجات");
        } else if (id == R.id.nav_trendmontag) {

            nav_Menu.findItem(R.id.nav_mabe3at).setIcon(R.drawable.ic_shopping_basket_off_24dp);
            nav_Menu.findItem(R.id.nav_montagat).setIcon(R.drawable.ic_shopping_basket_off_24dp);
            nav_Menu.findItem(R.id.nav_trendmontag).setIcon(R.drawable.ic_trending_up_on_24dp);
            nav_Menu.findItem(R.id.nav_customer).setIcon(R.drawable.ic_people_off_24dp);
            nav_Menu.findItem(R.id.nav_contact).setIcon(R.drawable.ic_message_off_24dp);
            nav_Menu.findItem(R.id.nav_management).setIcon(R.drawable.ic_assistant_photo_off_24dp);
//            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//            fragmentManager.beginTransaction().replace(R.id.frame_mabi3at, new Fragment_agent_report()).addToBackStack("Fragment_agent_report").commit();
//            getSupportActionBar().setTitle("المنتجات الأكثر بيعا");
            Intent intent = new Intent(this, Mabi3atNavigator.class);
            intent.putExtra("fragment","trend");
            startActivity(intent);
        } else if (id == R.id.nav_customer) {

            nav_Menu.findItem(R.id.nav_mabe3at).setIcon(R.drawable.ic_shopping_basket_off_24dp);
            nav_Menu.findItem(R.id.nav_montagat).setIcon(R.drawable.ic_shopping_basket_off_24dp);
            nav_Menu.findItem(R.id.nav_trendmontag).setIcon(R.drawable.ic_trending_up_off_24dp);
            nav_Menu.findItem(R.id.nav_customer).setIcon(R.drawable.ic_people_on_24dp);
            nav_Menu.findItem(R.id.nav_contact).setIcon(R.drawable.ic_message_off_24dp);
            nav_Menu.findItem(R.id.nav_management).setIcon(R.drawable.ic_assistant_photo_off_24dp);
//            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//            fragmentManager.beginTransaction().replace(R.id.frame_mabi3at, new Fragment_agent_report()).addToBackStack("Fragment_agent_report").commit();
//            getSupportActionBar().setTitle("العملاء");
            Intent intent = new Intent(this, Mabi3atNavigator.class);
            intent.putExtra("fragment","report");
            startActivity(intent);
        } else if (id == R.id.nav_contact) {

            nav_Menu.findItem(R.id.nav_mabe3at).setIcon(R.drawable.ic_shopping_basket_off_24dp);
            nav_Menu.findItem(R.id.nav_montagat).setIcon(R.drawable.ic_shopping_basket_off_24dp);
            nav_Menu.findItem(R.id.nav_trendmontag).setIcon(R.drawable.ic_trending_up_off_24dp);
            nav_Menu.findItem(R.id.nav_customer).setIcon(R.drawable.ic_people_off_24dp);
            nav_Menu.findItem(R.id.nav_contact).setIcon(R.drawable.ic_message_on_24dp);
            nav_Menu.findItem(R.id.nav_management).setIcon(R.drawable.ic_assistant_photo_off_24dp);
            getSupportActionBar().setTitle("التواصل");
        } else if (id == R.id.nav_management) {

            nav_Menu.findItem(R.id.nav_mabe3at).setIcon(R.drawable.ic_shopping_basket_off_24dp);
            nav_Menu.findItem(R.id.nav_montagat).setIcon(R.drawable.ic_shopping_basket_off_24dp);
            nav_Menu.findItem(R.id.nav_trendmontag).setIcon(R.drawable.ic_trending_up_off_24dp);
            nav_Menu.findItem(R.id.nav_customer).setIcon(R.drawable.ic_people_off_24dp);
            nav_Menu.findItem(R.id.nav_contact).setIcon(R.drawable.ic_message_off_24dp);
            nav_Menu.findItem(R.id.nav_management).setIcon(R.drawable.ic_assistant_photo_on_24dp);
            getSupportActionBar().setTitle("إداره الموظفين");
        } else if (id == R.id.nav_out) {

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}
