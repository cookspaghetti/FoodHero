package service.customer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import dto.CustomerDTO;
import service.utils.JsonUtils;

public class CustomerService {

	private static final String SYS_PATH = "/FoodHero/src/main/resources/database/";

	// Method to create an customer and save to a text file in JSON format
	public void createCustomer(CustomerDTO customer) {

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

	// Method to read customer info
	public CustomerDTO readCustomer(String id) {
		
		String filePath = SYS_PATH + "customer.txt";

		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;

			// Read each line (JSON object) in the file
			while ((line = br.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				// Check if the ID matches
				if (json.getString("id").equals(id)) {
					// Construct Customer object
					CustomerDTO customer = new CustomerDTO();
					customer.setId(json.getString("id"));
					customer.setName(json.getString("name"));
					customer.setPhoneNumber(json.getString("phoneNumber"));
					customer.setAddress(json.getString("address"));
					customer.setEmailAddress(json.getString("email"));
					customer.setCredit(json.getDouble("credit"));

					// Convert JSON arrays to List<String>
					customer.setOrderHistory(JsonUtils.jsonArrayToList(json.getJSONArray("orderHistory")));
					customer.setVendorReviews(JsonUtils.jsonArrayToList(json.getJSONArray("vendorReviews")));
					customer.setRunnerReviews(JsonUtils.jsonArrayToList(json.getJSONArray("runnerReviews")));
					customer.setComplains(JsonUtils.jsonArrayToList(json.getJSONArray("complains")));
					customer.setTransactions(JsonUtils.jsonArrayToList(json.getJSONArray("transactions")));
					customer.setDeliveryAddresses(JsonUtils.jsonArrayToList(json.getJSONArray("deliveryAddresses")));

					return customer; // Return the found customer
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading customer file: " + e.getMessage());
		}

		System.out.println("Customer with ID " + id + " not found.");
		return null; // Return null if not found
	}
	
	// Method to update customer
	public void updateCustomer(CustomerDTO updatedCustomer) {
		
	    String filePath = SYS_PATH + "customer.txt";
	    
	    List<String> updatedLines = new ArrayList<>();
	    boolean found = false;

	    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
	        String line;

	        while ((line = br.readLine()) != null) {
	            JSONObject json = new JSONObject(line);

	            // Check if the current customer matches the ID
	            if (json.getString("id").equals(updatedCustomer.getId())) {
	                // Update JSON object with new customer data
	                json.put("name", updatedCustomer.getName());
	                json.put("phoneNumber", updatedCustomer.getPhoneNumber());
	                json.put("address", updatedCustomer.getAddress());
	                json.put("email", updatedCustomer.getEmailAddress());
	                json.put("credit", updatedCustomer.getCredit());
	                json.put("orderHistory", updatedCustomer.getOrderHistory());
	                json.put("vendorReviews", updatedCustomer.getVendorReviews());
	                json.put("runnerReviews", updatedCustomer.getRunnerReviews());
	                json.put("complains", updatedCustomer.getComplains());
	                json.put("transactions", updatedCustomer.getTransactions());
	                json.put("deliveryAddresses", updatedCustomer.getDeliveryAddresses());

	                found = true;
	            }

	            // Add the updated line to the list
	            updatedLines.add(json.toString());
	        }
	    } catch (IOException e) {
	        System.err.println("Error reading customer file: " + e.getMessage());
	        return;
	    }

	    // Write the updated content back to the file
	    try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) {
	        for (String updatedLine : updatedLines) {
	            bw.write(updatedLine);
	            bw.newLine();
	        }
	        if (found) {
	            System.out.println("Customer with ID " + updatedCustomer.getId() + " updated successfully.");
	        } else {
	            System.out.println("Customer with ID " + updatedCustomer.getId() + " not found.");
	        }
	    } catch (IOException e) {
	        System.err.println("Error writing updated customer data: " + e.getMessage());
	    }
	}
	
	// Method to delete a customer
	public void deleteCustomer(String id) {
		
	    String filePath = SYS_PATH + "customer.txt";
	    
	    List<String> updatedLines = new ArrayList<>();
	    boolean found = false;

	    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
	        String line;

	        while ((line = br.readLine()) != null) {
	            JSONObject json = new JSONObject(line);

	            // Skip the line if the ID matches (deleting this record)
	            if (json.getString("id").equals(id)) {
	                found = true; // Mark as found
	                continue;     // Skip adding this record to the updated list
	            }
	            updatedLines.add(json.toString());
	        }
	    } catch (IOException e) {
	        System.err.println("Error reading customer file: " + e.getMessage());
	        return;
	    }

	    // Write the updated list back to the file
	    try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) {
	        for (String updatedLine : updatedLines) {
	            bw.write(updatedLine);
	            bw.newLine();
	        }
	        if (found) {
	            System.out.println("Customer with ID " + id + " deleted successfully.");
	        } else {
	            System.out.println("Customer with ID " + id + " not found.");
	        }
	    } catch (IOException e) {
	        System.err.println("Error writing updated customer data: " + e.getMessage());
	    }
	}

}
