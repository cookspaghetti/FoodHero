package dto;

public class AddTransaction extends Transaction {

	private Long adminId;
    private String paymentMethod;
    
    // Getters and Setters
	public Long getAdminId() {
		return adminId;
	}
	public void setAdminId(Long adminId) {
		this.adminId = adminId;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
    
    
	
}
