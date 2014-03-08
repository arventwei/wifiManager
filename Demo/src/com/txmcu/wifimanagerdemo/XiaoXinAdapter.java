package com.txmcu.wifimanagerdemo;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class XiaoXinAdapter extends BaseAdapter {


	
	public List<XiaoxinInfo> mResults;

	private Context mContext;

	public XiaoXinAdapter(List<XiaoxinInfo> results, Context mContext) {

		this.mResults = results;
		this.mContext = mContext;
		System.out.println("into  WifiHotAdapter results =" + this.mResults);

	}

	@Override
	public int getCount() {
		System.out.println("into  WifiHotAdapter getCount() results size=" + mResults.size());
		return mResults.size();
	}

	@Override
	public Object getItem(int position) {
		System.out.println("into  WifiHotAdapter getItem  Object=" + mResults.get(position));
		return mResults.get(position);
	}

	@Override
	public long getItemId(int position) {
		System.out.println("into  WifiHotAdapter getItemId  position=" + position);
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		System.out.println("into getView()");
		TextView nameTxt = null;
		TextView levelTxt = null;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.wifihot_layout, null);
		}
		nameTxt = (TextView) convertView.findViewById(R.id.hotName);
		levelTxt = (TextView) convertView.findViewById(R.id.hotLevel);
		nameTxt.setText(mResults.get(position).nameString);
		levelTxt.setText("Level :" + mResults.get(position).deviceId);
		System.out.println("out getView()");
		return convertView;
	}

	public void refreshData(List<XiaoxinInfo> results) {
		System.out.println("into refreshData(List<ScanResult> results) results.size =" + results.size());
		this.mResults = results;
		this.notifyDataSetChanged();
		System.out.println("out refreshData(List<ScanResult> results)");
	}

	public void clearData() {

		if (mResults != null && mResults.size() > 0) {
			mResults.clear();
			mResults = null;
		}
	}
}
