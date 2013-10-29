package jap.gr.java_conf.dateroid;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RestaurantListAdapter extends ArrayAdapter<HashMap<String, String>> {

	private LayoutInflater mInflater;
	private List<HashMap<String, String>> datas;
	
	public RestaurantListAdapter(Context context, List<HashMap<String, String>> objects) {
		super(context, 0, objects);
		
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		datas = objects;
	}
	
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.list_item_restaurant, null);
		}
		
		HashMap<String, String> item = this.getItem(position);
	    if(item != null){
	    	final ImageView shopImageView = (ImageView)convertView.findViewById(R.id.shopImage);
	  	    final TextView shopName = (TextView)convertView.findViewById(R.id.name);
	  	    final TextView shopCategory = (TextView)convertView.findViewById(R.id.categoryName);
	  	    
	  	    new AsyncTask<String, Void, Drawable>(){
	  	    	@Override
	  			protected Drawable doInBackground(String... params) {
	  				try {
	  					InputStream is = (InputStream) new URL(params[0]).getContent();
	  					Drawable d = Drawable.createFromStream(is, "");
	  					is.close();
	  					return d;
	  				} catch (Exception e) {
	  					e.printStackTrace();
	  				}
	  				return null;
	  			}
	  	    	
	  			@Override
	  			protected void onPostExecute(Drawable result) {
	  				try {
	  					shopName.setText(datas.get(position).get("name"));
	  					shopCategory.setText(datas.get(position).get("category_name_l"));
	  					shopImageView.setImageDrawable(result);
	  				} catch (Exception e) {
	  				}
	  			};
	  	    }.execute(datas.get(position).get("shop_image1"));
			
	    }
	    return convertView;
	}

	public String getId(int position) {
		return datas.get(position).get("id");
	}

}
