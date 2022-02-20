package com.example.mainactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

public class ReserveTypeActivity extends AppCompatActivity {

    private SharedPreferences sp;
    private SharedPreferences.Editor ed;

    private static final String ROOM_TYPE = "Type";

    private String type = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_type);
        setUp();
    }

    private void setUp() {
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        ed = sp.edit();
    }

    public void SuiteType(View view) {
        type = "Suite";
        ed.putString(ROOM_TYPE, type);
        ed.apply();

        Intent suiteIntent = new Intent(this, CustomerRoomsActivity.class);
        startActivity(suiteIntent);
    }


    public void RoomType(View view) {
        type = "Room";
        ed.putString(ROOM_TYPE, type);
        ed.apply();

        Intent roomIntent = new Intent(this, CustomerRoomsActivity.class);
        startActivity(roomIntent);
    }
}