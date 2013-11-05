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
				// 問い合わせURLを作成
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
		mCategory.add(new MyObject("RSFST09000", "居酒屋"));
		mCategory.add(new MyObject("RSFST02000", "日本料理・郷土料理"));
		mCategory.add(new MyObject("RSFST03000", "すし・魚料理・シーフード"));
		mCategory.add(new MyObject("RSFST04000", "鍋"));
		mCategory.add(new MyObject("RSFST05000", "焼肉・ホルモン"));
		mCategory.add(new MyObject("RSFST06000", "焼き鳥・肉料理・串料理"));
		mCategory.add(new MyObject("RSFST01000", "和食"));
		mCategory.add(new MyObject("RSFST07000", "お好み焼き・粉物"));
		mCategory.add(new MyObject("RSFST08000", "ラーメン・麺料理"));
		mCategory.add(new MyObject("RSFST14000", "中華"));
		mCategory.add(new MyObject("RSFST11000", "イタリアン・フレンチ"));
		mCategory.add(new MyObject("RSFST13000", "洋食"));
		mCategory.add(new MyObject("RSFST12000", "欧米・各国料理"));
		mCategory.add(new MyObject("RSFST16000", "カレー"));
		mCategory.add(new MyObject("RSFST15000", "アジア・エスニック料理"));
		mCategory.add(new MyObject("RSFST17000", "オーガニック・創作料理"));
		mCategory.add(new MyObject("RSFST10000", "ダイニングバー・バー・ビアホール"));
		mCategory.add(new MyObject("RSFST21000", "お酒"));
		mCategory.add(new MyObject("RSFST18000", "カフェ・スイーツ"));
		mCategory.add(new MyObject("RSFST19000", "宴会・カラオケ・エンターテイメント"));
		mCategory.add(new MyObject("RSFST20000", "ファミレス・ファーストフード"));
		mCategory.add(new MyObject("RSFST90000", "その他の料理"));

		//prefecture
		mPrefecture.add(new MyObject("PREF40", "福岡県"));
	}

}
