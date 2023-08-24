package ly.qubit.service.mapper;

import ly.qubit.domain.SocialSecurityPensioner;
import ly.qubit.service.dto.SocialSecurityPensionerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SocialSecurityPensioner} and its DTO {@link SocialSecurityPensionerDTO}.
 */
@Mapper(componentModel = "spring")
public interface SocialSecurityPensionerMapper extends EntityMapper<SocialSecurityPensionerDTO, SocialSecurityPensioner> {}
