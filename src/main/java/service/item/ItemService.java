package service.item;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import dto.ItemDTO;

public class ItemService {
	
	private static final String SYS_PATH = "/FoodHero/src/main/resources/database/";

	// Method to create an item and save it to a text file in JSON format
	public void createItem(ItemDTO item) {
		
	    String filePath = SYS_PATH + "item.txt";

	    // Construct JSON Object
	    JSONObject json = new JSONObject();
	    json.put("id", item.getId());
	    json.put("name", item.getName());
	    json.put("price", item.getPrice());
	    json.put("vendorId", item.getVendorId());
	    json.put("description", item.getDescription());
	    json.put("availability", item.isAvailability());

	    // Write JSON to text file
	    try (FileWriter file = new FileWriter(filePath, true)) {
	        file.write(json.toString() + System.lineSeparator());
	        System.out.println("Item created successfully!");
	    } catch (IOException e) {
	        System.err.println("Error writing item to file: " + e.getMessage());
	    }
	}
	
	// Method to read item information from a text file
	public ItemDTO readItem(String id) {
		
	    String filePath = SYS_PATH + "item.txt";

	    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            JSONObject json = new JSONObject(line);
	            if (json.getString("id").equals(id)) {
	                // Create Item object from JSON data
	                ItemDTO item = new ItemDTO();
	                item.setId(json.getString("id"));
	                item.setName(json.getString("name"));
	                item.setPrice(json.getDouble("price"));
	                item.setVendorId(json.getString("vendorId"));
	                item.setDescription(json.getString("description"));
	                item.setAvailability(json.getBoolean("availability"));

	                return item; // Return the found item
	            }
	        }
	    } catch (IOException e) {
	        System.err.println("Error reading item from file: " + e.getMessage());
	    } catch (JSONException e) {
	        System.err.println("Error parsing JSON data: " + e.getMessage());
	    }

	    System.out.println("Item with ID " + id + " not found.");
	    return null; // Return null if the item is not found
	}
	
	// Method to read all items from the text file
    public List<ItemDTO> readAllItem() {
        String filePath = SYS_PATH + "item.txt";
        List<ItemDTO> items = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                JSONObject json = new JSONObject(line);

                ItemDTO item = new ItemDTO();
                item.setId(json.getString("id"));
                item.setName(json.getString("name"));
                item.setPrice(json.getDouble("price"));
                item.setVendorId(json.getString("vendorId"));
                item.setDescription(json.getString("description"));
                item.setAvailability(json.getBoolean("availability"));

                items.add(item); // Add to the list
            }
        } catch (IOException e) {
            System.err.println("Error reading items from file: " + e.getMessage());
        } catch (JSONException e) {
            System.err.println("Error parsing JSON: " + e.getMessage());
        }

        return items; // Return the list of items
    }
	
	// Method to update an item in the text file
	public void updateItem(ItemDTO updatedItem) {
		
	    String filePath = SYS_PATH + "item.txt";
	    
	    List<String> fileContent = new ArrayList<>();

	    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
	        String line;

	        // Read all lines and update the matching item
	        while ((line = reader.readLine()) != null) {
	            JSONObject json = new JSONObject(line);

	            if (json.getString("id").equals(updatedItem.getId())) {
	                // Update the item's details
	                json.put("name", updatedItem.getName());
	                json.put("price", updatedItem.getPrice());
	                json.put("vendorId", updatedItem.getVendorId());
	                json.put("description", updatedItem.getDescription());
	                json.put("availability", updatedItem.isAvailability());
	            }
	            fileContent.add(json.toString());
	        }
	    } catch (IOException | JSONException e) {
	        System.err.println("Error reading item file: " + e.getMessage());
	        return;
	    }

	    // Write the updated content back to the file
	    try (FileWriter writer = new FileWriter(filePath, false)) { // Overwrite file
	        for (String content : fileContent) {
	            writer.write(content + System.lineSeparator());
	        }
	        System.out.println("Item updated successfully!");
	    } catch (IOException e) {
	        System.err.println("Error writing updated item to file: " + e.getMessage());
	    }
	}
	
	// Method to delete an item from the text file
	public void deleteItem(String itemId) {
		
	    String filePath = SYS_PATH + "item.txt";
	    
	    List<String> fileContent = new ArrayList<>();
	    boolean itemFound = false;

	    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
	        String line;

	        // Read all items and exclude the one to be deleted
	        while ((line = reader.readLine()) != null) {
	            JSONObject json = new JSONObject(line);
	            if (!json.getString("id").equals(itemId)) {
	                fileContent.add(json.toString());
	            } else {
	                itemFound = true; // Item to delete found
	            }
	        }
	    } catch (IOException | JSONException e) {
	        System.err.println("Error reading item file: " + e.getMessage());
	        return;
	    }

	    if (!itemFound) {
	        System.out.println("Item with ID " + itemId + " not found.");
	        return;
	    }

	    // Write the updated content back to the file
	    try (FileWriter writer = new FileWriter(filePath, false)) { // Overwrite file
	        for (String content : fileContent) {
	            writer.write(content + System.lineSeparator());
	        }
	        System.out.println("Item deleted successfully!");
	    } catch (IOException e) {
	        System.err.println("Error writing to file after deletion: " + e.getMessage());
	    }
	}

}
