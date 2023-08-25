package ly.qubit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import ly.qubit.IntegrationTest;
import ly.qubit.domain.Beneficiary;
import ly.qubit.domain.BeneficiaryId;
import ly.qubit.domain.enumeration.EntitlementType;
import ly.qubit.repository.BeneficiaryRepository;
import ly.qubit.service.BeneficiaryService;
import ly.qubit.service.dto.BeneficiaryDto_Empd;
import ly.qubit.service.dto.BeneficiaryIdDto;
import ly.qubit.service.mapper.BeneficiaryEmpededMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BeneficiaryResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class BeneficiaryResourceIT {

    private static final EntitlementType DEFAULT_ENTITLEMENT_TYPE = EntitlementType.PENSION;
    private static final EntitlementType UPDATED_ENTITLEMENT_TYPE = EntitlementType.MEDICAL;

    private static final String DEFAULT_ENTITLEMENT_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_ENTITLEMENT_DETAILS = "BBBBBBBBBB";
    private static final String DEFAULT_FAMILY_MEMBER_ID = "1";
    private static final String UPDATED_FAMILY_MEMBER_ID = "2";
    private static final String DEFAULT_ANNUAL_DEC_ID = "1";
    private static final String UPDATED_ANNUAL_DEC_ID = "2";

    private static final String ENTITY_API_URL = "/api/beneficiaries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{fid}/{aid}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    private static final BeneficiaryId DEFULT_FAMILY_Member_Annual_Declaration_Id = new BeneficiaryId(
        Long.getLong(DEFAULT_FAMILY_MEMBER_ID),
        Long.getLong(DEFAULT_ANNUAL_DEC_ID)
    );
    private static final BeneficiaryId UPDATED_FAMILY_Member_Annual_Declaration_Id = new BeneficiaryId(
        Long.getLong(UPDATED_FAMILY_MEMBER_ID),
        Long.getLong(UPDATED_ANNUAL_DEC_ID)
    );
    private static final BeneficiaryId RANDOME_FAMILY_Member_Annual_Declaration_Id = new BeneficiaryId(
        count.incrementAndGet(),
        count.incrementAndGet()
    );

    private static final BeneficiaryIdDto RANDOME_BENEFICIARY_ID_DTO = new BeneficiaryIdDto(
        count.incrementAndGet(),
        count.incrementAndGet()
    );

    @Autowired
    private BeneficiaryRepository beneficiaryRepository;

    @Mock
    private BeneficiaryRepository beneficiaryRepositoryMock;

    @Autowired
    private BeneficiaryEmpededMapper beneficiaryMapper;

    @Mock
    private BeneficiaryService beneficiaryServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBeneficiaryMockMvc;

    private Beneficiary beneficiary;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Beneficiary createEntity(EntityManager em) {
        return new Beneficiary()
            .entitlementType(DEFAULT_ENTITLEMENT_TYPE)
            .entitlementDetails(DEFAULT_ENTITLEMENT_DETAILS)
            .id(DEFULT_FAMILY_Member_Annual_Declaration_Id);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Beneficiary createUpdatedEntity(EntityManager em) {
        return new Beneficiary()
            .entitlementType(UPDATED_ENTITLEMENT_TYPE)
            .entitlementDetails(UPDATED_ENTITLEMENT_DETAILS)
            .id(UPDATED_FAMILY_Member_Annual_Declaration_Id);
    }

    @BeforeEach
    public void initTest() {
        beneficiary = createEntity(em);
    }

    @Test
    @Transactional
    void createBeneficiary() throws Exception {
        int databaseSizeBeforeCreate = beneficiaryRepository.findAll().size();
        // Create the Beneficiary
        BeneficiaryDto_Empd beneficiaryDTO = beneficiaryMapper.toDto(beneficiary);
        restBeneficiaryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(beneficiaryDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Beneficiary in the database
        List<Beneficiary> beneficiaryList = beneficiaryRepository.findAll();
        assertThat(beneficiaryList).hasSize(databaseSizeBeforeCreate + 1);
        Beneficiary testBeneficiary = beneficiaryList.get(beneficiaryList.size() - 1);
        assertThat(testBeneficiary.getEntitlementType()).isEqualTo(DEFAULT_ENTITLEMENT_TYPE);
        assertThat(testBeneficiary.getEntitlementDetails()).isEqualTo(DEFAULT_ENTITLEMENT_DETAILS);
    }

    @Test
    @Transactional
    void createBeneficiaryWithExistingId() throws Exception {
        // Create the Beneficiary with an existing ID
        beneficiary.setId(new BeneficiaryId(1L, 1L));
        BeneficiaryDto_Empd beneficiaryDTO = beneficiaryMapper.toDto(beneficiary);

        int databaseSizeBeforeCreate = beneficiaryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBeneficiaryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(beneficiaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Beneficiary in the database
        List<Beneficiary> beneficiaryList = beneficiaryRepository.findAll();
        assertThat(beneficiaryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEntitlementTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = beneficiaryRepository.findAll().size();
        // set the field null
        beneficiary.setEntitlementType(null);

        // Create the Beneficiary, which fails.
        BeneficiaryDto_Empd beneficiaryDTO = beneficiaryMapper.toDto(beneficiary);

        restBeneficiaryMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(beneficiaryDTO))
            )
            .andExpect(status().isBadRequest());

        List<Beneficiary> beneficiaryList = beneficiaryRepository.findAll();
        assertThat(beneficiaryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBeneficiaries() throws Exception {
        // Initialize the database
        beneficiaryRepository.saveAndFlush(beneficiary);

        // Get all the beneficiaryList
        restBeneficiaryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            // .andExpect(jsonPath("$.[*].id").value(hasItem(beneficiary.getId().intValue())))
            .andExpect(jsonPath("$.[*].entitlementType").value(hasItem(DEFAULT_ENTITLEMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].entitlementDetails").value(hasItem(DEFAULT_ENTITLEMENT_DETAILS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBeneficiariesWithEagerRelationshipsIsEnabled() throws Exception {
        when(beneficiaryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBeneficiaryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(beneficiaryServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllBeneficiariesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(beneficiaryServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restBeneficiaryMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(beneficiaryRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getBeneficiary() throws Exception {
        // Initialize the database
        beneficiaryRepository.saveAndFlush(beneficiary);

        // Get the beneficiary
        restBeneficiaryMockMvc
            .perform(get(ENTITY_API_URL_ID, beneficiary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            // .andExpect(jsonPath("$.id").value(beneficiary.getId().intValue()))
            .andExpect(jsonPath("$.entitlementType").value(DEFAULT_ENTITLEMENT_TYPE.toString()))
            .andExpect(jsonPath("$.entitlementDetails").value(DEFAULT_ENTITLEMENT_DETAILS));
    }

    @Test
    @Transactional
    void getNonExistingBeneficiary() throws Exception {
        // Get the beneficiary
        restBeneficiaryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBeneficiary() throws Exception {
        // Initialize the database
        beneficiaryRepository.saveAndFlush(beneficiary);

        int databaseSizeBeforeUpdate = beneficiaryRepository.findAll().size();

        // Update the beneficiary
        Beneficiary updatedBeneficiary = beneficiaryRepository.findById(beneficiary.getId()).get();
        // Disconnect from session so that the updates on updatedBeneficiary are not directly saved in db
        em.detach(updatedBeneficiary);
        updatedBeneficiary.entitlementType(UPDATED_ENTITLEMENT_TYPE).entitlementDetails(UPDATED_ENTITLEMENT_DETAILS);
        BeneficiaryDto_Empd beneficiaryDTO = beneficiaryMapper.toDto(updatedBeneficiary);

        restBeneficiaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, beneficiaryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(beneficiaryDTO))
            )
            .andExpect(status().isOk());

        // Validate the Beneficiary in the database
        List<Beneficiary> beneficiaryList = beneficiaryRepository.findAll();
        assertThat(beneficiaryList).hasSize(databaseSizeBeforeUpdate);
        Beneficiary testBeneficiary = beneficiaryList.get(beneficiaryList.size() - 1);
        assertThat(testBeneficiary.getEntitlementType()).isEqualTo(UPDATED_ENTITLEMENT_TYPE);
        assertThat(testBeneficiary.getEntitlementDetails()).isEqualTo(UPDATED_ENTITLEMENT_DETAILS);
    }

    @Test
    @Transactional
    void putNonExistingBeneficiary() throws Exception {
        int databaseSizeBeforeUpdate = beneficiaryRepository.findAll().size();
        beneficiary.setId(new BeneficiaryId(2L, 2L));

        // Create the Beneficiary
        BeneficiaryDto_Empd beneficiaryDTO = beneficiaryMapper.toDto(beneficiary);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBeneficiaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, beneficiaryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(beneficiaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Beneficiary in the database
        List<Beneficiary> beneficiaryList = beneficiaryRepository.findAll();
        assertThat(beneficiaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBeneficiary() throws Exception {
        int databaseSizeBeforeUpdate = beneficiaryRepository.findAll().size();
        beneficiary.setId(RANDOME_FAMILY_Member_Annual_Declaration_Id);

        // Create the Beneficiary
        BeneficiaryDto_Empd beneficiaryDTO = beneficiaryMapper.toDto(beneficiary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBeneficiaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(beneficiaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Beneficiary in the database
        List<Beneficiary> beneficiaryList = beneficiaryRepository.findAll();
        assertThat(beneficiaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBeneficiary() throws Exception {
        int databaseSizeBeforeUpdate = beneficiaryRepository.findAll().size();
        beneficiary.setId(RANDOME_FAMILY_Member_Annual_Declaration_Id);

        // Create the Beneficiary
        BeneficiaryDto_Empd beneficiaryDTO = beneficiaryMapper.toDto(beneficiary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBeneficiaryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(beneficiaryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Beneficiary in the database
        List<Beneficiary> beneficiaryList = beneficiaryRepository.findAll();
        assertThat(beneficiaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBeneficiaryWithPatch() throws Exception {
        // Initialize the database
        beneficiaryRepository.saveAndFlush(beneficiary);

        int databaseSizeBeforeUpdate = beneficiaryRepository.findAll().size();

        // Update the beneficiary using partial update
        Beneficiary partialUpdatedBeneficiary = new Beneficiary();
        partialUpdatedBeneficiary.setId(beneficiary.getId());

        partialUpdatedBeneficiary.entitlementType(UPDATED_ENTITLEMENT_TYPE).entitlementDetails(UPDATED_ENTITLEMENT_DETAILS);

        restBeneficiaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBeneficiary.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBeneficiary))
            )
            .andExpect(status().isOk());

        // Validate the Beneficiary in the database
        List<Beneficiary> beneficiaryList = beneficiaryRepository.findAll();
        assertThat(beneficiaryList).hasSize(databaseSizeBeforeUpdate);
        Beneficiary testBeneficiary = beneficiaryList.get(beneficiaryList.size() - 1);
        assertThat(testBeneficiary.getEntitlementType()).isEqualTo(UPDATED_ENTITLEMENT_TYPE);
        assertThat(testBeneficiary.getEntitlementDetails()).isEqualTo(UPDATED_ENTITLEMENT_DETAILS);
    }

    @Test
    @Transactional
    void fullUpdateBeneficiaryWithPatch() throws Exception {
        // Initialize the database
        beneficiaryRepository.saveAndFlush(beneficiary);

        int databaseSizeBeforeUpdate = beneficiaryRepository.findAll().size();

        // Update the beneficiary using partial update
        Beneficiary partialUpdatedBeneficiary = new Beneficiary();
        partialUpdatedBeneficiary.setId(beneficiary.getId());

        partialUpdatedBeneficiary.entitlementType(UPDATED_ENTITLEMENT_TYPE).entitlementDetails(UPDATED_ENTITLEMENT_DETAILS);

        restBeneficiaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBeneficiary.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBeneficiary))
            )
            .andExpect(status().isOk());

        // Validate the Beneficiary in the database
        List<Beneficiary> beneficiaryList = beneficiaryRepository.findAll();
        assertThat(beneficiaryList).hasSize(databaseSizeBeforeUpdate);
        Beneficiary testBeneficiary = beneficiaryList.get(beneficiaryList.size() - 1);
        assertThat(testBeneficiary.getEntitlementType()).isEqualTo(UPDATED_ENTITLEMENT_TYPE);
        assertThat(testBeneficiary.getEntitlementDetails()).isEqualTo(UPDATED_ENTITLEMENT_DETAILS);
    }

    @Test
    @Transactional
    void patchNonExistingBeneficiary() throws Exception {
        int databaseSizeBeforeUpdate = beneficiaryRepository.findAll().size();
        beneficiary.setId(RANDOME_FAMILY_Member_Annual_Declaration_Id);

        // Create the Beneficiary
        BeneficiaryDto_Empd beneficiaryDTO = beneficiaryMapper.toDto(beneficiary);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBeneficiaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, beneficiaryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(beneficiaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Beneficiary in the database
        List<Beneficiary> beneficiaryList = beneficiaryRepository.findAll();
        assertThat(beneficiaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBeneficiary() throws Exception {
        int databaseSizeBeforeUpdate = beneficiaryRepository.findAll().size();
        beneficiary.setId(RANDOME_FAMILY_Member_Annual_Declaration_Id);

        // Create the Beneficiary
        BeneficiaryDto_Empd beneficiaryDTO = beneficiaryMapper.toDto(beneficiary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBeneficiaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(beneficiaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Beneficiary in the database
        List<Beneficiary> beneficiaryList = beneficiaryRepository.findAll();
        assertThat(beneficiaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBeneficiary() throws Exception {
        int databaseSizeBeforeUpdate = beneficiaryRepository.findAll().size();
        beneficiary.setId(RANDOME_FAMILY_Member_Annual_Declaration_Id);

        // Create the Beneficiary
        BeneficiaryDto_Empd beneficiaryDTO = beneficiaryMapper.toDto(beneficiary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBeneficiaryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(beneficiaryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Beneficiary in the database
        List<Beneficiary> beneficiaryList = beneficiaryRepository.findAll();
        assertThat(beneficiaryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBeneficiary() throws Exception {
        // Initialize the database
        beneficiaryRepository.saveAndFlush(beneficiary);

        int databaseSizeBeforeDelete = beneficiaryRepository.findAll().size();

        // Delete the beneficiary
        restBeneficiaryMockMvc
            .perform(delete(ENTITY_API_URL_ID, beneficiary.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Beneficiary> beneficiaryList = beneficiaryRepository.findAll();
        assertThat(beneficiaryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
