package com.smarttiger.bigdial;

import java.util.Arrays;

import com.smarttiger.bigdial.DataControl.LuckyData;
import com.smarttiger.bigdial.DataControl.SettingData;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CheatActivity extends Activity {

	private Context context;
	private DataControl dataControl;
	private SettingData settingData;
	private LuckyData luckyData;
	private CheckBox hasCheatBox;
	private EditText cheatIndexEdit;
	private ListView listView;
    private LayoutInflater mLayoutInflater;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cheat_activity);
		context = this;
        mLayoutInflater = LayoutInflater.from(this);
        
        Intent intent = getIntent();
        
		
		dataControl = new DataControl(this);
		settingData = dataControl.getSettingData();
		luckyData = dataControl.getLuckyData();
		settingData.cheatCount = luckyData.itemCount;
		if(settingData.cheatIndexs == null || settingData.cheatIndexs.length != settingData.cheatCount) {
			settingData.cheatIndexs = new boolean[settingData.cheatCount];
			Arrays.fill(settingData.cheatIndexs, false); 
		}
		
		initView();
		initListView();
		
	}
	
	private void initView()
	{
		hasCheatBox = (CheckBox) findViewById(R.id.hasCheatBox);
		hasCheatBox.setChecked(settingData.hasCheat);
		hasCheatBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				settingData.hasCheat = isChecked;
			}
		});
	}

	
	private void initListView()
	{
		listView = (ListView) findViewById(R.id.list);
		listView.setAdapter(new CheatAdapter());
		listView.setItemChecked(2, true);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				if(luckyData.weights[position] == 0)
					return;
				ViewHolder holder = (ViewHolder) view.getTag();
				boolean isCheck = ! holder.checkBox.isChecked();
				holder.checkBox.setChecked(isCheck);
				settingData.cheatIndexs[position] = isCheck;
				
			}
		});
	}
	
	
	private class CheatAdapter extends BaseAdapter
	{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return luckyData.itemCount;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
		 */
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final ViewHolder holder;
			if(convertView ==null) {
				convertView = mLayoutInflater.inflate(R.layout.cheat_item, parent, false);
				holder = new ViewHolder();
				holder.checkBox = (CheckBox) convertView.findViewById(R.id.check_box);
				holder.textView = (TextView) convertView.findViewById(R.id.text_view);
				holder.redoView = (ImageView) convertView.findViewById(R.id.redo_image);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.checkBox.setChecked(settingData.cheatIndexs[position]);
			holder.textView.setText(luckyData.names[position]);
			
			holder.redoView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					luckyData.weights[position] = 1;
					luckyData.allWeight++;
					holder.checkBox.setEnabled(true);
					((View) v.getParent()).setEnabled(true);
					holder.redoView.setVisibility(View.GONE);
				}
			});
			
			if(luckyData.weights[position] == 0) {
				holder.checkBox.setEnabled(false);
				convertView.setEnabled(false);
				holder.redoView.setVisibility(View.VISIBLE);
			}
			else {
				holder.checkBox.setEnabled(true);
				convertView.setEnabled(true);
				holder.redoView.setVisibility(View.GONE);
			}
				
			return convertView;
		}
		
	}
	private class ViewHolder
	{
		CheckBox checkBox;
		TextView textView;
		ImageView redoView;
	}
	
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		dataControl.saveSetting(settingData);
		dataControl.putItems(luckyData);
	}

	public void onBackToMain(View view) {
		this.finish();
	}
}
