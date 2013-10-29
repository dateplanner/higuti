package jap.gr.java_conf.dateroid;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class ConditionPlanFragment extends Fragment {
	
    public static final String ARG_PAGE = "plan";
    private int mPageNumber;
    private TextView title;
    private ImageButton indoor;
    private ImageButton outdoor;
    
    public static ConditionPlanFragment create(int pageNumber) {
        ConditionPlanFragment fragment = new ConditionPlanFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ConditionPlanFragment() {
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
                .inflate(R.layout.fragment_condition_plan, container, false);
        
        title = (TextView)rootView.findViewById(R.id.title_text);
        indoor = (ImageButton)rootView.findViewById(R.id.indoor_btn);
        outdoor = (ImageButton)rootView.findViewById(R.id.outdoor_btn);
       
		title.setText(getText(R.string.plan_title));
		
        
        indoor.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
               //次のフラグメント
            }
        });
        
        outdoor.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 次のフラグメント
			}
		});
        return rootView;
    }

    public int getPageNumber() {
        return mPageNumber;
    }
    
}
