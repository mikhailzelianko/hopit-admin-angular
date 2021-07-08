package com.brodyagi.hopit.web.rest;

import com.brodyagi.hopit.domain.AdditionalMapObject;
import com.brodyagi.hopit.repository.AdditionalMapObjectRepository;
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
 * REST controller for managing {@link com.brodyagi.hopit.domain.AdditionalMapObject}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AdditionalMapObjectResource {

    private final Logger log = LoggerFactory.getLogger(AdditionalMapObjectResource.class);

    private static final String ENTITY_NAME = "additionalMapObject";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AdditionalMapObjectRepository additionalMapObjectRepository;

    public AdditionalMapObjectResource(AdditionalMapObjectRepository additionalMapObjectRepository) {
        this.additionalMapObjectRepository = additionalMapObjectRepository;
    }

    /**
     * {@code POST  /additional-map-objects} : Create a new additionalMapObject.
     *
     * @param additionalMapObject the additionalMapObject to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new additionalMapObject, or with status {@code 400 (Bad Request)} if the additionalMapObject has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/additional-map-objects")
    public ResponseEntity<AdditionalMapObject> createAdditionalMapObject(@Valid @RequestBody AdditionalMapObject additionalMapObject)
        throws URISyntaxException {
        log.debug("REST request to save AdditionalMapObject : {}", additionalMapObject);
        if (additionalMapObject.getId() != null) {
            throw new BadRequestAlertException("A new additionalMapObject cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AdditionalMapObject result = additionalMapObjectRepository.save(additionalMapObject);
        return ResponseEntity
            .created(new URI("/api/additional-map-objects/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /additional-map-objects/:id} : Updates an existing additionalMapObject.
     *
     * @param id the id of the additionalMapObject to save.
     * @param additionalMapObject the additionalMapObject to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated additionalMapObject,
     * or with status {@code 400 (Bad Request)} if the additionalMapObject is not valid,
     * or with status {@code 500 (Internal Server Error)} if the additionalMapObject couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/additional-map-objects/{id}")
    public ResponseEntity<AdditionalMapObject> updateAdditionalMapObject(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AdditionalMapObject additionalMapObject
    ) throws URISyntaxException {
        log.debug("REST request to update AdditionalMapObject : {}, {}", id, additionalMapObject);
        if (additionalMapObject.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, additionalMapObject.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!additionalMapObjectRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AdditionalMapObject result = additionalMapObjectRepository.save(additionalMapObject);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, additionalMapObject.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /additional-map-objects/:id} : Partial updates given fields of an existing additionalMapObject, field will ignore if it is null
     *
     * @param id the id of the additionalMapObject to save.
     * @param additionalMapObject the additionalMapObject to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated additionalMapObject,
     * or with status {@code 400 (Bad Request)} if the additionalMapObject is not valid,
     * or with status {@code 404 (Not Found)} if the additionalMapObject is not found,
     * or with status {@code 500 (Internal Server Error)} if the additionalMapObject couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/additional-map-objects/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<AdditionalMapObject> partialUpdateAdditionalMapObject(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AdditionalMapObject additionalMapObject
    ) throws URISyntaxException {
        log.debug("REST request to partial update AdditionalMapObject partially : {}, {}", id, additionalMapObject);
        if (additionalMapObject.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, additionalMapObject.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!additionalMapObjectRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AdditionalMapObject> result = additionalMapObjectRepository
            .findById(additionalMapObject.getId())
            .map(
                existingAdditionalMapObject -> {
                    if (additionalMapObject.getTitle() != null) {
                        existingAdditionalMapObject.setTitle(additionalMapObject.getTitle());
                    }
                    if (additionalMapObject.getObjectLat() != null) {
                        existingAdditionalMapObject.setObjectLat(additionalMapObject.getObjectLat());
                    }
                    if (additionalMapObject.getObjectLong() != null) {
                        existingAdditionalMapObject.setObjectLong(additionalMapObject.getObjectLong());
                    }
                    if (additionalMapObject.getType() != null) {
                        existingAdditionalMapObject.setType(additionalMapObject.getType());
                    }
                    if (additionalMapObject.getDescription() != null) {
                        existingAdditionalMapObject.setDescription(additionalMapObject.getDescription());
                    }

                    return existingAdditionalMapObject;
                }
            )
            .map(additionalMapObjectRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, additionalMapObject.getId().toString())
        );
    }

    /**
     * {@code GET  /additional-map-objects} : get all the additionalMapObjects.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of additionalMapObjects in body.
     */
    @GetMapping("/additional-map-objects")
    public List<AdditionalMapObject> getAllAdditionalMapObjects() {
        log.debug("REST request to get all AdditionalMapObjects");
        return additionalMapObjectRepository.findAll();
    }

    /**
     * {@code GET  /additional-map-objects/:id} : get the "id" additionalMapObject.
     *
     * @param id the id of the additionalMapObject to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the additionalMapObject, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/additional-map-objects/{id}")
    public ResponseEntity<AdditionalMapObject> getAdditionalMapObject(@PathVariable Long id) {
        log.debug("REST request to get AdditionalMapObject : {}", id);
        Optional<AdditionalMapObject> additionalMapObject = additionalMapObjectRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(additionalMapObject);
    }

    /**
     * {@code DELETE  /additional-map-objects/:id} : delete the "id" additionalMapObject.
     *
     * @param id the id of the additionalMapObject to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/additional-map-objects/{id}")
    public ResponseEntity<Void> deleteAdditionalMapObject(@PathVariable Long id) {
        log.debug("REST request to delete AdditionalMapObject : {}", id);
        additionalMapObjectRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
