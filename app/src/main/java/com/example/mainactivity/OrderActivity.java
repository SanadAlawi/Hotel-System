package com.example.mainactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderActivity extends AppCompatActivity {

    private TextView orderActivity_tv_empty;

    private RecyclerView rv;
    private ArrayList<Order> orderList = new ArrayList<>();
    OrderAdapter orderAdapter = new OrderAdapter(this);


    private static final String ORDER_URL = "http://10.0.2.2/COMP438_Project/order.php";
    private static final String ORDER_DONE_URL = "http://10.0.2.2/COMP438_Project/order_done.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        setUpViews();
        getOrdersFromDB();
    }

    private void setUpViews() {
        orderActivity_tv_empty = findViewById(R.id.orderActivity_tv_empty);
        rv = findViewById(R.id.orderActivity_rv_orders);

        orderAdapter.setOnItemClickListener(new OrderAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                RemoveOrder(position);
            }
        });
    }

    private void getOrdersFromDB() {
        Thread orderThread = new Thread(new AsyncTask());
        orderThread.start();
    }

    private void RemoveOrder(int position){
        RemoveOrderFromDB(position);

        orderList.remove(position);
        orderAdapter.notifyItemRemoved(position);

    }

    private void RemoveOrderFromDB(int position) {
        Order order = orderList.get(position);

        StringRequest removeOrderRequest = new StringRequest(Request.Method.POST, ORDER_DONE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                if(response.equals("success"))
                    Toast.makeText(getApplicationContext(), "Order Removed Successfully", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("orderID", order.getOrderId()+"");

                return data;
            }
        };

        Singleton.getInstance(this).addToRequestQueue(removeOrderRequest);
    }




    private class AsyncTask implements Runnable{

        @Override
        public void run() {
            StringRequest orderRequest = new StringRequest(Request.Method.POST, ORDER_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray orderArray = new JSONArray(response);
                        if(orderArray.length() <= 0)
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    orderActivity_tv_empty.setVisibility(View.VISIBLE);
                                    orderActivity_tv_empty.setText("No Orders Yet!!!");
                                }
                            });
                        }
                        for (int i = 0 ; i < orderArray.length() ; i++)
                        {
                            JSONObject orderObject = orderArray.getJSONObject(i);

                            int roomID = orderObject.getInt("roomID");
                            int orderID = orderObject.getInt("orderID");
                            String orderType = orderObject.getString("orderType");
                            String orderDescription = orderObject.getString("orderDescription");

                            Order order = new Order(orderID, roomID, orderType, orderDescription);

                            orderList.add(order);
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    orderAdapter.setOrderList(orderList);


                    rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    rv.setHasFixedSize(true);
                    rv.setAdapter(orderAdapter);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


            Singleton.getInstance(getApplicationContext()).addToRequestQueue(orderRequest);

        }
    }




}