package com.waupi.application;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.waupi.constants.Constant;

public class TutorialInfo {

		public int isTutorialTraverse = 0;
		public boolean isUserAuthenticated = false;
		public SharedPreferences preferences;
		
		public TutorialInfo(Context ctx){
			preferences = ctx.getSharedPreferences(Constant.Values.TUTORIALTRAVERSE.name(), Context.MODE_PRIVATE);
			isTutorialTraverse = preferences.getInt(Constant.Values.ISTRAVERSETUTORIAL.name(), 0);
			isUserAuthenticated = preferences.getBoolean(Constant.Values.ISUSER_AUTHENTICATED.name(), false);
		}

		public void setTutorialInfo(int isTutorialTraverse) {
			this.isTutorialTraverse = isTutorialTraverse;
			
			Editor edit = preferences.edit();
			edit.putInt(Constant.Values.ISTRAVERSETUTORIAL.name(), isTutorialTraverse);
			edit.commit();
		}
		
		public void setUserAuthenticationInfo(Boolean isAuthenticated) {
			
			System.out.println("### "+ isAuthenticated);
			this.isUserAuthenticated = isAuthenticated;
			
			Editor edit = preferences.edit();
			edit.putBoolean(Constant.Values.ISUSER_AUTHENTICATED.name(), isUserAuthenticated);
			edit.commit();
		}
		
		public boolean isAuthenticated(){
			return preferences.getBoolean(Constant.Values.ISUSER_AUTHENTICATED.name(), false);
		}
}
