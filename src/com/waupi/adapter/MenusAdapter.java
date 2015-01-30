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
import com.waupi.bean.RestaurantMenuBean;
import com.waupi.constants.Constant;
import com.waupi.screens.R;
import com.waupi.util.ImageLoader;

public class MenusAdapter extends ArrayAdapter<EventsBean>{
	private ArrayList<RestaurantMenuBean> mMerchant = new ArrayList<RestaurantMenuBean>();
	private ViewHolder mHolder;
	private Activity activity;
	public ImageLoader imageLoader;

	public MenusAdapter(Activity activity, int textViewResourceId,ArrayList<RestaurantMenuBean> items) {
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
			v = vi.inflate(R.layout.menu_row, null);

			mHolder = new ViewHolder();
			v.setTag(mHolder);
			
			mHolder.iv_menu=(ImageView)v.findViewById(R.id.iv_menu);
			mHolder.tv_menu_desc = (TextView)v.findViewById(R.id.tv_menu_desc);
			mHolder.tv_menu_name = (TextView)v.findViewById(R.id.tv_menu_name);	
			mHolder.menu_rating = (RatingBar)v.findViewById(R.id.menu_rating);
		}
		else {
			mHolder =  (ViewHolder) v.getTag();
		}				
		
		final RestaurantMenuBean mVendor = mMerchant.get(position);
		
		if(mVendor != null){			
			mHolder.tv_menu_name.setText(mVendor.getName());
			mHolder.tv_menu_desc.setText(mVendor.getDesc());	
			mHolder.menu_rating.setRating(Float.parseFloat(mVendor.getRating()));
			if(!(mVendor.getImagepath().length() <= 0)){
				imageLoader.DisplayImage(Constant.URL+mVendor.getImagepath(), mHolder.iv_menu);
			}
		}		
		return v;
	}
	
	class ViewHolder {
		public RatingBar menu_rating;
		public TextView tv_menu_desc;
		public TextView tv_menu_name;
		public ImageView iv_menu;
	}
}