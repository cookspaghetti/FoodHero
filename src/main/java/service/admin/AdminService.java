package service.admin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import dto.AdminDTO;
import enumeration.ResponseCode;
import enumeration.ServiceType;
import service.general.SessionControlService;
import service.utils.IdGenerationUtils;

public class AdminService {

	private static final String SYS_PATH = "src\\main\\resources\\database\\user\\";

	// Method to create an admin and save to a text file in JSON format
	public static ResponseCode createAdmin(AdminDTO admin) {

		String filePath = SYS_PATH + "admin.txt";

		// Construct JSON Object
		JSONObject json = new JSONObject();
		json.put("id", IdGenerationUtils.getNextId(ServiceType.ADMIN, null, null));
	    json.put("name", admin.getName());
	    json.put("phoneNumber", admin.getPhoneNumber());
	    json.put("addressId", admin.getAddressId());
	    json.put("emailAddress", admin.getEmailAddress());
	    json.put("password", admin.getPassword());
	    json.put("status", admin.getStatus());

		// Write JSON to text file
		try (FileWriter file = new FileWriter(filePath, true)) {
			file.write(json.toString() + System.lineSeparator());
			System.out.println("Admin created successfully!");
			return ResponseCode.SUCCESS;
		} catch (IOException e) {
			System.err.println("Error writing admin to file: " + e.getMessage());
			return ResponseCode.IO_EXCEPTION;
		}
	}

	// Method to read admin info
	public static AdminDTO readAdmin(String id) {

		String filePath = SYS_PATH + "admin.txt";

		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;

			// Read each line (JSON object) in the file
			while ((line = br.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				// Check if the ID matches
				if (json.getString("id").equals(id)) {
					// Construct Admin object
					AdminDTO admin = new AdminDTO();
					admin.setId(json.getString("id"));
	                admin.setName(json.getString("name"));
	                admin.setPhoneNumber(json.getString("phoneNumber"));
	                admin.setAddressId(json.getString("addressId")); // Fixed key
	                admin.setEmailAddress(json.getString("emailAddress")); // Fixed key
	                admin.setPassword(json.getString("password")); // Added password
	                admin.setStatus(json.getBoolean("status"));

					return admin; // Return the found admin
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading admin file: " + e.getMessage());
		}

		System.out.println("Admin with ID " + id + " not found.");
		return null; // Return null if not found
	}

	// Method to read all admins from the text file
	public static List<AdminDTO> readAllAdmin() {
		String filePath = SYS_PATH + "admin.txt";
		List<AdminDTO> admins = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = reader.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				AdminDTO admin = new AdminDTO();
				admin.setId(json.getString("id"));
                admin.setName(json.getString("name"));
                admin.setPhoneNumber(json.getString("phoneNumber"));
                admin.setAddressId(json.getString("addressId")); // Fixed key
                admin.setEmailAddress(json.getString("emailAddress")); // Fixed key
                admin.setPassword(json.getString("password")); // Added password
                admin.setStatus(json.getBoolean("status"));

				admins.add(admin); // Add to the list
			}
		} catch (IOException e) {
			System.err.println("Error reading admins from file: " + e.getMessage());
		} catch (JSONException e) {
			System.err.println("Error parsing JSON: " + e.getMessage());
		}

		return admins; // Return the list of admins
	}

	// Method to update admin
	public static ResponseCode updateAdmin(AdminDTO updatedAdmin) {

		String filePath = SYS_PATH + "admin.txt";

		List<String> updatedLines = new ArrayList<>();
		boolean found = false;

		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = br.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				// Check if the current admin matches the ID
				if (json.getString("id").equals(updatedAdmin.getId())) {
					// Update JSON object with new admin data
					json.put("name", updatedAdmin.getName());
	                json.put("phoneNumber", updatedAdmin.getPhoneNumber());
	                json.put("addressId", updatedAdmin.getAddressId()); // Fixed key
	                json.put("emailAddress", updatedAdmin.getEmailAddress()); // Fixed key
	                json.put("password", updatedAdmin.getPassword()); // Added password
	                json.put("status", updatedAdmin.getStatus()); // Added status

					found = true;
				}

				// Add the updated line to the list
				updatedLines.add(json.toString());
			}
		} catch (IOException e) {
			System.err.println("Error reading admin file: " + e.getMessage());
			return ResponseCode.IO_EXCEPTION;
		}

		// Write the updated content back to the file
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) {
			for (String updatedLine : updatedLines) {
				bw.write(updatedLine);
				bw.newLine();
			}
			if (found) {
				System.out.println("Admin with ID " + updatedAdmin.getId() + " updated successfully.");
				return ResponseCode.SUCCESS;
			} else {
				System.out.println("Admin with ID " + updatedAdmin.getId() + " not found.");
				return ResponseCode.RECORD_NOT_FOUND;
			}
		} catch (IOException e) {
			System.err.println("Error writing updated admin data: " + e.getMessage());
			return ResponseCode.IO_EXCEPTION;
		}
	}

	// Method to delete an admin
	public static ResponseCode deleteAdmin(String id) {

		if (id == SessionControlService.getId()) {
			System.out.println("Cannot delete currently logged in admin.");
			return ResponseCode.ACCESS_DENIED;
		}

		String filePath = SYS_PATH + "admin.txt";

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
			System.err.println("Error reading admin file: " + e.getMessage());
			return ResponseCode.IO_EXCEPTION;
		}

		// Write the updated list back to the file
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) {
			for (String updatedLine : updatedLines) {
				bw.write(updatedLine);
				bw.newLine();
			}
			if (found) {
				System.out.println("Admin with ID " + id + " deleted successfully.");
				return ResponseCode.SUCCESS;
			} else {
				System.out.println("Admin with ID " + id + " not found.");
				return ResponseCode.RECORD_NOT_FOUND;
			}
		} catch (IOException e) {
			System.err.println("Error writing updated admin data: " + e.getMessage());
			return ResponseCode.IO_EXCEPTION;
		}
	}

	// Check duplicate phone number
	public static boolean checkDuplicatePhone(String phoneNumber) {
		String filePath = SYS_PATH + "admin.txt";

		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = br.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				if (json.getString("phoneNumber").equals(phoneNumber)) {
					return true;
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading admin file: " + e.getMessage());
		}

		return false;
	}

	// Check duplicate email address
	public static boolean checkDuplicateEmail(String emailAddress) {
		String filePath = SYS_PATH + "admin.txt";

		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = br.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				if (json.getString("emailAddress").equals(emailAddress)) {
					return true;
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading admin file: " + e.getMessage());
		}

		return false;
	}
}
