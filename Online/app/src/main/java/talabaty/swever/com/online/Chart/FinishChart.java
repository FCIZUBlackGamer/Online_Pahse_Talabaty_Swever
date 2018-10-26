package talabaty.swever.com.online.Chart;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.itextpdf.text.pdf.BarcodeEAN;
import com.itextpdf.text.pdf.PdfContentByte;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;
import talabaty.swever.com.online.R;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;
import static com.android.volley.VolleyLog.TAG;

import talabaty.swever.com.online.Chart.Models.*;
import talabaty.swever.com.online.FinalBell.*;
import talabaty.swever.com.online.*;

//talabaty-213109
public class FinishChart extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        android.location.LocationListener,
        GoogleMap.OnMarkerDragListener {

    private GoogleMap mMap;
    TextView total;
    Button conf;
    Cursor cursor, cursorUserId;
    ChartDatabase chartDatabase;
    ChartAdditionalDatabase chartAdditionalDatabase;
    List<ChartModel> modelList;
    Location location;

    LoginDatabae loginDatabae ;
    String userId;

    private static final int PERMISSION_REQUEST_CODE = 7001;
    private static final int PLAY_SERVICE_REQUEST = 7002;

    private static final int UPDATE_INTERVAL = 5000;//5 detik
    private static final int FASTEST_INTERVAL = 3000;//3detik
    private static final int DISPLACEMENT = 10;

    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;

    private PlaceAutocompleteFragment placeAutocompleteFragment;

    Marker marker;
    String addres, city, region, state;
    View view;

    List<Bell> bellList;
    Location first, last;
    ProgressDialog progressDialog;
    LatLng latLng;
    ChartModel model;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

//        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        view = inflater.inflate(R.layout.dialog_chart_buy, container, false);

        Log.e("Fragment", "Found");
        chartDatabase = new ChartDatabase(getActivity());
        chartAdditionalDatabase = new ChartAdditionalDatabase(getActivity());
        cursor = chartDatabase.ShowData();
        modelList = new ArrayList<>();

        loginDatabae = new LoginDatabae(getActivity());
        cursorUserId = loginDatabae.ShowData();
        while (cursorUserId.moveToNext()){
            userId = cursorUserId.getString(2);
        }

//        BarcodeEAN codeEAN = new BarcodeEAN();
//        codeEAN.setCodeType(codeEAN.EAN13);
//        codeEAN.setCode("9780201615883");
//        PdfContentByte cb;
//        Image imageEAN = codeEAN.createImageWithBarcode(cb, null, null);

        total = view.findViewById(R.id.total);
        conf = view.findViewById(R.id.send);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getActivity().getApplicationContext());
        ((SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map)).getMapAsync(this);

        setUpLocation();

        bellList = new ArrayList<>();
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();


        placeAutocompleteFragment = (PlaceAutocompleteFragment) getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

//        placeAutocompleteFragment.setFilter(new AutocompleteFilter.Builder().setCountry("ID").build());

        placeAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(getActivity(), Locale.getDefault());

                final LatLng latLngLoc = place.getLatLng();

//                Log.e("Lat2 ", latLngLoc.latitude + " ");
//                Log.e("Long2 ", latLngLoc.longitude + " ");

                if (!String.valueOf(latLngLoc.latitude).isEmpty() || !String.valueOf(latLngLoc.longitude).isEmpty()) {
                    first = new Location("");
                    first.setLongitude(latLngLoc.longitude);
                    first.setLatitude(latLngLoc.latitude);
                    try {
                        addresses = geocoder.getFromLocation(latLngLoc.latitude, latLngLoc.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                        String address = addresses.get(0).getAddressLine(0);
                        String cit = addresses.get(0).getLocality();
                        String stat = addresses.get(0).getAdminArea();
                        String countr = addresses.get(0).getCountryName();
//                    String postalCode = addresses.get(0).getPostalCode();
                        String knownNam = addresses.get(0).getFeatureName();
                        addres = address;
                        city = cit;
                        state = stat;
                        region = knownNam;
                        Snackbar.make(
                                view, addres,
                                Snackbar.LENGTH_LONG).show();
//                    if (city != null)
                        Log.e("city ", cit + "");
                        Log.e("state ", stat + "");
                        Log.e("country ", countr + "");
                        Log.e("knownName ", knownNam + "");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (marker != null) {
                        marker.remove();

                    }
                    mMap.clear();
                    marker = mMap.addMarker(new MarkerOptions().position(latLngLoc).title(place.getName().toString()));
//                mMap.animateCamera(CameraUpdateFactory.zoomTo(12), 2000, null);
//                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngLoc, 12.0f));
                    CameraUpdate location = CameraUpdateFactory.newLatLngZoom(
                            latLngLoc, 15);
                    mMap.animateCamera(location);
                }
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(getActivity(), "" + status.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


        int tota = 0;
        while (cursor.moveToNext()) {
            tota += Float.parseFloat(cursor.getString(7));
        }
        total.setText(tota + "");

//        capital = confirm.findViewById(R.id.capital);
//        city = confirm.findViewById(R.id.city);
//        region = confirm.findViewById(R.id.region);
//        ignore = confirm.findViewById(R.id.close);
//        delivery_placesList = delivery_optionsList = indexOfdelivery_placesList = indexOfdelivery_optionsList = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    1);
        }

        conf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo: Buy Products
                cursor = chartDatabase.ShowData();
                while (cursor.moveToNext()) {
                    model = new ChartModel();
                    model.setId((int)Float.parseFloat(cursor.getString(12)));
                    Log.e("ID",model.getId()+"");
                    try {
                        model.setSize(Integer.parseInt(cursor.getString(4)));
                        model.setColor(Integer.parseInt(cursor.getString(3)));
                    }catch (Exception e){
                        Log.e("Color&Size", e.getMessage());
                    }
                    model.setName(cursor.getString(1));
                    model.setImage("");
                    model.setIsOffer((int)Float.parseFloat(cursor.getString(14)));
                    Log.e("IsOfferrr",model.getIsOffer()+"");
                    if (model.getIsOffer() == 2){
                        int id = model.getId();
                        List<AdditionalModel> list = new ArrayList<>();
                        Log.e("FinishCartId",id+"");
                        chartAdditionalDatabase = new ChartAdditionalDatabase(getActivity());
                        Cursor curso = chartAdditionalDatabase.ShowData(id+"");
                        while (curso.moveToNext()){
                            list.add(new AdditionalModel(curso.getString(2),
                                    curso.getString(3),
                                    curso.getString(4)));
                        }
                        model.setAdditionList(list);
                        Log.e("Model.Size()",model.getAdditionList().size()+"");
                    }

                    model.setPrice(Double.parseDouble(cursor.getString(7)));
                    model.setAmount((int)Float.parseFloat(cursor.getString(5)));

                    String temp_address = cursor.getString(11);
                    Log.e("LOOOOOO",temp_address+" ");
                    if (!temp_address.isEmpty()) {

                        String url = "https://maps.googleapis.com/maps/api/geocode/json?address="
                                + Uri.encode(temp_address) + "&sensor=true&key=AIzaSyB9SAb6LxefQVLS3h-0I0mIhMaw6SwDHzI";
                        Log.e("Address", url);

                        StringRequest stateReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject location = new JSONObject(response);
                                    // Get JSON Array called "results" and then get the 0th
                                    // complete object as JSON
                                    location = location.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
//                                    Log.e("LOCATION", String.valueOf(location));
                                    // Get the value of the attribute whose name is
                                    // "formatted_string"
                                    if (location.getDouble("lat") != 0 && location.getDouble("lng") != 0) {
                                        latLng = new LatLng(location.getDouble("lat"), location.getDouble("lng"));
//                                        Log.e("latLng.lat",latLng.latitude+"");
//                                        Log.e("latLng.lon",latLng.longitude+"");
                                        //Do what you want

                                        last = new Location("gps");
//                                        Log.e("locatio.latitude", latLng.latitude+"");
//                                        Log.e("locatio.longitude", latLng.longitude+"");

                                        try {
                                            last.setLatitude(latLng.latitude);
                                            last.setLongitude(latLng.longitude);
                                            double distanceInMeters = first.distanceTo(last);
                                            model.setDistance(distanceInMeters);
                                        } catch (Exception e) {
                                             Log.e("Error", e.getMessage());
                                        }
                                        modelList.add(model);
                                        Date date = new Date();
//                System.out.println(formatter.format(date));
                                        Gson gson = new Gson();
                                        String mod = gson.toJson(modelList);
                                        Log.e("Model", gson.toJson(mod));
                                        uploadChart(mod, addres, region, city, state, date, modelList);

                                    }
                                } catch (JSONException e1) {
                                    Log.e("xxxxxxxxxxxx",e1.getMessage()+"");

                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("yyyyyyyyyyyyyyyyyyy",error.getMessage()+"");
                            }
                        });
                        // add it to the queue
                        stateReq.setRetryPolicy(new DefaultRetryPolicy(
                                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                                2,  // maxNumRetries = 2 means no retry
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        Volley.newRequestQueue(getActivity()).add(stateReq);
                    }else {
                        Log.e("Address","No Address");
                    }
                    //LatLng locatio = getLocationFromAddress(getActivity(), temp_address);

                }
//                SimpleDateFormat formatter = new SimpleDateFormat("YYYY/MM/dd hh:mm:ss");

            }
        });
    }

    public LatLng getLocationFromAddress(Context context, String strAddress) {

        if (!strAddress.isEmpty()) {

            String url = "https://maps.googleapis.com/maps/api/geocode/json?address="
                    + Uri.encode(strAddress) + "&sensor=true&key=AIzaSyB9SAb6LxefQVLS3h-0I0mIhMaw6SwDHzI";
            Log.e("Address", url);

            StringRequest stateReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject location = new JSONObject(response);
                        // Get JSON Array called "results" and then get the 0th
                        // complete object as JSON
                        location = location.getJSONArray("result").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
                        Log.e("LOCATION", String.valueOf(location));
                        // Get the value of the attribute whose name is
                        // "formatted_string"
                        if (location.getDouble("lat") != 0 && location.getDouble("lng") != 0) {
                            latLng = new LatLng(location.getDouble("lat"), location.getDouble("lng"));
                            Log.e("latLng.lat",latLng.latitude+"");
                            Log.e("latLng.lon",latLng.longitude+"");
                            //Do what you want
                        }
                    } catch (JSONException e1) {
                        Log.e("xxxxxxxxxxxx",e1.getMessage()+"");

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("yyyyyyyyyyyyyyyyyyy",error.getMessage()+"");
                }
            });
            // add it to the queue
            stateReq.setRetryPolicy(new DefaultRetryPolicy(
                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                    2,  // maxNumRetries = 2 means no retry
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(getActivity()).add(stateReq);
        }
//        Geocoder coder = new Geocoder(context);
//        List<Address> address;
//        LatLng p1 = null;
//
//        try {
//            // May throw an IOException
//            address = coder.getFromLocationName(strAddress, 5);
//            if (address == null) {
//                return null;
//            }
//
//            Address location = address.get(0);
//            p1 = new LatLng(location.getLatitude(), location.getLongitude());
//
//        } catch (IOException ex) {
//
//            ex.printStackTrace();
//        }
//
//        return p1;

        return latLng;
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //autocompleteFragment.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                Log.i(TAG, "Place:" + place.toString());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {

            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setUpMapIfNeeded(googleMap);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(0, 0), 12));

//        mMap.setMyLocationEnabled(true);
//        mMap.setOnMarkerDragListener(this);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    showSettingsAlert();
                }
            }
        });

        LatLng sydney = new LatLng(23.8917436, 45.0951262);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("مكانك"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 12.0f));


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
                if (marker != null) {
                    marker.remove();

                }
//                mMap.clear();
//                mMap.addMarker(new MarkerOptions().position(latLng).title("مكانك"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
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
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        } else {
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, listener);
            mMap.setMyLocationEnabled(true);
        }
    }

    private void setUpMapIfNeeded(GoogleMap googleMap) {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = googleMap;

            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mMap.setMyLocationEnabled(true);
            // Check if we were successful in obtaining the map.
            if (mMap != null) {


                mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                    @Override
                    public void onMyLocationChange(Location arg0) {
                        // TODO Auto-generated method stub
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(arg0.getLatitude(), arg0.getLongitude()))
                                .title("مكانك")
                                .draggable(true)
                                .icon(BitmapDescriptorFactory
                                        .defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                    }
                });

            }
        } else if (mMap != null) {

            try {
                mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                    @Override
                    public void onMyLocationChange(Location arg0) {
                        // TODO Auto-generated method stub
                        mMap.clear();
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(arg0.getLatitude(), arg0.getLongitude()))
                                .title("موقعك")
                                .draggable(true)
                                .icon(BitmapDescriptorFactory
                                        .defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                    }
                });
            } catch (Exception e) {

            }


        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }

    }

    @SuppressLint("NewApi")
    private void uploadChart(final String mod, final String addres, final String Region, final String City, final String State, final Date date, final List<ChartModel> models) {

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("جارى حجز الطلب ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://onlineapi.rivile.com/Order/OrderPreview",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        Log.e("Response", response);
                        try {
                            //Todo: Still Just Test And Retrieve Data
                            JSONObject object = new JSONObject(response);
                            JSONArray array = object.getJSONArray("List");
                            if (array.length() > 0) {


                                /*string token, string Order, string Address, string Region, string City, string State, int UserId , string Date*/
                                for (int x = 0; x < array.length(); x++) {
                                    JSONObject b = array.getJSONObject(x);
                                    Bell bell = new Bell();
                                    /** Get Bell Info*/
                                    bell.setId(b.getInt("Id"));
                                    bell.setBarcode(b.getString("Barcode"));
                                    bell.setTotalPrice(b.getDouble("TotalPrice"));
                                    bell.setPhone(b.getString("Phone"));
                                    bell.setChargeValue(b.getDouble("ChargeValue"));
                                    bell.setAddress(b.getString("Address"));
                                    bell.setContact_name(b.getString("ShopName"));
                                    /** Get Products Info for one bell */
                                    JSONArray OperOrder = new JSONArray(b.getString("OperOrder"));
                                    String Amount = "";
                                    String Price = "";
                                    String name = "";
                                    if (OperOrder.length() > 0) {
                                        List<Double> AmountValues, PriceValues;
                                        List<String> SaleType, Sale;
                                        Sale = SaleType = new ArrayList<>();
                                        AmountValues = PriceValues = new ArrayList<>();
                                        for (int i = 0; i < OperOrder.length(); i++) {
                                            JSONObject object2 = OperOrder.getJSONObject(i);

                                            name += object2.getString("Name") + "\n";
                                            Amount += object2.getInt("Amount") + "\n";
                                            Price += object2.getDouble("Price") + "\n";
                                            try {
                                                Sale.add(b.getString("Sale"));
                                            }catch (Exception e){
                                                Sale.add(null);
                                            }

                                            try {
                                                SaleType.add(b.getString("SaleType"));
                                            }catch (Exception e){
                                                SaleType.add(null);
                                            }

                                            double temp_amount= object2.getInt("Amount");
                                            double temp_price= object2.getDouble("Price");
                                            AmountValues.add(temp_amount);
                                            PriceValues.add(temp_price);

                                        }
                                        bell.setAmountValues(AmountValues);
                                        bell.setPriceValues(PriceValues);
                                        bell.setSaleType(SaleType);
                                        bell.setSale(Sale);
                                    }

                                    bell.setName(name);
                                    bell.setPrice(Price);
                                    bell.setAmount(Amount);

                                    bellList.add(bell);
                                }


                                Intent intent = new Intent(getActivity() ,FinalBell_Activity.class);
                                intent.putExtra("model",(Serializable) bellList);
                                intent.putExtra("mod",(Serializable) models);
                                intent.putExtra("Address", addres + "");
                                intent.putExtra("Region", Region + "");
                                intent.putExtra("City", City + "");
                                intent.putExtra("State", State + "");
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View layout = inflater.inflate(R.layout.toast_warning,null);

                TextView text = (TextView) layout.findViewById(R.id.txt);

                if (error instanceof ServerError)
                    text.setText("خطأ فى الاتصال بالخادم");
                else if (error instanceof TimeoutError)
                    text.setText("خطأ فى مدة الاتصال");
                else if (error instanceof NetworkError)
                    text.setText("شبكه الانترنت ضعيفه حاليا");

                Toast toast = new Toast(getActivity());
                toast.setGravity(Gravity.BOTTOM, 0, 0);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(layout);
                toast.show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("token", "?za[ZbGNz2B}MXYZ");
                map.put("Order", mod + "");
                map.put("Address", addres + "");
                Log.e("Adress", addres + "");
                map.put("Region", Region + "");
                Log.e("Region", Region + "");
                map.put("City", City + "");
                Log.e("City", City + "");
                map.put("State", State + "");
                Log.e("State", State + "");
                //Log.e("Id", userId.getString(2));
                map.put("UserId",userId+"");
                map.put("Date", new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(date) + "");
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

    private void setUpLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, PERMISSION_REQUEST_CODE);
        } else {
            if (checkPlayServices()) {
                buildGoogleApiClient();
                createLocationRequest();
                displayLocation();
            }
        }
    }

    private void displayLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLocation != null) {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(getActivity(), Locale.getDefault());
            final double latitude = mLocation.getLatitude();
            final double longitude = mLocation.getLongitude();

            first = new Location("");
            first.setLongitude(longitude);
            first.setLatitude(latitude);

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                String address = addresses.get(0).getAddressLine(0);
                String cit = addresses.get(0).getLocality();
                String stat = addresses.get(0).getAdminArea();
                String countr = addresses.get(0).getCountryName();
//                    String postalCode = addresses.get(0).getPostalCode();
                String knownNam = addresses.get(0).getFeatureName();
                addres = address;
                city = cit;
                state = stat;
                region = knownNam;
                Snackbar.make(
                        view, addres,
                        Snackbar.LENGTH_LONG).show();
//                    if (city != null)
                Log.e("city ", cit + "");
                Log.e("state ", stat + "");
                Log.e("country ", countr + "");
                Log.e("knownName ", knownNam + "");
            } catch (IOException e) {
                e.printStackTrace();
            }

            //show marker
            mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("موقعك"));
            //Animate camera to your position
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15.0f));
        }
    }

    @SuppressLint("RestrictedApi")
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(), PLAY_SERVICE_REQUEST).show();
            else {
                Toast.makeText(getActivity(), "getActivity() device is not supported", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkPlayServices()) {
                        buildGoogleApiClient();
                        createLocationRequest();
                        displayLocation();
                    }
                }
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            displayLocation();
        } catch (Exception e) {

        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
        displayLocation();
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

    @Override
    public void onMarkerDragStart(Marker marker) {
        Toast.makeText(getActivity(), "Dragging Start",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        LatLng latLng = marker.getPosition();
//                lat = latLng.latitude;
//                lon = latLng.longitude;
        Toast.makeText(
                getActivity(),
                "Lat " + latLng.latitude + " "
                        + "Long " + latLng.longitude,
                Toast.LENGTH_LONG).show();
        Log.e("Lat1 ", latLng.latitude + " ");
        Log.e("Long1 ", latLng.longitude + " ");
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);
        first = new Location("");
        first.setLongitude(latLng.longitude);
        first.setLatitude(latLng.latitude);
//        mMap.clear();
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        Toast.makeText(getActivity(), "Dragging End",
                Toast.LENGTH_SHORT).show();
    }
}
