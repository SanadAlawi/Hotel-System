package com.example.mainactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;

public class AdminActivity extends AppCompatActivity {

    private SharedPreferences sp;
    private SharedPreferences.Editor ed;

    private static final String ROOM_TYPE = "Type"; // Shared Preference Key, save room type(suite room, normal room)

    private String type = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        setUpPreferences();
    }

    private void setUpPreferences() {
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        ed = sp.edit();
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

    public void SignUpEmployee(View view) {
        Intent signupIntent = new Intent(this, SignupReceptionistActivity.class);
        startActivity(signupIntent);
    }

    public void receptionistView(View view) {
        Intent receptionistIntent = new Intent(this, ReceptionistsActivity.class);
        startActivity(receptionistIntent);
    }
}