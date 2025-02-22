package service.review;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import dto.RunnerReviewDTO;
import dto.VendorReviewDTO;
import enumeration.ResponseCode;
import enumeration.ServiceType;
import service.utils.IdGenerationUtils;

public class ReviewService {

	private static final String SYS_PATH = "src\\main\\resources\\database\\review\\";

	// Method to create a VendorReview and save it to a text file in JSON format
	public ResponseCode createVendorReview(VendorReviewDTO review) {
		
        String filePath = SYS_PATH + "vendor_" + review.getVendorId() + "_review.txt";
        
        JSONObject json = new JSONObject();
        json.put("id", IdGenerationUtils.getNextId(ServiceType.VENDOR_REVIEW, review.getVendorId(), null));
        json.put("orderId", review.getOrderId());
        json.put("customerId", review.getCustomerId());
        json.put("rating", review.getRating());
        json.put("comments", review.getComments());
        json.put("vendorId", review.getVendorId());

        try (FileWriter file = new FileWriter(filePath, true)) {
            file.write(json.toString() + System.lineSeparator());
            return ResponseCode.SUCCESS;
        } catch (IOException e) {
            return ResponseCode.IO_EXCEPTION;
        }
    }

	// Method to read a VendorReview from the text file
	public static VendorReviewDTO readVendorReview(String vendorId, String id) {
		
		String filePath = SYS_PATH + "vendor_" + vendorId + "_review.txt";
		
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
	public static List<VendorReviewDTO> readAllVendorReview(String vendorId) {
		
		String filePath = SYS_PATH + "vendor_" + vendorId + "_review.txt";
		
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
	public ResponseCode updateVendorReview(VendorReviewDTO updatedReview) {
		
        String filePath = SYS_PATH + "vendor_" + updatedReview.getVendorId() + "_review.txt";
        
        List<String> updatedLines = new ArrayList<>();
        boolean isUpdated = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                JSONObject json = new JSONObject(line);
                if (json.getString("id").equals(updatedReview.getId())) {
                    json.put("orderId", updatedReview.getOrderId());
                    json.put("customerId", updatedReview.getCustomerId());
                    json.put("rating", updatedReview.getRating());
                    json.put("comments", updatedReview.getComments());
                    json.put("vendorId", updatedReview.getVendorId());
                    isUpdated = true;
                }
                updatedLines.add(json.toString());
            }
        } catch (IOException e) {
            return ResponseCode.IO_EXCEPTION;
        } catch (JSONException e) {
            return ResponseCode.JSON_EXCEPTION;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String updatedLine : updatedLines) {
                writer.write(updatedLine);
                writer.newLine();
            }
        } catch (IOException e) {
            return ResponseCode.IO_EXCEPTION;
        }

        return isUpdated ? ResponseCode.SUCCESS : ResponseCode.RECORD_NOT_FOUND;
    }

	// Method to delete a VendorReview
	public ResponseCode deleteVendorReview(String vendorId, String reviewId) {
		
        String filePath = SYS_PATH + "vendor_" + vendorId + "_review.txt";
        
        List<String> remainingReviews = new ArrayList<>();
        boolean isDeleted = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                JSONObject json = new JSONObject(line);
                if (json.getString("id").equals(reviewId)) {
                    isDeleted = true;
                    continue;
                }
                remainingReviews.add(json.toString());
            }
        } catch (IOException e) {
            return ResponseCode.IO_EXCEPTION;
        } catch (JSONException e) {
            return ResponseCode.JSON_EXCEPTION;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String review : remainingReviews) {
                writer.write(review);
                writer.newLine();
            }
        } catch (IOException e) {
            return ResponseCode.IO_EXCEPTION;
        }

        return isDeleted ? ResponseCode.SUCCESS : ResponseCode.RECORD_NOT_FOUND;
    }

	// Method to create data storage files for items
	public static ResponseCode createVendorReviewDataStorage(String vendorId) {
		
        String filePath = SYS_PATH + "vendor_" + vendorId + "_review.txt";
        
        try {
            File reviewFile = new File(filePath);
            if (reviewFile.createNewFile()) {
                return ResponseCode.SUCCESS;
            } else {
                return ResponseCode.SUCCESS; // File already exists, still considered successful
            }
        } catch (IOException e) {
            return ResponseCode.IO_EXCEPTION;
        }
    }

	// Method to create a RunnerReview and save to a text file
	public ResponseCode createRunnerReview(RunnerReviewDTO review) {
		
	    String filePath = SYS_PATH + "runner_" + review.getRunnerId() + "_review.txt";

	    JSONObject json = new JSONObject();
	    json.put("id", IdGenerationUtils.getNextId(ServiceType.RUNNER_REVIEW, null, review.getRunnerId()));
	    json.put("orderId", review.getOrderId());
	    json.put("customerId", review.getCustomerId());
	    json.put("rating", review.getRating());
	    json.put("comments", review.getComments());
	    json.put("runnerId", review.getRunnerId());

	    try (FileWriter file = new FileWriter(filePath, true)) {
	        file.write(json.toString() + System.lineSeparator());
	        System.out.println("RunnerReview created successfully!");
	        return ResponseCode.SUCCESS;
	    } catch (IOException e) {
	        System.err.println("Error writing RunnerReview to file: " + e.getMessage());
	        return ResponseCode.IO_EXCEPTION;
	    }
	}

	// Method to read a RunnerReview from the text file
	public RunnerReviewDTO readRunnerReview(String runnerId, String id) {

		String filePath = SYS_PATH + "runner_" + runnerId + "_review.txt";

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
	public static List<RunnerReviewDTO> readAllRunnerReview(String runnerId) {

		String filePath = SYS_PATH + "runner_" + runnerId + "_review.txt";

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
	public ResponseCode updateRunnerReview(RunnerReviewDTO updatedReview) {
		
	    String filePath = SYS_PATH + "runner_" + updatedReview.getRunnerId() + "_review.txt";
	    
	    List<String> fileContent = new ArrayList<>();
	    boolean reviewFound = false;

	    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            JSONObject json = new JSONObject(line);
	            if (json.getString("id").equals(updatedReview.getId())) {
	                json.put("orderId", updatedReview.getOrderId());
	                json.put("customerId", updatedReview.getCustomerId());
	                json.put("rating", updatedReview.getRating());
	                json.put("comments", updatedReview.getComments());
	                json.put("runnerId", updatedReview.getRunnerId());
	                reviewFound = true;
	            }
	            fileContent.add(json.toString());
	        }
	    } catch (IOException e) {
	        System.err.println("Error reading RunnerReview from file: " + e.getMessage());
	        return ResponseCode.IO_EXCEPTION;
	    } catch (JSONException e) {
	        System.err.println("Error parsing JSON: " + e.getMessage());
	        return ResponseCode.JSON_EXCEPTION;
	    }

	    if (reviewFound) {
	        try (FileWriter writer = new FileWriter(filePath)) {
	            for (String updatedLine : fileContent) {
	                writer.write(updatedLine + System.lineSeparator());
	            }
	            System.out.println("RunnerReview updated successfully!");
	            return ResponseCode.SUCCESS;
	        } catch (IOException e) {
	            System.err.println("Error writing updated RunnerReview to file: " + e.getMessage());
	            return ResponseCode.IO_EXCEPTION;
	        }
	    } else {
	        System.out.println("RunnerReview with ID " + updatedReview.getId() + " not found.");
	        return ResponseCode.RECORD_NOT_FOUND;
	    }
	}

	// Method to delete a RunnerReview by ID
	public ResponseCode deleteRunnerReview(String runnerId, String reviewId) {
		
	    String filePath = SYS_PATH + "runner_" + runnerId + "_review.txt";
	    
	    List<String> fileContent = new ArrayList<>();
	    boolean reviewFound = false;

	    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            JSONObject json = new JSONObject(line);
	            if (json.getString("id").equals(reviewId)) {
	                reviewFound = true;
	                continue; // Skip this review
	            }
	            fileContent.add(json.toString());
	        }
	    } catch (IOException e) {
	        System.err.println("Error reading RunnerReview from file: " + e.getMessage());
	        return ResponseCode.IO_EXCEPTION;
	    } catch (JSONException e) {
	        System.err.println("Error parsing JSON: " + e.getMessage());
	        return ResponseCode.JSON_EXCEPTION;
	    }

	    if (reviewFound) {
	        try (FileWriter writer = new FileWriter(filePath)) {
	            for (String updatedLine : fileContent) {
	                writer.write(updatedLine + System.lineSeparator());
	            }
	            System.out.println("RunnerReview deleted successfully!");
	            return ResponseCode.SUCCESS;
	        } catch (IOException e) {
	            System.err.println("Error writing updated RunnerReview to file: " + e.getMessage());
	            return ResponseCode.IO_EXCEPTION;
	        }
	    } else {
	        System.out.println("RunnerReview with ID " + reviewId + " not found.");
	        return ResponseCode.RECORD_NOT_FOUND;
	    }
	}

	// Method to create data storage files for items
	public static ResponseCode createRunnerReviewDataStorage(String runnerId) {
		
	    String filePath = SYS_PATH + "runner_" + runnerId + "_review.txt";
	    
	    try {
	        File reviewFile = new File(filePath);
	        if (reviewFile.createNewFile()) {
	            System.out.println("Runner review data storage created for runner: " + runnerId);
	            return ResponseCode.SUCCESS;
	        } else {
	            System.out.println("Runner review data storage already exists for runner: " + runnerId);
	            return ResponseCode.SUCCESS;
	        }
	        
	    } catch (IOException e) {
	        System.err.println("Error creating data storage for runner review " + runnerId + ": " + e.getMessage());
	        return ResponseCode.IO_EXCEPTION;
	    }
	}
}
