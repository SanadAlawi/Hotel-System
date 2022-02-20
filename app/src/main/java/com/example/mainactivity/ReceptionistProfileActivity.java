package com.example.mainactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ReceptionistProfileActivity extends AppCompatActivity {

    private EditText receptionistProfileActivity_et_ID;
    private EditText receptionistProfileActivity_et_name;
    private EditText receptionistProfileActivity_et_Address;
    private EditText receptionistProfileActivity_et_phone;
    private EditText receptionistProfileActivity_et_salary;
    private EditText receptionistProfileActivity_et_hourWork;
    private EditText receptionistProfileActivity_et_username;
    private EditText receptionistProfileActivity_et_email;
    private Spinner receptionistProfileActivity_sp_service;

    private static final String SELECTED_RECEPTIONIST = "Receptionist";
    private Receptionist receptionist;

    private static final String URL = "http://10.0.2.2/COMP438_Project/edit_receptionist.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receptionist_profile);

        setUpViews();
        checkReceptionist();
        putReceptionistInformation();

        setUpListener();
    }

    private void setUpViews() {
        receptionistProfileActivity_et_ID = findViewById(R.id.receptionistProfileActivity_et_ID);
        receptionistProfileActivity_et_name = findViewById(R.id.receptionistProfileActivity_et_name);
        receptionistProfileActivity_et_Address = findViewById(R.id.receptionistProfileActivity_et_Address);
        receptionistProfileActivity_et_phone = findViewById(R.id.receptionistProfileActivity_et_phone);
        receptionistProfileActivity_et_salary = findViewById(R.id.receptionistProfileActivity_et_salary);
        receptionistProfileActivity_et_hourWork = findViewById(R.id.receptionistProfileActivity_et_hourWork);
        receptionistProfileActivity_et_username = findViewById(R.id.receptionistProfileActivity_et_username);
        receptionistProfileActivity_et_email = findViewById(R.id.receptionistProfileActivity_et_email);
        receptionistProfileActivity_sp_service = findViewById(R.id.receptionistProfileActivity_sp_service);
    }

    private void checkReceptionist() {
        receptionist = (Receptionist) getIntent().getSerializableExtra(SELECTED_RECEPTIONIST);
//        Toast.makeText(this, receptionist.toString() , Toast.LENGTH_LONG).show();
    }

    private void putReceptionistInformation() {
        receptionistProfileActivity_et_ID.setText(receptionist.getReceptionistID());
        receptionistProfileActivity_et_name.setText(receptionist.getReceptionistName());
        receptionistProfileActivity_et_Address.setText(receptionist.getReceptionistAddress());
        receptionistProfileActivity_et_phone.setText(receptionist.getReceptionistPhone());
        receptionistProfileActivity_et_salary.setText(receptionist.getReceptionistSalary());
        receptionistProfileActivity_et_hourWork.setText(receptionist.getReceptionistHourWork());
        receptionistProfileActivity_et_username.setText(receptionist.getReceptionistUid());
        receptionistProfileActivity_et_email.setText(receptionist.getReceptionistEmail());

        for (int i = 0 ; i < receptionistProfileActivity_sp_service.getCount() ; i++)
        {
            if(receptionistProfileActivity_sp_service.getItemAtPosition(i).equals(receptionist.getReceptionistService()))
            {
                receptionistProfileActivity_sp_service.setSelection(i);
                break;
            }
        }

    }

    private void setUpListener() {
        // Username Validation
        receptionistProfileActivity_et_username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String username = receptionistProfileActivity_et_username.getText().toString();
                if(!hasFocus)
                {
                    if(!username.startsWith("receptionist_"))
                    {
                        String user = "receptionist_" + username;
                        username = user;
                    }

                    receptionistProfileActivity_et_username.setText(username);
                }
            }
        });
    }

    public void Edit(View view) {
        Thread editReceptionistThread = new Thread(new AsyncTask());
        editReceptionistThread.start();
        closeKeyboard();
    }

    private void closeKeyboard(){
        View view = getCurrentFocus();
        if(view != null)
        {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    private class AsyncTask implements Runnable{

        @Override
        public void run() {
            StringRequest editReceptionistRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(response.equals("success"))
                        Toast.makeText(getApplicationContext(), "Edit Successfully", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    data.put("receptionistName", receptionistProfileActivity_et_name.getText().toString());
                    data.put("receptionistPhone", receptionistProfileActivity_et_phone.getText().toString());
                    data.put("receptionistAddress", receptionistProfileActivity_et_Address.getText().toString());
                    data.put("receptionistUid", receptionistProfileActivity_et_username.getText().toString());
                    data.put("receptionistSalary", receptionistProfileActivity_et_salary.getText().toString());
                    data.put("receptionistHourWork", receptionistProfileActivity_et_hourWork.getText().toString());
                    data.put("receptionistService", receptionistProfileActivity_sp_service.getSelectedItem().toString());
                    data.put("receptionistID", receptionist.getReceptionistID());


                    return data;
                }
            };

            Singleton.getInstance(getApplicationContext()).addToRequestQueue(editReceptionistRequest);
        }

    }




}