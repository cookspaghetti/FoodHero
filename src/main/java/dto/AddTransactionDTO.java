package dto;

import enumeration.PaymentMethod;

public class AddTransactionDTO extends TransactionDTO {
	
	private String adminId;
    private PaymentMethod paymentMethod;
    
    // Constructor
    public AddTransactionDTO(String adminId, PaymentMethod paymentMethod) {
		super();
		this.adminId = adminId;
		this.paymentMethod = paymentMethod;
	}
    
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
