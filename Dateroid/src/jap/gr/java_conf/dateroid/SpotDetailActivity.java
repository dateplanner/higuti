package jap.gr.java_conf.dateroid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SpotDetailActivity extends Activity {

	private ImageView spotImage;
	private ImageView type;
	private ImageButton back;
	private ImageButton decide;
	private ImageButton favorite;
	private ImageButton webSearch;
	private ImageButton call;
	private TextView name;
	private TextView price;
	private TextView address;
	
	private int spotId;
	private DBAdapter dbAdapter;
	private String telephoneNo;
	private String latitude;
	private String longitude;
	private String className;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_spot_detail);
		
		Intent intent = getIntent();
		if(intent == null){
			return;
		}
		spotId = intent.getIntExtra("spotId", 0);
		className = getCallingActivity().getClassName();
		
		spotImage = (ImageView)findViewById(R.id.spot_image);
		type = (ImageView)findViewById(R.id.type_image);
		back = (ImageButton)findViewById(R.id.back_btn);
		decide = (ImageButton)findViewById(R.id.decide);
		favorite = (ImageButton)findViewById(R.id.favorite_btn);
		webSearch = (ImageButton)findViewById(R.id.websearch_btn);
		call = (ImageButton)findViewById(R.id.call_btn);
		name = (TextView)findViewById(R.id.name_text);
		price = (TextView)findViewById(R.id.price_text);
		address = (TextView)findViewById(R.id.address_text);
		
		if(className.equals(HistoryActivity.class.getName())){
			favorite.setVisibility(View.INVISIBLE);
		}; 
				
		
		dbAdapter = new DBAdapter(this);
		dbAdapter.openReadableDatabase();
		Cursor cursor = dbAdapter.selectDateSpotById(spotId);
		cursor.moveToFirst();
		
		name.setText(cursor.getString(cursor.getColumnIndex("datespot_name")));
		price.setText(cursor.getString(cursor.getColumnIndex("datespot_price")));
		address.setText(cursor.getString(cursor.getColumnIndex("datespot_address")));
		telephoneNo = cursor.getString(cursor.getColumnIndex("datespot_phone_number"));
		latitude = cursor.getString(cursor.getColumnIndex("datespot_latitude"));
		longitude = cursor.getString(cursor.getColumnIndex("datespot_longitude"));
		
		dbAdapter.close();
		

		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(getApplicationContext(), RestaurantListActivity.class);
//				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(intent);
			}
		});
		
		decide.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse("geo:" + latitude + "," + longitude + 
						"?q=" + latitude + "," + longitude + "(" + name.getText() + ")");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		});
		
		favorite.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(SpotDetailActivity.this)
				.setTitle(getText(R.string.save_favorite_spot_title))
				.setMessage(getText(R.string.save_favorite_spot_message))
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dbAdapter = new DBAdapter(getApplicationContext());
						dbAdapter.openWritableDatabase();
						dbAdapter.insertFavoriteSpot(spotId);
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
		
		webSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
				intent.putExtra(SearchManager.QUERY, name.getText());
				startActivity(intent);
			}
		});
		
		call.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!telephoneNo.isEmpty()) {
					telephoneNo = "tel:" + telephoneNo;
					Uri uri = Uri.parse(telephoneNo);
					Intent intent = new Intent(Intent.ACTION_DIAL, uri);
					startActivity(intent);
				}else {
					Toast.makeText(getApplicationContext(), getText(R.string.noTel), Toast.LENGTH_SHORT).show();
				}
			}
		});
		
	}

	
}
