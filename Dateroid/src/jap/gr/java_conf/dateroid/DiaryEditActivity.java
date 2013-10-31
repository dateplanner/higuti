package jap.gr.java_conf.dateroid;

import java.sql.RowId;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.R.integer;
import android.R.string;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    private ImageView curImageView;
    private int  diaryId;
    private int curImageNo;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_edit);

        Intent intent = getIntent();
        if(intent == null){
        	return;
        }
        diaryId = intent.getIntExtra("diaryId", 0);
        
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
        
        date.setText(DateFormat.format("yyyy年MM月dd日", Calendar.getInstance()));

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
				dbAdapter = new DBAdapter(getApplicationContext());
				dbAdapter.openWritableDatabase();
				if (diaryId == 0) {
					String today = DateFormat.format("yyyyMMdd", Calendar.getInstance()).toString();
					//デートプラントの結合をどうするか要検討
					long rowId = dbAdapter.insertDiary(today, title.getText().toString(),
							diary.getText().toString());
					if(rowId == -1){
						//エラー処理
					}
					int j = 1;
					for (int i = 0; i < photoIdArray.length; i++) {
						long photoId = photoIdArray[i];
						if(photoId != 0){
							dbAdapter.insertDiaryPhoto(rowId, j, photoId);
						}
						j++;
					}
				}else{
					
				}
			}
		});
        
        save.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
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

}
