package ly.qubit.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FamilyMemberMapperTest {

    private FamilyMemberMapper familyMemberMapper;

    @BeforeEach
    public void setUp() {
        familyMemberMapper = new FamilyMemberMapperImpl();
    }
}
