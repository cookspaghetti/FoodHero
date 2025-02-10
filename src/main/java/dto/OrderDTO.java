package dto;

import java.time.LocalDateTime;
import java.util.HashMap;

public class OrderDTO {
	
	private String id;
    private String customerId;
    private String vendorId;
    private String runnerId;
    private HashMap<String, Integer> items; // item id, amount
    private String status;
    private double totalAmount;
    private double deliveryFee;
    private LocalDateTime placementTime;
    private LocalDateTime completionTime;
    private String notes;
    private AddressDTO deliveryAddress;
    
    // Getters and Setters
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getVendorId() {
		return vendorId;
	}
	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}
	public String getRunnerId() {
		return runnerId;
	}
	public void setRunnerId(String runnerId) {
		this.runnerId = runnerId;
	}
	public HashMap<String, Integer> getItems() {
		return items;
	}
	public void setItems(HashMap<String, Integer> items) {
		this.items = items;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public double getDeliveryFee() {
		return deliveryFee;
	}
	public void setDeliveryFee(double deliveryFee) {
		this.deliveryFee = deliveryFee;
	}
	public LocalDateTime getPlacementTime() {
		return placementTime;
	}
	public void setPlacementTime(LocalDateTime placementTime) {
		this.placementTime = placementTime;
	}
	public LocalDateTime getCompletionTime() {
		return completionTime;
	}
	public void setCompletionTime(LocalDateTime completionTime) {
		this.completionTime = completionTime;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public AddressDTO getDeliveryAddress() {
		return deliveryAddress;
	}
	public void setDeliveryAddress(AddressDTO deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}
    
}
