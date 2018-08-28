package talabaty.swever.com.online.ContactUs;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import talabaty.swever.com.online.CategoryModel;
import talabaty.swever.com.online.R;
import talabaty.swever.com.online.Switch_nav;

public class FragmentContactUs extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    TextView desc, address, phone, site, fb, titter, youtube, gplus, insta;
    EditText title, name, content, email;
    Button send;

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

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("جارى تحميل البيانات ...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://onlineapi.sweverteam.com/contact/SendMail",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                progressDialog.dismiss();
                                if (response.equals("\"success\"")){
                                    Toast.makeText(getActivity(),"تم الإرسال بنجاح",Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(getActivity(),"عذرا حدث خطأ ما يرجى الابلاغ عنه والمحاوله لاحقا",Toast.LENGTH_SHORT).show();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
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
                        map.put("token", "?za[ZbGNz2B}MXYZ");
                        map.put("name", name.getText().toString());
                        map.put("email", email.getText().toString());
                        map.put("subject", title.getText().toString());
                        map.put("message", content.getText().toString());
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
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

        site.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent email = new Intent(android.content.Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{site.getText().toString()});
                email.setType("message/rfc822");
                startActivity(Intent.createChooser(email, "Choose an Email client :"));
            }
        });
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fb.getText().toString()));
                startActivity(browserIntent);
            }
        });
        titter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(titter.getText().toString()));
                startActivity(browserIntent);
            }
        });
        youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtube.getText().toString()));
                startActivity(browserIntent);
            }
        });
        gplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(gplus.getText().toString()));
                startActivity(browserIntent);
            }
        });

        /** Still Insta */
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
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("جارى تحميل البيانات ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://onlineapi.sweverteam.com/contact/SocialMedia",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
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
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
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



}
