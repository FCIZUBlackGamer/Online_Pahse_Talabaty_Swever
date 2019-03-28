package talabaty.swever.com.online;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import talabaty.swever.com.online.Utils.APIURLUtil;
import talabaty.swever.com.online.Utils.AppRepository;
import talabaty.swever.com.online.Utils.AppToastUtil;
import talabaty.swever.com.online.Utils.StringUtil;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmailEditText, mPasswordEditText;
    private Button mLoginButton;
    private TextView mNewAccountTextView;
    private LoginDatabase mLoginDatabase;
    private Cursor mCursor;

    private ProgressDialog progressDialog;

    private AppRepository mRepository;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mCursor = mLoginDatabase.ShowData();
        while (mCursor.moveToNext()) {
            if (!mCursor.getString(0).equals("") && mCursor.getString(4).equals("1")) {
                Intent intent = new Intent(LoginActivity.this, SwitchNavActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_login);

        mRepository = AppRepository.getInstance(getApplication());

        mLoginDatabase = new LoginDatabase(this);

        mLoginButton = findViewById(R.id.login_button);
        mNewAccountTextView = findViewById(R.id.new_account);
        mEmailEditText = findViewById(R.id.email);
        mPasswordEditText = findViewById(R.id.password);

        mLoginButton.setOnClickListener(v -> {
            final String name = mEmailEditText.getText().toString();
            final String password = mPasswordEditText.getText().toString();
            if (validate(name, password)) {
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("جارى تحميل البيانات ...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                mRepository.login(name, password).observe(this, response -> {
                    progressDialog.dismiss();

                    if (response.equals(StringUtil.RESPONSE_FAIL)) {
                        YoYo.with(Techniques.Tada)
                                .duration(700)
                                .repeat(1)
                                .playOn(findViewById(R.id.log));

                        AppToastUtil.showErrorToast("كلمة مرور أو إسم مستخدم خطأ",
                                AppToastUtil.LENGTH_LONG, LoginActivity.this);

                        mPasswordEditText.setText("");
                    } else if (!response.equals(StringUtil.DISMISS_PROGRESS_DIALOG)) {
                        try {
                            JSONObject object = new JSONObject(response);
                            String id = object.getInt("Id") + "";

                            String name1 = object.getString("FirstName") +
                                    " " +
                                    object.getString("LastName");

                            String photo = object.getString("Photo");
                            String phone = object.getString("Phone");
                            String AccountType = object.getString("AccountType");
                            String email = object.getString("Mail");

                            mLoginDatabase.UpdateData(
                                    "1",
                                    name1,
                                    id,
                                    phone,
                                    "1",
                                    APIURLUtil.IMAGE_BASE_URL + photo,
                                    AccountType,
                                    email
                            );

                            Intent intent = new Intent(LoginActivity.this, SwitchNavActivity.class);
                            startActivity(intent);
                            finish();
                        } catch (JSONException e) {
                            Log.e(StringUtil.EXCEPTION_TAG, e.getMessage());
                        }
                    }
                });
            }
        });

        mNewAccountTextView.setOnClickListener(v -> startActivity(
                new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    private boolean validate(String name, String pass) {
        boolean res = true;
        if (name.length() < 1) {
            mEmailEditText.setError("ادخل اسم مستخدم");
            res = false;
        }
        if (pass.isEmpty() || pass.equals(" ")) {
            mPasswordEditText.setError("كلمه مرور فارغه");
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

//    String WarningMessage = null;
//                    if (error instanceof ServerError)
//    WarningMessage = "خطأ فى الاتصال بالخادم";
//                    else if (error instanceof TimeoutError)
//    WarningMessage = "خطأ فى مدة الاتصال";
//                    else if (error instanceof NetworkError)
//    WarningMessage = "شبكه الانترنت ضعيفه حاليا";
//
//                    if (WarningMessage != null) AppToastUtil.showWarningToast(WarningMessage,
//    AppToastUtil.LENGTH_LONG, getContext());
//
//
//                     AppToastUtil.showInfoToast("لا توجد بيانات",
//    AppToastUtil.LENGTH_LONG, getContext());
}
