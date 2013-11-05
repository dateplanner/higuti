package com.gr.java_conf.dateroid;

import jap.gr.java_conf.dateroid.R;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class RestaurantDetailActivity extends Activity {

	private static String[] xmlAttributes = new String[]{"name", "latitude", "longitude", "url_mobile",
		"shop_image1", "address", "tel", "category_name_l", "budget"}; 
	
	ImageView restaurantPhoto;
	ImageView type;
	ImageButton back;
	ImageButton decide;
	ImageButton favorite;
	ImageButton webSearch;
	ImageButton call;
	TextView name;
	TextView price;
	TextView address;
	
	String restaurantId;
	String imageURL;
	String guruNaviURL;
	String telephoneNo;
	String latitude;
	String longitude;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restaurant_detail);
		
		Intent intent = getIntent();
		if (intent == null) {
			return;
		}
		restaurantId = intent.getStringExtra("restaurantId");
		
		restaurantPhoto = (ImageView)findViewById(R.id.restaurant_image);
		type = (ImageView)findViewById(R.id.type_image);
		back = (ImageButton)findViewById(R.id.back_btn);
		decide = (ImageButton)findViewById(R.id.decide);
		favorite = (ImageButton)findViewById(R.id.favorite_btn);
		webSearch = (ImageButton)findViewById(R.id.websearch_btn);
		call = (ImageButton)findViewById(R.id.call_btn);
		name = (TextView)findViewById(R.id.name);
		price = (TextView)findViewById(R.id.price);
		address = (TextView)findViewById(R.id.address);
		
		
		Uri.Builder builder = new Uri.Builder();
		builder.scheme("http");
		builder.authority(getString(R.string.gurunavi_authority));
		builder.path(getString(R.string.gurunavi_path_restaurant));
		builder.appendQueryParameter("keyid", getString(R.string.gurunavi_key));
		builder.appendQueryParameter("id", restaurantId);
		
		new AsyncTask<String, Void, HashMap<String, String>>() {
			@Override
			protected HashMap<String, String> doInBackground(String... params) {
				try {
					HttpClient client = new DefaultHttpClient();
					HttpResponse response = client.execute(new HttpGet(params[0]));
					InputStream is = new ByteArrayInputStream(EntityUtils.toByteArray(response.getEntity()));
					RestaurantInfoXMLParser info = new RestaurantInfoXMLParser();
					
					return info.xmlParse(is, xmlAttributes).get(0);
				} catch (Exception e) {
				}
				return null;
			}
			
			@Override
			protected void onPostExecute(HashMap<String, String> result) {
				imageURL = result.get("shop_image1");
				guruNaviURL = result.get("url_mobile");
				telephoneNo = "tel:" + result.get("tel");
				latitude = result.get("latitude");
				longitude = result.get("longitude");
				
				name.setText(result.get("name"));
				price.setText(result.get("budget"));
				address.setText(result.get("address"));
			}
		}.execute(builder.build().toString()); 
		
		
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), RestaurantListActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
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
				
			}
		});
		
		webSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse(guruNaviURL);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		});
		
		call.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse(telephoneNo);
				
				Intent intent = new Intent(Intent.ACTION_DIAL, uri);
				startActivity(intent);
			}
		});
		
	}

	@Override
	protected void onStart() {
		super.onStart();
//•Û—¯
//		new AsyncTask<String, Void, Drawable>(){
//	    	@Override
//			protected Drawable doInBackground(String... params) {
//				try {
//					InputStream is = (InputStream) new URL(params[0]).getContent();
//					Drawable d = Drawable.createFromStream(is, "");
//					is.close();
//					Log.v("test", params[0]);
//					return d;
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				return null;
//	    	}
//		
//			@Override
//			protected void onPostExecute(Drawable result) {
//				try {
//					restaurantPhoto.setImageDrawable(result);
//					Log.v("test", "aaa");
//				} catch (Exception e) {
//				}
//			};
//		}.execute(imageURL);
	}
	

	
	
}
