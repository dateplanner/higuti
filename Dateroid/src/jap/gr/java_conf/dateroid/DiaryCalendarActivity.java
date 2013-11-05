package jap.gr.java_conf.dateroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class DiaryCalendarActivity extends Activity {

	ImageButton button;
	ImageButton button1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_diary_calendar);
		
		button = (ImageButton)findViewById(R.id.button);
		button1 = (ImageButton)findViewById(R.id.button1);
		
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), DiaryEditActivity.class);
				intent.putExtra("diaryDate", "20131101");
				startActivity(intent);
			}
		});
		
		button1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), DiaryReadActivity.class);
				intent.putExtra("diaryDate", "20131101");
				startActivity(intent);
			}
		});
		
	}

}
