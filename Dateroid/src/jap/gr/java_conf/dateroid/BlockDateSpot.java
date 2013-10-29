package jap.gr.java_conf.dateroid;

import java.util.ArrayList;


public class BlockDateSpot extends Block{

	int spotId;
	String spotName;
	String spotAddress;
	ArrayList<Integer> spotGenre;
	
	public BlockDateSpot(){
		spotGenre = new ArrayList<Integer>();
	}
	
	
	public int getSpotId() {
		return spotId;
	}


	public void setSpotId(int spotId) {
		this.spotId = spotId;
	}


	public String getSpotName() {
		return spotName;
	}


	public void setSpotName(String spotName) {
		this.spotName = spotName;
	}


	public String getSpotAddress() {
		return spotAddress;
	}


	public void setSpotAddress(String spotAddress) {
		this.spotAddress = spotAddress;
	}


	
	
	
}
