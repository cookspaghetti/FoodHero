package dto;

import enumeration.PaymentMethod;

public class AddTransactionDTO extends TransactionDTO {

	private String adminId;
    private PaymentMethod paymentMethod;
    
    // Getters and Setters
	public String getAdminId() {
		return adminId;
	}
	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}
	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
    
    
	
}
