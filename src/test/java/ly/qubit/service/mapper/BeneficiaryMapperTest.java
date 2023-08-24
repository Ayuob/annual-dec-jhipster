package ly.qubit.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BeneficiaryMapperTest {

    private BeneficiaryMapper beneficiaryMapper;

    @BeforeEach
    public void setUp() {
        beneficiaryMapper = new BeneficiaryMapperImpl();
    }
}
