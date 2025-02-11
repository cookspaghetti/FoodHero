package dto;

import enumeration.ComplainStatus;

public class ComplainDTO {
	
	private String id;
    private String customerId;
    private String orderId;
    private String description;
    private ComplainStatus status;
    private String solution;
    
    // Constructor
    public ComplainDTO(String id, String customerId, String orderId, String description, ComplainStatus status,
			String solution) {
		super();
		this.id = id;
		this.customerId = customerId;
		this.orderId = orderId;
		this.description = description;
		this.status = status;
		this.solution = solution;
	}
    
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
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public ComplainStatus getStatus() {
		return status;
	}
	public void setStatus(ComplainStatus status) {
		this.status = status;
	}
	public String getSolution() {
		return solution;
	}
	public void setSolution(String solution) {
		this.solution = solution;
	}
    
}
