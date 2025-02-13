package service.customer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import dto.CustomerDTO;
import enumeration.ResponseCode;
import service.utils.JsonUtils;

public class CustomerService {

	private static final String SYS_PATH = "src\\main\\resources\\database\\user\\";

	// Method to create an customer and save to a text file in JSON format
	public ResponseCode createCustomer(CustomerDTO customer) {

		String filePath = SYS_PATH + "customer.txt";

		// Construct JSON Object
		JSONObject json = new JSONObject();
		json.put("id", customer.getId());
		json.put("name", customer.getName());
		json.put("phoneNumber", customer.getPhoneNumber());
		json.put("addressId", customer.getAddressId());                // Fixed field name
		json.put("emailAddress", customer.getEmailAddress());          // Fixed field name
		json.put("password", customer.getPassword());                  // Added password
		json.put("status", customer.getStatus());                      // Added status
		json.put("credit", customer.getCredit());

		// Add Lists of IDs
		json.put("orderHistory", customer.getOrderHistory() != null ? new JSONArray(customer.getOrderHistory()) : new JSONArray());
		json.put("vendorReviews", customer.getVendorReviews() != null ? new JSONArray(customer.getVendorReviews()) : new JSONArray());
		json.put("runnerReviews", customer.getRunnerReviews() != null ? new JSONArray(customer.getRunnerReviews()) : new JSONArray());
		json.put("complains", customer.getComplains() != null ? new JSONArray(customer.getComplains()) : new JSONArray());
		json.put("transactions", customer.getTransactions() != null ? new JSONArray(customer.getTransactions()) : new JSONArray());
		json.put("deliveryAddresses", customer.getDeliveryAddresses() != null ? new JSONArray(customer.getDeliveryAddresses()) : new JSONArray());

		// Write JSON to text file
		try (FileWriter file = new FileWriter(filePath, true)) {
			file.write(json.toString() + System.lineSeparator());
			System.out.println("Customer created successfully!");
			return ResponseCode.SUCCESS;
		} catch (IOException e) {
			System.err.println("Error writing customer to file: " + e.getMessage());
			return ResponseCode.IO_EXCEPTION;
		}
	}

	// Method to read customer info
	public static CustomerDTO readCustomer(String id) {

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
					customer.setAddressId(json.optString("addressId", ""));            // Fixed field name
					customer.setEmailAddress(json.optString("emailAddress", ""));      // Fixed field name
					customer.setPassword(json.optString("password", ""));              // Added password
					customer.setStatus(json.optBoolean("status", true));               // Added status with default true
					customer.setCredit(json.optDouble("credit", 0.0));

					// Convert JSON arrays to List<String> with null safety
					customer.setOrderHistory(JsonUtils.jsonArrayToList(json.optJSONArray("orderHistory")));
					customer.setVendorReviews(JsonUtils.jsonArrayToList(json.optJSONArray("vendorReviews")));
					customer.setRunnerReviews(JsonUtils.jsonArrayToList(json.optJSONArray("runnerReviews")));
					customer.setComplains(JsonUtils.jsonArrayToList(json.optJSONArray("complains")));
					customer.setTransactions(JsonUtils.jsonArrayToList(json.optJSONArray("transactions")));
					customer.setDeliveryAddresses(JsonUtils.jsonArrayToList(json.optJSONArray("deliveryAddresses")));

					return customer; // Return the found customer
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading customer file: " + e.getMessage());
		}

		System.out.println("Customer with ID " + id + " not found.");
		return null; // Return null if not found
	}

	// Method to read all customers from the text file
	public List<CustomerDTO> readAllCustomer() {
		String filePath = SYS_PATH + "customer.txt";
		List<CustomerDTO> customers = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = reader.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				CustomerDTO customer = new CustomerDTO();
				customer.setId(json.getString("id"));
				customer.setName(json.getString("name"));
				customer.setPhoneNumber(json.getString("phoneNumber"));
				customer.setAddressId(json.optString("addressId", ""));            // Fixed field name
				customer.setEmailAddress(json.optString("emailAddress", ""));      // Fixed field name
				customer.setPassword(json.optString("password", ""));              // Added password
				customer.setStatus(json.optBoolean("status", true));               // Added status with default true
				customer.setCredit(json.optDouble("credit", 0.0));

				// Convert JSON arrays to List<String> with null safety
				customer.setOrderHistory(JsonUtils.jsonArrayToList(json.optJSONArray("orderHistory")));
				customer.setVendorReviews(JsonUtils.jsonArrayToList(json.optJSONArray("vendorReviews")));
				customer.setRunnerReviews(JsonUtils.jsonArrayToList(json.optJSONArray("runnerReviews")));
				customer.setComplains(JsonUtils.jsonArrayToList(json.optJSONArray("complains")));
				customer.setTransactions(JsonUtils.jsonArrayToList(json.optJSONArray("transactions")));
				customer.setDeliveryAddresses(JsonUtils.jsonArrayToList(json.optJSONArray("deliveryAddresses")));
				customers.add(customer); // Add to the list
			}
		} catch (IOException e) {
			System.err.println("Error reading customers from file: " + e.getMessage());
		} catch (JSONException e) {
			System.err.println("Error parsing JSON: " + e.getMessage());
		}

		return customers; // Return the list of customers
	}

	// Method to update customer
	public ResponseCode updateCustomer(CustomerDTO updatedCustomer) {

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
					json.put("addressId", updatedCustomer.getAddressId());                // Fixed field name
					json.put("emailAddress", updatedCustomer.getEmailAddress());          // Fixed field name
					json.put("password", updatedCustomer.getPassword());                  // Added password
					json.put("status", updatedCustomer.getStatus());                       // Added status
					json.put("credit", updatedCustomer.getCredit());

					// Convert Lists to JSONArrays
					json.put("orderHistory", new JSONArray(updatedCustomer.getOrderHistory()));
					json.put("vendorReviews", new JSONArray(updatedCustomer.getVendorReviews()));
					json.put("runnerReviews", new JSONArray(updatedCustomer.getRunnerReviews()));
					json.put("complains", new JSONArray(updatedCustomer.getComplains()));
					json.put("transactions", new JSONArray(updatedCustomer.getTransactions()));
					json.put("deliveryAddresses", new JSONArray(updatedCustomer.getDeliveryAddresses()));

					found = true;
				}

				// Add the updated line to the list
				updatedLines.add(json.toString());
			}
		} catch (IOException e) {
			System.err.println("Error reading customer file: " + e.getMessage());
			return ResponseCode.IO_EXCEPTION;
		}

		// Write the updated content back to the file
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) {
			for (String updatedLine : updatedLines) {
				bw.write(updatedLine);
				bw.newLine();
			}
			if (found) {
				System.out.println("Customer with ID " + updatedCustomer.getId() + " updated successfully.");
				return ResponseCode.SUCCESS;
			} else {
				System.out.println("Customer with ID " + updatedCustomer.getId() + " not found.");
				return ResponseCode.RECORD_NOT_FOUND;
			}
		} catch (IOException e) {
			System.err.println("Error writing updated customer data: " + e.getMessage());
			return ResponseCode.IO_EXCEPTION;
		}
	}

	// Method to delete a customer
	public static ResponseCode deleteCustomer(String id) {

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
			return ResponseCode.IO_EXCEPTION;
		}

		// Write the updated list back to the file
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) {
			for (String updatedLine : updatedLines) {
				bw.write(updatedLine);
				bw.newLine();
			}
			if (found) {
				System.out.println("Customer with ID " + id + " deleted successfully.");
				return ResponseCode.SUCCESS;
			} else {
				System.out.println("Customer with ID " + id + " not found.");
				return ResponseCode.RECORD_NOT_FOUND;
			}
		} catch (IOException e) {
			System.err.println("Error writing updated customer data: " + e.getMessage());
			return ResponseCode.IO_EXCEPTION;
		}
	}

}
