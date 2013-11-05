package com.gr.java_conf.dateroid;

import jap.gr.java_conf.dateroid.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class HistoryPageFragment extends Fragment {
	
    public static final String ARG_PAGE = "list";
    private int mPageNumber;
    private ListView list;
    private TextView title;
    
    public static HistoryPageFragment create(int pageNumber) {
        HistoryPageFragment fragment = new HistoryPageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public HistoryPageFragment() {
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
        
        int curPageNo = getPageNumber();
        if(curPageNo == 0){
        	title.setText(getText(R.string.favoriteDatePlan));
        }else if(curPageNo == 1){
        	title.setText(getText(R.string.dateHistory));
        }else {
			title.setText(getText(R.string.favoriteDateSpot));
		}
        
//        dotLine.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.setClass(getActivity(), PhotoListActivity.class);
//                intent.putExtra("curPageNo", getPageNumber());
//                startActivity(intent);
//            }
//        });
        return rootView;
    }

    public int getPageNumber() {
        return mPageNumber;
    }
    
}
