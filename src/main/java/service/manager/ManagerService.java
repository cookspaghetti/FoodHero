package service.manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import dto.ManagerDTO;
import enumeration.ResponseCode;
import enumeration.ServiceType;
import service.utils.IdGenerationUtils;

public class ManagerService {

	private static final String SYS_PATH = "src\\main\\resources\\database\\user\\";

	// Method to create a manager and save to a text file in JSON format
	public static ResponseCode createManager(ManagerDTO manager) {

		String filePath = SYS_PATH + "manager.txt";

		// Construct JSON Object
		JSONObject json = new JSONObject();
		json.put("id", IdGenerationUtils.getNextId(ServiceType.MANAGER, null, null));
		json.put("name", manager.getName());
		json.put("phoneNumber", manager.getPhoneNumber());
		json.put("addressId", manager.getAddressId());
		json.put("emailAddress", manager.getEmailAddress());
		json.put("password", manager.getPassword());
		json.put("status", manager.getStatus());

		// Write JSON to text file
		try (FileWriter file = new FileWriter(filePath, true)) {
			file.write(json.toString() + System.lineSeparator());
			System.out.println("Manager created successfully!");
			return ResponseCode.SUCCESS;
		} catch (IOException e) {
			System.err.println("Error writing manager to file: " + e.getMessage());
			return ResponseCode.IO_EXCEPTION;
		}
	}

	// Method to read manager info
	public static ManagerDTO readManager(String id) {

		String filePath = SYS_PATH + "manager.txt";

		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;

			// Read each line (JSON object) in the file
			while ((line = br.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				// Check if the ID matches
				if (json.getString("id").equals(id)) {
					// Construct Admin object
					ManagerDTO manager = new ManagerDTO();
					manager.setId(json.getString("id"));
					manager.setName(json.getString("name"));
					manager.setPhoneNumber(json.getString("phoneNumber"));
					manager.setAddressId(json.getString("addressId")); // Fixed key
					manager.setEmailAddress(json.getString("emailAddress")); // Fixed key
					manager.setPassword(json.getString("password")); // Added password
					manager.setStatus(json.getBoolean("status"));

					return manager; // Return the found admin
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading manager file: " + e.getMessage());
		}

		System.out.println("Manager with ID " + id + " not found.");
		return null; // Return null if not found
	}

	// Method to read all managers from the text file
	public static List<ManagerDTO> readAllManager() {
		String filePath = SYS_PATH + "manager.txt";
		List<ManagerDTO> managers = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = reader.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				ManagerDTO manager = new ManagerDTO();
				manager.setId(json.getString("id"));
				manager.setName(json.getString("name"));
				manager.setPhoneNumber(json.getString("phoneNumber"));
				manager.setAddressId(json.getString("addressId")); // Fixed key
				manager.setEmailAddress(json.getString("emailAddress")); // Fixed key
				manager.setPassword(json.getString("password")); // Added password
				manager.setStatus(json.getBoolean("status"));

				managers.add(manager); // Add to the list
			}
		} catch (IOException e) {
			System.err.println("Error reading managers from file: " + e.getMessage());
		} catch (JSONException e) {
			System.err.println("Error parsing JSON: " + e.getMessage());
		}

		return managers; // Return the list of admins
	}

	// Method to update manager
	public static ResponseCode updateManager(ManagerDTO updatedManager) {
		String filePath = SYS_PATH + "manager.txt";
		List<String> updatedLines = new ArrayList<>();
		boolean found = false;

		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = br.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				// Check if the current manager matches the ID
				if (json.getString("id").equals(updatedManager.getId())) {
					// Update JSON object with new manager data
					json.put("id", updatedManager.getId());
					json.put("name", updatedManager.getName());
					json.put("phoneNumber", updatedManager.getPhoneNumber());
					json.put("addressId", updatedManager.getAddressId());
					json.put("emailAddress", updatedManager.getEmailAddress());
					json.put("password", updatedManager.getPassword());
					json.put("status", updatedManager.getStatus());

					found = true;
				}

				// Add the (possibly updated) line to the list
				updatedLines.add(json.toString());
			}
		} catch (IOException e) {
			System.err.println("Error reading manager file: " + e.getMessage());
			return ResponseCode.IO_EXCEPTION;
		}

		// Write the updated content back to the file
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) {
			for (String updatedLine : updatedLines) {
				bw.write(updatedLine);
				bw.newLine();
			}
			if (found) {
				System.out.println("Manager with ID " + updatedManager.getId() + " updated successfully.");
				return ResponseCode.SUCCESS;
			} else {
				System.out.println("Manager with ID " + updatedManager.getId() + " not found.");
				return ResponseCode.RECORD_NOT_FOUND;
			}
		} catch (IOException e) {
			System.err.println("Error writing updated manager data: " + e.getMessage());
			return ResponseCode.IO_EXCEPTION;
		}
	}

	// Method to delete a manager
	public static ResponseCode deleteManager(String id) {

		String filePath = SYS_PATH + "manager.txt";

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
			System.err.println("Error reading manager file: " + e.getMessage());
			return ResponseCode.IO_EXCEPTION;
		}

		// Write the updated list back to the file
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) {
			for (String updatedLine : updatedLines) {
				bw.write(updatedLine);
				bw.newLine();
			}
			if (found) {
				System.out.println("Manager with ID " + id + " deleted successfully.");
				return ResponseCode.SUCCESS;
			} else {
				System.out.println("Manager with ID " + id + " not found.");
				return ResponseCode.RECORD_NOT_FOUND;
			}
		} catch (IOException e) {
			System.err.println("Error writing updated manager data: " + e.getMessage());
			return ResponseCode.IO_EXCEPTION;
		}
	}

}
