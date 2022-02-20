package com.example.mainactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.InputMethodService;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SignupReceptionistActivity extends AppCompatActivity {

    private EditText signupReceptionistActivity_et_name;
    private EditText signupReceptionistActivity_et_phone;
    private EditText signupReceptionistActivity_et_Address;
    private EditText signupReceptionistActivity_et_username;
    private EditText signupReceptionistActivity_et_email;
    private EditText signupReceptionistActivity_et_password;
    private EditText signupReceptionistActivity_et_repeatPassword;
    private EditText signupReceptionistActivity_et_salary;
    private EditText signupReceptionistActivity_et_hourWork;

    private Spinner signupReceptionistActivity_sp_service;
//    private EditText signupReceptionistActivity_et_service;

    private final static String URL = "http://10.0.2.2/COMP438_Project/signup_receptionist.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_receptionist);

        setUpViews();
        setUpListener();
    }

    private void setUpViews() {
        signupReceptionistActivity_et_name = findViewById(R.id.signupReceptionistActivity_et_name);
        signupReceptionistActivity_et_phone = findViewById(R.id.signupReceptionistActivity_et_phone);
        signupReceptionistActivity_et_Address = findViewById(R.id.signupReceptionistActivity_et_Address);
        signupReceptionistActivity_et_username = findViewById(R.id.signupReceptionistActivity_et_username);
        signupReceptionistActivity_et_email = findViewById(R.id.signupReceptionistActivity_et_email);
        signupReceptionistActivity_et_password = findViewById(R.id.signupReceptionistActivity_et_password);
        signupReceptionistActivity_et_repeatPassword = findViewById(R.id.signupReceptionistActivity_et_repeatPassword);
        signupReceptionistActivity_et_salary = findViewById(R.id.signupReceptionistActivity_et_salary);
        signupReceptionistActivity_et_hourWork = findViewById(R.id.signupReceptionistActivity_et_hourWork);

        signupReceptionistActivity_sp_service = findViewById(R.id.signupReceptionistActivity_sp_service);


//        signupReceptionistActivity_et_service = findViewById(R.id.signupReceptionistActivity_et_service);
    }

    private void setUpListener() {
        // Email Validation
        signupReceptionistActivity_et_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String email = signupReceptionistActivity_et_email.getText().toString().trim();
                if(!hasFocus)
                {
                    int hash = email.indexOf("@");
                    if(hash != -1)
                    {
                        email = email.substring(0, hash);
                        email += "@receptionist.com";
                    }else
                        email += "@receptionist.com";

                    signupReceptionistActivity_et_email.setText(email);
                }
            }
        });

        // Username Validation
        signupReceptionistActivity_et_username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String username = signupReceptionistActivity_et_username.getText().toString().trim();
                if(!hasFocus)
                {
                    if(!username.toLowerCase().startsWith("receptionist_"))
                    {
                        String user = "receptionist_" + username;
                        username = user;
                    }

                    signupReceptionistActivity_et_username.setText(username);
                }
            }
        });
    }

    public void login(View view) {
        String name = signupReceptionistActivity_et_name.getText().toString().trim();
        String username = signupReceptionistActivity_et_username.getText().toString().trim();
        String pwd = signupReceptionistActivity_et_password.getText().toString().trim();
        String repeatPwd = signupReceptionistActivity_et_repeatPassword.getText().toString().trim();
        String salary = signupReceptionistActivity_et_salary.getText().toString().trim();
        String phone = signupReceptionistActivity_et_phone.getText().toString().trim();
        String hourWork = signupReceptionistActivity_et_hourWork.getText().toString().trim();
        String address = signupReceptionistActivity_et_Address.getText().toString().trim();
        String email = signupReceptionistActivity_et_email.getText().toString().trim();

        String service = signupReceptionistActivity_sp_service.getSelectedItem().toString();
//        String service = signupReceptionistActivity_et_service.getText().toString();


        if(name.isEmpty() || username.isEmpty() || pwd.isEmpty() || repeatPwd.isEmpty() || salary.isEmpty() || phone.isEmpty() || hourWork.isEmpty() || address.isEmpty() || email.isEmpty() || service.isEmpty()){
            Toast.makeText(this, "Fill All The Fields!!!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!pwd.equals(repeatPwd)){
            Toast.makeText(this, "Password Not Match!!!", Toast.LENGTH_SHORT).show();
            return;
        }

        Thread signupThread = new Thread(new AsyncTask(username, pwd, name, email, phone, address, salary, hourWork, service));
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

        outState.putString("name", signupReceptionistActivity_et_name.getText().toString());
        outState.putString("address", signupReceptionistActivity_et_Address.getText().toString());
        outState.putString("phone", signupReceptionistActivity_et_phone.getText().toString());
        outState.putString("email", signupReceptionistActivity_et_email.getText().toString());
        outState.putString("username", signupReceptionistActivity_et_username.getText().toString());
        outState.putString("password", signupReceptionistActivity_et_password.getText().toString());
        outState.putString("repeatPassword", signupReceptionistActivity_et_repeatPassword.getText().toString());
        outState.putString("salary", signupReceptionistActivity_et_salary.getText().toString());
        outState.putString("hourWork", signupReceptionistActivity_et_hourWork.getText().toString());
        outState.putInt("service", signupReceptionistActivity_sp_service.getSelectedItemPosition());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        signupReceptionistActivity_et_name.setText(savedInstanceState.getString("name"));
        signupReceptionistActivity_et_Address.setText(savedInstanceState.getString("address"));
        signupReceptionistActivity_et_phone.setText(savedInstanceState.getString("phone"));
        signupReceptionistActivity_et_email.setText(savedInstanceState.getString("email"));
        signupReceptionistActivity_et_username.setText(savedInstanceState.getString("username"));
        signupReceptionistActivity_et_password.setText(savedInstanceState.getString("password"));
        signupReceptionistActivity_et_repeatPassword.setText(savedInstanceState.getString("repeatPassword"));
        signupReceptionistActivity_et_salary.setText(savedInstanceState.getString("salary"));
        signupReceptionistActivity_et_hourWork.setText(savedInstanceState.getString("hourWork"));
        signupReceptionistActivity_sp_service.setSelection(savedInstanceState.getInt("service"));

    }

    private  class AsyncTask implements Runnable{

        private String receptionistUid;
        private String receptionistPwd;
        private String receptionistName;
        private String receptionistEmail;
        private String receptionistPhone;
        private String receptionistAddress;
        private String receptionistSalary;
        private String receptionistHourWork;
        private String receptionistService;

        public AsyncTask(String receptionistUid, String receptionistPwd, String receptionistName, String receptionistEmail, String receptionistPhone, String receptionistAddress, String receptionistSalary, String receptionistHourWork, String receptionistService) {
            this.receptionistUid = receptionistUid;
            this.receptionistPwd = receptionistPwd;
            this.receptionistName = receptionistName;
            this.receptionistEmail = receptionistEmail;
            this.receptionistPhone = receptionistPhone;
            this.receptionistAddress = receptionistAddress;
            this.receptionistSalary = receptionistSalary;
            this.receptionistHourWork = receptionistHourWork;
            this.receptionistService = receptionistService;
        }

        @Override
        public void run() {
            StringRequest SignupRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if(response.equals("success")){
                        Toast.makeText(getApplicationContext(), "Receptionist " + receptionistName + " Added Successfully", Toast.LENGTH_SHORT).show();
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
                    data.put("receptionistName", receptionistName);
                    data.put("receptionistEmail", receptionistEmail);
                    data.put("receptionistUid", receptionistUid);
                    data.put("receptionistPwd", receptionistPwd);
                    data.put("receptionistPhone", receptionistPhone);
                    data.put("receptionistAddress", receptionistAddress);
                    data.put("receptionistSalary", receptionistSalary);
                    data.put("receptionistHourWork", receptionistHourWork);
                    data.put("receptionistService", receptionistService);

                    return data;
                }
            };


            Singleton.getInstance(getApplicationContext()).addToRequestQueue(SignupRequest);

        }
    }
































}