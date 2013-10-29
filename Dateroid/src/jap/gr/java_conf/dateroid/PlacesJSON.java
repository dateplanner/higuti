package jap.gr.java_conf.dateroid;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;
import android.webkit.JavascriptInterface;

import com.google.gson.Gson;

public class PlacesJSON{

	private geocodeObject location;
	private int radius;
	private ArrayList<String> types;

	public enum Radius{
		THREE_HUNDRED,
		FIVE_HUNDRED,
		ONE_THOUSAND,
		THREE_THOUSAND
	}
	
	public enum Types implements Parcelable{
		BAR,
		CAFE,
		CONVENIENCE_STORE,
		GAS_STATION,
		MOVIE_THEATER,
		PARK,
		PARKING,
		RESTAURANT,;

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeInt(ordinal());
		}
		
		public static final Creator<Types> CREATOR = new Creator<PlacesJSON.Types>() {
			@Override
			public Types createFromParcel(Parcel source) {
				return Types.values()[source.readInt()];
			}

			@Override
			public Types[] newArray(int size) {
				return new Types[size];
			}
		};
	}
	
	public PlacesJSON(){
		location = null;
		radius = 0;
		types = new ArrayList<String>();
	}
	
	public void setLocation(double latitude, double longtitude){
		location = new geocodeObject(latitude, longtitude);
	}
	
	public void setRadius(Radius radius){
		switch (radius) {
		case THREE_HUNDRED:
			this.radius = 300;
			break;
		case FIVE_HUNDRED:
			this.radius = 500;
			break;
		case ONE_THOUSAND:
			this.radius = 1000;
			break;
		case THREE_THOUSAND:
			this.radius = 3000;
			break;
		}
	}
	
	public void setTypes(Types type){
		types.add(type.toString().toLowerCase());
	}
	
	@JavascriptInterface
	public String getJSON(){
		String json = new Gson().toJson(this);
		return json;
	}
	
	private static class geocodeObject{
		private double latitude;
		private double longtitude;
		
		public geocodeObject(double latitude, double longtitude){
			this.latitude = latitude;
			this.longtitude = longtitude;
		}
	}


}

