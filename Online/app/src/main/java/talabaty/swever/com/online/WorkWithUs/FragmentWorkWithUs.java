package talabaty.swever.com.online.WorkWithUs;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import talabaty.swever.com.online.CategoryModel;
import talabaty.swever.com.online.R;
import talabaty.swever.com.online.Register;
import talabaty.swever.com.online.Switch_nav;
import talabaty.swever.com.online.UserModel;

import static android.app.Activity.RESULT_OK;
import talabaty.swever.com.online.*;

public class FragmentWorkWithUs extends Fragment {
    Button id_open, end_date, save;
    RadioGroup radiogroup;
    Spinner package_id, cat_id, doc, delivery;
    ImageView id_image;
    EditText phone, national_id, name;

    private static final int CAMERA_REQUEST = 1888;
    private int PICK_IMAGE_REQUEST = 1;

    String baseUrl = "http://www.selltlbaty.sweverteam.com/";
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
    LoginDatabae loginDatabae ;
    Cursor cursor;

    DatePickerDialog.OnDateSetListener DatePicker1;

    boolean te = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        View view = inflater.inflate(R.layout.fragment_work_with_us, container, false);
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
        delivery = view.findViewById(R.id.delivery);
        loginDatabae = new LoginDatabae(getActivity());


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
                if (checkedId == R.id.salver){
                    package_id.setVisibility(View.VISIBLE);
                    loadPackages();
                    packag = false;
                }else if (checkedId == R.id.gold){
                    package_id.setVisibility(View.GONE);
                    packag = true;
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cursor = loginDatabae.ShowData();

                withUsModel = new WorkWithUsModel();
                if (doc.getSelectedItem().toString().equals("بطاقه هوية شخصيه")){
                    withUsModel.setPhotoType(1);
                }else {
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
                end_date.setText(year + "/" + month +"/" + dayOfMonth);
            }
        };

        id_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layout = new LinearLayout(getActivity());
                layout.setOrientation(LinearLayout.VERTICAL);
                Button cam = new Button(getActivity());
                cam.setText("فتح الكاميرا");
                cam.setWidth(20);
                cam.setTextSize(24);
                cam.setHeight(60);
                Button gallary = new Button(getActivity());
                gallary.setText("فتح المعرض");
                gallary.setWidth(20);
                gallary.setTextSize(24);
                gallary.setHeight(60);
                Button cancel = new Button(getActivity());
                cancel.setText("إلغاء");
                cancel.setWidth(20);
                cancel.setTextSize(24);
                cancel.setHeight(60);

                layout.addView(cam);
                layout.addView(gallary);
                layout.addView(cancel);

                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(false)
                        .setTitle("ارفاق صوره")
                        .setView(layout);

                final AlertDialog dialog = builder.create();
                dialog.show();

                gallary.setOnClickListener(new View.OnClickListener() {
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

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    private void uploadImage() {

        final Gson gson = new Gson();
        Log.e("Connection UploadImage", imageStrings.size()+"");

        final String allImages = gson.toJson(imageStrings);
        Log.e("Start: ", allImages);
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Uploading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        Log.e("Path: ", s);
                        try {

                            JSONObject object = new JSONObject(s);
                            JSONArray array = object.getJSONArray("Images");
                            for (int x = 0; x < array.length(); x++) {
                                String object1 = array.getString(x);
                                withUsModel.setPhoto(object1);
                            }

                            final String jsonInString = gson.toJson(withUsModel);
                            Log.e("Data", jsonInString);
                            if (packag){
                                int temp = 0;
                                for (int r=0; r<spin_cat.size(); r++){
                                    Log.e("R",spin_cat.get(r).getId()+"");
                                    if (String.valueOf(spin_cat.get(r).getName()).equals(cat_id.getSelectedItem().toString())){
                                        temp = spin_cat.get(r).getId();
                                        Log.e("INDEX",temp+"");
                                    }
                                }
                                Log.e("Index",temp+"");
                                uploadMontage(jsonInString,-1, name.getText().toString(),temp);
                            }else {
                                int temp = 0;
                                for (int r=0; r<spins.size(); r++){
                                    Log.e("R",spins.get(r).getId()+"");
                                    if (String.valueOf(spins.get(r).getValue()).equals(package_id.getSelectedItem().toString())){
                                        temp = spins.get(r).getId();
                                        Log.e("INDEX",temp+"");
                                    }
                                }
                                Log.e("Index",temp+"");

                                int temp1 = 0;
                                for (int r=0; r<spin_cat.size(); r++){
                                    Log.e("R",spin_cat.get(r).getId()+"");
                                    if (String.valueOf(spin_cat.get(r).getName()).equals(cat_id.getSelectedItem().toString())){
                                        temp1 = spin_cat.get(r).getId();
                                        Log.e("INDEX",temp1+"");
                                    }
                                }
                                Log.e("Index",temp1+"");

                                uploadMontage(jsonInString,temp, name.getText().toString(),temp1);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
//                        Toast.makeText(Register.getActivity(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
                        if (volleyError instanceof ServerError)
                            Log.e("Error: ", "Server Error");
                        else if (volleyError instanceof TimeoutError)
                            Log.e("Error: ", "Timeout Error");
                        else if (volleyError instanceof NetworkError)
                            Log.e("Error: ", "Bad Network");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put(KEY_IMAGE, allImages);

                params.put(KEY_NAME, "Mohamed");

                //returning parameters
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                2,  // maxNumRetries = 2 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getActivity()).add(stringRequest);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(getActivity(), "camera permission granted", Toast.LENGTH_LONG).show();

            } else {

                Toast.makeText(getActivity(), "camera permission denied", Toast.LENGTH_LONG).show();

            }

        }
    }//end onRequestPermissionsResult

    //handling the image chooser activity result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri filePath;
        imageStrings = new ArrayList<>();
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath= data.getData();

            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                id_image.setImageBitmap(bitmap);
                if (id_image.getDrawable() == null){
                    imageStrings.add(getStringImage(bitmap));
                    Log.e("Image",imageStrings.get(0));
                    Log.e("Length",imageStrings.size()+"");
                }else {
                    imageStrings.add(0,getStringImage(bitmap));
                    Log.e("Image",imageStrings.get(0));
                    Log.e("Length",imageStrings.size()+"");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            bitmap = (Bitmap) data.getExtras().get("data");
            id_image.setImageBitmap(bitmap);
            if (id_image.getDrawable() == null){
                imageStrings.add(getStringImage(bitmap));
                Log.e("Image",imageStrings.get(0));
            }else {
                imageStrings.add(0,getStringImage(bitmap));
                Log.e("Image",imageStrings.get(0));
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

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("جارى تحميل البيانات ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://onlineapi.sweverteam.com/SubScrip/Package",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject object = new JSONObject(response);
                            JSONArray array = object.getJSONArray("package");
                            if (array.length() > 0) {


                                for (int x=0; x<array.length(); x++){
                                    JSONObject jsonObject = array.getJSONObject(x);

                                    spin e = new spin(jsonObject.getInt("Id"),jsonObject.getInt("Value"));
                                    spins.add(e);
                                }
//                                Log.e("Length", values.size()+"");

                                for (int s=0; s<spins.size(); s++){
                                    values.add(spins.get(s).getValue()+"");
                                    Log.e("RR",spins.get(s).getId()+"");
                                }
                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                                        android.R.layout.simple_spinner_dropdown_item, values);
                                package_id.setAdapter(dataAdapter);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof ServerError)
                    Toast.makeText(getActivity(), "خطأ إثناء الاتصال بالخادم", Toast.LENGTH_SHORT).show();
                else if (error instanceof NetworkError)
                    Toast.makeText(getActivity(), "خطأ فى شبكه الانترنت", Toast.LENGTH_SHORT).show();
                else if (error instanceof TimeoutError)
                    Toast.makeText(getActivity(), "خطأ فى مده الانتظار", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("token","?za[ZbGNz2B}MXYZ");
                return map;
            }
        };
        progressDialog.dismiss();
//        Volley.newRequestQueue(getActivity()).add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                2,  // maxNumRetries = 2 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    private void loadCategory() {
        spin_cat = new ArrayList<>();

        if (spin_cat.size() > 0) {
            for (int x = 0; x < spin_cat.size(); x++) {
                spin_cat.remove(x);
                cats.remove(x);
            }
        }

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("جارى تحميل البيانات ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://onlineapi.sweverteam.com/SubScrip/Categories",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject object = new JSONObject(response);
                            JSONArray array = object.getJSONArray("package");
                            if (array.length() > 0) {


                                for (int x=0; x<array.length(); x++){
                                    JSONObject jsonObject = array.getJSONObject(x);

                                    spin e = new spin(jsonObject.getInt("Id"),jsonObject.getString("Name"));
                                    spin_cat.add(e);
                                }
//                                Log.e("Length", values.size()+"");

                                for (int s=0; s<spin_cat.size(); s++){
                                    cats.add(spin_cat.get(s).getName()+"");
                                    Log.e("RR",spin_cat.get(s).getId()+"");
                                }
                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                                        android.R.layout.simple_spinner_dropdown_item, cats);
                                cat_id.setAdapter(dataAdapter);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof ServerError)
                    Toast.makeText(getActivity(), "خطأ إثناء الاتصال بالخادم", Toast.LENGTH_SHORT).show();
                else if (error instanceof NetworkError)
                    Toast.makeText(getActivity(), "خطأ فى شبكه الانترنت", Toast.LENGTH_SHORT).show();
                else if (error instanceof TimeoutError)
                    Toast.makeText(getActivity(), "خطأ فى مده الانتظار", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("token","?za[ZbGNz2B}MXYZ");
                return map;
            }
        };
        progressDialog.dismiss();
//        Volley.newRequestQueue(getActivity()).add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                2,  // maxNumRetries = 2 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getActivity()).add(stringRequest);
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

    private void uploadMontage(final String jsonInString, final int PackagesValueId,final String Name,final int CategoryId) {
        Log.e("Connection UploadMontag", "Here");
        Log.e("Full Model",jsonInString);

        if (delivery.getSelectedItem().toString().equals("بالكيلو")){
            te = false;
        }else {
            te = true;
        }
        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Uploading...", "Please wait...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://onlineapi.sweverteam.com/SubScrip/Edit",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        Log.e("Data: ", s);
                        if (s.equals("\"Success\"")){
                            Toast toast = Toast.makeText(getActivity(), " تم الإشتراك بنجاح ", Toast.LENGTH_SHORT);
                            TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                            v.setTextColor(Color.GREEN);
                            toast.show();
                        }else if (s.equals("\"duplicated\"")){
                            Toast toast = Toast.makeText(getActivity(), "اسم هه عمل مكرر يرجى اختيار اسم جديد و المحاوله مره اخرى", Toast.LENGTH_SHORT);
                            TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                            v.setTextColor(Color.RED);
                            toast.show();
                        }else{
                            Toast toast = Toast.makeText(getActivity(), "عذرا حدث خطأ أثناء اجراء العملية  .. يرجي المحاوله لاحقا", Toast.LENGTH_SHORT);
                            TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
                            v.setTextColor(Color.RED);
                            toast.show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
//                        Toast.makeText(getActivity(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
                        if (volleyError instanceof ServerError)
                            Log.e("Error: ", "Server Error");
                        else if (volleyError instanceof TimeoutError)
                            Log.e("Error: ", "Timeout Error");
                        else if (volleyError instanceof NetworkError)
                            Log.e("Error: ", "Bad Network");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
//                for (int x= 0; x<imageSources.size(); x++) {
//                    String image = getStringImage(bitmap);
//                }

                //Creating parameters
                Map<String, String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put("userinfo", jsonInString);
                Log.e("userinfo", jsonInString);


                params.put("PackagesValueId", PackagesValueId+"");
                Log.e("PackagesValueId", PackagesValueId+"");

                params.put("Name", Name);
                Log.e("Name", Name);

                params.put("DelivaryType", te+"");
                Log.e("DelivaryType", te+"");

                params.put("CategoryId", CategoryId+"");
                Log.e("CategoryId", CategoryId+"");

                params.put("token", "?za[ZbGNz2B}MXYZ");

                //returning parameters
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                2,  // maxNumRetries = 2 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }
}
