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

import com.waupi.bean.AmenitiesBean;
import com.waupi.bean.EventsBean;
import com.waupi.constants.Constant;
import com.waupi.screens.R;
import com.waupi.util.ImageLoader;

public class AmenitiesAdapter extends ArrayAdapter<EventsBean>{
	private ArrayList<AmenitiesBean> mMerchant = new ArrayList<AmenitiesBean>();
	private ViewHolder mHolder;
	private Activity activity;
	public ImageLoader imageLoader;

	public AmenitiesAdapter(Activity activity, int textViewResourceId,ArrayList<AmenitiesBean> items) {
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
			
			mHolder.iv_amenity=(ImageView)v.findViewById(R.id.iv_restaurant);
			mHolder.amenity_rating = (RatingBar)v.findViewById(R.id.restaurant_rating);
			mHolder.tv_amenity_address = (TextView)v.findViewById(R.id.tv_restaurant_address);
			mHolder.tv_amenity_name = (TextView)v.findViewById(R.id.tv_restaurant_name);	
			
		}
		else {
			mHolder =  (ViewHolder) v.getTag();
		}				
		
		final AmenitiesBean mVendor = mMerchant.get(position);
		
		if(mVendor != null){			
			mHolder.tv_amenity_name.setText(mVendor.getAmenity_name());
			mHolder.tv_amenity_address.setText(mVendor.getAmenity_desc());	
			mHolder.amenity_rating.setRating(Float.parseFloat(mVendor.getAmenities_rating()));
			System.out.println("!!iamge link:"+mVendor.getAmenity_image());
			if(!mVendor.getAmenity_image().equalsIgnoreCase("")){
				imageLoader.DisplayImage(Constant.URL+mVendor.getAmenity_image(), mHolder.iv_amenity);
			}else{
				mHolder.iv_amenity.setImageResource(R.drawable.amenities_no_image);
			}
		}		
		return v;
	}
	
	class ViewHolder {
		public RatingBar amenity_rating;
		public TextView tv_amenity_name,tv_amenity_address;
		public ImageView iv_amenity;
	}
}