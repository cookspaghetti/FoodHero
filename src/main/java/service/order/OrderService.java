package service.order;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import dto.NotificationDTO;
import dto.OrderDTO;
import dto.TaskDTO;
import enumeration.OrderStatus;
import enumeration.ResponseCode;
import enumeration.ServiceType;
import enumeration.TaskStatus;
import service.notification.NotificationService;
import service.runner.RunnerService;
import service.task.TaskService;
import service.utils.IdGenerationUtils;

public class OrderService {

	private static final String SYS_PATH = "src\\main\\resources\\database\\order\\";

	// Method to create an order and save to a text file in JSON format
	public static ResponseCode createOrder(OrderDTO order) {

		String filePath = SYS_PATH + "order.txt";

		// Construct JSON Object
		JSONObject json = new JSONObject();
		json.put("id", IdGenerationUtils.getNextId(ServiceType.ORDER, null, null));
		json.put("customerId", order.getCustomerId());
		json.put("vendorId", order.getVendorId());
		json.put("runnerId", order.getRunnerId());
		json.put("status", order.getStatus());
		json.put("totalAmount", order.getTotalAmount());
		json.put("deliveryFee", order.getDeliveryFee());
		json.put("placementTime", order.getPlacementTime().toString());
		json.put("completionTime", order.getCompletionTime() != null ? order.getCompletionTime().toString() : "");
		json.put("notes", order.getNotes());
		json.put("deliveryAddress", order.getDeliveryAddress());

		// Convert items HashMap to JSONObject
		JSONObject itemsJson = new JSONObject(order.getItems());
		json.put("items", itemsJson);

		// Write JSON to text file
		try (FileWriter file = new FileWriter(filePath, true)) {
			file.write(json.toString() + System.lineSeparator());
			System.out.println("Order created successfully!");

			// Send notification to the vendor
			NotificationDTO notification = new NotificationDTO();
			notification.setUserId(order.getVendorId());
			notification.setMessage("New order received: " + order.getId());
			notification.setTimestamp(LocalDateTime.now());
			ResponseCode response = NotificationService.createNotification(notification);
			if (response != ResponseCode.SUCCESS) {
				System.err.println("Failed to send notification to vendor");
			}

			// Create task for runner
			TaskDTO task = new TaskDTO();
			task.setOrderId(order.getId());
			task.setRunnerId(RunnerService.assignRunner(order.getDeliveryAddress()));
			task.setStatus(TaskStatus.PENDING);
			task.setTaskDetails("Deliver order " + order.getId() + " to " + order.getDeliveryAddress());
			task.setCustomerAddress(order.getDeliveryAddress());
			task.setDeliveryFee(order.getDeliveryFee());
			if (task.getRunnerId() == null) {
				return ResponseCode.RUNNER_NOT_FOUND;
			}
			response = TaskService.createTask(task);
			if (response != ResponseCode.SUCCESS) {
				return response;
			}

			return ResponseCode.SUCCESS;
		} catch (IOException e) {
			System.err.println("Error writing order to file: " + e.getMessage());
			return ResponseCode.IO_EXCEPTION;
		}
	}

	// Method to read an order from the text file
	public static OrderDTO readOrder(String id) {

		String filePath = SYS_PATH + "order.txt";

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = reader.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				if (json.getString("id").equals(id)) {
					OrderDTO order = new OrderDTO();
					order.setId(json.getString("id"));
					order.setCustomerId(json.getString("customerId"));
					order.setVendorId(json.getString("vendorId"));
					order.setRunnerId(json.getString("runnerId"));
					order.setStatus(OrderStatus.valueOf(json.getString("status")));
					order.setTotalAmount(json.getDouble("totalAmount"));
					order.setDeliveryFee(json.getDouble("deliveryFee"));
					order.setNotes(json.getString("notes"));
					order.setDeliveryAddress(json.getString("deliveryAddress"));

					// Parse placementTime and completionTime
					order.setPlacementTime(LocalDateTime.parse(json.getString("placementTime")));
					if (json.has("completionTime") && !json.getString("completionTime").isEmpty()) {
						order.setCompletionTime(LocalDateTime.parse(json.getString("completionTime")));
					}

					// Parse items (HashMap<String, Integer>)
					JSONObject itemsJson = json.getJSONObject("items");
					HashMap<String, Integer> items = new HashMap<>();
					for (String key : itemsJson.keySet()) {
						items.put(key, itemsJson.getInt(key));
					}
					order.setItems(items);

					return order;  // Return the matched order
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading order from file: " + e.getMessage());
		} catch (JSONException e) {
			System.err.println("Error parsing JSON: " + e.getMessage());
		}

		System.out.println("Order with ID " + id + " not found.");
		return null;
	}

	// Method to read customer orders from the text file
	public static List<OrderDTO> readCustomerOrders(String customerId) {
		String filePath = SYS_PATH + "order.txt";
		List<OrderDTO> orders = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = reader.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				if (json.getString("customerId").equals(customerId)) {
					OrderDTO order = new OrderDTO();
					order.setId(json.getString("id"));
					order.setCustomerId(json.getString("customerId"));
					order.setVendorId(json.getString("vendorId"));
					order.setRunnerId(json.getString("runnerId"));
					order.setStatus(OrderStatus.valueOf(json.getString("status")));
					order.setTotalAmount(json.getDouble("totalAmount"));
					order.setDeliveryFee(json.getDouble("deliveryFee"));
					order.setNotes(json.getString("notes"));
					order.setDeliveryAddress(json.getString("deliveryAddress"));

					// Parse placementTime and completionTime
					order.setPlacementTime(LocalDateTime.parse(json.getString("placementTime")));
					if (json.has("completionTime") && !json.getString("completionTime").isEmpty()) {
						order.setCompletionTime(LocalDateTime.parse(json.getString("completionTime")));
					}

					// Parse items (HashMap<String, Integer>)
					JSONObject itemsJson = json.getJSONObject("items");
					HashMap<String, Integer> items = new HashMap<>();
					for (String key : itemsJson.keySet()) {
						items.put(key, itemsJson.getInt(key));
					}
					order.setItems(items);

					orders.add(order);  // Add to the list
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading orders from file: " + e.getMessage());
		} catch (JSONException e) {
			System.err.println("Error parsing JSON: " + e.getMessage());
		}

		return orders;  // Return the list of orders
	}

	// Method to read vendor orders from the text file
	public static List<OrderDTO> readVendorOrders(String vendorId) {
		String filePath = SYS_PATH + "order.txt";
		List<OrderDTO> orders = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = reader.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				if (json.getString("vendorId").equals(vendorId)) {
					OrderDTO order = new OrderDTO();
					order.setId(json.getString("id"));
					order.setCustomerId(json.getString("customerId"));
					order.setVendorId(json.getString("vendorId"));
					order.setRunnerId(json.getString("runnerId"));
					order.setStatus(OrderStatus.valueOf(json.getString("status")));
					order.setTotalAmount(json.getDouble("totalAmount"));
					order.setDeliveryFee(json.getDouble("deliveryFee"));
					order.setNotes(json.getString("notes"));
					order.setDeliveryAddress(json.getString("deliveryAddress"));

					// Parse placementTime and completionTime
					order.setPlacementTime(LocalDateTime.parse(json.getString("placementTime")));
					if (json.has("completionTime") && !json.getString("completionTime").isEmpty()) {
						order.setCompletionTime(LocalDateTime.parse(json.getString("completionTime")));
					}

					// Parse items (HashMap<String, Integer>)
					JSONObject itemsJson = json.getJSONObject("items");
					HashMap<String, Integer> items = new HashMap<>();
					for (String key : itemsJson.keySet()) {
						items.put(key, itemsJson.getInt(key));
					}
					order.setItems(items);

					orders.add(order);  // Add to the list
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading orders from file: " + e.getMessage());
		} catch (JSONException e) {
			System.err.println("Error parsing JSON: " + e.getMessage());
		}

		return orders;  // Return the list of orders
	}

	// Method to read runner orders from the text file
	public static List<OrderDTO> readRunnerOrders(String runnerId) {
		String filePath = SYS_PATH + "order.txt";
		List<OrderDTO> orders = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = reader.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				if (json.getString("runnerId").equals(runnerId)) {
					OrderDTO order = new OrderDTO();
					order.setId(json.getString("id"));
					order.setCustomerId(json.getString("customerId"));
					order.setVendorId(json.getString("vendorId"));
					order.setRunnerId(json.getString("runnerId"));
					order.setStatus(OrderStatus.valueOf(json.getString("status")));
					order.setTotalAmount(json.getDouble("totalAmount"));
					order.setDeliveryFee(json.getDouble("deliveryFee"));
					order.setNotes(json.getString("notes"));
					order.setDeliveryAddress(json.getString("deliveryAddress"));

					// Parse placementTime and completionTime
					order.setPlacementTime(LocalDateTime.parse(json.getString("placementTime")));
					if (json.has("completionTime") && !json.getString("completionTime").isEmpty()) {
						order.setCompletionTime(LocalDateTime.parse(json.getString("completionTime")));
					}

					// Parse items (HashMap<String, Integer>)
					JSONObject itemsJson = json.getJSONObject("items");
					HashMap<String, Integer> items = new HashMap<>();
					for (String key : itemsJson.keySet()) {
						items.put(key, itemsJson.getInt(key));
					}
					order.setItems(items);

					orders.add(order);  // Add to the list
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading orders from file: " + e.getMessage());
		} catch (JSONException e) {
			System.err.println("Error parsing JSON: " + e.getMessage());
		}

		return orders;  // Return the list of orders
	}

	// Method to read all orders from the text file
	public static List<OrderDTO> readAllOrder() {
		String filePath = SYS_PATH + "order.txt";
		List<OrderDTO> orders = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = reader.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				OrderDTO order = new OrderDTO();
				order.setId(json.getString("id"));
				order.setCustomerId(json.getString("customerId"));
				order.setVendorId(json.getString("vendorId"));
				order.setRunnerId(json.getString("runnerId"));

				// Convert items (stored as JSON) to HashMap<String, Integer>
				JSONObject itemsJson = json.getJSONObject("items");
				HashMap<String, Integer> items = new HashMap<>();
				for (String key : itemsJson.keySet()) {
					items.put(key, itemsJson.getInt(key));
				}
				order.setItems(items);

				order.setStatus(OrderStatus.valueOf(json.getString("status")));
				order.setTotalAmount(json.getDouble("totalAmount"));
				order.setDeliveryFee(json.getDouble("deliveryFee"));
				order.setPlacementTime(LocalDateTime.parse(json.getString("placementTime")));

				// Optional fields
				if (json.has("completionTime") && !json.isNull("completionTime")) {
					order.setCompletionTime(LocalDateTime.parse(json.getString("completionTime")));
				}

				order.setNotes(json.optString("notes", ""));
				order.setDeliveryAddress(json.getString("deliveryAddress"));

				orders.add(order); // Add to the list
			}
		} catch (IOException e) {
			System.err.println("Error reading orders from file: " + e.getMessage());
		} catch (JSONException e) {
			System.err.println("Error parsing JSON: " + e.getMessage());
		}

		return orders; // Return the list of orders
	}

	// Method to update an existing order
	public static ResponseCode updateOrder(OrderDTO updatedOrder) {
		String filePath = SYS_PATH + "order.txt";
		List<String> updatedLines = new ArrayList<>();
		boolean isUpdated = false;
	
		// Input validation
		if (updatedOrder == null || updatedOrder.getId() == null) {
			System.err.println("Invalid order data provided");
			return ResponseCode.INVALID_INPUT;
		}
	
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
	
			while ((line = reader.readLine()) != null) {
				JSONObject json = new JSONObject(line);
	
				if (json.getString("id").equals(updatedOrder.getId())) {
					// Update the fields with values from updatedOrder
					json.put("customerId", updatedOrder.getCustomerId());
					json.put("vendorId", updatedOrder.getVendorId());
					json.put("runnerId", updatedOrder.getRunnerId());
					json.put("status", updatedOrder.getStatus().toString());
					json.put("totalAmount", updatedOrder.getTotalAmount());
					json.put("deliveryFee", updatedOrder.getDeliveryFee());
					json.put("placementTime", updatedOrder.getPlacementTime().toString());
					json.put("completionTime", 
						updatedOrder.getCompletionTime() != null ? 
						updatedOrder.getCompletionTime().toString() : 
						JSONObject.NULL);
					json.put("notes", updatedOrder.getNotes() != null ? updatedOrder.getNotes() : "");
					json.put("deliveryAddress", updatedOrder.getDeliveryAddress());
	
					// Convert items HashMap to JSONObject
					JSONObject itemsJson = new JSONObject();
					if (updatedOrder.getItems() != null) {
						for (Map.Entry<String, Integer> entry : updatedOrder.getItems().entrySet()) {
							itemsJson.put(entry.getKey(), entry.getValue());
						}
					}
					json.put("items", itemsJson);
	
					isUpdated = true;
				}
				updatedLines.add(json.toString());
			}
	
			// Write the updated data back to the file if there were changes
			if (isUpdated) {
				try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
					for (String updatedLine : updatedLines) {
						writer.write(updatedLine);
						writer.newLine();
					}
					System.out.println("Order with ID " + updatedOrder.getId() + " updated successfully.");
					return ResponseCode.SUCCESS;
				}
			} else {
				System.out.println("Order with ID " + updatedOrder.getId() + " not found.");
				return ResponseCode.RECORD_NOT_FOUND;
			}
		} catch (IOException e) {
			System.err.println("Error updating order: " + e.getMessage());
			return ResponseCode.IO_EXCEPTION;
		} catch (JSONException e) {
			System.err.println("Error processing JSON data: " + e.getMessage());
			return ResponseCode.JSON_EXCEPTION;
		}
	}

	// Method to delete an order from the text file
	public ResponseCode deleteOrder(String id) {

		String filePath = SYS_PATH + "order.txt";

		List<String> updatedLines = new ArrayList<>();
		boolean isDeleted = false;

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = reader.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				// Keep orders that do NOT match the ID
				if (!json.getString("id").equals(id)) {
					updatedLines.add(json.toString());
				} else {
					isDeleted = true;  // Mark the order as deleted
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading orders from file: " + e.getMessage());
			return ResponseCode.IO_EXCEPTION;
		} catch (JSONException e) {
			System.err.println("Error parsing JSON: " + e.getMessage());
			return ResponseCode.JSON_EXCEPTION;
		}

		// Write the remaining orders back to the file
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
			for (String updatedLine : updatedLines) {
				writer.write(updatedLine);
				writer.newLine();
			}

			if (isDeleted) {
				System.out.println("Order with ID " + id + " deleted successfully.");
				return ResponseCode.SUCCESS;
			} else {
				System.out.println("Order with ID " + id + " not found.");
				return ResponseCode.RECORD_NOT_FOUND;
			}
		} catch (IOException e) {
			System.err.println("Error writing orders to file: " + e.getMessage());
			return ResponseCode.IO_EXCEPTION;
		}
	}

}
