package jap.gr.java_conf.dateroid;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
public class DiaryEditActivity extends FragmentActivity {
	
	//登録できる写真の数
    private static final int NUM_PHOTOS = 4;

    private TextView today;
    private ImageView photo1;
    private ImageView photo2;
    private ImageView photo3;
    private ImageView photo4;
    
    private PagerAdapter mPagerAdapter;

    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_edit);

        today = (TextView)findViewById(R.id.date);
        photo1 = (ImageView)findViewById(R.id.photo1);
        photo2 = (ImageView)findViewById(R.id.photo2);
        photo3 = (ImageView)findViewById(R.id.photo3);
        photo4 = (ImageView)findViewById(R.id.photo4);
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日");
        Date date = new Date();
        today.setText(sdf.format(date));

        photo1.setOnClickListener(photoClickListener);
        photo2.setOnClickListener(photoClickListener);
        photo3.setOnClickListener(photoClickListener);
        photo4.setOnClickListener(photoClickListener);
        
    }

    private View.OnClickListener photoClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int curPageNo = 0;
			switch (v.getId()) {
			case R.id.photo1:
				curPageNo = R.id.photo1;
				break;
			case R.id.photo2:
				curPageNo = R.id.photo2;
				break;
			case R.id.photo3:
				curPageNo = R.id.photo3;
				break;
			case R.id.photo4:
				curPageNo = R.id.photo4;
				break;
			}
			Intent intent = new Intent(getApplicationContext(), PhotoListActivity.class);
			intent.putExtra("curPageNo", curPageNo);
			startActivity(intent);
		}
	};
    
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
//        getMenuInflater().inflate(R.menu.activity_screen_slide, menu);
//
//        menu.findItem(R.id.action_previous).setEnabled(mPager.getCurrentItem() > 0);
//
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

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//            	//アクションバーのホームボタン
//                NavUtils.navigateUpTo(this, new Intent(this, MainActivity.class));
//                return true;
//
//            case R.id.action_previous:
//                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
//                return true;
//
//            case R.id.action_next:
//                mPager.setCurrentItem(mPager.getCurrentItem() + 1);
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return DiaryEditPageFragment.create(position);
        }

        @Override
        public int getCount() {
            return NUM_PHOTOS;
        }
    }


}
