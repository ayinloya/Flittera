package io.github.ayinloya.fragments;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.parse.ui.ParseLoginBuilder;

import java.util.ArrayList;
import java.util.List;

import io.github.ayinloya.adapters.FollowerRecyclerAdapter;
import io.github.ayinloya.dataModels.Follower;
import io.github.ayinloya.flittera.MainActivity;
import io.github.ayinloya.flittera.R;
import twitter4j.PagableResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;


public class FollowersFragment extends Fragment {

    public static List<Follower> followersList = new ArrayList<>();
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    private FollowerRecyclerAdapter adapter;
    private MainActivity mActivity;
    private String userTwitterName;
    private String userTwitterId;

    public static FollowersFragment getInstance() {
        FollowersFragment myFragment = new FollowersFragment();
        return myFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new FollowerRecyclerAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_followers_fragment, container, false);
        intializeViews(view);

        recyclerView.setAdapter(adapter);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        refreshPostList();
        try {

//            getActivity().getParent().setTitle(ParseTwitterUtils.getTwitter().getScreenName());
        } catch (Exception e) {
            Log.e("Followers", "Title not set");
        }
        if (!isConnected()) {
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_LONG).show();
        }
        return view;
    }

    public void refreshPostList() {

        ((MainActivity) getActivity()).showProgress();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Followers");

//        Toast.makeText(getActivity(), ParseTwitterUtils.getTwitter().getAuthToken(), Toast.LENGTH_LONG).show();
        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> followerList, ParseException e) {
                if (e == null) {
                    // If there are results, update the list of posts
                    // and notify the adapter
                    followersList.clear();
                    if (ParseTwitterUtils.getTwitter().getScreenName() != null) {
                     /*   VolleySingleton volleySingleton = VolleySingleton.getInstance();
                        RequestQueue requestQueue = volleySingleton.getRequestQueue();

                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getFollowers(ParseTwitterUtils.getTwitter().getUserId()), TwitterCom.getAuthorization_header_string(), new Response.Listener<JSONObject>() {


                            @Override
                            public void onResponse(JSONObject response) {

                                try {
                                    Log.e("*Does it?***", " e" + response.getJSONArray("users").length());
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                }
                                try {
                                    Log.e("Twitter Response", " ++++++++++++++ " + response.getJSONArray("users").length());
                                    Toast.makeText(MyApplication.getAppInstance(), response.getJSONArray("users").length(), Toast.LENGTH_LONG).show();
//
                                } catch (JSONException e1) {
                                    Log.e("*error*", e1.getMessage());
                                    e1.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Volley error", " " + error.getMessage());
                                Log.e("Volley error", " " + TwitterCom.getAuthorization_header_string());
                            }
                        }) {
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                super.getHeaders();
                                Map<String, String> headers = new HashMap<>();
//                                headers.put("Content-Type", "application/json;charset=utf-8");
                                headers.put("Authorization", TwitterCom.getAuthorization_header_string());
                                return headers;
                            }
                        };*/

                       /* try {
                            Log.e("headers volley", request.getHeaders().get("Authorization"));

                        } catch (AuthFailureError authFailureError) {
                            authFailureError.printStackTrace();
                        }

                        requestQueue.add(request);*/

                        initTwitter4jConfigs();
                        for (ParseObject follower : followerList) {
                            Follower followerData = new Follower(follower.getObjectId(), follower.getString("followerId"));
                            followerData.setObjectId(follower.getObjectId());

                            userTwitterName = ParseTwitterUtils.getTwitter().getScreenName();
                            userTwitterId = ParseTwitterUtils.getTwitter().getUserId();

                            followerData.setFollowerName(userTwitterName);
                            followerData.setTwitterUserId(userTwitterId);
                            followerData.setFollowDate(follower.getDate("createdAt"));
                            followersList.add(followerData);
                            adapter.setFollowers(followersList);
                        }
                    }

//                    mActivity.setSupportProgressBarIndeterminateVisibility(false);
                    ((MainActivity) getActivity()).hideProgress();

                    Toast.makeText(getActivity(), "The query is done, my boss", Toast.LENGTH_LONG).show();
                } else {
                    Log.d(getClass().getSimpleName(), "Error: " + e.getMessage());
                }
            }

        });
        if (!isConnected()) {
            query.fromLocalDatastore();
        }
    }

    private void initTwitter4jConfigs() {
        final ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(ParseTwitterUtils.getTwitter().getConsumerKey());
        builder.setOAuthConsumerSecret(ParseTwitterUtils.getTwitter().getConsumerSecret());
        builder.setOAuthAccessToken(ParseTwitterUtils.getTwitter().getAuthToken());
        builder.setOAuthAccessTokenSecret(ParseTwitterUtils.getTwitter().getAuthTokenSecret());

        final Configuration configuration = builder.build();
        final TwitterFactory factory = new TwitterFactory(configuration);
        Twitter twitter = factory.getInstance();
        ArrayList<User> followers = new ArrayList<>();
        long nextCursor = -1;
        try {
            do {
                PagableResponseList<User> usersResponse = twitter.getFollowersList(ParseTwitterUtils.getTwitter().getScreenName(), nextCursor);
                System.out.println("size() of first iteration:" + usersResponse.size());
                nextCursor = usersResponse.getNextCursor();
                followers.addAll(usersResponse);
            } while (nextCursor > 0);
        } catch (TwitterException e) {
            Log.e("Twitter 4 j", e.getErrorMessage());
            e.printStackTrace();
        }

        for (User user : followers) {
            System.out.println(user.getScreenName() + ", id: " + user.getId());
        }
    }


    private String getFollowers(String userId) {
        String followersUrl = "https://api.twitter.com/1.1/followers/list.json?user_id=" + ParseTwitterUtils.getTwitter().getUserId();//+ "&skip_status=true&include_user_entities=false&oauth_token=" + ParseTwitterUtils.getTwitter().getAuthToken();
        Toast.makeText(getActivity(), userId, Toast.LENGTH_LONG).show();

        return followersUrl;
    }

    private void intializeViews(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.followerList);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_followers, menu);
    }

    private boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                       activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_refresh) {
            refreshPostList();
            return true;
        }
        if (id == R.id.action_logOut) {
            logoutUser();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void logoutUser() {
        final ParseLoginBuilder builder = new ParseLoginBuilder(getActivity());
        if (isConnected()) {
            ParseUser.logOut();
            startActivityForResult(builder.build(), 0);
            ParseTwitterUtils.getTwitter().getAuthToken();
        } else {
            Toast.makeText(getActivity(), "Couldn't log you out, No internet connection found", Toast.LENGTH_LONG).show();

        }
    }
}

