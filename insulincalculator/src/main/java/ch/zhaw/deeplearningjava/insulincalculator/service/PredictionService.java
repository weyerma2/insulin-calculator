package ch.zhaw.deeplearningjava.insulincalculator.service;

import ai.djl.Application;
import ai.djl.ModelException;
import ai.djl.inference.Predictor;
import ai.djl.modality.Classifications;
import ai.djl.modality.cv.Image;
import ai.djl.modality.cv.ImageFactory;
import ai.djl.modality.cv.translator.ImageClassificationTranslator;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;
import ch.zhaw.deeplearningjava.insulincalculator.util.SwissFoodApiClient;
import org.springframework.stereotype.Service;
import java.util.Optional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

@Service
public class PredictionService {

    public record PredictionResult(String food, float carbs) {}

    private final SwissFoodApiClient apiClient = new SwissFoodApiClient();

    public PredictionResult predictCarbsWithFood(File imageFile, float portionGrams)
        throws IOException, ModelException, TranslateException {
        // 🧠 Schritt 1: Bildklassifikation mit DJL
        String predictedFood = runDjlPrediction(imageFile);
        System.out.println("🔍 Vorhergesagtes Gericht: " + predictedFood);

        // 🌐 Schritt 2: API-Ninjas abfragen
        Optional<Float> carbsOpt = apiClient.searchFood(predictedFood);

        // 🧮 Schritt 3: Berechnung der KH für Benutzerportion
        float carbsPer100g = carbsOpt.orElse(30.0f); // Fallbackwert
        float totalCarbs = (carbsPer100g * portionGrams) / 100f;

        return new PredictionResult(predictedFood, totalCarbs);
    }

    private String runDjlPrediction(File imageFile) throws IOException, ModelException, TranslateException {
        Criteria<Image, Classifications> criteria = Criteria.builder()
            .optApplication(Application.CV.IMAGE_CLASSIFICATION)
            .setTypes(Image.class, Classifications.class)
            .optFilter("layers", "50")
            .optFilter("flavor", "v1")
            .optFilter("dataset", "cifar10")
            .optEngine("PyTorch")
            .optProgress(new ProgressBar())
            .build();

        try (ZooModel<Image, Classifications> model = criteria.loadModel();
             Predictor<Image, Classifications> predictor = model.newPredictor()) {

            Image img = ImageFactory.getInstance().fromFile(imageFile.toPath());
            Classifications result = predictor.predict(img);

            //return result.best().getClassName(); // z. B. "pizza"
            return "pizza"; // Dummy-Wert für die Vorhersage
        }
    }
}
