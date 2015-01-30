package com.waupi.adapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.waupi.bean.CouponBean;
import com.waupi.constants.Constant;
import com.waupi.screens.R;
import com.waupi.util.ImageLoader;

public class CouponAdapter extends ArrayAdapter<CouponBean>{
	private ArrayList<CouponBean> mMerchant = new ArrayList<CouponBean>();
	private ViewHolder mHolder;
	private Activity activity;
	public ImageLoader imageLoader;
	private static final String cacheDir = "/Android/data/cache/";
    private static final String CACHE_FILENAME = ".cache";


	public CouponAdapter(Activity activity, int textViewResourceId,ArrayList<CouponBean> items) {
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
			v = vi.inflate(R.layout.coupon_list_row, null);

			mHolder = new ViewHolder();
			v.setTag(mHolder);
			
			mHolder.iv_coupon=(ImageView)v.findViewById(R.id.img_coupon);
			mHolder.tv_validity = (TextView)v.findViewById(R.id.tv_validity);
			mHolder.tv_status = (TextView)v.findViewById(R.id.tv_status);
			mHolder.tv_coupon_name = (TextView)v.findViewById(R.id.tv_coupon_name);	
		}
		else {
			mHolder =  (ViewHolder) v.getTag();
		}				
		
		final CouponBean mVendor = mMerchant.get(position);
		
		if(mVendor != null){			
			mHolder.tv_coupon_name.setText(mVendor.getDescripton());
			mHolder.tv_validity.setText("Expires "+mVendor.getValidiy());	
			mHolder.tv_status.setText(mVendor.getStatus());
			if(!mVendor.getImage().equalsIgnoreCase("")){
				imageLoader.DisplayImage(Constant.URL+mVendor.getImage(), mHolder.iv_coupon);
			}else{
				mHolder.iv_coupon.setImageResource(R.drawable.amenities_no_image);
			}
		}		
		return v;
	}
	
	class ViewHolder {
		public TextView tv_coupon_name,tv_validity,tv_status;
		public ImageView iv_coupon;
	}
}