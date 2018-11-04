package com.example.myfirstapp;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /** Called when the user clicks the Send button */
    public void sendMessage(View view){
        // Do something in response to button
        EditText editText =(EditText) findViewById(R.id.edit_message);
        String location = editText.getText().toString();
        location = location.replace(" ", "+");
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + location + "&key=AIzaSyB3R3NhOdAkzw6m-lu2OObbMA8NBQ1_5mw";

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject geometryComponents = ((JSONArray) response.get("results")).getJSONObject(0).getJSONObject("geometry");
                            double lat = geometryComponents.getJSONObject("location").getDouble("lat");
                            double lon = geometryComponents.getJSONObject("location").getDouble("lng");
                            JSONArray addressComponents = ((JSONArray) response.get("results")).getJSONObject(0).getJSONArray("address_components");
                            String county = addressComponents.getJSONObject(3).get("long_name").toString();
                            updateMap(lat, lon, county);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        queue.add(jsonObjectRequest);
    }
    
    public void updateMap(double lat, double lng, String county) {
        mMap.clear();
        LatLng coord = new LatLng(lat, lng);
        Marker m1 = mMap.addMarker(new MarkerOptions().position(coord).title(county));
        m1.showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(coord));
        mMap.setMinZoomPreference(6.0f);
    }

    public void setSatelliteType(View view) {
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    }

    public void setMapType(View view) {
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    public void zoom(View view)
    {
        if(view.getId() == R.id.zoomin)
        {
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
        }
        if(view.getId() == R.id.zoomout)
        {
            mMap.animateCamera(CameraUpdateFactory.zoomOut());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
}
