package jap.gr.java_conf.dateroid;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class HistoryFavoriteSpotFragment extends Fragment {
	
    public static final String ARG_PAGE = "favoritespot";
    
    private int mPageNumber;
    private DBAdapter dbAdapter;
    
    private TextView title;
    private ListView list;
    
    private static String[] spotFrom = {"favorite_spot_name", "favorite_spot_price", "favorite_spot_address"};
    private static int[] spotTo = {R.id.place_text, R.id.price_text, R.id.address_text};
    
    public static HistoryFavoriteSpotFragment create(int pageNumber) {
        HistoryFavoriteSpotFragment fragment = new HistoryFavoriteSpotFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public HistoryFavoriteSpotFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
    }
    

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_history_list, container, false);
        
        title = (TextView)rootView.findViewById(R.id.title_text);
        list = (ListView)rootView.findViewById(R.id.history_list);
       
        
		title.setText(getText(R.string.favoriteDateSpot));
        
		dbAdapter = new DBAdapter(getActivity());
		dbAdapter.openReadableDatabase();
		
		Cursor cursor = dbAdapter.selectFavoriteSpot();
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(), R.layout.list_item_favorite_spot,
					cursor, spotFrom, spotTo);
	
		list.setAdapter(adapter);
		dbAdapter.close();

		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(getActivity(), SpotDetailActivity.class);
				intent.putExtra("spotId", (int)id);
				startActivityForResult(intent, 0);
			}
		});
		
        return rootView;
    }

    public int getPageNumber() {
        return mPageNumber;
    }
    
}
