package jap.gr.java_conf.dateroid;

import java.util.ArrayList;

import android.webkit.JavascriptInterface;

import com.google.gson.Gson;

public class DirectionsJSON {

	String origin;
	String destination;
	String travelMode;
	ArrayList<LocationObject> waypoints;
	
	public enum TravelMode{
		DRIVING,
		WALKING
	}
	
	public DirectionsJSON(){
		origin = null;
		destination = null;
		travelMode = null;
		waypoints = new ArrayList<LocationObject>();
	}
	
	public void setOrigin(String address){
		this.origin = address;
	}
	
	public void setDestination(String address){
		this.destination = address;
	}
	
	public void setTravelMode(TravelMode travelMode){
		this.travelMode = travelMode.toString();
	}
	
	public void setWaypoint(String address){
		waypoints.add(new LocationObject(address));
	}
	
	@JavascriptInterface
	public String getJSON(){
		String json = new Gson().toJson(this);
		return json;
	}
	
	private static class LocationObject{
		private String location;
		
		public LocationObject(String location){
			this.location = location;
		}
	}
}
