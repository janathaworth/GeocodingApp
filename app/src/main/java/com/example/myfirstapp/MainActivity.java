package com.example.myfirstapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    TextView txtCoord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtCoord = (TextView) findViewById(R.id.txtCoordinates);
    }

    /** Called when the user clicks the Send button */
    public void sendMessage(View view){
        // Do something in response to button
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText =(EditText) findViewById(R.id.edit_message);
        String location = editText.getText().toString();
        location = location.replace(" ", "+");
//        intent.putExtra(EXTRA_MESSAGE, message);
//        startActivity(intent);
        RequestQueue queue = Volley.newRequestQueue(this);

        // String url = "https://maps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&key=AIzaSyB3R3NhOdAkzw6m-lu2OObbMA8NBQ1_5mw";
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + location + "&key=AIzaSyB3R3NhOdAkzw6m-lu2OObbMA8NBQ1_5mw";

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {
                        // txtCoord.setText("Response: " + response.toString());

                        try {
                            JSONObject resultContents = ((JSONArray) response.get("results")).getJSONObject(0).getJSONObject("geometry");
                            String lat = resultContents.getJSONObject("location").get("lat").toString();
                            String lon = resultContents.getJSONObject("location").get("lng").toString();
                            txtCoord.setText("latitude: " + lat  + "\nlongitude: " + lon);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        System.out.println("error" + error);

                    }
                });

//        String message = "message";
//        intent.putExtra(EXTRA_MESSAGE, message);
//        startActivity(intent);

        queue.add(jsonObjectRequest);
    }
}
