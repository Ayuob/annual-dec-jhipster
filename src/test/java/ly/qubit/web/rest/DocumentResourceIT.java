package ly.qubit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import ly.qubit.IntegrationTest;
import ly.qubit.domain.Document;
import ly.qubit.domain.enumeration.DocumentdType;
import ly.qubit.repository.DocumentRepository;
import ly.qubit.service.DocumentService;
import ly.qubit.service.dto.DocumentDTO;
import ly.qubit.service.mapper.DocumentMapper;
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
 * Integration tests for the {@link DocumentResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DocumentResourceIT {

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_FILE_PATH = "BBBBBBBBBB";

    private static final Instant DEFAULT_SUBMISSION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SUBMISSION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final DocumentdType DEFAULT_DOCUMENTD_TYPE = DocumentdType.IDENTITY;
    private static final DocumentdType UPDATED_DOCUMENTD_TYPE = DocumentdType.INCOME;

    private static final String ENTITY_API_URL = "/api/documents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DocumentRepository documentRepository;

    @Mock
    private DocumentRepository documentRepositoryMock;

    @Autowired
    private DocumentMapper documentMapper;

    @Mock
    private DocumentService documentServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentMockMvc;

    private Document document;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Document createEntity(EntityManager em) {
        Document document = new Document()
            .fileName(DEFAULT_FILE_NAME)
            .filePath(DEFAULT_FILE_PATH)
            .submissionDate(DEFAULT_SUBMISSION_DATE)
            .documentdType(DEFAULT_DOCUMENTD_TYPE);
        return document;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Document createUpdatedEntity(EntityManager em) {
        Document document = new Document()
            .fileName(UPDATED_FILE_NAME)
            .filePath(UPDATED_FILE_PATH)
            .submissionDate(UPDATED_SUBMISSION_DATE)
            .documentdType(UPDATED_DOCUMENTD_TYPE);
        return document;
    }

    @BeforeEach
    public void initTest() {
        document = createEntity(em);
    }

    @Test
    @Transactional
    void createDocument() throws Exception {
        int databaseSizeBeforeCreate = documentRepository.findAll().size();
        // Create the Document
        DocumentDTO documentDTO = documentMapper.toDto(document);
        restDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentDTO)))
            .andExpect(status().isCreated());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeCreate + 1);
        Document testDocument = documentList.get(documentList.size() - 1);
        assertThat(testDocument.getFileName()).isEqualTo(DEFAULT_FILE_NAME);
        assertThat(testDocument.getFilePath()).isEqualTo(DEFAULT_FILE_PATH);
        assertThat(testDocument.getSubmissionDate()).isEqualTo(DEFAULT_SUBMISSION_DATE);
        assertThat(testDocument.getDocumentdType()).isEqualTo(DEFAULT_DOCUMENTD_TYPE);
    }

    @Test
    @Transactional
    void createDocumentWithExistingId() throws Exception {
        // Create the Document with an existing ID
        document.setId(1L);
        DocumentDTO documentDTO = documentMapper.toDto(document);

        int databaseSizeBeforeCreate = documentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFileNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = documentRepository.findAll().size();
        // set the field null
        document.setFileName(null);

        // Create the Document, which fails.
        DocumentDTO documentDTO = documentMapper.toDto(document);

        restDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentDTO)))
            .andExpect(status().isBadRequest());

        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFilePathIsRequired() throws Exception {
        int databaseSizeBeforeTest = documentRepository.findAll().size();
        // set the field null
        document.setFilePath(null);

        // Create the Document, which fails.
        DocumentDTO documentDTO = documentMapper.toDto(document);

        restDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentDTO)))
            .andExpect(status().isBadRequest());

        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubmissionDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = documentRepository.findAll().size();
        // set the field null
        document.setSubmissionDate(null);

        // Create the Document, which fails.
        DocumentDTO documentDTO = documentMapper.toDto(document);

        restDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentDTO)))
            .andExpect(status().isBadRequest());

        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDocuments() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get all the documentList
        restDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(document.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].filePath").value(hasItem(DEFAULT_FILE_PATH)))
            .andExpect(jsonPath("$.[*].submissionDate").value(hasItem(DEFAULT_SUBMISSION_DATE.toString())))
            .andExpect(jsonPath("$.[*].documentdType").value(hasItem(DEFAULT_DOCUMENTD_TYPE.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDocumentsWithEagerRelationshipsIsEnabled() throws Exception {
        when(documentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDocumentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(documentServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDocumentsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(documentServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDocumentMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(documentRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDocument() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        // Get the document
        restDocumentMockMvc
            .perform(get(ENTITY_API_URL_ID, document.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(document.getId().intValue()))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME))
            .andExpect(jsonPath("$.filePath").value(DEFAULT_FILE_PATH))
            .andExpect(jsonPath("$.submissionDate").value(DEFAULT_SUBMISSION_DATE.toString()))
            .andExpect(jsonPath("$.documentdType").value(DEFAULT_DOCUMENTD_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDocument() throws Exception {
        // Get the document
        restDocumentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocument() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        int databaseSizeBeforeUpdate = documentRepository.findAll().size();

        // Update the document
        Document updatedDocument = documentRepository.findById(document.getId()).get();
        // Disconnect from session so that the updates on updatedDocument are not directly saved in db
        em.detach(updatedDocument);
        updatedDocument
            .fileName(UPDATED_FILE_NAME)
            .filePath(UPDATED_FILE_PATH)
            .submissionDate(UPDATED_SUBMISSION_DATE)
            .documentdType(UPDATED_DOCUMENTD_TYPE);
        DocumentDTO documentDTO = documentMapper.toDto(updatedDocument);

        restDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
        Document testDocument = documentList.get(documentList.size() - 1);
        assertThat(testDocument.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testDocument.getFilePath()).isEqualTo(UPDATED_FILE_PATH);
        assertThat(testDocument.getSubmissionDate()).isEqualTo(UPDATED_SUBMISSION_DATE);
        assertThat(testDocument.getDocumentdType()).isEqualTo(UPDATED_DOCUMENTD_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingDocument() throws Exception {
        int databaseSizeBeforeUpdate = documentRepository.findAll().size();
        document.setId(count.incrementAndGet());

        // Create the Document
        DocumentDTO documentDTO = documentMapper.toDto(document);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocument() throws Exception {
        int databaseSizeBeforeUpdate = documentRepository.findAll().size();
        document.setId(count.incrementAndGet());

        // Create the Document
        DocumentDTO documentDTO = documentMapper.toDto(document);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(documentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocument() throws Exception {
        int databaseSizeBeforeUpdate = documentRepository.findAll().size();
        document.setId(count.incrementAndGet());

        // Create the Document
        DocumentDTO documentDTO = documentMapper.toDto(document);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(documentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocumentWithPatch() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        int databaseSizeBeforeUpdate = documentRepository.findAll().size();

        // Update the document using partial update
        Document partialUpdatedDocument = new Document();
        partialUpdatedDocument.setId(document.getId());

        partialUpdatedDocument.fileName(UPDATED_FILE_NAME).filePath(UPDATED_FILE_PATH);

        restDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocument))
            )
            .andExpect(status().isOk());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
        Document testDocument = documentList.get(documentList.size() - 1);
        assertThat(testDocument.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testDocument.getFilePath()).isEqualTo(UPDATED_FILE_PATH);
        assertThat(testDocument.getSubmissionDate()).isEqualTo(DEFAULT_SUBMISSION_DATE);
        assertThat(testDocument.getDocumentdType()).isEqualTo(DEFAULT_DOCUMENTD_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateDocumentWithPatch() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        int databaseSizeBeforeUpdate = documentRepository.findAll().size();

        // Update the document using partial update
        Document partialUpdatedDocument = new Document();
        partialUpdatedDocument.setId(document.getId());

        partialUpdatedDocument
            .fileName(UPDATED_FILE_NAME)
            .filePath(UPDATED_FILE_PATH)
            .submissionDate(UPDATED_SUBMISSION_DATE)
            .documentdType(UPDATED_DOCUMENTD_TYPE);

        restDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDocument))
            )
            .andExpect(status().isOk());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
        Document testDocument = documentList.get(documentList.size() - 1);
        assertThat(testDocument.getFileName()).isEqualTo(UPDATED_FILE_NAME);
        assertThat(testDocument.getFilePath()).isEqualTo(UPDATED_FILE_PATH);
        assertThat(testDocument.getSubmissionDate()).isEqualTo(UPDATED_SUBMISSION_DATE);
        assertThat(testDocument.getDocumentdType()).isEqualTo(UPDATED_DOCUMENTD_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingDocument() throws Exception {
        int databaseSizeBeforeUpdate = documentRepository.findAll().size();
        document.setId(count.incrementAndGet());

        // Create the Document
        DocumentDTO documentDTO = documentMapper.toDto(document);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocument() throws Exception {
        int databaseSizeBeforeUpdate = documentRepository.findAll().size();
        document.setId(count.incrementAndGet());

        // Create the Document
        DocumentDTO documentDTO = documentMapper.toDto(document);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(documentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocument() throws Exception {
        int databaseSizeBeforeUpdate = documentRepository.findAll().size();
        document.setId(count.incrementAndGet());

        // Create the Document
        DocumentDTO documentDTO = documentMapper.toDto(document);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(documentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Document in the database
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocument() throws Exception {
        // Initialize the database
        documentRepository.saveAndFlush(document);

        int databaseSizeBeforeDelete = documentRepository.findAll().size();

        // Delete the document
        restDocumentMockMvc
            .perform(delete(ENTITY_API_URL_ID, document.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Document> documentList = documentRepository.findAll();
        assertThat(documentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
