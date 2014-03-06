package com.txmcu.wifimanagerdemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;

import com.txmcu.xiaoxin.XinStateManager;
import com.txmcu.xiaoxin.XinStateManager.ConfigType;
import com.txmcu.xiaoxin.XinStateManager.XinOperations;

public class SettingActivity extends Activity implements XinOperations {

	ProgressDialog progress;
	XinStateManager xinMgr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_setting);
		xinMgr = XinStateManager.getInstance(this, this);
		xinMgr.Init();
		
		progress = ProgressDialog.show(this, "dialog title",
			    "dialog message", true);
	}
	
	@Override  
    protected void onDestroy() {
		xinMgr.Destroy();
		xinMgr=null;
        super.onDestroy();  
       // Log.v(TAG, "onDestroy");  
        
    }

	@Override
	public void initResult(boolean result) {
		// TODO Auto-generated method stub
		progress.dismiss();
		
	}

	@Override
	public void configResult(ConfigType type) {
		// TODO Auto-generated method stub
		
	} 
}
