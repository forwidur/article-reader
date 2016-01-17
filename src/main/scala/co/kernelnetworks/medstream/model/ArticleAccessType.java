package co.kernelnetworks.medstream.model;

public enum ArticleAccessType {
    Free((byte) 0),
    Subscription((byte) 1);

    private final byte id;

    ArticleAccessType(final byte newValue) {
        id = newValue;
    }

    public byte getId() {
        return id;
    }

    public static ArticleAccessType fromId(byte id) throws RuntimeException {
        switch (id) {
            case 0:
                return Free;
            case 1:
                return Subscription;
            default:
                throw new RuntimeException(String.format("Cannot find ArticleAccessType with id %d", id));
        }
    }

}
