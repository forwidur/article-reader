package co.kernelnetworks.medstream.server.models.shared;

import java.util.HashMap;
import java.util.Map;

public enum RatingType {
    UpVote(1);

    private static final Map<Integer, RatingType> map = getMap();

    private static Map<Integer, RatingType> getMap() {
        Map<Integer, RatingType> result = new HashMap<>();
        for (RatingType item : RatingType.values()) result.put(item.id, item);
        return result;
    }

    private final int id;

    RatingType(final int newValue) {
        id = newValue;
    }

    public int getId() {
        return id;
    }

    public static RatingType fromId(int id) throws RuntimeException {
        if (map.containsKey(id)) return map.get(id);
        else throw new RuntimeException(String.format("Cannot find RatingType with id %d", id));
    }
}
