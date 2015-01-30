package com.waupi.Fragment;

import java.util.ArrayList;

import com.waupi.adapter.EventsAdapter;
import com.waupi.bean.EventsBean;
import com.waupi.screens.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class Events extends Fragment{
	private static ArrayList<EventsBean> eventsList;
	private EventsAdapter adapter;
	private ListView lv_events;

	public static Fragment newInstance(Context context, ArrayList<EventsBean> list){
		eventsList = list;
		Events event = new Events();
		return event;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.events, null);
		lv_events = (ListView)root.findViewById(R.id.lv_events);
		
		adapter = new EventsAdapter(getActivity(), R.layout.event_row, eventsList);
		lv_events.setAdapter(adapter);
		
		return root;
	}
}
