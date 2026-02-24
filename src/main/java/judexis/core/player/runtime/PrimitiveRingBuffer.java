package judexis.core.player.runtime;

public final class PrimitiveRingBuffer {

    private final double[] values;
    private final double[] scratch;
    private int cursor;
    private int count;

    public PrimitiveRingBuffer(int size) {
        this.values = new double[size];
        this.scratch = new double[size];
    }

    public void push(double value) {
        values[cursor] = value;
        cursor++;
        if (cursor == values.length) {
            cursor = 0;
        }
        if (count < values.length) {
            count++;
        }
    }

    public int count() {
        return count;
    }

    public int capacity() {
        return values.length;
    }

    public double valueAt(int reverseIndex) {
        if (reverseIndex < 0 || reverseIndex >= count) {
            return 0.0D;
        }
        int index = cursor - 1 - reverseIndex;
        while (index < 0) {
            index += values.length;
        }
        return values[index];
    }

    public double mean() {
        if (count == 0) {
            return 0.0D;
        }
        double sum = 0.0D;
        for (int i = 0; i < count; i++) {
            sum += values[i];
        }
        return sum / count;
    }

    public double variance() {
        if (count <= 1) {
            return 0.0D;
        }
        double mean = mean();
        double sum = 0.0D;
        for (int i = 0; i < count; i++) {
            double d = values[i] - mean;
            sum += d * d;
        }
        return sum / (count - 1);
    }

    public double coefficientOfVariation() {
        double mean = mean();
        if (mean == 0.0D) {
            return 0.0D;
        }
        return Math.sqrt(variance()) / Math.abs(mean);
    }

    public double percentile95() {
        if (count == 0) {
            return 0.0D;
        }
        for (int i = 0; i < count; i++) {
            scratch[i] = values[i];
        }
        for (int i = 1; i < count; i++) {
            double key = scratch[i];
            int j = i - 1;
            while (j >= 0 && scratch[j] > key) {
                scratch[j + 1] = scratch[j];
                j--;
            }
            scratch[j + 1] = key;
        }
        int idx = (int) Math.floor((count - 1) * 0.95D);
        return scratch[idx];
    }

    public double entropy8() {
        if (count == 0) {
            return 0.0D;
        }

        double min = values[0];
        double max = values[0];
        for (int i = 1; i < count; i++) {
            double value = values[i];
            if (value < min) {
                min = value;
            }
            if (value > max) {
                max = value;
            }
        }

        double range = max - min;
        if (range <= 1.0E-9D) {
            return 0.0D;
        }

        int b0 = 0;
        int b1 = 0;
        int b2 = 0;
        int b3 = 0;
        int b4 = 0;
        int b5 = 0;
        int b6 = 0;
        int b7 = 0;

        for (int i = 0; i < count; i++) {
            double normalized = (values[i] - min) / range;
            int bucket = (int) (normalized * 7.999999D);
            if (bucket == 0) {
                b0++;
            } else if (bucket == 1) {
                b1++;
            } else if (bucket == 2) {
                b2++;
            } else if (bucket == 3) {
                b3++;
            } else if (bucket == 4) {
                b4++;
            } else if (bucket == 5) {
                b5++;
            } else if (bucket == 6) {
                b6++;
            } else {
                b7++;
            }
        }

        return entropyPart(b0) + entropyPart(b1) + entropyPart(b2) + entropyPart(b3)
                + entropyPart(b4) + entropyPart(b5) + entropyPart(b6) + entropyPart(b7);
    }

    private double entropyPart(int bucketCount) {
        if (bucketCount == 0) {
            return 0.0D;
        }
        double probability = (double) bucketCount / (double) count;
        return -probability * (Math.log(probability) / Math.log(2.0D));
    }
}
