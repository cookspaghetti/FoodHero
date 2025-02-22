package service.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONObject;

import enumeration.ServiceType;
import service.general.SessionControlService;

public class IdGenerationUtils {

	public static String getNextId(ServiceType type, String vendorId, String runnerId) {

		String filePath;
		String abbreviation = "ERR"; // e.g adm for admin, mgr for manager
		String nextId;

		switch (type) {
		case ADMIN: 
			filePath = "src\\main\\resources\\database\\user\\admin.txt";
			abbreviation = "ADM";
			break;
		case MANAGER: 
			filePath = "src\\main\\resources\\database\\user\\manager.txt";
			abbreviation = "MGR";
			break;
		case CUSTOMER: 
			filePath = "src\\main\\resources\\database\\user\\customer.txt";
			abbreviation = "CUS";
			break;
		case VENDOR: 
			filePath = "src\\main\\resources\\database\\user\\vendor.txt";
			abbreviation = "VDR";
			break;
		case RUNNER: 
			filePath = "src\\main\\resources\\database\\user\\runner.txt";
			abbreviation = "RUN";
			break;
		case ITEM: 
			filePath = "src\\main\\resources\\database\\item\\vendor_" + SessionControlService.getId() + "_items.txt";
			abbreviation = "ITM";
			break;
		case ORDER:
			filePath = "src\\main\\resources\\database\\order\\order.txt";
			abbreviation = "ORD";
			break;
		case VENDOR_REVIEW:
			filePath = "src\\main\\resources\\database\\review\\vendor_" + vendorId + ".txt";
			abbreviation = "VRE";
			break;
		case RUNNER_REVIEW:
			filePath = "src\\main\\resources\\database\\review\\runner_" + runnerId + ".txt";
			abbreviation = "RRE";
			break;
		case ADD_TRANSACTION:
			filePath = "src\\main\\resources\\database\\transaction\\add_transaction.txt";
			abbreviation = "ADD";
			break;
		case DEDUCT_TRANSACTION:
			filePath = "src\\main\\resources\\database\\transaction\\deduct_transaction.txt";
			abbreviation = "DED";
			break;
		case TASK:
			filePath = "src\\main\\resources\\database\\task\\task.txt";
			abbreviation = "TAK";
			break;
		case COMPLAIN:
			filePath = "src\\main\\resources\\database\\complain\\complain.txt";
			abbreviation = "COM";
			break;
		case NOTIFICATION:
			filePath = "src\\main\\resources\\database\\notification\\notification.txt";
			abbreviation = "NOT";
			break;
		case ADDRESS:
			filePath = "src\\main\\resources\\database\\address\\address.txt";
			abbreviation = "ADR";
			break;
		default:
			filePath = "ERR";
		}

		int maxId = 0;

		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				JSONObject json = new JSONObject(line);
				String id = json.getString("id");

				// Extract numeric part from IDs
				int currentId = Integer.parseInt(id.substring(3));
				if (currentId > maxId) {
					maxId = currentId;
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading admin file for ID generation: " + e.getMessage());
		}
		maxId = maxId + 1;
		if (maxId < 10) {
		    nextId = abbreviation + "0000" + maxId;          // e.g., ABC00001
		} else if (maxId < 100) {
		    nextId = abbreviation + "000" + maxId;           // e.g., ABC00012
		} else if (maxId < 1000) {
		    nextId = abbreviation + "00" + maxId;            // e.g., ABC00123
		} else if (maxId < 10000) {
		    nextId = abbreviation + "0" + maxId;             // e.g., ABC01234
		} else {
		    nextId = abbreviation + maxId;                   // e.g., ABC12345
		}
		
		return nextId;
	}
}
