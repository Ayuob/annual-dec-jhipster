package ly.qubit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import ly.qubit.domain.enumeration.DocumentdType;

/**
 * A Document.
 */
@Entity
@Table(name = "document")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Document implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "file_name", nullable = false)
    private String fileName;

    @NotNull
    @Column(name = "file_path", nullable = false)
    private String filePath;

    @NotNull
    @Column(name = "submission_date", nullable = false)
    private Instant submissionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "documentd_type")
    private DocumentdType documentdType;

    @ManyToOne
    @JsonIgnoreProperties(value = { "documents", "pensioner", "familyMembers" }, allowSetters = true)
    private AnnualDeclaration annualDeclaration;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Document id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return this.fileName;
    }

    public Document fileName(String fileName) {
        this.setFileName(fileName);
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public Document filePath(String filePath) {
        this.setFilePath(filePath);
        return this;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Instant getSubmissionDate() {
        return this.submissionDate;
    }

    public Document submissionDate(Instant submissionDate) {
        this.setSubmissionDate(submissionDate);
        return this;
    }

    public void setSubmissionDate(Instant submissionDate) {
        this.submissionDate = submissionDate;
    }

    public DocumentdType getDocumentdType() {
        return this.documentdType;
    }

    public Document documentdType(DocumentdType documentdType) {
        this.setDocumentdType(documentdType);
        return this;
    }

    public void setDocumentdType(DocumentdType documentdType) {
        this.documentdType = documentdType;
    }

    public AnnualDeclaration getAnnualDeclaration() {
        return this.annualDeclaration;
    }

    public void setAnnualDeclaration(AnnualDeclaration annualDeclaration) {
        this.annualDeclaration = annualDeclaration;
    }

    public Document annualDeclaration(AnnualDeclaration annualDeclaration) {
        this.setAnnualDeclaration(annualDeclaration);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Document)) {
            return false;
        }
        return id != null && id.equals(((Document) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Document{" +
            "id=" + getId() +
            ", fileName='" + getFileName() + "'" +
            ", filePath='" + getFilePath() + "'" +
            ", submissionDate='" + getSubmissionDate() + "'" +
            ", documentdType='" + getDocumentdType() + "'" +
            "}";
    }
}
