package ch.zhaw.deeplearningjava.insulincalculator.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FoodDataLoader {

    public static Map<String, Float> loadCarbohydrateData(String csvFilePath) {
        Map<String, Float> foodToCarbs = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            // Überspringe die Kopfzeile
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                if (values.length > 0) {
                    String foodName = values[0].trim().toLowerCase();
                    try {
                        float carbs = Float.parseFloat(values[1].trim().replace(",", "."));
                        foodToCarbs.put(foodName, carbs);
                    } catch (NumberFormatException e) {
                        // Falls der Kohlenhydratwert nicht geparst werden kann, überspringe diesen Eintrag
                        continue;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return foodToCarbs;
    }
}
