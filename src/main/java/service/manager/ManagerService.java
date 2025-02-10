package service.manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import dto.ManagerDTO;

public class ManagerService {

	private static final String SYS_PATH = "/FoodHero/src/main/resources/database/";

	// Method to create a manager and save to a text file in JSON format
	public void createManager(ManagerDTO manager) {

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

	// Method to read manager info
	public ManagerDTO readAdmin(String id) {

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
					manager.setAddress(json.getString("address"));
					manager.setEmailAddress(json.getString("email"));

					return manager; // Return the found admin
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading manager file: " + e.getMessage());
		}

		System.out.println("Manager with ID " + id + " not found.");
		return null; // Return null if not found
	}
	
	// Method to update manager
	public void updateManager(ManagerDTO updatedManager) {
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
	                json.put("name", updatedManager.getName());
	                json.put("phoneNumber", updatedManager.getPhoneNumber());
	                json.put("address", updatedManager.getAddress());
	                json.put("email", updatedManager.getEmailAddress());

	                found = true;
	            }

	            // Add the (possibly updated) line to the list
	            updatedLines.add(json.toString());
	        }
	    } catch (IOException e) {
	        System.err.println("Error reading manager file: " + e.getMessage());
	        return;
	    }

	    // Write the updated content back to the file
	    try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) {
	        for (String updatedLine : updatedLines) {
	            bw.write(updatedLine);
	            bw.newLine();
	        }
	        if (found) {
	            System.out.println("Manager with ID " + updatedManager.getId() + " updated successfully.");
	        } else {
	            System.out.println("Manager with ID " + updatedManager.getId() + " not found.");
	        }
	    } catch (IOException e) {
	        System.err.println("Error writing updated manager data: " + e.getMessage());
	    }
	}
	
	// Method to delete a manager
	public void deleteManager(String id) {
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
	        return;
	    }

	    // Write the updated list back to the file
	    try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) {
	        for (String updatedLine : updatedLines) {
	            bw.write(updatedLine);
	            bw.newLine();
	        }
	        if (found) {
	            System.out.println("Manager with ID " + id + " deleted successfully.");
	        } else {
	            System.out.println("Manager with ID " + id + " not found.");
	        }
	    } catch (IOException e) {
	        System.err.println("Error writing updated manager data: " + e.getMessage());
	    }
	}

}
