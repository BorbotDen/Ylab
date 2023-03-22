import java.util.Comparator;

public class IndexElement implements Comparable {
    private int index;
    private long value;

    public IndexElement(int index, long value) {
        this.index = index;
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public int compareTo(Object o) {
        IndexElement other = (IndexElement) o;
        if (this.value > other.value) {
            return 1;
        } else if (this.value < other.value) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return "IndexElement{" +
                "index=" + index +
                ", value=" + value +
                '}';
    }
}

