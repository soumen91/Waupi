package com.waupi.Fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.waupi.bean.AtmBean;
import com.waupi.screens.BaseScreen;
import com.waupi.screens.R;

public class Atm extends Fragment {
	private static BaseScreen base;
	private Address address1,address2,address3,address4,address5;
	public GoogleMap map;
	public Marker cabMarker;
	Marker melbourne;
	private double lat = 0.0f;
	private double lng = 0.0f;
	private static ArrayList<AtmBean> atmtList;
	public static Fragment newInstance(BaseScreen b, ArrayList<AtmBean> list) {
		base = b;
		atmtList = list;
		Atm atm = new Atm();
		return atm;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.atms, null);

		initializeMap();
		
		
		
		/*map.addMarker(new MarkerOptions()
		.position(new LatLng(address1.getLatitude(), address1.getLongitude()))
		.title("ACTION ATM INC.")
		.icon(BitmapDescriptorFactory
		.fromResource(R.drawable.atm_map_icon))).hideInfoWindow();
		
		map.addMarker(new MarkerOptions()
		.position(new LatLng(address2.getLatitude(), address2.getLongitude()))
		.title("MONEY ACCESS SERVICE")
		.icon(BitmapDescriptorFactory
		.fromResource(R.drawable.atm_map_icon))).hideInfoWindow();
		
		map.addMarker(new MarkerOptions()
		.position(new LatLng(address3.getLatitude(), address3.getLongitude()))
		.title("ST. JOHNS BANK & TRUST COMPANY")
		.icon(BitmapDescriptorFactory
		.fromResource(R.drawable.atm_map_icon))).hideInfoWindow();
		
		 map.addMarker(new MarkerOptions()
		.position(new LatLng(address4.getLatitude(), address4.getLongitude()))
		.title("PETRO MART")
		.icon(BitmapDescriptorFactory
		.fromResource(R.drawable.atm_map_icon))).hideInfoWindow();
		
		 map.addMarker(new MarkerOptions()
			.position(new LatLng(address5.getLatitude(), address5.getLongitude()))
			.title("7-ELEVEN #20206")
			.icon(BitmapDescriptorFactory
			.fromResource(R.drawable.atm_map_icon))).hideInfoWindow();
		
		
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
				address1.getLatitude(), address1.getLongitude()), 10));*/
		
		return root;
	}

	public Address getLocationFromAddress(String strAddress) {

		Geocoder coder = new Geocoder(base);
		List<Address> address;

		try {
			address = coder.getFromLocationName(strAddress, 5);
			if (address == null) {
				return null;
			}
			Address location = address.get(0);
			location.getLatitude();
			location.getLongitude();
			
			return location;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private void initializeMap() {
		map = ((SupportMapFragment) base.getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		
		setMarkersOnMap();
	}
	
	private void setMarkersOnMap() {
		for(int i = 0 ;i <atmtList.size();i++){
			map.addMarker(new MarkerOptions()
			.position(new LatLng(Double.parseDouble(atmtList.get(i).getAtm_lat().toString()), Double.parseDouble(atmtList.get(i).getAtm_lng().toString())))
			.title(atmtList.get(i).getAtm_name())
			.icon(BitmapDescriptorFactory
			.fromResource(R.drawable.atm_map_icon)));
		}
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(atmtList.get(0).getAtm_lat().toString()),
				Double.parseDouble(atmtList.get(0).getAtm_lng().toString())), 10));
	}

	@Override
	public void onDestroyView() {
	    Fragment f = (Fragment) base.getSupportFragmentManager().findFragmentById(R.id.map);        
	    if (f != null) {
	    	base.getSupportFragmentManager().beginTransaction().remove(f).commitAllowingStateLoss();
	    }
	    super.onDestroyView();   
	}
}
