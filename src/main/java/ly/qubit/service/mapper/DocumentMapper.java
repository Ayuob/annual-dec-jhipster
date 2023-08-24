package ly.qubit.service.mapper;

import ly.qubit.domain.AnnualDeclaration;
import ly.qubit.domain.Document;
import ly.qubit.service.dto.AnnualDeclarationDTO;
import ly.qubit.service.dto.DocumentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Document} and its DTO {@link DocumentDTO}.
 */
@Mapper(componentModel = "spring")
public interface DocumentMapper extends EntityMapper<DocumentDTO, Document> {
    @Mapping(target = "annualDeclaration", source = "annualDeclaration", qualifiedByName = "annualDeclarationSubmissionDate")
    DocumentDTO toDto(Document s);

    @Named("annualDeclarationSubmissionDate")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "submissionDate", source = "submissionDate")
    AnnualDeclarationDTO toDtoAnnualDeclarationSubmissionDate(AnnualDeclaration annualDeclaration);
}
