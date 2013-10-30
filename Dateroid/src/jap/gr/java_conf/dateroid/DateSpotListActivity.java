package jap.gr.java_conf.dateroid;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class DateSpotListActivity extends Activity {

	private ListView spotList;
	
	private int placeType;
	private DBAdapter dbAdapter;
	
	private String[] dateSpotFrom = {"datespot_name", "datespot_price", "datespot_address"};
	private int[] dateSpotTo = {R.id.spotname_text, R.id.price_text, R.id.address_text};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_spot_list);
		
		spotList = (ListView)findViewById(R.id.spotlist);
		
		Intent intent = getIntent();
		if(intent == null){
			return;
		}
		placeType = intent.getIntExtra("placeType", 0);
		
		dbAdapter = new DBAdapter(this);
		dbAdapter.openReadableDatabase();
		Cursor cursor = dbAdapter.selectDateSpotByPlaceType(placeType);
		
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.list_item_datespot,
				cursor, dateSpotFrom, dateSpotTo);
		spotList.setAdapter(adapter);
		
		spotList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(getApplicationContext(), SpotDetailActivity.class);
				intent.putExtra("spotId", (int)id);
				startActivity(intent);
			}
		});
	}

}
