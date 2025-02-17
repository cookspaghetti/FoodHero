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

import dto.ComplaintDTO;
import enumeration.ComplaintStatus;
import enumeration.ResponseCode;

public class ComplaintService {

	private static final String SYS_PATH = "src\\main\\resources\\database\\complain\\";

	// Method to create a complain and save it to a text file in JSON format
	public void createComplaint(ComplaintDTO complain) {
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
	public static ComplaintDTO readComplaint(String id) {
		String filePath = SYS_PATH + "complain.txt";

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = reader.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				if (json.getString("id").equals(id)) {
					ComplaintDTO complain = new ComplaintDTO();
					complain.setId(json.getString("id"));
					complain.setCustomerId(json.getString("customerId"));
					complain.setOrderId(json.getString("orderId"));
					complain.setDescription(json.getString("description"));
					complain.setStatus(ComplaintStatus.valueOf(json.getString("status")));
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
	public List<ComplaintDTO> readAllComplaint() {
		String filePath = SYS_PATH + "complain.txt";
		List<ComplaintDTO> complaints = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = reader.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				ComplaintDTO complain = new ComplaintDTO();
				complain.setId(json.getString("id"));
				complain.setCustomerId(json.getString("customerId"));
				complain.setOrderId(json.getString("orderId"));
				complain.setDescription(json.getString("description"));
				complain.setStatus(ComplaintStatus.valueOf(json.getString("status")));
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

	// Method to update complain
	public ResponseCode updateComplaint(ComplaintDTO updatedComplain) {
		String filePath = SYS_PATH + "complain.txt";
		List<String> updatedLines = new ArrayList<>();
		boolean found = false;

		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = br.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				// Check if the current complain matches the ID
				if (json.getString("id").equals(updatedComplain.getId())) {
					// Update JSON object with new Complain data
					json.put("id", updatedComplain.getId());
					json.put("customerId", updatedComplain.getCustomerId());
					json.put("orderId", updatedComplain.getOrderId());
					json.put("description", updatedComplain.getDescription());
					json.put("status", updatedComplain.getStatus());
					json.put("solution", updatedComplain.getSolution());

					found = true;
				}

				// Add the (possibly updated) line to the list
				updatedLines.add(json.toString());
			}
		} catch (IOException e) {
			System.err.println("Error reading complain file: " + e.getMessage());
			return ResponseCode.IO_EXCEPTION;
		}

		// Write the updated content back to the file
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) {
			for (String updatedLine : updatedLines) {
				bw.write(updatedLine);
				bw.newLine();
			}
			if (found) {
				System.out.println("Complain with ID " + updatedComplain.getId() + " updated successfully.");
				return ResponseCode.SUCCESS;
			} else {
				System.out.println("Complain with ID " + updatedComplain.getId() + " not found.");
				return ResponseCode.RECORD_NOT_FOUND;
			}
		} catch (IOException e) {
			System.err.println("Error writing updated complain data: " + e.getMessage());
			return ResponseCode.IO_EXCEPTION;
		}
	}

	// Method to delete a complain from the text file
	public void deleteComplaint(String id) {
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
