package com.gr.java_conf.dateroid;

import jap.gr.java_conf.dateroid.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;

public class FavoriteRegisterActivity extends Activity {

	EditText title;
	EditText comment;
	RatingBar rating;
	ImageButton register;
	ImageButton upload;
	
	private DBAdapter dbAdapter;
	private int favoriteId;
//	private String className;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorite_register);
		
		Intent intent = getIntent();
		if(intent == null){
			return;
		}
		favoriteId = intent.getIntExtra("favoriteId", 0);
//		className = getCallingActivity().getClassName();
		
		title = (EditText)findViewById(R.id.title_edit);
		comment = (EditText)findViewById(R.id.comment_edit);
		rating = (RatingBar)findViewById(R.id.rating);
		register = (ImageButton)findViewById(R.id.register_btn);
		upload = (ImageButton)findViewById(R.id.upload_btn);
		
		register.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dbAdapter = new DBAdapter(getApplicationContext());
				dbAdapter.openWritableDatabase();
//				if(className.equals(SpotDetailActivity.class.getName())){
//					dbAdapter.insertFavoriteSpot(favoriteId, title.getText().toString(), 
//							comment.getText().toString(), (double)rating.getRating());
//				}else{
//					dbAdapter.insertFavoriteDatePlan(favoriteId, title.getText().toString(), 
//							comment.getText().toString(), rating.getRating());
//				}
				dbAdapter.insertFavoriteDatePlan(favoriteId, title.getText().toString(), 
						comment.getText().toString(), rating.getRating());
				dbAdapter.close();
			}
		});
		
		upload.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
		
		
	}

}
