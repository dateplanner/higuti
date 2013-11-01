package jap.gr.java_conf.dateroid;

import jap.gr.java_conf.dateroid.DirectionsJSON.TravelMode;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
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
	private int[] areas;
	private int outdoorFlg;
	private int blocks;
	
	
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
		blocks = intent.getIntExtra("blocks", 0);
		
		ArrayList<Object> items = new ArrayList<Object>();
		
		dbAdapter = new DBAdapter(this);
		dbAdapter.openReadableDatabase();
		Cursor cursor = dbAdapter.selectAutoDateSpot(areas, outdoorFlg, blocks);
		int blockNo;
		while (cursor.moveToNext()) {
			BlockDateSpot blockDS = new BlockDateSpot();
			blockNo = cursor.getPosition() + 1;
			
			blockDS.setBlockNo(blockNo);
			blockDS.setSpotId(cursor.getInt(cursor.getColumnIndex("_id")));
			blockDS.setSpotName(cursor.getString(cursor.getColumnIndex("datespot_name")));
			blockDS.setAddress(cursor.getString(cursor.getColumnIndex("datespot_address")));

			items.add(blockDS);
//			if(blockNo == 2){
//				items.add(new BlockRestaurant());
//			}
		}
		dbAdapter.close();
		BlockAdapter adapter = new BlockAdapter(this, items);
		blockList.setAdapter(adapter);
		
		blockList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
					directionsJSON.setWaypoint(waypointBlock.getAddress());
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

	
	
}
