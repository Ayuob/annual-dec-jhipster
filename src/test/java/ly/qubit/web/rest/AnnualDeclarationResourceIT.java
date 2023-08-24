package ly.qubit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import ly.qubit.IntegrationTest;
import ly.qubit.domain.AnnualDeclaration;
import ly.qubit.domain.enumeration.DeclarationStatus;
import ly.qubit.repository.AnnualDeclarationRepository;
import ly.qubit.service.AnnualDeclarationService;
import ly.qubit.service.dto.AnnualDeclarationDTO;
import ly.qubit.service.mapper.AnnualDeclarationMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AnnualDeclarationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AnnualDeclarationResourceIT {

    private static final LocalDate DEFAULT_SUBMISSION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SUBMISSION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final DeclarationStatus DEFAULT_STATUS = DeclarationStatus.SUBMITTED;
    private static final DeclarationStatus UPDATED_STATUS = DeclarationStatus.APPROVED;

    private static final String ENTITY_API_URL = "/api/annual-declarations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AnnualDeclarationRepository annualDeclarationRepository;

    @Mock
    private AnnualDeclarationRepository annualDeclarationRepositoryMock;

    @Autowired
    private AnnualDeclarationMapper annualDeclarationMapper;

    @Mock
    private AnnualDeclarationService annualDeclarationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAnnualDeclarationMockMvc;

    private AnnualDeclaration annualDeclaration;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnnualDeclaration createEntity(EntityManager em) {
        AnnualDeclaration annualDeclaration = new AnnualDeclaration().submissionDate(DEFAULT_SUBMISSION_DATE).status(DEFAULT_STATUS);
        return annualDeclaration;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnnualDeclaration createUpdatedEntity(EntityManager em) {
        AnnualDeclaration annualDeclaration = new AnnualDeclaration().submissionDate(UPDATED_SUBMISSION_DATE).status(UPDATED_STATUS);
        return annualDeclaration;
    }

    @BeforeEach
    public void initTest() {
        annualDeclaration = createEntity(em);
    }

    @Test
    @Transactional
    void createAnnualDeclaration() throws Exception {
        int databaseSizeBeforeCreate = annualDeclarationRepository.findAll().size();
        // Create the AnnualDeclaration
        AnnualDeclarationDTO annualDeclarationDTO = annualDeclarationMapper.toDto(annualDeclaration);
        restAnnualDeclarationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(annualDeclarationDTO))
            )
            .andExpect(status().isCreated());

        // Validate the AnnualDeclaration in the database
        List<AnnualDeclaration> annualDeclarationList = annualDeclarationRepository.findAll();
        assertThat(annualDeclarationList).hasSize(databaseSizeBeforeCreate + 1);
        AnnualDeclaration testAnnualDeclaration = annualDeclarationList.get(annualDeclarationList.size() - 1);
        assertThat(testAnnualDeclaration.getSubmissionDate()).isEqualTo(DEFAULT_SUBMISSION_DATE);
        assertThat(testAnnualDeclaration.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createAnnualDeclarationWithExistingId() throws Exception {
        // Create the AnnualDeclaration with an existing ID
        annualDeclaration.setId(1L);
        AnnualDeclarationDTO annualDeclarationDTO = annualDeclarationMapper.toDto(annualDeclaration);

        int databaseSizeBeforeCreate = annualDeclarationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnnualDeclarationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(annualDeclarationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnnualDeclaration in the database
        List<AnnualDeclaration> annualDeclarationList = annualDeclarationRepository.findAll();
        assertThat(annualDeclarationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSubmissionDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = annualDeclarationRepository.findAll().size();
        // set the field null
        annualDeclaration.setSubmissionDate(null);

        // Create the AnnualDeclaration, which fails.
        AnnualDeclarationDTO annualDeclarationDTO = annualDeclarationMapper.toDto(annualDeclaration);

        restAnnualDeclarationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(annualDeclarationDTO))
            )
            .andExpect(status().isBadRequest());

        List<AnnualDeclaration> annualDeclarationList = annualDeclarationRepository.findAll();
        assertThat(annualDeclarationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = annualDeclarationRepository.findAll().size();
        // set the field null
        annualDeclaration.setStatus(null);

        // Create the AnnualDeclaration, which fails.
        AnnualDeclarationDTO annualDeclarationDTO = annualDeclarationMapper.toDto(annualDeclaration);

        restAnnualDeclarationMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(annualDeclarationDTO))
            )
            .andExpect(status().isBadRequest());

        List<AnnualDeclaration> annualDeclarationList = annualDeclarationRepository.findAll();
        assertThat(annualDeclarationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAnnualDeclarations() throws Exception {
        // Initialize the database
        annualDeclarationRepository.saveAndFlush(annualDeclaration);

        // Get all the annualDeclarationList
        restAnnualDeclarationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(annualDeclaration.getId().intValue())))
            .andExpect(jsonPath("$.[*].submissionDate").value(hasItem(DEFAULT_SUBMISSION_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAnnualDeclarationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(annualDeclarationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAnnualDeclarationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(annualDeclarationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAnnualDeclarationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(annualDeclarationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAnnualDeclarationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(annualDeclarationRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAnnualDeclaration() throws Exception {
        // Initialize the database
        annualDeclarationRepository.saveAndFlush(annualDeclaration);

        // Get the annualDeclaration
        restAnnualDeclarationMockMvc
            .perform(get(ENTITY_API_URL_ID, annualDeclaration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(annualDeclaration.getId().intValue()))
            .andExpect(jsonPath("$.submissionDate").value(DEFAULT_SUBMISSION_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAnnualDeclaration() throws Exception {
        // Get the annualDeclaration
        restAnnualDeclarationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAnnualDeclaration() throws Exception {
        // Initialize the database
        annualDeclarationRepository.saveAndFlush(annualDeclaration);

        int databaseSizeBeforeUpdate = annualDeclarationRepository.findAll().size();

        // Update the annualDeclaration
        AnnualDeclaration updatedAnnualDeclaration = annualDeclarationRepository.findById(annualDeclaration.getId()).get();
        // Disconnect from session so that the updates on updatedAnnualDeclaration are not directly saved in db
        em.detach(updatedAnnualDeclaration);
        updatedAnnualDeclaration.submissionDate(UPDATED_SUBMISSION_DATE).status(UPDATED_STATUS);
        AnnualDeclarationDTO annualDeclarationDTO = annualDeclarationMapper.toDto(updatedAnnualDeclaration);

        restAnnualDeclarationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, annualDeclarationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(annualDeclarationDTO))
            )
            .andExpect(status().isOk());

        // Validate the AnnualDeclaration in the database
        List<AnnualDeclaration> annualDeclarationList = annualDeclarationRepository.findAll();
        assertThat(annualDeclarationList).hasSize(databaseSizeBeforeUpdate);
        AnnualDeclaration testAnnualDeclaration = annualDeclarationList.get(annualDeclarationList.size() - 1);
        assertThat(testAnnualDeclaration.getSubmissionDate()).isEqualTo(UPDATED_SUBMISSION_DATE);
        assertThat(testAnnualDeclaration.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingAnnualDeclaration() throws Exception {
        int databaseSizeBeforeUpdate = annualDeclarationRepository.findAll().size();
        annualDeclaration.setId(count.incrementAndGet());

        // Create the AnnualDeclaration
        AnnualDeclarationDTO annualDeclarationDTO = annualDeclarationMapper.toDto(annualDeclaration);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnnualDeclarationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, annualDeclarationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(annualDeclarationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnnualDeclaration in the database
        List<AnnualDeclaration> annualDeclarationList = annualDeclarationRepository.findAll();
        assertThat(annualDeclarationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAnnualDeclaration() throws Exception {
        int databaseSizeBeforeUpdate = annualDeclarationRepository.findAll().size();
        annualDeclaration.setId(count.incrementAndGet());

        // Create the AnnualDeclaration
        AnnualDeclarationDTO annualDeclarationDTO = annualDeclarationMapper.toDto(annualDeclaration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnnualDeclarationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(annualDeclarationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnnualDeclaration in the database
        List<AnnualDeclaration> annualDeclarationList = annualDeclarationRepository.findAll();
        assertThat(annualDeclarationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAnnualDeclaration() throws Exception {
        int databaseSizeBeforeUpdate = annualDeclarationRepository.findAll().size();
        annualDeclaration.setId(count.incrementAndGet());

        // Create the AnnualDeclaration
        AnnualDeclarationDTO annualDeclarationDTO = annualDeclarationMapper.toDto(annualDeclaration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnnualDeclarationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(annualDeclarationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AnnualDeclaration in the database
        List<AnnualDeclaration> annualDeclarationList = annualDeclarationRepository.findAll();
        assertThat(annualDeclarationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAnnualDeclarationWithPatch() throws Exception {
        // Initialize the database
        annualDeclarationRepository.saveAndFlush(annualDeclaration);

        int databaseSizeBeforeUpdate = annualDeclarationRepository.findAll().size();

        // Update the annualDeclaration using partial update
        AnnualDeclaration partialUpdatedAnnualDeclaration = new AnnualDeclaration();
        partialUpdatedAnnualDeclaration.setId(annualDeclaration.getId());

        partialUpdatedAnnualDeclaration.submissionDate(UPDATED_SUBMISSION_DATE);

        restAnnualDeclarationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnnualDeclaration.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAnnualDeclaration))
            )
            .andExpect(status().isOk());

        // Validate the AnnualDeclaration in the database
        List<AnnualDeclaration> annualDeclarationList = annualDeclarationRepository.findAll();
        assertThat(annualDeclarationList).hasSize(databaseSizeBeforeUpdate);
        AnnualDeclaration testAnnualDeclaration = annualDeclarationList.get(annualDeclarationList.size() - 1);
        assertThat(testAnnualDeclaration.getSubmissionDate()).isEqualTo(UPDATED_SUBMISSION_DATE);
        assertThat(testAnnualDeclaration.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateAnnualDeclarationWithPatch() throws Exception {
        // Initialize the database
        annualDeclarationRepository.saveAndFlush(annualDeclaration);

        int databaseSizeBeforeUpdate = annualDeclarationRepository.findAll().size();

        // Update the annualDeclaration using partial update
        AnnualDeclaration partialUpdatedAnnualDeclaration = new AnnualDeclaration();
        partialUpdatedAnnualDeclaration.setId(annualDeclaration.getId());

        partialUpdatedAnnualDeclaration.submissionDate(UPDATED_SUBMISSION_DATE).status(UPDATED_STATUS);

        restAnnualDeclarationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnnualDeclaration.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAnnualDeclaration))
            )
            .andExpect(status().isOk());

        // Validate the AnnualDeclaration in the database
        List<AnnualDeclaration> annualDeclarationList = annualDeclarationRepository.findAll();
        assertThat(annualDeclarationList).hasSize(databaseSizeBeforeUpdate);
        AnnualDeclaration testAnnualDeclaration = annualDeclarationList.get(annualDeclarationList.size() - 1);
        assertThat(testAnnualDeclaration.getSubmissionDate()).isEqualTo(UPDATED_SUBMISSION_DATE);
        assertThat(testAnnualDeclaration.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingAnnualDeclaration() throws Exception {
        int databaseSizeBeforeUpdate = annualDeclarationRepository.findAll().size();
        annualDeclaration.setId(count.incrementAndGet());

        // Create the AnnualDeclaration
        AnnualDeclarationDTO annualDeclarationDTO = annualDeclarationMapper.toDto(annualDeclaration);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnnualDeclarationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, annualDeclarationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(annualDeclarationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnnualDeclaration in the database
        List<AnnualDeclaration> annualDeclarationList = annualDeclarationRepository.findAll();
        assertThat(annualDeclarationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAnnualDeclaration() throws Exception {
        int databaseSizeBeforeUpdate = annualDeclarationRepository.findAll().size();
        annualDeclaration.setId(count.incrementAndGet());

        // Create the AnnualDeclaration
        AnnualDeclarationDTO annualDeclarationDTO = annualDeclarationMapper.toDto(annualDeclaration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnnualDeclarationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(annualDeclarationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnnualDeclaration in the database
        List<AnnualDeclaration> annualDeclarationList = annualDeclarationRepository.findAll();
        assertThat(annualDeclarationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAnnualDeclaration() throws Exception {
        int databaseSizeBeforeUpdate = annualDeclarationRepository.findAll().size();
        annualDeclaration.setId(count.incrementAndGet());

        // Create the AnnualDeclaration
        AnnualDeclarationDTO annualDeclarationDTO = annualDeclarationMapper.toDto(annualDeclaration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnnualDeclarationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(annualDeclarationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AnnualDeclaration in the database
        List<AnnualDeclaration> annualDeclarationList = annualDeclarationRepository.findAll();
        assertThat(annualDeclarationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAnnualDeclaration() throws Exception {
        // Initialize the database
        annualDeclarationRepository.saveAndFlush(annualDeclaration);

        int databaseSizeBeforeDelete = annualDeclarationRepository.findAll().size();

        // Delete the annualDeclaration
        restAnnualDeclarationMockMvc
            .perform(delete(ENTITY_API_URL_ID, annualDeclaration.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AnnualDeclaration> annualDeclarationList = annualDeclarationRepository.findAll();
        assertThat(annualDeclarationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
