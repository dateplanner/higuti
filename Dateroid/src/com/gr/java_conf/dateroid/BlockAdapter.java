package com.gr.java_conf.dateroid;

import jap.gr.java_conf.dateroid.R;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class BlockAdapter extends BaseAdapter {

	private Context context;
	private List<Block> items;
	private List<Object> viewTypes;
	private LayoutInflater inflater;
	
	public BlockAdapter(Context context, List<Block> items){
		this.context = context;
		this.items = items;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		viewTypes = new ArrayList<Object>();
		viewTypes.add(BlockDateSpot.class);
		viewTypes.add(BlockRestaurant.class);
		viewTypes.add(BlockFree.class);
	}
	
	@Override
	public int getCount() {
		return items.size();
	}

	
	@Override
	public Block getItem(int position) {
		return items.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getViewTypeCount() {
		return 3;
	}
	
	@Override
	public int getItemViewType(int position) {
		Object item = items.get(position);
		return viewTypes.indexOf(item.getClass());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View blockView =null;
		
		Block baseItem = items.get(position);
		if(baseItem instanceof BlockDateSpot){
			if(convertView == null){
				blockView = inflater.inflate(R.layout.list_item_block_datespot, parent, false);
			}else {
				blockView = convertView;
			}
			TextView blockNo = (TextView)blockView.findViewById(R.id.blockNo_text);
			TextView spotName = (TextView)blockView.findViewById(R.id.spotName_text);
			TextView address = (TextView)blockView.findViewById(R.id.address_text);
			
			BlockDateSpot item = (BlockDateSpot)baseItem;
			
			blockNo.setText(String.valueOf(item.getBlockNo()));
			spotName.setText(item.getSpotName());
			address.setText(item.getAddress());
		}else if(baseItem instanceof BlockRestaurant){
			if(convertView == null){
				blockView = inflater.inflate(R.layout.list_item_block_restaurant, parent, false);
			}else {
				blockView = convertView;
			}
			ImageView restaurant = (ImageView)blockView.findViewById(R.id.restaurant_image);
//			restaurant.setImageDrawable(drawable);
		}else {
			if(convertView == null){
				blockView = inflater.inflate(R.layout.list_item_block_free, parent, false);
			}else {
				blockView = convertView;
			}
			TextView blockNo = (TextView)blockView.findViewById(R.id.blockNo_text);
			TextView comment = (TextView)blockView.findViewById(R.id.comment_text);
			
			BlockFree item = (BlockFree)baseItem;
			blockNo.setText(String.valueOf(item.getBlockNo()));
			comment.setText(item.getComment());
		}
		
		return blockView;
	}
	

}
