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

import com.waupi.bean.DealsBean;
import com.waupi.bean.EventsBean;
import com.waupi.constants.Constant;
import com.waupi.screens.R;
import com.waupi.util.ImageLoader;

public class DealsAdapter extends ArrayAdapter<EventsBean>{
	private ArrayList<DealsBean> mMerchant = new ArrayList<DealsBean>();
	private ViewHolder mHolder;
	private Activity activity;
	public ImageLoader imageLoader;

	public DealsAdapter(Activity activity, int textViewResourceId,ArrayList<DealsBean> items) {
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
			v = vi.inflate(R.layout.deals_row, null);

			mHolder = new ViewHolder();
			v.setTag(mHolder);
			
			mHolder.iv_dealimage=(ImageView)v.findViewById(R.id.iv_dealimage);
			mHolder.tv_deals_validity = (TextView)v.findViewById(R.id.tv_deals_validity);
			mHolder.tv_deals_name = (TextView)v.findViewById(R.id.tv_deals_name);	
		}
		else {
			mHolder =  (ViewHolder) v.getTag();
		}				
		
		final DealsBean mVendor = mMerchant.get(position);
		
		if(mVendor != null){			
			mHolder.tv_deals_name.setText(mVendor.getDescripton());
			mHolder.tv_deals_validity.setText("Expires "+mVendor.getValidiy());	
			if(!(mVendor.getImage().length() <= 0)){
				imageLoader.DisplayImage(Constant.URL+mVendor.getImage(), mHolder.iv_dealimage);
			}else{
				if(position == 0){
					mHolder.iv_dealimage.setImageResource(R.drawable.deals_noimg);
				}
			}
		}		
		return v;
	}
	
	class ViewHolder {
		public TextView tv_deals_name;
		public TextView tv_deals_validity;
		public ImageView iv_dealimage;
	}
}