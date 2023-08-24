package ly.qubit.domain.enumeration;

/**
 * The DeclarationStatus enumeration.
 */
public enum DeclarationStatus {
    SUBMITTED("Submitted"),
    APPROVED("Approved"),
    REJECTED("Rejected");

    private final String value;

    DeclarationStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
