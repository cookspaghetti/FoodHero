package service.vendor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;
import org.json.JSONArray;

import dto.VendorDTO;
import service.utils.JsonUtils;

public class VendorService {

	private static final String SYS_PATH = "/FoodHero/src/main/resources/database/";

	// Method to create a vendor and save to a text file in JSON format
	public void createVendor(VendorDTO vendor) {

		String filePath = SYS_PATH + "vendor.txt";

		// Construct JSON Object
		JSONObject json = new JSONObject();
		json.put("id", vendor.getId());
		json.put("name", vendor.getName());
		json.put("phoneNumber", vendor.getPhoneNumber());
		json.put("address", vendor.getAddress());
		json.put("email", vendor.getEmailAddress());
		json.put("vendorName", vendor.getVendorName());
		json.put("items", new JSONObject(vendor.getItems()));
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
	
	// Method to read vendor info
	public VendorDTO readVendor(String id) {
	    String filePath = SYS_PATH + "vendor.txt";

	    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
	        String line;

	        // Read each line (JSON object) in the file
	        while ((line = br.readLine()) != null) {
	            JSONObject json = new JSONObject(line);

	            // Check if the ID matches
	            if (json.getString("id").equals(id)) {
	                // Construct Vendor object
	                VendorDTO vendor = new VendorDTO();
	                vendor.setId(json.getString("id"));
	                vendor.setName(json.getString("name"));
	                vendor.setPhoneNumber(json.getString("phoneNumber"));
	                vendor.setAddress(json.getString("address"));
	                vendor.setEmailAddress(json.getString("email"));
	                vendor.setVendorName(json.getString("vendorName"));

	                // Convert JSON Object (items) to HashMap<String, Integer>
	                HashMap<String, Integer> itemsMap = new HashMap<>();
	                JSONObject itemsJson = json.getJSONObject("items");
	                for (String key : itemsJson.keySet()) {
	                    itemsMap.put(key, itemsJson.getInt(key));
	                }
	                vendor.setItems(itemsMap);

	                // Convert JSON arrays to List<String>
	                vendor.setOrderHistory(JsonUtils.jsonArrayToList(json.getJSONArray("orderHistory")));
	                vendor.setReviews(JsonUtils.jsonArrayToList(json.getJSONArray("reviews")));

	                return vendor; // Return the found vendor
	            }
	        }
	    } catch (IOException e) {
	        System.err.println("Error reading vendor file: " + e.getMessage());
	    }

	    System.out.println("Vendor with ID " + id + " not found.");
	    return null; // Return null if not found
	}
	
	// Method to update vendor
	public void updateVendor(VendorDTO updatedVendor) {
	    String filePath = SYS_PATH + "vendor.txt";
	    List<String> updatedLines = new ArrayList<>();
	    boolean found = false;

	    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
	        String line;

	        while ((line = br.readLine()) != null) {
	            JSONObject json = new JSONObject(line);

	            // Check if the current vendor matches the ID
	            if (json.getString("id").equals(updatedVendor.getId())) {
	                // Update JSON object with new vendor data
	                json.put("name", updatedVendor.getName());
	                json.put("phoneNumber", updatedVendor.getPhoneNumber());
	                json.put("address", updatedVendor.getAddress());
	                json.put("email", updatedVendor.getEmailAddress());
	                json.put("vendorName", updatedVendor.getVendorName());
	                json.put("items", new JSONArray(updatedVendor.getItems()));
	                json.put("orderHistory", new JSONArray(updatedVendor.getOrderHistory()));
	                json.put("reviews", new JSONArray(updatedVendor.getReviews()));

	                found = true;
	            }

	            // Add the (possibly updated) line to the list
	            updatedLines.add(json.toString());
	        }
	    } catch (IOException e) {
	        System.err.println("Error reading vendor file: " + e.getMessage());
	        return;
	    }

	    // Write the updated content back to the file
	    try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) {
	        for (String updatedLine : updatedLines) {
	            bw.write(updatedLine);
	            bw.newLine();
	        }
	        if (found) {
	            System.out.println("Vendor with ID " + updatedVendor.getId() + " updated successfully.");
	        } else {
	            System.out.println("Vendor with ID " + updatedVendor.getId() + " not found.");
	        }
	    } catch (IOException e) {
	        System.err.println("Error writing updated vendor data: " + e.getMessage());
	    }
	}


}
