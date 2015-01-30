package com.waupi.Fragment;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.waupi.adapter.DealsAdapter;
import com.waupi.bean.DealsBean;
import com.waupi.constants.Constant;
import com.waupi.network.HttpClient;
import com.waupi.screens.BaseScreen;
import com.waupi.screens.R;

public class Restaurant_deals extends Fragment{
	private DealsAdapter adapter;
	private static ArrayList<DealsBean> deallist = new ArrayList<DealsBean>();
	private ListView lv_deals;
	private static BaseScreen base;
	private int position = 0;
	
	public static Fragment newInstance(BaseScreen context, ArrayList<DealsBean> dealsarrList){
		Restaurant_deals deals = new Restaurant_deals();
		deallist = dealsarrList;
		base = context;
		return deals;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.restaurent_deals, null);
		
		lv_deals = (ListView)root.findViewById(R.id.lv_deals);
		adapter = new DealsAdapter(base, R.layout.deals_row, deallist);
		lv_deals.setAdapter(adapter);
		
		lv_deals.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				position= pos;
				//sendRequestToserver();
			}
		});
		return root;
	}
	private void sendRequestToserver() {
		Thread t = new Thread(){
			public void run(){
				base.doShowLoading();
				callWebserver();
				base.doRemoveLoading();
			}
		};
		t.start();
	}
	private void callWebserver() {
		JSONObject ob = new JSONObject();
		try {
			ob.put("code", deallist.get(position).getCode());
			String response = HttpClient.getInstance(base).SendHttpPost(Constant.COUON_REQUEST, ob.toString());
			if(response != null){
				JSONObject obj = new JSONObject(response);
				if(obj.getBoolean("status")){
					showAlertMessage(obj.getBoolean("status"));
				}else if(!obj.getBoolean("status")){
					showAlertMessage(obj.getBoolean("status"));
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	private void showAlertMessage(boolean b) {
		final boolean flag = b;
		base.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				final Dialog dialog = new Dialog(base);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
				dialog.setContentView(R.layout.coupon_request_dialog);
				dialog.setCancelable(false);
						
				TextView tv_text = (TextView)dialog.findViewById(R.id.tv_text);
				if(flag){
					tv_text.setText("Your request for coupon has been received & we will deliver it shortly upon your hotel stay confirmation!!!");
				}else{
					tv_text.setText("You have already request for this coupon!!!");
				}
				Button btn_yes = (Button)dialog.findViewById(R.id.btn_yes);
				btn_yes.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						dialog.cancel();			
						}
				});
				dialog.show();
			}
		});
	}
}
