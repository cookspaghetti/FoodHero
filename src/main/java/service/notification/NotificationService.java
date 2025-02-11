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

public class NotificationService {

	private static final String SYS_PATH = "src\\main\\resources\\database\\notification\\";

	// Method to create a notification and save it to a text file in JSON format
	public void createNotification(NotificationDTO notification) {

		String filePath = SYS_PATH + "notification.txt";

		// Construct JSON Object
		JSONObject json = new JSONObject();
		json.put("id", notification.getId());
		json.put("recipientId", notification.getUserId());
		json.put("title", notification.getTitle());
		json.put("message", notification.getMessage());
		json.put("timestamp", notification.getTimestamp().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
		json.put("isRead", notification.isRead());

		// Write JSON to text file
		try (FileWriter file = new FileWriter(filePath, true)) {
			file.write(json.toString() + System.lineSeparator());
			System.out.println("Notification created successfully!");
		} catch (IOException e) {
			System.err.println("Error writing notification to file: " + e.getMessage());
		}
	}

	// Method to read a notification from the text file
	public NotificationDTO readNotification(String userId) {
		String filePath = SYS_PATH + "notification.txt";

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
					notification.setTimestamp(LocalDateTime.parse(json.getString("timestamp"), DateTimeFormatter.ISO_LOCAL_DATE_TIME));
					notification.setRead(json.getBoolean("isRead"));

					return notification;
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading notification from file: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Error parsing JSON: " + e.getMessage());
		}

		System.out.println("Notification for User " + userId + " not found.");
		return null;
	}

	// Method to delete a notification from the text file
    public void deleteNotification(String id) {
    	
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
            return;
        }

        // Write back the updated list without the deleted notification
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String notification : updatedNotifications) {
                writer.write(notification);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing updated notifications to file: " + e.getMessage());
            return;
        }

        if (isDeleted) {
            System.out.println("Notification with ID " + id + " deleted successfully.");
        } else {
            System.out.println("Notification with ID " + id + " not found.");
        }
    }
}
