package dto;

import java.util.List;

public class Vendor extends User {
	
	private String vendorName;
    private List<String> items;
    private List<String> orderHistory;
    private List<String> reviews;
    
    // Getters and Setters
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public List<String> getItems() {
		return items;
	}
	public void setItems(List<String> items) {
		this.items = items;
	}
	public List<String> getOrderHistory() {
		return orderHistory;
	}
	public void setOrderHistory(List<String> orderHistory) {
		this.orderHistory = orderHistory;
	}
	public List<String> getReviews() {
		return reviews;
	}
	public void setReviews(List<String> reviews) {
		this.reviews = reviews;
	}
    
}
