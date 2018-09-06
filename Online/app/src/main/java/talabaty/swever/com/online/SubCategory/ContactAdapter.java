package talabaty.swever.com.online.SubCategory;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import talabaty.swever.com.online.Fields.MostTrend.Product;
import talabaty.swever.com.online.Fields.MostViewed.Contact;
import talabaty.swever.com.online.R;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.Vholder> {

    Context context;
    List<Contact> contacts;
    FragmentManager fragmentManager;
    List<Product> product_List;
    List<Integer> subCatId;

    private int lastPosition = -1;

    public ContactAdapter(Context context, List<Contact> contact) {
        this.context = context;
        contacts = new ArrayList<>();
        this.contacts = contact;
    }

    @NonNull
    @Override
    public Vholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_subcategory_contact, parent, false);
        fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        product_List = new ArrayList<>();
        return new Vholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Vholder holder, final int position) {

        setAnimation(holder.itemView, position);

        holder.name.setText(contacts.get(position).getName());
        if (!contacts.get(position).getCompany_logo().isEmpty()) {
            Picasso.with(context).load(contacts.get(position).getCompany_logo()).into(holder.logo);
        }

        holder.move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.select_dialog_item);
                //Todo: First Connect To Api To get List Of SubCategory For One Contact
                loadSubCategory(contacts.get(position).getId(), arrayAdapter);


            }
        });
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class Vholder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView logo;
        Button move;

        public Vholder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.company_name);
            logo = itemView.findViewById(R.id.company_logo);
            move = itemView.findViewById(R.id.move);
        }

    }

    private void loadSubCategory(final int ID, final ArrayAdapter<String> arrayAdapter) {
        subCatId = new ArrayList<>();
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("جارى تحميل البيانات ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://onlineapi.sweverteam.com/Categories/ListOfCategories",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        try {

                            JSONObject object = new JSONObject(response);
                            JSONArray array = object.getJSONArray("Categories");
                            if (array.length() > 0) {
                                //Todo: Show AlertDialog And List SubCategory Of One Contact
                                AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
                                builderSingle.setIcon(R.drawable.ic_menu_camera);
                                builderSingle.setTitle("من فضلك أختر احد من الأقسام لعرض المنتجات");
                                //Todo: Fill SubCategory Of One Contact From Api
                                for (int x = 0; x < array.length(); x++) {
                                    JSONObject object1 = array.getJSONObject(x);
                                    arrayAdapter.add(object1.getString("Name"));
                                    subCatId.add(object1.getInt("Id"));
                                }
                                builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String strName = arrayAdapter.getItem(which);
                                        System.out.println("Name: " + strName);
                                        System.out.println("Id: "+subCatId.get(arrayAdapter.getPosition(strName)));
                                        //Todo: Second Connect To Api To get List Of Products Of One SubCategory For Same Contact
                                        loadProductOfSubCategory(subCatId.get(arrayAdapter.getPosition(strName)),product_List);

                                    }
                                });
                                builderSingle.show();

                            } else {
                                Toast.makeText(context, "عذرا لا يوجد اقسام بداخل جهه العمل حاليا", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(context, "خطأ إثناء الاتصال بالخادم", Toast.LENGTH_SHORT).show();
                else if (error instanceof NetworkError)
                    Toast.makeText(context, "خطأ فى شبكه الانترنت", Toast.LENGTH_SHORT).show();
                else if (error instanceof TimeoutError)
                    Toast.makeText(context, "خطأ فى مده الانتظار", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("ShopId", ID + "");
                map.put("token", "?za[ZbGNz2B}MXYZ");
                return map;
            }
        };
//        Volley.newRequestQueue(getActivity()).add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                2,  // maxNumRetries = 2 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(stringRequest);
    }

    private void loadProductOfSubCategory(final int ID, final List<Product> product_List) {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("جارى تحميل البيانات ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://onlineapi.sweverteam.com/Products/ListByCategory/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        progressDialog.dismiss();
                        try {

                            JSONObject object = new JSONObject(response);
                            JSONArray array = object.getJSONArray("List");
                            if (array.length() > 0) {
                                if (product_List.size() > 0) {
                                    for (int i = 0; i < product_List.size(); i++) {
                                        product_List.remove(0);
                                    }

                                }
                                //Todo: Solve Problem (Displaying Contacts Not Products) to Display Products
                                //Todo: Fill Products List From Api And Pass To FragmentProduct()
                                for (int x = 0; x < array.length(); x++) {
                                    JSONObject object1 = array.getJSONObject(x);
                                    Product r = new Product(object1.getInt("Id"),
                                            object1.getString("Name"),
                                            "http://selltlbaty.sweverteam.com" + object1.getString("Photo"),
                                            (float) object1.getDouble("Price"),
                                            (float) object1.getDouble("Sale"),
                                            (float) object1.getDouble("Rate")
                                    );
                                    product_List.add(r);
                                }
                                Log.e("List",product_List.toArray().length+"");
                                fragmentManager.beginTransaction()
                                        .replace(R.id.rec_product, new FragmentProduct().setList(product_List)).commit();

                            } else {
                                Toast.makeText(context, "عذرا لا يوجد منتجات حاليا فى ذلك القسم", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(context, "خطأ إثناء الاتصال بالخادم", Toast.LENGTH_SHORT).show();
                else if (error instanceof NetworkError)
                    Toast.makeText(context, "خطأ فى شبكه الانترنت", Toast.LENGTH_SHORT).show();
                else if (error instanceof TimeoutError)
                    Toast.makeText(context, "خطأ فى مده الانتظار", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("CategeoryId", ID + "");
                map.put("x", "0");
                map.put("count", "10000000");
                map.put("type", "1");
                map.put("token", "?za[ZbGNz2B}MXYZ");
                return map;
            }
        };
//        Volley.newRequestQueue(getActivity()).add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                2,  // maxNumRetries = 2 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(context).add(stringRequest);
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

}
