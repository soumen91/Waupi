package com.waupi.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

import com.waupi.Fragment.Amenities;
import com.waupi.Fragment.Atm;
import com.waupi.Fragment.Events;
import com.waupi.Fragment.Restaurant;
import com.waupi.bean.AmenitiesBean;
import com.waupi.bean.AtmBean;
import com.waupi.bean.EventsBean;
import com.waupi.bean.RestaurantBean;
import com.waupi.screens.ExploreHotel;

public class ViewPagerAdapter extends FragmentPagerAdapter {

	private ExploreHotel base;
	public static int totalPage = 4;
	private FragmentTransaction mCurTransaction = null;
	private ArrayList<RestaurantBean> restaurantList;
	private ArrayList<AtmBean> atmtList;
	private ArrayList<EventsBean> eventsList;
	private ArrayList<AmenitiesBean> amenitiesList;

	public ViewPagerAdapter(ExploreHotel b, FragmentManager fm, ArrayList<RestaurantBean> restaurantList, ArrayList<AtmBean> atmList, ArrayList<EventsBean> eventList, ArrayList<AmenitiesBean> amenitiesList) {
		super(fm);
		base = b;
		this.restaurantList = restaurantList;
		this.atmtList = atmList;
		this.eventsList = eventList;
		this.amenitiesList = amenitiesList;
	}

	@Override
	public Fragment getItem(int position) {
		
		Fragment f = new Fragment();
		switch (position) {
		case 0:
			f = Amenities.newInstance(base,amenitiesList);
			break;
		case 1:
			f = Restaurant.newInstance(base,restaurantList);
			break;
		case 2:
			f = Atm.newInstance(base,atmtList);
			break;
		case 3:
			f = Events.newInstance(base,eventsList);
			break;
		}
		return f;
	}

	@Override
	public int getCount() {
		return totalPage;
	}
}
