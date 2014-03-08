package com.txmcu.wifimanagerdemo;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.txmcu.xiaoxin.XinStateManager;
import com.txmcu.xiaoxin.XinStateManager.ConfigType;
import com.txmcu.xiaoxin.XinStateManager.XinOperations;

public class SettingActivity extends Activity implements XinOperations,
		OnClickListener {

	private static String TAG = "SettingActivity";
	
	ProgressDialog progress;
	XinStateManager xinMgr;
	EditText editSSIDEditText;
	EditText editPwdEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_setting);

		((ImageView) findViewById(R.id.back_img)).setOnClickListener(this);
		((Button) findViewById(R.id.btnconfig)).setOnClickListener(this);
		editSSIDEditText = (EditText) findViewById(R.id.editSSID);
		editPwdEditText = (EditText) findViewById(R.id.editPwd);

		xinMgr = XinStateManager.getInstance(this, this);
		xinMgr.Init();

		progress = ProgressDialog.show(this, "设置", "初始化配置", true,true,new DialogInterface.OnCancelListener(){
            @Override
            public void onCancel(DialogInterface dialog) {
                //YourTask.this.cancel(true);
            	//if(task!=null)
            	//	task.cancel();
                finish();
            }
        });
		
	}

	@Override
	protected void onDestroy() {
		xinMgr.Destroy();
		xinMgr = null;
		super.onDestroy();
		// Log.v(TAG, "onDestroy");

	}

	@Override
	public void initResult(boolean result, String SSID) {
		// TODO Auto-generated method stub
		Log.i(TAG, "init result"+SSID);
		progress.dismiss();
		progress = null;
		editSSIDEditText.setText(SSID);
	}

	@Override
	public void configResult(ConfigType type) {
		// TODO Auto-generated method stub
		if (progress != null) {
			progress.dismiss();
		}
		Log.i(TAG, "configResult result"+type);
		if(type == ConfigType.Succeed)
		{
			XiaoxinInfo info = new XiaoxinInfo();
			MainActivity.scannlist.add(info);
		}
		finish();
	}
	int cooldown = 120;
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.back_img) {
			finish();
		} else if (v.getId() == R.id.btnconfig) {
			progress = ProgressDialog.show(this, "设置", "开始连接设备 剩余120秒", true,true,new DialogInterface.OnCancelListener(){
                @Override
                public void onCancel(DialogInterface dialog) {
                    //YourTask.this.cancel(true);
                	if(task!=null)
                		task.cancel();
                    finish();
                }
            });
			xinMgr.Config(editSSIDEditText.getText().toString(),
					editPwdEditText.getText().toString());
			cooldown = 120;
			timer.schedule(task, 1000,1000);
			Log.i(TAG, "start config");
		}

	}
	
	@Override
	public void onBackPressed()
	{
	    finish();
	}

	private final Timer timer = new Timer();

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			// 要做的事情
			if (msg.what ==1) {
				cooldown--;
				if(cooldown<0)
				{
					progress.dismiss();
					if(task!=null)
						task.cancel();
					//Toast.makeText(SettingActivity.this, "设置失败", Toast.LENGTH_LONG).show();
					finish();
				}
				else {
					progress.setMessage("开始连接设备 剩余"+cooldown+"秒");
				}
				//Log.i(TAG, "PROGRESS");
				
			}
			super.handleMessage(msg);
		}

	};

	private TimerTask task = new TimerTask() {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message message = new Message();
			message.what = 1;
			handler.sendMessage(message);
		}
	};
}
