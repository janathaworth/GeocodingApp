package com.example.myfirstapp;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    TextView txtCoord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        txtCoord = (TextView) findViewById(R.id.txtCoordinates);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /** Called when the user clicks the Send button */
    public void sendMessage(View view){
        // Do something in response to button
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText =(EditText) findViewById(R.id.edit_message);
        String location = editText.getText().toString();
        location = location.replace(" ", "+");
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
                            double lat = resultContents.getJSONObject("location").getDouble("lat");
                            double lon = resultContents.getJSONObject("location").getDouble("lng");
                            //txtCoord.setText("latitude: " + lat  + "\nlongitude: " + lon);
                            updateMap(lat, lon);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });
        queue.add(jsonObjectRequest);
    }
    public void updateMap(double lat, double lng) {
        LatLng coord = new LatLng(lat, lng);
        Marker m1 = mMap.addMarker(new MarkerOptions().position(coord));
        m1.showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(coord));
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}
