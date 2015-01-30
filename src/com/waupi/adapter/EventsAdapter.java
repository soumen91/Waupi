package com.waupi.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.waupi.bean.EventsBean;
import com.waupi.constants.Constant;
import com.waupi.screens.R;
import com.waupi.util.ImageLoader;

public class EventsAdapter extends ArrayAdapter<EventsBean>{
	private ArrayList<EventsBean> mMerchant = new ArrayList<EventsBean>();
	private ViewHolder mHolder;
	private Activity activity;
	public ImageLoader imageLoader;

	public EventsAdapter(Activity activity, int textViewResourceId,ArrayList<EventsBean> items) {
		super(activity, textViewResourceId);
		this.activity = activity;
		this.mMerchant = items;	
		imageLoader=new ImageLoader(activity);
	}		  

	@Override
	public int getCount() {
		return mMerchant.size();
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView( final int position,  View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.event_row, null);

			mHolder = new ViewHolder();
			v.setTag(mHolder);
			
			mHolder.iv_event=(ImageView)v.findViewById(R.id.iv_event);
			mHolder.tv_date = (TextView)v.findViewById(R.id.tv_date);
			mHolder.tv_event_name = (TextView)v.findViewById(R.id.tv_event_name);	
			mHolder.events_rating = (RatingBar)v.findViewById(R.id.events_rating);
		}
		else {
			mHolder =  (ViewHolder) v.getTag();
		}				
		
		final EventsBean mVendor = mMerchant.get(position);
		
		if(mVendor != null){			
			mHolder.tv_event_name.setText(mVendor.getEvent_name());
			mHolder.tv_date.setText(mVendor.getEvent_date());	
			mHolder.events_rating.setRating(Float.parseFloat(mVendor.getEvent_rating()));
			if(!mVendor.getEvent_image().equalsIgnoreCase("")){
				imageLoader.DisplayImage(Constant.URL+mVendor.getEvent_image(), mHolder.iv_event);
			}
		}		
		return v;
	}
	
	class ViewHolder {
		public RatingBar events_rating;
		public TextView tv_date;
		public TextView tv_event_name;
		public ImageView iv_event;
	}
}