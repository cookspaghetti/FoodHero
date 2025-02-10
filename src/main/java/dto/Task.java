package dto;

import java.time.LocalDateTime;

public class Task {

	
	private Long id;
    private Long orderId;
    private Long runnerId;
    private String status;
    private String taskDetails;
    private LocalDateTime acceptanceTime;
    private LocalDateTime completionTime;
    
    // Getters and Setters
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Long getRunnerId() {
		return runnerId;
	}
	public void setRunnerId(Long runnerId) {
		this.runnerId = runnerId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTaskDetails() {
		return taskDetails;
	}
	public void setTaskDetails(String taskDetails) {
		this.taskDetails = taskDetails;
	}
	public LocalDateTime getAcceptanceTime() {
		return acceptanceTime;
	}
	public void setAcceptanceTime(LocalDateTime acceptanceTime) {
		this.acceptanceTime = acceptanceTime;
	}
	public LocalDateTime getCompletionTime() {
		return completionTime;
	}
	public void setCompletionTime(LocalDateTime completionTime) {
		this.completionTime = completionTime;
	}
    
}
