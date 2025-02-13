package dto;

import java.time.LocalDateTime;
import java.util.List;

public class RunnerDTO extends UserDTO {
	
	private String plateNumber;
    private List<String> tasks;
    private double earnings;
    private double ratings;
    private Boolean available;
    private List<String> reviews;
    private String lastDeliveredAddress;
    private LocalDateTime lastDeliveryDate;

	// Getters and Setters
	public String getPlateNumber() {
		return plateNumber;
	}
	public void setPlateNumber(String plateNumber) {
		this.plateNumber = plateNumber;
	}
	public List<String> getTasks() {
		return tasks;
	}
	public void setTasks(List<String> tasks) {
		this.tasks = tasks;
	}
	public double getEarnings() {
		return earnings;
	}
	public void setEarnings(double earnings) {
		this.earnings = earnings;
	}
	public List<String> getReviews() {
		return reviews;
	}
	public void setReviews(List<String> reviews) {
		this.reviews = reviews;
	}
	public String getLastDeliveredAddress() {
		return lastDeliveredAddress;
	}
	public void setLastDeliveredAddress(String lastDeliveredAddress) {
		this.lastDeliveredAddress = lastDeliveredAddress;
	}
	public LocalDateTime getLastDeliveryDate() {
		return lastDeliveryDate;
	}
	public void setLastDeliveryDate(LocalDateTime lastDeliveryDate) {
		this.lastDeliveryDate = lastDeliveryDate;
	}
	public double getRatings() {
		return ratings;
	}
	public void setRatings(double ratings) {
		this.ratings = ratings;
	}
	public Boolean isAvailable() {
		return available;
	}
	public void setAvailable(Boolean available) {
		this.available = available;
	}
    
}
