package service.address;

import org.json.JSONObject;

import dto.AddressDTO;
import enumeration.ResponseCode;
import enumeration.ServiceType;
import service.utils.IdGenerationUtils;

import java.io.*;
import java.util.*;

public class AddressService {

    private static final String SYS_PATH = "src\\main\\resources\\database\\address\\";
    private static final String FILE_PATH = SYS_PATH + "address.txt";

    // ======= CREATE =======
    public static ResponseCode createAddress(AddressDTO address) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            JSONObject json = new JSONObject();
            json.put("id", IdGenerationUtils.getNextId(ServiceType.ADDRESS, null, null));
            json.put("userId", address.getUserId());
            json.put("street", address.getStreet());
            json.put("city", address.getCity());
            json.put("state", address.getState());
            json.put("postalCode", address.getPostalCode());
            json.put("country", address.getCountry());

            writer.write(json.toString());
            writer.newLine();
            return ResponseCode.SUCCESS;
        } catch (IOException e) {
            System.err.println("Error saving address: " + e.getMessage());
            return ResponseCode.IO_EXCEPTION;
        }
    }

    // ======= READ =======
    public static AddressDTO getAddressById(String id) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                JSONObject json = new JSONObject(line);
                if (json.getString("id").equals(id)) {
                    return createAddressFromJson(json);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading address: " + e.getMessage());
        }
        return null;
    }

    public static List<AddressDTO> getAllAddresses(String userId) {
        List<AddressDTO> addresses = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                JSONObject json = new JSONObject(line);
                if (json.getString("userId").equals(userId)) {
                    addresses.add(createAddressFromJson(json));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading addresses: " + e.getMessage());
        }
        return addresses;
    }

    // ======= UPDATE =======
    public static ResponseCode updateAddress(AddressDTO updatedAddress) {
        if (updatedAddress == null || updatedAddress.getId() == null) {
            return ResponseCode.INVALID_INPUT;
        }
    
        List<String> updatedLines = new ArrayList<>();
        boolean found = false;
    
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                JSONObject json = new JSONObject(line);
                if (json.getString("id").equals(updatedAddress.getId())) {
                    updatedLines.add(convertToJson(updatedAddress).toString());
                    found = true;
                } else {
                    updatedLines.add(line);
                }
            }
    
            if (found) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
                    for (String updatedLine : updatedLines) {
                        writer.write(updatedLine);
                        writer.newLine();
                    }
                    return ResponseCode.SUCCESS;
                }
            }
        } catch (IOException e) {
            System.err.println("Error updating address: " + e.getMessage());
            return ResponseCode.IO_EXCEPTION;
        }
    
        return ResponseCode.RECORD_NOT_FOUND;
    }

    // ======= DELETE =======
    public static ResponseCode deleteAddress(String id) {
        if (id == null || id.trim().isEmpty()) {
            return ResponseCode.INVALID_INPUT;
        }
    
        List<String> remainingLines = new ArrayList<>();
        boolean found = false;
    
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                JSONObject json = new JSONObject(line);
                if (!json.getString("id").equals(id)) {
                    remainingLines.add(line);
                } else {
                    found = true;
                }
            }
    
            if (found) {
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
                    for (String remainingLine : remainingLines) {
                        writer.write(remainingLine);
                        writer.newLine();
                    }
                    return ResponseCode.SUCCESS;
                }
            }
        } catch (IOException e) {
            System.err.println("Error deleting address: " + e.getMessage());
            return ResponseCode.IO_EXCEPTION;
        }
    
        return ResponseCode.RECORD_NOT_FOUND;
    }

    // ======= Helper Methods =======
    private static AddressDTO createAddressFromJson(JSONObject json) {
        AddressDTO address = new AddressDTO();
        address.setId(json.getString("id"));
        address.setUserId(json.getString("userId"));
        address.setStreet(json.getString("street"));
        address.setCity(json.getString("city"));
        address.setState(json.getString("state"));
        address.setPostalCode(json.getString("postalCode"));
        address.setCountry(json.getString("country"));
        return address;
    }

    private static JSONObject convertToJson(AddressDTO address) {
        JSONObject json = new JSONObject();
        json.put("id", address.getId());
        json.put("userId", address.getUserId());
        json.put("street", address.getStreet());
        json.put("city", address.getCity());
        json.put("state", address.getState());
        json.put("postalCode", address.getPostalCode());
        json.put("country", address.getCountry());
        return json;
    }

    public static String concatAddress(AddressDTO address) {
        return address.getStreet() + ", " + address.getPostalCode() + " " + address.getCity() + ", " + address.getState() + ", " + address.getCountry();
    }
}
