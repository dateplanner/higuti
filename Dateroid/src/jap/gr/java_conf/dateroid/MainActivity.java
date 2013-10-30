package jap.gr.java_conf.dateroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class MainActivity extends Activity {

	ImageButton plan;
	ImageButton search;
	ImageButton history;
	ImageButton diary;
	
	DBAdapter dbAdapter;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setDatabase();
        
        plan = (ImageButton)findViewById(R.id.plan_main);
        search = (ImageButton)findViewById(R.id.search_main);
        history = (ImageButton)findViewById(R.id.history_main);
        diary = (ImageButton)findViewById(R.id.diary_main);
        
        plan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), ConditionActivity.class);
				startActivity(intent);
			}
		});
        
        search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), PlaceSearchActivity.class);
				startActivity(intent);
			}
		});
        
        history.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
				startActivity(intent);
			}
		});
        
        diary.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), DiaryCalendarActivity.class);
				startActivity(intent);
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    private void setDatabase(){
    	dbAdapter = new DBAdapter(this);
    	dbAdapter.createDataBase();
    }
    
}
