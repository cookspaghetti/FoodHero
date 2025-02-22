package enumeration;

public enum OrderStatus {
	
    DINE_IN,         // Order is for dine-in

	PENDING,         // Order placed but not yet processed
    PROCESSING,      // Order is being prepared by the vendor
    RUNNER_ASSIGNED, // Runner has been assigned to the order
    READY_FOR_TAKEAWAY, // Order is ready for the customer to pick up
    READY_FOR_PICKUP,// Order is ready for the runner to pick up
    ON_THE_WAY,      // Runner is delivering the order
    DELIVERED,       // Order successfully delivered to the customer
    CANCELLED,       // Order has been cancelled
    FAILED           // Order failed due to issues
    
}
