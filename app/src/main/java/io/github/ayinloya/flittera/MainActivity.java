package io.github.ayinloya.flittera;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.android.volley.RequestQueue;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.ParseTwitterUtils;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.ui.ParseLoginBuilder;

import io.github.ayinloya.adapters.ViewPagerAdapter;
import io.github.ayinloya.fragments.NavigationDrawerFragment;
import io.github.ayinloya.network.VolleySingleton;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;
import twitter4j.Twitter;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;


public class MainActivity extends ActionBarActivity implements MaterialTabListener {
    public String mUsername;
    Toolbar toolbar;
    NavigationDrawerFragment navigationDrawerFragment;
    Twitter twitter;
    //    private FollowerRecyclerAdapter adapter;
    private MaterialTabHost tabHost;
    private ViewPager viewPager;
    private AccessToken accessToken;
    private RequestToken requestToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.activity_main);

        RequestQueue requestQueue = VolleySingleton.getInstance().getRequestQueue();
//        adapter = new FollowerRecyclerAdapter(followers);
        initializeParse();
        initializeViews();
        setActionBar();
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            loadLoginView();
        }


        ViewPagerAdapter adapter = new ViewPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                tabHost.setSelectedNavigationItem(position);
            }
        });
        viewPager.setOffscreenPageLimit(2);
        for (int i = 0; i < adapter.getCount(); i++) {
            tabHost.addTab(
                                  tabHost.newTab()
                                          .setText(adapter.getPageTitle(i))
                                          .setTabListener(this)
            );

        }
//        initTwitter4jConfigs();
        registerPushNotification();
//        refreshPostList();
    }

   /* private void initTwitter4jConfigs() {
        final ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(ParseTwitterUtils.getTwitter().getConsumerKey());
        builder.setOAuthConsumerSecret(ParseTwitterUtils.getTwitter().getConsumerSecret());
        builder.setOAuthAccessToken(ParseTwitterUtils.getTwitter().getAuthToken());
        builder.setOAuthAccessTokenSecret(ParseTwitterUtils.getTwitter().getAuthTokenSecret());

        final Configuration configuration = builder.build();
        final TwitterFactory factory = new TwitterFactory(configuration);
        twitter = factory.getInstance();
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
*/

    private void loadLoginView() {
//        ParseLoginBuilder builder = new ParseLoginBuilder(this);

        final ParseLoginBuilder builder = new ParseLoginBuilder(this);
        if (isConnected()) {
            startActivityForResult(builder.build(), 0);
            ParseTwitterUtils.getTwitter().getAuthToken();
        } else {
            ParseUser.logOut();
        }

//        ParseTwitterUtils.logIn(this, new LogInCallback() {
//            @Override
//            public void done(ParseUser user, ParseException err) {
//                if (user == null) {
//                    Log.d("MyApp", "Uh oh. The user cancelled the Twitter login.");
//                    startActivityForResult(builder.build(), 0);
//                } else if (user.isNew()) {
//                    Log.d("MyApp", "User signed up and logged in through Twitter!");
//                } else {
//                    Log.d("MyApp", "User logged in through Twitter!");
//                }
//            }
//        });

    }

    private void initializeParse() {
        Parse.initialize(MyApplication.getAppInstance(), MyApplication.APP_ID, MyApplication.CLIENT_KEY);
        ParseTwitterUtils.initialize(MyApplication.CUSTOMER_KEY, MyApplication.CUSTOMER_SECRET);
    }

    private boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                       activeNetwork.isConnectedOrConnecting();
    }

    private void setActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    private void registerPushNotification() {
        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });
    }

    private void initializeViews() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        navigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.drawer_layout);
        tabHost = (MaterialTabHost) findViewById(R.id.materialTabHost);
        viewPager = (ViewPager) findViewById(R.id.pager);
    }

    private void newParseUser() {
        ParseUser user = new ParseUser();
        user.setUsername("my name");
        user.setPassword("my pass");
        user.setEmail("email@example.com");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

        return super.onOptionsItemSelected(item);
    }


  /*  private void refreshPostList() {

        setSupportProgressBarIndeterminateVisibility(true);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Follower");

        query.findInBackground(new FindCallback<ParseObject>() {

            @Override
            public void done(List<ParseObject> followerList, ParseException e) {
                if (e == null) {
                    // If there are results, update the list of posts
                    // and notify the adapter
                    followers.clear();
                    for (ParseObject follower : followerList) {
                        Follower followerData = new Follower(follower.getObjectId(), follower.getString("followerId"));
                        followers.add(followerData);
                    }
                    adapter.notifyDataSetChanged();
                    setSupportProgressBarIndeterminateVisibility(false);

                } else {
                    Log.d(getClass().getSimpleName(), "Error: " + e.getMessage());
                }
            }
        });
    }*/

    public void showProgress() {
        setSupportProgressBarIndeterminateVisibility(true);
    }

    public void hideProgress() {
        setSupportProgressBarIndeterminateVisibility(false);
    }

    @Override
    public void onTabSelected(MaterialTab materialTab) {
        viewPager.setCurrentItem(materialTab.getPosition(), true);
    }

    @Override
    public void onTabReselected(MaterialTab materialTab) {

    }

    @Override
    public void onTabUnselected(MaterialTab materialTab) {

    }
}
