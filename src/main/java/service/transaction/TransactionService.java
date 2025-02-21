package service.transaction;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import dto.AddTransactionDTO;
import dto.DeductTransactionDTO;
import dto.TransactionDTO;
import enumeration.PaymentMethod;
import enumeration.ResponseCode;

public class TransactionService {

	private static final String SYS_PATH = "src\\main\\resources\\database\\transaction\\";

	// Method to create an AddTransaction and save to a text file in JSON format
	public static ResponseCode createAddTransaction(AddTransactionDTO transaction) {

		String filePath = SYS_PATH + "add_transaction.txt";

		// Construct JSON Object
		JSONObject json = new JSONObject();
		json.put("id", transaction.getId());
		json.put("customerId", transaction.getCustomerId());
		json.put("amount", transaction.getAmount());
		json.put("date", transaction.getDate().toString());
		json.put("description", transaction.getDescription());
		json.put("adminId", transaction.getAdminId());
		json.put("paymentMethod", transaction.getPaymentMethod());

		// Write JSON to text file
		try (FileWriter file = new FileWriter(filePath, true)) {
			file.write(json.toString() + System.lineSeparator());
			System.out.println("AddTransaction created successfully!");
			return ResponseCode.SUCCESS;
		} catch (IOException e) {
			System.err.println("Error writing AddTransaction to file: " + e.getMessage());
			return ResponseCode.IO_EXCEPTION;
		}
	}

	// Method to read an AddTransaction from the text file
	public AddTransactionDTO readAddTransaction(String id) {

		String filePath = SYS_PATH + "add_transaction.txt";

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = reader.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				if (json.getString("id").equals(id)) {
					AddTransactionDTO transaction = new AddTransactionDTO();
					transaction.setId(json.getString("id"));
					transaction.setCustomerId(json.getString("customerId"));
					transaction.setAmount(json.getDouble("amount"));
					transaction.setDate(LocalDateTime.parse(json.getString("date")));
					transaction.setDescription(json.getString("description"));
					transaction.setAdminId(json.getString("adminId"));
					transaction.setPaymentMethod(PaymentMethod.valueOf(json.getString("paymentMethod")));

					return transaction;
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading AddTransaction from file: " + e.getMessage());
		} catch (JSONException e) {
			System.err.println("Error parsing JSON: " + e.getMessage());
		}

		System.out.println("AddTransaction with ID " + id + " not found.");
		return null;
	}

	// Method to read all AddTransaction from the text file
	public List<AddTransactionDTO> readAllAddTransaction(String customerId) {

		String filePath = SYS_PATH + "add_transaction.txt";

		List<AddTransactionDTO> addTransactions = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = reader.readLine()) != null) {

				try {
					JSONObject json = new JSONObject(line);

					if (json.getString("customerId").equals(customerId)) {
						AddTransactionDTO transaction = new AddTransactionDTO();
						transaction.setId(json.getString("id"));
						transaction.setCustomerId(json.getString("customerId"));
						transaction.setAmount(json.getDouble("amount"));
						transaction.setDate(LocalDateTime.parse(json.getString("date")));
						transaction.setDescription(json.getString("description"));
						transaction.setAdminId(json.getString("adminId"));
						transaction.setPaymentMethod(PaymentMethod.valueOf(json.getString("paymentMethod")));

						addTransactions.add(transaction);
					}

				} catch (JSONException e) {
					System.err.println("Error parsing AddTransaction JSON: " + e.getMessage());
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading AddTransaction from file: " + e.getMessage());
		}

		return addTransactions;
	}

	// Method to delete an AddTransaction from the text file
	public ResponseCode deleteAddTransaction(String id) {

		String filePath = SYS_PATH + "add_transaction.txt";

		List<String> updatedTransactions = new ArrayList<>();
		boolean isDeleted = false;

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = reader.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				if (!json.getString("id").equals(id)) {
					updatedTransactions.add(line); // Keep transactions with different IDs
				} else {
					isDeleted = true; // Mark as deleted
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading AddTransaction from file: " + e.getMessage());
			return ResponseCode.IO_EXCEPTION;
		}

		if (isDeleted) {
			try (FileWriter writer = new FileWriter(filePath, false)) { // Overwrite the file
				for (String transaction : updatedTransactions) {
					writer.write(transaction + System.lineSeparator());
				}
				System.out.println("AddTransaction with ID " + id + " deleted successfully!");
				return ResponseCode.SUCCESS;
			} catch (IOException e) {
				System.err.println("Error writing AddTransaction to file: " + e.getMessage());
				return ResponseCode.IO_EXCEPTION;
			}
		} else {
			System.out.println("AddTransaction with ID " + id + " not found.");
			return ResponseCode.RECORD_NOT_FOUND;
		}
	}

	// Method to create a DeductTransaction and save it to a text file in JSON format
	public ResponseCode createDeductTransaction(DeductTransactionDTO transaction) {

		String filePath = SYS_PATH + "deduct_transaction.txt";

		// Construct JSON Object
		JSONObject json = new JSONObject();
		json.put("id", transaction.getId());
		json.put("customerId", transaction.getCustomerId());
		json.put("amount", transaction.getAmount());
		json.put("date", transaction.getDate().toString());
		json.put("description", transaction.getDescription());
		json.put("orderId", transaction.getOrderId());

		// Write JSON to text file
		try (FileWriter file = new FileWriter(filePath, true)) {
			file.write(json.toString() + System.lineSeparator());
			System.out.println("DeductTransaction created successfully!");
			return ResponseCode.SUCCESS;
		} catch (IOException e) {
			System.err.println("Error writing DeductTransaction to file: " + e.getMessage());
			return ResponseCode.IO_EXCEPTION;
		}
	}

	// Method to read a DeductTransaction from the text file
	public DeductTransactionDTO readDeductTransaction(String id) {

		String filePath = SYS_PATH + "deduct_transaction.txt";

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = reader.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				if (json.getString("id").equals(id)) {
					DeductTransactionDTO transaction = new DeductTransactionDTO();
					transaction.setId(json.getString("id"));
					transaction.setCustomerId(json.getString("customerId"));
					transaction.setAmount(json.getDouble("amount"));
					transaction.setDate(LocalDateTime.parse(json.getString("date")));
					transaction.setDescription(json.getString("description"));
					transaction.setOrderId(json.getString("orderId"));

					return transaction;
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading DeductTransaction from file: " + e.getMessage());
		} catch (JSONException e) {
			System.err.println("Error parsing JSON: " + e.getMessage());
		}

		System.out.println("DeductTransaction with ID " + id + " not found.");
		return null;
	}

	// Method to read all DeductTransaction from the text file
	public List<DeductTransactionDTO> readAllDeductTransaction(String customerId) {

		String filePath = SYS_PATH + "deduct_transaction.txt";

		List<DeductTransactionDTO> deductTransactions = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = reader.readLine()) != null) {

				try { 
					JSONObject json = new JSONObject(line);

					if (json.getString("customerId").equals(customerId)) {
						DeductTransactionDTO transaction = new DeductTransactionDTO();
						transaction.setId(json.getString("id"));
						transaction.setCustomerId(json.getString("customerId"));
						transaction.setAmount(json.getDouble("amount"));
						transaction.setDate(LocalDateTime.parse(json.getString("date")));
						transaction.setDescription(json.getString("description"));
						transaction.setOrderId(json.getString("orderId"));

						deductTransactions.add(transaction);
					}
				} catch (JSONException e) {
					System.err.println("Error parsing JSON: " + e.getMessage());
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading DeductTransaction from file: " + e.getMessage());
		}

		return deductTransactions;
	}

	// Method to delete a DeductTransaction from the text file
	public ResponseCode deleteDeductTransaction(String id) {

		String filePath = SYS_PATH + "deduct_transaction.txt";

		List<String> updatedTransactions = new ArrayList<>();
		boolean found = false;

		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;

			while ((line = reader.readLine()) != null) {
				JSONObject json = new JSONObject(line);

				if (json.getString("id").equals(id)) {
					found = true; // Transaction found, skip adding to updated list
				} else {
					updatedTransactions.add(line); // Keep other transactions
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading DeductTransaction from file: " + e.getMessage());
			return ResponseCode.IO_EXCEPTION;
		}

		if (found) {
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
				for (String transaction : updatedTransactions) {
					writer.write(transaction);
					writer.newLine();
				}
				System.out.println("DeductTransaction with ID " + id + " deleted successfully.");
				return ResponseCode.SUCCESS;
			} catch (IOException e) {
				System.err.println("Error writing DeductTransaction to file: " + e.getMessage());
				return ResponseCode.IO_EXCEPTION;
			}
		} else {
			System.out.println("DeductTransaction with ID " + id + " not found.");
			return ResponseCode.RECORD_NOT_FOUND;
		}
	}

	// Method to read all transactions from both files
	public List<TransactionDTO> readAllTransaction(String customerId) {
		List<TransactionDTO> transactions = new ArrayList<>();
		transactions.addAll(readAllAddTransaction(customerId));
		transactions.addAll(readAllDeductTransaction(customerId));
		return transactions;
	}
}
