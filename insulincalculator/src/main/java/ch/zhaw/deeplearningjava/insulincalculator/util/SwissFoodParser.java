package ch.zhaw.deeplearningjava.insulincalculator.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SwissFoodParser {

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Extrahiert den Kohlenhydratwert aus der API-Antwort (JSON-String).
     * Liefert den Wert aus dem ersten Treffer in Gramm pro 100g.
     */
    public float extractCarbsPer100g(String jsonResponse) {
        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            if (root.isArray() && root.size() > 0) {
                JsonNode firstMatch = root.get(0);
                JsonNode nutrients = firstMatch.get("nutrients");
                if (nutrients != null && nutrients.has("carbohydrates")) {
                    return (float) nutrients.get("carbohydrates").asDouble();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 15f; // Fallback-Wert
    }
}
