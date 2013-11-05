package jap.gr.java_conf.dateroid;


public class BlockFree extends Block{

	String comment;
	
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	String getAddress() {
		return null;
	}

	@Override
	void setAddress(String address) {
	}

	@Override
	void setPin(boolean pin) {
	}

	@Override
	boolean isPin() {
		return true;
	}

}
