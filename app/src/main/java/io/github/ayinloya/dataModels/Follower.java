package io.github.ayinloya.dataModels;

import java.util.Date;

/**
 * Created by barnabas on 4/29/15.
 */
public class Follower {
    private String objectId;
    private String followerId;
    private Date followDate;
    private String followerName;
    private String followerImage;
    private String twitterUserId;

    public Follower(String objectId,String followerId) {
        this.objectId = objectId;
        this.followerId=followerId;


    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getFollowerId() {
        return followerId;
    }

    public void setFollowerId(String followerId) {
        this.followerId = followerId;
    }

    public Date getFollowDate() {
        return followDate;
    }

    public void setFollowDate(Date followDate) {
        this.followDate = followDate;
    }

    public String getFollowerName() {
        return followerName;
    }

    public void setFollowerName(String followerName) {
        this.followerName = followerName;
    }

    public String getFollowerImage() {
        return followerImage;
    }

    public void setFollowerImage(String followerImage) {
        this.followerImage = followerImage;
    }

    public void setTwitterUserId(String twitterUserId) {
        this.twitterUserId = twitterUserId;
    }

    public String getTwitterUserId() {
        return twitterUserId;
    }
}
