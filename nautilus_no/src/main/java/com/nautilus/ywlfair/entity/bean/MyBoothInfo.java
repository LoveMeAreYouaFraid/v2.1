package com.nautilus.ywlfair.entity.bean;

public class MyBoothInfo extends BaseItem{

	private static final long serialVersionUID = -6595386696207243747L;
	
	private long boothId;
	
	private String boothType;
	
	private double price;
	
	private long num;
	
    private int actId;

    private long startdate;

    private int status;

    private String name;

    private long orderTime;

    private long enddate;

    private String address;

    private String imgUrl;

    private int type;

    private String orderId;

    private String roundNo;

    public String getRoundNo() {
        return roundNo;
    }

    public void setRoundNo(String roundNo) {
        this.roundNo = roundNo;
    }

    public String getOrderID() {
        return orderId;
    }

    public void setOrderID(String orderID) {
        this.orderId = orderID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public long getBoothId() {
		return boothId;
	}

	public void setBoothId(long boothId) {
		this.boothId = boothId;
	}

	public String getBoothType() {
		return boothType;
	}

	public void setBoothType(String boothType) {
		this.boothType = boothType;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public long getNum() {
		return num;
	}

	public void setNum(long num) {
		this.num = num;
	}

    public int getActId() {
        return actId;
    }

    public void setActId(int actId) {
        this.actId = actId;
    }

    public long getStartdate() {
        return startdate;
    }

    public void setStartdate(long startdate) {
        this.startdate = startdate;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(long orderTime) {
        this.orderTime = orderTime;
    }

    public long getEnddate() {
        return enddate;
    }

    public void setEnddate(long enddate) {
        this.enddate = enddate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
