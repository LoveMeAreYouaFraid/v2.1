package com.nautilus.ywlfair.entity.bean;

import java.util.List;

public class GoodsClassInfo extends BaseItem{

	private static final long serialVersionUID = -6506013140238436482L;
	
	private long id;
	
	private long parentId;
	
	private String className;

    private int level;
	
	private List<GoodsClassInfo> childClassList;
	
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<GoodsClassInfo> getChildClassList() {
        return childClassList;
    }

    public void setChildClassList(List<GoodsClassInfo> childClassList) {
        this.childClassList = childClassList;
    }


}
