package pl.prv.techalive.encoder.constants;

/**
 * All supported Padding
 */
public enum Padding {

    NO_PADDING ("NoPadding"),
    PKCS5PADDING("PKCS5Padding"),
    PKCS7PADDING("PKCS7Padding"),
    ISO10126PADDING("ISO10126Padding");


    private final String padding;

    Padding(String padding) {
        this.padding = padding;
    }

    public String getPadding() {
        return padding;
    }
}
