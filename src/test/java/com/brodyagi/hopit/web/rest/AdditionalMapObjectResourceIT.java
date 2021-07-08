package com.brodyagi.hopit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.brodyagi.hopit.IntegrationTest;
import com.brodyagi.hopit.domain.AdditionalMapObject;
import com.brodyagi.hopit.repository.AdditionalMapObjectRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AdditionalMapObjectResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AdditionalMapObjectResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Long DEFAULT_OBJECT_LAT = 1L;
    private static final Long UPDATED_OBJECT_LAT = 2L;

    private static final Long DEFAULT_OBJECT_LONG = 1L;
    private static final Long UPDATED_OBJECT_LONG = 2L;

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/additional-map-objects";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AdditionalMapObjectRepository additionalMapObjectRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAdditionalMapObjectMockMvc;

    private AdditionalMapObject additionalMapObject;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdditionalMapObject createEntity(EntityManager em) {
        AdditionalMapObject additionalMapObject = new AdditionalMapObject()
            .title(DEFAULT_TITLE)
            .objectLat(DEFAULT_OBJECT_LAT)
            .objectLong(DEFAULT_OBJECT_LONG)
            .type(DEFAULT_TYPE)
            .description(DEFAULT_DESCRIPTION);
        return additionalMapObject;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdditionalMapObject createUpdatedEntity(EntityManager em) {
        AdditionalMapObject additionalMapObject = new AdditionalMapObject()
            .title(UPDATED_TITLE)
            .objectLat(UPDATED_OBJECT_LAT)
            .objectLong(UPDATED_OBJECT_LONG)
            .type(UPDATED_TYPE)
            .description(UPDATED_DESCRIPTION);
        return additionalMapObject;
    }

    @BeforeEach
    public void initTest() {
        additionalMapObject = createEntity(em);
    }

    @Test
    @Transactional
    void createAdditionalMapObject() throws Exception {
        int databaseSizeBeforeCreate = additionalMapObjectRepository.findAll().size();
        // Create the AdditionalMapObject
        restAdditionalMapObjectMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(additionalMapObject))
            )
            .andExpect(status().isCreated());

        // Validate the AdditionalMapObject in the database
        List<AdditionalMapObject> additionalMapObjectList = additionalMapObjectRepository.findAll();
        assertThat(additionalMapObjectList).hasSize(databaseSizeBeforeCreate + 1);
        AdditionalMapObject testAdditionalMapObject = additionalMapObjectList.get(additionalMapObjectList.size() - 1);
        assertThat(testAdditionalMapObject.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testAdditionalMapObject.getObjectLat()).isEqualTo(DEFAULT_OBJECT_LAT);
        assertThat(testAdditionalMapObject.getObjectLong()).isEqualTo(DEFAULT_OBJECT_LONG);
        assertThat(testAdditionalMapObject.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testAdditionalMapObject.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createAdditionalMapObjectWithExistingId() throws Exception {
        // Create the AdditionalMapObject with an existing ID
        additionalMapObject.setId(1L);

        int databaseSizeBeforeCreate = additionalMapObjectRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdditionalMapObjectMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(additionalMapObject))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdditionalMapObject in the database
        List<AdditionalMapObject> additionalMapObjectList = additionalMapObjectRepository.findAll();
        assertThat(additionalMapObjectList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = additionalMapObjectRepository.findAll().size();
        // set the field null
        additionalMapObject.setTitle(null);

        // Create the AdditionalMapObject, which fails.

        restAdditionalMapObjectMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(additionalMapObject))
            )
            .andExpect(status().isBadRequest());

        List<AdditionalMapObject> additionalMapObjectList = additionalMapObjectRepository.findAll();
        assertThat(additionalMapObjectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAdditionalMapObjects() throws Exception {
        // Initialize the database
        additionalMapObjectRepository.saveAndFlush(additionalMapObject);

        // Get all the additionalMapObjectList
        restAdditionalMapObjectMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(additionalMapObject.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].objectLat").value(hasItem(DEFAULT_OBJECT_LAT.intValue())))
            .andExpect(jsonPath("$.[*].objectLong").value(hasItem(DEFAULT_OBJECT_LONG.intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getAdditionalMapObject() throws Exception {
        // Initialize the database
        additionalMapObjectRepository.saveAndFlush(additionalMapObject);

        // Get the additionalMapObject
        restAdditionalMapObjectMockMvc
            .perform(get(ENTITY_API_URL_ID, additionalMapObject.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(additionalMapObject.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.objectLat").value(DEFAULT_OBJECT_LAT.intValue()))
            .andExpect(jsonPath("$.objectLong").value(DEFAULT_OBJECT_LONG.intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingAdditionalMapObject() throws Exception {
        // Get the additionalMapObject
        restAdditionalMapObjectMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAdditionalMapObject() throws Exception {
        // Initialize the database
        additionalMapObjectRepository.saveAndFlush(additionalMapObject);

        int databaseSizeBeforeUpdate = additionalMapObjectRepository.findAll().size();

        // Update the additionalMapObject
        AdditionalMapObject updatedAdditionalMapObject = additionalMapObjectRepository.findById(additionalMapObject.getId()).get();
        // Disconnect from session so that the updates on updatedAdditionalMapObject are not directly saved in db
        em.detach(updatedAdditionalMapObject);
        updatedAdditionalMapObject
            .title(UPDATED_TITLE)
            .objectLat(UPDATED_OBJECT_LAT)
            .objectLong(UPDATED_OBJECT_LONG)
            .type(UPDATED_TYPE)
            .description(UPDATED_DESCRIPTION);

        restAdditionalMapObjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAdditionalMapObject.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAdditionalMapObject))
            )
            .andExpect(status().isOk());

        // Validate the AdditionalMapObject in the database
        List<AdditionalMapObject> additionalMapObjectList = additionalMapObjectRepository.findAll();
        assertThat(additionalMapObjectList).hasSize(databaseSizeBeforeUpdate);
        AdditionalMapObject testAdditionalMapObject = additionalMapObjectList.get(additionalMapObjectList.size() - 1);
        assertThat(testAdditionalMapObject.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testAdditionalMapObject.getObjectLat()).isEqualTo(UPDATED_OBJECT_LAT);
        assertThat(testAdditionalMapObject.getObjectLong()).isEqualTo(UPDATED_OBJECT_LONG);
        assertThat(testAdditionalMapObject.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAdditionalMapObject.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingAdditionalMapObject() throws Exception {
        int databaseSizeBeforeUpdate = additionalMapObjectRepository.findAll().size();
        additionalMapObject.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdditionalMapObjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, additionalMapObject.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(additionalMapObject))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdditionalMapObject in the database
        List<AdditionalMapObject> additionalMapObjectList = additionalMapObjectRepository.findAll();
        assertThat(additionalMapObjectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAdditionalMapObject() throws Exception {
        int databaseSizeBeforeUpdate = additionalMapObjectRepository.findAll().size();
        additionalMapObject.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdditionalMapObjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(additionalMapObject))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdditionalMapObject in the database
        List<AdditionalMapObject> additionalMapObjectList = additionalMapObjectRepository.findAll();
        assertThat(additionalMapObjectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAdditionalMapObject() throws Exception {
        int databaseSizeBeforeUpdate = additionalMapObjectRepository.findAll().size();
        additionalMapObject.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdditionalMapObjectMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(additionalMapObject))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AdditionalMapObject in the database
        List<AdditionalMapObject> additionalMapObjectList = additionalMapObjectRepository.findAll();
        assertThat(additionalMapObjectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAdditionalMapObjectWithPatch() throws Exception {
        // Initialize the database
        additionalMapObjectRepository.saveAndFlush(additionalMapObject);

        int databaseSizeBeforeUpdate = additionalMapObjectRepository.findAll().size();

        // Update the additionalMapObject using partial update
        AdditionalMapObject partialUpdatedAdditionalMapObject = new AdditionalMapObject();
        partialUpdatedAdditionalMapObject.setId(additionalMapObject.getId());

        partialUpdatedAdditionalMapObject
            .title(UPDATED_TITLE)
            .objectLat(UPDATED_OBJECT_LAT)
            .objectLong(UPDATED_OBJECT_LONG)
            .description(UPDATED_DESCRIPTION);

        restAdditionalMapObjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdditionalMapObject.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAdditionalMapObject))
            )
            .andExpect(status().isOk());

        // Validate the AdditionalMapObject in the database
        List<AdditionalMapObject> additionalMapObjectList = additionalMapObjectRepository.findAll();
        assertThat(additionalMapObjectList).hasSize(databaseSizeBeforeUpdate);
        AdditionalMapObject testAdditionalMapObject = additionalMapObjectList.get(additionalMapObjectList.size() - 1);
        assertThat(testAdditionalMapObject.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testAdditionalMapObject.getObjectLat()).isEqualTo(UPDATED_OBJECT_LAT);
        assertThat(testAdditionalMapObject.getObjectLong()).isEqualTo(UPDATED_OBJECT_LONG);
        assertThat(testAdditionalMapObject.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testAdditionalMapObject.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateAdditionalMapObjectWithPatch() throws Exception {
        // Initialize the database
        additionalMapObjectRepository.saveAndFlush(additionalMapObject);

        int databaseSizeBeforeUpdate = additionalMapObjectRepository.findAll().size();

        // Update the additionalMapObject using partial update
        AdditionalMapObject partialUpdatedAdditionalMapObject = new AdditionalMapObject();
        partialUpdatedAdditionalMapObject.setId(additionalMapObject.getId());

        partialUpdatedAdditionalMapObject
            .title(UPDATED_TITLE)
            .objectLat(UPDATED_OBJECT_LAT)
            .objectLong(UPDATED_OBJECT_LONG)
            .type(UPDATED_TYPE)
            .description(UPDATED_DESCRIPTION);

        restAdditionalMapObjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdditionalMapObject.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAdditionalMapObject))
            )
            .andExpect(status().isOk());

        // Validate the AdditionalMapObject in the database
        List<AdditionalMapObject> additionalMapObjectList = additionalMapObjectRepository.findAll();
        assertThat(additionalMapObjectList).hasSize(databaseSizeBeforeUpdate);
        AdditionalMapObject testAdditionalMapObject = additionalMapObjectList.get(additionalMapObjectList.size() - 1);
        assertThat(testAdditionalMapObject.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testAdditionalMapObject.getObjectLat()).isEqualTo(UPDATED_OBJECT_LAT);
        assertThat(testAdditionalMapObject.getObjectLong()).isEqualTo(UPDATED_OBJECT_LONG);
        assertThat(testAdditionalMapObject.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAdditionalMapObject.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingAdditionalMapObject() throws Exception {
        int databaseSizeBeforeUpdate = additionalMapObjectRepository.findAll().size();
        additionalMapObject.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdditionalMapObjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, additionalMapObject.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(additionalMapObject))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdditionalMapObject in the database
        List<AdditionalMapObject> additionalMapObjectList = additionalMapObjectRepository.findAll();
        assertThat(additionalMapObjectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAdditionalMapObject() throws Exception {
        int databaseSizeBeforeUpdate = additionalMapObjectRepository.findAll().size();
        additionalMapObject.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdditionalMapObjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(additionalMapObject))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdditionalMapObject in the database
        List<AdditionalMapObject> additionalMapObjectList = additionalMapObjectRepository.findAll();
        assertThat(additionalMapObjectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAdditionalMapObject() throws Exception {
        int databaseSizeBeforeUpdate = additionalMapObjectRepository.findAll().size();
        additionalMapObject.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdditionalMapObjectMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(additionalMapObject))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AdditionalMapObject in the database
        List<AdditionalMapObject> additionalMapObjectList = additionalMapObjectRepository.findAll();
        assertThat(additionalMapObjectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAdditionalMapObject() throws Exception {
        // Initialize the database
        additionalMapObjectRepository.saveAndFlush(additionalMapObject);

        int databaseSizeBeforeDelete = additionalMapObjectRepository.findAll().size();

        // Delete the additionalMapObject
        restAdditionalMapObjectMockMvc
            .perform(delete(ENTITY_API_URL_ID, additionalMapObject.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AdditionalMapObject> additionalMapObjectList = additionalMapObjectRepository.findAll();
        assertThat(additionalMapObjectList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
