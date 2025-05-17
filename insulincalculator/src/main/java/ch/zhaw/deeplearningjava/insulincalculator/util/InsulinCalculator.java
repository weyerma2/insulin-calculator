package ch.zhaw.deeplearningjava.insulincalculator.util;

public class InsulinCalculator {

    public double calculateInsulinDose(double carbsInGrams, double ratio) {
        return Math.round((carbsInGrams / ratio) * 10.0) / 10.0;
    }
}
