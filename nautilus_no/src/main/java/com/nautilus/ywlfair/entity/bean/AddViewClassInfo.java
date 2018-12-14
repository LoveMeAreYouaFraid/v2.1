package com.nautilus.ywlfair.entity.bean;

import java.io.Serializable;

/**
 * Created by lp on 2016/3/6.
 */
public class AddViewClassInfo implements Serializable {
    private String name, power;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }
}