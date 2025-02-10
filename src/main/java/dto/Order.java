package dto;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
	
	private Long id;
    private Long customerId;
    private Long vendorId;
    private Long runnerId;
    private List<Item> items;
    private String status;
    private double totalAmount;
    private double deliveryFee;
    private LocalDateTime placementTime;
    private LocalDateTime completionTime;
    private String notes;
    private Address deliveryAddress;
    
    // Getters and Setters
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	public Long getVendorId() {
		return vendorId;
	}
	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}
	public Long getRunnerId() {
		return runnerId;
	}
	public void setRunnerId(Long runnerId) {
		this.runnerId = runnerId;
	}
	public List<Item> getItems() {
		return items;
	}
	public void setItems(List<Item> items) {
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
	public Address getDeliveryAddress() {
		return deliveryAddress;
	}
	public void setDeliveryAddress(Address deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}
    
}
