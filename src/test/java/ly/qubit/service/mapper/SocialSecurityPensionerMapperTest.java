package ly.qubit.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SocialSecurityPensionerMapperTest {

    private SocialSecurityPensionerMapper socialSecurityPensionerMapper;

    @BeforeEach
    public void setUp() {
        socialSecurityPensionerMapper = new SocialSecurityPensionerMapperImpl();
    }
}
