package jap.gr.java_conf.dateroid;

import android.R.integer;
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

public class ConditionAreaFragment extends Fragment {
	
    public static final String ARG_PAGE = "area";
    
    private int mPageNumber;
    private DBAdapter dbAdapter;
    
    private TextView title;
    private ListView list;
    private ImageButton makePlan;
    
    private static String[] areaFrom = {"area_small_classification_name"};
    private static int[] areaTo = {R.id.smallArea_text};
    
    public static ConditionAreaFragment create(int pageNumber) {
        ConditionAreaFragment fragment = new ConditionAreaFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ConditionAreaFragment() {
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
                .inflate(R.layout.fragment_condition_area, container, false);
        
        title = (TextView)rootView.findViewById(R.id.title_text);
        list = (ListView)rootView.findViewById(R.id.area_list);
        makePlan = (ImageButton)rootView.findViewById(R.id.create_btn);
       
        
		title.setText(getText(R.string.area_title));
        
		dbAdapter = new DBAdapter(getActivity());
		dbAdapter.openReadableDatabase();
		
		//40=•Ÿ‰ª
		Cursor cursor = dbAdapter.selectSmallArea(40);
		SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(), R.layout.list_item_condition_area,
				cursor, areaFrom, areaTo);
		list.setAdapter(adapter);
		
        makePlan.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
              Intent intent = new Intent();
              intent.setClass(getActivity(), EditPlanActivity.class);
              intent.putExtra("areas", new int[]{1, 7});
              intent.putExtra("outdoorFlg", 0);
              intent.putExtra("blocks", 4);
              startActivity(intent);      
			}
        });
        return rootView;
    }

    public int getPageNumber() {
        return mPageNumber;
    }
    
}
