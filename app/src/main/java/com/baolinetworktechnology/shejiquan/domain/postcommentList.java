package com.baolinetworktechnology.shejiquan.domain;

import android.text.TextUtils;

/**
 * Created by Administrator on 2017/4/19.
 */

public class postcommentList {
    private int commnentID;
    private String ownerName;
    private String ownerGUID;
    private String commentContent;
    private String replayUserName;
    private int replayID;
    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
    public String getCommentContent() {
        return commentContent;
    }

    public void setCommentContent(String commentContent) {
        this.commentContent = commentContent;
    }

    public String getReplayUserName() {
        return replayUserName;
    }

    public void setReplayUserName(String replayUserName) {
        this.replayUserName = replayUserName;
    }
    public int getCommnentID() {
        return commnentID;
    }

    public void setCommnentID(int commnentID) {
        this.commnentID = commnentID;
    }
    public String getOwnerGUID() {
        return ownerGUID;
    }

    public void setOwnerGUID(String ownerGUID) {
        this.ownerGUID = ownerGUID;
    }
}
