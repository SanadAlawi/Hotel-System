package com.example.mainactivity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.DatePicker;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SuiteRoomActivity extends AppCompatActivity {

    private TextView suiteRoomActivity_tv_dateStart;
    private TextView suiteRoomActivity_tv_dateEnd;
    private TextView suiteRoomActivity_btn_reserveButton;
    private TextView suiteRoomActivity_tv_costPerDay;
    private TextView suiteRoomActivity_tv_totalCost;
    private HorizontalScrollView suiteRoomActivity_lv_suitePhotos;
    private TextView suiteRoomActivity_tv_suiteBed;

    private ImageView suiteRoomActivity_im_Photo0;
    private ImageView suiteRoomActivity_im_Photo1;
    private ImageView suiteRoomActivity_im_Photo2;


    private SharedPreferences sp;

    private static final String CURRENT_USER = "User"; // Shared Preference Key, save current user object(customer, receptionist, admin)
    private String USER_ID;
    private String ROOM_ID;
    private String TOTAL_COST;

    private static final String SELECT_ROOM = "Type"; // Shared Preference Key, save room object that we clicked

    private Room SelectRoom;
    private LocalDate startDate;
    private LocalDate endDate;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MMMM/d", Locale.US);

    private static final String URL = "http://10.0.2.2/COMP438_Project/reserve.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suite_room);

        setUpViews();
        checkRoom();
        setUpPreferences();

    }


    private void setUpViews() {
        suiteRoomActivity_tv_dateStart = findViewById(R.id.suiteRoomActivity_tv_dateStart);
        suiteRoomActivity_tv_dateEnd = findViewById(R.id.suiteRoomActivity_tv_dateEnd);
        suiteRoomActivity_btn_reserveButton = findViewById(R.id.suiteRoomActivity_btn_reserveButton);
        suiteRoomActivity_tv_costPerDay = findViewById(R.id.suiteRoomActivity_tv_costPerDay);
        suiteRoomActivity_tv_totalCost = findViewById(R.id.suiteRoomActivity_tv_totalCost);
        suiteRoomActivity_tv_suiteBed = findViewById(R.id.suiteRoomActivity_tv_suiteBed);
        suiteRoomActivity_lv_suitePhotos = findViewById(R.id.suiteRoomActivity_lv_suitePhotos);

        suiteRoomActivity_im_Photo0 = findViewById(R.id.suiteRoomActivity_im_Photo0);
        suiteRoomActivity_im_Photo1 = findViewById(R.id.suiteRoomActivity_im_Photo1);
        suiteRoomActivity_im_Photo2 = findViewById(R.id.suiteRoomActivity_im_Photo2);

    }

    private void checkRoom() {
        SelectRoom = (Room) getIntent().getSerializableExtra(SELECT_ROOM);
        ROOM_ID = String.valueOf(SelectRoom.getRoomID());

        Glide.with(this).load(SelectRoom.getImage0()).into(suiteRoomActivity_im_Photo0);
        Glide.with(this).load(SelectRoom.getImage1()).into(suiteRoomActivity_im_Photo1);
        Glide.with(this).load(SelectRoom.getImage2()).into(suiteRoomActivity_im_Photo2);

        suiteRoomActivity_tv_costPerDay.setText(SelectRoom.getCostPerDay()+"$ a Day");
        suiteRoomActivity_tv_suiteBed.setText("1_ Bed Number: "+SelectRoom.getPersonNumber()+" Bed");
    }


    private void setUpPreferences() {
        sp = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            JSONObject user = new JSONObject(sp.getString(CURRENT_USER, ""));

            String userType = user.getString("userType");
            if(userType.equals("customer"))
                USER_ID = user.getString("customerID");
            if(userType.equals("receptionist")){
                suiteRoomActivity_tv_dateStart.setEnabled(false);
                suiteRoomActivity_tv_dateEnd.setEnabled(false);
                suiteRoomActivity_btn_reserveButton.setEnabled(false);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void StartDate(View view) {
        PutDate("Start");
    }

    public void EndDate(View view) {
        PutDate("End");
    }

    private void PutDate(String startORend) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month += 1;
                String date = month+"-"+day+"-"+year;
                Calendar c = Calendar.getInstance();
                c.set(year, month, day);
                if(startORend.equals("Start"))
                {
                    suiteRoomActivity_tv_dateStart.setText(date);
                    startDate = LocalDate.of(year, month, day);
                    Toast.makeText(getApplicationContext(), startDate+":"+startDate.getYear(), Toast.LENGTH_SHORT).show();
                }else if(startORend.equals("End"))
                {
                    suiteRoomActivity_tv_dateEnd.setText(date);
                    endDate = LocalDate.of(year, month, day);;
                }
            }
        };


        DatePickerDialog datePicker = new DatePickerDialog(
                this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateSetListener, year, month, day);
        datePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePicker.show();

        if(startDate != null && endDate != null){
            long days = daysBetween();
            TOTAL_COST = days * Integer.parseInt(SelectRoom.getCostPerDay())+"";
            suiteRoomActivity_tv_totalCost.setText("Total Cost: "+(TOTAL_COST +"$"));
        }
    }

    private long daysBetween() {
        int difference = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            difference = (int) ChronoUnit.DAYS.between(startDate, endDate);
        }
        return Math.abs(difference) + 1;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void Reserve(View view) {
        Thread reserveThread = new Thread(new AsyncTask(USER_ID, ROOM_ID, TOTAL_COST, startDate, endDate));
        reserveThread.start();
    }





    private class AsyncTask implements Runnable{

        private String userID;
        private String roomID;
        private String cost;
        private String dataStart;
        private String dataEnd;

        @RequiresApi(api = Build.VERSION_CODES.O)
        public AsyncTask(String userID, String roomID, String cost, LocalDate dataStart, LocalDate dataEnd) {
            this.userID = userID;
            this.roomID = roomID;
            this.cost = cost;
            this.dataStart = dataStart.getYear()+"-"+dataStart.getMonthValue()+"-"+dataStart.getDayOfMonth();
            this.dataEnd = dataEnd.getYear()+"-"+dataEnd.getMonthValue()+"-"+dataEnd.getDayOfMonth();
            Toast.makeText(getApplicationContext(), "Start Date" + this.dataStart + "\nEnd Date" +this.dataEnd, Toast.LENGTH_LONG).show();
        }

        @Override
        public void run() {
            StringRequest reserveRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (response.equals("success"))
                        Toast.makeText(getApplicationContext(), "Reserved Successfully", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "Response Error!!!", Toast.LENGTH_LONG).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    data.put("customerID", userID);
                    data.put("roomID", roomID);
                    data.put("cost", cost);
                    data.put("dateStart", dataStart);
                    data.put("dateEnd", dataEnd);

                    return data;
                }
            };

            Singleton.getInstance(getApplicationContext()).addToRequestQueue(reserveRequest);
        }

    }






}