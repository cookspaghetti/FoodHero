package dto;

public class RunnerReviewDTO extends ReviewDTO {
	
	private String runnerId;
	
	// Constructor
	public RunnerReviewDTO(String runnerId) {
		super();
		this.runnerId = runnerId;
	}

	// Getters and Setters
	public String getRunnerId() {
		return runnerId;
	}

	public void setRunnerId(String runnerId) {
		this.runnerId = runnerId;
	}
	
}
