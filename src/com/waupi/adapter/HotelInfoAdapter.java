package com.waupi.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.waupi.adapter.HotelInfoAdapter.ViewHolder;
import com.waupi.bean.HotelInfoBean;
import com.waupi.screens.R;
import com.waupi.util.ImageLoader;

public class HotelInfoAdapter extends ArrayAdapter<HotelInfoBean>{
	private ArrayList<HotelInfoBean> mMerchant = new ArrayList<HotelInfoBean>();
	private ViewHolder mHolder;
	private Activity activity;
	public ImageLoader imageLoader;

	public HotelInfoAdapter(Activity activity, int textViewResourceId,ArrayList<HotelInfoBean> items) {
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
			v = vi.inflate(R.layout.hotel_list_row, null);

			mHolder = new ViewHolder();
			v.setTag(mHolder);
			
			mHolder.img_hotel=(ImageView)v.findViewById(R.id.img_hotel);
			mHolder.tv_hotel_address = (TextView)v.findViewById(R.id.tv_hotel_address);
			mHolder.tv_hotel_name = (TextView)v.findViewById(R.id.tv_hotel_name);	
			mHolder.review_rating = (RatingBar)v.findViewById(R.id.review_rating);
		}
		else {
			mHolder =  (ViewHolder) v.getTag();
		}				
		
		final HotelInfoBean mVendor = mMerchant.get(position);
		
		if(mVendor != null){			
			mHolder.tv_hotel_address.setText(mVendor.getLocation());
			mHolder.tv_hotel_name.setText(mVendor.name);			
			mHolder.review_rating.setRating(Float.parseFloat(mVendor.rating));				
			//imageLoader.DisplayImage(Constant.IMAGE_URL+mVendor.image, mHolder.img_hotel);
		}		
		return v;
	}
	
	class ViewHolder {
		public RatingBar review_rating;
		public TextView tv_hotel_address;
		public TextView tv_hotel_name;
		public ImageView img_hotel;
	}
}
