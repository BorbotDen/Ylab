public class StatsAccumulatorAlternative implements StatsAccumulator{

        private int min = Integer.MAX_VALUE;
        private int max = Integer.MIN_VALUE;
        private int count = 0;
        private double sum = 0;
        public void add(int value) {
            min = Math.min(min, value);
            max = Math.max(max, value);
            count++;
            sum += value;
        }

        public int getMin() {
            return min;
        }

        public int getMax() {
            return max;
        }

        public int getCount() {
            return count;
        }

        public Double getAvg() {
            return count > 0 ? (sum / count) : 0;
        }
    }