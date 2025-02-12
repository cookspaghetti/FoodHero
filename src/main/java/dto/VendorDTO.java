package dto;

import java.util.HashMap;
import java.util.List;

public class VendorDTO extends UserDTO {
	
	private String vendorName;
	private Boolean open;
    private HashMap<String, Integer> items; // item id, stocks left
    private List<String> orderHistory;
    private double ratings;
    private List<String> reviews;
    
	// Getters and Setters
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public HashMap<String, Integer> getItems() {
		return items;
	}
	public void setItems(HashMap<String, Integer> items) {
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
	public double getRatings() {
		return ratings;
	}
	public void setRatings(double ratings) {
		this.ratings = ratings;
	}
	public Boolean getOpen() {
		return open;
	}
	public void setOpen(Boolean open) {
		this.open = open;
	}
    
}
