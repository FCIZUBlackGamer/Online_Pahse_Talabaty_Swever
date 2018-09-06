package talabaty.swever.com.online.Fields.MostViewed;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.FadeInDownAnimator;
import talabaty.swever.com.online.R;
import talabaty.swever.com.online.SubCategory.ContactAdapter;
import talabaty.swever.com.online.Switch_nav;

import static android.content.Context.LOCATION_SERVICE;

public class FragmentMostViewed extends Fragment implements LocationListener {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<Contact> contacts;

    int temp_first, temp_last;
    TextView next, num, last;
    int item_num, page_num;

//Todo: Delete All Page And ReWrite Code To Get Location City Name +(Load Contact)
    private LocationManager locationManager;
    private String provider;


    static String Type = "null";
    String Link;

    public FragmentMostViewed setType(String type) {
        FragmentMostViewed trend = new FragmentMostViewed();
        Type = type;
        return trend;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        View view = inflater.inflate(R.layout.fragment_home_most_viewed, container, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rec);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new FadeInDownAnimator(new OvershootInterpolator(1f)));
        contacts = new ArrayList<>();
        next = view.findViewById(R.id.next);
        last = view.findViewById(R.id.previous);
        num = view.findViewById(R.id.item_num);
        item_num = page_num = 0;
        num.setText(1 + "");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
//        locationManager.requestLocationUpdates(provider, 400, 1, getActivity());
    }


    @Override
    public void onPause() {
        super.onPause();
//        locationManager.removeUpdates(this);
    }

    @Override
    public void onStart() {
        super.onStart();
//        getMyCurrentLocation();
//
//        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//
//        Criteria criteria = new Criteria();
//        provider = locationManager.getBestProvider(criteria, false);
//        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        Location location = locationManager.getLastKnownLocation(provider);


//        if (location != null) {
//            System.out.println("Provider " + provider + " has been selected.");
//            onLocationChanged(location);
//        } else {
//            Log.e("Key","Location not available");
//        }
        temp_first = 0;
        temp_last = 10;
        page_num = 0;

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Item Num", item_num + "");
                if (contacts.size() == 80) {
                    loadContact(item_num, "1");
                } else {
                    Snackbar.make(v, "نهايه جهات العمل", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (page_num > 1) {
                    Log.e("Item Num", item_num + "");
                    loadContact(item_num, "0");
                } else {
                    Snackbar.make(v, "بدايه جهات العمل", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        showSettingsAlert();
        LocationManager manager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                //code when location changed
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//                lat=location.getLatitude();
//                lon=location.getLatitude();
//                Toast.makeText(
//                        getActivity(),
//                        "Lat " + latLng.latitude + " "
//                                + "Long " + latLng.longitude,
//                        Toast.LENGTH_LONG).show();
                Log.e("Lat ", latLng.latitude + " ");
                Log.e("Long ", latLng.longitude + " ");

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        //Todo: Edit Links To Be For Contact Not Products (Also Add LoadData() To Load Contact)
        if (Type.equals("trend")) {
            ((Switch_nav) getActivity())
                    .setActionBarTitle("الأكثر زيارة");
            Link = "http://onlineapi.sweverteam.com/shops/MostVisited/list";
        }else {
            ((Switch_nav) getActivity())
                    .setActionBarTitle("جهات العمل ");
            Link = "http://onlineapi.sweverteam.com/shops/list";
        }

//        for (int x=0; x<20; x++){
//
//            Contact contact = new Contact(x,"Apple",4,"Zagazig El-Zraa","zozo_fofa@yahoo.com","123456789","","");
//            contacts.add(contact);
//        }

        loadContact(0, "1");
    }

    private void loadContact(final int x, final String type) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("جارى تحميل البيانات ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Link,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject object = new JSONObject(response);
                            JSONArray array = object.getJSONArray("List");
                            if (array.length() > 0) {
                                final int size = contacts.size();
                                if (size > 0) {
                                    for (int i = 0; i < size; i++) {
                                        contacts.remove(0);
                                        adapter.notifyItemRemoved(i);
                                    }

                                }

                                for (int x = 0; x < array.length(); x++) {
                                    JSONObject object1 = array.getJSONObject(x);

                                    Contact info = new Contact(
                                            object1.getInt("Id"),
                                            object1.getString("Name"),
                                            (float) object1.getDouble("Rate"),
                                            object1.getString("Address"),
                                            object1.getString("Email"),
                                            object1.getString("Phone"),
                                            "http://selltlbaty.sweverteam.com" + object1.getString("Photo"),
                                            ""
                                    );
                                    contacts.add(info);

                                }

                                adapter = new MostViewedAdapter(getActivity(),contacts);
                                AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter);
                                alphaAdapter.setDuration(3000);
                                recyclerView.setAdapter(adapter);

                            } else {
                                Toast toast = Toast.makeText(getActivity(), "لا توجد جهات عمل جديده", Toast.LENGTH_SHORT);
                                TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                                v.setTextColor(Color.GREEN);
                                toast.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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
                map.put("count", 80 + "");
                map.put("type", type + "");
                map.put("x", x + "");
                map.put("token", "?za[ZbGNz2B}MXYZ");
                return map;
            }
        };
        progressDialog.dismiss();
//        Volley.newRequestQueue(getActivity()).add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                2,  // maxNumRetries = 2 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getActivity()).add(stringRequest);
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
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                dialog.cancel();
            }
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

    @Override
    public void onLocationChanged(Location location) {
        //You had this as int. It is advised to have Lat/Loing as double.
        double lat = location.getLatitude();
        double lng = location.getLongitude();

        Geocoder geoCoder = new Geocoder(getActivity(), Locale.getDefault());
        StringBuilder builder = new StringBuilder();
        try {
            List<Address> address = geoCoder.getFromLocation(lat, lng, 1);
            int maxLines = address.get(0).getMaxAddressLineIndex();
            for (int i=0; i<maxLines; i++) {
                String addressStr = address.get(0).getAddressLine(i);
                builder.append(addressStr);
                builder.append(" ");
            }

            String fnialAddress = builder.toString(); //This is the complete address.

            Log.e("Lat",String.valueOf(lat));
            Log.e("Lon",String.valueOf(lng));
            Log.e("Address",fnialAddress); //This will display the final address.

        } catch (IOException e) {
            // Handle IOException
        } catch (NullPointerException e) {
            // Handle NullPointerException
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(getActivity(), "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(getActivity(), "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

//    @SuppressLint("MissingPermission")
//    void getMyCurrentLocation() {
//
//
//        LocationManager locManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//        LocationListener locListener = new MyLocationListener();
//
//
//        try{gps_enabled=locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);}catch(Exception ex){}
//        try{network_enabled=locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);}catch(Exception ex){}
//
//        //don't start listeners if no provider is enabled
//        //if(!gps_enabled && !network_enabled)
//        //return false;
//
//        if(gps_enabled){
//            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
//
//        }
//
//
//        if(gps_enabled){
//            location=locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//
//
//        }
//
//
//        if(network_enabled && location==null){
//            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);
//
//        }
//
//
//        if(network_enabled && location==null)    {
//            location=locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
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
//            Location loc= getLastKnownLocation(getActivity());
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
//        try
//        {
//// Getting address from found locations.
//            Geocoder geocoder;
//
//            List<Address> addresses;
//            geocoder = new Geocoder(getActivity(), Locale.getDefault());
//            addresses = geocoder.getFromLocation(MyLat, MyLong, 1);
//
//            StateName= addresses.get(0).getAdminArea();
//            CityName = addresses.get(0).getLocality();
//            CountryName = addresses.get(0).getCountryName();
//            // you can get more details other than this . like country code, state code, etc.
//
//
//          /*  Toast.makeText(this," StateName " + StateName+"\n"+
//                    " CityName " + CityName+"\n"+
//                    " CountryName " + CountryName,Toast.LENGTH_LONG).show();
//                    */
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//
//        Toast.makeText(getActivity(),""+MyLat+"\n"+
//                MyLong+"\n StateName " + StateName +
//                "\n CityName " + CityName +"\n CountryName "
//                + CountryName, Toast.LENGTH_LONG).show();
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
//    public static Location getLastKnownLocation(Context context)
//    {
//        Location location = null;
//        @SuppressLint("WrongConstant") LocationManager locationmanager = (LocationManager)context.getSystemService("location");
//        List list = locationmanager.getAllProviders();
//        boolean i = false;
//        Iterator iterator = list.iterator();
//        do
//        {
//            //System.out.println("---------------------------------------------------------------------");
//            if(!iterator.hasNext())
//                break;
//            String s = (String)iterator.next();
//            //if(i != 0 && !locationmanager.isProviderEnabled(s))
//            if(i != false && !locationmanager.isProviderEnabled(s))
//                continue;
//            // System.out.println("provider ===> "+s);
//            @SuppressLint("MissingPermission") Location location1 = locationmanager.getLastKnownLocation(s);
//            if(location1 == null)
//                continue;
//            if(location != null)
//            {
//                //System.out.println("location ===> "+location);
//                //System.out.println("location1 ===> "+location);
//                float f = location.getAccuracy();
//                float f1 = location1.getAccuracy();
//                if(f >= f1)
//                {
//                    long l = location1.getTime();
//                    long l1 = location.getTime();
//                    if(l - l1 <= 600000L)
//                        continue;
//                }
//            }
//            location = location1;
//            // System.out.println("location  out ===> "+location);
//            //System.out.println("location1 out===> "+location);
//            i = locationmanager.isProviderEnabled(s);
//            // System.out.println("---------------------------------------------------------------------");
//        } while(true);
//        return location;
//    }
}
