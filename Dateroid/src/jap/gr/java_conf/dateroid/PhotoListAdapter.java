package jap.gr.java_conf.dateroid;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PhotoListAdapter extends ArrayAdapter<ArrayList<String>> {

	private LayoutInflater mInflater;
	private Context mContext;
	private List<ArrayList<String>> datas;
	
  
	public PhotoListAdapter(Context context, List<ArrayList<String>> objects) {
		super(context, 0, objects);
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mContext = context;
		datas = objects;
	}

	@Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_photo, null);
        }
        
        ArrayList<String> item = this.getItem(position);
        if (item != null) {
            TextView display_name = (TextView)convertView.findViewById(R.id.display_name);
        	ImageView photo = (ImageView)convertView.findViewById(R.id.photo);

            ContentResolver resolver = mContext.getContentResolver();
            Bitmap bmp = MediaStore.Images.Thumbnails.getThumbnail(resolver, Long.parseLong(datas.get(position).get(1)),
            		MediaStore.Images.Thumbnails.MICRO_KIND, null);
            
            display_name.setText(datas.get(position).get(0));
            photo.setImageBitmap(bmp);
        }
        return convertView;
    }
	
	@Override
	public long getItemId(int position) {
		return Long.parseLong(datas.get(position).get(1));
	}

}
