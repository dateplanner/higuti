package com.gr.java_conf.dateroid;

import jap.gr.java_conf.dateroid.R;

import java.util.Arrays;
import java.util.Calendar;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookRequestError;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;

public class DiaryEditActivity extends Activity{

	private static final String OAUTH_VERIFIER = "oauth_verifier";
	
	private ImageButton toHome;
	private ImageButton facebook;
	private ImageButton save;
	private ImageButton tweet;
	private EditText title;
	private EditText diary;
    private TextView date;
    private ImageView photo1;
    private ImageView photo2;
    private ImageView photo3;
    private ImageView photo4;
    
    private DBAdapter dbAdapter;
    private long[] photoIdArray = new long[4];
    private ImageView[] photoImageArray;
    private ImageView curImageView;
    private int diaryId;
    private int curImageNo;
    
    private String callbackURL;
	private Twitter twitter;
	private RequestToken requestToken;
	private ProgressDialog progress;

	private UiLifecycleHelper uiHelper;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_edit);

        Intent intent = getIntent();
        if(intent == null){
        	return;
        }
        
        toHome = (ImageButton)findViewById(R.id.toHome);
        facebook = (ImageButton)findViewById(R.id.facebook);
        save = (ImageButton)findViewById(R.id.save);
        tweet = (ImageButton)findViewById(R.id.tweet);
        title = (EditText)findViewById(R.id.title);
        diary = (EditText)findViewById(R.id.diary);
        date = (TextView)findViewById(R.id.date);
        photo1 = (ImageView)findViewById(R.id.photo1);
        photo2 = (ImageView)findViewById(R.id.photo2);
        photo3 = (ImageView)findViewById(R.id.photo3);
        photo4 = (ImageView)findViewById(R.id.photo4);
        
        String diaryDate = intent.getStringExtra("diaryDate");

        StringBuilder builder = new StringBuilder();
        builder.append(diaryDate.substring(0, 4) + "年");
        builder.append(diaryDate.substring(4, 6) + "月");
        builder.append(diaryDate.substring(6, 8) + "日");
        date.setText(builder.toString());
        
        setDiaryEditView(diaryDate);

        
        toHome.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
        
        uiHelper = new UiLifecycleHelper(this, facebookCallback);
        uiHelper.onCreate(savedInstanceState);
        facebook.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Session session = Session.getActiveSession();
				if(session.isOpened()){
					publishFacebook(diary.getText().toString());
				}else{
					Session.openActiveSession(DiaryEditActivity.this, true, facebookCallback);
				}
			}
		});
        
        callbackURL = getString(R.string.twitter_callback_url);
		twitter = TwitterUtils.getInstance(this);
        tweet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(TwitterUtils.hasAccessToken(getApplicationContext())){
					tweet(diary.getText().toString());
				}else{
					startTwitterOAuthrize();
				}
			}
		});
        
        save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				new AlertDialog.Builder(DiaryEditActivity.this)
				.setTitle(getText(R.string.save_diary_title))
				.setMessage(getText(R.string.save_diary_message))
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dbAdapter = new DBAdapter(getApplicationContext());
						dbAdapter.openWritableDatabase();
						String today = DateFormat.format("yyyyMMdd", Calendar.getInstance()).toString();
						//デートプラントの結合をどうするか要検討
						long rowId = dbAdapter.insertDiary(today, title.getText().toString(),
								diary.getText().toString());
						
						if(rowId == -1){
							//エラー処理
						}else {
							int j = 1;
							for (int i = 0; i < photoIdArray.length; i++) {
								long photoId = photoIdArray[i];
								if(photoId != 0){
									dbAdapter.insertDiaryPhoto(rowId, j, photoId);
									j++;
								}
							}
						}
						dbAdapter.close();
						//カレンダー画面へ戻る（インテントの発行？）
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
        photo1.setOnClickListener(photoClickListener);
        photo2.setOnClickListener(photoClickListener);
        photo3.setOnClickListener(photoClickListener);
        photo4.setOnClickListener(photoClickListener);
    }

    private View.OnClickListener photoClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.photo1:
				curImageNo = 0;
				break;
			case R.id.photo2:
				curImageNo = 1;
				break;
			case R.id.photo3:
				curImageNo = 2;
				break;
			case R.id.photo4:
				curImageNo = 3;
				break;
			}
			curImageView = (ImageView)v;
			Intent intent = new Intent(getApplicationContext(), PhotoListActivity.class);
			startActivityForResult(intent, 0);
		}
	};
	
	@Override
	protected void onResume() {
		super.onResume();
		uiHelper.onResume();
	}

	@Override
	 public void onPause() {
	     super.onPause();
	     uiHelper.onPause();
	 }
	
	 @Override
	protected void onStop() {
		super.onStop();
		uiHelper.onStop();
	}

	@Override
	 public void onDestroy() {
	   super.onDestroy();
	   uiHelper.onDestroy();
	 }
	 
	 @Override
	 protected void onSaveInstanceState(Bundle outState) {
	   super.onSaveInstanceState(outState);
	   uiHelper.onSaveInstanceState(outState);
	 }
	
	@Override
	protected void onNewIntent(Intent intent) {
		if(intent == null || intent.getData() == null 
				|| !intent.getData().toString().startsWith(callbackURL)){
			return;
		}
		String verifier = intent.getData().getQueryParameter(OAUTH_VERIFIER);
		AsyncTask<String, Void, AccessToken> task = new AsyncTask<String, Void, AccessToken>(){
			@Override
			protected AccessToken doInBackground(String... params) {
				try{
					return twitter.getOAuthAccessToken(requestToken, params[0]);
				}catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(AccessToken accessToken) {
				if(accessToken != null){
					Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
					TwitterUtils.storeAccessToken(getApplicationContext(), accessToken);
				}else{
					Toast.makeText(getApplicationContext(), "failure", Toast.LENGTH_SHORT).show();
				}
			}
		};
		task.execute(verifier);
	}
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
//		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
		
		if(resultCode == RESULT_OK && requestCode == 0 && data != null){
			long photoId = data.getLongExtra("selectedPhoto", 0);
			photoIdArray[curImageNo] = photoId;
			ContentResolver resolver = getContentResolver();
			Bitmap bmp = MediaStore.Images.Thumbnails.getThumbnail(resolver, photoId,
	         		MediaStore.Images.Thumbnails.MINI_KIND, null);
			curImageView.setImageBitmap(bmp);
		}
	};
	
	private void setDiaryEditView(String diaryDate){
		dbAdapter = new DBAdapter(this);
		dbAdapter.openReadableDatabase();
		Cursor diaryCursor =  dbAdapter.selectDiary(diaryDate);
		
		if(diaryCursor.getCount() != 0){
			diaryCursor.moveToFirst();
			title.setText(diaryCursor.getString(diaryCursor.getColumnIndex("diary_title")));
			diary.setText(diaryCursor.getString(diaryCursor.getColumnIndex("diary_text")));
			diaryId = diaryCursor.getInt(diaryCursor.getColumnIndex("_id"));
			
			Cursor photoCursor = dbAdapter.selectDiaryPhoto(diaryId);
			photoImageArray = new ImageView[]{photo1, photo2, photo3, photo4};
			
			int length = photoCursor.getCount();
	        for (int i = 0; i < length; i++) {
	        	photoCursor.moveToNext();
				photoIdArray[i] = photoCursor.getLong(photoCursor.getColumnIndex("diary_photo_id"));
			}
	        
			ContentResolver resolver = getContentResolver();
	        for (int i = 0; i < length; i++) {
				Bitmap bmp = MediaStore.Images.Thumbnails.getThumbnail(resolver, photoIdArray[i],
		         		MediaStore.Images.Thumbnails.MINI_KIND, null);
	            photoImageArray[i].setImageBitmap(bmp);
			}
		}
		dbAdapter.close();
	};

	private void startTwitterOAuthrize(){
		AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>(){
			
			@Override
			protected void onPreExecute() {
				progress = new ProgressDialog(DiaryEditActivity.this);
				progress.setMessage("wait...");
				progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progress.show();
			}
			
			@Override
			protected String doInBackground(Void... params) {
				try{
					requestToken = twitter.getOAuthRequestToken(callbackURL);
					return requestToken.getAuthorizationURL();
				}catch (Exception e) {
					Log.v("test", e.getMessage());
				}
				return null;
			}

			@Override
			protected void onPostExecute(String authURL) {
				if(authURL != null){
					startActivityForResult(new Intent(Intent.ACTION_VIEW, Uri.parse(authURL)), 0);
				}
				progress.dismiss();
			}
		};
		task.execute();
	}
	
	private void tweet(String tweetString){
		AsyncTask<String, Void, Boolean> task = new AsyncTask<String, Void, Boolean>(){
			@Override
			protected void onPreExecute() {
				progress = new ProgressDialog(DiaryEditActivity.this);
				progress.setMessage("wait...");
				progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				progress.show();
			}
			
			@Override
			protected Boolean doInBackground(String... params) {
				try{
					twitter.updateStatus(params[0]);
					return true;
				}catch (TwitterException e) {
					//status duplicated
					e.printStackTrace();
					return false;
				}
			}
			
			@Override
			protected void onPostExecute(Boolean result) {
				if(result){
					Toast.makeText(getApplicationContext(), "Tweet 成功", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(getApplicationContext(), "Tweet失敗", Toast.LENGTH_SHORT).show();
				}
				progress.dismiss();
			}
		};
		task.execute(tweetString);
	}
	
	private void publishFacebook(String publishString){
		Session session = Session.getActiveSession();
		
		if(session != null){
			if(!session.getPermissions().contains("publish_actions")){
				Session.NewPermissionsRequest request = 
						new Session.NewPermissionsRequest(DiaryEditActivity.this, Arrays.asList("publish_actions"));
				session.requestNewPublishPermissions(request);
				return;
			}
			
			Request.Callback callback = new Request.Callback() {
				@Override
				public void onCompleted(Response response) {
					FacebookRequestError error = response.getError();
					if(error != null){
						Toast.makeText(getApplicationContext(), "投稿成功", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(getApplicationContext(), "投稿失敗", Toast.LENGTH_SHORT).show();
					}
				}
			};
			
			Request request = Request.newStatusUpdateRequest(session, publishString, callback);
			RequestAsyncTask task = new RequestAsyncTask(request);
			task.execute();
		}
	}
	
	
	private Session.StatusCallback facebookCallback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception){
		if(session.isOpened()){
		}
	}

}

