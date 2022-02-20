package com.example.mainactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void HotelActivity(View view) {
        Intent hotelIntent = new Intent(this, HotelActivity.class);
        startActivity(hotelIntent);
    }

    public void CustomerActivity(View view) {
        Intent customerIntent = new Intent(this, CustomerProfileActivity.class);
        startActivity(customerIntent);
    }

    public void reservedActivity(View view) {
        Intent reservedIntent = new Intent(this, ReservedActivity.class);
        startActivity(reservedIntent);
    }

    public void FavoriteActivity(View view) {
        Intent favoriteIntent = new Intent(this, FavoriteActivity.class);
        startActivity(favoriteIntent);
    }

    public void RoomsActivity(View view) {
        Intent roomsIntent = new Intent(this, ReserveTypeActivity.class);
        startActivity(roomsIntent);
    }


    public void CurrencyConverter(View view) {
        Intent currencyConverterIntent = new Intent(this, CurrencyConverterApiActivity.class);
        startActivity(currencyConverterIntent);
    }
}