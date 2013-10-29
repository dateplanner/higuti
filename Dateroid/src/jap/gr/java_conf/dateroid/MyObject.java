package jap.gr.java_conf.dateroid;


public class MyObject implements KeyValueItem {

	private String _id;
	private String value;
	
	public MyObject(){
		
	}
	
	public MyObject(String _id, String value){
		this._id = _id;
		this.value = value;
	}
	
	public String getId(){
		return _id;
	}
	
	public void setId(String _id){
		this._id = _id;
	}
	
	public String getValue(){
		return value;
	}
	
	public void setValue(String value){
		this.value = value;
	}
	
	@Override
	public String getOptionKey() {
		return _id;
	}

	@Override
	public String getOptionValue() {
		return value;
	}

}
