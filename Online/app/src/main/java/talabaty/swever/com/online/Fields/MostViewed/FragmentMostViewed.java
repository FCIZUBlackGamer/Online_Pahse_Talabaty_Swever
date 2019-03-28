package talabaty.swever.com.online.Fields.MostViewed;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import talabaty.swever.com.online.Contact.FragmentHomeContacts;
import talabaty.swever.com.online.PrepareFood.FragmentPrepareFood;
import talabaty.swever.com.online.R;
import talabaty.swever.com.online.SwitchNavActivity;
import talabaty.swever.com.online.Utils.APIURLUtil;
import talabaty.swever.com.online.Utils.AppRepository;
import talabaty.swever.com.online.Utils.AppToastUtil;
import talabaty.swever.com.online.Utils.StringUtil;

public class FragmentMostViewed extends Fragment {


    GridView gridView;
    MostViewedAdapter mostViewedAdapter;
    List<Contact> contacts;
    FragmentManager fragmentManager;

    int temp_first, temp_last;
    TextView next, num, last;
    int item_num, page_num;

    String state = null;

    static String Type = "null";
    String Link;
    View view;
    LinearLayout layout;
    ProgressDialog progressDialog, progressDialog2;
//    boolean enabled;
//
//    private static final int REQUEST_LOCATION = 1;
//    private boolean gps_enabled = false;
//    private boolean network_enabled = false;
//    Location location;
//
//    Double MyLat, MyLong;
//    String CityName = "";
//    String StateName = "";
//    String CountryName = "";
//
//    public static final String NO_LOCATION_NAME = "NONE";
//
//    public LocationListener listener;

    private AppRepository mRepository;

    public FragmentMostViewed setType(String type) {
        FragmentMostViewed trend = new FragmentMostViewed();
        Type = type;
        return trend;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mRepository = AppRepository.getInstance(getActivity().getApplication());

        view = inflater.inflate(R.layout.fragment_home_most_viewed, container, false);
        gridView = view.findViewById(R.id.gridview);
        contacts = new ArrayList<>();
        next = view.findViewById(R.id.next);
        last = view.findViewById(R.id.previous);
        num = view.findViewById(R.id.item_num);
        layout = view.findViewById(R.id.layout);
        item_num = page_num = 0;
        num.setText(1 + "");
        fragmentManager = getFragmentManager();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
//        locationManager.requestLocationUpdates(provider, 400, 1, getActivity());

//        LocationManager service = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
//        enabled = service
//                .isProviderEnabled(LocationManager.GPS_PROVIDER);
//
//        // check if enabled and if not send user to the GSP settings
//        // Better solution would be to display a dialog and suggesting to
//        // go to the settings
//        if (!enabled) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//            builder.setMessage("يرجى تفعيل خاصيه ال gps لتحديد جهات العمل الاقرب منك")
//                    .setPositiveButton("موافق", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                            startActivity(intent);
//                        }
//                    }).setNegativeButton("ليس الأن", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    Snackbar.make(view, "لا توجد بيانات حاليا يرجى تفعيل ال gps", Snackbar.LENGTH_LONG).show();
//                }
//            }).show();
//
//        } else {
//            getMyCurrentLocation();
//        }

        temp_first = 0;
        temp_last = 10;
        page_num = 0;

        next.setOnClickListener(v -> {
            Log.e("Item Num", item_num + "");
            if (contacts.size() == 80) {
                loadContact(item_num, 1);
            } else {
                Snackbar.make(v, "نهايه جهات العمل", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        last.setOnClickListener(v -> {
            if (page_num > 1) {
                Log.e("Item Num", item_num + "");
                loadContact(item_num, 0);
            } else {
                Snackbar.make(v, "بدايه جهات العمل", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
/** الاكثر زياره*/
        if (Type.equals("trend")) {
            ((SwitchNavActivity) getActivity()).setActionBarTitle("الأكثر زيارة");
            Link = APIURLUtil.LIST_MOST_VISITED_SHOPS_URL;
            loadContact(0, 1);
        } else if (Type.equals("nearest")) {
            /** الاقرب**/
            ((SwitchNavActivity) getActivity()).setActionBarTitle("الأقرب");
            Link = APIURLUtil.LIST_NEAREST_SHOPS_URL;

            GpsLocationTracker mGpsLocationTracker = new GpsLocationTracker(getActivity());

            /**
             * Set GPS Location fetched address
             */
            if (mGpsLocationTracker.canGetLocation()) {
                double latitude = mGpsLocationTracker.getLatitude();
                double longitude = mGpsLocationTracker.getLongitude();
                Log.i("ssss", String.format("latitude: %s", latitude));
                Log.i("rrrr", String.format("longitude: %s", longitude));

                Geocoder geoCoder = new Geocoder(getActivity(), Locale.getDefault());

                try {
                    List<Address> address = geoCoder.getFromLocation(latitude, longitude, 1);

                    String addressStr = address.get(0).getAddressLine(0);
                    state = address.get(0).getAdminArea();

                    Log.e("Lat", String.valueOf(latitude));
                    Log.e("Lon", String.valueOf(longitude));
                    Log.e("Address", addressStr); //This will display the final address.
                    Log.e("state", state); //This will display the final address.

                    loadContact(0, 1);

                } catch (Exception e) {
                    // Handle IOException
                }

            } else {
                mGpsLocationTracker.showSettingsAlert();
            }

        } else if (Type.equals("normal")) {
            /** المنتجات العاديه*/
            Link = "http://onlineapi.rivile.com/shops/list";
            loadContact(0, 1);
        } else if (Type.equals("prepare_food")) {
            /** جهز وجبتى**/
            Link = "http://onlineapi.rivile.com/BeTheChef/ShopList";
            loadContactOffersPrepareFood();
        }
    }

    private void loadContact(final int x, final int type) {
        progressDialog2 = new ProgressDialog(getActivity());
        progressDialog2.setMessage("جارى تحميل البيانات ...");
        progressDialog2.setCancelable(false);
        progressDialog2.show();

        if (Link.equals(APIURLUtil.LIST_MOST_VISITED_SHOPS_URL)) {
            mRepository.listMostVisitedShops(type, 80, x).observe(this,
                    this::handleNetworkResponseFromFunction_loadContact);
        } else if (Link.equals(APIURLUtil.LIST_NEAREST_SHOPS_URL)) {
            mRepository.listNearestShops(type, 80, x).observe(this,
                    this::handleNetworkResponseFromFunction_loadContact);
        }
    }

    private void handleNetworkResponseFromFunction_loadContact(String response) {
        if (response != null) {
            progressDialog2.dismiss();
            if (!response.equals(StringUtil.DISMISS_PROGRESS_DIALOG)) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("List");
                    if (array.length() > 0) {

//                                final int size = contacts.size();
//                                if (size > 0) {
//                                    for (int i = 0; i < size; i++) {
//                                        contacts.remove(0);
//
//                                    }
//                                    mostViewedAdapter.notifyDataSetChanged();
//                                }

                        contacts = new ArrayList<>();

                        if (state == null) {
                            for (int x1 = 0; x1 < array.length(); x1++) {
                                JSONObject object1 = array.getJSONObject(x1);

                                Contact info = new Contact(
                                        object1.getInt("Id"),
                                        object1.getString("Name"),
                                        (float) object1.getDouble("Rate"),
                                        object1.getString("Address"),
                                        "BBB",// object1.getString("Email")
                                        object1.getString("Phone"),
                                        "http://selltlbaty.rivile.com" + object1.getString("Photo"),
                                        ""
                                );
                                contacts.add(info);

                            }
                        } else {
                            showSettingsAlert();
                            state = state.trim().toLowerCase();
                            String shop_address = "";

                            for (int x1 = 0; x1 < array.length(); x1++) {
                                JSONObject object1 = array.getJSONObject(x1);
                                Log.e("InCity", state);
                                String[] plit_add = object1.getString("Address").split(",");
                                for (int i = 0; i < plit_add.length; i++) {
                                    shop_address = plit_add[i];
                                }
                                Log.e("OutCity", shop_address);
                                if (state.contains(shop_address.toLowerCase().trim())) {
                                    Contact info = new Contact(
                                            object1.getInt("Id"),
                                            object1.getString("Name"),
                                            (float) object1.getDouble("Rate"),
                                            object1.getString("Address"),
                                            "BBB",// object1.getString("Email")
                                            object1.getString("Phone"),
                                            APIURLUtil.IMAGE_BASE_URL + object1.getString("Photo"),
                                            ""
                                    );
                                    contacts.add(info);
                                }
                            }
                        }
                        mostViewedAdapter = new MostViewedAdapter(getActivity(), contacts);
                        gridView.setAdapter(mostViewedAdapter);
                        Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.fly_in_from_center);
                        gridView.setAnimation(anim);
                        anim.start();
                        gridView.setOnItemClickListener((parent, view, position, id) -> fragmentManager.beginTransaction()
                                .replace(R.id.frame_home, FragmentHomeContacts.setData(contacts.get(position).getId(), contacts.get(position).getPhone(), contacts.get(position).getEmail(),
                                        contacts.get(position).getLocation(), contacts.get(position).getName(), contacts.get(position).getCompany_logo(),
                                        contacts.get(position).getRate())).addToBackStack("FragmentHomeContacts").commit());

                    } else {
                        AppToastUtil.showInfoToast("لا توجد بيانات",
                                AppToastUtil.LENGTH_LONG, getContext());
                    }
                } catch (JSONException e) {
                    Log.e(StringUtil.EXCEPTION_TAG, e.getMessage());
                }
            }
        }
    }

    private void loadContactOffersPrepareFood() {
        layout.setVisibility(View.GONE);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("جارى تحميل البيانات ...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        if (Link.equals(APIURLUtil.LIST_MOST_VISITED_SHOPS_URL)) {
            mRepository.listMostVisitedShops(null, null, null).observe(this,
                    this::handleNetworkResponseFromFunction_loadContactOffersPrepareFood);
        } else if (Link.equals(APIURLUtil.LIST_NEAREST_SHOPS_URL)) {
            mRepository.listNearestShops(null, null, null).observe(this,
                    this::handleNetworkResponseFromFunction_loadContactOffersPrepareFood);
        }
    }

    private void handleNetworkResponseFromFunction_loadContactOffersPrepareFood(String response) {
        if (response != null) {
            progressDialog.dismiss();

            if (!response.equals(StringUtil.DISMISS_PROGRESS_DIALOG)) {
                try {
                    JSONObject object = new JSONObject(response);
                    JSONArray array = object.getJSONArray("List");
                    if (array.length() > 0) {

//                                final int size = contacts.size();
//                                if (size > 0) {
//                                    for (int i = 0; i < size; i++) {
//                                        contacts.remove(0);
//
//                                    }
//                                    mostViewedAdapter.notifyDataSetChanged();
//                                }
                        contacts = new ArrayList<>();

                        for (int x = 0; x < array.length(); x++) {
                            JSONObject object1 = array.getJSONObject(x);

                            Contact info = new Contact(
                                    object1.getInt("Id"),
                                    object1.getString("Name"),
                                    (float) object1.getDouble("Rate"),
                                    object1.getString("Address"),
                                    "BBB",// object1.getString("Email")
                                    object1.getString("Phone"),
                                    "http://selltlbaty.rivile.com" + object1.getString("Photo"),
                                    ""
                            );
                            contacts.add(info);

                        }

                        mostViewedAdapter = new MostViewedAdapter(getActivity(), contacts, 0);
                        gridView.setAdapter(mostViewedAdapter);
                        Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.fly_in_from_center);
                        gridView.setAnimation(anim);
                        anim.start();
                        gridView.setOnItemClickListener((parent, view, position, id) -> fragmentManager.beginTransaction()
                                .replace(R.id.frame_home, new FragmentPrepareFood().setData(contacts.get(position).getId())).addToBackStack("FragmentPrepareFood").commit());

                    } else {
                        AppToastUtil.showInfoToast("لا توجد بيانات",
                                AppToastUtil.LENGTH_LONG, getContext());
                    }
                } catch (JSONException e) {
                    Log.e(StringUtil.EXCEPTION_TAG, e.getMessage());
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        if (progressDialog2 != null && progressDialog2.isShowing()) {
            progressDialog2.dismiss();
        }

    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", (dialog, which) -> {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
            dialog.cancel();
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

//    @Override
//    public void onLocationChanged(Location location) {
//        //You had this as int. It is advised to have Lat/Loing as double.
//        double lat = location.getLatitude();
//        double lng = location.getLongitude();
//
//        Geocoder geoCoder = new Geocoder(getActivity(), Locale.getDefault());
//        StringBuilder builder = new StringBuilder();
//        try {
//            List<Address> address = geoCoder.getFromLocation(lat, lng, 1);
//            int maxLines = address.get(0).getMaxAddressLineIndex();
//            for (int i=0; i<maxLines; i++) {
//                String addressStr = address.get(0).getAddressLine(i);
//                state = address.get(0).getCountryName();
//                builder.append(addressStr);
//                builder.append(" ");
//            }
//
//            String fnialAddress = builder.toString(); //This is the complete address.
//
//            Log.e("Lat",String.valueOf(lat));
//            Log.e("Lon",String.valueOf(lng));
//            Log.e("Address",fnialAddress); //This will display the final address.
//            Log.e("state", state); //This will display the final address.
//
//        } catch (IOException e) {
//            // Handle IOException
//        } catch (NullPointerException e) {
//            // Handle NullPointerException
//        }
//    }
//
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//        AppToastUtil.makeText(getActivity(), "Enabled new provider " + provider,
//                AppToastUtil.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onProviderDisabled(String provider) {
//        AppToastUtil.makeText(getActivity(), "Disabled provider " + provider,
//                AppToastUtil.LENGTH_SHORT).show();
//    }

//
//    @SuppressLint("MissingPermission")
//    void getMyCurrentLocation() {
//
//
//        LocationManager locManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//        LocationListener locListener = new FragmentMostViewed.MyLocationListener();
//
//
//        try {
//            gps_enabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        } catch (Exception ex) {
//        }
//        try {
//            network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//        } catch (Exception ex) {
//        }
//
//        //don't start listeners if no provider is enabled
//        //if(!gps_enabled && !network_enabled)
//        //return false;
//
//        if (gps_enabled) {
//            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
//
//        }
//
//
//        if (gps_enabled) {
//            location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//
//
//        }
//
//
//        if (network_enabled && location == null) {
//            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);
//
//        }
//
//
//        if (network_enabled && location == null) {
//            location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//
//        }
//
//        if (location != null) {
//
//            MyLat = location.getLatitude();
//            MyLong = location.getLongitude();
//
//
//        } else {
//            Location loc = getLastKnownLocation(getActivity());
//            if (loc != null) {
//
//                MyLat = loc.getLatitude();
//                MyLong = loc.getLongitude();
//
//
//            }
//        }
//        locManager.removeUpdates(locListener); // removes the periodic updates from location listener to //avoid battery drainage. If you want to get location at the periodic intervals call this method using //pending intent.
//
//        try {
//// Getting address from found locations.
//            Geocoder geocoder;
//
//            List<Address> addresses;
//            geocoder = new Geocoder(getActivity(), Locale.getDefault());
//            addresses = geocoder.getFromLocation(MyLat, MyLong, 1);
//
//            StateName = addresses.get(0).getAdminArea();
//            CityName = addresses.get(0).getLocality();
//            CountryName = addresses.get(0).getCountryName();
//            // you can get more details other than this . like country code, state code, etc.
//
//
//          /*  AppToastUtil.makeText(this," StateName " + StateName+"\n"+
//                    " CityName " + CityName+"\n"+
//                    " CountryName " + CountryName,AppToastUtil.LENGTH_LONG).show();
//                    */
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        final Handler mHandler = new Handler();
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                for (int i = 0; i <= 30; i++) {
//                    final int currentProgressCount = i;
//                    try {
//                        Thread.sleep(500);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
////                  Update the value background thread to UI thread
//                    mHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            AppToastUtil.makeText(getActivity(), "" + MyLat + "\n" +
//                                    MyLong + "\n StateName " + StateName +
//                                    "\n CityName " + CityName + "\n CountryName "
//                                    + CountryName, AppToastUtil.LENGTH_LONG).show();
//                        }
//                    });
//                }
//            }
//        }).start();
//
//    }
//
//    // Location listener class. to get location.
//    public class MyLocationListener implements LocationListener {
//        public void onLocationChanged(Location location) {
//            if (location != null) {
//            }
//        }
//
//        public void onProviderDisabled(String provider) {
//            // TODO Auto-generated method stub
//        }
//
//        public void onProviderEnabled(String provider) {
//            // TODO Auto-generated method stub
//        }
//
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//            // TODO Auto-generated method stub
//        }
//    }
//
//// below method to get the last remembered location. because we don't get locations all the times .At some instances we are unable to get the location from GPS. so at that moment it will show us the last stored location.
//
//    public static Location getLastKnownLocation(Context context) {
//        Location location = null;
//        @SuppressLint("WrongConstant") LocationManager locationmanager = (LocationManager) context.getSystemService("location");
//        List list = locationmanager.getAllProviders();
//        boolean i = false;
//        Iterator iterator = list.iterator();
//        do {
//            //System.out.println("---------------------------------------------------------------------");
//            if (!iterator.hasNext())
//                break;
//            String s = (String) iterator.next();
//            //if(i != 0 && !locationmanager.isProviderEnabled(s))
//            if (i != false && !locationmanager.isProviderEnabled(s))
//                continue;
//            // System.out.println("provider ===> "+s);
//            @SuppressLint("MissingPermission") Location location1 = locationmanager.getLastKnownLocation(s);
//            if (location1 == null)
//                continue;
//            if (location != null) {
//                //System.out.println("location ===> "+location);
//                //System.out.println("location1 ===> "+location);
//                float f = location.getAccuracy();
//                float f1 = location1.getAccuracy();
//                if (f >= f1) {
//                    long l = location1.getTime();
//                    long l1 = location.getTime();
//                    if (l - l1 <= 600000L)
//                        continue;
//                }
//            }
//            location = location1;
//            // System.out.println("location  out ===> "+location);
//            //System.out.println("location1 out===> "+location);
//            i = locationmanager.isProviderEnabled(s);
//            // System.out.println("---------------------------------------------------------------------");
//        } while (true);
//        return location;
//    }

}
