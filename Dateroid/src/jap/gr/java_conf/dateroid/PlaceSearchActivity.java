package jap.gr.java_conf.dateroid;

import jap.gr.java_conf.dateroid.PlacesJSON.Types;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;

public class PlaceSearchActivity extends Activity {

	EditText keyword;
	ImageButton search;
	ImageButton toilet;
	ImageButton convenience;
	ImageButton gasstand;
	ImageButton cafe;
	ImageButton outdoor;
	ImageButton indoor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_placesearch);
		
		keyword = (EditText)findViewById(R.id.keyword_edit);
		search = (ImageButton)findViewById(R.id.keyword_btn);
		toilet = (ImageButton)findViewById(R.id.toilet_btn);
		convenience = (ImageButton)findViewById(R.id.convenience_btn);
		gasstand = (ImageButton)findViewById(R.id.gasstand_btn);
		cafe = (ImageButton)findViewById(R.id.cafe_btn);
		outdoor = (ImageButton)findViewById(R.id.outdoor_btn);
		indoor = (ImageButton)findViewById(R.id.indoor_btn);
		
		search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
		
		//ë∂ç›ÇµÇ»Ç¢
		toilet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
		
		convenience.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), PlaceMapActivity.class);
				intent.putExtra("placeType", (Parcelable)Types.CONVENIENCE_STORE);
				startActivity(intent);
			}
		});
		
		gasstand.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), PlaceMapActivity.class);
				intent.putExtra("placeType", (Parcelable)Types.GAS_STATION);
				startActivity(intent);
			}
		});
		
		cafe.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), PlaceMapActivity.class);
				intent.putExtra("placeType", (Parcelable)Types.CAFE);
				startActivity(intent);
			}
		});
		
		outdoor.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), DateSpotListActivity.class);
				intent.putExtra("placeType", 1);
				startActivity(intent);
			}
		});
		
		indoor.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), DateSpotListActivity.class);
				intent.putExtra("placeType", 0);
				startActivity(intent);
			}
		});
	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.placesearch_activity, menu);

        return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case R.id.toTop:
			intent = new Intent(this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;

		case R.id.toGuruNavi:
			intent = new Intent(this, RestaurantFreeSearchActivity.class);
			startActivity(intent);
			break;
		}
		return true;
	}
		
	
}
