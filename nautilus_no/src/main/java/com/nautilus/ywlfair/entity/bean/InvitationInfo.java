package com.nautilus.ywlfair.entity.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/29.
 */
public class InvitationInfo implements Serializable {
    private String invitationCode;
    private String jobDescImageWidth;
    private String jobDescImageHeight;
    //任务说明图片url
    private String jobDescImageUrl;
    //背景头图url
    private String invitationDescImageUrl;
    private String invitationDescImageWidth;
    private String invitationDescImageHeight;

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public String getJobDescImageWidth() {
        return jobDescImageWidth;
    }

    public void setJobDescImageWidth(String jobDescImageWidth) {
        this.jobDescImageWidth = jobDescImageWidth;
    }

    public String getJobDescImageHeight() {
        return jobDescImageHeight;
    }

    public void setJobDescImageHeight(String jobDescImageHeight) {
        this.jobDescImageHeight = jobDescImageHeight;
    }

    public String getJobDescImageUrl() {
        return jobDescImageUrl;
    }

    public void setJobDescImageUrl(String jobDescImageUrl) {
        this.jobDescImageUrl = jobDescImageUrl;
    }

    public String getInvitationDescImageUrl() {
        return invitationDescImageUrl;
    }

    public void setInvitationDescImageUrl(String invitationDescImageUrl) {
        this.invitationDescImageUrl = invitationDescImageUrl;
    }

    public String getInvitationDescImageWidth() {
        return invitationDescImageWidth;
    }

    public void setInvitationDescImageWidth(String invitationDescImageWidth) {
        this.invitationDescImageWidth = invitationDescImageWidth;
    }

    public String getInvitationDescImageHeight() {
        return invitationDescImageHeight;
    }

    public void setInvitationDescImageHeight(String invitationDescImageHeight) {
        this.invitationDescImageHeight = invitationDescImageHeight;
    }
}
