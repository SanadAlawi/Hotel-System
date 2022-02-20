package com.example.mainactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class ReceptionistsActivity extends AppCompatActivity {

    private RecyclerView receptionistsActivity_rv_receptionists;
    private ArrayList<Receptionist> receptionistList = new ArrayList<>();
    private ReceptionistAdapter receptionistAdapter = new ReceptionistAdapter(this);

    private static final String SELECTED_RECEPTIONIST = "Receptionist";

    private static final String RECEPTIONIST_URL = "http://10.0.2.2/COMP438_Project/show_receptionists.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receptionists);

        setUpViews();
        getReceptionistsFromDB();
    }

    private void setUpViews() {
        receptionistsActivity_rv_receptionists = findViewById(R.id.receptionistsActivity_rv_receptionists);
        receptionistAdapter.setOnItemClickListener(new ReceptionistAdapter.OnItemClickListener() {
            @Override
            public void onClickListener(int position) {
                Intent receptionistProfileIntent = new Intent(getApplicationContext(), ReceptionistProfileActivity.class);
                receptionistProfileIntent.putExtra(SELECTED_RECEPTIONIST, receptionistList.get(position));
                startActivity(receptionistProfileIntent);
            }
        });
    }

    private void getReceptionistsFromDB() {
        Thread receptionistThread = new Thread(new AsyncTask());
        receptionistThread.start();
    }



    private class AsyncTask implements Runnable{

        @Override
        public void run() {
            StringRequest receptionistRequest = new StringRequest(Request.Method.POST, RECEPTIONIST_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONArray receptionistArray = new JSONArray(response);

                        for (int i = 0 ; i < receptionistArray.length() ; i++)
                        {
                            JSONObject receptionistObject = receptionistArray.getJSONObject(i);

                            String receptionistID = receptionistObject.getString("receptionistID");
                            String receptionistName = receptionistObject.getString("receptionistName");
                            String receptionistUid = receptionistObject.getString("receptionistUid");
                            String receptionistPwd = receptionistObject.getString("receptionistPwd");
                            String receptionistSalary = receptionistObject.getString("receptionistSalary");
                            String receptionistPhone = receptionistObject.getString("receptionistPhone");
                            String receptionistHourWork = receptionistObject.getString("receptionistHourWork");
                            String receptionistAddress = receptionistObject.getString("receptionistAddress");
                            String receptionistEmail = receptionistObject.getString("receptionistEmail");
                            String receptionistService = receptionistObject.getString("receptionistService");

                            Receptionist receptionist = new Receptionist(receptionistID, receptionistName, receptionistUid, receptionistPwd, receptionistSalary, receptionistPhone, receptionistHourWork, receptionistAddress, receptionistEmail, receptionistService);

                            receptionistList.add(receptionist);
                        }
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    receptionistAdapter.setReceptionistList(receptionistList);

                    receptionistsActivity_rv_receptionists.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    receptionistsActivity_rv_receptionists.setHasFixedSize(true);
                    receptionistsActivity_rv_receptionists.setAdapter(receptionistAdapter);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            Singleton.getInstance(getApplicationContext()).addToRequestQueue(receptionistRequest);
        }


    }


}