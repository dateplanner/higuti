package com.gr.java_conf.dateroid;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class DBAdapter{
	
	static final String DATABASE_NAME = "dateroid.db";
	static final String DATABASE_NAME_ASSET = "dateroid";
	static final int DATABASE_VERSION = 1;	
	
	public static final String TABLE_NAME_1 = "area_large_classification";
	public static final String TABLE_NAME_2 = "area_small_classification";
	public static final String TABLE_NAME_3 = "date_plan";
	public static final String TABLE_NAME_4 = "date_plan_detail";
	public static final String TABLE_NAME_5 = "date_spot";
	public static final String TABLE_NAME_6 = "diary";
	public static final String TABLE_NAME_7 = "diary_photo";
	public static final String TABLE_NAME_8 = "favorite_date_plan";
	public static final String TABLE_NAME_9 = "favorite_restaurant";
	public static final String TABLE_NAME_10 = "favorite_spot";
	public static final String TABLE_NAME_11 = "genre";
	public static final String TABLE_NAME_12 = "spot_genre";
	
	protected DBHelper dbHelper;
	protected SQLiteDatabase db;
	
	private SQLiteStatement stmt;
	
	public DBAdapter(Context context){
		dbHelper = new DBHelper(context);
	}
	
	public DBAdapter openWritableDatabase(){
		db = dbHelper.getWritableDatabase();
		return this;
	}
	
	public DBAdapter openReadableDatabase(){
		db = dbHelper.getReadableDatabase();
		return this;
	}
	
	public void close(){
		dbHelper.close();
	}
	
	/////////////////////////////////////////////////////////SELECT
	public Cursor selectDiary(String diary_date){
		return db.rawQuery("SELECT * FROM diary WHERE diary_date = ?", new String[]{diary_date});
	}
	
	public Cursor selectDiaryPhoto(int diaryId){
		return db.rawQuery("SELECT diary_photo_id FROM diary_photo WHERE diary_id = ? ORDER BY diary_photo_serno",
				new String[]{String.valueOf(diaryId)});
	}
	
	
	public Cursor selectSmallArea(int large_area_id){
		return db.rawQuery("SELECT * FROM area_small_classification " +
					"WHERE area_large_classification_id = ? ORDER BY _id", new String[]{String.valueOf(large_area_id)});
	}
	
	public Cursor selectAutoDateSpot(int areas[], int outdoorFlg, int blocks){
		int length = areas.length;
		ArrayList<String> args = new ArrayList<String>(); 
		for (int i = 0; i < length; i++) {
			args.add(String.valueOf(areas[i]));
		}
		args.add(String.valueOf(outdoorFlg));
		args.add(String.valueOf(blocks));
		
		StringBuilder sb = new StringBuilder();
		sb.append("?");
		for (int i = 1; i < length; i++) {
			sb.append(", ?");
		}
		String sqlString = "SELECT * FROM date_spot WHERE area_small_classification_id IN(" + sb.toString()+ ") " +
				"AND datespot_outdoor_flg = ? order by random() limit ?";
		return db.rawQuery(sqlString, (String[])args.toArray(new String[args.size()]));
	}
	
	public Cursor selectDateSpotById(int placeId){
		return db.rawQuery("SELECT * FROM date_spot WHERE _id = ?", new String[]{String.valueOf(placeId)});
	}
	
	public Cursor selectDateSpotByPlaceType(int placeType){
		return db.rawQuery("SELECT * FROM date_spot WHERE datespot_outdoor_flg = ? ORDER BY datespot_kana",
				new String[]{String.valueOf(placeType)});
	}
	
	
//	public Cursor selectExecutedDate(){
//		//ビューを返すように変更
//}
	
	public Cursor selectFavoriteSpot(){
		return db.rawQuery("SELECT * FROM favorite_spot_view", null);
	}
	
	public Cursor selectFavoritePlan(){
		return db.rawQuery("SELECT * FROM favorite_date_plan ORDER BY _id", null);
	}
	
	/////////////////////////////////////////////////////////INSERT
//	public long insertDiary(String date, String title, String text, int id){
	public long insertDiary(String date, String title, String text){
		long rowId;
//		stmt = db.compileStatement("INSERT INTO diary(diary_date, diary_text, date_plan_id) " +
//				"VALUES(?, ?, ?, ?)");
		stmt = db.compileStatement("INSERT INTO diary(diary_date, diary_title, diary_text) " +
				"VALUES(?, ?, ?)");
		stmt.bindString(1, date);
		if(title != null){
			stmt.bindString(2, title);
		}else{
			stmt.bindNull(2);
		}
		if(text != null){
			stmt.bindString(3, text);
		}else{
			stmt.bindNull(3);
		}
//		stmt.bindLong(4, id);
		db.beginTransaction();
		try {
			rowId = stmt.executeInsert();
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
		return rowId;
	}
	
	public void insertDiaryPhoto(long diary_id, int serno, long photo_id){
		stmt = db.compileStatement("INSERT INTO diary_photo(diary_id, diary_photo_serno" +
						", diary_photo_id) VALUES(?, ?, ?)");
		stmt.bindLong(1, diary_id);
		stmt.bindLong(2, serno);
		stmt.bindLong(3, photo_id);
		db.beginTransaction();
		try {
			stmt.executeInsert();
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
	}
	
	public long insertDatePlan(String date){
		long rowId;
		stmt = db.compileStatement("INSERT INTO date_plan(date_plan_date)" +
				" VALUES(?)");
		stmt.bindString(1, date);
		db.beginTransaction();
		try {
			rowId = stmt.executeInsert();
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
		return rowId;
	}
	
	public void insertPlanSpotDetail(long plan_id, int detail_id, int spot_id, int block_type){
		stmt = db.compileStatement("INSERT INTO date_plan_detail(date_plan_id, date_plan_detail_id" +
						",date_spot_id, block_type) VALUES(?, ?, ?, ?)");
		stmt.bindLong(1, plan_id);
		stmt.bindLong(2, detail_id);
		stmt.bindLong(3, spot_id);
		stmt.bindLong(4, block_type);
		db.beginTransaction();
		try {
			stmt.executeInsert();
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
	}
	
	public void insertPlanRestaurantDetail(long plan_id, int detail_id, String restaurant_id, int block_type){
		stmt = db.compileStatement("INSERT INTO date_plan_detail(date_plan_id, date_plan_detail_id" +
						", restaurant_id, block_type) VALUES(?, ?, ?, ?)");
		stmt.bindLong(1, plan_id);
		stmt.bindLong(2, detail_id);
		if(restaurant_id != null){
			stmt.bindString(3, restaurant_id);
		}else {
			stmt.bindNull(3);
		}
		stmt.bindLong(4, block_type);
		db.beginTransaction();
		try {
			stmt.executeInsert();
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
	}
	
	public void insertPlanFreeDetail(long plan_id, int detail_id, String comment, int block_type){
		stmt = db.compileStatement("INSERT INTO date_plan_detail(date_plan_id, date_plan_detail_id" +
						", date_plan_detail_comment, block_type) VALUES(?, ?, ?, ?)");
		stmt.bindLong(1, plan_id);
		stmt.bindLong(2, detail_id);
		if(comment != null){
			stmt.bindString(3, comment);
		}else {
			stmt.bindNull(3);
		}
		stmt.bindLong(4, block_type);
		db.beginTransaction();
		try {
			stmt.executeInsert();
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
	}
	
	public void insertFavoriteSpot(int spot_id){
		stmt = db.compileStatement("INSERT INTO favorite_spot(favorite_spot_id) VALUES(?)");
		stmt.bindLong(1, spot_id);
		db.beginTransaction();
		try {
			stmt.executeInsert();
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
	}
	
	public void insertFavoriteDatePlan(int plan_id, String title, String comment, double rating){
		stmt = db.compileStatement("INSERT INTO favorite_date_plan(date_plan_id, favorite_date_plan_title" +
						", favorite_date_plan_comment, favorite_date_plan_rating) VALUES(?, ?, ?, ?)");
		stmt.bindLong(1, plan_id);
		if(title != null){
			stmt.bindString(2, title);
		}else {
			stmt.bindNull(2);
		}
		if(comment != null){
			stmt.bindString(3, comment);
		}else {
			stmt.bindNull(3);
		}
		stmt.bindDouble(4, rating);
		try {
			stmt.executeInsert();
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
	}
	
	////////////////////////////////////DELETE
	public void deleteDiary(String date){
		stmt = db.compileStatement("DELETE FROM diary WHERE diary_date = ?");
		stmt.bindString(1, date);
		db.beginTransaction();
		try {
			stmt.executeUpdateDelete();
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
	}
	
	public void deleteDiaryPhoto(int diary_id, int serno){
		stmt = db.compileStatement("DELETE FROM diary_photo WHERE diary_photo_id = ?" +
				" AND diary_photo_serno = ?");
		stmt.bindLong(1, diary_id);
		stmt.bindLong(2, serno);
		db.beginTransaction();
		try {
			stmt.executeUpdateDelete();
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
	}
	
	public void deleteDatePlan(int plan_id){
		stmt = db.compileStatement("DELETE FROM date_plan WHERE _id = ?");
		stmt.bindLong(1, plan_id);
		db.beginTransaction();
		try {
			stmt.executeUpdateDelete();
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
	}
	
	public void deleteDatePlanDetail(int plan_id, int detail_id){
		stmt = db.compileStatement("DELETE FROM date_plan_detail WHERE date_plan_id = ? " +
				"AND date_plan_detail_id = ?");
		stmt.bindLong(1, plan_id);
		stmt.bindLong(2, detail_id);
		db.beginTransaction();
		try {
			stmt.executeUpdateDelete();
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
	}
	
	public void deleteFavoriteSpot(int _id){
		stmt = db.compileStatement("DELETE FROM favorite_spot WHERE _id = ?");
		stmt.bindLong(1, _id);
		db.beginTransaction();
		try {
			stmt.executeUpdateDelete();
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
	}
	
	public void deleteFavoriteDatePlan(int _id){
		stmt = db.compileStatement("DELETE FROM favorite_date_plan WHERE _id = ?");
		stmt.bindLong(1, _id);
		try {
			stmt.executeUpdateDelete();
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
	}
	
	public void createDataBase(){
		try {
			dbHelper.createEmptyDataBase();
		} catch (Exception e) {
		}
	}
	
	private static class DBHelper extends SQLiteOpenHelper{

		private final File dbFilePath; 
		private final Context context;
		
		public DBHelper(Context context){
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			dbFilePath = context.getDatabasePath(DATABASE_NAME);
			this.context = context;
		}
		
		public void createEmptyDataBase() throws IOException{
			boolean dbExist = checkDataBaseExists();
			if(!dbExist){
				getReadableDatabase();
				try{
					copyDataBaseFromAsset();
					
					String path = dbFilePath.getAbsolutePath();
					SQLiteDatabase checkDb = null;
					try{
						checkDb = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
					}catch(SQLiteException e){
						
					}
					if(checkDb != null){
						checkDb.setVersion(DATABASE_VERSION);
						checkDb.close();
					}
				}catch(SQLiteException e){
				}
			}			
		}
		
		private boolean checkDataBaseExists(){
			String path = dbFilePath.getAbsolutePath();

			SQLiteDatabase checkDb = null;
			try{
				checkDb = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
			}catch(SQLiteException e){
			}
			
			if(checkDb == null){
				return false;
			}
			
			if (checkDb.getVersion() == DATABASE_VERSION) {
				checkDb.close();
				return true;
			}
			
			File file = new File(path);
			file.delete();
			return false;
		}
		
		private void copyDataBaseFromAsset() throws IOException{
			InputStream is = context.getAssets().open(DATABASE_NAME_ASSET);
			OutputStream os = new FileOutputStream(dbFilePath);
			
			byte[] buffer = new byte[1024];
			int size;
			while((size = is.read(buffer)) > 0){
				os.write(buffer, 0, size);
			}
			
			os.flush();
			os.close();
			is.close();
		}
		
		public void onCreate(SQLiteDatabase db) {
//			db.beginTransaction();
//			try{
//				//地域小分類テーブル
//				db.execSQL("create table area_small_classification(" +
//						"_id INTEGER PRIMARY KEY AUTOINCREMENT" +
//						", area_large_classification_id INTEGER not null" +
//						", area_small_classification_name TEXT not null" +
//						", foreign key(area_large_classification_id) references area_large_classification(_id) on delete cascade" +
//						")");
//				
//				//地域大分類テーブル
//				db.execSQL("create table area_large_classification(" +
//						"_id INTEGER PRIMARY KEY AUTOINCREMENT" +
//						", area_large_classification_name TEXT not null" +
//						")");
//				
//				//日記テーブル
//				db.execSQL("create table diary(" +
//						"_id INTEGER PRIMARY KEY AUTOINCREMENT" +
//						", diary_date TEXT not null" +
//						", diary_title TEXT" +
//						", diary_text TEXT" +
//						", date_plan_id INTEGER" +
//						", foreign key (date_plan_id) references date_plan(_id) on delete set null" +
//						")");
//				
//				//日記写真テーブル
//				db.execSQL("create table diary_photo(diary_id INTEGER" +
//						", diary_photo_serno INTEGER" +
//						", diary_photo_id INTEGER not null" +
//						", PRIMARY KEY(diary_photo_id, diary_photo_serno)" +
//						", foreign key (diary_id) references diary(_id) on delete cascade" +
//						")");
//				
//				//デートプランテーブル
//				db.execSQL("create table date_plan(" +
//						"_id INTEGER PRIMARY KEY AUTOINCREMENT" +
//						", date_plan_date TEXT" +
//						", date_plan_executed_flg INTEGER default 0" +
//						")");
//				
//				//デートプラン詳細テーブル
//				db.execSQL("create table date_plan_detail(" +
//						"_id INTEGER PRIMARY KEY AUTOINCREMENT" +
//						", date_plan_id INTEGER not null" +
//						", date_plan_detail_id INTEGER not null" +
//						", date_plan_detail_comment TEXT" +
//						", restaurant_id TEXT" +
//						", date_spot_id INTEGER" +
//						", block_type INTEGER" +
//						", foreign key (date_plan_id) references date_plan(_id) on delete cascade" +
//						", foreign key (date_spot_id) references date_spot(_id) on delete set null" +
//						")");
//				
//				//お気に入りスポットテーブル
//				db.execSQL("create table favorite_spot(" +
//						"_id INTEGER PRIMARY KEY AUTOINCREMENT" +
//						", favorite_spot_id INTEGER not null" +
//						", foreign key (favorite_spot_id) references date_spot(_id) on delete cascade" +
//						")");
//				
//				//お気に入りレストランテーブル 廃止？
////				db.execSQL("create table favorite_restaurant(" +
////						"_id INTEGER PRIMARY KEY AUTOINCREMENT" +
////						", restaurant_id TEXT not null" +
////						")");
//				
//				//お気に入りデートプランテーブル
//				db.execSQL("create table favorite_date_plan(_id INTEGER PRIMARY KEY AUTOINCREMENT" +
//						", date_plan_id INTEGER not null" +
//						", favorite_date_plan_title TEXT" +
//						", favorite_date_plan_comment TEXT" +
//						", favorite_date_plan_rating REAL" +
//						", foreign key (date_plan_id) references date_plan(_id) on delete cascade" + 
//						")");
//				
//				//デートスポットテーブル
//				db.execSQL("create table date_spot (" +
//						"_id INTEGER PRIMARY KEY AUTOINCREMENT" +
//						", datespot_name TEXT not null" +
//						", datespot_kana TEXT not null" +
//						", datespot_address TEXT not null" +
//						", datespot_phone_number TEXT" +
//						", datespot_latitude REAL" +
//						", datespot_longitude REAL" +
//						", datespot_outdoor_flg INTEGER default 0 not null" +
//						", datespot_night_scene INTEGER default 0 not null" +
//						", datespot_price INTEGER default 0 not null" +
//						", area_small_classification_id INTEGER not null" +
//						", foreign key (area_small_classification_id) references area_small_classification(_id) on delete set null" +
//						")");
//				
//				//ジャンルテーブル
//				db.execSQL("create table genre (" +
//						"_id INTEGER PRIMARY KEY AUTOINCREMENT" +
//						", genre_name TEXT not null" +
//						")");
//				
//				//スポットジャンルテーブル
//				db.execSQL("create table spot_genre (" +
//						"date_spot_id INTEGER" +
//						", genre_id INTEGER" +
//						", PRIMARY KEY (date_spot_id,genre_id)" +
//						", foreign key (date_spot_id) references date_spot(_id) on delete cascade" +
//						", foreign key (genre_id) references genre(genre_id) on delete cascade)");
//			
//				//お気に入りスポットビュー
//				db.execSQL("create view favorite_spot_view as" +
//						" select favorite_spot.favorite_spot_id as _id, datespot_name as favorite_spot_name" +
//						", datespot_price as favorite_spot_price, datespot_address as favorite_spot_address" +
//						" from favorite_spot left outer join date_spot on favorite_spot.favorite_spot_id = date_spot._id" +
//						" order by favorite_spot._id);
			
//				//実行済デート、エリア小分類名ビュー
//				db.execSQL("create view executed_plan_small_area as select date_plan._id as _id," +
//						" area_small_classification_name from date_plan, date_plan_detail, date_spot," +
//						" area_small_classification where  date_plan._id = date_plan_detail.date_plan_id" +
//						" and date_plan_detail.date_spot_id = date_spot._id" +
//						" and date_spot.area_small_classification_id = area_small_classification._id" +
//						" and date_plan_executed_flg = 1;");
//			}finally{
//				db.endTransaction();
//			}
		}

		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//			//既存のテーブルを破棄
//			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_1);
//			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_2);
//			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_3);
//			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_4);
//			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_5);
//			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_6);
//			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_7);
//			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_8);
////			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_9);
//			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_10);
//			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_11);
//			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_12);
//			onCreate(db);
		}
		
		public void onOpen(SQLiteDatabase db){
			super.onOpen(db);
			if(!db.isReadOnly()){
				db.execSQL("PRAGMA foreign_keys = ON;");
			}
		}
	}

}
