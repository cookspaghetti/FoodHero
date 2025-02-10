package dto;

import java.util.List;

public class CustomerDTO extends UserDTO {
	
	private double credit;
    private List<String> orderHistory;
    private List<String> vendorReviews;
    private List<String> runnerReviews;
    private List<String> complains;
    private List<String> transactions;
    private List<String> deliveryAddresses;
    
    // Getters and Setters
	public double getCredit() {
		return credit;
	}
	public void setCredit(double credit) {
		this.credit = credit;
	}
	public List<String> getOrderHistory() {
		return orderHistory;
	}
	public void setOrderHistory(List<String> orderHistory) {
		this.orderHistory = orderHistory;
	}
	public List<String> getVendorReviews() {
		return vendorReviews;
	}
	public void setVendorReviews(List<String> vendorReviews) {
		this.vendorReviews = vendorReviews;
	}
	public List<String> getRunnerReviews() {
		return runnerReviews;
	}
	public void setRunnerReviews(List<String> runnerReviews) {
		this.runnerReviews = runnerReviews;
	}
	public List<String> getComplains() {
		return complains;
	}
	public void setComplains(List<String> complains) {
		this.complains = complains;
	}
	public List<String> getTransactions() {
		return transactions;
	}
	public void setTransactions(List<String> transactions) {
		this.transactions = transactions;
	}
	public List<String> getDeliveryAddresses() {
		return deliveryAddresses;
	}
	public void setDeliveryAddresses(List<String> deliveryAddresses) {
		this.deliveryAddresses = deliveryAddresses;
	}
    
}
