package ch.zhaw.deeplearningjava.insulincalculator.controller;

import ch.zhaw.deeplearningjava.insulincalculator.service.PredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import ai.djl.translate.TranslateException; // Add this import for TranslateException
import ai.djl.ModelException; // Add this import for ModelException

@RestController
@RequestMapping("/api")
public class ImageUploadController {

    @Autowired
    private PredictionService predictionService;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> handleUpload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("weight") float weight) throws IOException, ModelException, TranslateException {

        // Temporäre Datei speichern
        File tempFile = File.createTempFile("upload-", file.getOriginalFilename());
        file.transferTo(tempFile);

        // Vorhersage aufrufen
        float carbs = predictionService.predictCarbs(tempFile, weight);


        // Beispiel: 1 IE Insulin pro 10g KH
        float insulinUnits = carbs / 10f;

        // Antwort zurückgeben
        Map<String, Object> result = new HashMap<>();
        result.put("carbs", carbs);
        result.put("insulin", insulinUnits);

        return ResponseEntity.ok(result);
    }
    
}
