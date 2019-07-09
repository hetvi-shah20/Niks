package com.example.niks.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.niks.Model.OrderDetails;
import com.example.niks.R;

import java.util.ArrayList;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder> {

    ArrayList<OrderDetails> listOrderDetails;
    Context mContext;

    public OrderDetailsAdapter(ArrayList<OrderDetails> listOrderDetails, Context mContext) {
        this.listOrderDetails = listOrderDetails;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();
        View mView = LayoutInflater.from(mContext).inflate(R.layout.order_details_row_item_layout, viewGroup, false);
        return new OrderDetailsAdapter.ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        OrderDetails order = listOrderDetails.get(i);
        holder.tvOrderPrice.setText(order.getPrice());
        holder.tvOrderName.setText(order.getProductName());
        holder.tvOrderQty.setText(order.getQuantity());
        holder.tvOrderAmount.setText("₹"+order.getAmount());
    }

    @Override
    public int getItemCount() {
        return listOrderDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvOrderName, tvOrderPrice, tvOrderQty, tvOrderAmount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderName = (TextView) itemView.findViewById(R.id.tv_order_details_name);
            tvOrderPrice = (TextView) itemView.findViewById(R.id.tv_order_details_price);
            tvOrderAmount = (TextView) itemView.findViewById(R.id.tv_order_details_amount);
            tvOrderQty = (TextView) itemView.findViewById(R.id.tv_order_details_qty);
        }
    }
}
