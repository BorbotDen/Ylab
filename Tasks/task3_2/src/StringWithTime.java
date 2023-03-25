import java.util.Date;

public class StringWithTime {
    private final String value;
    private final Date date;

    public StringWithTime(String value, Date date) {
        this.value = value;
        this.date = date;
    }

    public String getValue() {
        return value;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "value='" + value + '\'' +
                " date last added=" + date +" ; ";
    }
}
