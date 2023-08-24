package ly.qubit.domain.enumeration;

/**
 * The DocumentdType enumeration.
 */
public enum DocumentdType {
    IDENTITY("Identity Document"),
    INCOME("Income Certificate"),
    FAMILY("Family Information"),
    MEDICAL("Medical Records"),
    RESIDENCE("Residence Proof"),
    OTHER("Other Document");

    private final String value;

    DocumentdType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
