package io.github.ayinloya.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import io.github.ayinloya.flittera.R;
import io.github.ayinloya.fragments.FollowersFragment;
import io.github.ayinloya.fragments.UnFollowersFragment;

/**
 * Created by barnabas on 4/29/15.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
//    int icons[] = {R.drawable.artist, R.drawable.album, R.drawable.song, R.drawable.playlist};
    String[] tabs;
Context context;
    public ViewPagerAdapter(Context context,FragmentManager fm) {
        super(fm);
        this.context=context;
        tabs =  context.getResources().getStringArray(R.array.nav_list);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return FollowersFragment.getInstance();
            case 1:
                return UnFollowersFragment.getInstance();
            default:
                return FollowersFragment.getInstance();
        }

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return context.getResources().getStringArray(R.array.nav_list)[position];
    }

    @Override
    public int getCount() {
        return 2;
    }

//    private Drawable getIcon(int position) {
//        return context.getResources().getDrawable(icons[position], null);
//    }
}
