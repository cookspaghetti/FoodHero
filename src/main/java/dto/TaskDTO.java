package dto;

import java.time.LocalDateTime;

public class TaskDTO {

	private String id;
    private String orderId;
    private String runnerId;
    private String status;
    private String taskDetails;
    private LocalDateTime acceptanceTime;
    private LocalDateTime completionTime;
    
    // Getters and Setters
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getRunnerId() {
		return runnerId;
	}
	public void setRunnerId(String runnerId) {
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
