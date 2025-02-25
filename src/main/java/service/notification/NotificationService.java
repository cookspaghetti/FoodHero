package service.notification;

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

import dto.NotificationDTO;
import enumeration.ResponseCode;
import enumeration.ServiceType;
import service.utils.IdGenerationUtils;

public class NotificationService {

	private static final String SYS_PATH = "src\\main\\resources\\database\\notification\\";

	// Method to create a notification and save it to a text file in JSON format
	public static ResponseCode createNotification(NotificationDTO notification) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		String filePath = SYS_PATH + "notification.txt";

		// Construct JSON Object
		JSONObject json = new JSONObject();
		json.put("id", IdGenerationUtils.getNextId(ServiceType.NOTIFICATION, null, null));
		json.put("userId", notification.getUserId());
		json.put("title", notification.getTitle());
		json.put("message", notification.getMessage());
		json.put("timestamp", notification.getTimestamp().format(formatter));
		json.put("isRead", notification.isRead());

		// Write JSON to text file
		try (FileWriter file = new FileWriter(filePath, true)) {
			file.write(json.toString() + System.lineSeparator());
			System.out.println("Notification created successfully!");
			return ResponseCode.SUCCESS;
		} catch (IOException e) {
			System.err.println("Error writing notification to file: " + e.getMessage());
			return ResponseCode.IO_EXCEPTION;
		}
	}

	// Method to read a notification from the text file
	public NotificationDTO readNotification(String id) {
		String filePath = SYS_PATH + "notification.txt";

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = reader.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				if (json.getString("id").equals(id)) {
					NotificationDTO notification = new NotificationDTO();
					notification.setId(json.getString("id"));
					notification.setUserId(json.getString("userId"));
					notification.setTitle(json.getString("title"));
					notification.setMessage(json.getString("message"));
					notification.setTimestamp(LocalDateTime.parse(json.getString("timestamp"), formatter));
					notification.setRead(json.getBoolean("isRead"));

					return notification;
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading notification from file: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Error parsing JSON: " + e.getMessage());
		}

		System.out.println("Notification with ID " + id + " not found.");
		return null;
	}

	// Method to read all notification from the text file
	public static List<NotificationDTO> readAllNotification(String userId) {
		String filePath = SYS_PATH + "notification.txt";
		
		List<NotificationDTO> notifications = new ArrayList<>();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = reader.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				if (json.getString("userId").equals(userId)) {
					NotificationDTO notification = new NotificationDTO();
					notification.setId(json.getString("id"));
					notification.setUserId(json.getString("userId"));
					notification.setTitle(json.getString("title"));
					notification.setMessage(json.getString("message"));
					notification.setTimestamp(LocalDateTime.parse(json.getString("timestamp"), formatter));
					notification.setRead(json.getBoolean("isRead"));
					
					notifications.add(notification);
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading notification from file: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Error parsing JSON: " + e.getMessage());
		}
		
		if (notifications.isEmpty()) {
			System.out.println("Notification for User " + userId + " not found.");
		}
		
		return notifications;
	}

	// Method to read manager notification from the text file
	public static List<NotificationDTO> readManagerNotification() {
		String filePath = SYS_PATH + "notification.txt";
		
		List<NotificationDTO> notifications = new ArrayList<>();

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = reader.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				if (json.getString("userId").equals("manager")) {
					NotificationDTO notification = new NotificationDTO();
					notification.setId(json.getString("id"));
					notification.setUserId(json.getString("userId"));
					notification.setTitle(json.getString("title"));
					notification.setMessage(json.getString("message"));
					notification.setTimestamp(LocalDateTime.parse(json.getString("timestamp"), formatter));
					notification.setRead(json.getBoolean("isRead"));
					
					notifications.add(notification);
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading notification from file: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Error parsing JSON: " + e.getMessage());
		}
		
		if (notifications.isEmpty()) {
			System.out.println("Notification for Manager not found.");
		}
		
		return notifications;
	}

	// Method to update a notification in the text file
	public static ResponseCode updateNotification(NotificationDTO notification) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		String filePath = SYS_PATH + "notification.txt";

		List<String> updatedNotifications = new ArrayList<>();
		boolean isUpdated = false;

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = reader.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				if (json.getString("id").equals(notification.getId())) {
					// Update the notification
					json.put("userId", notification.getUserId());
					json.put("title", notification.getTitle());
					json.put("message", notification.getMessage());
					json.put("timestamp", notification.getTimestamp().format(formatter));
					json.put("isRead", notification.isRead());
					isUpdated = true;
				}

				updatedNotifications.add(json.toString());
			}
		} catch (IOException e) {
			System.err.println("Error reading notifications from file: " + e.getMessage());
			return ResponseCode.IO_EXCEPTION;
		}

		// Write back the updated list
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
			for (String updatedNotification : updatedNotifications) {
				writer.write(updatedNotification);
				writer.newLine();
			}
		} catch (IOException e) {
			System.err.println("Error writing updated notifications to file: " + e.getMessage());
			return ResponseCode.IO_EXCEPTION;
		}

		if (isUpdated) {
			System.out.println("Notification with ID " + notification.getId() + " updated successfully.");
			return ResponseCode.SUCCESS;
		} else {
			System.out.println("Notification with ID " + notification.getId() + " not found.");
			return ResponseCode.RECORD_NOT_FOUND;
		}
	}

	// Method to delete a notification from the text file
	public ResponseCode deleteNotification(String id) {

		String filePath = SYS_PATH + "notification.txt";

		List<String> updatedNotifications = new ArrayList<>();
		boolean isDeleted = false;

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = reader.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				if (json.getString("id").equals(id)) {
					isDeleted = true; // Found and marked for deletion
				} else {
					updatedNotifications.add(line); // Keep other notifications
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading notifications from file: " + e.getMessage());
			return ResponseCode.IO_EXCEPTION;
		}

		// Write back the updated list without the deleted notification
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
			for (String notification : updatedNotifications) {
				writer.write(notification);
				writer.newLine();
			}
		} catch (IOException e) {
			System.err.println("Error writing updated notifications to file: " + e.getMessage());
			return ResponseCode.IO_EXCEPTION;
		}

		if (isDeleted) {
			System.out.println("Notification with ID " + id + " deleted successfully.");
			return ResponseCode.SUCCESS;
		} else {
			System.out.println("Notification with ID " + id + " not found.");
			return ResponseCode.RECORD_NOT_FOUND;
		}
	}

}
