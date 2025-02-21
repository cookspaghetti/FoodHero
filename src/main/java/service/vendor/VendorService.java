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
import org.json.JSONException;

import dto.VendorDTO;
import enumeration.ResponseCode;
import enumeration.VendorType;
import service.item.ItemService;
import service.review.ReviewService;
import service.utils.JsonUtils;

public class VendorService {

	private static final String SYS_PATH = "src\\main\\resources\\database\\user\\";

	// Method to create a vendor and save to a text file in JSON format
	public ResponseCode createVendor(VendorDTO vendor) {

		String filePath = SYS_PATH + "vendor.txt";

		// Construct JSON Object
		JSONObject json = new JSONObject();
		json.put("id", vendor.getId());
	    json.put("name", vendor.getName());
	    json.put("phoneNumber", vendor.getPhoneNumber());
	    json.put("addressId", vendor.getAddressId());                    
	    json.put("emailAddress", vendor.getEmailAddress());              
	    json.put("password", vendor.getPassword());                       
	    json.put("status", vendor.getStatus());
	    json.put("vendorName", vendor.getVendorName());
	    json.put("vendorType", vendor.getVendorType());
	    json.put("items", new JSONObject(vendor.getItems()));             
	    json.put("orderHistory", new JSONArray(vendor.getOrderHistory()));
	    json.put("ratings", vendor.getRatings());
	    json.put("reviews", new JSONArray(vendor.getReviews()));  
	    json.put("open", vendor.getOpen());

		// Write JSON to text file
		try (FileWriter file = new FileWriter(filePath, true)) {
			file.write(json.toString() + System.lineSeparator());
			System.out.println("Vendor created successfully!");
		} catch (IOException e) {
			System.err.println("Error writing vendor to file: " + e.getMessage());
			return ResponseCode.IO_EXCEPTION;
		} catch (Exception e) {
	        System.err.println("Unknown error occurred: " + e.getMessage());
	        return ResponseCode.UNKNOWN_EXCEPTION;
	    }
		
		// create item and review data storage
		ResponseCode createItemDataStorageResponse = ItemService.createVendorItemDataStorage(vendor.getId());
		if (createItemDataStorageResponse != ResponseCode.SUCCESS) {
			return createItemDataStorageResponse;
		}
		
		ResponseCode createReviewDataStorageResponse = ReviewService.createVendorReviewDataStorage(vendor.getId());
		if (ReviewService.createVendorReviewDataStorage(vendor.getId()) != ResponseCode.SUCCESS) {
			return createReviewDataStorageResponse;
		}
		
		return ResponseCode.SUCCESS;
	}

	// Method to read vendor info
	public static VendorDTO readVendor(String id) {
		
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
					vendor.setAddressId(json.getString("addressId"));
					vendor.setEmailAddress(json.getString("email"));
					vendor.setStatus(json.getBoolean("status"));
					vendor.setVendorName(json.getString("vendorName"));
					vendor.setVendorType(VendorType.valueOf(json.getString("vendorType")));
					vendor.setRatings(json.getDouble("ratings"));
					vendor.setPassword(json.getString("password"));
					vendor.setOpen(json.getBoolean("open"));

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

	// Method to read vendor info
	public static VendorDTO readVendorByEmail(String email) {
		
		String filePath = SYS_PATH + "vendor.txt";

		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;

			// Read each line (JSON object) in the file
			while ((line = br.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				// Check if the ID matches
				if (json.getString("email").equals(email)) {
					// Construct Vendor object
					VendorDTO vendor = new VendorDTO();
					vendor.setId(json.getString("id"));
					vendor.setName(json.getString("name"));
					vendor.setPhoneNumber(json.getString("phoneNumber"));
					vendor.setAddressId(json.getString("addressId"));
					vendor.setEmailAddress(json.getString("email"));
					vendor.setStatus(json.getBoolean("status"));
					vendor.setVendorName(json.getString("vendorName"));
					vendor.setVendorType(VendorType.valueOf(json.getString("vendorType")));
					vendor.setRatings(json.getDouble("ratings"));
					vendor.setPassword(json.getString("password"));
					vendor.setOpen(json.getBoolean("open"));
					
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

		System.out.println("Vendor with email " + email + " not found.");
		return null; // Return null if not found
	}

	// Method to read all vendors from the text file
	public static List<VendorDTO> readAllVendor() {
		
		String filePath = SYS_PATH + "vendor.txt";
		
		List<VendorDTO> vendors = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = reader.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				VendorDTO vendor = new VendorDTO();
				vendor.setId(json.getString("id"));
				vendor.setName(json.getString("name"));
				vendor.setPhoneNumber(json.getString("phoneNumber"));
				vendor.setAddressId(json.getString("addressId"));
				vendor.setEmailAddress(json.getString("email"));
				vendor.setStatus(json.getBoolean("status"));
				vendor.setVendorName(json.getString("vendorName"));
				vendor.setVendorType(VendorType.valueOf(json.getString("vendorType")));
				vendor.setRatings(json.getDouble("ratings"));
				vendor.setPassword(json.getString("password"));
				vendor.setOpen(json.getBoolean("open"));

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

				vendors.add(vendor); // Add to the list
			}
		} catch (IOException e) {
			System.err.println("Error reading vendors from file: " + e.getMessage());
		} catch (JSONException e) {
			System.err.println("Error parsing JSON: " + e.getMessage());
		}

		return vendors; // Return the list of vendors
	}

	// Method to update vendor
	public ResponseCode updateVendor(VendorDTO updatedVendor) {

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
					json.put("id", updatedVendor.getId());
				    json.put("name", updatedVendor.getName());
				    json.put("phoneNumber", updatedVendor.getPhoneNumber());
				    json.put("addressId", updatedVendor.getAddressId());                      // Fixed field name
				    json.put("emailAddress", updatedVendor.getEmailAddress());               // Fixed field name
				    json.put("password", updatedVendor.getPassword());                       // Added password
				    json.put("status", updatedVendor.getStatus());
				    json.put("vendorName", updatedVendor.getVendorName());
				    json.put("vendorType", updatedVendor.getVendorType());
				    json.put("items", new JSONObject(updatedVendor.getItems()));             // Convert HashMap to JSONObject
				    json.put("orderHistory", new JSONArray(updatedVendor.getOrderHistory())); // Correct JSONArray conversion
				    json.put("ratings", updatedVendor.getRatings());
				    json.put("reviews", new JSONArray(updatedVendor.getReviews()));
				    json.put("open", updatedVendor.getOpen());

					found = true;
				}

				// Add the (possibly updated) line to the list
				updatedLines.add(json.toString());
			}
		} catch (IOException e) {
			System.err.println("Error reading vendor file: " + e.getMessage());
			return ResponseCode.IO_EXCEPTION;
		}

		// Write the updated content back to the file
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) {
			for (String updatedLine : updatedLines) {
				bw.write(updatedLine);
				bw.newLine();
			}
			if (found) {
				System.out.println("Vendor with ID " + updatedVendor.getId() + " updated successfully.");
				return ResponseCode.SUCCESS;
			} else {
				System.out.println("Vendor with ID " + updatedVendor.getId() + " not found.");
				return ResponseCode.RECORD_NOT_FOUND;
			}
		} catch (IOException e) {
			System.err.println("Error writing updated vendor data: " + e.getMessage());
			return ResponseCode.IO_EXCEPTION;
		} catch (Exception e) {
	        System.err.println("Unknown error occurred: " + e.getMessage());
	        return ResponseCode.UNKNOWN_EXCEPTION;
	    }
	}

	// Method to delete a vendor
	public static ResponseCode deleteVendor(String id) {
	    String filePath = SYS_PATH + "vendor.txt";
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
	        System.err.println("Error reading vendor file: " + e.getMessage());
	        return ResponseCode.IO_EXCEPTION;
	    } catch (JSONException e) {
	        System.err.println("Error parsing JSON: " + e.getMessage());
	        return ResponseCode.JSON_EXCEPTION;
	    }

	    // If the vendor wasn't found, return the appropriate response code
	    if (!found) {
	        System.out.println("Vendor with ID " + id + " not found.");
	        return ResponseCode.RECORD_NOT_FOUND;
	    }

	    // Write the updated list back to the file
	    try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) {
	        for (String updatedLine : updatedLines) {
	            bw.write(updatedLine);
	            bw.newLine();
	        }
	        System.out.println("Vendor with ID " + id + " deleted successfully.");
	        return ResponseCode.SUCCESS;
	    } catch (IOException e) {
	        System.err.println("Error writing updated vendor data: " + e.getMessage());
	        return ResponseCode.IO_EXCEPTION;
	    } catch (Exception e) {
	        System.err.println("Unknown error occurred: " + e.getMessage());
	        return ResponseCode.UNKNOWN_EXCEPTION;
	    }
	}

}
