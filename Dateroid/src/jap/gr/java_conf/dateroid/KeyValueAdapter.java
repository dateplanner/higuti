package jap.gr.java_conf.dateroid;

import java.util.List;

import android.R.integer;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class KeyValueAdapter extends ArrayAdapter<String> {

	private List<? extends KeyValueItem> items;
	private Context context;
	
	public KeyValueAdapter(Context context, int resource,
			List<? extends KeyValueItem> items) {
		super(context, resource);
		this.context = context;
		this.items = items;
	}

	public int getCount() {
		return items.size();
	}

	public String getItem(int position) {
		return items.get(position).getStringValue();
	}

	public String getStringKey(int position) {
		return items.get(position).getStringKey();
	}
	
	public int getIntKey(int position){
		return items.get(position).getIntKey();
	}

	public int getPosition(String itemId) {
		for (int i = 0; i < items.size(); i++) {
			if(items.get(i).getStringKey() == itemId){
				return i;
			}
		}
		return 0;
	}
	
	public int getPosition(int itemId){
		for (int i = 0; i < items.size(); i++) {
			if(items.get(i).getIntKey() == itemId){
				return i;
			}
		}
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView label = new TextView(context);
		label.setTextColor(Color.BLACK);
		label.setText(items.get(position).getStringValue());
		return label;
	}
	
	

}
