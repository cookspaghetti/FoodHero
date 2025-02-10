package service.admin;

import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONObject;

import dto.Admin;
import dto.Customer;
import dto.Manager;
import dto.Runner;
import dto.Vendor;

public class UserService {

	private static final String SYS_PATH = "/FoodHero/src/main/resources/database/";

	// Method to create an admin and save to a text file in JSON format
	public void createAdmin(Admin admin) {

		String filePath = SYS_PATH + "admin.txt";

		// Construct JSON Object
		JSONObject json = new JSONObject();
		json.put("id", admin.getId());
		json.put("name", admin.getName());
		json.put("phoneNumber", admin.getPhoneNumber());
		json.put("address", admin.getAddress());
		json.put("email", admin.getEmailAddress());

		// Write JSON to text file
		try (FileWriter file = new FileWriter(filePath, true)) {
			file.write(json.toString() + System.lineSeparator());
			System.out.println("Admin created successfully!");
		} catch (IOException e) {
			System.err.println("Error writing admin to file: " + e.getMessage());
		}
	}

	// Method to create an customer and save to a text file in JSON format
	public void createCustomer(Customer customer) {

		String filePath = SYS_PATH + "customer.txt";

		// Construct JSON Object
		JSONObject json = new JSONObject();
		json.put("id", customer.getId());
		json.put("name", customer.getName());
		json.put("phoneNumber", customer.getPhoneNumber());
		json.put("address", customer.getAddress());
		json.put("email", customer.getEmailAddress());
		json.put("credit", customer.getCredit());

		// Add Lists of IDs
		json.put("orderHistory", customer.getOrderHistory());
		json.put("vendorReviews", customer.getVendorReviews());
		json.put("runnerReviews", customer.getRunnerReviews());
		json.put("complains", customer.getComplains());
		json.put("transactions", customer.getTransactions());
		json.put("deliveryAddresses", customer.getDeliveryAddresses());

		// Write JSON to text file
		try (FileWriter file = new FileWriter(filePath, true)) {
			file.write(json.toString() + System.lineSeparator());
			System.out.println("Customer created successfully!");
		} catch (IOException e) {
			System.err.println("Error writing customer to file: " + e.getMessage());
		}
	}
	
	// Method to create a manager and save to a text file in JSON format
	public void createManager(Manager manager) {

	    String filePath = SYS_PATH + "manager.txt";

	    // Construct JSON Object
	    JSONObject json = new JSONObject();
	    json.put("id", manager.getId());
	    json.put("name", manager.getName());
	    json.put("phoneNumber", manager.getPhoneNumber());
	    json.put("address", manager.getAddress());
	    json.put("email", manager.getEmailAddress());

	    // Write JSON to text file
	    try (FileWriter file = new FileWriter(filePath, true)) {
	        file.write(json.toString() + System.lineSeparator());
	        System.out.println("Manager created successfully!");
	    } catch (IOException e) {
	        System.err.println("Error writing manager to file: " + e.getMessage());
	    }
	}
	
	// Method to create a delivery runner and save to a text file in JSON format
	public void createRunner(Runner runner) {

	    String filePath = SYS_PATH + "runner.txt";

	    // Construct JSON Object
	    JSONObject json = new JSONObject();
	    json.put("id", runner.getId());
	    json.put("name", runner.getName());
	    json.put("phoneNumber", runner.getPhoneNumber());
	    json.put("address", runner.getAddress());
	    json.put("email", runner.getEmailAddress());
	    json.put("plateNumber", runner.getPlateNumber());
	    json.put("tasks", runner.getTasks()); // List of task IDs
	    json.put("earnings", runner.getEarnings());
	    json.put("reviews", runner.getReviews()); // List of review IDs
	    json.put("lastDeliveredAddress", runner.getLastDeliveredAddress());
	    json.put("lastDeliveryDate", runner.getLastDeliveryDate().toString()); // Convert LocalDateTime to String

	    // Write JSON to text file
	    try (FileWriter file = new FileWriter(filePath, true)) {
	        file.write(json.toString() + System.lineSeparator());
	        System.out.println("Runner created successfully!");
	    } catch (IOException e) {
	        System.err.println("Error writing runner to file: " + e.getMessage());
	    }
	}
	
	// Method to create a vendor and save to a text file in JSON format
	public void createVendor(Vendor vendor) {

	    String filePath = SYS_PATH + "vendor.txt";

	    // Construct JSON Object
	    JSONObject json = new JSONObject();
	    json.put("id", vendor.getId());
	    json.put("name", vendor.getName());
	    json.put("phoneNumber", vendor.getPhoneNumber());
	    json.put("address", vendor.getAddress());
	    json.put("email", vendor.getEmailAddress());
	    json.put("vendorName", vendor.getVendorName());
	    json.put("items", vendor.getItems()); // List of item IDs
	    json.put("orderHistory", vendor.getOrderHistory()); // List of order IDs
	    json.put("reviews", vendor.getReviews()); // List of review IDs

	    // Write JSON to text file
	    try (FileWriter file = new FileWriter(filePath, true)) {
	        file.write(json.toString() + System.lineSeparator());
	        System.out.println("Vendor created successfully!");
	    } catch (IOException e) {
	        System.err.println("Error writing vendor to file: " + e.getMessage());
	    }
	}

}
