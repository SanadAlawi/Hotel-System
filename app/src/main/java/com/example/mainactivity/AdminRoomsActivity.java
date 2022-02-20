package com.example.mainactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
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

public class AdminRoomsActivity extends AppCompatActivity {


    private static final String ROOM_TYPE = "Type"; // Shared Preference Key, save room type(suite room, normal room)
    private static final String CURRENT_USER = "User"; // Shared Preference Key, save current user object(customer, receptionist, admin)
    private static final String SELECT_ROOM = "Type"; // Shared Preference Key, save room object that we clicked
    private String USER_TYPE = "";
    private SharedPreferences sp;


    private RecyclerView rv;
    private ArrayList<Room> roomList = new ArrayList<>();
    private AdminRoomAdapter adminRoomAdapter = new AdminRoomAdapter(this);

    private EditText adminRoomsActivity_ed_filterSearch;
    private Spinner adminRoomsActivity_sp_filterType;

    private String USER_ID;
    private String filterType = "";

    private static final String URL = "http://10.0.2.2/COMP438_Project/admin_rooms.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_rooms);

        setUpViews();
        setUpPreferences();
        checkUser();
        GetRoomsFromDB();

        checkFilter();
    }


    private void setUpViews() {
        adminRoomsActivity_ed_filterSearch = findViewById(R.id.adminRoomsActivity_ed_filterSearch);
        adminRoomsActivity_sp_filterType = findViewById(R.id.adminRoomsActivity_sp_filterType);
    }

    private void setUpPreferences() {
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        rv = findViewById(R.id.adminRoomsActivity_rv_room);
        adminRoomAdapter.setOnItemClickListener(new AdminRoomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });

    }



    private void checkUser() {
        try {
            JSONObject user = new JSONObject(sp.getString(CURRENT_USER, ""));
            USER_TYPE = user.getString("userType");

        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }



    private void checkFilter() {
        adminRoomsActivity_sp_filterType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterType = adminRoomsActivity_sp_filterType.getSelectedItem().toString().toLowerCase().trim();

                if(filterType.equals("reserved") || filterType.equals("not reserved"))
                {
                    filterReserved(filterType);
                    adminRoomsActivity_ed_filterSearch.setEnabled(false);
                }else
                    adminRoomsActivity_ed_filterSearch.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adminRoomsActivity_ed_filterSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
//                filterType = customerRoomsActivity_sp_filterType.getSelectedItem().toString().toLowerCase().trim();

                if(!s.toString().isEmpty())
                    Filter(s.toString());
                else
                    adminRoomAdapter.OriginalList();
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

        adminRoomAdapter.FilterList(filterRoom);
    }

    private void filterReserved(String isReserved){
        ArrayList<Room> filter = new ArrayList<>();

        for(Room room : roomList)
        {
            if(room.getIsReserved().toLowerCase().trim().equals(isReserved))
            {
                filter.add(room);
            }
        }

        adminRoomAdapter.FilterList(filter);
    }

    private void GetRoomsFromDB() {

        Thread roomsThread = new Thread(new AsyncTask());
        roomsThread.start();
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
                            String userID = roomObject.getString("customerID");
                            String reserved = "Reserved";
                            if(userID.equals("null"))
                            {
                                reserved = "Not Reserved";
                            }


                            Room room = new Room(roomID, person, roomType, im0, im1, im2, costPerDay, rating,userID, reserved);

                            roomList.add(room);

                        }
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    adminRoomAdapter.setRoomList(roomList);
                    rv.setLayoutManager(new LinearLayoutManager(getBaseContext()));
                    rv.setHasFixedSize(true);

                    rv.setAdapter(adminRoomAdapter);

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

                    return data;
                }
            };

            Singleton.getInstance(getApplicationContext()).addToRequestQueue(customerRoomsRequest);

        }
    }










}