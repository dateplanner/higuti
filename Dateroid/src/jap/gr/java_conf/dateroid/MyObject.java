package jap.gr.java_conf.dateroid;

import android.os.Parcel;
import android.os.Parcelable;

public class MyObject implements KeyValueItem , Parcelable{

	private int id;
	private String _id;
	private String value;
	
	
	public MyObject(Parcel source){
		this.id = source.readInt();
		this.value = source.readString();
	}
	
	public MyObject(String _id, String value){
		this._id = _id;
		this.value = value;
	}
	
	public MyObject(int id, String value){
		this.id = id;
		this.value = value;
	}
	
	@Override
	public int getIntKey() {
		return id;
	}
	
	@Override
	public String getStringKey() {
		return _id;
	}

	@Override
	public String getStringValue() {
		return value;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(id);
		dest.writeString(value);
	}
	
	public static final Parcelable.Creator<MyObject> CREATOR
			= new Parcelable.Creator<MyObject>() {
		@Override
		public MyObject createFromParcel(Parcel source) {
			return new MyObject(source);
		}

		@Override
		public MyObject[] newArray(int size) {
			return new MyObject[size];
		}
	};
}
