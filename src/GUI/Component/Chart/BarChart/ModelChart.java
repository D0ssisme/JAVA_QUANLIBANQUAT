package GUI.Component.Chart.BarChart;

public class ModelChart {
    private String label;
    private double[] values;

    public ModelChart(String label, double[] values) {
        this.label = label;
        this.values = values;
    }

    public String getLabel() {
        return label;
    }

    public double[] getValues() {
        return values;
    }
}
