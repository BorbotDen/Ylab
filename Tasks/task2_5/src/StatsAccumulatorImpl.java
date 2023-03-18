public class StatsAccumulatorImpl implements StatsAccumulator {
    private int min=Integer.MAX_VALUE;
    private int max=Integer.MIN_VALUE;
    private int count=0;
    private double avg=0;

    @Override
    public void add(int value) {
        if (value > max) {
            max = value;
        }
        if (value < min) {
            min = value;
        }
        count++;
        avg = (avg + value) / count;
    }

    @Override
    public int getMin() {
        return min;
    }

    @Override
    public int getMax() {
        return max;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Double getAvg() {
        return avg;
    }
}
