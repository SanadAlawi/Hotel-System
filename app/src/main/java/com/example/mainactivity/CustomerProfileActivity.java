package com.example.mainactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CustomerProfileActivity extends AppCompatActivity {

    // Views
    private EditText customerProfileActivity_et_name;
    private EditText customerProfileActivity_et_phone;
    private EditText customerProfileActivity_et_Address;
    private EditText customerProfileActivity_et_username;
    private EditText customerProfileActivity_et_email;
    private ProgressBar customerProfileActivity_pb_progress;

    // Preferences
    private SharedPreferences sp;
//    private SharedPreferences.Editor ed;

    private static final String CURRENT_USER = "User";
    private String USER_ID;

    private static final String URL = "http://10.0.2.2/COMP438_Project/edit_customer.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);

        setUpViews();
        setUpPreferences();
        setUpValues();
    }

    private void setUpValues() {
        try {
            JSONObject user = new JSONObject(sp.getString(CURRENT_USER, ""));
            customerProfileActivity_et_name.setText(user.getString("customerName"));
            customerProfileActivity_et_username.setText(user.getString("customerUid"));
            customerProfileActivity_et_phone.setText(user.getString("customerPhone"));
            customerProfileActivity_et_Address.setText(user.getString("customerAddress"));
            customerProfileActivity_et_email.setText(user.getString("customerEmail"));
            USER_ID = user.getString("customerID");
            Toast.makeText(this, "User ID: "+USER_ID, Toast.LENGTH_LONG).show();
            customerProfileActivity_et_email.setEnabled(false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setUpPreferences() {
        sp = PreferenceManager.getDefaultSharedPreferences(this);
//        ed = sp.edit();
    }

    private void setUpViews() {
        customerProfileActivity_et_name = findViewById(R.id.customerProfileActivity_et_name);
        customerProfileActivity_et_phone = findViewById(R.id.customerProfileActivity_et_phone);
        customerProfileActivity_et_Address = findViewById(R.id.customerProfileActivity_et_Address);
        customerProfileActivity_et_username = findViewById(R.id.customerProfileActivity_et_username);
        customerProfileActivity_et_email = findViewById(R.id.customerProfileActivity_et_email);
        customerProfileActivity_pb_progress = findViewById(R.id.customerProfileActivity_pb_progress);
    }

    public void EditUser(View view) {
        Thread editThread = new Thread(new AsyncTask());
        editThread.start();
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
            StringRequest editRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    if(response.equals("success"))
                        Toast.makeText(getApplicationContext(), "Edited Successfully", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    data.put("customerID", String.valueOf(USER_ID));
                    data.put("customerName", customerProfileActivity_et_name.getText().toString());
                    data.put("customerUid", customerProfileActivity_et_username.getText().toString());
                    data.put("customerPhone", customerProfileActivity_et_phone.getText().toString());
                    data.put("customerAddress", customerProfileActivity_et_Address.getText().toString());

                    return data;
                }
            };


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    customerProfileActivity_pb_progress.setVisibility(View.VISIBLE);
                }
            });

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Singleton.getInstance(getApplicationContext()).addToRequestQueue(editRequest);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    customerProfileActivity_pb_progress.setVisibility(View.GONE);
                }
            });
        }
    }


}