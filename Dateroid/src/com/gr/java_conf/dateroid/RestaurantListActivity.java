package com.gr.java_conf.dateroid;

import jap.gr.java_conf.dateroid.R;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class RestaurantListActivity extends Activity {

	ListView restaurantList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restaurantllist);
		
		restaurantList = (ListView)findViewById(R.id.restaurantlist);
		
		Intent intent = getIntent();
		if(intent == null){
			return;
		}
		
		restaurantList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				RestaurantListAdapter adapter = (RestaurantListAdapter)parent.getAdapter();
				Intent intent = new Intent(getApplicationContext(), RestaurantDetailActivity.class);
				intent.putExtra("restaurantId", adapter.getId(position));
				startActivity(intent);
			}
		});
		
		String gurunaviUrl = intent.getStringExtra("gurunaviUrl");
		
		new AsyncTask<String, Void, InputStream>(){
			@Override
			protected InputStream doInBackground(String... params) {
				try{
					HttpClient client = new DefaultHttpClient();
					HttpResponse response = client.execute(new HttpGet(params[0]));
					
					InputStream inputStream = new ByteArrayInputStream(EntityUtils.toByteArray(response.getEntity()));
					return inputStream;
				}catch(Exception e){
					e.printStackTrace();
					return null;
				}
			}
			
			protected void onPostExecute(InputStream result) {
				try {
					RestaurantInfoXMLParser info = new RestaurantInfoXMLParser();
					ArrayList<HashMap<String, String>> a = info.xmlParse(result, 
							new String[]{"id", "name", "shop_image1", "category_name_l"});
					RestaurantListAdapter shopAdapter = new RestaurantListAdapter(getApplicationContext(), a);
				
					restaurantList.setAdapter(shopAdapter);
				} catch (Exception e) {
				}
			}
		}.execute(gurunaviUrl);
	}
}
