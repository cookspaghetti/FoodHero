package service.general;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import dto.AdminDTO;
import dto.CustomerDTO;
import dto.ManagerDTO;
import dto.RunnerDTO;
import dto.UserDTO;
import dto.VendorDTO;
import enumeration.Role;
import enumeration.VendorType;

public class SessionControlService {
	
	// User DTO
	private static UserDTO user;
	
	// Common user information
	private static String id;
	private static String name;
	private static String phoneNumber;
	private static String addressId;
	private static String emailAddress;
	private static String password;
	private static Boolean status;
	private static Role role;

	// Role-specific information
	private static String plateNumber;                    // Runner
	private static List<String> tasks;                    // Runner
	private static double earnings;                       // Runner
	private static double ratings;                        // Runner/Vendor
	private static List<String> reviews;                  // Runner/Vendor
	private static String lastDeliveredAddress;           // Runner
	private static LocalDateTime lastDeliveryDate;        // Runner
	private static Boolean available;					  // Runner
	private static String currentTask;					  // Runner
	

	private static String vendorName;                     // Vendor
	private static HashMap<String, Integer> items;        // Vendor
	private static List<String> orderHistory;             // Vendor
	private static Boolean open;						  // Vendor
	private static VendorType vendorType;				  // Vendor
	
	private static double credit;						  // Customer
    private static List<String> vendorReviews;			  // Customer
    private static List<String> runnerReviews;			  // Customer
    private static List<String> complains;				  // Customer
    private static List<String> transactions;			  // Customer
    private static List<String> deliveryAddresses;		  // Customer
    
    private static HashMap<String, Integer> cartItems;    // item id, amount

	// === Set Session for Admin ===
	public static void setSession(AdminDTO admin) {
		clearSession();
		user = admin;
		role = Role.ADMIN;
		setCommonUserInfo(admin);
	}

	// === Set Session for Manager ===
	public static void setSession(ManagerDTO manager) {
		clearSession();
		user = manager;
		role = Role.MANAGER;
		setCommonUserInfo(manager);
	}

	// === Set Session for Runner ===
	public static void setSession(RunnerDTO runner) {
		clearSession();
		user = runner;
		role = Role.RUNNER;
		setCommonUserInfo(runner);

		plateNumber = runner.getPlateNumber();
		tasks = runner.getTasks();
		earnings = runner.getEarnings();
		ratings = runner.getRatings();
		reviews = runner.getReviews();
		lastDeliveredAddress = runner.getLastDeliveredAddress();
		lastDeliveryDate = runner.getLastDeliveryDate();
		available = runner.isAvailable();
		currentTask = runner.getCurrentTask();
	}

	// === Set Session for Vendor ===
	public static void setSession(VendorDTO vendor) {
		clearSession();
		user = vendor;
		role = Role.VENDOR;
		setCommonUserInfo(vendor);

		vendorName = vendor.getVendorName();
		items = vendor.getItems();
		orderHistory = vendor.getOrderHistory();
		ratings = vendor.getRatings();
		reviews = vendor.getReviews();
		open = vendor.getOpen();
		vendorType = vendor.getVendorType();
	}

	// === Set Session for Customer ===
	public static void setSession(CustomerDTO customer) {
		clearSession();
		user = customer;
		role = Role.VENDOR;
		setCommonUserInfo(customer);

		credit = customer.getCredit();
	    orderHistory = customer.getOrderHistory();
	    vendorReviews = customer.getVendorReviews();
	    runnerReviews = customer.getRunnerReviews();
	    complains = customer.getComplains();
	    transactions  = customer.getTransactions();
	    deliveryAddresses = customer.getDeliveryAddresses();
	}

	// === Clear Session (Logout) ===
	public static void clearSession() {
		user = null;
		id = name = phoneNumber = addressId = emailAddress = password = null;
		status = null;
		role = null;

		plateNumber = lastDeliveredAddress = vendorName = null;
		tasks = reviews = orderHistory = null;
		items = null;
		earnings = ratings = 0.0;
		lastDeliveryDate = null;
	}

	// === Helper to Set Common User Info ===
	private static void setCommonUserInfo(UserDTO user) {
		id = user.getId();
		name = user.getName();
		phoneNumber = user.getPhoneNumber();
		addressId = user.getAddressId();
		emailAddress = user.getEmailAddress();
		password = user.getPassword();
		status = user.getStatus();
	}

	// === Getters for Session Info ===
	public static UserDTO getUser() { return user; };
	public static String getId() { return id; }
	public static String getName() { return name; }
	public static String getPhoneNumber() { return phoneNumber; }
	public static String getAddressId() { return addressId; }
	public static String getEmailAddress() { return emailAddress; }
	public static String getPassword() { return password; }
	public static Boolean getStatus() { return status; }
	public static Role getRole() { return role; }

	public static String getPlateNumber() { return plateNumber; }
	public static List<String> getTasks() { return tasks; }
	public static double getEarnings() { return earnings; }
	public static double getRatings() { return ratings; }
	public static List<String> getReviews() { return reviews; }
	public static String getLastDeliveredAddress() { return lastDeliveredAddress; }
	public static LocalDateTime getLastDeliveryDate() { return lastDeliveryDate; }
	public static Boolean getAvailable() { return available; }
	public static String getCurrentTask() { return currentTask; }

	public static String getVendorName() { return vendorName; }
	public static HashMap<String, Integer> getItems() { return items; }
	public static List<String> getOrderHistory() { return orderHistory; }
	public static double getCredit() { return credit; };
    public static List<String> getVendorReviews() { return vendorReviews; };
    public static List<String> getRunnerReviews() { return runnerReviews; };
    public static List<String> getComplains() { return complains; };
    public static List<String> getTransactions() { return transactions; };
    public static List<String> getDeliveryAddresses() { return deliveryAddresses; };
	public static Boolean getOpen() { return open; }
	public static VendorType getVendorType() { return vendorType; }

	// Cart Service
	public static void addToCart(String itemId, int amount) {
		if (cartItems == null) {
			cartItems = new HashMap<>();
		}
		cartItems.put(itemId, amount);
	}

	public static void removeFromCart(String itemId) {
		if (cartItems != null) {
			cartItems.remove(itemId);
		}
	}

	public static HashMap<String, Integer> getCartItems() {
		return cartItems;
	}

	public static void clearCart() {
		cartItems = null;
	}

	public static void updateCart(String itemId, int amount) {
		if (cartItems != null) {
			cartItems.put(itemId, amount);
		}
	}
}
