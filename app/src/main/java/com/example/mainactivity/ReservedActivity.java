package com.example.mainactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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

public class ReservedActivity extends AppCompatActivity {

    private TextView reservedRoomsActivity_tv_notReserved;

    private RecyclerView rv;
    ReserveAdapter reserveAdapter = new ReserveAdapter(this);
    private ArrayList<Reserve> reserveList = new ArrayList<>();

    // Preferences
    private SharedPreferences sp;
//    private SharedPreferences.Editor ed;

    private static final String CURRENT_USER = "User";
    private static final String SELECTED_ROOM = "Room";
    private String USER_ID;
    private String ROOM_ID; // Specific for selected room want deleted

    private static final String RESERVE_URL = "http://10.0.2.2/COMP438_Project/reserved_rooms.php";
    private static final String REMOVE_RESERVATION_URL = "http://10.0.2.2/COMP438_Project/remove_reservation.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserved_rooms);


        sp = PreferenceManager.getDefaultSharedPreferences(this);

        setUpViews();

        setUpValues();
        GetRoomsFromDB();
    }

    private void setUpViews() {
        rv = findViewById(R.id.reservedRoomsActivity_rv_reserved);
        reservedRoomsActivity_tv_notReserved = findViewById(R.id.reservedRoomsActivity_tv_notReserved);

        reserveAdapter.setOnItemClickListener(new ReserveAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent serviceIntent = new Intent(getApplicationContext(), ServiceActivity.class);
                serviceIntent.putExtra(SELECTED_ROOM, reserveList.get(position));
                startActivity(serviceIntent);
            }

            @Override
            public void onRemoveClick(int position) {
                ROOM_ID = reserveList.get(position).getRoomID()+"";
                RemoveReservation();

                reserveList.remove(position);
                reserveAdapter.notifyItemRemoved(position);

            }
        });
    }

    private void setUpValues() {
        try {
            JSONObject user = new JSONObject(sp.getString(CURRENT_USER, ""));
            USER_ID = user.getString("customerID");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void GetRoomsFromDB() {
        Thread roomsThread = new Thread(new AsyncTask());
        roomsThread.start();
    }


    private void RemoveReservation(){
        StringRequest reservationRemoveRequest = new StringRequest(Request.Method.POST, REMOVE_RESERVATION_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("customerID", USER_ID);
                data.put("roomID", ROOM_ID);

                return data;
            }
        };

        Singleton.getInstance(this).addToRequestQueue(reservationRemoveRequest);
    }


    private class AsyncTask implements Runnable{
        @Override
        public void run() {
            StringRequest reservedRequest = new StringRequest(Request.Method.POST, RESERVE_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONArray roomArray = new JSONArray(response);
                        if(roomArray.length() <= 0)
                        {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    reservedRoomsActivity_tv_notReserved.setVisibility(View.VISIBLE);
                                    reservedRoomsActivity_tv_notReserved.setText("No reservation for you, for the reason that you did not book a room or the reservation period has expired!!!");
                                }
                            });
                        }

                        for (int i = 0; i < roomArray.length(); i++)
                        {
                            JSONObject reserveObject = roomArray.getJSONObject(i);

                            String roomID = reserveObject.getString("roomID");
                            String im0 = reserveObject.getString("image0");
                            String dateStart = reserveObject.getString("dateStart");
                            String dateEnd = reserveObject.getString("dateEnd");
                            int cost = reserveObject.getInt("cost");
                            double rating = reserveObject.getDouble("rating");

                            Reserve reserve = new Reserve(roomID, dateStart, dateEnd, cost, im0, rating);

                            reserveList.add(reserve);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    reserveAdapter.setReserveList(reserveList);
                    rv.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                    rv.setHasFixedSize(true);
                    rv.setAdapter(reserveAdapter);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    data.put("customerID", USER_ID);

                    return data;
                }
            };


            Singleton.getInstance(getApplicationContext()).addToRequestQueue(reservedRequest);

        }
    }







}