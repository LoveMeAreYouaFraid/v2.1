package com.nautilus.ywlfair.entity.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/18.
 */
public class VendorLevel implements Serializable{

    private int level;

    private String levelName;

    private int lowerLimit;

    private int upperLimit;

    private String privilegeId;

    private int privilegeCount;

    private double saleAmount;

    private int score;

    private String scoreHelpUrl;

    private String levelHelpUrl;

    public String getLevelHelpUrl() {
        return levelHelpUrl;
    }

    public void setLevelHelpUrl(String levelHelpUrl) {
        this.levelHelpUrl = levelHelpUrl;
    }

    public String getScoreHelpUrl() {
        return scoreHelpUrl;
    }

    public void setScoreHelpUrl(String scoreHelpUrl) {
        this.scoreHelpUrl = scoreHelpUrl;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getLevelName() {
        return levelName;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public int getLowerLimit() {
        return lowerLimit;
    }

    public void setLowerLimit(int lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    public int getUpperLimit() {
        return upperLimit;
    }

    public void setUpperLimit(int upperLimit) {
        this.upperLimit = upperLimit;
    }

    public String getPrivilegeId() {
        return privilegeId;
    }

    public void setPrivilegeId(String privilegeId) {
        this.privilegeId = privilegeId;
    }

    public int getPrivilegeCount() {
        return privilegeCount;
    }

    public void setPrivilegeCount(int privilegeCount) {
        this.privilegeCount = privilegeCount;
    }

    public double getSaleAmount() {
        return saleAmount;
    }

    public void setSaleAmount(double saleAmount) {
        this.saleAmount = saleAmount;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
