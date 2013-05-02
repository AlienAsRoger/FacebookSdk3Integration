package com.facebook.samples.hellofacebook;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created with IntelliJ IDEA.
 * User: roger sent2roger@gmail.com
 * Date: 01.05.13
 * Time: 9:57
 */
public class TestActivity extends FragmentActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity);
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.fragment_container, new TestFragment())
				.addToBackStack(TestFragment.class.getSimpleName())
				.commit();
	}

//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//
//		FragmentManager fragmentManager = getSupportFragmentManager();
//		TestFragment fragmentByTag = (TestFragment) fragmentManager.findFragmentByTag(TestFragment.class.getSimpleName());
//		if (fragmentByTag != null) {
//			fragmentByTag.onActivityResult(requestCode, resultCode, data);
//		}
//
//
//	}
}