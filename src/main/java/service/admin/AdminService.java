package service.admin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import dto.AdminDTO;

public class AdminService {

	private static final String SYS_PATH = "/FoodHero/src/main/resources/database/";

	// Method to create an admin and save to a text file in JSON format
	public void createAdmin(AdminDTO admin) {

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

	// Method to read admin info
	public AdminDTO readAdmin(String id) {

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
					admin.setAddress(json.getString("address"));
					admin.setEmailAddress(json.getString("email"));

					return admin; // Return the found admin
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading admin file: " + e.getMessage());
		}

		System.out.println("Admin with ID " + id + " not found.");
		return null; // Return null if not found
	}

	// Method to update admin
	public void updateAdmin(AdminDTO updatedAdmin) {
		
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
					json.put("address", updatedAdmin.getAddress());
					json.put("email", updatedAdmin.getEmailAddress());

					found = true;
				}

				// Add the updated line to the list
				updatedLines.add(json.toString());
			}
		} catch (IOException e) {
			System.err.println("Error reading admin file: " + e.getMessage());
			return;
		}

		// Write the updated content back to the file
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) {
			for (String updatedLine : updatedLines) {
				bw.write(updatedLine);
				bw.newLine();
			}
			if (found) {
				System.out.println("Admin with ID " + updatedAdmin.getId() + " updated successfully.");
			} else {
				System.out.println("Admin with ID " + updatedAdmin.getId() + " not found.");
			}
		} catch (IOException e) {
			System.err.println("Error writing updated admin data: " + e.getMessage());
		}
	}
	
	// Method to delete an admin
	public void deleteAdmin(String id) {
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
	        return;
	    }

	    // Write the updated list back to the file
	    try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) {
	        for (String updatedLine : updatedLines) {
	            bw.write(updatedLine);
	            bw.newLine();
	        }
	        if (found) {
	            System.out.println("Admin with ID " + id + " deleted successfully.");
	        } else {
	            System.out.println("Admin with ID " + id + " not found.");
	        }
	    } catch (IOException e) {
	        System.err.println("Error writing updated admin data: " + e.getMessage());
	    }
	}


}
