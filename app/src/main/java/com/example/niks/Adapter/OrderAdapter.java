package com.example.niks.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.niks.Model.Order;
import com.example.niks.OrderHistoryActivity;
import com.example.niks.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class OrderAdapter  extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private Context mContext;
    ArrayList<Order> listOrder;

    SetOnOrderListItemClickListener onOrderListItemClickListener;

    public SetOnOrderListItemClickListener getOnOrderListItemClickListener() {
        return onOrderListItemClickListener;
    }

    public void setOnOrderListItemClickListener(SetOnOrderListItemClickListener onOrderListItemClickListener) {
        this.onOrderListItemClickListener = onOrderListItemClickListener;
    }

    public OrderAdapter(Context mContext, ArrayList<Order> listOrder) {
        this.mContext = mContext;
        this.listOrder = listOrder;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();
        View mView = LayoutInflater.from(mContext).inflate(R.layout.order_row_item, viewGroup, false);
        return new OrderAdapter.ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        final Order order = listOrder.get(i);
        viewHolder.tvOrderID.setText(order.getOrderID());

        String Total = order.getTotalAmount();
        double total = Double.parseDouble(Total);
        DecimalFormat df1 = new DecimalFormat("0.00");
        df1.setMaximumFractionDigits(2);
        String totalAmount = (df1.format(total));
        Log.d("Total Amount", totalAmount);

        viewHolder.tvTotalAmount.setText("₹"+totalAmount);
        viewHolder.tvDate.setText(order.getDate());
        viewHolder.ll_order_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, OrderHistoryActivity.class);
                i.putExtra("OrderID", order.getOrderID());
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listOrder.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvOrderID, tvTotalAmount, tvDate;
        LinearLayout ll_order_list;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderID = (TextView) itemView.findViewById(R.id.orderID);
            tvTotalAmount = (TextView) itemView.findViewById(R.id.total_amount);
            tvDate = (TextView) itemView.findViewById(R.id.date);

            ll_order_list = (LinearLayout) itemView.findViewById(R.id.ll_order_list);
        }
    }


    public interface SetOnOrderListItemClickListener {
        public void onOrderListItemClicked(Order order, int position);
    }
}
