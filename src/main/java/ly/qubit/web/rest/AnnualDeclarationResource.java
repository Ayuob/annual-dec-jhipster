package ly.qubit.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import ly.qubit.repository.AnnualDeclarationRepository;
import ly.qubit.service.AnnualDeclarationService;
import ly.qubit.service.dto.AnnualDeclarationDTO;
import ly.qubit.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link ly.qubit.domain.AnnualDeclaration}.
 */
@RestController
@RequestMapping("/api")
public class AnnualDeclarationResource {

    private final Logger log = LoggerFactory.getLogger(AnnualDeclarationResource.class);

    private static final String ENTITY_NAME = "annualDeclaration";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AnnualDeclarationService annualDeclarationService;

    private final AnnualDeclarationRepository annualDeclarationRepository;

    public AnnualDeclarationResource(
        AnnualDeclarationService annualDeclarationService,
        AnnualDeclarationRepository annualDeclarationRepository
    ) {
        this.annualDeclarationService = annualDeclarationService;
        this.annualDeclarationRepository = annualDeclarationRepository;
    }

    /**
     * {@code POST  /annual-declarations} : Create a new annualDeclaration.
     *
     * @param annualDeclarationDTO the annualDeclarationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new annualDeclarationDTO, or with status {@code 400 (Bad Request)} if the annualDeclaration has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/annual-declarations")
    public ResponseEntity<AnnualDeclarationDTO> createAnnualDeclaration(@Valid @RequestBody AnnualDeclarationDTO annualDeclarationDTO)
        throws URISyntaxException {
        log.debug("REST request to save AnnualDeclaration : {}", annualDeclarationDTO);
        if (annualDeclarationDTO.getId() != null) {
            throw new BadRequestAlertException("A new annualDeclaration cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AnnualDeclarationDTO result = annualDeclarationService.save(annualDeclarationDTO);
        return ResponseEntity
            .created(new URI("/api/annual-declarations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /annual-declarations/:id} : Updates an existing annualDeclaration.
     *
     * @param id the id of the annualDeclarationDTO to save.
     * @param annualDeclarationDTO the annualDeclarationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated annualDeclarationDTO,
     * or with status {@code 400 (Bad Request)} if the annualDeclarationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the annualDeclarationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/annual-declarations/{id}")
    public ResponseEntity<AnnualDeclarationDTO> updateAnnualDeclaration(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AnnualDeclarationDTO annualDeclarationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AnnualDeclaration : {}, {}", id, annualDeclarationDTO);
        if (annualDeclarationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, annualDeclarationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!annualDeclarationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AnnualDeclarationDTO result = annualDeclarationService.update(annualDeclarationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, annualDeclarationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /annual-declarations/:id} : Partial updates given fields of an existing annualDeclaration, field will ignore if it is null
     *
     * @param id the id of the annualDeclarationDTO to save.
     * @param annualDeclarationDTO the annualDeclarationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated annualDeclarationDTO,
     * or with status {@code 400 (Bad Request)} if the annualDeclarationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the annualDeclarationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the annualDeclarationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/annual-declarations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AnnualDeclarationDTO> partialUpdateAnnualDeclaration(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AnnualDeclarationDTO annualDeclarationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AnnualDeclaration partially : {}, {}", id, annualDeclarationDTO);
        if (annualDeclarationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, annualDeclarationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!annualDeclarationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AnnualDeclarationDTO> result = annualDeclarationService.partialUpdate(annualDeclarationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, annualDeclarationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /annual-declarations} : get all the annualDeclarations.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of annualDeclarations in body.
     */
    @GetMapping("/annual-declarations")
    public ResponseEntity<List<AnnualDeclarationDTO>> getAllAnnualDeclarations(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of AnnualDeclarations");
        Page<AnnualDeclarationDTO> page;
        if (eagerload) {
            page = annualDeclarationService.findAllWithEagerRelationships(pageable);
        } else {
            page = annualDeclarationService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /annual-declarations/:id} : get the "id" annualDeclaration.
     *
     * @param id the id of the annualDeclarationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the annualDeclarationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/annual-declarations/{id}")
    public ResponseEntity<AnnualDeclarationDTO> getAnnualDeclaration(@PathVariable Long id) {
        log.debug("REST request to get AnnualDeclaration : {}", id);
        Optional<AnnualDeclarationDTO> annualDeclarationDTO = annualDeclarationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(annualDeclarationDTO);
    }

    /**
     * {@code DELETE  /annual-declarations/:id} : delete the "id" annualDeclaration.
     *
     * @param id the id of the annualDeclarationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/annual-declarations/{id}")
    public ResponseEntity<Void> deleteAnnualDeclaration(@PathVariable Long id) {
        log.debug("REST request to delete AnnualDeclaration : {}", id);
        annualDeclarationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
