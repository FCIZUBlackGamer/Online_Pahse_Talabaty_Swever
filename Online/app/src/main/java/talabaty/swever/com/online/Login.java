package talabaty.swever.com.online;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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

public class Login extends AppCompatActivity {

    EditText email, password;
    Button login;
    TextView new_account;
    LoginDatabae loginDatabae ;
    Cursor cursor;

    ProgressDialog progressDialog;

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
                finish();
            }
        }

        login = findViewById(R.id.login_button);
        new_account = findViewById(R.id.new_account);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name = email.getText().toString();
                final String passwor = password.getText().toString();
                if (validate(name, passwor)) {
                    progressDialog = new ProgressDialog(Login.this);
                    progressDialog.setMessage("جارى تحميل البيانات ...");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    RequestQueue queue = Volley.newRequestQueue(Login.this);
                    StringRequest request = new StringRequest(Request.Method.POST, "http://onlineapi.rivile.com/Login/Login", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.e("Response", response);
                            progressDialog.dismiss();

                            if (response.equals("\"fail\"")) {

                                YoYo.with(Techniques.Tada)
                                        .duration(700)
                                        .repeat(1)
                                        .playOn(findViewById(R.id.log));

                                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                                View layout = inflater.inflate(R.layout.toast_error, null);

                                TextView text = (TextView) layout.findViewById(R.id.txt);
                                text.setText("كلمه مرور أو أسم مستخدم خطأ");

                                Toast toast = new Toast(Login.this);
                                toast.setGravity(Gravity.BOTTOM, 0, 0);
                                toast.setDuration(Toast.LENGTH_LONG);
                                toast.setView(layout);
                                toast.show();
                                password.setText("");
                            } else {

//                            Toast.makeText(Login.this, "Welcome Home!", Toast.LENGTH_SHORT).show();
                                try {
                                    JSONObject object = new JSONObject(response);
                                    //JSONArray array = object.getJSONArray("user");
                                    String id = object.getInt("Id") + "";
                                    Log.e("ID", id);
                                    String name = object.getString("FirstName").toString() + object.getString("LastName").toString();
                                    String photo = object.getString("Photo").toString();
                                    String phone = object.getString("Phone").toString();

                                    loginDatabae.UpdateData("1", name, id, phone, "1", "http://selltlbaty.rivile.com/" + photo);



                                    progressDialog.dismiss();
                                    Intent intent = new Intent(Login.this, Switch_nav.class);

                                    startActivity(intent);
                                    finish();


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                            View layout = inflater.inflate(R.layout.toast_warning, null);

                            TextView text = (TextView) layout.findViewById(R.id.txt);

                            if (error instanceof ServerError)
                                text.setText("خطأ فى الاتصال بالخادم");
                            else if (error instanceof TimeoutError)
                                text.setText("خطأ فى مدة الاتصال");
                            else if (error instanceof NetworkError)
                                text.setText("شبكه الانترنت ضعيفه حاليا");

                            Toast toast = new Toast(Login.this);
                            toast.setGravity(Gravity.BOTTOM, 0, 0);
                            toast.setDuration(Toast.LENGTH_LONG);
                            toast.setView(layout);
                            toast.show();
                        }
                    }) {

                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("Name", name);
                            params.put("Password", passwor);
                            params.put("token", "?za[ZbGNz2B}MXYZ");
                            return params;
                        }
                    };
                    queue.add(request);
                }
            }
        });


        new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Register.class));

            }
        });
    }

    private boolean validate(String name, String pass){
        boolean res = true;
        if (name.length()<1){
            email.setError("ادخل اسم مستخدم");
            res = false;
        }
        if (pass.isEmpty() || pass.equals(" ")){
            password.setError("كلمه مرور فارغه");
            res = false;
        }
        return res;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
