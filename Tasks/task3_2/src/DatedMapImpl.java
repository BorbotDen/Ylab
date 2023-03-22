import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DatedMapImpl implements DatedMap {
    private final Map<String, StringWithTime> stringWithLastPutTime = new HashMap<>();

    @Override
    public void put(String key, String value) {
        Date newDate = new Date(System.currentTimeMillis());
        StringWithTime valueWithTime = new StringWithTime(value, newDate);
        if (stringWithLastPutTime.containsKey(key)) {
            stringWithLastPutTime.replace(key, valueWithTime);
        } else {
            stringWithLastPutTime.put(key, valueWithTime);

        }
    }

    @Override
    public String get(String key) {
         StringWithTime stringWithTime =stringWithLastPutTime.get(key);
        return stringWithTime != null ? stringWithTime.getValue() : null;
    }

    @Override
    public boolean containsKey(String key) {
        return stringWithLastPutTime.containsKey(key);
    }

    @Override
    public void remove(String key) {
        stringWithLastPutTime.remove(key);
    }

    @Override
    public Set<String> keySet() {
        return stringWithLastPutTime.keySet();
    }

    @Override
    public Date getKeyLastInsertionDate(String key) {
        StringWithTime stringWithTime =stringWithLastPutTime.get(key);
        return stringWithTime != null? stringWithLastPutTime.get(key).getDate(): null;
    }
}
