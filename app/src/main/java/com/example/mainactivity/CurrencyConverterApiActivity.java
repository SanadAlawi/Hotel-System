package com.example.mainactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class CurrencyConverterApiActivity extends AppCompatActivity {

    private TextView currencyConverterApiActivity_tv_convertFrom;
    private TextView currencyConverterApiActivity_tv_convertTo;
    private TextView currencyConverterApiActivity_et_amountConvert;
    private TextView currencyConverterApiActivity_tv_convertRate;


    private String currencyFrom;
    private String currencyTo;

    private Dialog currencyFromDialog;
    private Dialog currencyToDialog;

    private String [] codes;

    private HashMap<String, Double> currencyTable = new HashMap<String, Double>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_conventer_api);


        setUpViews();
        getCurrencyFromApi("USD");
        addEditTextListener();
    }


    private void setUpViews() {
        currencyConverterApiActivity_tv_convertFrom = findViewById(R.id.currencyConverterApiActivity_tv_convertFrom);
        currencyConverterApiActivity_tv_convertTo = findViewById(R.id.currencyConverterApiActivity_tv_convertTo);
        currencyConverterApiActivity_et_amountConvert = findViewById(R.id.currencyConverterApiActivity_et_amountConvert);
        currencyConverterApiActivity_tv_convertRate = findViewById(R.id.currencyConverterApiActivity_tv_convertRate);
    }

    private void getCurrencyFromApi(String currency) {
        Thread currencyThread= new Thread(new AsyncTask(currency));
        currencyThread.start();
    }

    private void addEditTextListener() {

        currencyConverterApiActivity_et_amountConvert.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!s.toString().isEmpty())
                {
                    if(!currencyFrom.isEmpty() && !currencyTo.isEmpty())
                        ComputeConversationRate(s.toString());
                }else
                    currencyConverterApiActivity_tv_convertRate.setText("0.0");
            }
        });
    }

    private void ComputeConversationRate(String amount){
        double conversationRate = Double.parseDouble(amount) * currencyTable.get(currencyTo);
        currencyConverterApiActivity_tv_convertRate.setText(conversationRate+"");
    }


    public void CurrencyFrom(View view) {
        currencyFromDialog = new Dialog(this);
        currencyFromDialog.setContentView(R.layout.currency_from_layout);
        currencyFromDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        currencyFromDialog.show();

        ListView listView = currencyFromDialog.findViewById(R.id.currencyFromLayout_lv_listView);

//        String [] codes = {"USD", "AED", "PHP", "CNY", "ILS"};
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, codes);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currencyFrom = codes[position];
                Toast.makeText(getApplicationContext(), "Currency From: " + currencyFrom, Toast.LENGTH_SHORT).show();
                currencyConverterApiActivity_tv_convertFrom.setText(currencyFrom);
                currencyFromDialog.dismiss();
            }
        });

        Thread currencyThread= new Thread(new AsyncTask(currencyFrom));
        currencyThread.start();
    }

    public void CurrencyTo(View view) {
        currencyToDialog = new Dialog(this);
        currencyToDialog.setContentView(R.layout.currency_to_layout);
        currencyToDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        currencyToDialog.show();

        ListView listView = currencyToDialog.findViewById(R.id.currencyToLayout_lv_listView);

//        String [] codes = {"USD", "AED", "PHP", "CNY", "ILS"};
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, codes);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currencyTo = codes[position];
                Toast.makeText(getApplicationContext(), "Currency To: " + currencyTo, Toast.LENGTH_SHORT).show();
                currencyConverterApiActivity_tv_convertTo.setText(currencyTo);
                currencyToDialog.dismiss();
            }
        });
    }


    private class AsyncTask implements Runnable{

        private String currency;

        public AsyncTask(String currency) {
            this.currency = currency;
        }

        @Override
        public void run() {
            String url = "https://api.fastforex.io/fetch-all?api_key=54de56b0a9-10f1bb95e0-r6875z&base=" + currency;
//            String url = "https://v6.exchangerate-api.com/v6/8c69fdb1ce78256ed9c94079/latest/" + currency;

            JsonObjectRequest currencyRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        JSONObject currencyObject = response.getJSONObject("results");
//                        JSONObject currencyObject = response.getJSONObject("conversion_rates");
                        List<String> codesList = new ArrayList<>();

                        currencyTable.put(currency, 1.0);
                        for (Iterator<String> it = currencyObject.keys(); it.hasNext(); ) {
                            String currencyCode = it.next();

                            currencyTable.put(currencyCode, currencyObject.getDouble(currencyCode)); // key-->value, currencyCode --> currencyValue

                            codesList.add(currencyCode); // Add to list to put it into spinner
                        }

                        codes = new String[codesList.size()];
//                        String [] codes = new String[codesList.size()];
                        codesList.toArray(codes);
                        Arrays.sort(codes);
//                        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, codes);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
//                                currencyConverterApiActivity_tv_convertFrom.setAdapter(arrayAdapter);
//                                currencyConverterApiActivity_sp_convertTo.setAdapter(arrayAdapter);
//                                currencyConverterApiActivity_tv_convertFrom.setSelection(arrayAdapter.getPosition(currency));
//                                currencyConverterApiActivity_sp_convertTo.setSelection(arrayAdapter.getPosition("ILS"));
                                currencyConverterApiActivity_tv_convertFrom.setText(currency);
                                currencyFrom = currency;
                            }
                        });

                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

            Singleton.getInstance(getApplicationContext()).addToRequestQueue(currencyRequest);
        }
    }
}