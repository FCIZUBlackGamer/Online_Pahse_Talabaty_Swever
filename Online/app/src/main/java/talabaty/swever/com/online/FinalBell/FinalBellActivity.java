package talabaty.swever.com.online.FinalBell;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import talabaty.swever.com.online.Cart.CartAdditionalDatabase;
import talabaty.swever.com.online.Cart.CartDatabase;
import talabaty.swever.com.online.Cart.CartModel;
import talabaty.swever.com.online.Cart.Models.Bell;
import talabaty.swever.com.online.LoginDatabae;
import talabaty.swever.com.online.R;
import talabaty.swever.com.online.SwitchNav;
import talabaty.swever.com.online.Utils.AppToastUtil;

public class FinalBellActivity extends AppCompatActivity {
    TextView name, addres, phone;
    ImageView barcode;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<Bell> bellList;
    List<CartModel> modelList;

    LoginDatabae loginDatabae;
    Cursor userId;
    String user_id;

    String addresString, RegionString, CityString, StateString;

    Button finish;
    Intent intent;
    Date date = new Date();

    CartDatabase cartDatabase;
    CartAdditionalDatabase cartAdditionalDatabase;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_bell);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView = findViewById(R.id.rec);
        recyclerView.setLayoutManager(layoutManager);
        bellList = new ArrayList<>();
        name = findViewById(R.id.name);
        addres = findViewById(R.id.address);
        phone = findViewById(R.id.phone);
        barcode = findViewById(R.id.barcode);
        finish = findViewById(R.id.ok);

        loginDatabae = new LoginDatabae(FinalBellActivity.this);
        userId = loginDatabae.ShowData();

        intent = getIntent();
        bellList = (ArrayList<Bell>) intent.getSerializableExtra("model");
        modelList = (ArrayList<CartModel>) intent.getSerializableExtra("mod");
        addresString = intent.getStringExtra("Address");
        RegionString = intent.getStringExtra("Region");
        CityString = intent.getStringExtra("City");
        StateString = intent.getStringExtra("State");
        cartAdditionalDatabase = new CartAdditionalDatabase(this);
        cartDatabase = new CartDatabase(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        while (userId.moveToNext()) {
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

        finish.setOnClickListener(v -> {
            Gson gson = new Gson();
            uploadCart(gson.toJson(modelList), addresString, RegionString, CityString, StateString, date);
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
    @SuppressLint("NewApi")
    private void uploadCart(final String mod, final String addres, final String Region, final String City, final String State, final Date date) {

        progressDialog = new ProgressDialog(FinalBellActivity.this);
        progressDialog.setMessage("جارى تحميل البيانات ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://onlineapi.rivile.com/Order/Order",
                response -> {
                    progressDialog.dismiss();
                    Log.e("Result", response);
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONArray array = object.getJSONArray("List");
                        if (array.length() > 0) {
                            //Empty DataBase Cart & Additions
                            cartDatabase.DeleteData();
                            cartAdditionalDatabase.DeleteData();
                            //Send Message To User
                            sendMessage("طلباتى", "تم تنفيذ الطلب وجارى المتابعه");
                            Intent intent = new Intent(FinalBellActivity.this, SwitchNav.class);

                            startActivity(intent);
                            finish();
                        }
                    } catch (Exception e) {

                    }
                }, error -> {
            progressDialog.dismiss();

            String WarningMessage = null;
            if (error instanceof ServerError)
                WarningMessage = "خطأ فى الاتصال بالخادم";
            else if (error instanceof TimeoutError)
                WarningMessage = "خطأ فى مدة الاتصال";
            else if (error instanceof NetworkError)
                WarningMessage = "شبكه الانترنت ضعيفه حاليا";

            if (WarningMessage != null) AppToastUtil.showWarningToast(WarningMessage,
                    AppToastUtil.LENGTH_LONG, FinalBellActivity.this);
        }) {
            @Override
            protected Map<String, String> getParams() {
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
                map.put("UserId", user_id + "");
                map.put("Date", new SimpleDateFormat("yyyy/MM/dd hh:mm:ss").format(date) + "");
                return map;
            }
        };
//        Volley.newRequestQueue(getActivity()).add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                2,  // maxNumRetries = 2 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(FinalBellActivity.this).add(stringRequest);
    }

    private void sendMessage(final String sub, final String message) {
        final StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://onlineapi.rivile.com/Order/Send",
                response -> {
                    progressDialog.dismiss();
                    Log.e("Result", response);
                    if (response.equals("\"Success\"")) {
//                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//                            View layout = inflater.inflate(R.layout.toast_info,null);
//
//                            TextView text = (TextView) layout.findViewById(R.id.txt);
//                            text.setText("تم ارسال راله");
//
//                            AppToastUtil toast = new AppToastUtil(getApplicationContext());
//                            toast.setGravity(Gravity.BOTTOM, 0, 0);
//                            toast.setDuration(AppToastUtil.LENGTH_LONG);
//                            toast.setView(layout);
//                            toast.show();
                    } else {
//                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//                            View layout = inflater.inflate(R.layout.toast_info,null);
//
//                            TextView text = (TextView) layout.findViewById(R.id.txt);
//                            text.setText("");
//
//                            AppToastUtil toast = new AppToastUtil(getApplicationContext());
//                            toast.setGravity(Gravity.BOTTOM, 0, 0);
//                            toast.setDuration(AppToastUtil.LENGTH_LONG);
//                            toast.setView(layout);
//                            toast.show();
                    }

                }, error -> {
            progressDialog.dismiss();

            String WarningMessage = null;
            if (error instanceof ServerError)
                WarningMessage = "خطأ فى الاتصال بالخادم";
            else if (error instanceof TimeoutError)
                WarningMessage = "خطأ فى مدة الاتصال";
            else if (error instanceof NetworkError)
                WarningMessage = "شبكه الانترنت ضعيفه حاليا";

            if (WarningMessage != null) AppToastUtil.showWarningToast(WarningMessage,
                    AppToastUtil.LENGTH_LONG, FinalBellActivity.this);
        }) {
            @Override
            protected Map<String, String> getParams() {
                HashMap<String, String> map = new HashMap<>();
                map.put("token", "?za[ZbGNz2B}MXYZ");
                map.put("Sub", sub + "");
                Log.e("Sub", sub + "");
                map.put("Mes", message + "");
                Log.e("Mes", message + "");
                map.put("UserId", user_id + "");
                return map;
            }
        };
//        Volley.newRequestQueue(getActivity()).add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                2,  // maxNumRetries = 2 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(FinalBellActivity.this).add(stringRequest);
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
            hints = new EnumMap<>(EncodeHintType.class);
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
        adapter = new FinalBellAdapter(this, bellList);
        recyclerView.setAdapter(adapter);
    }
}