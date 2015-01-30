package com.waupi.Fragment;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.waupi.adapter.MenusAdapter;
import com.waupi.bean.RestaurantMenuBean;
import com.waupi.screens.R;

public class Restaurant_menu extends Fragment{
	public static ArrayList<RestaurantMenuBean> arrayList;
	private ListView lv_menus;
	private MenusAdapter adapter;
	public static Fragment newInstance(Context context, ArrayList<RestaurantMenuBean> list){
		Restaurant_menu deals = new Restaurant_menu();
		arrayList = list;
		return deals;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.restaurent_menu, null);
		lv_menus = (ListView)root.findViewById(R.id.lv_menus);
		adapter = new MenusAdapter(getActivity(), R.layout.menu_row, arrayList);
		lv_menus.setAdapter(adapter);
		return root;
	}
}
