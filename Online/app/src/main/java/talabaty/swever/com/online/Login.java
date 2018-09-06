package talabaty.swever.com.online;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class Login extends AppCompatActivity {

    EditText email, password;
    CheckBox keep_me_login;
    Button login;
    TextView new_account;
    LoginDatabae loginDatabae ;
    Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_login);

        loginDatabae = new LoginDatabae(this);
        cursor = loginDatabae.ShowData();
        while (cursor.moveToNext()) {
            if (!cursor.getString(0).equals("") && cursor.getString(4).equals("1")) {
                Intent intent = new Intent(Login.this, Switch_nav.class);
//                intent.putExtra("fragment", "mabi3at");
                startActivity(intent);
            }
        }

        login = findViewById(R.id.login_button);
        new_account = findViewById(R.id.new_account);
        keep_me_login = findViewById(R.id.keep_me_login);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name = email.getText().toString();
                final String passwor = password.getText().toString();
                RequestQueue queue = Volley.newRequestQueue(Login.this);
                StringRequest request = new StringRequest(Request.Method.POST, "http://onlineapi.sweverteam.com/Login/Login", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response",response);
                        if (response.equals("\"fail\"")) {

                            YoYo.with(Techniques.Tada)
                            .duration(700)
                            .repeat(1)
                            .playOn(findViewById(R.id.log));
                            Toast.makeText(Login.this, "كلمه مرور أو أسم مستخدم خطأ", Toast.LENGTH_SHORT).show();
                            password.setText("");
                        } else {


                            final AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                            GifImageView gifImageView = new GifImageView(Login.this);
                            gifImageView.setImageResource(R.drawable.load);
                            builder.setCancelable(false);
                            builder.setView(gifImageView);
                            final AlertDialog dlg = builder.create();

                            dlg .getWindow().setBackgroundDrawable(new ColorDrawable(0));
                            dlg.show();
//                            Toast.makeText(Login.this, "Welcome Home!", Toast.LENGTH_SHORT).show();
                            try {
                                JSONObject object = new JSONObject(response);
                                //JSONArray array = object.getJSONArray("user");
                                String id = object.getInt("Id") + "";
                                String name = object.getString("FirstName").toString() + object.getString("LastName").toString();
                                String photo = object.getString("Photo").toString();
                                String phone = object.getString("Phone").toString();

                                if (keep_me_login.isChecked()) {
                                    loginDatabae.UpdateData("1", name, id, phone,"1", "http://selltlbaty.sweverteam.com/"+photo);
                                }

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        dlg.dismiss();
                                        Intent intent = new Intent(Login.this,Switch_nav.class);

                                        startActivity(intent);
                                    }
                                }, 5000);   //5 seconds


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof ServerError)
                            Toast.makeText(Login.this, "Server Error", Toast.LENGTH_SHORT).show();
                        else if (error instanceof TimeoutError)
                            Toast.makeText(Login.this, "Connection Timed Out", Toast.LENGTH_SHORT).show();
                        else if (error instanceof NetworkError)
                            Toast.makeText(Login.this, "Bad Network Connection", Toast.LENGTH_SHORT).show();
                    }
                }) {

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Name", name);
                        params.put("Password", passwor);
                        params.put("token","?za[ZbGNz2B}MXYZ");
                        return params;
                    }
                };
                queue.add(request);

            }
        });

        new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Register.class));

            }
        });
    }
}
