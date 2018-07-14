package com.talabaty.swever.admin.Mabi3at;

import android.content.pm.ActivityInfo;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.talabaty.swever.admin.AgentReports.Fragment_agent_report;
import com.talabaty.swever.admin.Home;
import com.talabaty.swever.admin.Mabi3at.DoneTalabat.DoneTalabat;
import com.talabaty.swever.admin.Mabi3at.NewTalabat.NewTalabatFragment;
import com.talabaty.swever.admin.Mabi3at.NotificationToFriendTalabat.NotificationToFriendTalabat;
import com.talabaty.swever.admin.Mabi3at.PendedTalabat.PendedTalabatFragment;
import com.talabaty.swever.admin.Mabi3at.ReadyTalabat.ReadyTalabatFragment;
import com.talabaty.swever.admin.Mabi3at.RejectedReports.RejectedReports;
import com.talabaty.swever.admin.Mabi3at.ReturnedTalabat.ReturnedTalabatFragment;
import com.talabaty.swever.admin.Mabi3at.SailedReports.SailedReports;
import com.talabaty.swever.admin.Mabi3atTrend.Mabi3atTrendReports;
import com.talabaty.swever.admin.Montagat.ControlMontag.ControlMontag;
import com.talabaty.swever.admin.Montagat.FragmentMontag;
import com.talabaty.swever.admin.R;

public class Mabi3atNavigator extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    NavigationView navigationView;
    //    Fragment fragment;
    FragmentManager fragmentManager;
    Intent intent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_talabat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Get Fragment Name
        intent = getIntent();

        fragmentManager = getSupportFragmentManager();

        if (intent.getStringExtra("fragment").equals("new")) {
            fragmentManager.beginTransaction().replace(R.id.new_talabat_frame, new NewTalabatFragment()).addToBackStack("").commit();
        } else if (intent.getStringExtra("fragment").equals("ready")) {
            fragmentManager.beginTransaction().replace(R.id.new_talabat_frame, new ReadyTalabatFragment()).addToBackStack("ReadyTalabatFragment").commit();
        } else if (intent.getStringExtra("fragment").equals("pend")) {
            fragmentManager.beginTransaction().replace(R.id.new_talabat_frame, new PendedTalabatFragment()).addToBackStack("PendedTalabatFragment").commit();
        } else if (intent.getStringExtra("fragment").equals("returned")) {
            fragmentManager.beginTransaction().replace(R.id.new_talabat_frame, new ReturnedTalabatFragment()).addToBackStack("ReturnedTalabatFragment").commit();
        } else if (intent.getStringExtra("fragment").equals("notification")) {
            fragmentManager.beginTransaction().replace(R.id.new_talabat_frame, new NotificationToFriendTalabat()).addToBackStack("NotificationToFriendTalabat").commit();
        } else if (intent.getStringExtra("fragment").equals("done")) {
            fragmentManager.beginTransaction().replace(R.id.new_talabat_frame, new DoneTalabat()).addToBackStack("DoneTalabat").commit();
        } else if (intent.getStringExtra("fragment").equals("rejected")) {
            fragmentManager.beginTransaction().replace(R.id.new_talabat_frame, new RejectedReports()).addToBackStack("RejectedReports").commit();
        } else if (intent.getStringExtra("fragment").equals("sailed")) {
            fragmentManager.beginTransaction().replace(R.id.new_talabat_frame, new SailedReports()).addToBackStack("SailedReports").commit();
        } else if (intent.getStringExtra("fragment").equals("control")) {
            fragmentManager.beginTransaction().replace(R.id.new_talabat_frame, new ControlMontag()).commit();
        } else if (intent.getStringExtra("fragment").equals("trend")) {
            fragmentManager.beginTransaction().replace(R.id.new_talabat_frame, new Mabi3atTrendReports()).commit();
        } else if (intent.getStringExtra("fragment").equals("report")) {
            fragmentManager.beginTransaction().replace(R.id.new_talabat_frame, new Fragment_agent_report()).commit();
        }
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
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
//            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            nav_Menu.findItem(R.id.nav_mabe3at).setIcon(R.drawable.ic_shopping_basket_on_24dp);
            nav_Menu.findItem(R.id.nav_montagat).setIcon(R.drawable.ic_shopping_basket_off_24dp);
            nav_Menu.findItem(R.id.nav_trendmontag).setIcon(R.drawable.ic_trending_up_off_24dp);
            nav_Menu.findItem(R.id.nav_customer).setIcon(R.drawable.ic_people_off_24dp);
            nav_Menu.findItem(R.id.nav_contact).setIcon(R.drawable.ic_message_off_24dp);
            nav_Menu.findItem(R.id.nav_management).setIcon(R.drawable.ic_assistant_photo_off_24dp);

//            fragmentManager.beginTransaction().replace(R.id.frame_mabi3at,new MainHome()).addToBackStack("MainHome").commit();
//            getSupportActionBar().setTitle("المبيعات");
//            getSupportActionBar().setTitle("المبيعات");
            Intent intent = new Intent(Mabi3atNavigator.this,Home.class);
            intent.putExtra("fragment","mabi3at");
            startActivity(intent);
        } else if (id == R.id.nav_montagat) {

            nav_Menu.findItem(R.id.nav_mabe3at).setIcon(R.drawable.ic_shopping_basket_off_24dp);
            nav_Menu.findItem(R.id.nav_montagat).setIcon(R.drawable.ic_shopping_basket_on_24dp);
            nav_Menu.findItem(R.id.nav_trendmontag).setIcon(R.drawable.ic_trending_up_off_24dp);
            nav_Menu.findItem(R.id.nav_customer).setIcon(R.drawable.ic_people_off_24dp);
            nav_Menu.findItem(R.id.nav_contact).setIcon(R.drawable.ic_message_off_24dp);
            nav_Menu.findItem(R.id.nav_management).setIcon(R.drawable.ic_assistant_photo_off_24dp);
//            getSupportActionBar().setTitle("المنتجات");
//            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//            fragmentManager.beginTransaction().replace(R.id.frame_mabi3at,new FragmentMontag()).addToBackStack("FragmentMontag").commit();
//            getSupportActionBar().setTitle("المنتجات");
            Intent intent = new Intent(Mabi3atNavigator.this,Home.class);
            intent.putExtra("fragment","montag");
            startActivity(intent);
        } else if (id == R.id.nav_trendmontag) {

            nav_Menu.findItem(R.id.nav_mabe3at).setIcon(R.drawable.ic_shopping_basket_off_24dp);
            nav_Menu.findItem(R.id.nav_montagat).setIcon(R.drawable.ic_shopping_basket_off_24dp);
            nav_Menu.findItem(R.id.nav_trendmontag).setIcon(R.drawable.ic_trending_up_on_24dp);
            nav_Menu.findItem(R.id.nav_customer).setIcon(R.drawable.ic_people_off_24dp);
            nav_Menu.findItem(R.id.nav_contact).setIcon(R.drawable.ic_message_off_24dp);
            nav_Menu.findItem(R.id.nav_management).setIcon(R.drawable.ic_assistant_photo_off_24dp);
//            getSupportActionBar().setTitle("المنتجات الأكثر بيعا");
//            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            fragmentManager.beginTransaction().replace(R.id.new_talabat_frame,new Mabi3atTrendReports()).addToBackStack("Mabi3atTrendReports").commit();
            getSupportActionBar().setTitle("المنتجات الأكثر بيعا");
        } else if (id == R.id.nav_customer) {

            nav_Menu.findItem(R.id.nav_mabe3at).setIcon(R.drawable.ic_shopping_basket_off_24dp);
            nav_Menu.findItem(R.id.nav_montagat).setIcon(R.drawable.ic_shopping_basket_off_24dp);
            nav_Menu.findItem(R.id.nav_trendmontag).setIcon(R.drawable.ic_trending_up_off_24dp);
            nav_Menu.findItem(R.id.nav_customer).setIcon(R.drawable.ic_people_on_24dp);
            nav_Menu.findItem(R.id.nav_contact).setIcon(R.drawable.ic_message_off_24dp);
            nav_Menu.findItem(R.id.nav_management).setIcon(R.drawable.ic_assistant_photo_off_24dp);
//            getSupportActionBar().setTitle("العملاء");
//            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            fragmentManager.beginTransaction().replace(R.id.new_talabat_frame,new Fragment_agent_report()).commit();
            getSupportActionBar().setTitle("العملاء");
        } else if (id == R.id.nav_contact) {

            nav_Menu.findItem(R.id.nav_mabe3at).setIcon(R.drawable.ic_shopping_basket_off_24dp);
            nav_Menu.findItem(R.id.nav_montagat).setIcon(R.drawable.ic_shopping_basket_off_24dp);
            nav_Menu.findItem(R.id.nav_trendmontag).setIcon(R.drawable.ic_trending_up_off_24dp);
            nav_Menu.findItem(R.id.nav_customer).setIcon(R.drawable.ic_people_off_24dp);
            nav_Menu.findItem(R.id.nav_contact).setIcon(R.drawable.ic_message_on_24dp);
            nav_Menu.findItem(R.id.nav_management).setIcon(R.drawable.ic_assistant_photo_off_24dp);
//            getSupportActionBar().setTitle("التواصل");
        } else if (id == R.id.nav_management) {

            nav_Menu.findItem(R.id.nav_mabe3at).setIcon(R.drawable.ic_shopping_basket_off_24dp);
            nav_Menu.findItem(R.id.nav_montagat).setIcon(R.drawable.ic_shopping_basket_off_24dp);
            nav_Menu.findItem(R.id.nav_trendmontag).setIcon(R.drawable.ic_trending_up_off_24dp);
            nav_Menu.findItem(R.id.nav_customer).setIcon(R.drawable.ic_people_off_24dp);
            nav_Menu.findItem(R.id.nav_contact).setIcon(R.drawable.ic_message_off_24dp);
            nav_Menu.findItem(R.id.nav_management).setIcon(R.drawable.ic_assistant_photo_on_24dp);
//            getSupportActionBar().setTitle("إداره الموظفين");
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
