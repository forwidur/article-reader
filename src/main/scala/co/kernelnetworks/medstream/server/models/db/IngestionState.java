package co.kernelnetworks.medstream.server.models.db;

public enum IngestionState {
    Newborn((byte) 0),
    Completed((byte) 1);

    private final byte id;

    IngestionState(final byte newValue) {
        id = newValue;
    }

    public byte getId() {
        return id;
    }

    public static IngestionState fromId(byte id) throws RuntimeException {
        switch (id) {
            case 0:
                return Newborn;
            case 1:
                return Completed;
            default:
                throw new RuntimeException(String.format("Cannot find IngestionState with id %d", id));
        }
    }
}
