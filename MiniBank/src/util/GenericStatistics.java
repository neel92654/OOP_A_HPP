package util;

public final class GenericStatistics {

    private GenericStatistics() {
    }

    public static <N extends Number> double average(N[] values) {
        if (values == null || values.length == 0) {
            throw new IllegalArgumentException("Values cannot be empty");
        }

        double total = 0;
        for (N value : values) {
            total += value.doubleValue();
        }
        return total / values.length;
    }
}
