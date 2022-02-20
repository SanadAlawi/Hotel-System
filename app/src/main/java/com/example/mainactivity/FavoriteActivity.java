package com.example.mainactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FavoriteActivity extends AppCompatActivity {

    private SharedPreferences sp;
    private static final String CURRENT_USER = "User"; // Shared Preference Key, save current user object(customer, receptionist, admin)

    private RecyclerView rv;
    private ArrayList<Room> roomList = new ArrayList<>();
    private CustomerRoomAdapter customerRoomAdapter = new CustomerRoomAdapter(this);

    private String USER_ID;


    private static final String FAVORITE_URL = "http://10.0.2.2/COMP438_Project/favorite.php";
    private static final String REMOVE_FAVORITE_URL = "http://10.0.2.2/COMP438_Project/remove_favorite.php";
    private static final String ADD_FAVORITE_URL = "http://10.0.2.2/COMP438_Project/add_favorite.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        setUpViews();
        checkUser();

        GetFavoritesRoomsFromDB();
    }

    private void setUpViews() {
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        rv = findViewById(R.id.favoriteActivity_rv_favorites);

        customerRoomAdapter.setOnItemClickListener(new CustomerRoomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Room room = roomList.get(position);
                Toast.makeText(getApplicationContext(), "Room Type: " + room.getRoomType(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFavoriteClick(int position) {
                boolean love = roomList.get(position).getLove();
                if(love)
                {
                    removeFromFavoriteList(roomList.get(position).getRoomID());
                    roomList.get(position).setLove(false);
                    customerRoomAdapter.FilterList(roomList);
                } else
                {
                    addToFavoriteList(roomList.get(position).getRoomID());
                    roomList.get(position).setLove(true);
                    customerRoomAdapter.FilterList(roomList);
                }

            }
        });
    }



    private void checkUser() {
        try {
            JSONObject user = new JSONObject(sp.getString(CURRENT_USER, ""));
            USER_ID = user.getString("customerID");

        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }



    private void removeFromFavoriteList(int roomID){
        StringRequest removeFavoriteRequest = new StringRequest(Request.Method.POST, REMOVE_FAVORITE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("success"))
                    Toast.makeText(getApplicationContext(), "Remove From Favorite List", Toast.LENGTH_SHORT).show();
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
                data.put("roomID", roomID+"");
                data.put("customerID", USER_ID+"");

                return data;
            }
        };

        Singleton.getInstance(this).addToRequestQueue(removeFavoriteRequest);
    }

    private void addToFavoriteList(int roomID){
        StringRequest addFavoriteRequest = new StringRequest(Request.Method.POST, ADD_FAVORITE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("success"))
                    Toast.makeText(getApplicationContext(), "Add To Favorite List", Toast.LENGTH_SHORT).show();
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
                data.put("roomID", roomID+"");
                data.put("customerID", USER_ID+"");

                return data;
            }
        };

        Singleton.getInstance(this).addToRequestQueue(addFavoriteRequest);
    }



    private void GetFavoritesRoomsFromDB() {
        Thread favoriteThread = new Thread(new AsyncTask());
        favoriteThread.start();
    }



    private class AsyncTask implements Runnable{

        @Override
        public void run() {
            StringRequest favoriteRequest = new StringRequest(Request.Method.POST, FAVORITE_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray favoriteArray = new JSONArray(response);
                        for (int i = 0 ; i < favoriteArray.length() ; i++)
                        {
                            JSONObject favoriteRoomObject = favoriteArray.getJSONObject(i);

                            int roomID = favoriteRoomObject.getInt("roomID");
                            String person = favoriteRoomObject.getString("person");
                            String roomType = favoriteRoomObject.getString("roomType");
                            String im0 = favoriteRoomObject.getString("image0");
                            String im1 = favoriteRoomObject.getString("image1");
                            String im2 = favoriteRoomObject.getString("image2");
                            String costPerDay = favoriteRoomObject.getString("costPerDay");
                            String rating = favoriteRoomObject.getString("rating");



                            Room room = new Room(roomID, person, roomType, im0, im1, im2, costPerDay, rating, true);

                            roomList.add(room);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    customerRoomAdapter.setRoomList(roomList);
                    rv.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                    rv.setHasFixedSize(true);

                    rv.setAdapter(customerRoomAdapter);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    data.put("customerID", USER_ID);

                    return data;
                }
            };

            Singleton.getInstance(getApplicationContext()).addToRequestQueue(favoriteRequest);
        }



    }



}