package com.lotaris.api.test.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.json.Json;
import javax.json.JsonObjectBuilder;

/**
 * Utility methods when working with JSON objects
 * 
 * @author Baptiste Roth <baptiste.roth@lotaris.com>
 */
public class JsonUtils {
	public static JsonObjectBuilder mapToJsonObjectBuilder(Map<String, String> map) {
		JsonObjectBuilder jsonObject = Json.createObjectBuilder();
		if (map == null || map.isEmpty()) {
			return jsonObject;
		}
		Iterator<Entry<String, String>> i = map.entrySet().iterator();
		while (i.hasNext()) {
			Entry<String, String> e = i.next();
			jsonObject.add(e.getKey(), e.getValue());
		}
		return jsonObject;
	}
}
