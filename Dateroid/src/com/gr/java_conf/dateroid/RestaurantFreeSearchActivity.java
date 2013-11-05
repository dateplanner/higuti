package com.gr.java_conf.dateroid;

import jap.gr.java_conf.dateroid.R;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Spinner;

public class RestaurantFreeSearchActivity extends Activity {

	private ArrayList<MyObject> mCategory = new ArrayList<MyObject>();
	private ArrayList<MyObject> mPrefecture = new ArrayList<MyObject>();

	Spinner category;
	Spinner prefecture;
	ImageButton search;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mealsearch);

		category = (Spinner) findViewById(R.id.category_spinner);
		prefecture = (Spinner) findViewById(R.id.prefecture_spinner);
		search = (ImageButton)findViewById(R.id.search_btn);

		initData();
		
		KeyValueAdapter categoryAdapter = new KeyValueAdapter(this, android.R.layout.simple_spinner_item, mCategory);
		categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		category.setAdapter(categoryAdapter);
		
		KeyValueAdapter prefectureAdapter = new KeyValueAdapter(this, android.R.layout.simple_spinner_item, mPrefecture);
		prefectureAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		prefecture.setAdapter(prefectureAdapter);
		
		search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// �₢���킹URL���쐬
				Uri.Builder builder = new Uri.Builder();
				builder.scheme("http");
				builder.authority(getString(R.string.gurunavi_authority));
				builder.path(getString(R.string.gurunavi_path_restaurant));
				builder.appendQueryParameter("keyid", getString(R.string.gurunavi_key));

				
				if (category.getSelectedItem() != null) {
					int position = category.getSelectedItemPosition();
					KeyValueAdapter adapter = (KeyValueAdapter)category.getAdapter();
					builder.appendQueryParameter("category_l", adapter.getStringKey(position));
				}

				if (prefecture.getSelectedItem() != null) {
					int position = prefecture.getSelectedItemPosition();
					KeyValueAdapter adapter = (KeyValueAdapter)prefecture.getAdapter();
					builder.appendQueryParameter("pref", adapter.getStringKey(position));
				}
				
				Intent intent = new Intent(getApplicationContext(), RestaurantListActivity.class);
				intent.putExtra("gurunaviUrl", builder.build().toString());
				startActivity(intent);
			}
		});
	}
	
	private void initData(){
		//category
		mCategory.add(new MyObject("RSFST09000", "������"));
		mCategory.add(new MyObject("RSFST02000", "���{�����E���y����"));
		mCategory.add(new MyObject("RSFST03000", "�����E�������E�V�[�t�[�h"));
		mCategory.add(new MyObject("RSFST04000", "��"));
		mCategory.add(new MyObject("RSFST05000", "�ē��E�z������"));
		mCategory.add(new MyObject("RSFST06000", "�Ă����E�������E������"));
		mCategory.add(new MyObject("RSFST01000", "�a�H"));
		mCategory.add(new MyObject("RSFST07000", "���D�ݏĂ��E����"));
		mCategory.add(new MyObject("RSFST08000", "���[�����E�˗���"));
		mCategory.add(new MyObject("RSFST14000", "����"));
		mCategory.add(new MyObject("RSFST11000", "�C�^���A���E�t�����`"));
		mCategory.add(new MyObject("RSFST13000", "�m�H"));
		mCategory.add(new MyObject("RSFST12000", "���āE�e������"));
		mCategory.add(new MyObject("RSFST16000", "�J���["));
		mCategory.add(new MyObject("RSFST15000", "�A�W�A�E�G�X�j�b�N����"));
		mCategory.add(new MyObject("RSFST17000", "�I�[�K�j�b�N�E�n�엿��"));
		mCategory.add(new MyObject("RSFST10000", "�_�C�j���O�o�[�E�o�[�E�r�A�z�[��"));
		mCategory.add(new MyObject("RSFST21000", "����"));
		mCategory.add(new MyObject("RSFST18000", "�J�t�F�E�X�C�[�c"));
		mCategory.add(new MyObject("RSFST19000", "����E�J���I�P�E�G���^�[�e�C�����g"));
		mCategory.add(new MyObject("RSFST20000", "�t�@�~���X�E�t�@�[�X�g�t�[�h"));
		mCategory.add(new MyObject("RSFST90000", "���̑��̗���"));

		//prefecture
		mPrefecture.add(new MyObject("PREF40", "������"));
	}

}
