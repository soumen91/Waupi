package com.waupi.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.waupi.Fragment.Restaurant_deals;
import com.waupi.Fragment.Restaurant_menu;
import com.waupi.Fragment.Restaurant_reviews;
import com.waupi.bean.DealsBean;
import com.waupi.bean.RestaurantMenuBean;
import com.waupi.screens.BaseScreen;

public class RestaurantViewPagerAdapter extends FragmentPagerAdapter{

	private BaseScreen base;
    public static int totalPage=3;
    public ArrayList<RestaurantMenuBean> arrayList;
    public ArrayList<DealsBean> dealsarrList;
    public String four_square_id= null;
    public RestaurantViewPagerAdapter(BaseScreen b, FragmentManager fm, ArrayList<RestaurantMenuBean> arrayList, ArrayList<DealsBean> dealslist, String four_square_id) {
        super(fm);
        base=b;
        this.arrayList = arrayList;
        this.dealsarrList = dealslist;
        this.four_square_id = four_square_id;
        }

	@Override
	public Fragment getItem(int position) {
		
		 Fragment f = new Fragment();
		 switch (position) {
		 	case 0:
		 		f=Restaurant_menu.newInstance(base,arrayList);
	            break;
		 	case 1:
	            f=Restaurant_reviews.newInstance(base,four_square_id);
	            break;
			case 2:
				f=Restaurant_deals.newInstance(base,dealsarrList);
				break;
		}
	return f;				 
	}

	@Override
	public int getCount() {
		return totalPage;
	}
}
