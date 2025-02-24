package service.distance;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.json.JSONArray;
import org.json.JSONObject;

import dto.AddressDTO;
import service.address.AddressService;

public class DistanceService {
    private static final String API_KEY = "678dc91aa3e5d493836800cqd90d6a4";
    private static final String GEOCODE_API_URL = "https://geocode.maps.co/search";
    private static final double EARTH_RADIUS = 6371; // Radius in kilometers

    public static double getDistance(AddressDTO address1, AddressDTO address2) {
        if (address1 == null || address2 == null) {
            throw new IllegalArgumentException("Addresses cannot be null");
        }

        String address1String = AddressService.concatAddress(address1);
        String address2String = AddressService.concatAddress(address2);

        // Get coordinates for both addresses
        double[] coords1 = getCoordinates(address1String);
        double[] coords2 = getCoordinates(address2String);

        if (coords1 == null || coords2 == null) {
            throw new RuntimeException("Could not get coordinates for addresses");
        }

        // Calculate distance using Haversine formula
        return calculateHaversineDistance(coords1[0], coords1[1], coords2[0], coords2[1]);
    }

    private static double[] getCoordinates(String address) {
        try {
            // Encode address for URL
            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8.toString());
            String urlString = String.format("%s?q=%s&api_key=%s", GEOCODE_API_URL, encodedAddress, API_KEY);
            
            URL url = new URI(urlString).toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Read response
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                // Parse JSON response
                JSONArray results = new JSONArray(response.toString());
                if (results.length() > 0) {
                    JSONObject location = results.getJSONObject(0);
                    return new double[]{
                        Double.parseDouble(location.getString("lat")),
                        Double.parseDouble(location.getString("lon"))
                    };
                }
            }
        } catch (Exception e) {
            System.err.println("Error getting coordinates: " + e.getMessage());
        }
        return null;
    }

    private static double calculateHaversineDistance(double lat1, double lon1, double lat2, double lon2) {
        // Convert to radians
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);

        // Haversine formula
        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                   Math.cos(lat1) * Math.cos(lat2) *
                   Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return EARTH_RADIUS * c; // Returns distance in kilometers
    }

    public static boolean verifyAddress(AddressDTO address) {
        if (address == null) {
            return false;
        }

        try {
            String addressString = AddressService.concatAddress(address);
            double[] coordinates = getCoordinates(addressString);
            return coordinates != null;
        } catch (Exception e) {
            System.err.println("Error verifying address: " + e.getMessage());
            return false;
        }
    }

    public static double getDeliveryFee(AddressDTO address1, AddressDTO address2) {
        if (address1 == null || address2 == null) {
            return 0;
        }

        double distance = getDistance(address1, address2);
        double fee = distance * 0.5; // RM0.50 per kilometer
        return Math.round(fee * 100.0) / 100.0; // Round to 2 decimal places
    }

    
}