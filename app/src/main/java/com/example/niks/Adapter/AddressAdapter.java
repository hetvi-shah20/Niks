package com.example.niks.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.niks.ApiHelper.JSONField;
import com.example.niks.Model.Shipping;
import com.example.niks.PlaceOrderActivity;
import com.example.niks.R;
import com.example.niks.UserSessionManager;

import java.util.ArrayList;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {
    private Context context ;
    ArrayList<Shipping> addressList;
    UserSessionManager userSessionManager ;



    public AddressAdapter(Context context, ArrayList<Shipping> addressList) {
        this.context = context;
        this.addressList = addressList;
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
        viewHolder.rbCheckAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, PlaceOrderActivity.class);
                i.putExtra(JSONField.SHIPPING_ID,shipping.getShipping_id());
                i.putExtra(JSONField.SHIPPING_FLATNO,shipping.getShipping_flatno());
                i.putExtra(JSONField.SHIPPING_STREET,shipping.getShipping_street());
                i.putExtra(JSONField.SHIPPING_LANDMARK,shipping.getShipping_landmark());
                i.putExtra(JSONField.SHIPPING_AREA,shipping.getShipping_area());
                i.putExtra(JSONField.SHIPPING_CITY,shipping.getShipping_city());
                i.putExtra(JSONField.SHIPPING_PINCODE,shipping.getShipping_pincode());
                Log.d("id",shipping.getShipping_id());
                context.startActivity(i);
            }
        });

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
