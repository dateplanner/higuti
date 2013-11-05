package com.gr.java_conf.dateroid;

import java.util.ArrayList;


public class BlockDateSpot extends Block{

	private int spotId;
	private String spotName;
	private String spotAddress;
	private boolean pinned;

	ArrayList<Integer> spotGenre;
	
	public BlockDateSpot(){
		spotGenre = new ArrayList<Integer>();
		pinned = false;
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


	public String getAddress() {
		return spotAddress;
	}

	public void setAddress(String address) {
		this.spotAddress = address;
	}
	
	public void setPin(boolean pin){
		pinned = pin;
	}
	
	public boolean isPin(){
		return pinned;
	}
	
	
	
}
