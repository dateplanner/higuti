package jap.gr.java_conf.dateroid;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class EditPlanActivity extends Activity {

	private ListView blockList;
	private ImageButton auto;
	private ImageButton update;
	private ImageButton gabagebox;
	private ImageButton newBlock;
	private ImageButton save;
	private ImageButton doDate;
	
	private DBAdapter dbAdapter;
	private int[] areas;
	private int outdoorFlg;
	private int blocks;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_plan);
		
		blockList = (ListView)findViewById(R.id.block_list);
		auto = (ImageButton)findViewById(R.id.auto_btn);
		update = (ImageButton)findViewById(R.id.update_btn);
		gabagebox = (ImageButton)findViewById(R.id.gabagebox_btn);
		newBlock = (ImageButton)findViewById(R.id.new_btn);
		save = (ImageButton)findViewById(R.id.save_btn);
		doDate = (ImageButton)findViewById(R.id.doDate_btn);
		
		Intent intent = getIntent();
		if(intent == null){
			return;
		}
		
		areas = intent.getIntArrayExtra("areas");
		outdoorFlg = intent.getIntExtra("outdoorFlg", 0);
		blocks = intent.getIntExtra("blocks", 0);
		
		ArrayList<Object> items = new ArrayList<Object>();
		
		dbAdapter = new DBAdapter(this);
		dbAdapter.openReadableDatabase();
		Cursor cursor = dbAdapter.selectAutoDateSpot(areas, outdoorFlg, blocks);
		int blockNo;
		while (cursor.moveToNext()) {
			BlockDateSpot blockDS = new BlockDateSpot();
			blockNo = cursor.getPosition() + 1;
			
			blockDS.setBlockNo(blockNo);
			blockDS.setSpotId(cursor.getInt(cursor.getColumnIndex("_id")));
			blockDS.setSpotName(cursor.getString(cursor.getColumnIndex("datespot_name")));
			blockDS.setSpotAddress(cursor.getString(cursor.getColumnIndex("datespot_address")));

			items.add(blockDS);
			if(blockNo == 2){
				items.add(new BlockRestaurant());
			}
		}
		BlockAdapter adapter = new BlockAdapter(this, items);
		blockList.setAdapter(adapter);
	}

	
	
}
