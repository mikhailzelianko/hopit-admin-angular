package com.brodyagi.hopit.web.rest;

import com.brodyagi.hopit.domain.TrailPathWaypoint;
import com.brodyagi.hopit.repository.TrailPathWaypointRepository;
import com.brodyagi.hopit.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.brodyagi.hopit.domain.TrailPathWaypoint}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TrailPathWaypointResource {

    private final Logger log = LoggerFactory.getLogger(TrailPathWaypointResource.class);

    private static final String ENTITY_NAME = "trailPathWaypoint";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TrailPathWaypointRepository trailPathWaypointRepository;

    public TrailPathWaypointResource(TrailPathWaypointRepository trailPathWaypointRepository) {
        this.trailPathWaypointRepository = trailPathWaypointRepository;
    }

    /**
     * {@code POST  /trail-path-waypoints} : Create a new trailPathWaypoint.
     *
     * @param trailPathWaypoint the trailPathWaypoint to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new trailPathWaypoint, or with status {@code 400 (Bad Request)} if the trailPathWaypoint has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/trail-path-waypoints")
    public ResponseEntity<TrailPathWaypoint> createTrailPathWaypoint(@RequestBody TrailPathWaypoint trailPathWaypoint)
        throws URISyntaxException {
        log.debug("REST request to save TrailPathWaypoint : {}", trailPathWaypoint);
        if (trailPathWaypoint.getId() != null) {
            throw new BadRequestAlertException("A new trailPathWaypoint cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TrailPathWaypoint result = trailPathWaypointRepository.save(trailPathWaypoint);
        return ResponseEntity
            .created(new URI("/api/trail-path-waypoints/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /trail-path-waypoints/:id} : Updates an existing trailPathWaypoint.
     *
     * @param id the id of the trailPathWaypoint to save.
     * @param trailPathWaypoint the trailPathWaypoint to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trailPathWaypoint,
     * or with status {@code 400 (Bad Request)} if the trailPathWaypoint is not valid,
     * or with status {@code 500 (Internal Server Error)} if the trailPathWaypoint couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/trail-path-waypoints/{id}")
    public ResponseEntity<TrailPathWaypoint> updateTrailPathWaypoint(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TrailPathWaypoint trailPathWaypoint
    ) throws URISyntaxException {
        log.debug("REST request to update TrailPathWaypoint : {}, {}", id, trailPathWaypoint);
        if (trailPathWaypoint.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trailPathWaypoint.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trailPathWaypointRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TrailPathWaypoint result = trailPathWaypointRepository.save(trailPathWaypoint);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, trailPathWaypoint.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /trail-path-waypoints/:id} : Partial updates given fields of an existing trailPathWaypoint, field will ignore if it is null
     *
     * @param id the id of the trailPathWaypoint to save.
     * @param trailPathWaypoint the trailPathWaypoint to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trailPathWaypoint,
     * or with status {@code 400 (Bad Request)} if the trailPathWaypoint is not valid,
     * or with status {@code 404 (Not Found)} if the trailPathWaypoint is not found,
     * or with status {@code 500 (Internal Server Error)} if the trailPathWaypoint couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/trail-path-waypoints/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TrailPathWaypoint> partialUpdateTrailPathWaypoint(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TrailPathWaypoint trailPathWaypoint
    ) throws URISyntaxException {
        log.debug("REST request to partial update TrailPathWaypoint partially : {}, {}", id, trailPathWaypoint);
        if (trailPathWaypoint.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trailPathWaypoint.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trailPathWaypointRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TrailPathWaypoint> result = trailPathWaypointRepository
            .findById(trailPathWaypoint.getId())
            .map(
                existingTrailPathWaypoint -> {
                    if (trailPathWaypoint.getWaypointLat() != null) {
                        existingTrailPathWaypoint.setWaypointLat(trailPathWaypoint.getWaypointLat());
                    }
                    if (trailPathWaypoint.getWaypointLong() != null) {
                        existingTrailPathWaypoint.setWaypointLong(trailPathWaypoint.getWaypointLong());
                    }

                    return existingTrailPathWaypoint;
                }
            )
            .map(trailPathWaypointRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, trailPathWaypoint.getId().toString())
        );
    }

    /**
     * {@code GET  /trail-path-waypoints} : get all the trailPathWaypoints.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of trailPathWaypoints in body.
     */
    @GetMapping("/trail-path-waypoints")
    public List<TrailPathWaypoint> getAllTrailPathWaypoints() {
        log.debug("REST request to get all TrailPathWaypoints");
        return trailPathWaypointRepository.findAll();
    }

    /**
     * {@code GET  /trail-path-waypoints/:id} : get the "id" trailPathWaypoint.
     *
     * @param id the id of the trailPathWaypoint to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the trailPathWaypoint, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/trail-path-waypoints/{id}")
    public ResponseEntity<TrailPathWaypoint> getTrailPathWaypoint(@PathVariable Long id) {
        log.debug("REST request to get TrailPathWaypoint : {}", id);
        Optional<TrailPathWaypoint> trailPathWaypoint = trailPathWaypointRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(trailPathWaypoint);
    }

    /**
     * {@code DELETE  /trail-path-waypoints/:id} : delete the "id" trailPathWaypoint.
     *
     * @param id the id of the trailPathWaypoint to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/trail-path-waypoints/{id}")
    public ResponseEntity<Void> deleteTrailPathWaypoint(@PathVariable Long id) {
        log.debug("REST request to delete TrailPathWaypoint : {}", id);
        trailPathWaypointRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
