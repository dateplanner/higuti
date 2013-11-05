package com.gr.java_conf.dateroid;

import jap.gr.java_conf.dateroid.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

import com.gr.java_conf.dateroid.DirectionsJSON.TravelMode;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.DragEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerOpenListener;

@SuppressWarnings("deprecation")
public class EditPlanActivity extends Activity {

	private ListView blockList;
	private ImageButton auto;
	private ImageButton update;
	private ImageButton gabagebox;
	private ImageButton newBlock;
	private ImageButton save;
	private ImageButton doDate;
	private SlidingDrawer drawer;
	private WebView webView;
	
	private DBAdapter dbAdapter;
	private LinkedList<Block> blocks;
	private int[] areas;
	private int outdoorFlg;
	private int blockCounts;
	private int currentPosition;
	private int newPosition;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_plan);
		
		blockList = (ListView)findViewById(R.id.block_list);
		auto = (ImageButton)findViewById(R.id.auto_btn);
		update = (ImageButton)findViewById(R.id.update_btn);
		gabagebox = (ImageButton)findViewById(R.id.gabagebox_btn);
		newBlock = (ImageButton)findViewById(R.id.new_btn);
		save = (ImageButton)findViewById(R.id.save_btn);
		doDate = (ImageButton)findViewById(R.id.doDate_btn);
		drawer = (SlidingDrawer)findViewById(R.id.drawer);
		webView = (WebView)findViewById(R.id.webview);
		
		Intent intent = getIntent();
		if(intent == null){
			return;
		}
		
		areas = intent.getIntArrayExtra("areas");
		outdoorFlg = intent.getIntExtra("outdoorFlg", 0);
		blockCounts = intent.getIntExtra("blocks", 0);
		
		blocks = new LinkedList<Block>();
		
		dbAdapter = new DBAdapter(this);
		dbAdapter.openReadableDatabase();
		Cursor cursor = dbAdapter.selectAutoDateSpot(areas, outdoorFlg, blockCounts);
		int blockNo = 1;
		while (cursor.moveToNext()) {
			BlockDateSpot blockDS = new BlockDateSpot();
			
			blockDS.setBlockNo(blockNo);
			blockDS.setSpotId(cursor.getInt(cursor.getColumnIndex("_id")));
			blockDS.setSpotName(cursor.getString(cursor.getColumnIndex("datespot_name")));
			blockDS.setAddress(cursor.getString(cursor.getColumnIndex("datespot_address")));

			blocks.add(blockDS);
			blockNo++;
			if(blockNo == 3){
				BlockRestaurant blockRS = new BlockRestaurant();
				blockRS.setBlockNo(blockNo);
				blocks.add(blockRS);
				blockNo++;
			}
		}
		BlockAdapter adapter = new BlockAdapter(this, blocks);
		blockList.setAdapter(adapter);
		cursor.close();
		
		blockList.setOnItemClickListener(blockClickListener);
		blockList.setOnItemLongClickListener(blockLongClickListener);
		newBlock.setOnLongClickListener(newBlockLongClickListener);
		
		gabagebox.setOnDragListener(blockDragListenertoGabageBox);
		blockList.setOnDragListener(BlockDragListener);
//		blockList.setOnDragListener(blockDragListenerforExchange);
		
		auto.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				int count = 0;
				ArrayList<Integer> exchangeList = new ArrayList<Integer>();
				for (Block block : blocks) {
					if(!block.isPin()){
						exchangeList.add(block.getBlockNo());
						count++;
					}
				}
				if(count == 0){
					//交換するブロックなし
					return;
				}
				dbAdapter = new DBAdapter(getApplicationContext());
				dbAdapter.openReadableDatabase();
				Cursor cursor = dbAdapter.selectAutoDateSpot(areas, outdoorFlg, count);
				for (int i = 0; i < count; i++) {
					cursor.moveToNext();
					BlockDateSpot blockDS = new BlockDateSpot();
					int blockPos = exchangeList.get(i);
					
					blockDS.setBlockNo(blockPos);
					blockDS.setSpotId(cursor.getInt(cursor.getColumnIndex("_id")));
					blockDS.setSpotName(cursor.getString(cursor.getColumnIndex("datespot_name")));
					blockDS.setAddress(cursor.getString(cursor.getColumnIndex("datespot_address")));
					blocks.set(blockPos -1, blockDS);
					
				}
				BlockAdapter adapter = new BlockAdapter(EditPlanActivity.this, blocks);
				blockList.setAdapter(adapter);
				cursor.close();
			}
				
		});
		
		save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(EditPlanActivity.this)
				.setTitle(getText(R.string.save_date_plan_title))
				.setMessage(getText(R.string.save_date_plan_message))
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dbAdapter = new DBAdapter(getApplicationContext());
						dbAdapter.openWritableDatabase();
						String today = DateFormat.format("yyyyMMdd", Calendar.getInstance()).toString();
						long rowId = dbAdapter.insertDatePlan(today);
						if(rowId == -1){
							//エラー処理
						}
						for (Block block : blocks) {
							int blockType;
							
							if(block instanceof BlockDateSpot){
								BlockDateSpot blockDS = (BlockDateSpot)block;
								blockType = 0;
								int spotId = blockDS.getSpotId();
								dbAdapter.insertPlanSpotDetail(rowId, block.getBlockNo(), spotId, blockType);
							}else if(block instanceof BlockRestaurant){
								BlockRestaurant blockRS = (BlockRestaurant)block;
								blockType = 1;
								String restaurantId = blockRS.getRestaurantId();
								dbAdapter.insertPlanRestaurantDetail(rowId, block.getBlockNo(), restaurantId, blockType);
							}else {
								BlockFree blockFR = (BlockFree)block;
								blockType = 2;
								String comment = blockFR.getComment();
								dbAdapter.insertPlanFreeDetail(rowId, block.getBlockNo(), comment, blockType);
							}
						}
					}
				})
				.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				}).show();
			}
		});
		
		drawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {
			@Override
			public void onDrawerOpened() {
				
				DirectionsJSON directionsJSON = new DirectionsJSON();
				
				ListAdapter adapter = blockList.getAdapter();
				
				Block originBlock = (Block)adapter.getItem(0);
				Block destinationBlock = (Block)adapter.getItem(adapter.getCount() -1);
				directionsJSON.setOrigin(originBlock.getAddress());
				directionsJSON.setDestination(destinationBlock.getAddress());
		
				for (int i = 1; i < adapter.getCount() -1; i++) {
					Block waypointBlock = (Block)adapter.getItem(i);
					String address = waypointBlock.getAddress();
					
					if(adapter.getItemViewType(i) != 2 &&  address != null){
						directionsJSON.setWaypoint(address);
					}
				}
				directionsJSON.setTravelMode(TravelMode.DRIVING);
				
				webView.addJavascriptInterface(directionsJSON, "android");
				webView.requestFocusFromTouch();
				webView.setWebViewClient(new WebViewClient());
				webView.setWebChromeClient(new WebChromeClient());
				
				//webページをロード
				webView.loadUrl("file:///android_asset/whole_direction.html");	
				WebSettings webSettings = webView.getSettings();
				webSettings.setJavaScriptEnabled(true);
			}
		});
	}
	
	private OnItemClickListener blockClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			ListAdapter adapter = (ListAdapter)parent.getAdapter();
			switch (adapter.getItemViewType(position)) {
			//デートスポット
			case 0:
				BlockDateSpot blockDS = (BlockDateSpot)adapter.getItem(position);

				break;
			//レストラン
			case 1:
				BlockRestaurant blockRS = (BlockRestaurant)adapter.getItem(position);
				if(blockRS.getRestaurantId() != null){
					
					
				}else {
					ArrayList<MyObject> spotList = new ArrayList<MyObject>();
					for (Block block : blocks) {
						if(block instanceof BlockDateSpot && ((BlockDateSpot)block).getSpotId() != 0){
							BlockDateSpot _blockDS = (BlockDateSpot)block;
							spotList.add(new MyObject(_blockDS.getSpotId(), _blockDS.getSpotName()));
						}
					}
					Intent intent = new Intent(getApplicationContext(), RestaurantNearbySearchActivity.class);
					intent.putExtra("spotList", spotList);
					startActivity(intent);
				}
				break;
			//フリー
			case 2:
				BlockFree blockFR = (BlockFree)adapter.getItem(position);
				break;
			}
			
		}
	};
	
	private OnItemLongClickListener blockLongClickListener = new OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			ClipDescription description = new ClipDescription("blockLongClick", 
					new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN});
			ClipData.Item item = new ClipData.Item(String.valueOf(id));
			ClipData dragData = new ClipData(description, item);
			
			DragShadowBuilder shadow = new DragShadowBuilder(view);
			view.startDrag(dragData, shadow, null, 0);
			return false;
		}
	};
	
	private OnLongClickListener newBlockLongClickListener = new OnLongClickListener() {
		@Override
		public boolean onLongClick(View v) {
			ClipDescription description = new ClipDescription("newBlockLongClick",
					new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN});
			ClipData.Item item = new ClipData.Item(v.toString());
			ClipData dragData = new ClipData(description, item);
			
			DragShadowBuilder shadow = new DragShadowBuilder(v);
			v.startDrag(dragData, shadow, null, 0);
			return false;
		}
	};
	
//	private OnDragListener newBlockDragListener = new OnDragListener() {
//		@Override
//		public boolean onDrag(View v, DragEvent event) {
//			switch (event.getAction()) {
//			case DragEvent.ACTION_DRAG_STARTED:
//				if(!(event.getClipDescription().getLabel().equals("newBlockLongClick")
//						&& v.getClass().equals(ListView.class))){
//					return false;
//				}
//				break;
//
//			case DragEvent.ACTION_DRAG_ENTERED:
//				//listの色を変更?
//				break;
//				
//			case DragEvent.ACTION_DRAG_EXITED:
//				break;
//				
//			case DragEvent.ACTION_DROP:
//				final int newPosition = ((ListView)v).pointToPosition((int)event.getX(), (int)event.getY());
//				
//				new AlertDialog.Builder(EditPlanActivity.this)
//				.setTitle(R.string.select_block_title)
//				.setItems(R.array.select_block_items, new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						switch (which) {
//						case 0:
//							BlockDateSpot blockDS = new BlockDateSpot();
//							blockDS.setBlockNo(newPosition);
//							insertBlockToPosition(blockDS, newPosition);
//							break;
//
//						case 1:
//							BlockRestaurant blockRS = new BlockRestaurant();
//							blockRS.setBlockNo(newPosition);
//							insertBlockToPosition(blockRS, newPosition);
//							break;
//							
//						case 2:
//							BlockFree blockFR = new BlockFree();
//							blockFR.setBlockNo(newPosition);
//							insertBlockToPosition(blockFR, newPosition);
//							break;
//							
//						case 3:
//							dialog.cancel();
//							break;
//						}
//					}
//				}).show();
//				break;
//			case DragEvent.ACTION_DRAG_ENDED:
//				
//				break;
//			}
//			return true;
//		}
//	};
	
	private OnDragListener BlockDragListener = new OnDragListener() {
		@Override
		public boolean onDrag(View v, DragEvent event) {
			switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				if(!((event.getClipDescription().getLabel().equals("newBlockLongClick")
						|| event.getClipDescription().getLabel().equals("blockLongClick"))
						&& v.getClass().equals(ListView.class))){
					return false;
				}
				currentPosition = ((ListView)v).pointToPosition((int)event.getX(),
						(int)event.getY() - getRelativeTop(v));
				break;

			case DragEvent.ACTION_DRAG_ENTERED:
				//listの色を変更?
				break;
				
			case DragEvent.ACTION_DRAG_EXITED:
				break;
				
			case DragEvent.ACTION_DROP:
				if(event.getClipDescription().getLabel().equals("newBlockLongClick")){
					final int newPosition = ((ListView)v).pointToPosition((int)event.getX(), (int)event.getY());
					
					new AlertDialog.Builder(EditPlanActivity.this)
					.setTitle(R.string.select_block_title)
					.setItems(R.array.select_block_items, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							switch (which) {
							case 0:
								BlockDateSpot blockDS = new BlockDateSpot();
								blockDS.setBlockNo(newPosition);
								insertBlockToPosition(blockDS, newPosition);
								break;

							case 1:
								BlockRestaurant blockRS = new BlockRestaurant();
								blockRS.setBlockNo(newPosition);
								insertBlockToPosition(blockRS, newPosition);
								break;
								
							case 2:
								BlockFree blockFR = new BlockFree();
								blockFR.setBlockNo(newPosition);
								insertBlockToPosition(blockFR, newPosition);
								break;
								
							case 3:
								dialog.cancel();
								break;
							}
						}
					}).show();
				}
				if(event.getClipDescription().getLabel().equals("blockLongClick")){
					newPosition = ((ListView)v).pointToPosition((int)event.getX(), (int)event.getY());
					if(newPosition == -1){
						newPosition = blocks.size() - 1;
					}
					exchangeBlock(currentPosition, newPosition);
				}
				break;
			case DragEvent.ACTION_DRAG_ENDED:
				
				break;
			}
			return true;
		}
	};
//	private OnDragListener blockDragListenerforExchange = new OnDragListener() {
//		@Override
//		public boolean onDrag(View v, DragEvent event) {
//			switch (event.getAction()) {
//			
//			case DragEvent.ACTION_DRAG_STARTED:
//				//ゴミ箱以外を無効にする
//				if(!(event.getClipDescription().getLabel().equals("blockLongClick")
//						&& v.getClass().equals(ListView.class))){
//					return false;
//				}
//				currentPosition = ((ListView)v).pointToPosition((int)event.getX(),
//						(int)event.getY() - getRelativeTop(v));
//				break;
//				
//			case DragEvent.ACTION_DRAG_ENTERED:
////				view.setColorFilter(Color.BLUE);
////				view.invalidate();
//				break;
//				
//			case DragEvent.ACTION_DRAG_EXITED:
////				view.clearColorFilter();
////				view.invalidate();
//				break;
//			case DragEvent.ACTION_DROP:
//				newPosition = ((ListView)v).pointToPosition((int)event.getX(), (int)event.getY());
//				if(newPosition == -1){
//					newPosition = blocks.size() - 1;
//				}
//				exchangeBlock(currentPosition, newPosition);
//				break;
//			case DragEvent.ACTION_DRAG_ENDED:
////				view.clearColorFilter();
//				break;
//			}
//			return true;
//		}
//
//	};
	
	private OnDragListener blockDragListenertoGabageBox = new OnDragListener() {
		@Override
		public boolean onDrag(View v, DragEvent event) {
			switch (event.getAction()) {
			case DragEvent.ACTION_DRAG_STARTED:
				//ゴミ箱以外を無効にする
				if(!(event.getClipDescription().getLabel().equals("blockLongClick") 
						&& v.getClass().equals(ImageButton.class))){
					return false;
				}
				break;
				
			case DragEvent.ACTION_DRAG_ENTERED:
				((ImageButton)v).setColorFilter(Color.BLUE);
				((ImageButton)v).invalidate();
				break;
				
			case DragEvent.ACTION_DRAG_EXITED:
				((ImageButton)v).clearColorFilter();
				((ImageButton)v).invalidate();
				break;
			case DragEvent.ACTION_DROP:
				ClipData.Item item = event.getClipData().getItemAt(0);
				int index = Integer.parseInt(item.getText().toString());
				blocks.remove(index);
				refleshBlockList();
				break;
			case DragEvent.ACTION_DRAG_ENDED:
				((ImageButton)v).clearColorFilter();
				break;
			}
			return true;
		}

	};
	
	private int getRelativeTop(View v) {
	    if (v.getParent() == v.getRootView()){
	        return v.getTop();
	    }else{
	        return v.getTop() + getRelativeTop((View)v.getParent());
	    }
	}
	
	private void refleshBlockList() {
		for (int i = 0; i < blocks.size(); i++) {
			blocks.get(i).setBlockNo(i + 1);
		}
		BlockAdapter adapter = new BlockAdapter(EditPlanActivity.this, blocks);
		blockList.setAdapter(adapter);
	}
	
	private void insertBlockToPosition(Block block, int position){
		if(position == -1){
			blocks.add(block);
		}else{
			Block lastBlock = blocks.getLast();
			for (int i = blocks.size() -1; i > position; i--) {
				blocks.set(i, blocks.get(i - 1));
			}
			blocks.set(position, block);
			blocks.addLast(lastBlock);
		}
		refleshBlockList();
	}
	
	private void exchangeBlock(int currentPosition, int newPosition){
		Block targetBlock = blocks.get(currentPosition);
		if(currentPosition < newPosition){
			for (int i = currentPosition; i < newPosition; i++) {
				blocks.set(i, blocks.get(i + 1));
			}
		}else if(currentPosition > newPosition){
			for (int i = currentPosition; i > newPosition; i--) {
				blocks.set(i, blocks.get(i - 1));
			}
		}
		blocks.set(newPosition, targetBlock);
		refleshBlockList();
	}
}
