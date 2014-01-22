package org.cityofchicago.dob.bfax;


import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MyLocationOverlay;

import android.view.Menu;

public class MainActivity extends MapActivity {

	private GoogleMap mMap;
	 private MapView mapView;
	    private MyLocationOverlay myLocOverlay;
	    MapController mc;
	    LatLngBounds ch2;
	    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        

        setUpMapIfNeeded();
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
            	  
            	    double lat = myLocation.getLatitude();
            	    double lon = myLocation.getLongitude();
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
    
    
}
    
