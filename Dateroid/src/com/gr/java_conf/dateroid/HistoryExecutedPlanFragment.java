package com.gr.java_conf.dateroid;

import jap.gr.java_conf.dateroid.R;
import android.R.integer;
import android.R.raw;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class HistoryExecutedPlanFragment extends Fragment {
	
    public static final String ARG_PAGE = "executedplan";
    
    private int mPageNumber;
    private DBAdapter dbAdapter;
    
    private TextView title;
    private ListView list;
    
    private static String[] executedFrom = {"favorite_date_plan_title", "favorite_date_plan_comment", "favorite_date_plan_rating"};
    private static int[] executedTo = {R.id.date_text, R.id.area_text};
    
    public static HistoryExecutedPlanFragment create(int pageNumber) {
        HistoryExecutedPlanFragment fragment = new HistoryExecutedPlanFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public HistoryExecutedPlanFragment() {
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
       
        
		title.setText(getText(R.string.dateHistory));
        
		dbAdapter = new DBAdapter(getActivity());
		dbAdapter.openReadableDatabase();
		
		Cursor cursor = dbAdapter.selectFavoritePlan();
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(), R.layout.list_item_executed_date,
				cursor, executedFrom, executedTo);
		list.setAdapter(adapter);
		dbAdapter.close();

        return rootView;
    }

    public int getPageNumber() {
        return mPageNumber;
    }
    
}
