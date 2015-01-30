package com.waupi.Fragment;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.waupi.adapter.RestaurantAdapter;
import com.waupi.bean.RestaurantBean;
import com.waupi.constants.Constant;
import com.waupi.screens.BaseScreen;
import com.waupi.screens.R;
import com.waupi.screens.Restaurant_Details;

public class Restaurant extends Fragment{
	
	ImageView imagview1,imagview2,imagview3,imagview4,imagview5,imagview6;
	private static ArrayList<RestaurantBean> restaurantList;
	private static BaseScreen base;
	public static Restaurant restaurent ;
	private RestaurantAdapter adapter;
	private ListView lv_restaurant;
	
	public static Fragment newInstance(BaseScreen b, ArrayList<RestaurantBean> list){
		base = b;
		restaurantList = list;
		restaurent = new Restaurant();
		return restaurent;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.restaurent, null);
		
		lv_restaurant = (ListView)root.findViewById(R.id.lv_restaurant);
		adapter = new RestaurantAdapter(getActivity(), R.layout.restaurant_row, restaurantList);
		lv_restaurant.setAdapter(adapter);
		
		lv_restaurant.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,long arg3) {
				Intent i = new Intent(base,Restaurant_Details.class);
				Constant.position = pos;
				startActivity(i);
			}
		});
		return root;
	}
}
