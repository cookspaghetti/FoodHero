package service.general;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import dto.AdminDTO;
import dto.CustomerDTO;
import dto.ManagerDTO;
import dto.RunnerDTO;
import dto.VendorDTO;
import enumeration.ResponseCode;
import enumeration.Role;
import service.utils.JsonUtils;

public class LoginService {

	private static final String SYS_PATH = "src\\main\\resources\\database\\user\\";

	private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

	public static ResponseCode login(Role role, String email, String password) {

		String filePath = SYS_PATH + getFileNameByRole(role);

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = reader.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				// Check for matching email and password
				if (json.getString("emailAddress").equals(email) && json.getString("password").equals(password)) {
					System.out.println(role + " logged in successfully!");
					setCurrentUserInfo(role, json);
					System.out.println("Current session: " + SessionControlService.getRole());
					return ResponseCode.SUCCESS;
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading from file: " + e.getMessage());
			return ResponseCode.IO_EXCEPTION;
		}

		System.out.println("Login failed for role: " + role);
		return ResponseCode.RECORD_NOT_FOUND;
	}

	// Get file name based on enum value
	private static String getFileNameByRole(Role role) {
		return switch (role) {
		case ADMIN -> "admin.txt";
		case VENDOR -> "vendor.txt";
		case RUNNER -> "runner.txt";
		case CUSTOMER -> "customer.txt";
		case MANAGER -> "manager.txt";
		};
	}

	// validate email input
	public static boolean validateEmail(String email) {
		Pattern pattern = Pattern.compile(EMAIL_REGEX);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	// Session Control
	private static void setCurrentUserInfo(Role role, JSONObject json) {

		switch (role) {
		case ADMIN:
			AdminDTO admin = new AdminDTO();
			admin.setId(json.getString("id"));
			admin.setName(json.getString("name"));
			admin.setPhoneNumber(json.getString("phoneNumber"));
			admin.setAddressId(json.getString("addressId"));
			admin.setEmailAddress(json.getString("emailAddress"));
			admin.setPassword(json.getString("password"));
			admin.setStatus(json.getBoolean("status"));

			SessionControlService.setSession(admin);
			break;

		case MANAGER:
			ManagerDTO manager = new ManagerDTO();
			manager.setId(json.getString("id"));
			manager.setName(json.getString("name"));
			manager.setPhoneNumber(json.getString("phoneNumber"));
			manager.setAddressId(json.getString("addressId"));
			manager.setEmailAddress(json.getString("emailAddress"));
			manager.setPassword(json.getString("password"));
			manager.setStatus(json.getBoolean("status"));

			SessionControlService.setSession(manager);
			break;

		case RUNNER:
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

			// Convert JSON arrays to List<String>
			runner.setTasks(JsonUtils.jsonArrayToList(json.getJSONArray("tasks")));
			runner.setReviews(JsonUtils.jsonArrayToList(json.getJSONArray("reviews")));

			runner.setLastDeliveredAddress(json.getString("lastDeliveredAddress"));

			// Parse lastDeliveryDate as LocalDateTime
			String dateStr = json.optString("lastDeliveryDate", null);
			if (dateStr != null && !dateStr.isEmpty()) {
				runner.setLastDeliveryDate(LocalDateTime.parse(dateStr));
			}

			SessionControlService.setSession(runner);
			break;

		case VENDOR:
			VendorDTO vendor = new VendorDTO();
			vendor.setId(json.getString("id"));
			vendor.setName(json.getString("name"));
			vendor.setPhoneNumber(json.getString("phoneNumber"));
			vendor.setAddressId(json.getString("addressId"));
			vendor.setEmailAddress(json.getString("emailAddress"));
			vendor.setStatus(json.getBoolean("status"));
			vendor.setVendorName(json.getString("vendorName"));
			vendor.setRatings(json.getDouble("ratings"));
			vendor.setPassword(json.getString("password"));

			// Convert JSON Object (items) to HashMap<String, Integer>
			HashMap<String, Integer> itemsMap = new HashMap<>();
			JSONObject itemsJson = json.getJSONObject("items");
			for (String key : itemsJson.keySet()) {
				itemsMap.put(key, itemsJson.getInt(key));
			}
			vendor.setItems(itemsMap);

			// Convert JSON arrays to List<String>
			vendor.setOrderHistory(JsonUtils.jsonArrayToList(json.getJSONArray("orderHistory")));
			vendor.setReviews(JsonUtils.jsonArrayToList(json.getJSONArray("reviews")));

			SessionControlService.setSession(vendor);
			break;

		case CUSTOMER:
			CustomerDTO customer = new CustomerDTO();
			customer.setId(json.getString("id"));
			customer.setName(json.getString("name"));
			customer.setPhoneNumber(json.getString("phoneNumber"));
			customer.setAddressId(json.optString("addressId", ""));            // Fixed field name
			customer.setEmailAddress(json.optString("emailAddress", ""));      // Fixed field name
			customer.setPassword(json.optString("password", ""));              // Added password
			customer.setStatus(json.optBoolean("status", true));               // Added status with default true
			customer.setCredit(json.optDouble("credit", 0.0));

			// Convert JSON arrays to List<String> with null safety
			customer.setOrderHistory(JsonUtils.jsonArrayToList(json.optJSONArray("orderHistory")));
			customer.setVendorReviews(JsonUtils.jsonArrayToList(json.optJSONArray("vendorReviews")));
			customer.setRunnerReviews(JsonUtils.jsonArrayToList(json.optJSONArray("runnerReviews")));
			customer.setComplains(JsonUtils.jsonArrayToList(json.optJSONArray("complains")));
			customer.setTransactions(JsonUtils.jsonArrayToList(json.optJSONArray("transactions")));
			customer.setDeliveryAddresses(JsonUtils.jsonArrayToList(json.optJSONArray("deliveryAddresses")));

			SessionControlService.setSession(customer);
			break;

		default:
			System.out.println("Invalid role.");
		}
	}

}
