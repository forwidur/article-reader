package co.kernelnetworks.medstream.server.models.shared;

import java.util.HashMap;
import java.util.Map;

public enum SignalType {
    ArticleVote(0),
    DigestPick(1);

    private static final Map<Integer, SignalType> map = getMap();

    private static Map<Integer, SignalType> getMap() {
        Map<Integer, SignalType> result = new HashMap<>();
        for (SignalType item : SignalType.values()) result.put(item.id, item);
        return result;
    }

    private final int id;

    SignalType(final int newValue) {
        id = newValue;
    }

    public int getId() {
        return id;
    }

    public static SignalType fromId(int id) throws RuntimeException {
        if (map.containsKey(id)) return map.get(id);
        else throw new RuntimeException(String.format("Cannot find SignalType with id %d", id));
    }

    public static SignalType fromUrl(String name) throws RuntimeException {
        for (SignalType enumn : SignalType.class.getEnumConstants())
            if (enumn.name().equalsIgnoreCase(name))
                return enumn;

        throw new RuntimeException(String.format("Cannot find SignalType with value `%s`", name));
    }
}
