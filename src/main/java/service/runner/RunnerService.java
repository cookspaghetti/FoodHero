package service.runner;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.json.JSONArray;

import dto.RunnerDTO;
import service.utils.JsonUtils;

public class RunnerService {

	private static final String SYS_PATH = "/FoodHero/src/main/resources/database/";

	// Method to create a delivery runner and save to a text file in JSON format
	public void createRunner(RunnerDTO runner) {

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

	// Method to read runner info
	public RunnerDTO readRunner(String id) {
		String filePath = SYS_PATH + "runner.txt";

		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;

			// Read each line (JSON object) in the file
			while ((line = br.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				// Check if the ID matches
				if (json.getString("id").equals(id)) {
					// Construct DeliveryRunner object
					RunnerDTO runner = new RunnerDTO();
					runner.setId(json.getString("id"));
					runner.setName(json.getString("name"));
					runner.setPhoneNumber(json.getString("phoneNumber"));
					runner.setAddress(json.getString("address"));
					runner.setEmailAddress(json.getString("email"));
					runner.setPlateNumber(json.getString("plateNumber"));
					runner.setEarnings(json.getDouble("earnings"));

					// Convert JSON arrays to List<String>
					runner.setTasks(JsonUtils.jsonArrayToList(json.getJSONArray("tasks")));
					runner.setReviews(JsonUtils.jsonArrayToList(json.getJSONArray("reviews")));
					
					runner.setLastDeliveredAddress(json.getString("lastDeliveredAddress"));

					// Parse lastDeliveryDate as LocalDateTime
					String dateStr = json.optString("lastDeliveryDate", null);
					if (dateStr != null && !dateStr.isEmpty()) {
						runner.setLastDeliveryDate(LocalDateTime.parse(dateStr));
					}

					return runner; // Return the found runner
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading runner file: " + e.getMessage());
		}

		System.out.println("Runner with ID " + id + " not found.");
		return null; // Return null if not found
	}
	
	// Method to update runner
	public void updateRunner(RunnerDTO updatedRunner) {
	    String filePath = SYS_PATH + "runner.txt";
	    List<String> updatedLines = new ArrayList<>();
	    boolean found = false;

	    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
	        String line;

	        while ((line = br.readLine()) != null) {
	            JSONObject json = new JSONObject(line);

	            // Check if the current runner matches the ID
	            if (json.getString("id").equals(updatedRunner.getId())) {
	                // Update JSON object with new runner data
	                json.put("name", updatedRunner.getName());
	                json.put("phoneNumber", updatedRunner.getPhoneNumber());
	                json.put("address", updatedRunner.getAddress());
	                json.put("email", updatedRunner.getEmailAddress());
	                json.put("plateNumber", updatedRunner.getPlateNumber());
	                json.put("tasks", new JSONArray(updatedRunner.getTasks()));
	                json.put("earnings", updatedRunner.getEarnings());
	                json.put("reviews", new JSONArray(updatedRunner.getReviews()));
	                json.put("lastDeliveredAddress", updatedRunner.getLastDeliveredAddress());
	                json.put("lastDeliveryDate", 
	                         updatedRunner.getLastDeliveryDate() != null ? 
	                         updatedRunner.getLastDeliveryDate().toString() : "");

	                found = true;
	            }

	            // Add the (possibly updated) line to the list
	            updatedLines.add(json.toString());
	        }
	    } catch (IOException e) {
	        System.err.println("Error reading runner file: " + e.getMessage());
	        return;
	    }

	    // Write the updated content back to the file
	    try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) {
	        for (String updatedLine : updatedLines) {
	            bw.write(updatedLine);
	            bw.newLine();
	        }
	        if (found) {
	            System.out.println("Runner with ID " + updatedRunner.getId() + " updated successfully.");
	        } else {
	            System.out.println("Runner with ID " + updatedRunner.getId() + " not found.");
	        }
	    } catch (IOException e) {
	        System.err.println("Error writing updated runner data: " + e.getMessage());
	    }
	}
	
	// Method to delete a runner
	public void deleteRunner(String id) {
		
	    String filePath = SYS_PATH + "runner.txt";
	    
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
	        System.err.println("Error reading runner file: " + e.getMessage());
	        return;
	    }

	    // Write the updated list back to the file
	    try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) {
	        for (String updatedLine : updatedLines) {
	            bw.write(updatedLine);
	            bw.newLine();
	        }
	        if (found) {
	            System.out.println("Runner with ID " + id + " deleted successfully.");
	        } else {
	            System.out.println("Runner with ID " + id + " not found.");
	        }
	    } catch (IOException e) {
	        System.err.println("Error writing updated runner data: " + e.getMessage());
	    }
	}

}
