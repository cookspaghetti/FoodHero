package dto;

public class VendorReviewDTO extends ReviewDTO {
	
	private String vendorId;
	
	// Constructor
	public VendorReviewDTO(String vendorId) {
		super();
		this.vendorId = vendorId;
	}

	// Getters and Setters
	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}
	
}
