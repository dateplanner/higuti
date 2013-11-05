package com.gr.java_conf.dateroid;

import jap.gr.java_conf.dateroid.R;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class PhotoListActivity extends Activity {

	private static final Uri photoUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	private ArrayList<ArrayList<String>> parentList = new ArrayList<ArrayList<String>>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_list);
		
		ListView photoList = (ListView)findViewById(R.id.photolist);
		
		ContentResolver resolver = getContentResolver();
		Cursor cursor = resolver.query(photoUri, null, null, null, 
				MediaStore.Images.ImageColumns.DATE_TAKEN  + " DESC");

		while (cursor.moveToNext()) {
			ArrayList<String> photoListItem = new ArrayList<String>();
			photoListItem.add(cursor.getString(cursor.getColumnIndex("_display_name")));
			photoListItem.add(cursor.getString(cursor.getColumnIndex("_id")));
			parentList.add(photoListItem);
		}

		PhotoListAdapter adapter = new PhotoListAdapter(this, parentList);
		photoList.setAdapter(adapter);
		
		photoList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Adapter adapter = parent.getAdapter();
				Intent intent = new Intent();
				intent.putExtra("selectedPhoto", adapter.getItemId(position));
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}

	

	
	
}
