package service.task;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import dto.NotificationDTO;
import dto.TaskDTO;
import enumeration.ResponseCode;
import enumeration.ServiceType;
import enumeration.TaskStatus;
import service.utils.IdGenerationUtils;
import service.notification.NotificationService;
import service.runner.RunnerService;

public class TaskService {

	private static final String SYS_PATH = "src\\main\\resources\\database\\task\\";

	// Method to create a task and save to a text file in JSON format
	public static ResponseCode createTask(TaskDTO task) {
		String filePath = SYS_PATH + "task.txt";

		// Construct JSON Object
		JSONObject json = new JSONObject();
		json.put("id", IdGenerationUtils.getNextId(ServiceType.TASK, null, null));
		json.put("orderId", task.getOrderId());
		json.put("runnerId", task.getRunnerId());
		json.put("status", task.getStatus());
		json.put("taskDetails", task.getTaskDetails());
		json.put("deliveryFee", task.getDeliveryFee());
		json.put("customerAddress", task.getCustomerAddress());
		json.put("acceptanceTime", task.getAcceptanceTime() != null ? task.getAcceptanceTime().toString() : JSONObject.NULL);
		json.put("completionTime", task.getCompletionTime() != null ? task.getCompletionTime().toString() : JSONObject.NULL);

		// Write JSON to text file
		try (FileWriter file = new FileWriter(filePath, true)) {
			file.write(json.toString() + System.lineSeparator());

			// send notification
			NotificationDTO notification = new NotificationDTO();
			notification.setUserId(task.getRunnerId());
			notification.setMessage("New task assigned with ID: " + json.getString("id"));
			notification.setRead(false);
			notification.setTimestamp(LocalDateTime.now());
			notification.setTitle("New Task");
			ResponseCode response = NotificationService.createNotification(notification);
			if (response != ResponseCode.SUCCESS) {
				System.err.println("Failed to send notification for new task.");
			}
			System.out.println("Task created successfully!");
			return ResponseCode.SUCCESS;
		} catch (IOException e) {
			System.err.println("Error writing task to file: " + e.getMessage());
			return ResponseCode.IO_EXCEPTION;
		}
	}

	// Method to read a task from the text file
	public static TaskDTO readTask(String id) {
		String filePath = SYS_PATH + "task.txt";

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = reader.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				if (json.getString("id").equals(id)) {
					TaskDTO task = new TaskDTO();
					task.setId(json.getString("id"));
					task.setOrderId(json.getString("orderId"));
					task.setRunnerId(json.getString("runnerId"));
					task.setStatus(TaskStatus.valueOf(json.getString("status")));
					task.setTaskDetails(json.getString("taskDetails"));
					task.setDeliveryFee(json.getDouble("deliveryFee"));
					task.setCustomerAddress(json.getString("customerAddress"));

					// Handling nullable LocalDateTime fields
					if (!json.isNull("acceptanceTime")) {
						task.setAcceptanceTime(LocalDateTime.parse(json.getString("acceptanceTime")));
					}
					if (!json.isNull("completionTime")) {
						task.setCompletionTime(LocalDateTime.parse(json.getString("completionTime")));
					}

					return task; // Task found
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading task from file: " + e.getMessage());
		} catch (JSONException e) {
			System.err.println("Error parsing JSON: " + e.getMessage());
		}

		System.out.println("Task with ID " + id + " not found.");
		return null;
	}

	// Method to read tasks by runner ID from the text file
	public static List<TaskDTO> readTaskByRunnerId(String runnerId) {
		String filePath = SYS_PATH + "task.txt";
		List<TaskDTO> tasks = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = reader.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				if (json.getString("runnerId").equals(runnerId)) {
					TaskDTO task = new TaskDTO();
					task.setId(json.getString("id"));
					task.setOrderId(json.getString("orderId"));
					task.setRunnerId(json.getString("runnerId"));
					task.setStatus(TaskStatus.valueOf(json.getString("status")));
					task.setTaskDetails(json.getString("taskDetails"));
					task.setDeliveryFee(json.getDouble("deliveryFee"));
					task.setCustomerAddress(json.getString("customerAddress"));

					// Handling nullable LocalDateTime fields
					if (!json.isNull("acceptanceTime")) {
						task.setAcceptanceTime(LocalDateTime.parse(json.getString("acceptanceTime")));
					}
					if (!json.isNull("completionTime")) {
						task.setCompletionTime(LocalDateTime.parse(json.getString("completionTime")));
					}

					tasks.add(task); // Add to the list
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading tasks from file: " + e.getMessage());
		} catch (JSONException e) {
			System.err.println("Error parsing JSON: " + e.getMessage());
		}

		return tasks; // Return the list of tasks
	}

	// Method to read all tasks from the text file
	public static  List<TaskDTO> readAllTask() {
		String filePath = SYS_PATH + "task.txt";
		List<TaskDTO> tasks = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = reader.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				TaskDTO task = new TaskDTO();
				task.setId(json.getString("id"));
				task.setOrderId(json.getString("orderId"));
				task.setRunnerId(json.getString("runnerId"));
				task.setStatus(TaskStatus.valueOf(json.getString("status")));
				task.setTaskDetails(json.getString("taskDetails"));
				task.setDeliveryFee(json.getDouble("deliveryFee"));
				task.setCustomerAddress(json.getString("customerAddress"));

				// Parse optional datetime fields
				if (json.has("acceptanceTime") && !json.isNull("acceptanceTime")) {
					task.setAcceptanceTime(LocalDateTime.parse(json.getString("acceptanceTime")));
				}
				if (json.has("completionTime") && !json.isNull("completionTime")) {
					task.setCompletionTime(LocalDateTime.parse(json.getString("completionTime")));
				}

				tasks.add(task); // Add to the list
			}
		} catch (IOException e) {
			System.err.println("Error reading tasks from file: " + e.getMessage());
		} catch (JSONException e) {
			System.err.println("Error parsing JSON: " + e.getMessage());
		}

		return tasks; // Return the list of tasks
	}

	// Method to update a task in the text file
	public static ResponseCode updateTask(TaskDTO updatedTask) {
		String filePath = SYS_PATH + "task.txt";
		List<String> updatedLines = new ArrayList<>();
	
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			boolean isUpdated = false;
	
			while ((line = reader.readLine()) != null) {
				JSONObject json = new JSONObject(line);
	
				if (json.getString("id").equals(updatedTask.getId())) {
					// Updating task details
					json.put("orderId", updatedTask.getOrderId());

					if (updatedTask.getRunnerId().equals("")) {
						System.out.println("Assigning runner to task with ID " + updatedTask.getId());
						json.put("runnerId", RunnerService.assignRunner(updatedTask.getCustomerAddress()));
					} else {
						System.out.println("Runner already assigned to task with ID " + updatedTask.getId());
						json.put("runnerId", updatedTask.getRunnerId());
					}
					
					json.put("status", updatedTask.getStatus());
					json.put("taskDetails", updatedTask.getTaskDetails());
					json.put("deliveryFee", updatedTask.getDeliveryFee());
					json.put("customerAddress", updatedTask.getCustomerAddress());
					json.put("acceptanceTime", 
							updatedTask.getAcceptanceTime() != null ? updatedTask.getAcceptanceTime().toString() : JSONObject.NULL);
					json.put("completionTime", 
							updatedTask.getCompletionTime() != null ? updatedTask.getCompletionTime().toString() : JSONObject.NULL);
	
					isUpdated = true; // Mark as updated
				}
	
				updatedLines.add(json.toString());
			}
	
			if (isUpdated) {
				// Write the updated content back to the file
				try (FileWriter writer = new FileWriter(filePath, false)) {
					for (String updatedLine : updatedLines) {
						writer.write(updatedLine + System.lineSeparator());
					}
				}
				System.out.println("Task with ID " + updatedTask.getId() + " updated successfully!");
				return ResponseCode.SUCCESS;
			} else {
				System.out.println("Task with ID " + updatedTask.getId() + " not found.");
				return ResponseCode.RECORD_NOT_FOUND;
			}
		} catch (IOException e) {
			System.err.println("Error updating task: " + e.getMessage());
			return ResponseCode.IO_EXCEPTION;
		} catch (JSONException e) {
			System.err.println("Error parsing JSON: " + e.getMessage());
			return ResponseCode.JSON_EXCEPTION;
		}
	}

	// Method to delete a task from the text file
	public static ResponseCode deleteTask(String id) {
		String filePath = SYS_PATH + "task.txt";
		List<String> remainingTasks = new ArrayList<>();
		boolean isDeleted = false;

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = reader.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				if (json.getString("orderId").equals(id)) {
					isDeleted = true; // Task found and will be deleted
					continue;         // Skip adding this task to the remaining list
				}
				remainingTasks.add(json.toString());
			}

			if (isDeleted) {
				// Overwrite file with remaining tasks
				try (FileWriter writer = new FileWriter(filePath, false)) {
					for (String task : remainingTasks) {
						writer.write(task + System.lineSeparator());
					}
				}
				System.out.println("Task with ID " + id + " deleted successfully!");
				return ResponseCode.SUCCESS;
			} else {
				System.out.println("Task with ID " + id + " not found.");
				return ResponseCode.RECORD_NOT_FOUND;
			}
		} catch (IOException e) {
			System.err.println("Error deleting task: " + e.getMessage());
			return ResponseCode.IO_EXCEPTION;
		} catch (JSONException e) {
			System.err.println("Error parsing JSON: " + e.getMessage());
			return ResponseCode.JSON_EXCEPTION;
		}
	}

}
