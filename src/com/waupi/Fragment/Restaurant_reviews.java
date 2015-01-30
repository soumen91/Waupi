package com.waupi.Fragment;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.waupi.adapter.ReviewAdapter;
import com.waupi.bean.ReviewsBean;
import com.waupi.network.HttpClient;
import com.waupi.screens.BaseScreen;
import com.waupi.screens.R;

public class Restaurant_reviews extends Fragment{

	public static String four_square_id =null;
	public ReviewAdapter adapter;
	public ArrayList<ReviewsBean> list = new ArrayList<ReviewsBean>();
	public static BaseScreen base;
	public ListView lv_reviews;
	
	public static Fragment newInstance(BaseScreen context, String four_square_id2){
		Restaurant_reviews deals = new Restaurant_reviews();
		four_square_id = four_square_id2;
		base = context;
		return deals;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.restaurent_reviews, null);
		
		lv_reviews = (ListView)root.findViewById(R.id.lv_reviews);
		getReviewList();
		return root;
	}
	private void getReviewList() {
		Thread t = new Thread(){
			public void run(){
				callWebserVice();
			}
		};
		t.start();
	}
	private void callWebserVice() {
		String url = "https://api.foursquare.com/v2/venues/" +
				four_square_id +
				"/tips?client_id=JXAAXQBZ0GD3DPAWZRHBKQN4QGIP5EMXTDKYRBW1Y3XXBUZT&client_secret=KO5W1KAFD4WFHVYI1Q3EIXF0OJHNZBHCNAN2C2Y1FX51ZYJJ&v=20140806";
		String response = HttpClient.getInstance(base).SendHttpGet(url);
		try {
			JSONObject ob = new JSONObject(response);
			JSONObject obj = ob.getJSONObject("response");
			JSONObject objct = obj.getJSONObject("tips");
			JSONArray arr = objct.getJSONArray("items");
			for(int i = 0 ; i < 5 ; i ++){
				JSONObject object = arr.getJSONObject(i);
				String id = object.getString("id");
				String text = object.getString("text");
				JSONObject jobj = object.getJSONObject("user");
				String firstname = jobj.getString("firstName");
				String lastName;
				if(!jobj.isNull("lastName")){
					lastName = jobj.getString("lastName");
				}else{
					lastName = "";
				}
				JSONObject jobject = jobj.getJSONObject("photo");
				String prefix = jobject.getString("prefix");
				String suffix = jobject.getString("suffix");
				String image_url = prefix + "120x120" + suffix;
				
				list.add(new ReviewsBean(id,
						firstname+" " +lastName,
						image_url,
						text));
			}
			updateUi();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	private void updateUi() {
		base.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				adapter = new ReviewAdapter(base, R.layout.review_row, list);
				lv_reviews.setAdapter(adapter);
			}
		});
	}
}
