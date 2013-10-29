package jap.gr.java_conf.dateroid;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class DiaryEditPageFragment extends Fragment {
	
    public static final String ARG_PAGE = "photo";
    private int mPageNumber;
    private ImageView dotLine;
    
    public static DiaryEditPageFragment create(int pageNumber) {
        DiaryEditPageFragment fragment = new DiaryEditPageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public DiaryEditPageFragment() {
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
                .inflate(R.layout.fragment_diary_edit_photo, container, false);
        
        dotLine = (ImageView)rootView.findViewById(R.id.dotline);
        
        dotLine.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), PhotoListActivity.class);
                intent.putExtra("curPageNo", getPageNumber());
                startActivity(intent);
            }
        });
        return rootView;
    }

    public int getPageNumber() {
        return mPageNumber;
    }
    
    public void setImage(Bitmap bmp){
    	dotLine.setImageBitmap(bmp);
    }
}
