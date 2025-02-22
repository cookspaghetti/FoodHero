package service.runner;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import dto.AddressDTO;
import dto.NotificationDTO;
import dto.RunnerDTO;
import enumeration.ResponseCode;
import enumeration.ServiceType;
import service.address.AddressService;
import service.notification.NotificationService;
import service.review.ReviewService;
import service.utils.IdGenerationUtils;
import service.utils.JsonUtils;

public class RunnerService {

	private static final String SYS_PATH = "src\\main\\resources\\database\\user\\";

	// Method to create a delivery runner and save to a text file in JSON format
	public static ResponseCode createRunner(RunnerDTO runner) {
		String filePath = SYS_PATH + "runner.txt";

		// Construct JSON Object
		JSONObject json = new JSONObject();
		json.put("id", IdGenerationUtils.getNextId(ServiceType.RUNNER, null, null));
		json.put("name", runner.getName());
		json.put("phoneNumber", runner.getPhoneNumber());
		json.put("addressId", runner.getAddressId()); // Fixed from "address"
		json.put("emailAddress", runner.getEmailAddress());
		json.put("password", runner.getPassword());   // Added password
		json.put("status", runner.getStatus());       // Added status
		json.put("plateNumber", runner.getPlateNumber());

		// Handle lists properly
		json.put("tasks", runner.getTasks() != null ? new JSONArray(runner.getTasks()) : new JSONArray());
		json.put("reviews", runner.getReviews() != null ? new JSONArray(runner.getReviews()) : new JSONArray());
		
		json.put("available", runner.isAvailable());
		json.put("currentTask", runner.getCurrentTask());
		json.put("earnings", runner.getEarnings());
		json.put("ratings", runner.getRatings());     // Added ratings field
		json.put("lastDeliveredAddress", runner.getLastDeliveredAddress() != null ? runner.getLastDeliveredAddress() : "");

		// Handle null lastDeliveryDate
		json.put("lastDeliveryDate", runner.getLastDeliveryDate() != null 
				? runner.getLastDeliveryDate().toString() 
						: "");

		// Write JSON to text file
		try (FileWriter file = new FileWriter(filePath, true)) {
			file.write(json.toString() + System.lineSeparator());
			System.out.println("Runner created successfully!");
		} catch (IOException e) {
			System.err.println("Error writing runner to file: " + e.getMessage());
			return ResponseCode.IO_EXCEPTION;
		}
		
		ResponseCode response = ReviewService.createRunnerReviewDataStorage(runner.getId());
		if (response != ResponseCode.SUCCESS) {
			return response;
		}
		
		return ResponseCode.SUCCESS;
	}

	// Method to read runner info
	public static RunnerDTO readRunner(String id) {
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
					runner.setAddressId(json.optString("addressId", ""));  // Fixed field name
					runner.setEmailAddress(json.optString("emailAddress", "")); // Fixed field name
					runner.setPassword(json.optString("password", ""));    // Added password
					runner.setStatus(json.optBoolean("status", true));     // Added status (default true)
					runner.setPlateNumber(json.optString("plateNumber", ""));
					runner.setEarnings(json.optDouble("earnings", 0.0));
					runner.setRatings(json.optDouble("ratings", 0.0));
					runner.setAvailable(json.optBoolean("available"));
					runner.setCurrentTask(json.getString("currentTask"));

					// Convert JSON arrays to List<String>
					runner.setTasks(JsonUtils.jsonArrayToList(json.getJSONArray("tasks")));
					runner.setReviews(JsonUtils.jsonArrayToList(json.getJSONArray("reviews")));

					runner.setLastDeliveredAddress(json.getString("lastDeliveredAddress"));

					// Parse lastDeliveryDate as LocalDateTime
					if (json.has("lastDeliveryDate") && !json.isNull("lastDeliveryDate")) {
						String dateStr = json.getString("lastDeliveryDate").trim();
						if (!dateStr.isEmpty()) {
							try {
								DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
								runner.setLastDeliveryDate(LocalDateTime.parse(dateStr, formatter));
							} catch (Exception e) {
								System.err.println("Error parsing date: " + e.getMessage());
								// Set to null if parsing fails
								runner.setLastDeliveryDate(null);
							}
						} else {
							runner.setLastDeliveryDate(null);
						}
					} else {
						runner.setLastDeliveryDate(null);
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

	// Method to read runner by name
	public static RunnerDTO readRunnerByName(String name) {
		String filePath = SYS_PATH + "runner.txt";

		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;

			// Read each line (JSON object) in the file
			while ((line = br.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				// Check if the name matches
				if (json.getString("name").equals(name)) {
					// Construct DeliveryRunner object
					RunnerDTO runner = new RunnerDTO();
					runner.setId(json.getString("id"));
					runner.setName(json.getString("name"));
					runner.setPhoneNumber(json.getString("phoneNumber"));
					runner.setAddressId(json.optString("addressId", ""));  // Fixed field name
					runner.setEmailAddress(json.optString("emailAddress", "")); // Fixed field name
					runner.setPassword(json.optString("password", ""));    // Added password
					runner.setStatus(json.optBoolean("status", true));     // Added status (default true)
					runner.setPlateNumber(json.optString("plateNumber", ""));
					runner.setEarnings(json.optDouble("earnings", 0.0));
					runner.setRatings(json.optDouble("ratings", 0.0));
					runner.setAvailable(json.optBoolean("available"));
					runner.setCurrentTask(json.getString("currentTask"));

					// Convert JSON arrays to List<String>
					runner.setTasks(JsonUtils.jsonArrayToList(json.getJSONArray("tasks")));
					runner.setReviews(JsonUtils.jsonArrayToList(json.getJSONArray("reviews")));

					runner.setLastDeliveredAddress(json.getString("lastDeliveredAddress"));

					// Parse lastDeliveryDate as LocalDateTime
					if (json.has("lastDeliveryDate") && !json.isNull("lastDeliveryDate")) {
						String dateStr = json.getString("lastDeliveryDate").trim();
						if (!dateStr.isEmpty()) {
							try {
								DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
								runner.setLastDeliveryDate(LocalDateTime.parse(dateStr, formatter));
							} catch (Exception e) {
								System.err.println("Error parsing date: " + e.getMessage());
								// Set to null if parsing fails
								runner.setLastDeliveryDate(null);
							}
						} else {
							runner.setLastDeliveryDate(null);
						}
					} else {
						runner.setLastDeliveryDate(null);
					}

					return runner; // Return the found runner
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading runner file: " + e.getMessage());
		}

		System.out.println("Runner with name " + name + " not found.");
		return null; // Return null if not found
	}

	// Method to read all runners from the text file
	public static List<RunnerDTO> readAllRunner() {
		String filePath = SYS_PATH + "runner.txt";
		List<RunnerDTO> runners = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = reader.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				RunnerDTO runner = new RunnerDTO();
				runner.setId(json.getString("id"));
				runner.setName(json.getString("name"));
				runner.setPhoneNumber(json.getString("phoneNumber"));
				runner.setAddressId(json.optString("addressId", ""));  // Fixed field name
				runner.setEmailAddress(json.optString("emailAddress", "")); // Fixed field name
				runner.setPassword(json.optString("password", ""));    // Added password
				runner.setStatus(json.optBoolean("status", true));     // Added status (default true)
				runner.setPlateNumber(json.optString("plateNumber", ""));
				runner.setEarnings(json.optDouble("earnings", 0.0));
				runner.setRatings(json.optDouble("ratings", 0.0));
				runner.setAvailable(json.optBoolean("available"));
				runner.setCurrentTask(json.getString("currentTask"));
				
				// Reading tasks and reviews (Lists)
				runner.setTasks(JsonUtils.jsonArrayToList(json.getJSONArray("tasks")));
				runner.setReviews(JsonUtils.jsonArrayToList(json.getJSONArray("reviews")));

				// Reading lastDeliveryDate (Optional)
				if (json.has("lastDeliveryDate") && !json.isNull("lastDeliveryDate")) {
					String dateStr = json.getString("lastDeliveryDate").trim();
					if (!dateStr.isEmpty()) {
						try {
							DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
							runner.setLastDeliveryDate(LocalDateTime.parse(dateStr, formatter));
						} catch (Exception e) {
							System.err.println("Error parsing date: " + e.getMessage());
							// Set to null if parsing fails
							runner.setLastDeliveryDate(null);
						}
					} else {
						runner.setLastDeliveryDate(null);
					}
				} else {
					runner.setLastDeliveryDate(null);
				}

				runners.add(runner); // Add to the list
			}
		} catch (IOException e) {
			System.err.println("Error reading runners from file: " + e.getMessage());
		} catch (JSONException e) {
			System.err.println("Error parsing JSON: " + e.getMessage());
		}

		return runners; // Return the list of runners
	}

	// Method to update runner
	public static ResponseCode updateRunner(RunnerDTO updatedRunner) {
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
					json.put("addressId", updatedRunner.getAddressId());                // Fixed field name
					json.put("emailAddress", updatedRunner.getEmailAddress());          // Fixed field name
					json.put("password", updatedRunner.getPassword());                  // Added password
					json.put("status", updatedRunner.getStatus());                      // Added status
					json.put("plateNumber", updatedRunner.getPlateNumber());
					json.put("earnings", updatedRunner.getEarnings());
					json.put("ratings", updatedRunner.getRatings());                    // Added ratings
					json.put("available", updatedRunner.isAvailable());
					json.put("currentTask", updatedRunner.getCurrentTask());
					
					// Convert Lists to JSON Arrays safely
					json.put("tasks", updatedRunner.getTasks() != null ? new JSONArray(updatedRunner.getTasks()) : new JSONArray());
					json.put("reviews", updatedRunner.getReviews() != null ? new JSONArray(updatedRunner.getReviews()) : new JSONArray());

					json.put("lastDeliveredAddress", updatedRunner.getLastDeliveredAddress() != null ? updatedRunner.getLastDeliveredAddress() : "");

					// Handle lastDeliveryDate safely
					json.put("lastDeliveryDate", 
							updatedRunner.getLastDeliveryDate() != null 
							? updatedRunner.getLastDeliveryDate().toString() 
									: "");

					found = true;
				}

				// Add the (possibly updated) line to the list
				updatedLines.add(json.toString());
			}
		} catch (IOException e) {
			System.err.println("Error reading runner file: " + e.getMessage());
			return ResponseCode.IO_EXCEPTION;
		}

		// Write the updated content back to the file
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) {
			for (String updatedLine : updatedLines) {
				bw.write(updatedLine);
				bw.newLine();
			}
			if (found) {
				System.out.println("Runner with ID " + updatedRunner.getId() + " updated successfully.");
				return ResponseCode.SUCCESS;
			} else {
				System.out.println("Runner with ID " + updatedRunner.getId() + " not found.");
				return ResponseCode.RECORD_NOT_FOUND;
			}
		} catch (IOException e) {
			System.err.println("Error writing updated runner data: " + e.getMessage());
			return ResponseCode.IO_EXCEPTION;
		}
	}

	// Method to delete a runner
	public static ResponseCode deleteRunner(String id) {

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
			return ResponseCode.IO_EXCEPTION;
		}

		// Write the updated list back to the file
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, false))) {
			for (String updatedLine : updatedLines) {
				bw.write(updatedLine);
				bw.newLine();
			}
			if (found) {
				System.out.println("Runner with ID " + id + " deleted successfully.");
				return ResponseCode.SUCCESS;
			} else {
				System.out.println("Runner with ID " + id + " not found.");
				return ResponseCode.RECORD_NOT_FOUND;
			}
		} catch (IOException e) {
			System.err.println("Error writing updated runner data: " + e.getMessage());
			return ResponseCode.IO_EXCEPTION;
		}
	}

	public static String assignRunner(String customerAddressId) {
		AddressDTO customerAddress = AddressService.getAddressById(customerAddressId);

		List<RunnerDTO> runners = readAllRunner();
		List<RunnerDTO> availableRunners = new ArrayList<>();
		RunnerDTO selectedRunner = null;

		for (RunnerDTO runner : runners) {
			if (runner.isAvailable()) {
				availableRunners.add(runner);
			}
		}

		if (availableRunners.isEmpty()) {
			System.out.println("No available runners found.");
			return null;
		}

		for (RunnerDTO runner : availableRunners) {
			AddressDTO lastDeliveredAddress;
			if (runner.getLastDeliveredAddress() != null) {
				lastDeliveredAddress = AddressService.getAddressById(runner.getLastDeliveredAddress());
			} else {
				lastDeliveredAddress = AddressService.getAddressById(runner.getAddressId());
			}

			if (lastDeliveredAddress.getCity().equals(customerAddress.getCity())
					&& lastDeliveredAddress.getState().equals(customerAddress.getState())) {
				selectedRunner = runner;
				break;
			}
		}
		if (selectedRunner != null) {
			NotificationDTO notification = new NotificationDTO();
			notification.setUserId(selectedRunner.getId());
			notification.setTitle("New Delivery Task");
			notification.setMessage("You have a new delivery task.");
			notification.setRead(false);
			notification.setTimestamp(LocalDateTime.now());
			NotificationService.createNotification(notification);
			return selectedRunner.getId();
		} else {
			return null;
		}
	}
}
