package service.item;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import dto.ItemDTO;
import dto.VendorDTO;
import enumeration.ResponseCode;
import enumeration.Role;
import service.general.SessionControlService;
import service.vendor.VendorService;

public class ItemService {

	private static final String SYS_PATH = "src\\main\\resources\\database\\item\\";

	// Method to create an item and save it to a text file in JSON format
	public ResponseCode createItem(ItemDTO item) {

		// Get current user info
		Role role = SessionControlService.getRole();
		String email = SessionControlService.getEmailAddress();
		String vendorId;

		if (role != Role.VENDOR) {
			return ResponseCode.ACCESS_DENIED;
		} else {
			VendorDTO vendor = VendorService.readVendorByEmail(email);
			vendorId = vendor.getId();
		}

		// Construct the file name
		String filePath = SYS_PATH + "item_" + vendorId + ".txt";

		// Construct JSON Object
		JSONObject json = new JSONObject();
		json.put("id", item.getId());
		json.put("name", item.getName());
		json.put("price", item.getPrice());
		json.put("defaultAmount", item.getDefaultAmount());
		json.put("vendorId", item.getVendorId());
		json.put("description", item.getDescription());
		json.put("availability", item.isAvailability());

		// Write JSON to text file
		try (FileWriter file = new FileWriter(filePath, true)) {
			file.write(json.toString() + System.lineSeparator());
			System.out.println("Item created successfully!");
			return ResponseCode.SUCCESS;
		} catch (IOException e) {
			System.err.println("Error writing item to file: " + e.getMessage());
			return ResponseCode.IO_EXCEPTION;
		}
	}

	// Method to read item information from a text file
	public static ItemDTO readItem(String vendorId, String id) {

		// Construct the file name
		String filePath = SYS_PATH + "item_" + vendorId + ".txt";

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = reader.readLine()) != null) {
				JSONObject json = new JSONObject(line);
				if (json.getString("id").equals(id)) {
					// Create Item object from JSON data
					ItemDTO item = new ItemDTO();
					item.setId(json.getString("id"));
					item.setName(json.getString("name"));
					item.setPrice(json.getDouble("price"));
					item.setDefaultAmount(json.getInt("defaultAmount"));
					item.setVendorId(json.getString("vendorId"));
					item.setDescription(json.getString("description"));
					item.setAvailability(json.getBoolean("availability"));

					return item; // Return the found item
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading item from file: " + e.getMessage());
		} catch (JSONException e) {
			System.err.println("Error parsing JSON data: " + e.getMessage());
		}

		System.out.println("Item with ID " + id + " not found.");
		return null; // Return null if the item is not found
	}

	// Method to read items in an order 
	public static List<ItemDTO> readItemsInOrder(String vendorId, List<String> itemList) {

		List<ItemDTO> items = new ArrayList<>();

		for (String itemId : itemList) {
			ItemDTO item = readItem(vendorId, itemId);
			if (item != null) {
				items.add(item);
			}
		}

		return items;
	}

	// Method to read all items from the text file
	public List<ItemDTO> readAllItem(String vendorId) {

		// Construct the file name
		String filePath = SYS_PATH + "item_" + vendorId + ".txt";

		List<ItemDTO> items = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = reader.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				ItemDTO item = new ItemDTO();
				item.setId(json.getString("id"));
				item.setName(json.getString("name"));
				item.setPrice(json.getDouble("price"));
				item.setDefaultAmount(json.getInt("defaultAmount"));
				item.setVendorId(json.getString("vendorId"));
				item.setDescription(json.getString("description"));
				item.setAvailability(json.getBoolean("availability"));

				items.add(item); // Add to the list
			}
		} catch (IOException e) {
			System.err.println("Error reading items from file: " + e.getMessage());
		} catch (JSONException e) {
			System.err.println("Error parsing JSON: " + e.getMessage());
		}

		return items; // Return the list of items
	}

	// Method to update an item in the text file
	public ResponseCode updateItem(ItemDTO updatedItem) {

		// Get current user info
		Role role = SessionControlService.getRole();
		String email = SessionControlService.getEmailAddress();
		String vendorId;

		if (role != Role.VENDOR) {
			return ResponseCode.ACCESS_DENIED;
		} else {
			VendorDTO vendor = VendorService.readVendorByEmail(email);
			vendorId = vendor.getId();
		}

		// Construct the file name
		String filePath = SYS_PATH + "item_" + vendorId + ".txt";

		List<String> fileContent = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			// Read all lines and update the matching item
			while ((line = reader.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				if (json.getString("id").equals(updatedItem.getId())) {
					// Update the item's details
					json.put("name", updatedItem.getName());
					json.put("price", updatedItem.getPrice());
					json.put("defaultAmount", updatedItem.getDefaultAmount());
					json.put("vendorId", updatedItem.getVendorId());
					json.put("description", updatedItem.getDescription());
					json.put("availability", updatedItem.isAvailability());
				}
				fileContent.add(json.toString());
			}
		} catch (IOException e) {
			System.err.println("Error reading item file: " + e.getMessage());
			return ResponseCode.IO_EXCEPTION;
		} catch (JSONException e) {
			System.err.println("Error reading item file: " + e.getMessage());
			return ResponseCode.JSON_EXCEPTION;
		}

		// Write the updated content back to the file
		try (FileWriter writer = new FileWriter(filePath, false)) { // Overwrite file
			for (String content : fileContent) {
				writer.write(content + System.lineSeparator());
			}
			System.out.println("Item updated successfully!");
			return ResponseCode.SUCCESS;
		} catch (IOException e) {
			System.err.println("Error writing updated item to file: " + e.getMessage());
			return ResponseCode.IO_EXCEPTION;
		}
	}

	// Method to delete an item from the text file
	public static ResponseCode deleteItem(String vendorId, String itemId) {

		// Construct the file name
		String filePath = SYS_PATH + "item_" + vendorId + ".txt";

		List<String> fileContent = new ArrayList<>();
		boolean itemFound = false;

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			// Read all items and exclude the one to be deleted
			while ((line = reader.readLine()) != null) {
				JSONObject json = new JSONObject(line);
				if (!json.getString("id").equals(itemId)) {
					fileContent.add(json.toString());
				} else {
					itemFound = true; // Item to delete found
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading item file: " + e.getMessage());
			return ResponseCode.IO_EXCEPTION;
		} catch (JSONException e) {
			System.err.println("Error reading item file: " + e.getMessage());
			return ResponseCode.JSON_EXCEPTION;
		}

		if (!itemFound) {
			System.out.println("Item with ID " + itemId + " not found.");
			return ResponseCode.RECORD_NOT_FOUND;
		}

		// Write the updated content back to the file
		try (FileWriter writer = new FileWriter(filePath, false)) { // Overwrite file
			for (String content : fileContent) {
				writer.write(content + System.lineSeparator());
			}
			System.out.println("Item deleted successfully!");
			return ResponseCode.SUCCESS;
		} catch (IOException e) {
			System.err.println("Error writing to file after deletion: " + e.getMessage());
			return ResponseCode.IO_EXCEPTION;
		}
	}

	// Method to create data storage files for items
	public static ResponseCode createVendorItemDataStorage(String vendorId) {
		
		String itemsFilePath = SYS_PATH + "vendor_" + vendorId + "_items.txt";

		try {
			// Create empty files if they don't exist
			File itemsFile = new File(itemsFilePath);

			if (itemsFile.createNewFile()) {
				System.out.println("Item data storage created for vendor: " + vendorId);
				return ResponseCode.SUCCESS;
			} else {
				System.out.println("Item data storage already exists for vendor: " + vendorId);
				return ResponseCode.SUCCESS;
			}

		} catch (IOException e) {
			System.err.println("Error creating data storage for vendor " + vendorId + ": " + e.getMessage());
			return ResponseCode.IO_EXCEPTION;
		}
	}

}
