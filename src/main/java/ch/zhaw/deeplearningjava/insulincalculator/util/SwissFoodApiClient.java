package ch.zhaw.deeplearningjava.insulincalculator.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.net.HttpURLConnection;

import java.util.Optional;

import java.net.URL;

public class SwissFoodApiClient {

    private static final String API_KEY = "HDmtaqyUwfRrnoYg1fBTXQ==dok5zhnM7BjsVJBq"; // 🔑 <- hier eintragen

    public Optional<Float> searchFood(String query) {
        try {
            String encodedQuery = java.net.URLEncoder.encode(query, "UTF-8");
            URL url = new URL("https://api.api-ninjas.com/v1/nutrition?query=" + encodedQuery);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("accept", "application/json");
            connection.setRequestProperty("X-Api-Key", API_KEY);

            try (InputStream responseStream = connection.getInputStream()) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(responseStream);

                if (!root.isArray() || root.size() == 0) {
                    System.out.println("⚠️ Keine Einträge gefunden für: " + query);
                    return Optional.empty();
                }

                JsonNode bestMatch = null;
                float maxCarbs = -1f;

                for (JsonNode item : root) {
                    float carbs = (float) item.path("carbohydrates_total_g").asDouble();
                    if (carbs > maxCarbs) {
                        maxCarbs = carbs;
                        bestMatch = item;
                    }
                }

                if (bestMatch != null) {
                    System.out.println("🔎 Best Match: " + bestMatch.path("name").asText() +
                                       " mit " + maxCarbs + "g Kohlenhydraten");
                    return Optional.of(maxCarbs);
                } else {
                    return Optional.empty();
                }
            }
        } catch (Exception e) {
            System.err.println("❌ API-Fehler: " + e.getMessage());
            return Optional.empty();
        }
    }
}
