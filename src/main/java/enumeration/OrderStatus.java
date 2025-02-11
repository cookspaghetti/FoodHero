package enumeration;

public enum OrderStatus {
	
	PENDING,         // Order placed but not yet processed
    PROCESSING,      // Order is being prepared by the vendor
    READY_FOR_PICKUP,// Order is ready for the runner to pick up
    ON_THE_WAY,      // Runner is delivering the order
    DELIVERED,       // Order successfully delivered to the customer
    CANCELLED,       // Order has been cancelled
    FAILED           // Order failed due to issues
    
}
