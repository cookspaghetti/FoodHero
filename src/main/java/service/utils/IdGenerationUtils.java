package service.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.json.JSONObject;

import enumeration.ServiceType;
import service.general.SessionControlService;

public class IdGenerationUtils {

	public String getNextId(ServiceType type, String vendorId, String runnerId) {

		String filePath;
		String abbreviation = "ERR"; // e.g adm for admin, mgr for manager
		String nextId;

		switch (type) {
		case ADMIN: 
			filePath = "src\\main\\resources\\database\\user\\admin.txt";
			abbreviation = "ADM";
		case MANAGER: 
			filePath = "src\\main\\resources\\database\\user\\manager.txt";
			abbreviation = "MGR";
		case CUSTOMER: 
			filePath = "src\\main\\resources\\database\\user\\customer.txt";
			abbreviation = "CUS";
		case VENDOR: 
			filePath = "src\\main\\resources\\database\\user\\vendor.txt";
			abbreviation = "VDR";
		case RUNNER: 
			filePath = "src\\main\\resources\\database\\user\\vendor.txt";
			abbreviation = "RUN";
		case ITEM: 
			filePath = "src\\main\\resources\\database\\item\\item_" + SessionControlService.getId() + ".txt";
			abbreviation = "ITM";
		case VENDOR_REVIEW:
			filePath = "src\\main\\resources\\database\\review\\vendor_" + vendorId + ".txt";
			abbreviation = "VRE";
		case RUNNER_REVIEW:
			filePath = "src\\main\\resources\\database\\review\\runner_" + runnerId + ".txt";
			abbreviation = "RRE";
		case ADD_TRANSACTION:
			filePath = "src\\main\\resources\\database\\transaction\\add_transaction.txt";
			abbreviation = "ADD";
		case DEDUCT_TRANSACTION:
			filePath = "src\\main\\resources\\database\\transaction\\deduct_transaction.txt";
			abbreviation = "DED";
		case TASK:
			filePath = "src\\main\\resources\\database\\item\\task\\task.txt";
			abbreviation = "TAK";
		case COMPLAIN:
			filePath = "src\\main\\resources\\database\\complain\\complain.txt";
			abbreviation = "COM";
		case NOTIFICATION:
			filePath = "src\\main\\resources\\database\\notification\\notification.txt";
			abbreviation = "NOT";
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
