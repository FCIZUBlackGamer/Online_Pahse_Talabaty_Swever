package talabaty.swever.com.online.ContactUs;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import talabaty.swever.com.online.R;
import talabaty.swever.com.online.Utils.AppRepository;
import talabaty.swever.com.online.Utils.AppToastUtil;
import talabaty.swever.com.online.Utils.StringUtil;

public class FragmentContactUs extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    TextView desc, address, phone, site, fb, titter, youtube, gplus, insta;
    EditText title, name, content, email;
    Button send;
    ProgressDialog progressDialog, progressDialog2;

    private AppRepository mRepository;

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_contact_us);
//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//
//    }

    public static FragmentContactUs newInstance() {
        FragmentContactUs fragment = new FragmentContactUs();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mRepository = AppRepository.getInstance(getActivity().getApplication());

        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        desc = view.findViewById(R.id.desc);
        address = view.findViewById(R.id.address);
        phone = view.findViewById(R.id.phone);
        site = view.findViewById(R.id.website);
        fb = view.findViewById(R.id.fb);
        titter = view.findViewById(R.id.twitter);
        youtube = view.findViewById(R.id.youtube);
        gplus = view.findViewById(R.id.gplus);
        insta = view.findViewById(R.id.insta);
        title = view.findViewById(R.id.message_title);
        name = view.findViewById(R.id.name);
        content = view.findViewById(R.id.message_content);
        email = view.findViewById(R.id.email);
        send = view.findViewById(R.id.send);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadDetails();


        send.setOnClickListener(v -> {
            if (valiate(name.getText().toString(),
                    title.getText().toString(),
                    email.getText().toString(),
                    content.getText().toString())) {

                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("جارى تحميل البيانات ...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                mRepository.contactUsWithSocial(name.getText().toString(), email.getText().toString(),
                        title.getText().toString(), content.getText().toString())
                        .observe(this, response -> {
                            if (response != null) {
                                progressDialog.dismiss();

                                if (!response.equals(StringUtil.DISMISS_PROGRESS_DIALOG)) {
                                    if (response.equals(StringUtil.RESPONSE_SUCCESS)) {
                                        AppToastUtil.showInfoToast("تم الإرسال بنجاح",
                                                AppToastUtil.LENGTH_LONG, getContext());
                                    } else {
                                        AppToastUtil.showWarningToast("عذرا حدث خطأ ما يرجى الابلاغ عنه والمحاوله لاحقا",
                                                AppToastUtil.LENGTH_LONG, getContext());
                                    }
                                }
                            }
                        });
            }
        });

        phone.setOnClickListener(v -> {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phone.getText().toString()));
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.CALL_PHONE)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CALL_PHONE},
                        10);

                // MY_PERMISSIONS_REQUEST_CALL_PHONE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            } else {
                //You already have permission
                try {
                    startActivity(callIntent);
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            }
            startActivity(callIntent);
        });

        site.setOnClickListener(v -> {
            Intent email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{site.getText().toString()});
            email.setType("message/rfc822");
            startActivity(Intent.createChooser(email, "Choose an Email client :"));
        });
        fb.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fb.getText().toString()));
            startActivity(browserIntent);
        });
        titter.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(titter.getText().toString()));
            startActivity(browserIntent);
        });
        youtube.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtube.getText().toString()));
            startActivity(browserIntent);
        });
        gplus.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(gplus.getText().toString()));
            startActivity(browserIntent);
        });

        /** Still Insta */
    }

    private boolean valiate(String nam, String titl, String emai, String conten) {
        boolean res = true;
        if (nam.length() < 3) {
            name.setError("ادخل اسم صحيح");
            res = false;
        }
        if (titl.length() < 3) {
            title.setError("ادخل عنوان صحيح");
            res = false;
        }
        if (emai.length() < 4 || !emai.contains("@") || !emai.contains(".")) {
            email.setError("ادخل بريد الكترونى صحيح");
            res = false;
        }
        if (conten.length() < 3) {
            content.setError("ادخل محتوى الرساله ");
            res = false;
        }
        return res;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(30.594768, 31.482765);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker at Swever"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 17.5f));

    }

    private void loadDetails() {
        progressDialog2 = new ProgressDialog(getActivity());
        progressDialog2.setMessage("جارى تحميل البيانات ...");
        progressDialog2.setCancelable(false);
        progressDialog2.show();

        mRepository.contactUsWithSocial().observe(this, response -> {
            if (response != null) {
                progressDialog2.dismiss();

                if (!response.equals(StringUtil.DISMISS_PROGRESS_DIALOG)) {
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONObject array = object.getJSONObject("list");
                        if (array.length() > 0) {

                            address.setText(array.getString("Location"));
                            phone.setText(array.getString("Phone"));
                            site.setText(array.getString("Mail"));
                            desc.setText(array.getString("Descripation"));
                            fb.setText(array.getString("Facebook"));
                            titter.setText(array.getString("Twiter"));
                            youtube.setText(array.getString("Youtube"));
                            gplus.setText(array.getString("Googleplus"));

                        }
                    } catch (JSONException e) {
                        Log.e(StringUtil.EXCEPTION_TAG, e.getMessage());
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
    }
}
