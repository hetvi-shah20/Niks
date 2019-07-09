package com.example.niks.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.niks.AddAddressActivity;
import com.example.niks.ApiHelper.JSONField;
import com.example.niks.ApiHelper.WebURL;
import com.example.niks.Model.Shipping;
import com.example.niks.MyAddressActivity;
import com.example.niks.Navigation;
import com.example.niks.PlaceOrderActivity;
import com.example.niks.R;
import com.example.niks.UserSessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {
    private Context context ;
    ArrayList<Shipping> addressList;
    UserSessionManager userSessionManager ;
    String totalitems,totalamounts,productArray;
    String tti;
    String shippingid;


    public AddressAdapter(Context context, ArrayList<Shipping> addressList, String totalitems, String totalamounts, String productArray) {
        this.context = context;
        this.addressList = addressList;
        this.totalitems = totalitems;
        this.totalamounts = totalamounts;
        this.productArray = productArray;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.row_address_layout,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Shipping shipping = addressList.get(i);
        userSessionManager = new UserSessionManager(context);
        viewHolder.usernameAdd.setText(userSessionManager.getUserName());
        String flatNoAdd = shipping.getShipping_flatno();
        viewHolder.flatNoAdd.setText(flatNoAdd);
        String landmarkAdd = shipping.getShipping_landmark();
        viewHolder.landmarkAdd.setText(landmarkAdd);
        String cityAdd =  shipping.getShipping_city();
        viewHolder.cityAdd.setText(cityAdd);
//        shippingid = shipping.getShipping_id();
//        Log.d("shipping id",shippingid);
        viewHolder.rbCheckAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to place order on thi address?");
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    Log.d("product Array",productArray);
                        shippingid = shipping.getShipping_id();
                        Log.d("shipping id",shippingid);
                    PlaceOrder();





                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context, MyAddressActivity.class);
                        context.startActivity(intent);
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }

    private void PlaceOrder() {

       final StringRequest stringRequest = new StringRequest(Request.Method.POST, WebURL.INSERT_ORDER_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseOrderResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();

                params.put(JSONField.KEY_USER_ID, userSessionManager.getUserId());
                params.put(JSONField.TOTAL_AMOUNT, totalamounts);
                params.put(JSONField.ORDER_DETAILS, productArray);
                params.put(JSONField.SHPPPING_NAME, userSessionManager.getUserName());
                params.put(JSONField.SHIPPING_MOBILE, userSessionManager.getUserPhone());
                params.put(JSONField.SHIPPING_ID,shippingid);
                return params;
            }



        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void parseOrderResponse(String response) {

        Log.d("INSERT_RESPONSE", response);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(response);
            int flag = jsonObject.optInt(JSONField.FLAG);
            String strMessage = jsonObject.optString(JSONField.MESSAGE);

            if (flag == 1) {

                showOrderPlaceDialog(strMessage);
            } else {

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showOrderPlaceDialog(String strMessage) {



            final Dialog orderDialog = new Dialog(context);
            orderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            orderDialog.setCanceledOnTouchOutside(false);
            orderDialog.setCancelable(false);
            orderDialog.setContentView(R.layout.order_placed_succesfully_pop_up);
            orderDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            orderDialog.getWindow().getDecorView().setBackground(new ColorDrawable(Color.TRANSPARENT));
            Button btnContinue = orderDialog.findViewById(R.id.btn_place_dialog_continue);
            btnContinue.setEnabled(true);
            btnContinue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderDialog.dismiss();
                    TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
                    taskStackBuilder.addParentStack(Navigation.class);
                    Intent intent = new Intent(context, Navigation.class);
                    taskStackBuilder.addNextIntentWithParentStack(intent);
                    taskStackBuilder.startActivities();
                }
            });
            orderDialog.show();

        }


    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView  usernameAdd,flatNoAdd,landmarkAdd,cityAdd;
        RadioButton rbCheckAddress;
        Drawable drawable;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameAdd = itemView.findViewById(R.id.usernameAdd);
            flatNoAdd = itemView.findViewById(R.id.flatNoAdd);
            landmarkAdd = itemView.findViewById(R.id.landmarkAdd);
            cityAdd = itemView.findViewById(R.id.cityAdd);
            rbCheckAddress = itemView.findViewById(R.id.rbCheckAddress);
            drawable =itemView.getResources().getDrawable( R.drawable.ic_edit );
        }
    }
}
