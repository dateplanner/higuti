package jap.gr.java_conf.dateroid;


public class BlockRestaurant extends Block{

	String restaurantId;
	String address;
	
	public String getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(String restaurantId) {
		this.restaurantId = restaurantId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	void setPin(boolean pin) {
	}

	@Override
	boolean isPin() {
		return true;
	}
	
}
