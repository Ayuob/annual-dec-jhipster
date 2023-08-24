package ly.qubit.domain.enumeration;

/**
 * The EntitlementType enumeration.
 */
public enum EntitlementType {
    PENSION("pension"),
    MEDICAL("medical"),
    EDUCATION("education"),
    HOUSING("housing"),
    OTHER("other");

    private final String value;

    EntitlementType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
