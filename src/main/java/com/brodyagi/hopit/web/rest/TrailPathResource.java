package com.brodyagi.hopit.web.rest;

import com.brodyagi.hopit.domain.TrailPath;
import com.brodyagi.hopit.repository.TrailPathRepository;
import com.brodyagi.hopit.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.brodyagi.hopit.domain.TrailPath}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TrailPathResource {

    private final Logger log = LoggerFactory.getLogger(TrailPathResource.class);

    private static final String ENTITY_NAME = "trailPath";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TrailPathRepository trailPathRepository;

    public TrailPathResource(TrailPathRepository trailPathRepository) {
        this.trailPathRepository = trailPathRepository;
    }

    /**
     * {@code POST  /trail-paths} : Create a new trailPath.
     *
     * @param trailPath the trailPath to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new trailPath, or with status {@code 400 (Bad Request)} if the trailPath has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/trail-paths")
    public ResponseEntity<TrailPath> createTrailPath(@Valid @RequestBody TrailPath trailPath) throws URISyntaxException {
        log.debug("REST request to save TrailPath : {}", trailPath);
        if (trailPath.getId() != null) {
            throw new BadRequestAlertException("A new trailPath cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TrailPath result = trailPathRepository.save(trailPath);
        return ResponseEntity
            .created(new URI("/api/trail-paths/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /trail-paths/:id} : Updates an existing trailPath.
     *
     * @param id the id of the trailPath to save.
     * @param trailPath the trailPath to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trailPath,
     * or with status {@code 400 (Bad Request)} if the trailPath is not valid,
     * or with status {@code 500 (Internal Server Error)} if the trailPath couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/trail-paths/{id}")
    public ResponseEntity<TrailPath> updateTrailPath(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TrailPath trailPath
    ) throws URISyntaxException {
        log.debug("REST request to update TrailPath : {}, {}", id, trailPath);
        if (trailPath.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trailPath.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trailPathRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TrailPath result = trailPathRepository.save(trailPath);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, trailPath.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /trail-paths/:id} : Partial updates given fields of an existing trailPath, field will ignore if it is null
     *
     * @param id the id of the trailPath to save.
     * @param trailPath the trailPath to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trailPath,
     * or with status {@code 400 (Bad Request)} if the trailPath is not valid,
     * or with status {@code 404 (Not Found)} if the trailPath is not found,
     * or with status {@code 500 (Internal Server Error)} if the trailPath couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/trail-paths/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TrailPath> partialUpdateTrailPath(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TrailPath trailPath
    ) throws URISyntaxException {
        log.debug("REST request to partial update TrailPath partially : {}, {}", id, trailPath);
        if (trailPath.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trailPath.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trailPathRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TrailPath> result = trailPathRepository
            .findById(trailPath.getId())
            .map(
                existingTrailPath -> {
                    if (trailPath.getTitle() != null) {
                        existingTrailPath.setTitle(trailPath.getTitle());
                    }
                    if (trailPath.getDescription() != null) {
                        existingTrailPath.setDescription(trailPath.getDescription());
                    }

                    return existingTrailPath;
                }
            )
            .map(trailPathRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, trailPath.getId().toString())
        );
    }

    /**
     * {@code GET  /trail-paths} : get all the trailPaths.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of trailPaths in body.
     */
    @GetMapping("/trail-paths")
    public List<TrailPath> getAllTrailPaths() {
        log.debug("REST request to get all TrailPaths");
        return trailPathRepository.findAll();
    }

    /**
     * {@code GET  /trail-paths/:id} : get the "id" trailPath.
     *
     * @param id the id of the trailPath to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the trailPath, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/trail-paths/{id}")
    public ResponseEntity<TrailPath> getTrailPath(@PathVariable Long id) {
        log.debug("REST request to get TrailPath : {}", id);
        Optional<TrailPath> trailPath = trailPathRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(trailPath);
    }

    /**
     * {@code DELETE  /trail-paths/:id} : delete the "id" trailPath.
     *
     * @param id the id of the trailPath to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/trail-paths/{id}")
    public ResponseEntity<Void> deleteTrailPath(@PathVariable Long id) {
        log.debug("REST request to delete TrailPath : {}", id);
        trailPathRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
