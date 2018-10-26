package talabaty.swever.com.online.FinalBell;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;
import talabaty.swever.com.online.Chart.ChartAdditionalDatabase;
import talabaty.swever.com.online.Chart.ChartDatabase;
import talabaty.swever.com.online.Chart.ChartModel;
import talabaty.swever.com.online.Chart.Models.Bell;
import talabaty.swever.com.online.Login;
import talabaty.swever.com.online.LoginDatabae;
import talabaty.swever.com.online.R;
import talabaty.swever.com.online.Register;
import talabaty.swever.com.online.Switch_nav;

public class FinalBell_Activity extends AppCompatActivity {
    TextView name, addres, phone;
    ImageView barcode;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<Bell> bellList;
    List<ChartModel> modelList;

    LoginDatabae loginDatabae ;
    Cursor userId;
    String user_id;

    String addresString , RegionString, CityString, StateString;

    Button finish;
    Intent intent;
    Date date = new Date();

    ChartDatabase chartDatabase;
    ChartAdditionalDatabase chartAdditionalDatabase;

    ProgressDialog progressDialog;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_bell);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView = (RecyclerView) findViewById(R.id.rec);
        recyclerView.setLayoutManager(layoutManager);
        bellList = new ArrayList<>();
        name = findViewById(R.id.name);
        addres = findViewById(R.id.address);
        phone = findViewById(R.id.phone);
        barcode = findViewById(R.id.barcode);
        finish = findViewById(R.id.ok);

        loginDatabae = new LoginDatabae(FinalBell_Activity.this);
        userId = loginDatabae.ShowData();

        intent = getIntent();
        bellList = (ArrayList<Bell>)intent.getSerializableExtra("model");
        modelList = (ArrayList<ChartModel>)intent.getSerializableExtra("mod");
        addresString = intent.getStringExtra("Address");
        RegionString = intent.getStringExtra("Region");
        CityString = intent.getStringExtra("City");
        StateString = intent.getStringExtra("State");
        chartAdditionalDatabase = new ChartAdditionalDatabase(this);
        chartDatabase = new ChartDatabase(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        while (userId.moveToNext()){
            user_id = userId.getString(2);
            name.setText(userId.getString(1)); /** From Internal Database */
        }


        addres.setText(bellList.get(0).getAddress());
        phone.setText(bellList.get(0).getPhone());



        Bitmap bitmap = null;
        try {
            bitmap = encodeAsBitmap(bellList.get(0).getBarcode(), BarcodeFormat.CODE_128, 600, 300);
            barcode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }


        loadBell();

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                uploadChart(gson.toJson(modelList), addresString, RegionString, CityString, StateString, date);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss();
        }

    }

    @SuppressLint("NewApi")
    private void uploadChart(final String mod, final String addres, final String Region, final String City, final String State, final Date date) {

        progressDialog = new ProgressDialog(FinalBell_Activity.this);
        progressDialog.setMessage("جارى تحميل البيانات ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://onlineapi.rivile.com/Order/Order",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.e("Result",response);
                        try{
                            JSONObject object = new JSONObject(response);
                            JSONArray array = object.getJSONArray("List");
                            if (array.length()>0){
                                //Empty DataBase Cart & Additions
                                chartDatabase.DeleteData();
                                chartAdditionalDatabase.DeleteData();
                                //Send Message To User
                                sendMessage("طلباتى","تم تنفيذ الطلب وجارى المتابعه");
                                Intent intent = new Intent(FinalBell_Activity.this, Switch_nav.class);

                                startActivity(intent);
                                finish();
                            }
                        }catch (Exception e){

                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View layout = inflater.inflate(R.layout.toast_warning,null);

                TextView text = (TextView) layout.findViewById(R.id.txt);

                if (error instanceof ServerError)
                    text.setText("خطأ فى الاتصال بالخادم");
                else if (error instanceof TimeoutError)
                    text.setText("خطأ فى مدة الاتصال");
                else if (error instanceof NetworkError)
                    text.setText("شبكه الانترنت ضعيفه حاليا");

                Toast toast = new Toast(FinalBell_Activity.this);
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
                Log.e("Order", mod + "");
                map.put("Address", addres + "");
                Log.e("Adress", addres + "");
                map.put("Region", Region + "");
                Log.e("Region", Region + "");
                map.put("City", City + "");
                Log.e("City", City + "");
                map.put("State", State + "");
                Log.e("State", State + "");
                map.put("UserId", user_id+"");
                map.put("Date", new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(date) + "");
                return map;
            }
        };
//        Volley.newRequestQueue(getActivity()).add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                2,  // maxNumRetries = 2 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(FinalBell_Activity.this).add(stringRequest);
    }

    private void sendMessage(final String sub, final String message){
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://onlineapi.rivile.com/Order/Send",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.e("Result",response);
                        if (response.equals("\"Success\"")){
//                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//                            View layout = inflater.inflate(R.layout.toast_info,null);
//
//                            TextView text = (TextView) layout.findViewById(R.id.txt);
//                            text.setText("تم ارسال راله");
//
//                            Toast toast = new Toast(getApplicationContext());
//                            toast.setGravity(Gravity.BOTTOM, 0, 0);
//                            toast.setDuration(Toast.LENGTH_LONG);
//                            toast.setView(layout);
//                            toast.show();
                        }else  {
//                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//                            View layout = inflater.inflate(R.layout.toast_info,null);
//
//                            TextView text = (TextView) layout.findViewById(R.id.txt);
//                            text.setText("");
//
//                            Toast toast = new Toast(getApplicationContext());
//                            toast.setGravity(Gravity.BOTTOM, 0, 0);
//                            toast.setDuration(Toast.LENGTH_LONG);
//                            toast.setView(layout);
//                            toast.show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                View layout = inflater.inflate(R.layout.toast_warning,null);

                TextView text = (TextView) layout.findViewById(R.id.txt);

                if (error instanceof ServerError)
                    text.setText("خطأ فى الاتصال بالخادم");
                else if (error instanceof TimeoutError)
                    text.setText("خطأ فى مدة الاتصال");
                else if (error instanceof NetworkError)
                    text.setText("شبكه الانترنت ضعيفه حاليا");

                Toast toast = new Toast(FinalBell_Activity.this);
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
                map.put("Sub", sub + "");
                Log.e("Sub", sub + "");
                map.put("Mes", message + "");
                Log.e("Mes", message + "");
                map.put("UserId", user_id+"");
                return map;
            }
        };
//        Volley.newRequestQueue(getActivity()).add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                2,  // maxNumRetries = 2 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(FinalBell_Activity.this).add(stringRequest);
    }
    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    Bitmap encodeAsBitmap(String contents, BarcodeFormat format, int img_width, int img_height) throws WriterException {
        String contentsToEncode = contents;
        if (contentsToEncode == null) {
            return null;
        }

        Map<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contentsToEncode);
        if (encoding != null) {
                hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result;
        try {
            result = writer.encode(contentsToEncode, format, img_width, img_height, hints);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if (contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }

    private void loadBell() {
        adapter = new FinalBellAdapter(this,bellList);
        recyclerView.setAdapter(adapter);
    }
}
