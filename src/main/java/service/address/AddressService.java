package service.address;

import org.json.JSONObject;

import dto.AddressDTO;
import enumeration.ResponseCode;

import java.io.*;
import java.util.*;

public class AddressService {

    private static final String SYS_PATH = "src\\main\\resources\\database\\address\\";
    private static final String FILE_PATH = SYS_PATH + "address.txt";

    // ======= CREATE =======
    public static ResponseCode saveAddress(AddressDTO address) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            JSONObject json = new JSONObject();
            json.put("id", address.getId());
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

    public static List<AddressDTO> getAllAddresses() {
        List<AddressDTO> addresses = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                addresses.add(createAddressFromJson(new JSONObject(line)));
            }
        } catch (IOException e) {
            System.err.println("Error reading addresses: " + e.getMessage());
        }
        return addresses;
    }

    // ======= UPDATE =======
    public static ResponseCode updateAddress(AddressDTO updatedAddress) {
        List<AddressDTO> addresses = getAllAddresses();
        boolean found = false;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (AddressDTO address : addresses) {
                if (address.getId().equals(updatedAddress.getId())) {
                    writer.write(convertToJson(updatedAddress).toString());
                    found = true;
                } else {
                    writer.write(convertToJson(address).toString());
                }
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error updating address: " + e.getMessage());
            return ResponseCode.IO_EXCEPTION;
        }
        return found ? ResponseCode.SUCCESS : ResponseCode.RECORD_NOT_FOUND;
    }

    // ======= DELETE =======
    public static ResponseCode deleteAddress(String id) {
        List<AddressDTO> addresses = getAllAddresses();
        boolean found = addresses.removeIf(address -> address.getId().equals(id));

        if (found) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
                for (AddressDTO address : addresses) {
                    writer.write(convertToJson(address).toString());
                    writer.newLine();
                }
            } catch (IOException e) {
                System.err.println("Error deleting address: " + e.getMessage());
                return ResponseCode.IO_EXCEPTION;
            }
        }
        return found ? ResponseCode.SUCCESS : ResponseCode.RECORD_NOT_FOUND;
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
