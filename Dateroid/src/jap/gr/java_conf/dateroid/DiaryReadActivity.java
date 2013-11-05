package jap.gr.java_conf.dateroid;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DiaryReadActivity extends FragmentActivity {
	
	private LinearLayout scrollLinear;
	private ImageButton toPrevious;
	private ImageButton edit;
	private ImageButton toHome;
	private ImageButton toNext;
	private TextView title;
    private TextView date;
	private TextView diary;
    
    private DBAdapter dbAdapter;
    private long[] photoIdArray;
    private String diaryDate;
    private int diaryId;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_read);

        Intent intent = getIntent();
        if(intent == null){
        	return;
        }
        diaryDate = intent.getStringExtra("diaryDate");
        
        scrollLinear = (LinearLayout)findViewById(R.id.scrollLinear);
        toPrevious = (ImageButton)findViewById(R.id.toPrevious);
        toNext = (ImageButton)findViewById(R.id.toNext);
        edit = (ImageButton)findViewById(R.id.edit);
        toHome = (ImageButton)findViewById(R.id.toHome);
        title = (TextView)findViewById(R.id.title);
        date = (TextView)findViewById(R.id.date);
        diary = (TextView)findViewById(R.id.diary);

        dbAdapter = new DBAdapter(this);
        dbAdapter.openReadableDatabase();
        
        Cursor diaryCursor = dbAdapter.selectDiary(diaryDate);
        diaryCursor.moveToFirst();
        
        StringBuilder builder = new StringBuilder();
        builder.append(diaryDate.substring(0, 4) + "”N");
        builder.append(diaryDate.substring(4, 6) + "ŒŽ");
        builder.append(diaryDate.substring(6, 8) + "“ú");
        date.setText(builder.toString());

        title.setText(diaryCursor.getString(diaryCursor.getColumnIndex("diary_title")));

        diary.setText(diaryCursor.getString(diaryCursor.getColumnIndex("diary_text")));
        diaryId = diaryCursor.getInt(diaryCursor.getColumnIndex("_id"));
        
        Cursor photoCursor = dbAdapter.selectDiaryPhoto(diaryId);
        
        int length = photoCursor.getCount();
        photoIdArray = new long[length];
        for (int i = 0; i < length; i++) {
        	photoCursor.moveToNext();
			photoIdArray[i] = photoCursor.getLong(photoCursor.getColumnIndex("diary_photo_id"));
		}
        
		ContentResolver resolver = getContentResolver();
        for (int i = 0; i < length; i++) {
            ImageView photo = new ImageView(this);
			Bitmap bmp = MediaStore.Images.Thumbnails.getThumbnail(resolver, photoIdArray[i],
	         		MediaStore.Images.Thumbnails.MINI_KIND, null);
            photo.setImageBitmap(bmp);
            
        	scrollLinear.addView(photo, new LinearLayout.LayoutParams(
        			LinearLayout.LayoutParams.MATCH_PARENT,
        			LinearLayout.LayoutParams.MATCH_PARENT));
		}
        
        dbAdapter.close();
        
        toPrevious.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(intent);
			}
		});
        
        toNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			
			}
		});
        
        edit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), DiaryEditActivity.class);
				intent.putExtra("diaryDate", diaryDate);
				startActivity(intent);
			}
		});
        
        toHome.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
        
    }

    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
//		if(resultCode == RESULT_OK && data != null){
//			long photoId = data.getLongExtra("selectedPhoto", 0);
//			photoIdArray[curImageNo] = photoId;
//			ContentResolver resolver = getContentResolver();
//			Bitmap bmp = MediaStore.Images.Thumbnails.getThumbnail(resolver, photoId,
//	         		MediaStore.Images.Thumbnails.MINI_KIND, null);
//			curImageView.setImageBitmap(bmp);
//		}
	};

}
