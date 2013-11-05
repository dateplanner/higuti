package jap.gr.java_conf.dateroid;

import java.util.Calendar;

import android.R.integer;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DiaryEditActivity extends FragmentActivity {

	private ImageButton toHome;
	private ImageButton edit;
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
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_edit);

        Intent intent = getIntent();
        if(intent == null){
        	return;
        }
        
        toHome = (ImageButton)findViewById(R.id.toHome);
        edit = (ImageButton)findViewById(R.id.edit);
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
        
        edit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
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
        
        tweet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK && data != null){
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

}
