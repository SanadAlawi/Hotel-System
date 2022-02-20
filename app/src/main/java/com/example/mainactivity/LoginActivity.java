package com.example.mainactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
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

public class LoginActivity extends AppCompatActivity {

    // Views
    private EditText loginActivity_et_username;
    private EditText loginActivity_et_password;
    private CheckBox loginActivity_cb_remember;
    private ProgressBar loginActivity_pb_progress;

    // Preferences
    private SharedPreferences sp;
    private SharedPreferences.Editor ed;

    // SharedPreferences Keys
    private static final String CURRENT_USER = "User"; // Shared Preference Key, save current user object(customer, receptionist, admin)
    private static final String CHECKED = "Checked";
    private static final String USERNAME = "Username";
    private static final String PASSWORD = "Password";
    private static final String FLAG = "Flag";

    private static String LOGIN_URL = "http://10.0.2.2/COMP438_Project/login.php";
    private static String tableType; // (customer, receptionist)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setUpViews();
        setUpPreferences();
        userRemember();
    }

    private void setUpViews() {
        loginActivity_et_username = findViewById(R.id.loginActivity_et_username);
        loginActivity_et_password = findViewById(R.id.loginActivity_et_password);
        loginActivity_cb_remember = findViewById(R.id.loginActivity_cb_remember);
        loginActivity_pb_progress = findViewById(R.id.loginActivity_pb_progress);
    }

    private void setUpPreferences() {
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        ed = sp.edit();
    }

    private void userRemember() {
        boolean flag = sp.getBoolean(FLAG, false);
        boolean isChecked = sp.getBoolean(CHECKED, false);
        if(flag)
        {
            if(isChecked)
            {
                loginActivity_et_username.setText(sp.getString(USERNAME, ""));
                loginActivity_et_password.setText(sp.getString(PASSWORD, ""));
                loginActivity_cb_remember.setChecked(true);
            }
        }
    }

    // Signup Button Action
    public void SignUp(View view) {
        Intent loginIntent = new Intent(this, SignupCustomerActivity.class);
        startActivity(loginIntent);
    }

    // Find User Type(customer, admin, receptionist) by username or gmail
    private String FindUserType(String userUid) {
        String userType = "customer";
        if(userUid.split("_")[0].equals("receptionist") || userUid.endsWith("receptionist.com"))
        {
            userType = "receptionist";
        }

        return userType;
    }


    public void Login(View view) {
        String userUid = loginActivity_et_username.getText().toString();
        String userPwd = loginActivity_et_password.getText().toString();
        if(userUid.isEmpty() || userPwd.isEmpty()){
            Toast.makeText(this, "Fill All Fields!!!", Toast.LENGTH_SHORT).show();
            return;
        }

        tableType = FindUserType(userUid); // Find user(customer or receptionist) by(email or username)

        Thread loginThread = new Thread(new AsyncTask(userUid, userPwd));
        loginThread.start();

        // Remember User
        if(loginActivity_cb_remember.isChecked()){
            ed.putBoolean(CHECKED, true);
            ed.putString(USERNAME, loginActivity_et_username.getText().toString());
            ed.putString(PASSWORD, loginActivity_et_password.getText().toString());
            ed.apply();
        }

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

        private String userUid;
        private String userPwd;

        public AsyncTask(String userUid, String userPwd) {
            this.userUid = userUid;
            this.userPwd = userPwd;
        }

        @Override
        public void run() {

            StringRequest loginRequest = new StringRequest(Request.Method.POST, LOGIN_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String errorType = jsonObject.getString("error");
                        if(errorType.equalsIgnoreCase("exist"))
                        {
                            Toast.makeText(getApplicationContext(), "User Not Found", Toast.LENGTH_SHORT).show();

                        }else if(errorType.equalsIgnoreCase("password"))
                        {
                            Toast.makeText(getApplicationContext(), "Wrong Password!!!", Toast.LENGTH_SHORT).show();

                        }else if(errorType.equalsIgnoreCase("success"))
                        {
                            ed.putString(CURRENT_USER, response);
                            ed.putBoolean(FLAG, true);
                            ed.apply();

                            String userType = jsonObject.getString("userType");
                            Toast.makeText(getApplicationContext(), "USER TYPE: "+userType, Toast.LENGTH_SHORT).show();
                            if(userType.equals("admin"))
                            {
                                Intent adminIntent = new Intent(getApplicationContext(), AdminActivity.class);
                                startActivity(adminIntent);

                            }else if(userType.equals("receptionist"))
                            {
                                Intent receptionistIntent = new Intent(getApplicationContext(), ReceptionistActivity.class);
                                startActivity(receptionistIntent);

                            } else if(userType.equals("customer"))
                            {
                                Intent homeIntent = new Intent(getApplicationContext(), HomeActivity.class);
                                startActivity(homeIntent);
                            }

                        }else
                            Toast.makeText(getApplicationContext(), "Unknown Error!!!", Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.getMessage()+"--> onErrorResponse", Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();
                    data.put("userUid", userUid);
                    data.put("userPwd", userPwd);
                    data.put("tableType", tableType);
                    return data;
                }
            };
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loginActivity_pb_progress.setVisibility(View.VISIBLE);
                }
            });
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Singleton.getInstance(getApplicationContext()).addToRequestQueue(loginRequest);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loginActivity_pb_progress.setVisibility(View.GONE);
                }
            });

        }

    }


}