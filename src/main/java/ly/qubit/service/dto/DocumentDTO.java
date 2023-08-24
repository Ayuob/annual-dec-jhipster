package ly.qubit.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;
import ly.qubit.domain.enumeration.DocumentdType;

/**
 * A DTO for the {@link ly.qubit.domain.Document} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentDTO implements Serializable {

    private Long id;

    @NotNull
    private String fileName;

    @NotNull
    private String filePath;

    @NotNull
    private Instant submissionDate;

    private DocumentdType documentdType;

    private AnnualDeclarationDTO annualDeclaration;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Instant getSubmissionDate() {
        return submissionDate;
    }

    public void setSubmissionDate(Instant submissionDate) {
        this.submissionDate = submissionDate;
    }

    public DocumentdType getDocumentdType() {
        return documentdType;
    }

    public void setDocumentdType(DocumentdType documentdType) {
        this.documentdType = documentdType;
    }

    public AnnualDeclarationDTO getAnnualDeclaration() {
        return annualDeclaration;
    }

    public void setAnnualDeclaration(AnnualDeclarationDTO annualDeclaration) {
        this.annualDeclaration = annualDeclaration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentDTO)) {
            return false;
        }

        DocumentDTO documentDTO = (DocumentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, documentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentDTO{" +
            "id=" + getId() +
            ", fileName='" + getFileName() + "'" +
            ", filePath='" + getFilePath() + "'" +
            ", submissionDate='" + getSubmissionDate() + "'" +
            ", documentdType='" + getDocumentdType() + "'" +
            ", annualDeclaration=" + getAnnualDeclaration() +
            "}";
    }
}
