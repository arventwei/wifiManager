package com.txmcu.wifimanagerdemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.txmcu.xiaoxin.XinStateManager;
import com.txmcu.xiaoxin.XinStateManager.ConfigType;
import com.txmcu.xiaoxin.XinStateManager.XinOperations;

public class SettingActivity extends Activity implements XinOperations, OnClickListener{

	ProgressDialog progress;
	XinStateManager xinMgr;
	EditText editSSIDEditText;
	EditText editPwdEditText;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_setting);
		
		
		((ImageView)findViewById(R.id.back_img)).setOnClickListener(this);
		((Button)findViewById(R.id.btnconfig)).setOnClickListener(this);
		editSSIDEditText = (EditText)findViewById(R.id.editSSID);
		editPwdEditText = (EditText)findViewById(R.id.editPwd);
		
		xinMgr = XinStateManager.getInstance(this, this);
		xinMgr.Init();
		
		progress = ProgressDialog.show(this, "设置",
			    "初始化配置", true);
	}
	
	@Override  
    protected void onDestroy() {
		xinMgr.Destroy();
		xinMgr=null;
        super.onDestroy();  
       // Log.v(TAG, "onDestroy");  
        
    }

	@Override
	public void initResult(boolean result,String SSID) {
		// TODO Auto-generated method stub
		progress.dismiss();
		progress = null;
		editSSIDEditText.setText(SSID);
	}

	@Override
	public void configResult(ConfigType type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId()==R.id.back_img) {
			finish();
		}
		else if(v.getId()==R.id.btnconfig){
			progress = ProgressDialog.show(this, "设置",
				    "开始连接设备 剩余120秒", true);
			 xinMgr.Config(editSSIDEditText.getText().toString(), editPwdEditText.getText().toString());
		}
		
	} 
}
