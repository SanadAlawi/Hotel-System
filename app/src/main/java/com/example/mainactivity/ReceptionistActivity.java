package com.example.mainactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONObject;

public class ReceptionistActivity extends AppCompatActivity {

    private SharedPreferences sp;
    private SharedPreferences.Editor ed;

    private static final String ROOM_TYPE = "Type";
    private static final String CURRENT_USER = "User";

    private String type = "";
    private String receptionistService = "";

    private Button receptionistActivity_btn_orders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receptionist);

        setUp();

        checkReceptionist();

        if(!receptionistService.equals("Room Service"))
            receptionistActivity_btn_orders.setVisibility(View.GONE);

    }

    private void setUp() {
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        ed = sp.edit();

        receptionistActivity_btn_orders = findViewById(R.id.receptionistActivity_btn_orders);
    }

    private void checkReceptionist(){
        try{
            JSONObject receptionist = new JSONObject(sp.getString(CURRENT_USER, ""));
//            Toast.makeText(getApplicationContext(), receptionist.getString("receptionistService"), Toast.LENGTH_LONG).show();
            receptionistService = receptionist.getString("receptionistService");
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void SuiteRoom(View view) {
        type = "Suite";
        ed.putString(ROOM_TYPE, type);
        ed.apply();


        Intent suiteIntent = new Intent(this, AdminRoomsActivity.class);
        startActivity(suiteIntent);
    }

    public void NormalRoom(View view) {
        type = "Room";
        ed.putString(ROOM_TYPE, type);
        ed.apply();

        Intent normalIntent = new Intent(this, AdminRoomsActivity.class);
        startActivity(normalIntent);
    }

    public void Orders(View view) {
        Intent orderIntent = new Intent(this, OrderActivity.class);
        startActivity(orderIntent);
    }
}