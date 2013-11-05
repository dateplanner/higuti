package jap.gr.java_conf.dateroid;

import jap.gr.java_conf.dateroid.DirectionsJSON.TravelMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerOpenListener;

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
	private ArrayList<Block> blocks;
	private int[] areas;
	private int outdoorFlg;
	private int blockCounts;
	
	
	@Override
	@SuppressWarnings("deprecation")
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
		
		blocks = new ArrayList<Block>();
		
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
		
		blockList.setOnItemClickListener(blockListItemClickListener);

		
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
	
	private OnItemClickListener blockListItemClickListener = new OnItemClickListener() {
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
						if(block instanceof BlockDateSpot){
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

	
	
}
