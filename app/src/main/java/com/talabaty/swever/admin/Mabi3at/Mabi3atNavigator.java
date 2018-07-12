package com.talabaty.swever.admin.Mabi3at;

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

import com.talabaty.swever.admin.Home;
import com.talabaty.swever.admin.Mabi3at.DoneTalabat.DoneTalabat;
import com.talabaty.swever.admin.Mabi3at.NewTalabat.NewTalabatFragment;
import com.talabaty.swever.admin.Mabi3at.NotificationToFriendTalabat.NotificationToFriendTalabat;
import com.talabaty.swever.admin.Mabi3at.PendedTalabat.PendedTalabatFragment;
import com.talabaty.swever.admin.Mabi3at.ReadyTalabat.ReadyTalabatFragment;
import com.talabaty.swever.admin.Mabi3at.RejectedReports.RejectedReports;
import com.talabaty.swever.admin.Mabi3at.ReturnedTalabat.ReturnedTalabatFragment;
import com.talabaty.swever.admin.Mabi3at.SailedReports.SailedReports;
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
            fragmentManager.beginTransaction().replace(R.id.new_talabat_frame, new NewTalabatFragment()).commit();
        } else if (intent.getStringExtra("fragment").equals("ready")) {
            fragmentManager.beginTransaction().replace(R.id.new_talabat_frame, new ReadyTalabatFragment()).commit();
        } else if (intent.getStringExtra("fragment").equals("pend")) {
            fragmentManager.beginTransaction().replace(R.id.new_talabat_frame, new PendedTalabatFragment()).commit();
        } else if (intent.getStringExtra("fragment").equals("returned")) {
            fragmentManager.beginTransaction().replace(R.id.new_talabat_frame, new ReturnedTalabatFragment()).commit();
        } else if (intent.getStringExtra("fragment").equals("notification")) {
            fragmentManager.beginTransaction().replace(R.id.new_talabat_frame, new NotificationToFriendTalabat()).commit();
        } else if (intent.getStringExtra("fragment").equals("done")) {
            fragmentManager.beginTransaction().replace(R.id.new_talabat_frame, new DoneTalabat()).commit();
        } else if (intent.getStringExtra("fragment").equals("rejected")) {
            fragmentManager.beginTransaction().replace(R.id.new_talabat_frame, new RejectedReports()).commit();
        } else if (intent.getStringExtra("fragment").equals("sailed")) {
            fragmentManager.beginTransaction().replace(R.id.new_talabat_frame, new SailedReports()).commit();
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

            nav_Menu.findItem(R.id.nav_mabe3at).setIcon(R.drawable.ic_shopping_basket_on_24dp);
            nav_Menu.findItem(R.id.nav_montagat).setIcon(R.drawable.ic_shopping_basket_off_24dp);
            nav_Menu.findItem(R.id.nav_trendmontag).setIcon(R.drawable.ic_trending_up_off_24dp);
            nav_Menu.findItem(R.id.nav_customer).setIcon(R.drawable.ic_people_off_24dp);
            nav_Menu.findItem(R.id.nav_contact).setIcon(R.drawable.ic_message_off_24dp);
            nav_Menu.findItem(R.id.nav_management).setIcon(R.drawable.ic_assistant_photo_off_24dp);

            startActivity(new Intent(Mabi3atNavigator.this, Home.class));
//            getSupportActionBar().setTitle("المبيعات");
        } else if (id == R.id.nav_montagat) {

            nav_Menu.findItem(R.id.nav_mabe3at).setIcon(R.drawable.ic_shopping_basket_off_24dp);
            nav_Menu.findItem(R.id.nav_montagat).setIcon(R.drawable.ic_shopping_basket_on_24dp);
            nav_Menu.findItem(R.id.nav_trendmontag).setIcon(R.drawable.ic_trending_up_off_24dp);
            nav_Menu.findItem(R.id.nav_customer).setIcon(R.drawable.ic_people_off_24dp);
            nav_Menu.findItem(R.id.nav_contact).setIcon(R.drawable.ic_message_off_24dp);
            nav_Menu.findItem(R.id.nav_management).setIcon(R.drawable.ic_assistant_photo_off_24dp);
//            getSupportActionBar().setTitle("المنتجات");
        } else if (id == R.id.nav_trendmontag) {

            nav_Menu.findItem(R.id.nav_mabe3at).setIcon(R.drawable.ic_shopping_basket_off_24dp);
            nav_Menu.findItem(R.id.nav_montagat).setIcon(R.drawable.ic_shopping_basket_off_24dp);
            nav_Menu.findItem(R.id.nav_trendmontag).setIcon(R.drawable.ic_trending_up_on_24dp);
            nav_Menu.findItem(R.id.nav_customer).setIcon(R.drawable.ic_people_off_24dp);
            nav_Menu.findItem(R.id.nav_contact).setIcon(R.drawable.ic_message_off_24dp);
            nav_Menu.findItem(R.id.nav_management).setIcon(R.drawable.ic_assistant_photo_off_24dp);
//            getSupportActionBar().setTitle("المنتجات الأكثر بيعا");
        } else if (id == R.id.nav_customer) {

            nav_Menu.findItem(R.id.nav_mabe3at).setIcon(R.drawable.ic_shopping_basket_off_24dp);
            nav_Menu.findItem(R.id.nav_montagat).setIcon(R.drawable.ic_shopping_basket_off_24dp);
            nav_Menu.findItem(R.id.nav_trendmontag).setIcon(R.drawable.ic_trending_up_off_24dp);
            nav_Menu.findItem(R.id.nav_customer).setIcon(R.drawable.ic_people_on_24dp);
            nav_Menu.findItem(R.id.nav_contact).setIcon(R.drawable.ic_message_off_24dp);
            nav_Menu.findItem(R.id.nav_management).setIcon(R.drawable.ic_assistant_photo_off_24dp);
//            getSupportActionBar().setTitle("العملاء");
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
