package jap.gr.java_conf.dateroid;


abstract class Block {

	private int blockNo;
	
	
	public int getBlockNo(){
		return this.blockNo;
	}
	
	public void setBlockNo(int blockNo){
		this.blockNo = blockNo;
	}
	
	abstract String getAddress();
	
	abstract void setAddress(String address);
	
	abstract void setPin(boolean pin);
	
	abstract boolean isPin();
}
