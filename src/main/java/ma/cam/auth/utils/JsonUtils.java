/**
 * 
 */
package ma.cam.auth.utils;

import java.util.Map;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author y.nadir
 *
 */
public class JsonUtils {

	private JsonUtils() {

	}

	public static String convertToJson(Object objectToConvert) throws JsonProcessingException {
		ObjectMapper obj = new ObjectMapper();
		return obj.writeValueAsString(objectToConvert);
	}

	/**
	 * Serialise a object to json String
	 * 
	 * @param <T>
	 * @param jsonString
	 * @param clazz
	 * @return
	 */
	public static <T> T convertToObject(String jsonString, Class<T> clazz) {
		try {
			return new ObjectMapper().readValue(jsonString, clazz);
		} catch (final Exception e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public static boolean hasJsonFormat(String json) {

		try {
			new JSONObject(json);
		} catch (JSONException e) {
			return false;
		}
		return true;
	}

	/**
	 * @param dmnOutputVariable
	 * @param dmnOutput
	 * @return
	 */
	public static String convertMapToJson(Map<String, String> map) {
		ObjectMapper mapper = new ObjectMapper();
		// create new node
		JsonNode node = mapper.createObjectNode();
		ObjectNode obj = (ObjectNode) node;
		map.forEach((k, v) -> obj.put(k, v));

		return obj.toString();
	}

}
