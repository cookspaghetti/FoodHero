package dto;

public class AddressDTO {
	
	private String id;
	private String userId;
	private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    
    // Getters and Setters
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
    
	// Methods
	public String getFullAddress() {
	    StringBuilder address = new StringBuilder();

	    if (street != null && !street.isEmpty()) {
	        address.append(street);
	    }
	    if (postalCode != null && !postalCode.isEmpty()) {
	    	if (address.length() > 0) address.append(", ");
	    	address.append(postalCode);
	    }
	    if (city != null && !city.isEmpty()) {
	        if (address.length() > 0) address.append(city);
	    }
	    if (state != null && !state.isEmpty()) {
	        if (address.length() > 0) address.append(", ");
	        address.append(state);
	    }
	    if (country != null && !country.isEmpty()) {
	        if (address.length() > 0) address.append(", ");
	        address.append(country);
	    }

	    return address.toString();
	}
	
}
