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

import com.waupi.bean.RestaurantBean;
import com.waupi.constants.Constant;
import com.waupi.screens.R;
import com.waupi.util.ImageLoader;

public class RestaurantAdapter extends ArrayAdapter<RestaurantBean>{
	private ArrayList<RestaurantBean> mMerchant = new ArrayList<RestaurantBean>();
	private ViewHolder mHolder;
	private Activity activity;
	public ImageLoader imageLoader;

	public RestaurantAdapter(Activity activity, int textViewResourceId,ArrayList<RestaurantBean> items) {
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
			v = vi.inflate(R.layout.restaurant_row, null);

			mHolder = new ViewHolder();
			v.setTag(mHolder);
			
			mHolder.iv_restaurant=(ImageView)v.findViewById(R.id.iv_restaurant);
			mHolder.tv_restaurant_name = (TextView)v.findViewById(R.id.tv_restaurant_name);
			mHolder.tv_restaurant_address = (TextView)v.findViewById(R.id.tv_restaurant_address);
			mHolder.restaurant_rating = (RatingBar)v.findViewById(R.id.restaurant_rating);	
		}
		else {
			mHolder =  (ViewHolder) v.getTag();
		}				
		
		final RestaurantBean mVendor = mMerchant.get(position);
		
		if(mVendor != null){			
			mHolder.tv_restaurant_name.setText(mVendor.getRestaurant_name());
			mHolder.tv_restaurant_address.setText(mVendor.getRestaurant_address());	
			mHolder.restaurant_rating.setRating(Float.parseFloat(mVendor.getRestaurant_rating()));
			if(!mVendor.getRestaurant_iamge().equalsIgnoreCase("")){
				imageLoader.DisplayImage(Constant.URL+mVendor.getRestaurant_iamge(), mHolder.iv_restaurant);
			}
		}		
		return v;
	}
	
	class ViewHolder {
		public RatingBar restaurant_rating;
		public TextView tv_restaurant_name,tv_restaurant_address;
		public ImageView iv_restaurant;
	}
}