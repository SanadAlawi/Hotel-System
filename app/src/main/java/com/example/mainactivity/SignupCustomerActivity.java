package com.example.mainactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SignupCustomerActivity extends AppCompatActivity {

    private EditText signupCustomerActivity_et_name;
    private EditText signupCustomerActivity_et_phone;
    private EditText signupCustomerActivity_et_Address;
    private EditText signupCustomerActivity_et_username;
    private EditText signupCustomerActivity_et_email;
    private EditText signupCustomerActivity_et_password;
    private EditText signupCustomerActivity_et_repeatPassword;
    private ProgressBar signupCustomerActivity_pb_progress;

    private final static String URL = "http://10.0.2.2/COMP438_Project/signup_customer.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_customer);

        setUpViews();
        setUpListener();

    }


    private void setUpViews() {
        signupCustomerActivity_et_name = findViewById(R.id.signupCustomerActivity_et_name);
        signupCustomerActivity_et_username = findViewById(R.id.signupCustomerActivity_et_username);
        signupCustomerActivity_et_email = findViewById(R.id.signupCustomerActivity_et_email);
        signupCustomerActivity_et_password = findViewById(R.id.signupCustomerActivity_et_password);
        signupCustomerActivity_et_repeatPassword = findViewById(R.id.signupCustomerActivity_et_repeatPassword);
        signupCustomerActivity_et_phone = findViewById(R.id.signupCustomerActivity_et_phone);
        signupCustomerActivity_et_Address = findViewById(R.id.signupCustomerActivity_et_Address);
        signupCustomerActivity_pb_progress = findViewById(R.id.signupCustomerActivity_pb_progress);
    }

    private void setUpListener() {
        // Username Validation
        signupCustomerActivity_et_username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String username = signupCustomerActivity_et_username.getText().toString().trim();
                if(!hasFocus)
                {
                    if(username.toLowerCase().startsWith("receptionist_"))
                    {
                        username = username.replace("receptionist_", "");
                        Toast.makeText(getApplicationContext(), "Username Should Not Start With: receptionist_", Toast.LENGTH_SHORT).show();
                    }

                    signupCustomerActivity_et_username.setText(username);
                }
            }
        });

        // Email Validation
        signupCustomerActivity_et_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String email = signupCustomerActivity_et_email.getText().toString().trim();
                if(!hasFocus)
                {
                    if(email.toLowerCase().endsWith("@receptionist.com"))
                    {
                        email = email.replace("receptionist.com", "");
                        Toast.makeText(getApplicationContext(), "Email Should Not End With: @receptionist.com", Toast.LENGTH_SHORT).show();
                    }
                    signupCustomerActivity_et_email.setText(email);
                }
            }
        });
    }


    public void login(View view) {
        String userName = signupCustomerActivity_et_name.getText().toString();
        String userEmail = signupCustomerActivity_et_email.getText().toString();
        String userUid = signupCustomerActivity_et_username.getText().toString();
        String userPwd = signupCustomerActivity_et_password.getText().toString();
        String repeatPwd = signupCustomerActivity_et_repeatPassword.getText().toString();
        String userPhone = signupCustomerActivity_et_phone.getText().toString();
        String userAddress = signupCustomerActivity_et_Address.getText().toString();

        if(!userEmail.matches("^[a-zA-Z][a-zA-Z0-9]*@[a-zA-Z]*\\.com"))
        {
            Toast.makeText(this, "Not Valid Email", Toast.LENGTH_SHORT).show();
            return ;
        }

        if(userEmail.isEmpty() || userName.isEmpty() || userUid.isEmpty() || userPwd.isEmpty() || repeatPwd.isEmpty() || userAddress.isEmpty() || userPhone.isEmpty()){
            Toast.makeText(this, "Fill All The Fields!!!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(userUid.startsWith("receptionist_")){
            Toast.makeText(this, "Username Should not start with word: receptionist_", Toast.LENGTH_SHORT).show();
            return ;
        }

        if(!userPwd.equals(repeatPwd)){
            Toast.makeText(this, "Password Not Match!!!", Toast.LENGTH_SHORT).show();
            return;
        }


        signupCustomerActivity_pb_progress.setVisibility(View.VISIBLE);
        Thread signupThread = new Thread(new AsyncTask(userUid, userPwd, userName, userEmail, userPhone, userAddress));
        signupThread.start();
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


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("name", signupCustomerActivity_et_name.getText().toString());
        outState.putString("address", signupCustomerActivity_et_Address.getText().toString());
        outState.putString("phone", signupCustomerActivity_et_phone.getText().toString());
        outState.putString("email", signupCustomerActivity_et_email.getText().toString());
        outState.putString("username", signupCustomerActivity_et_username.getText().toString());
        outState.putString("password", signupCustomerActivity_et_password.getText().toString());
        outState.putString("repeatPassword", signupCustomerActivity_et_repeatPassword.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        signupCustomerActivity_et_name.setText(savedInstanceState.getString("name"));
        signupCustomerActivity_et_Address.setText(savedInstanceState.getString("address"));
        signupCustomerActivity_et_phone.setText(savedInstanceState.getString("phone"));
        signupCustomerActivity_et_email.setText(savedInstanceState.getString("email"));
        signupCustomerActivity_et_username.setText(savedInstanceState.getString("username"));
        signupCustomerActivity_et_password.setText(savedInstanceState.getString("password"));
        signupCustomerActivity_et_repeatPassword.setText(savedInstanceState.getString("repeatPassword"));
    }

    private  class AsyncTask implements Runnable{

        private String userUid;
        private String userPwd;
        private String userName;
        private String userEmail;
        private String userPhone;
        private String userAddress;

        public AsyncTask(String userUid, String userPwd, String userName, String userEmail, String userPhone, String userAddress) {
            this.userUid = userUid;
            this.userPwd = userPwd;
            this.userName = userName;
            this.userEmail = userEmail;
            this.userPhone = userPhone;
            this.userAddress = userAddress;
        }

        @Override
        public void run() {
            StringRequest SignupRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                    if(response.equals("success")){
                        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(loginIntent);
                    }else if(response.equals("error_exist")){
                        Toast.makeText(getApplicationContext(), "Username Or Email Already Taken!!!", Toast.LENGTH_SHORT).show();
                    }else
                        Toast.makeText(getBaseContext(), "Error In Sign Up", Toast.LENGTH_SHORT).show();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getBaseContext(), "Error In Data Base", Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    data.put("customerName", userName);
                    data.put("customerEmail", userEmail);
                    data.put("customerUid", userUid);
                    data.put("customerPwd", userPwd);
                    data.put("customerPhone", userPhone);
                    data.put("customerAddress", userAddress);
                    return data;
                }
            };

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    signupCustomerActivity_pb_progress.setVisibility(View.VISIBLE);
                }
            });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Singleton.getInstance(getApplicationContext()).addToRequestQueue(SignupRequest);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    signupCustomerActivity_pb_progress.setVisibility(View.GONE);
                }
            });


        }
    }




}