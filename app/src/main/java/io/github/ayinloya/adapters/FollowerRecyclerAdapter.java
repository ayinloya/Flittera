package io.github.ayinloya.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.github.ayinloya.dataModels.Follower;
import io.github.ayinloya.flittera.R;

/**
 * Created by barnabas on 4/29/15.
 */
public class FollowerRecyclerAdapter extends RecyclerView.Adapter<FollowerRecyclerAdapter.MyViewHolder> {
    private List<Follower> followers=new ArrayList<>();

    public FollowerRecyclerAdapter() {
    }

    @Override
    public FollowerRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.follower_row, parent, false);
        return new MyViewHolder(view);
    }

//    public FollowerRecyclerAdapter getfollowerRecyclerAdapter() {
//        return this;
//    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Follower follower=followers.get(position);
        holder.followerName.setText(follower.getFollowerName());
        holder.followerDate.setText((CharSequence) follower.getFollowDate());
//        holder.followerImage.setImageURI(Uri.parse(follower.getFollowerName()));
    }

    public void setFollowers(List<Follower> followers) {
        this.followers = followers;
//        notifyItemRangeChanged(0, followers.size());
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return followers.size();
    }




    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView followerName;
        public TextView followerDate;
        public ImageView followerImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            followerName = (TextView) itemView.findViewById(R.id.followerName);
            followerDate = (TextView) itemView.findViewById(R.id.followDate);
            followerImage = (ImageView) itemView.findViewById(R.id.followerImage);
        }
    }
}
