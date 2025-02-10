package dto;

public class AddTransactionDTO extends TransactionDTO {

	private String adminId;
    private String paymentMethod;
    
    // Getters and Setters
	public String getAdminId() {
		return adminId;
	}
	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
    
    
	
}
