package ly.qubit.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AnnualDeclarationMapperTest {

    private AnnualDeclarationMapper annualDeclarationMapper;

    @BeforeEach
    public void setUp() {
        annualDeclarationMapper = new AnnualDeclarationMapperImpl();
    }
}
