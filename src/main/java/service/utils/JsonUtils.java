package service.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

public class JsonUtils {

	// Helper method to convert JSONArray to List<String>
	public static List<String> jsonArrayToList(JSONArray jsonArray) {
		List<String> list = new ArrayList<>();
		for (int i = 0; i < jsonArray.length(); i++) {
			list.add(jsonArray.getString(i));
		}
		return list;
	}
}
