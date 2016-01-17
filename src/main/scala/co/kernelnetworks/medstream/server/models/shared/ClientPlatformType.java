package co.kernelnetworks.medstream.server.models.shared;

public enum ClientPlatformType {
    iOS("ios"),
    Android("android");

    private final String text;

    ClientPlatformType(final String name) {
        this.text = name;
    }

    public String getText() {
        return text;
    }

    public static ClientPlatformType fromText(String name) throws RuntimeException {

        for (ClientPlatformType v : values())
            if (v.text.equalsIgnoreCase(name))
                return v;

        throw new RuntimeException(String.format("Cannot find ClientPlatformType with id %s", name));
    }
}
