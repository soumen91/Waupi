package com.waupi.Fragment;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.waupi.adapter.AmenitiesAdapter;
import com.waupi.bean.AmenitiesBean;
import com.waupi.screens.R;

public class Amenities extends Fragment{
	
	private AmenitiesAdapter adapter;
	private ListView lv_amenities;
	private static ArrayList<AmenitiesBean> amenitiesList;
	public static Fragment newInstance(Context context, ArrayList<AmenitiesBean> list){
		amenitiesList = list;
		Amenities amenity = new Amenities();
		return amenity;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.amenities, null);
		
		lv_amenities = (ListView)root.findViewById(R.id.lv_amenities);
		adapter = new AmenitiesAdapter(getActivity(), R.layout.restaurant_row, amenitiesList);
		lv_amenities.setAdapter(adapter);
		
		return root;
	}
}
