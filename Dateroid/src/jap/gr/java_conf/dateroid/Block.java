package jap.gr.java_conf.dateroid;

import android.R.integer;



abstract class Block {

	private boolean pinned;
	private int blockNo;
	
	public Block(){
		pinned = false;
	}
	
	public void setPin(boolean pin){
		pinned = pin;
	}
	
	public boolean isPin(){
		return pinned;
	}
	
	public int getBlockNo(){
		return this.blockNo;
	}
	
	public void setBlockNo(int blockNo){
		this.blockNo = blockNo;
	}
	
	abstract String getAddress();
	abstract void setAddress(String address);
	
}
