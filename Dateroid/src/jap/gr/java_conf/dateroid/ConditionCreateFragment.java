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

public class ConditionCreateFragment extends Fragment {
	
    public static final String ARG_PAGE = "create";
    private int mPageNumber;
    private TextView title;
    private ImageButton auto;
    private ImageButton manual;
    private ImageButton recommend;
    
    public static ConditionCreateFragment create(int pageNumber) {
        ConditionCreateFragment fragment = new ConditionCreateFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ConditionCreateFragment() {
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
                .inflate(R.layout.fragment_condition_create, container, false);
        
        title = (TextView)rootView.findViewById(R.id.title_text);
        auto = (ImageButton)rootView.findViewById(R.id.auto_btn);
        manual = (ImageButton)rootView.findViewById(R.id.manual_btn);
        recommend = (ImageButton)rootView.findViewById(R.id.recommend_btn);
       
		title.setText(getText(R.string.create_title));
		
        
        auto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
               //éüÇÃÉtÉâÉOÉÅÉìÉg
            }
        });
        
        manual.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent();
//              intent.setClass(getActivity(), .class);
//              intent.putExtra("planMode", );
//				startActivity(intent);
			}
		});
        
        recommend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
	            intent.setClass(getActivity(), SharedDateListActivity.class);
				startActivity(intent);
			}
		});
        return rootView;
    }

    public int getPageNumber() {
        return mPageNumber;
    }
    
}
