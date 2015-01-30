package com.waupi.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.waupi.bean.ReviewsBean;
import com.waupi.constants.Constant;
import com.waupi.screens.R;
import com.waupi.util.ImageLoader;

public class ReviewAdapter extends ArrayAdapter<ReviewsBean>{
	private ArrayList<ReviewsBean> mMerchant = new ArrayList<ReviewsBean>();
	private ViewHolder mHolder;
	private Activity activity;
	public ImageLoader imageLoader;

	public ReviewAdapter(Activity activity, int textViewResourceId,ArrayList<ReviewsBean> items) {
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
			v = vi.inflate(R.layout.review_row, null);

			mHolder = new ViewHolder();
			v.setTag(mHolder);
			
			mHolder.iv_reviewer=(ImageView)v.findViewById(R.id.iv_reviewer);
			mHolder.tv_reviewer_name = (TextView)v.findViewById(R.id.tv_reviewer_name);
			mHolder.tv_reviewer_text = (TextView)v.findViewById(R.id.tv_reviewer_text);
				
		}
		else {
			mHolder =  (ViewHolder) v.getTag();
		}				
		
		final ReviewsBean mVendor = mMerchant.get(position);
		
		if(mVendor != null){			
			mHolder.tv_reviewer_name.setText(mVendor.getReview_name());
			mHolder.tv_reviewer_text.setText(mVendor.getReviewer_text());	
			//System.out.println("!!image "+);
			
			if(!mVendor.getReviewer_image().equalsIgnoreCase("")){
				imageLoader.DisplayImage(mVendor.getReviewer_image(), mHolder.iv_reviewer);
			}else{
				mHolder.iv_reviewer.setImageResource(R.drawable.user_noimg_icon);
			}
		}		
		return v;
	}
	
	class ViewHolder {
		public TextView tv_reviewer_name,tv_reviewer_text;
		public ImageView iv_reviewer;
	}
}