package com.wrapp.friendlist;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.ProfilePictureView;

public class ListFriendsFrag extends Fragment {
	
	//private static final String TAG = "ListFriendsFragment";
	
	private ProfilePictureView profilePictureView;
	private TextView userNameView;
	private UiLifecycleHelper uiHelper;
	private static final int REAUTH_ACTIVITY_CODE = 100;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    super.onCreateView(inflater, container, savedInstanceState);
	    uiHelper = new UiLifecycleHelper(getActivity(), callback);
	    uiHelper.onCreate(savedInstanceState);
	    View view = inflater.inflate(R.layout.friendslist, container, false);
	    profilePictureView = (ProfilePictureView) view.findViewById(R.id.selection_profile_pic);
	    profilePictureView.setCropped(true);
	    userNameView = (TextView) view.findViewById(R.id.selection_user_name);
	    Button showFriendButton=(Button) view.findViewById(R.id.showfriendbutton);
	    showFriendButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startShowFriendsActivity(ShowFriendActivity.FRIEND_PICKER, 100);
				
			}
		});
	    Session session = Session.getActiveSession();
	    if (session != null && session.isOpened()) {
	        // Get the user's data
	        makeMeRequest(session);
	    }
	    return view;
	}
	
	private void makeMeRequest(final Session session) {
	   
	    Request request = Request.newMeRequest(session, 
	            new Request.GraphUserCallback() {
	        @Override
	        public void onCompleted(GraphUser user, Response response) {
	           
	            if (session == Session.getActiveSession()) {
	                if (user != null) {	                   
	                    profilePictureView.setProfileId(user.getId());	                 
	                    userNameView.setText(user.getName());
	                }
	            }
	            if (response.getError() != null) {
	                // Handle errors
	            }
	        }
	    });
	    request.executeAsync();
	} 
	
	private void onSessionStateChange(final Session session, SessionState state, Exception exception) {
	    if (session != null && session.isOpened()) {
	        makeMeRequest(session);
	    }
	}
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(final Session session, final SessionState state, final Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    if (requestCode == REAUTH_ACTIVITY_CODE) {
	        uiHelper.onActivityResult(requestCode, resultCode, data);
	    }
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    uiHelper.onResume();
	}

	@Override
	public void onSaveInstanceState(Bundle bundle) {
	    super.onSaveInstanceState(bundle);
	    uiHelper.onSaveInstanceState(bundle);
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

	
	private void startShowFriendsActivity(Uri data, int requestCode) {
	    Intent intent = new Intent();
	    intent.setData(data);
	    intent.setClass(getActivity(), ShowFriendActivity.class);
	    startActivityForResult(intent, requestCode);
	}
}
