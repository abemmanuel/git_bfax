package org.cityofchicago.dob.bfax;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.util.Log;
import android.view.Menu;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MyLocationOverlay;

import android.view.View;
import android.view.View.OnClickListener;

import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends MapActivity implements
OnMapClickListener, OnMapLongClickListener{

	private GoogleMap mMap;
	 private MapView mapView;
	    private MyLocationOverlay myLocOverlay;
	    MapController mc;
	    LatLngBounds ch2;
	    static double lat;
	    static double lon;
	    double lat_new;
	    double lon_new;
	    EditText mEdit;
	    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        

        setUpMapIfNeeded();
        
        mMap.setOnMapLongClickListener(this); 
        
        Button mondayEdit= (Button)findViewById(R.id.button1);
        mEdit   = (EditText)findViewById(R.id.editText1);
        
        mondayEdit.setOnClickListener(new OnClickListener() 
        {   public void onClick(View v) 
            {   
        	if (mEdit.getText().toString().equals("")) {
        	
                Intent intent = new Intent(MainActivity.this, Building.class);
                Bundle b = new Bundle();
                b.putDouble("lat", lat); 
                b.putDouble("lon", lon); 
                intent.putExtras(b); 
                    startActivity(intent);      
                    finish();
        	}
        	else {
        		
        		getLatLongFromAddress(mEdit.getText().toString()+",Chicago, IL");
        		 Intent intent = new Intent(MainActivity.this, Building.class);
                 Bundle b = new Bundle();
                 b.putDouble("lat", lat); 
                 b.putDouble("lon", lon); 
                 intent.putExtras(b); 
                     startActivity(intent);      
                     finish();
        	}
            }
        });
        
    }

	@Override
	public void onMapLongClick(LatLng point) {
	lat = point.latitude; 
	lon = point.longitude;
	
	mMap.clear();
	mMap.addMarker(new MarkerOptions()
    .position(point)
    .title("You are here")           
    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))); 
	}
	@Override
	public void onMapClick(LatLng point) {
		lat = point.latitude; 
		lon = point.longitude;
	}
	  
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    protected boolean isRouteDisplayed() {
    	return false;
    }
    
    
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                                .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                // The Map is verified. It is now safe to manipulate the map.
            	
            	//mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(chicago.getCenter(), 13));

            	 mMap.setMyLocationEnabled(true);
            	LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        	    Location myLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        	    // Location wasn't found, check the next most accurate place for the current location
        	    if (myLocation == null) {
        	        Criteria criteria = new Criteria();
        	        criteria.setAccuracy(Criteria.ACCURACY_HIGH);
        	        // Finds a provider that matches the criteria
        	        String provider = lm.getBestProvider(criteria, true);
        	        // Use the provider to get the last known location
        	        myLocation = lm.getLastKnownLocation(provider);
        	        
        	        
        	    }
            	  
            	     lat = myLocation.getLatitude();
            	     lon = myLocation.getLongitude();
            	     //lat_new = myLocation.getLatitude();
            	     //lon_new = myLocation.getLongitude();
            	    final LatLng ch2 = new LatLng(lat, lon);
            	    
            	     mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ch2, 17));
            	     /*CameraPosition cameraPosition = CameraPosition.builder()
            	                .target(ch2)
            	                .zoom(17)
            	    		 	.tilt(0)
            	               .bearing(0)
            	                .build();
            	        
            	        // Animate the change in camera view over 2 seconds
            	        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
            	                2000, null);*/
            	       // mMap.setBuildingsEnabled(false);
            	
            }
        }
    }
    
    public  void getLatLongFromAddress(String youraddress) {
    	Geocoder coder = new Geocoder(this);
    	List<Address> address;

    	try {
    	    address = coder.getFromLocationName(youraddress,5);
    	    if (address == null) {
    	       // return null;
    	    }
    	    Address location = address.get(0);
    	   // location.getLatitude();
    	    //location.getLongitude();

    	    //GeoPoint p1 = new GeoPoint((int) (location.getLatitude() * 1E6),
    	                     // (int) (location.getLongitude() * 1E6));
lat=location.getLatitude();
lon=location.getLongitude();
    	     //return p1;
    	}catch (Exception e) {
            //return false;

        }
 
    }
  
    
  
    
}
    
