package com.gr.java_conf.dateroid;

import jap.gr.java_conf.dateroid.R;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
public class HistoryActivity extends FragmentActivity {
	
	//表示するリストの数
    private static final int NUM_LISTS = 3;

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mPager = (ViewPager) findViewById(R.id.pager);
        
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

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.history_activity, menu);

//        int titleRes;
//        if(mPager.getCurrentItem() == mPagerAdapter.getCount() -1){
//        	titleRes = R.string.action_finish;
//        }else {
//			titleRes = R.string.action_next;
//		}
//        
//        MenuItem item = menu.add(Menu.NONE, R.id.action_next, Menu.NONE, titleRes);
//        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toFavoriteDate:
            	mPager.setCurrentItem(0);
                return true;

            case R.id.toDateHistory:
                mPager.setCurrentItem(1);
                return true;

            case R.id.toFavoriteDateSpot:
                mPager.setCurrentItem(2);
                return true;
        }
        return super.onOptionsItemSelected(item);
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
	            fragment = HistoryFavoritePlanFragment.create(position);
	            break;
			case 1:
				fragment = HistoryExecutedPlanFragment.create(position);
				break;
			case 2:
				fragment = HistoryFavoriteSpotFragment.create(position);
				break;
			}
            return fragment;
        }

        @Override
        public int getCount() {
            return NUM_LISTS;
        }
    }
}
