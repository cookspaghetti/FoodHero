package service.complain;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import dto.ComplainDTO;
import enumeration.ComplainStatus;

public class ComplainService {

	private static final String SYS_PATH = "/FoodHero/src/main/resources/database/";

	// Method to create a complain and save it to a text file in JSON format
	public void createComplain(ComplainDTO complain) {
		String filePath = SYS_PATH + "complain.txt";

		// Construct JSON Object
		JSONObject json = new JSONObject();
		json.put("id", complain.getId());
		json.put("customerId", complain.getCustomerId());
		json.put("orderId", complain.getOrderId());
		json.put("description", complain.getDescription());
		json.put("status", complain.getStatus());
		json.put("solution", complain.getSolution());

		// Write JSON to text file
		try (FileWriter file = new FileWriter(filePath, true)) {
			file.write(json.toString() + System.lineSeparator());
			System.out.println("Complain created successfully!");
		} catch (IOException e) {
			System.err.println("Error writing complain to file: " + e.getMessage());
		}
	}

	// Method to read a complain from the text file
	public ComplainDTO readComplain(String id) {
		String filePath = SYS_PATH + "complain.txt";

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = reader.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				if (json.getString("id").equals(id)) {
					ComplainDTO complain = new ComplainDTO();
					complain.setId(json.getString("id"));
					complain.setCustomerId(json.getString("customerId"));
					complain.setOrderId(json.getString("orderId"));
					complain.setDescription(json.getString("description"));
					complain.setStatus(ComplainStatus.valueOf(json.getString("status")));
					complain.setSolution(json.getString("solution"));

					return complain; // Complain found, return it
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading complain from file: " + e.getMessage());
		} catch (JSONException e) {
			System.err.println("Error parsing JSON: " + e.getMessage());
		}

		System.out.println("Complain with ID " + id + " not found.");
		return null; // If no matching complain is found
	}
	
	// Method to read all complaints from the text file
    public List<ComplainDTO> readAllComplain() {
        String filePath = SYS_PATH + "complain.txt";
        List<ComplainDTO> complaints = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                JSONObject json = new JSONObject(line);

                ComplainDTO complain = new ComplainDTO();
                complain.setId(json.getString("id"));
                complain.setCustomerId(json.getString("customerId"));
                complain.setOrderId(json.getString("orderId"));
                complain.setDescription(json.getString("description"));
                complain.setStatus(ComplainStatus.valueOf(json.getString("status")));
                complain.setSolution(json.getString("solution"));

                complaints.add(complain); // Add to the list
            }
        } catch (IOException e) {
            System.err.println("Error reading complaints from file: " + e.getMessage());
        } catch (JSONException e) {
            System.err.println("Error parsing JSON: " + e.getMessage());
        }

        return complaints; // Return the list of complaints
    }

	// Method to delete a complain from the text file
	public void deleteComplain(String id) {
		String filePath = SYS_PATH + "complain.txt";
		List<String> updatedData = new ArrayList<>();
		boolean found = false;

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = reader.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				if (json.getString("id").equals(id)) {
					found = true; // Mark the complain as found
					System.out.println("Complain with ID " + id + " deleted successfully.");
					continue; // Skip adding this line to the updatedData list
				}
				updatedData.add(line); // Keep other complains
			}
		} catch (IOException e) {
			System.err.println("Error reading complain from file: " + e.getMessage());
		} catch (JSONException e) {
			System.err.println("Error parsing JSON: " + e.getMessage());
		}

		if (found) {
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
				for (String updatedLine : updatedData) {
					writer.write(updatedLine);
					writer.newLine();
				}
			} catch (IOException e) {
				System.err.println("Error writing updated complain data to file: " + e.getMessage());
			}
		} else {
			System.out.println("Complain with ID " + id + " not found.");
		}
	}
}
