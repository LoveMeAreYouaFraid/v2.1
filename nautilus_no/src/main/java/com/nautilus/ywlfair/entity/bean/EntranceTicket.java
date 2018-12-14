package com.nautilus.ywlfair.entity.bean;

public class EntranceTicket extends BaseItem {
	
	private static final long serialVersionUID = -1579080438762565333L;

	private String ticketCode;
	
	private String ticketImgUrl;
	
	private int numberOfPerTicket;
	


	public String getTicketCode() {
		return ticketCode;
	}

	public void setTicketCode(String ticketCode) {
		this.ticketCode = ticketCode;
	}

	public String getTicketImgUrl() {
		return ticketImgUrl;
	}

	public void setTicketImgUrl(String ticketImgUrl) {
		this.ticketImgUrl = ticketImgUrl;
	}

	public int getNumberOfPerTicket() {
		return numberOfPerTicket;
	}

	public void setNumberOfPerTicket(int numberOfPerTicket) {
		this.numberOfPerTicket = numberOfPerTicket;
	}
	
	
	
	
}
