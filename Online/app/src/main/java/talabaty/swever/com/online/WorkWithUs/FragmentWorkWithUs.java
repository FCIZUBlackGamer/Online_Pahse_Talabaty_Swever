package talabaty.swever.com.online.WorkWithUs;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
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
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import talabaty.swever.com.online.LoginDatabase;
import talabaty.swever.com.online.R;
import talabaty.swever.com.online.SwitchNavActivity;
import talabaty.swever.com.online.Utils.AppRepository;
import talabaty.swever.com.online.Utils.AppToastUtil;
import talabaty.swever.com.online.Utils.StringUtil;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;
import static com.android.volley.VolleyLog.TAG;

public class FragmentWorkWithUs extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        android.location.LocationListener,
        GoogleMap.OnMarkerDragListener {
    Button id_open, end_date, save;
    RadioGroup radiogroup;
    Spinner package_id, cat_id, doc, delivery;
    ImageView id_image;
    EditText phone, national_id, name;

    private static final int CAMERA_REQUEST = 1888;
    private int PICK_IMAGE_REQUEST = 1;

    String baseUrl = "http://selltlbaty.rivile.com/";
    private String UPLOAD_URL = baseUrl + "Uploads/UploadAndro";
    private String KEY_IMAGE = "base64imageString";
    private String KEY_NAME = "name";
    //    List<byte[]> bytes;
    // To Get Data
    WorkWithUsModel withUsModel = null;
    Bitmap bitmap;
    List<String> imageStrings;
    List<String> cats, values;
    List<spin> spins, spin_cat;
    boolean packag = false;
    LoginDatabase loginDatabase;
    Cursor cursor;

    DatePickerDialog.OnDateSetListener DatePicker1;
    ProgressDialog progressDialog, progressDialog2, progressDialog3, progressDialog4;

    boolean te = false;
    int im = 0;

    View Camera_view;
    ImageView close, minimize, cam, gal;
    FragmentManager fragmentManager;
    TextView addresss;

    private GoogleMap mMap;
    Location location;

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
    Location mylocation;

    private AppRepository mRepository;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mRepository = AppRepository.getInstance(getActivity().getApplication());

        view = inflater.inflate(R.layout.fragment_work_with_us, container, false);
        radiogroup = view.findViewById(R.id.radiogroup);
        package_id = view.findViewById(R.id.package_id);
        id_open = view.findViewById(R.id.id_open);
        id_image = view.findViewById(R.id.id_image);
        end_date = view.findViewById(R.id.end_date);
        save = view.findViewById(R.id.save);
        phone = view.findViewById(R.id.phone);
        name = view.findViewById(R.id.name);
        national_id = view.findViewById(R.id.national_id);
        cat_id = view.findViewById(R.id.cat_id);
        doc = view.findViewById(R.id.doc);
        addresss = view.findViewById(R.id.address);
        delivery = view.findViewById(R.id.delivery);
        loginDatabase = new LoginDatabase(getActivity());
        fragmentManager = getFragmentManager();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getActivity().getApplicationContext());
        ((SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map)).getMapAsync(this);

        setUpLocation();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        requestStoragePermission();
        loadCategory();

        cats = values = new ArrayList<>();


        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.salver) {
                    package_id.setVisibility(View.VISIBLE);
                    loadPackages();
                    packag = false;
                } else if (checkedId == R.id.gold) {
                    package_id.setVisibility(View.GONE);
                    packag = true;
                }
            }
        });

        radiogroup.check(R.id.gold);
        package_id.setVisibility(View.GONE);
        packag = true;

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
                    mylocation = new Location("");
                    mylocation.setLongitude(latLngLoc.longitude);
                    mylocation.setLatitude(latLngLoc.latitude);
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
                AppToastUtil.showErrorToast(status.toString(),
                        AppToastUtil.LENGTH_LONG, getContext());
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (valid(phone.getText().toString(),
                        national_id.getText().toString(),
                        name.getText().toString())) {
                    cursor = loginDatabase.ShowData();

                    withUsModel = new WorkWithUsModel();
                    if (doc.getSelectedItem().toString().equals("بطاقه هوية شخصيه")) {
                        withUsModel.setPhotoType(1);
                    } else {
                        withUsModel.setPhotoType(2);
                    }
                    withUsModel.setPhone(phone.getText().toString());
                    withUsModel.setCountryId(1);
                    withUsModel.setEndDate(end_date.getText().toString());
                    withUsModel.setIdentityId(Long.parseLong(national_id.getText().toString()));
                    while (cursor.moveToNext()) {
                        withUsModel.setId(Integer.parseInt(cursor.getString(2)));
                    }
                    uploadImage();
                }
            }
        });

        end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth
                        , DatePicker1
                        , year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        DatePicker1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                end_date.setText(year + "/" + month + "/" + dayOfMonth);
            }
        };

        id_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                Camera_view = inflater.inflate(R.layout.camera_view, null);

                close = Camera_view.findViewById(R.id.close);
                minimize = Camera_view.findViewById(R.id.minimize);
                minimize.setVisibility(View.GONE);
                cam = Camera_view.findViewById(R.id.cam);
                gal = Camera_view.findViewById(R.id.gal);

                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(false)
                        .setView(Camera_view);

                final AlertDialog dialog = builder.create();
                dialog.show();

                gal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openGalary();
                        dialog.dismiss();
                    }
                });

                cam.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openCamera();
                        dialog.dismiss();
                    }
                });

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });
            }
        });
    }

    private boolean valid(String ph, String nid, String nam) {
        boolean res = true;
        if (ph.length() != 11) {
            phone.setError("رقم هاتف خطأ");
            res = false;
        }
        if (nid.length() != 14) {
            national_id.setError("رقم قومى خطأ");
            res = false;
        }
        if (nam.length() < 3) {
            name.setError("اسم غير صحيح");
            res = false;
        }
        if (end_date.getText().length() < 1) {
            AppToastUtil.showErrorToast("يرجى ادخال تاريخ صلاحيه",
                    AppToastUtil.LENGTH_LONG, getContext());

            res = false;
        }
        if (im != 1) {
            AppToastUtil.showErrorToast("يرجى ارفاق صوره الهويه الشخصيه",
                    AppToastUtil.LENGTH_LONG, getContext());

            res = false;
        }

        return res;
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

            mylocation = new Location("");
            mylocation.setLongitude(longitude);
            mylocation.setLatitude(latitude);

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

                addresss.setText(addres + "");
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
                AppToastUtil.showErrorToast("device is not supported",
                        AppToastUtil.LENGTH_LONG, getContext());
                getActivity().finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                AppToastUtil.showInfoToast("camera permission granted",
                        AppToastUtil.LENGTH_SHORT, getContext());
            } else {
                AppToastUtil.showWarningToast("camera permission denied",
                        AppToastUtil.LENGTH_LONG, getContext());
            }
        }

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

    private void uploadImage() {

        final Gson gson = new Gson();
        Log.e("Connection UploadImage", imageStrings.size() + "");

        final String allImages = gson.toJson(imageStrings);
        Log.e("Start: ", allImages);
        //Showing the progress dialog
        progressDialog = ProgressDialog.show(getActivity(), "Uploading...", "Please wait...", false, false);

        mRepository.uploadImage(allImages, "Mohamed").observe(this, response -> {
            if (response != null) {
                progressDialog.dismiss();

                if (!response.equals(StringUtil.DISMISS_PROGRESS_DIALOG)) {
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONArray array = object.getJSONArray("Images");
                        for (int x = 0; x < array.length(); x++) {
                            String object1 = array.getString(x);
                            withUsModel.setPhoto(object1);
                        }

                        final String jsonInString = gson.toJson(withUsModel);
                        Log.e("Data", jsonInString);
                        if (packag) {
                            int temp = 0;
                            for (int r = 0; r < spin_cat.size(); r++) {
                                Log.e("R", spin_cat.get(r).getId() + "");
                                if (String.valueOf(spin_cat.get(r).getName()).equals(cat_id.getSelectedItem().toString())) {
                                    temp = spin_cat.get(r).getId();
                                    Log.e("INDEX", temp + "");
                                }
                            }
                            Log.e("Index", temp + "");
                            uploadMontage(jsonInString, 0, name.getText().toString(), temp);
                        } else {
                            int temp = 0;
                            for (int r = 0; r < spins.size(); r++) {
                                Log.e("R", spins.get(r).getId() + "");
                                if (String.valueOf(spins.get(r).getValue()).equals(package_id.getSelectedItem().toString())) {
                                    temp = spins.get(r).getId();
                                    Log.e("INDEX", temp + "");
                                }
                            }
                            Log.e("Index", temp + "");

                            int temp1 = 0;
                            for (int r = 0; r < spin_cat.size(); r++) {
                                Log.e("R", spin_cat.get(r).getId() + "");
                                if (String.valueOf(spin_cat.get(r).getName()).equals(cat_id.getSelectedItem().toString())) {
                                    temp1 = spin_cat.get(r).getId();
                                    Log.e("INDEX", temp1 + "");
                                }
                            }
                            Log.e("Index", temp1 + "");

                            uploadMontage(jsonInString, temp, name.getText().toString(), temp1);
                        }

                    } catch (JSONException e) {
                        Log.e(StringUtil.EXCEPTION_TAG, e.getMessage());
                    }
                }
            }
        });
    }

    private void openGalary() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void openCamera() {
        //Todo: Open Camera

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);

    }

    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to getActivity() block
            //Here you can explain why you need getActivity() permission
            //Explain here why you need getActivity() permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
    }


    //handling the image chooser activity result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri filePath;
        imageStrings = new ArrayList<>();
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                id_image.setImageBitmap(bitmap);
                if (id_image.getDrawable() == null) {
                    imageStrings.add(getStringImage(bitmap));
                    im = 1;
                    Log.e("Image", imageStrings.get(0));
                    Log.e("Length", imageStrings.size() + "");
                } else {
                    imageStrings.add(0, getStringImage(bitmap));
                    im = 1;
                    Log.e("Image", imageStrings.get(0));
                    Log.e("Length", imageStrings.size() + "");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");
            id_image.setImageBitmap(bitmap);
            if (id_image.getDrawable() == null) {
                imageStrings.add(getStringImage(bitmap));
                im = 1;
                Log.e("Image", imageStrings.get(0));
            } else {
                imageStrings.add(0, getStringImage(bitmap));
                im = 1;
                Log.e("Image", imageStrings.get(0));
            }

        }

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                Log.i(TAG, "Place:" + String.valueOf(place));
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {

            }
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    //Todo: Load Packages
    private void loadPackages() {
        values = new ArrayList<>();
        spins = new ArrayList<>();

        if (values.size() > 0) {
            for (int x = 0; x < values.size(); x++) {
                values.remove(x);
            }
        }

        if (spins.size() > 0) {
            for (int x = 0; x < spins.size(); x++) {
                spins.remove(x);
            }
        }

        progressDialog2 = new ProgressDialog(getActivity());
        progressDialog2.setMessage("جارى تحميل البيانات ...");
        progressDialog2.setCancelable(false);
        progressDialog2.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://onlineapi.rivile.com/SubScrip/Package",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            progressDialog2.dismiss();
                            JSONObject object = new JSONObject(response);
                            JSONArray array = object.getJSONArray("List");
                            if (array.length() > 0) {


                                for (int x = 0; x < array.length(); x++) {
                                    JSONObject jsonObject = array.getJSONObject(x);

                                    spin e = new spin(jsonObject.getInt("Id"), jsonObject.getInt("Value"));
                                    spins.add(e);
                                }
//                                Log.e("Length", values.size()+"");

                                for (int s = 0; s < spins.size(); s++) {
                                    values.add(spins.get(s).getValue() + "");
                                    Log.e("RR", spins.get(s).getId() + "");
                                }
                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                                        android.R.layout.simple_spinner_dropdown_item, values);
                                package_id.setAdapter(dataAdapter);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, error -> {

            progressDialog2.dismiss();
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
                2,  // maxNumRetries = 2 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getActivity()).add(stringRequest);

    }

    private void loadCategory() {
        spin_cat = new ArrayList<>();
        cats = new ArrayList<>();

        if (spin_cat.size() > 0) {
            for (int x = 0; x < spin_cat.size(); x++) {
                spin_cat.remove(x);
                cats.remove(x);
            }
        }

        progressDialog3 = new ProgressDialog(getActivity());
        progressDialog3.setMessage("جارى تحميل البيانات ...");
        progressDialog3.setCancelable(false);
        progressDialog3.show();

        mRepository.listCategories().observe(this, response -> {
            if (response != null) {
                progressDialog3.dismiss();

                if (!response.equals(StringUtil.DISMISS_PROGRESS_DIALOG)) {
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONArray array = object.getJSONArray("Fields");
                        if (array.length() > 0) {


                            for (int x = 0; x < array.length(); x++) {
                                JSONObject jsonObject = array.getJSONObject(x);

                                spin e = new spin(jsonObject.getInt("Id"), jsonObject.getString("Name"));
                                spin_cat.add(e);
                            }
//                                Log.e("Length", values.size()+"");

                            for (int s = 0; s < spin_cat.size(); s++) {
                                cats.add(spin_cat.get(s).getName() + "");
                                Log.e("RR", spin_cat.get(s).getId() + "");
                            }
                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                                    android.R.layout.simple_spinner_dropdown_item, cats);
                            cat_id.setAdapter(dataAdapter);

                        }
                    } catch (JSONException e) {
                        Log.e(StringUtil.EXCEPTION_TAG, e.getMessage());
                    }
                }
            }
        });
    }

//    public void startNewActivity(Context context, String packageName) {
//        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
//        if (intent == null) {
//            // Bring user to the market or let them choose an app?
//            intent = new Intent(Intent.ACTION_VIEW);
//            intent.setData(Uri.parse("market://details?id=" + packageName));
//        }
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
//    }

    private void uploadMontage(final String jsonInString, final int PackagesValueId, final String Name, final int CategoryId) {
        Log.e("Connection UploadMontag", "Here");

        te = !delivery.getSelectedItem().toString().equals("بالكيلو");

        withUsModel.setDelivaryType(te);
        withUsModel.setAddress(addres);
        withUsModel.setCategoryId(CategoryId);
        withUsModel.setName(Name);
        if (PackagesValueId != 0) {
            withUsModel.setPackagesValueId(PackagesValueId);
        }
        Gson gson = new Gson();

        final String ebn3rs = gson.toJson(withUsModel);
        Log.e("Full Model", ebn3rs);

        progressDialog4 = ProgressDialog.show(getActivity(), "Uploading...", "Please wait...", false, false);

        mRepository.addShop(ebn3rs).observe(this, response -> {
            if (response != null) {
                progressDialog4.dismiss();

                if (!response.equals(StringUtil.DISMISS_PROGRESS_DIALOG)) {
                    if (response.equals(StringUtil.RESPONSE_SUCCESS)) {
                        AppToastUtil.showInfoToast("تم اضافه المحل بنجاح",
                                AppToastUtil.LENGTH_LONG, getContext());

                        getActivity().startActivity(new Intent(getActivity(), SwitchNavActivity.class));
                    } else {
                        String warningMessage;
                        switch (response) {
                            case "\"الاسم مكرر\"":
                                warningMessage = "اسم جهه عمل مكرر يرجى اختيار اسم جديد و المحاوله مره اخرى";
                                break;
                            case "\"هذا المستخدم مالك لمحل بالفعل \"":
                                warningMessage = "هذا المستخدم مالك لمحل بالفعل";
                                break;
                            case "\"خطاء أثناء الحفظ\"":
                                warningMessage = "خطأ أثناء الحفظ";
                                break;
                            default:
                                warningMessage = "عذرا حدث خطأ أثناء اجراء العملية  .. يرجي المحاوله لاحقا";
                                break;
                        }

                        AppToastUtil.showWarningToast(warningMessage, AppToastUtil.LENGTH_LONG, getContext());
                    }
                }
            }
        });
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
        if (progressDialog3 != null && progressDialog3.isShowing()) {
            progressDialog3.dismiss();
        }
        if (progressDialog4 != null && progressDialog4.isShowing()) {
            progressDialog4.dismiss();
        }
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
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {
        LatLng latLng = marker.getPosition();
//                lat = latLng.latitude;
//                lon = latLng.longitude;
        AppToastUtil.showInfoToast("Lat " + latLng.latitude + " "
                        + "Long " + latLng.longitude,
                AppToastUtil.LENGTH_SHORT, getContext());

        Log.e("Lat1 ", latLng.latitude + " ");
        Log.e("Long1 ", latLng.longitude + " ");
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);
        mylocation = new Location("");
        mylocation.setLongitude(latLng.longitude);
        mylocation.setLatitude(latLng.latitude);
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

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
//                AppToastUtil.makeText(
//                        getActivity(),
//                        "Lat " + latLng.latitude + " "
//                                + "Long " + latLng.longitude,
//                        AppToastUtil.LENGTH_LONG).show();
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

    public void showSettingsAlert() {
        androidx.appcompat.app.AlertDialog.Builder alertDialog = new androidx.appcompat.app.AlertDialog.Builder(getActivity());

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
}
