package service.review;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import dto.RunnerReviewDTO;
import dto.VendorReviewDTO;

public class ReviewService {

	private static final String SYS_PATH = "/FoodHero/src/main/resources/database/";

	// Method to create a VendorReview and save it to a text file in JSON format
	public void createVendorReview(VendorReviewDTO review) {
		String filePath = SYS_PATH + "vendor_review.txt";

		// Construct JSON Object
		JSONObject json = new JSONObject();
		json.put("id", review.getId());
		json.put("orderId", review.getOrderId());
		json.put("customerId", review.getCustomerId());
		json.put("rating", review.getRating());
		json.put("comments", review.getComments());
		json.put("vendorId", review.getVendorId());

		// Write JSON to text file
		try (FileWriter file = new FileWriter(filePath, true)) { // Append mode
			file.write(json.toString() + System.lineSeparator());
			System.out.println("VendorReview created successfully!");
		} catch (IOException e) {
			System.err.println("Error writing VendorReview to file: " + e.getMessage());
		}
	}

	// Method to read a VendorReview from the text file
	public VendorReviewDTO readVendorReview(String id) {
		String filePath = SYS_PATH + "vendor_review.txt";

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = reader.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				if (json.getString("id").equals(id)) {
					VendorReviewDTO review = new VendorReviewDTO();
					review.setId(json.getString("id"));
					review.setOrderId(json.getString("orderId"));
					review.setCustomerId(json.getString("customerId"));
					review.setRating(json.getInt("rating"));
					review.setComments(json.getString("comments"));
					review.setVendorId(json.getString("vendorId"));

					return review; // Review found, return it
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading VendorReview from file: " + e.getMessage());
		} catch (JSONException e) {
			System.err.println("Error parsing JSON: " + e.getMessage());
		}

		System.out.println("VendorReview with ID " + id + " not found.");
		return null; // If no matching review is found
	}

	// Method to read all VendorReviews for a specific vendor
	public List<VendorReviewDTO> readAllVendorReview(String vendorId) {
		String filePath = SYS_PATH + "vendor_review.txt";
		List<VendorReviewDTO> vendorReviews = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = reader.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				// Check if the vendorId matches
				if (json.getString("vendorId").equals(vendorId)) {
					VendorReviewDTO review = new VendorReviewDTO();
					review.setId(json.getString("id"));
					review.setOrderId(json.getString("orderId"));
					review.setCustomerId(json.getString("customerId"));
					review.setRating(json.getInt("rating"));
					review.setComments(json.getString("comments"));
					review.setVendorId(json.getString("vendorId"));

					vendorReviews.add(review); // Add to the list
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading VendorReview from file: " + e.getMessage());
		} catch (JSONException e) {
			System.err.println("Error parsing JSON: " + e.getMessage());
		}

		if (vendorReviews.isEmpty()) {
			System.out.println("No reviews found for Vendor ID: " + vendorId);
		}

		return vendorReviews; // Return all matched reviews
	}

	// Method to update a VendorReview
	public void updateVendorReview(VendorReviewDTO updatedReview) {
		String filePath = SYS_PATH + "vendor_review.txt";
		List<String> updatedLines = new ArrayList<>();
		boolean isUpdated = false;

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = reader.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				// Check if the review ID matches
				if (json.getString("id").equals(updatedReview.getId())) {
					// Update the review details
					json.put("orderId", updatedReview.getOrderId());
					json.put("customerId", updatedReview.getCustomerId());
					json.put("rating", updatedReview.getRating());
					json.put("comments", updatedReview.getComments());
					json.put("vendorId", updatedReview.getVendorId());

					isUpdated = true;
				}

				updatedLines.add(json.toString()); // Add to the updated list
			}
		} catch (IOException e) {
			System.err.println("Error reading VendorReview from file: " + e.getMessage());
		} catch (JSONException e) {
			System.err.println("Error parsing JSON: " + e.getMessage());
		}

		// Write the updated data back to the file
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
			for (String updatedLine : updatedLines) {
				writer.write(updatedLine);
				writer.newLine();
			}
		} catch (IOException e) {
			System.err.println("Error writing updated VendorReview to file: " + e.getMessage());
		}

		if (isUpdated) {
			System.out.println("VendorReview with ID " + updatedReview.getId() + " updated successfully!");
		} else {
			System.out.println("VendorReview with ID " + updatedReview.getId() + " not found.");
		}
	}

	// Method to delete a VendorReview
	public void deleteVendorReview(String reviewId) {
		String filePath = SYS_PATH + "vendor_review.txt";
		List<String> remainingReviews = new ArrayList<>();
		boolean isDeleted = false;

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = reader.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				// Skip the review if the ID matches (i.e., delete it)
				if (json.getString("id").equals(reviewId)) {
					isDeleted = true;
					continue;
				}

				remainingReviews.add(json.toString()); // Add to the list if not deleted
			}
		} catch (IOException e) {
			System.err.println("Error reading VendorReview from file: " + e.getMessage());
		} catch (JSONException e) {
			System.err.println("Error parsing JSON: " + e.getMessage());
		}

		// Write the remaining reviews back to the file
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
			for (String review : remainingReviews) {
				writer.write(review);
				writer.newLine();
			}
		} catch (IOException e) {
			System.err.println("Error writing VendorReview to file: " + e.getMessage());
		}

		if (isDeleted) {
			System.out.println("VendorReview with ID " + reviewId + " deleted successfully!");
		} else {
			System.out.println("VendorReview with ID " + reviewId + " not found.");
		}
	}

	// Method to create a RunnerReview and save to a text file
	public void createRunnerReview(RunnerReviewDTO review) {
		String filePath = SYS_PATH + "runner_review.txt";

		// Construct JSON Object
		JSONObject json = new JSONObject();
		json.put("id", review.getId());
		json.put("orderId", review.getOrderId());
		json.put("customerId", review.getCustomerId());
		json.put("rating", review.getRating());
		json.put("comments", review.getComments());
		json.put("runnerId", review.getRunnerId());

		// Write JSON to the text file
		try (FileWriter file = new FileWriter(filePath, true)) { // Append mode
			file.write(json.toString() + System.lineSeparator());
			System.out.println("RunnerReview created successfully!");
		} catch (IOException e) {
			System.err.println("Error writing RunnerReview to file: " + e.getMessage());
		}
	}

	// Method to read a RunnerReview from the text file
	public RunnerReviewDTO readRunnerReview(String id) {
		String filePath = SYS_PATH + "runner_review.txt";

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = reader.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				// Check if the ID matches
				if (json.getString("id").equals(id)) {
					RunnerReviewDTO review = new RunnerReviewDTO();
					review.setId(json.getString("id"));
					review.setOrderId(json.getString("orderId"));
					review.setCustomerId(json.getString("customerId"));
					review.setRating(json.getInt("rating"));
					review.setComments(json.getString("comments"));
					review.setRunnerId(json.getString("runnerId"));

					return review; // Review found, return it
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading RunnerReview from file: " + e.getMessage());
		} catch (JSONException e) {
			System.err.println("Error parsing JSON: " + e.getMessage());
		}

		System.out.println("RunnerReview with ID " + id + " not found.");
		return null; // No matching review found
	}

	// Method to read all RunnerReviews for a specific runnerId
	public List<RunnerReviewDTO> readAllRunnerReview(String runnerId) {
		String filePath = SYS_PATH + "runner_review.txt";
		List<RunnerReviewDTO> reviews = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = reader.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				// Check if the runnerId matches
				if (json.getString("runnerId").equals(runnerId)) {
					RunnerReviewDTO review = new RunnerReviewDTO();
					review.setId(json.getString("id"));
					review.setOrderId(json.getString("orderId"));
					review.setCustomerId(json.getString("customerId"));
					review.setRating(json.getInt("rating"));
					review.setComments(json.getString("comments"));
					review.setRunnerId(json.getString("runnerId"));

					reviews.add(review); // Add matching review to the list
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading RunnerReview from file: " + e.getMessage());
		} catch (JSONException e) {
			System.err.println("Error parsing JSON: " + e.getMessage());
		}

		return reviews; // Return the list of reviews (empty if none found)
	}

	// Method to update an existing RunnerReview
	public void updateRunnerReview(RunnerReviewDTO updatedReview) {
		String filePath = SYS_PATH + "runner_review.txt";
		List<String> fileContent = new ArrayList<>();
		boolean reviewFound = false;

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = reader.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				// Check if the review ID matches
				if (json.getString("id").equals(updatedReview.getId())) {
					// Update JSON with new values
					json.put("orderId", updatedReview.getOrderId());
					json.put("customerId", updatedReview.getCustomerId());
					json.put("rating", updatedReview.getRating());
					json.put("comments", updatedReview.getComments());
					json.put("runnerId", updatedReview.getRunnerId());

					reviewFound = true; // Review found and updated
				}

				fileContent.add(json.toString());
			}
		} catch (IOException e) {
			System.err.println("Error reading RunnerReview from file: " + e.getMessage());
			return;
		} catch (JSONException e) {
			System.err.println("Error parsing JSON: " + e.getMessage());
			return;
		}

		if (reviewFound) {
			try (FileWriter writer = new FileWriter(filePath)) {
				for (String updatedLine : fileContent) {
					writer.write(updatedLine + System.lineSeparator());
				}
				System.out.println("RunnerReview updated successfully!");
			} catch (IOException e) {
				System.err.println("Error writing updated RunnerReview to file: " + e.getMessage());
			}
		} else {
			System.out.println("RunnerReview with ID " + updatedReview.getId() + " not found.");
		}
	}

	// Method to delete a RunnerReview by ID
	public void deleteRunnerReview(String reviewId) {
		String filePath = SYS_PATH + "runner_review.txt";
		List<String> fileContent = new ArrayList<>();
		boolean reviewFound = false;

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = reader.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				// ❌ Skip the review that matches the ID (i.e., delete it)
				if (json.getString("id").equals(reviewId)) {
					reviewFound = true; // Mark as found
					continue;           // Skip adding this review back
				}

				fileContent.add(json.toString());
			}
		} catch (IOException e) {
			System.err.println("Error reading RunnerReview from file: " + e.getMessage());
			return;
		} catch (JSONException e) {
			System.err.println("Error parsing JSON: " + e.getMessage());
			return;
		}

		if (reviewFound) {
			try (FileWriter writer = new FileWriter(filePath)) {
				for (String updatedLine : fileContent) {
					writer.write(updatedLine + System.lineSeparator());
				}
				System.out.println("RunnerReview deleted successfully!");
			} catch (IOException e) {
				System.err.println("Error writing updated RunnerReview to file: " + e.getMessage());
			}
		} else {
			System.out.println("RunnerReview with ID " + reviewId + " not found.");
		}
	}
}
