package dto;

public class DeductTransactionDTO extends TransactionDTO {
	
	private String orderId;
	
	// Constructor
	public DeductTransactionDTO(String orderId) {
		super();
		this.orderId = orderId;
	}

	// Getters and Setters
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	
	
}
