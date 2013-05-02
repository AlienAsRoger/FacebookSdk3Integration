package com.facebook.samples.hellofacebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.facebook.*;
import com.facebook.model.GraphUser;
import com.facebook.widget.FriendPickerFragment;
import com.facebook.widget.LoginButton;
import com.facebook.widget.PickerFragment;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: roger sent2roger@gmail.com
 * Date: 01.05.13
 * Time: 8:51
 */
public class TestFragment extends Fragment implements View.OnClickListener {

	private GraphUser graphUser;

	private UiLifecycleHelper uiHelper;
	private FriendPickerFragment friendPickerFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		uiHelper = new UiLifecycleHelper(getActivity(), callback);

		uiHelper.onCreate(savedInstanceState);

		friendPickerFragment = new FriendPickerFragment();

		friendPickerFragment.setOnErrorListener(new PickerFragment.OnErrorListener() {
			@Override
			public void onError(PickerFragment<?> fragment, FacebookException error) {
				Log.d("TEST"," error happened, error = " + error.toString());
			}
		});
		friendPickerFragment.setOnDoneButtonClickedListener(new PickerFragment.OnDoneButtonClickedListener() {
			@Override
			public void onDoneButtonClicked(PickerFragment<?> fragment) {
				Log.d("TEST"," done clicked");
				for (GraphUser user : friendPickerFragment.getSelection()) {
					Log.d("TEST"," user = " + user.toString());
				}
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.test_frame, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		view.findViewById(R.id.getMeBtn).setOnClickListener(this);
		view.findViewById(R.id.getFriendsBtn).setOnClickListener(this);

		LoginButton loginBtn = (LoginButton) view.findViewById(R.id.loginBtn);
		loginBtn.setFragment(this);

		loginBtn.setReadPermissions(Arrays.asList("user_status","email"));

		loginBtn.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
			@Override
			public void onUserInfoFetched(GraphUser user) {
				graphUser = user;
			}
		});

		FragmentManager manager = getFragmentManager();
		manager.beginTransaction().replace(R.id.picker_fragment, friendPickerFragment).commit();
	}

	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();

		updateUI();

		try {
			friendPickerFragment.loadData(true);
		} catch (Exception ex) {
			onError(ex);
		}
	}

	private void onError(Exception ex) {
		Log.e("TEST", "exception = " + ex.toString());

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		updateUI();
	}

	private void updateUI() {
		if (graphUser != null) {
			Log.d("TEST", graphUser.getFirstName() + graphUser.asMap().get("email"));
		}
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.getMeBtn) {
			Session session = Session.getActiveSession();
			Log.d("TEST", "session.getState() = " + session.getState());
			if (session.getState().isOpened()) {
				Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

					@Override
					public void onCompleted(GraphUser user, Response response) {
						if (user != null) {
							updateUI();
						}
					}
				});
			}
		} else if (view.getId() == R.id.getFriendsBtn) {
			try {
				friendPickerFragment.loadData(true);
			} catch (Exception ex) {
				onError(ex);
			}
		}
	}


}
