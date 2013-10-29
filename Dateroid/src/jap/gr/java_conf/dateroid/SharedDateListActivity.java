package jap.gr.java_conf.dateroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class SharedDateListActivity extends Activity {

	private ListView sharedDateList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shared_datelist);
		
		sharedDateList = (ListView)findViewById(R.id.sharedDate_list);
		
		sharedDateList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				//intent plan‰æ–Ê‚Ö
			}
		});
		
	}

	
}
