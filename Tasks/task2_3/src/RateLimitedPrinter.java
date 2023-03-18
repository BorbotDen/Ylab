public class RateLimitedPrinter {
    private final long interval;
    private long timePointPrint;

    public RateLimitedPrinter(int interval) {
        this.interval = interval;
        this.timePointPrint = System.currentTimeMillis() - interval;
    }

    public void print(String message) {
        long rightNowTime = System.currentTimeMillis();
        if (rightNowTime > timePointPrint) {
            System.out.println(message);
            timePointPrint = rightNowTime + interval;
        }
    }
}
