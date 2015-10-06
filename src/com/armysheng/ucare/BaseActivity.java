package com.armysheng.ucare;

import android.app.Activity;
import android.content.Intent;

import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public abstract class BaseActivity extends Activity{
	
	public static final String AppID = "222222";
	
	protected Tencent mTencent;
	
	//初始化腾讯实例
	public void initTencent()
	{
		
		mTencent = Tencent.createInstance(AppID, this);
	}
	
	public void login()
	{
		mTencent.login(this, "all", new  IUiListener(){
			
			public void onError(UiError arg0){
				showMessage("Login Error:"+ arg0.errorMessage);
			}
			

			public void onComplete(Object arg0) {
				// TODO Auto-generated method stub
				showMessage("Login Success:"+ arg0.toString());
		         Intent intent = new Intent();  
	                intent.setClass(BaseActivity.this, PlayVedio.class); 
	                intent.putExtra("rtspAddr", "from weibo");  //�������  
	                finish();
	                startActivity(intent);

			}
			
			public void onCancel(){
				showMessage("Login Cancel");
			}


			


			
			
		} );
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		if(mTencent != null){
			mTencent.onActivityResult(requestCode, resultCode, data);
		}
		
	}
	public abstract  void showMessage(String msg);

}
