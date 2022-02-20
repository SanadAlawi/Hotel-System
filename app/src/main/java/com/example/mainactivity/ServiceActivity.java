package com.example.mainactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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

public class ServiceActivity extends AppCompatActivity {

    private Spinner serviceActivity_sp_orderType;
    private EditText serviceActivity_tv_orderDescription;

    private Reserve selectedRoom;
    private static final String SELECTED_ROOM = "Room";

    private String orderType;
    private String orderDescription;

    private static final String URL = "http://10.0.2.2/COMP438_Project/sent_order.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        setUpViews();
        getSelectedRoom();

        Toast.makeText(this, "Service Activity", Toast.LENGTH_SHORT).show();
    }

    private void getSelectedRoom() {
        selectedRoom = (Reserve) getIntent().getSerializableExtra(SELECTED_ROOM);
    }

    private void setUpViews() {
        serviceActivity_sp_orderType = findViewById(R.id.serviceActivity_sp_orderType);
        serviceActivity_tv_orderDescription = findViewById(R.id.serviceActivity_tv_orderDescription);
    }

    public void SentOrder(View view) {

        orderType = serviceActivity_sp_orderType.getSelectedItem()+"";
        orderDescription  = serviceActivity_tv_orderDescription.getText().toString();

        if(orderType.isEmpty() || orderDescription.isEmpty())
        {
            Toast.makeText(this, "Add readable order!!!", Toast.LENGTH_SHORT).show();
            return ;
        }

            closeKeyboard();
//        Toast.makeText(getApplicationContext(),   "roomID: " + selectedRoom.getRoomID() +"\nOrder Type: " + orderType + "\n Order Description: " + orderDescription, Toast.LENGTH_LONG).show();

        StringRequest sentOrderRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();
                if(response.equals("success"))
                    Toast.makeText(getApplicationContext(), "Sent Successfully", Toast.LENGTH_SHORT).show();
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
                data.put("roomID", selectedRoom.getRoomID());
                data.put("orderDescription", orderDescription);
                data.put("orderType", orderType);

                return data;
            }
        };
        Singleton.getInstance(getApplicationContext()).addToRequestQueue(sentOrderRequest);
    }

    private void closeKeyboard(){
        View view = getCurrentFocus();
        if(view != null)
        {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


}