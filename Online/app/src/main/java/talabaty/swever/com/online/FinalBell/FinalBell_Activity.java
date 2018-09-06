package talabaty.swever.com.online.FinalBell;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import talabaty.swever.com.online.Chart.Models.Bell;
import talabaty.swever.com.online.LoginDatabae;
import talabaty.swever.com.online.R;

public class FinalBell_Activity extends AppCompatActivity {
    TextView name, addres, phone;
    ImageView barcode;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<Bell> bellList;

    LoginDatabae loginDatabae ;
    Cursor userId;

    String addresString , RegionString, CityString, StateString;

    Button finish;
    Intent intent;
    Date date = new Date();


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
        addresString = intent.getStringExtra("Address");
        RegionString = intent.getStringExtra("Region");
        CityString = intent.getStringExtra("City");
        StateString = intent.getStringExtra("State");

    }

    @Override
    protected void onStart() {
        super.onStart();
        name.setText("My Name"); /** From Internal Database */
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
                uploadChart(gson.toJson(bellList), addresString, RegionString, CityString, StateString, date);
            }
        });
    }

    @SuppressLint("NewApi")
    private void uploadChart(final String mod, final String addres, final String Region, final String City, final String State, final Date date) {


        final AlertDialog.Builder builder = new AlertDialog.Builder(FinalBell_Activity.this);
        GifImageView gifImageView = new GifImageView(FinalBell_Activity.this);
        gifImageView.setImageResource(R.drawable.load);
        builder.setCancelable(false);
        builder.setView(gifImageView);
        final AlertDialog dlg = builder.create();
        dlg .getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dlg.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://onlineapi.sweverteam.com/Order/Order",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                dlg.dismiss();
                            }
                        }, 5000);   //5 seconds
                        Log.e("Result",response);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        dlg.dismiss();
                    }
                }, 5000);   //5 seconds
                if (error instanceof ServerError)
                    Toast.makeText(FinalBell_Activity.this, "خطأ إثناء الاتصال بالخادم", Toast.LENGTH_SHORT).show();
                else if (error instanceof NetworkError)
                    Toast.makeText(FinalBell_Activity.this, "خطأ فى شبكه الانترنت", Toast.LENGTH_SHORT).show();
                else if (error instanceof TimeoutError)
                    Toast.makeText(FinalBell_Activity.this, "خطأ فى مده الانتظار", Toast.LENGTH_SHORT).show();
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
                map.put("UserId", userId.getString(2)+"");
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
