package ch.zhaw.deeplearningjava.insulincalculator.model;

public class NutritionResult {

    private float carbs;
    private double insulin;

    public NutritionResult(float carbs, double insulin) {
        this.carbs = carbs;
        this.insulin = insulin;
    }

    public float getCarbs() {
        return carbs;
    }

    public void setCarbs(float carbs) {
        this.carbs = carbs;
    }

    public double getInsulin() {
        return insulin;
    }

    public void setInsulin(double insulin) {
        this.insulin = insulin;
    }
}
