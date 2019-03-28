package talabaty.swever.com.online;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fourhcode.forhutils.FUtilsValidation;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import talabaty.swever.com.online.Utils.AppRepository;
import talabaty.swever.com.online.Utils.AppToastUtil;
import talabaty.swever.com.online.Utils.StringUtil;

public class RegisterActivity extends AppCompatActivity {

    EditText fname, lname, email, pass, repass, phone;
    TextView login;
    Button signup, date_of_birth;
    CircleImageView image;

    private int PICK_IMAGE_REQUEST = 1;
    final int CAMERA_PIC_REQUEST = 1337;

    List<ImageSource> Gallary;
    String baseUrl = "http://selltlbaty.rivile.com/";
    private String UPLOAD_URL = baseUrl + "Uploads/UploadAndro";

    private String KEY_IMAGE = "base64imageString";
    private String KEY_NAME = "name";
    //    List<byte[]> bytes;
    // To Get Data
    UserModel userModel = null;
    private static final int CAMERA_REQUEST = 1888;
    Bitmap bitmap;
    List<String> imageStrings;

    DatePickerDialog.OnDateSetListener DatePicker1;

    View Camera_view;
    ImageView close, minimize, cam, gal;
    FloatingActionButton appear;
    int close_type;

    ProgressDialog progressDialog, progressDialog2;

    int im = 0;

    private AppRepository mRepository;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_register);
        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        appear = findViewById(R.id.appear);
        date_of_birth = findViewById(R.id.date_of_birth);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        repass = findViewById(R.id.repassword);
        login = findViewById(R.id.goto_login);
        signup = findViewById(R.id.signup);
        image = findViewById(R.id.image);
        phone = findViewById(R.id.phone);
        imageStrings = new ArrayList<>();

        mRepository = AppRepository.getInstance(getApplication());
    }

    @Override
    protected void onStart() {
        super.onStart();
        userModel = new UserModel();
        appear.setVisibility(View.GONE);
        date_of_birth.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(
                    RegisterActivity.this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth
                    , DatePicker1
                    , year, month, day);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        });

        DatePicker1 = (view, year, month, dayOfMonth) -> {
            month = month + 1;
            date_of_birth.setText(year + "/" + month + "/" + dayOfMonth);
        };

        requestStoragePermission();
        image.setOnClickListener(v -> {

            final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            Camera_view = inflater.inflate(R.layout.camera_view, null);

            close = Camera_view.findViewById(R.id.close);
            minimize = Camera_view.findViewById(R.id.minimize);
            cam = Camera_view.findViewById(R.id.cam);
            gal = Camera_view.findViewById(R.id.gal);

            final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
            builder.setCancelable(false)
                    .setView(Camera_view);

            final AlertDialog dialog = builder.create();
            dialog.show();

            gal.setOnClickListener(v12 -> {
                openGalary();
                dialog.dismiss();
            });

            cam.setOnClickListener(v1 -> {
                openCamera();
                dialog.dismiss();
            });

            close.setOnClickListener(v13 -> {
                close_type = 0;
                dialog.dismiss();
                runOnUiThread(() -> appear.setVisibility(View.GONE));

            });

            minimize.setOnClickListener(v14 -> {
                close_type = 1;
                dialog.dismiss();
                runOnUiThread(() -> appear.setVisibility(View.VISIBLE));

            });
        });

/** in case user pressed minimize button */
        appear.setOnClickListener(v -> {

            final LayoutInflater inflater = (LayoutInflater) RegisterActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            Camera_view = inflater.inflate(R.layout.camera_view, null);

            close = Camera_view.findViewById(R.id.close);
            minimize = Camera_view.findViewById(R.id.minimize);
            cam = Camera_view.findViewById(R.id.cam);
            gal = Camera_view.findViewById(R.id.gal);

            final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
            builder.setCancelable(false)
                    .setView(Camera_view);

            final AlertDialog dialog = builder.create();
            dialog.show();

            gal.setOnClickListener(v15 -> {
                openGalary();
                dialog.dismiss();
            });

            cam.setOnClickListener(v16 -> {
                openCamera();
                dialog.dismiss();
            });

            close.setOnClickListener(v17 -> {
                close_type = 0;
                dialog.dismiss();
                runOnUiThread(() -> appear.setVisibility(View.GONE));
            });

            minimize.setOnClickListener(v18 -> {
                close_type = 1;
                dialog.dismiss();
                runOnUiThread(() -> appear.setVisibility(View.VISIBLE));
            });
        });

        login.setOnClickListener(v -> startActivity(new Intent(RegisterActivity.this, LoginActivity.class)));

        signup.setOnClickListener(v -> {
            //Todo: Validate Inputs #Done


            if (FUtilsValidation.isPhone(phone.getText().toString()) &&
                    FUtilsValidation.isValidEmail(email, "صيغه بريد الكترونى خاطئه") &&
                    !FUtilsValidation.isEmpty(fname, "ادخل اسم صحيح") &&
                    !FUtilsValidation.isEmpty(lname, "ادخل اسم صحيح") &&
                    FUtilsValidation.isPasswordEqual(pass, repass, "كلمة المرور غير متطابقة") &&
                    FUtilsValidation.isDateValid(date_of_birth.getText().toString(), "yyyy/mm/dd") &&
                    im == 1) {
                userModel.setCountryId(1);
                userModel.setFirstName(fname.getText().toString());
                userModel.setLastName(lname.getText().toString());
                userModel.setDateOfBirth(date_of_birth.getText().toString());
                userModel.setPassword(pass.getText().toString());
                userModel.setMail(email.getText().toString());
                userModel.setPhone(phone.getText().toString());
                uploadImage();
            }
        });
    }

//    private boolean valid(String fnam, String lnam, String pas, String ema, String pho) {
//        boolean res = true;
//        if (fnam.length() < 3){
//            fname.setError("اسم غير صحيح");
//            res = false;
//        }if (lnam.length() < 3){
//            lname.setError("اسم غير صحيح");
//            res = false;
//        }if (FUtilsValidation.isPhone(phone.getText().toString())){
//            phone.setError("رقم هاتف خطأ");
//            res = false;
//        }if (pas.length() < 6){
//            pass.setError("كلمه مرور ضعيفه");
//            res = false;
//        }if (ema.length() < 4 || !ema.contains("@")|| !ema.contains(".")){
//            email.setError("ادخل بريد الكترونى صحيح");
//            res = false;
//        }if (date_of_birth.length() < 1){
//            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//            View layout = inflater.inflate(R.layout.toast_error,null);
//
//            TextView text = (TextView) layout.findViewById(R.id.txt);
//
//            text.setText("يرجى ادخال تاريخ ميلاد");
//
//            AppToastUtil toast = new AppToastUtil(RegisterActivity.this);
//            toast.setGravity(Gravity.BOTTOM, 0, 0);
//            toast.setDuration(AppToastUtil.LENGTH_LONG);
//            toast.setView(layout);
//            toast.show();
//
//            res = false;
//        }if (im != 1){
//            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//            View layout = inflater.inflate(R.layout.toast_error,null);
//T
//            TextView text = (TextView) layout.findViewById(R.id.txt);
//
//            text.setText("يرجى ارفاق صوره الشخصيه");
//
//            AppToastUtil toast = new AppToastUtil(RegisterActivity.this);
//            toast.setGravity(Gravity.BOTTOM, 0, 0);
//            toast.setDuration(AppToastUtil.LENGTH_LONG);
//            toast.setView(layout);
//            toast.show();
//            res = false;
//        }
//
//        return res;
//    }

    private void uploadImage() {
        final Gson gson = new Gson();
        Log.e("Connection UploadImage", "Here");

        final String allImages = gson.toJson(imageStrings);
        Log.e("Start: ", allImages);
        //Showing the progress dialog
        progressDialog = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);

        mRepository.uploadImage(allImages, "Mohamed").observe(this, response -> {
            if (response != null) {
                progressDialog.dismiss();

                if (!response.equals(StringUtil.DISMISS_PROGRESS_DIALOG)) {
                    try {
                        JSONObject object = new JSONObject(response);
                        JSONArray array = object.getJSONArray("Images");
                        for (int x = 0; x < array.length(); x++) {
                            String object1 = array.getString(x);
                            userModel.setPhoto(object1);
                        }

                        final String jsonInString = gson.toJson(userModel);
//                            Log.e("Data", jsonInString);
//                            Log.e("Gallary", gson.toJson(Gallary));
                        /** upload all data after upload images */
                        uploadMontage(jsonInString);
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
        if (ContextCompat.checkSelfPermission(RegisterActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                AppToastUtil.showInfoToast("camera permission granted", Toast.LENGTH_LONG,
                        this);
            } else {
                AppToastUtil.showWarningToast("camera permission denied",
                        Toast.LENGTH_LONG, this);
            }
        }
    }//end onRequestPermissionsResult

    //handling the image chooser activity result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri filePath;
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(RegisterActivity.this.getContentResolver(), filePath);
                image.setImageBitmap(bitmap);
                if (image.getDrawable() == null) {
                    imageStrings.add(getStringImage(bitmap));
                } else {
                    imageStrings.add(0, getStringImage(bitmap));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");
            image.setImageBitmap(bitmap);
            if (image.getDrawable() == null) {
                imageStrings.add(getStringImage(bitmap));
            } else {
                imageStrings.add(0, getStringImage(bitmap));
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

    private void uploadMontage(final String jsonInString) {
        Log.e("Connection UploadMontag", "Here");
        Log.e("Full Model", jsonInString);
        progressDialog2 = ProgressDialog.show(this, "Uploading...", "Please wait...", false, false);

        mRepository.addUser(jsonInString).observe(this, response -> {
            if (response != null) {
                progressDialog2.dismiss();

                if (!response.equals(StringUtil.DISMISS_PROGRESS_DIALOG)) {
                    Log.e("Data: ", response);
                    String infoMessage;
                    switch (response) {
                        case StringUtil.RESPONSE_DUPLICATE:
                            infoMessage = " البريد الالكتروني او رقم التليفون مكرر  .. يرجي إعاده المحاوله";
                            break;
                        case StringUtil.RESPONSE_FAIL:
                            infoMessage = "عذرا حدث خطأ أثناء اجراء العملية  .. يرجي المحاوله لاحقا";
                            break;
                        default:
                            infoMessage = "اسم المستخدم هو " + response;
                            break;
                    }
                    AppToastUtil.showInfoToast(infoMessage,
                            AppToastUtil.LENGTH_LONG, RegisterActivity.this);
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
