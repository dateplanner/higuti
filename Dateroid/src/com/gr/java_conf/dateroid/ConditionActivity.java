package com.gr.java_conf.dateroid;

import jap.gr.java_conf.dateroid.R;
import android.R.integer;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

public class ConditionActivity extends FragmentActivity {
	
	//表示するリストの数
    private static final int NUM_CONDITION = 3;

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    
    private boolean outdoor;
    private int[] areas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_condition);

        mPager = (ViewPager) findViewById(R.id.pager);
//        mPager = (SwipeableSwitchViewPager) findViewById(R.id.pager);
//        mPager.switchSwipeable(false);
        
        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
            	//ページング中はオプションメニューを無効化
                invalidateOptionsMenu();
            }
        });
    }

    @Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
//		
//		if(!intent.hasExtra("selectedPhoto") && !intent.hasExtra("curPageNo")){
//			return;
//		}
//		long photoId = intent.getLongExtra("selectedPhoto", 0);
//		int curPageNo = intent.getIntExtra("curPageNo", 0);
//		mPager.setCurrentItem(curPageNo);
//		
//		ContentResolver resolver = getContentResolver();
//		Bitmap bmp = MediaStore.Images.Thumbnails.getThumbnail(resolver, photoId,
//         		MediaStore.Images.Thumbnails.MINI_KIND, null);
//		
//		ViewPager pager = (ViewPager)findViewById(R.id.pager);
//		FragmentStatePagerAdapter sAdapter = (FragmentStatePagerAdapter)pager.getAdapter();
//		DiaryEditPageFragment page = (DiaryEditPageFragment)sAdapter.instantiateItem(pager, pager.getCurrentItem());
//
//		page.setImage(bmp);
	}
    
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
        	Fragment fragment = null;
        	switch (position) {
			case 0:
	            fragment = ConditionCreateFragment.create(position);
	            break;
			case 1:
				fragment = ConditionPlanFragment.create(position);
				break;
			case 2:
				fragment = ConditionAreaFragment.create(position);
				break;
			}
        	return fragment;
        }

        @Override
        public int getCount() {
            return NUM_CONDITION;
        }
    }
}
