package com.waupi.util;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.waupi.screens.EtaShuttle;

public class TrackLocation implements LocationListener, GooglePlayServicesClient.ConnectionCallbacks, GooglePlayServicesClient.OnConnectionFailedListener{
	
	private  EtaShuttle etashuttle;
	private static LocationClient mLocationClient;
	private static LocationRequest mLocationRequest;
	public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	public static final int MILLISECONDS_PER_SECOND = 1000;
	public static final int UPDATE_INTERVAL_IN_SECONDS = 2;
	public static final int FAST_CEILING_IN_SECONDS = 1;
	public static final long UPDATE_INTERVAL_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
			* UPDATE_INTERVAL_IN_SECONDS;
	public static final long FAST_INTERVAL_CEILING_IN_MILLISECONDS = MILLISECONDS_PER_SECOND
			* FAST_CEILING_IN_SECONDS;
	
	public TrackLocation(EtaShuttle shuttel) {
		etashuttle = shuttel;
		open();
	}
	
	private  void open() {
		
		mLocationRequest = LocationRequest.create();
	    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
		mLocationRequest.setFastestInterval(FAST_INTERVAL_CEILING_IN_MILLISECONDS);
		mLocationClient = new LocationClient(etashuttle, this, this);
		mLocationClient.connect();
		if (mLocationClient.isConnected()) {
			mLocationClient.removeLocationUpdates(this);
			mLocationClient.requestLocationUpdates(mLocationRequest, this);

		}
	}
	  
	public void removeLocationUpdate(){
		mLocationClient.removeLocationUpdates(this);		
	}
	
	public void requestUpdate(){
		mLocationClient.requestLocationUpdates(mLocationRequest, this);
		
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		
		mLocationClient.requestLocationUpdates(mLocationRequest,this);
	}

	@Override
	public void onDisconnected() {
		
	}

	@Override
	public void onLocationChanged(Location location) {
		
		etashuttle.map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 15));
		removeLocationUpdate();
		}
}
