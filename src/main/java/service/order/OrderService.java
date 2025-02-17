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

import dto.OrderDTO;
import enumeration.OrderStatus;
import enumeration.ResponseCode;

public class OrderService {

	private static final String SYS_PATH = "src\\main\\resources\\database\\order\\";

	// Method to create an order and save to a text file in JSON format
	public ResponseCode createOrder(OrderDTO order) {

		String filePath = SYS_PATH + "order.txt";

		// Construct JSON Object
		JSONObject json = new JSONObject();
		json.put("id", order.getId());
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
	public List<OrderDTO> readAllOrder() {
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
	public ResponseCode updateOrder(String id, OrderDTO updatedOrder) {

		String filePath = SYS_PATH + "order.txt";

		List<String> updatedLines = new ArrayList<>();
		boolean isUpdated = false;

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = reader.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				if (json.getString("id").equals(id)) {
					// Update the fields with values from updatedOrder
					json.put("customerId", updatedOrder.getCustomerId());
					json.put("vendorId", updatedOrder.getVendorId());
					json.put("runnerId", updatedOrder.getRunnerId());
					json.put("status", updatedOrder.getStatus());
					json.put("totalAmount", updatedOrder.getTotalAmount());
					json.put("deliveryFee", updatedOrder.getDeliveryFee());
					json.put("placementTime", updatedOrder.getPlacementTime().toString());
					json.put("completionTime", updatedOrder.getCompletionTime() != null 
							? updatedOrder.getCompletionTime().toString() 
									: "");
					json.put("notes", updatedOrder.getNotes());
					json.put("deliveryAddress", updatedOrder.getDeliveryAddress());

					// Convert items HashMap to JSONObject
					JSONObject itemsJson = new JSONObject();
					for (Map.Entry<String, Integer> entry : updatedOrder.getItems().entrySet()) {
						itemsJson.put(entry.getKey(), entry.getValue());
					}
					json.put("items", itemsJson);

					isUpdated = true;
				}

				updatedLines.add(json.toString()); // Add the (updated or original) order to the list
			}
		} catch (IOException e) {
			System.err.println("Error reading orders from file: " + e.getMessage());
			return ResponseCode.IO_EXCEPTION;
		} catch (JSONException e) {
			System.err.println("Error parsing JSON: " + e.getMessage());
			return ResponseCode.JSON_EXCEPTION;
		}

		// Write the updated data back to the file
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
			for (String updatedLine : updatedLines) {
				writer.write(updatedLine);
				writer.newLine();
			}

			if (isUpdated) {
				System.out.println("Order with ID " + id + " updated successfully.");
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
