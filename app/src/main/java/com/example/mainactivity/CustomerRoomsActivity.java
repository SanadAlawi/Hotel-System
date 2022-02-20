package com.example.mainactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
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

public class CustomerRoomsActivity extends AppCompatActivity {

    private static final String ROOM_TYPE = "Type"; // Shared Preference Key, save room type(suite room, normal room)
    private static final String CURRENT_USER = "User"; // Shared Preference Key, save current user object(customer, receptionist, admin)
    private static final String SELECT_ROOM = "Type"; // Shared Preference Key, save room object that we clicked
    private String USER_TYPE = "";
    private SharedPreferences sp;

    private RecyclerView rv;
    private ArrayList<Room> roomList = new ArrayList<>();
    private CustomerRoomAdapter customerRoomAdapter = new CustomerRoomAdapter(this);

    private EditText customerRoomsActivity_et_filterSearch;
    private Spinner customerRoomsActivity_sp_filterType;


    private String USER_ID;
    private String filterType = "";

//    private String AdminProperties = "0";


    private static final String URL = "http://10.0.2.2/COMP438_Project/customer_rooms.php";
    private static final String ADD_FAVORITE_URL = "http://10.0.2.2/COMP438_Project/add_favorite.php";
    private static final String REMOVE_FAVORITE_URL = "http://10.0.2.2/COMP438_Project/remove_favorite.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_rooms);

        setUpViews();
        setUpPreferences();
        checkUser();
        GetRoomsFromDB();

        checkFilter();
    }

    private void setUpViews() {
        customerRoomsActivity_et_filterSearch = findViewById(R.id.customerRoomsActivity_et_filterSearch);
        customerRoomsActivity_sp_filterType = findViewById(R.id.customerRoomsActivity_sp_filterType);
    }

    private void setUpPreferences() {
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        rv = findViewById(R.id.customerRoomsActivity_rv_room);
        customerRoomAdapter.setOnItemClickListener(new CustomerRoomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
//                if(USER_TYPE.equals("customer") || USER_TYPE.equals("receptionist"))
//                {
                    if(sp.getString(ROOM_TYPE, "").equals("Suite")){
                        Intent suiteIntent = new Intent(getApplicationContext(), SuiteRoomActivity.class);
                        suiteIntent.putExtra(SELECT_ROOM, roomList.get(position));
                        startActivity(suiteIntent);
                    } else if(sp.getString(ROOM_TYPE, "").equals("Room")){
                        Intent roomIntent = new Intent(getApplicationContext(), NormalRoomActivity.class);
                        roomIntent.putExtra(SELECT_ROOM, roomList.get(position));
                        startActivity(roomIntent);
                    } else{
                        Toast.makeText(getApplicationContext(), "NON", Toast.LENGTH_SHORT).show();
                    }
//                }
            }

            @Override
            public void onFavoriteClick(int position) {
//                Toast.makeText(getApplicationContext(), "This Is Favorite", Toast.LENGTH_SHORT).show();
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

    private void checkUser() {
        try {
            JSONObject user = new JSONObject(sp.getString(CURRENT_USER, ""));
            USER_TYPE = user.getString("userType");

            if(USER_TYPE.equals("customer")) {
                USER_ID = user.getString("customerID");
            }

        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void GetRoomsFromDB() {

        Thread roomsThread = new Thread(new AsyncTask());
        roomsThread.start();
    }

    private void checkFilter() {
        customerRoomsActivity_sp_filterType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterType = customerRoomsActivity_sp_filterType.getSelectedItem().toString().toLowerCase().trim();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        customerRoomsActivity_et_filterSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty())
                    Filter(s.toString());
                else
                    customerRoomAdapter.OriginalList();
            }
        });
    }

    private void Filter(String text){
        ArrayList<Room> filterRoom = new ArrayList<>();

        for (Room room : roomList)
        {
            if(filterType.equals("person"))
            {
                if(room.getPersonNumber().equals(text))
                    filterRoom.add(room);
            }

            if(filterType.equals("rating"))
            {
                if(!text.isEmpty() && Float.parseFloat(room.getRating()) == Float.parseFloat(text))
                    filterRoom.add(room);
            }

            if(filterType.equals("cost"))
            {
                if(room.getCostPerDay().equals(text))
                    filterRoom.add(room);
            }
        }

        customerRoomAdapter.FilterList(filterRoom);
    }



    private class AsyncTask implements Runnable{
        @Override
        public void run() {
            StringRequest customerRoomsRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONArray roomArray = new JSONArray(response);
                        for (int i = 0; i < roomArray.length(); i++) {
                            JSONObject roomObject = roomArray.getJSONObject(i);

                            int roomID = roomObject.getInt("roomID");
                            String person = roomObject.getString("person");
                            String roomType = roomObject.getString("roomType");
                            String im0 = roomObject.getString("image0");
                            String im1 = roomObject.getString("image1");
                            String im2 = roomObject.getString("image2");
                            String costPerDay = roomObject.getString("costPerDay");
                            String rating = roomObject.getString("rating");

                            boolean love = false;
                            String userID = roomObject.getString("customerID");
                            if(!userID.equals("null"))
                            {
                                love = true;
                            }

                            Room room = new Room(roomID, person, roomType, im0, im1, im2, costPerDay, rating, love);

                            roomList.add(room);

                        }
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    customerRoomAdapter.setRoomList(roomList);
                    rv.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                    rv.setHasFixedSize(true);

                    rv.setAdapter(customerRoomAdapter);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Error From PHP Code:  " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    data.put("roomType", sp.getString(ROOM_TYPE, ""));
                    data.put("customerID", USER_ID);
//                    data.put("admin", AdminProperties);


                    return data;
                }
            };

            Singleton.getInstance(getApplicationContext()).addToRequestQueue(customerRoomsRequest);

        }
    }


}