package com.syz.guidepage.demo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author syz
 * @date 2016-4-20
 */
public class GuideActivity extends Activity implements OnTouchListener, OnClickListener {

	
	private ViewPager mViewPager;
	private ViewPageAdapter adapter;
	private int flaggingWidth;
	private int size = 0;
	private int lastX = 0;
	private int currentIndex = 0;
	private CirclePageIndicator indicator;
	private boolean locker = true;
	private List<View> views = new ArrayList<View>();
	private TextView mTextView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏应用程序的标题栏
		//隐藏系统标题栏
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_guide);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		flaggingWidth = dm.widthPixels / 2;

		initViewPager();
	}
	
	protected void initViewPager() {
		mViewPager = (ViewPager) findViewById(R.id.pager_splash_ad);
		View view = LayoutInflater.from(this).inflate(R.layout.guide_view_pager, null);
		ImageView iv_ad = (ImageView) view.findViewById(R.id.pic_view);
		// iv_ad.setImageResource(R.drawable.guide1);//极其耗内存，图片资源过大，很容易出现内存OOM
		// ImageLoaderUtil.loadImage(null, iv_ad, R.drawable.guide1);
		Bitmap bitmap = readBitmap(R.drawable.guide1);
		iv_ad.setImageBitmap(bitmap);
		views.add(view);

		View view1 = LayoutInflater.from(this).inflate(R.layout.guide_view_pager, null);
		ImageView iv_ad1 = (ImageView) view1.findViewById(R.id.pic_view);
		Bitmap bitmap1 = readBitmap(R.drawable.guide2);
		iv_ad1.setImageBitmap(bitmap1);
		views.add(view1);

		View view2 = LayoutInflater.from(this).inflate(R.layout.guide_view_pager, null);
		ImageView iv_ad2 = (ImageView) view2.findViewById(R.id.pic_view);
		mTextView = (TextView) view2.findViewById(R.id.goto_main);
		mTextView.setVisibility(View.VISIBLE);
		mTextView.setOnClickListener(this);
		Bitmap bitmap2 = readBitmap(R.drawable.guide3);
		iv_ad2.setImageBitmap(bitmap2);
		views.add(view2);

		size = views.size();

		adapter = new ViewPageAdapter(views);
		mViewPager.setAdapter(adapter);
		indicator = (CirclePageIndicator) findViewById(R.id.viewflowindic);
		indicator.setmListener(new MypageChangeListener());
		indicator.setViewPager(mViewPager);

		mViewPager.setOnTouchListener(this);
	}

	protected Bitmap readBitmap(int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();

		// 对像素要求不高的尽量使用RGB_565
		// opt.inPreferredConfig = Bitmap.Config.RGB_565;
		// 该配置比上面的RGB_565要强一点，支持透明的方式
		opt.inPreferredConfig = Bitmap.Config.ARGB_8888;

		opt.inPurgeable = true;// 表示可回收
		opt.inInputShareable = true;// 可共享，与上面配合使用

		// 获取资源图片
		InputStream is = this.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.goto_main) {
			gotoMain();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

	/**
	 * view适配器
	 */
	protected class ViewPageAdapter extends PagerAdapter {

		List<View> list = new ArrayList<View>();

		public ViewPageAdapter(List<View> list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			((ViewPager) container).addView(list.get(position), 0);
			return list.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView(list.get(position));
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;// 官方提示这样写
		}

	}

	private void gotoMain() {
		startActivity(new Intent(this, MainActivity.class));
		finish();
		overridePendingTransition(R.anim.alpha_in_anim, R.anim.alpha_out_anim);
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			lastX = (int) event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			if ((lastX - event.getX()) > flaggingWidth && (currentIndex == size - 1) && locker) {
				locker = false;
				gotoMain();
			}
			break;
		default:
			break;
		}
		return false;
	}

	private class MypageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrollStateChanged(int position) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int arg0) {
			currentIndex = arg0;
		}

	}

}