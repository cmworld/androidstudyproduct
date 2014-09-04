package com.app.entity;

public class Poi {

    public String title;
    public String titleImg;
    public String description;
    public String username;
    public long userId;
    public long views;
    public int aid;
    public int channelId;
    public int comments;
    public long releaseDate;
    public int stows;
    
    public int getStows() {
        return stows;
    }

    public void setStows(int stows) {
        this.stows = stows;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getTitleImg() {
        return titleImg;
    }


    public void setTitleImg(String titleImg) {
        this.titleImg = titleImg;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }


    public long getUserId() {
        return userId;
    }


    public void setUserId(long userId) {
        this.userId = userId;
    }


    public long getViews() {
        return views;
    }


    public void setViews(long views) {
        this.views = views;
    }


    public int getAid() {
        return aid;
    }


    public void setAid(int aid) {
        this.aid = aid;
    }


    public int getChannelId() {
        return channelId;
    }


    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }


    public int getComments() {
        return comments;
    }


    public void setComments(int comments) {
        this.comments = comments;
    }


    public long getReleaseDate() {
        return releaseDate;
    }


    public void setReleaseDate(long releaseDate) {
        this.releaseDate = releaseDate;
    }
	
}
