package com.nautilus.ywlfair.entity.bean;

import java.io.File;
import java.io.Serializable;

public class PictureInfo implements Serializable {

    private int width;

    private int height;

    private File file;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}