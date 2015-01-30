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

import com.waupi.bean.DashBoardHotelListBean;
import com.waupi.constants.Constant;
import com.waupi.screens.R;
import com.waupi.util.ImageLoader;

public class DashBoardHotelListAdapter extends ArrayAdapter<DashBoardHotelListBean>{
	private ArrayList<DashBoardHotelListBean> mMerchant = new ArrayList<DashBoardHotelListBean>();
	private ViewHolder mHolder;
	private Activity activity;
	public ImageLoader imageLoader;

	public DashBoardHotelListAdapter(Activity activity, int textViewResourceId,ArrayList<DashBoardHotelListBean> items) {
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
			v = vi.inflate(R.layout.dashboard_hotel_list_row, null);

			mHolder = new ViewHolder();
			v.setTag(mHolder);
			
			mHolder.iv_hotel=(ImageView)v.findViewById(R.id.img_hotel);
			mHolder.tv_hotel_desc = (TextView)v.findViewById(R.id.tv_hotel_address);
			mHolder.tv_hotel_name = (TextView)v.findViewById(R.id.tv_hotel_name);	
			mHolder.hotel_rating = (RatingBar)v.findViewById(R.id.review_rating);
		}
		else {
			mHolder =  (ViewHolder) v.getTag();
		}				
		
		final DashBoardHotelListBean mVendor = mMerchant.get(position);
		
		if(mVendor != null){			
			mHolder.tv_hotel_name.setText(mVendor.getHotel_name());
			mHolder.tv_hotel_desc.setText(mVendor.getHotel_address());	
			mHolder.hotel_rating.setRating(Float.parseFloat(mVendor.getHotel_rating()));
			if(!mVendor.getHotel_image().equalsIgnoreCase("")){
				imageLoader.DisplayImage(Constant.URL+mVendor.getHotel_image(), mHolder.iv_hotel);
			}else{
				mHolder.iv_hotel.setImageResource(R.drawable.hotel_no_img);
			}
		}		
		return v;
	}
	
	class ViewHolder {
		public RatingBar hotel_rating;
		public TextView tv_hotel_desc;
		public TextView tv_hotel_name;
		public ImageView iv_hotel;
	}
}
