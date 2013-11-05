package com.gr.java_conf.dateroid;

import jap.gr.java_conf.dateroid.R;

import java.util.ArrayList;
import java.util.HashMap;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class RestaurantNearbySearchActivity extends Activity {

	private ArrayList<MyObject> categoryList = new ArrayList<MyObject>();
	private ArrayList<MyObject> rangeList = new ArrayList<MyObject>();

	Spinner spotSpinner;
	Spinner rangeSpinner;
	Spinner categorySpinner;
	ImageButton search;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_restaurant_nearbysearch);

		Intent intent = getIntent();
		if(intent == null){
			return;
		}
		ArrayList<MyObject> spotList = (ArrayList<MyObject>)intent.getSerializableExtra("spotList");
		
		spotSpinner = (Spinner) findViewById(R.id.spot_spinner);
		rangeSpinner = (Spinner) findViewById(R.id.range_spinner);
		categorySpinner = (Spinner)findViewById(R.id.category_spinner);
		search = (ImageButton)findViewById(R.id.search);
		
		initData();
		
		KeyValueAdapter spotAdapter = new KeyValueAdapter(this, android.R.layout.simple_spinner_item, spotList);
		spotAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spotSpinner.setAdapter(spotAdapter);
		
		KeyValueAdapter rangeAdapter = new KeyValueAdapter(this, android.R.layout.simple_spinner_item, rangeList);
		rangeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		rangeSpinner.setAdapter(rangeAdapter);
		
		KeyValueAdapter categoryAdapter = new KeyValueAdapter(this, android.R.layout.simple_spinner_item, categoryList);
		categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		categorySpinner.setAdapter(categoryAdapter);
		
		search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// �₢���킹URL���쐬
				Uri.Builder builder = new Uri.Builder();
				builder.scheme("http");
				builder.authority(getString(R.string.gurunavi_authority));
				builder.path(getString(R.string.gurunavi_path_restaurant));
				builder.appendQueryParameter("keyid", getString(R.string.gurunavi_key));

				if (categorySpinner.getSelectedItem() != null) {
					int position = categorySpinner.getSelectedItemPosition();
					KeyValueAdapter adapter = (KeyValueAdapter)categorySpinner.getAdapter();
					builder.appendQueryParameter("category_l", adapter.getStringKey(position));
				}
				
				if (spotSpinner.getSelectedItem() != null) {
					int position = spotSpinner.getSelectedItemPosition();
					KeyValueAdapter adapter = (KeyValueAdapter)spotSpinner.getAdapter();
					int spotId = adapter.getIntKey(position);
					
					DBAdapter dbAdapter = new DBAdapter(getApplicationContext());
					dbAdapter.openReadableDatabase();
					Cursor cursor = dbAdapter.selectDateSpotById(spotId);
					cursor.moveToFirst();
					
					builder.appendQueryParameter("latitude", cursor.getString(cursor.getColumnIndex("datespot_latitude")));
					builder.appendQueryParameter("longitude", cursor.getString(cursor.getColumnIndex("datespot_longitude")));
				}
				
				if (rangeSpinner.getSelectedItem() != null) {
					int position = rangeSpinner.getSelectedItemPosition();
					KeyValueAdapter adapter = (KeyValueAdapter)rangeSpinner.getAdapter();
					builder.appendQueryParameter("range", String.valueOf(adapter.getIntKey(position)));
				}
				
				Intent intent = new Intent(getApplicationContext(), RestaurantListActivity.class);
				intent.putExtra("gurunaviUrl", builder.build().toString());
				startActivity(intent);
			}
		});
	}
	
	private void initData(){
		//category
		categoryList.add(new MyObject("RSFST09000", "������"));
		categoryList.add(new MyObject("RSFST02000", "���{�����E���y����"));
		categoryList.add(new MyObject("RSFST03000", "�����E�������E�V�[�t�[�h"));
		categoryList.add(new MyObject("RSFST04000", "��"));
		categoryList.add(new MyObject("RSFST05000", "�ē��E�z������"));
		categoryList.add(new MyObject("RSFST06000", "�Ă����E�������E������"));
		categoryList.add(new MyObject("RSFST01000", "�a�H"));
		categoryList.add(new MyObject("RSFST07000", "���D�ݏĂ��E����"));
		categoryList.add(new MyObject("RSFST08000", "���[�����E�˗���"));
		categoryList.add(new MyObject("RSFST14000", "����"));
		categoryList.add(new MyObject("RSFST11000", "�C�^���A���E�t�����`"));
		categoryList.add(new MyObject("RSFST13000", "�m�H"));
		categoryList.add(new MyObject("RSFST12000", "���āE�e������"));
		categoryList.add(new MyObject("RSFST16000", "�J���["));
		categoryList.add(new MyObject("RSFST15000", "�A�W�A�E�G�X�j�b�N����"));
		categoryList.add(new MyObject("RSFST17000", "�I�[�K�j�b�N�E�n�엿��"));
		categoryList.add(new MyObject("RSFST10000", "�_�C�j���O�o�[�E�o�[�E�r�A�z�[��"));
		categoryList.add(new MyObject("RSFST21000", "����"));
		categoryList.add(new MyObject("RSFST18000", "�J�t�F�E�X�C�[�c"));
		categoryList.add(new MyObject("RSFST19000", "����E�J���I�P�E�G���^�[�e�C�����g"));
		categoryList.add(new MyObject("RSFST20000", "�t�@�~���X�E�t�@�[�X�g�t�[�h"));
		categoryList.add(new MyObject("RSFST90000", "���̑��̗���"));

		rangeList.add(new MyObject(1, "300m"));
		rangeList.add(new MyObject(2, "500m"));
		rangeList.add(new MyObject(3, "1000m"));
		rangeList.add(new MyObject(4, "2000m"));
		rangeList.add(new MyObject(5, "3000m"));
				
	}

}
