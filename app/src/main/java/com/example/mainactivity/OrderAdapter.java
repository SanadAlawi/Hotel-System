package com.example.mainactivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderHolder> {

    private ArrayList<Order> orderList;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }


    public OrderAdapter(ArrayList<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    public OrderAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public OrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_layout, parent, false);
        return new OrderHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHolder holder, int position) {
        Order order = orderList.get(position);

        holder.orderType.setText(order.getOrderType());
        holder.roomID.setText("Room Number: " + order.getRoomId()+"");
        holder.orderDescription.setText(order.getOrderDescription());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }


    public ArrayList<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(ArrayList<Order> orderList) {
        this.orderList = orderList;
    }

    public class OrderHolder extends RecyclerView.ViewHolder{
        private TextView orderType;
        private TextView roomID;
        private TextView orderDescription;
        private Button orderDone;
        private static final String URL = "http://10.0.2.2/COMP438_Project/search.php";
        public OrderHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            orderType = itemView.findViewById(R.id.orderActivity_tv_orderType);
            roomID = itemView.findViewById(R.id.orderActivity_tv_roomID);
            orderDescription = itemView.findViewById(R.id.orderActivity_tv_orderDescription);
            orderDone = itemView.findViewById(R.id.orderActivity_btn_orderDone);

            orderDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                    {
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION)
                        {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });

        }
    }


}
