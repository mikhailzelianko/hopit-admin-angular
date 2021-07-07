package com.brodyagi.hopit.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.brodyagi.hopit.IntegrationTest;
import com.brodyagi.hopit.domain.Trail;
import com.brodyagi.hopit.domain.enumeration.TrailType;
import com.brodyagi.hopit.repository.TrailRepository;
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
 * Integration tests for the {@link TrailResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TrailResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_SHORT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_SPECIAL_RULES = "AAAAAAAAAA";
    private static final String UPDATED_SPECIAL_RULES = "BBBBBBBBBB";

    private static final TrailType DEFAULT_TYPE = TrailType.HIKING;
    private static final TrailType UPDATED_TYPE = TrailType.BIKE;

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final Long DEFAULT_ENTER_LAT = 1L;
    private static final Long UPDATED_ENTER_LAT = 2L;

    private static final Long DEFAULT_ENTER_LONG = 1L;
    private static final Long UPDATED_ENTER_LONG = 2L;

    private static final Boolean DEFAULT_FLAG_UNAVAILABLE = false;
    private static final Boolean UPDATED_FLAG_UNAVAILABLE = true;

    private static final Boolean DEFAULT_FLAG_WATER = false;
    private static final Boolean UPDATED_FLAG_WATER = true;

    private static final Boolean DEFAULT_FLAG_BIRDWATCHING = false;
    private static final Boolean UPDATED_FLAG_BIRDWATCHING = true;

    private static final Boolean DEFAULT_FLAG_RELIGIOUS = false;
    private static final Boolean UPDATED_FLAG_RELIGIOUS = true;

    private static final Boolean DEFAULT_FLAG_FISHING = false;
    private static final Boolean UPDATED_FLAG_FISHING = true;

    private static final Boolean DEFAULT_FLAG_PARKING = false;
    private static final Boolean UPDATED_FLAG_PARKING = true;

    private static final Boolean DEFAULT_FLAG_WC = false;
    private static final Boolean UPDATED_FLAG_WC = true;

    private static final Boolean DEFAULT_FLAG_CAMPING = false;
    private static final Boolean UPDATED_FLAG_CAMPING = true;

    private static final Boolean DEFAULT_FLAG_PICNIC = false;
    private static final Boolean UPDATED_FLAG_PICNIC = true;

    private static final Boolean DEFAULT_FLAG_STREETFOOD = false;
    private static final Boolean UPDATED_FLAG_STREETFOOD = true;

    private static final String DEFAULT_SOURCE = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE = "BBBBBBBBBB";

    private static final String DEFAULT_ADMIN_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_ADMIN_COMMENT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/trails";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TrailRepository trailRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTrailMockMvc;

    private Trail trail;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trail createEntity(EntityManager em) {
        Trail trail = new Trail()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .shortDescription(DEFAULT_SHORT_DESCRIPTION)
            .specialRules(DEFAULT_SPECIAL_RULES)
            .type(DEFAULT_TYPE)
            .price(DEFAULT_PRICE)
            .enterLat(DEFAULT_ENTER_LAT)
            .enterLong(DEFAULT_ENTER_LONG)
            .flagUnavailable(DEFAULT_FLAG_UNAVAILABLE)
            .flagWater(DEFAULT_FLAG_WATER)
            .flagBirdwatching(DEFAULT_FLAG_BIRDWATCHING)
            .flagReligious(DEFAULT_FLAG_RELIGIOUS)
            .flagFishing(DEFAULT_FLAG_FISHING)
            .flagParking(DEFAULT_FLAG_PARKING)
            .flagWC(DEFAULT_FLAG_WC)
            .flagCamping(DEFAULT_FLAG_CAMPING)
            .flagPicnic(DEFAULT_FLAG_PICNIC)
            .flagStreetfood(DEFAULT_FLAG_STREETFOOD)
            .source(DEFAULT_SOURCE)
            .adminComment(DEFAULT_ADMIN_COMMENT);
        return trail;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Trail createUpdatedEntity(EntityManager em) {
        Trail trail = new Trail()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .shortDescription(UPDATED_SHORT_DESCRIPTION)
            .specialRules(UPDATED_SPECIAL_RULES)
            .type(UPDATED_TYPE)
            .price(UPDATED_PRICE)
            .enterLat(UPDATED_ENTER_LAT)
            .enterLong(UPDATED_ENTER_LONG)
            .flagUnavailable(UPDATED_FLAG_UNAVAILABLE)
            .flagWater(UPDATED_FLAG_WATER)
            .flagBirdwatching(UPDATED_FLAG_BIRDWATCHING)
            .flagReligious(UPDATED_FLAG_RELIGIOUS)
            .flagFishing(UPDATED_FLAG_FISHING)
            .flagParking(UPDATED_FLAG_PARKING)
            .flagWC(UPDATED_FLAG_WC)
            .flagCamping(UPDATED_FLAG_CAMPING)
            .flagPicnic(UPDATED_FLAG_PICNIC)
            .flagStreetfood(UPDATED_FLAG_STREETFOOD)
            .source(UPDATED_SOURCE)
            .adminComment(UPDATED_ADMIN_COMMENT);
        return trail;
    }

    @BeforeEach
    public void initTest() {
        trail = createEntity(em);
    }

    @Test
    @Transactional
    void createTrail() throws Exception {
        int databaseSizeBeforeCreate = trailRepository.findAll().size();
        // Create the Trail
        restTrailMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trail)))
            .andExpect(status().isCreated());

        // Validate the Trail in the database
        List<Trail> trailList = trailRepository.findAll();
        assertThat(trailList).hasSize(databaseSizeBeforeCreate + 1);
        Trail testTrail = trailList.get(trailList.size() - 1);
        assertThat(testTrail.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testTrail.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTrail.getShortDescription()).isEqualTo(DEFAULT_SHORT_DESCRIPTION);
        assertThat(testTrail.getSpecialRules()).isEqualTo(DEFAULT_SPECIAL_RULES);
        assertThat(testTrail.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testTrail.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testTrail.getEnterLat()).isEqualTo(DEFAULT_ENTER_LAT);
        assertThat(testTrail.getEnterLong()).isEqualTo(DEFAULT_ENTER_LONG);
        assertThat(testTrail.getFlagUnavailable()).isEqualTo(DEFAULT_FLAG_UNAVAILABLE);
        assertThat(testTrail.getFlagWater()).isEqualTo(DEFAULT_FLAG_WATER);
        assertThat(testTrail.getFlagBirdwatching()).isEqualTo(DEFAULT_FLAG_BIRDWATCHING);
        assertThat(testTrail.getFlagReligious()).isEqualTo(DEFAULT_FLAG_RELIGIOUS);
        assertThat(testTrail.getFlagFishing()).isEqualTo(DEFAULT_FLAG_FISHING);
        assertThat(testTrail.getFlagParking()).isEqualTo(DEFAULT_FLAG_PARKING);
        assertThat(testTrail.getFlagWC()).isEqualTo(DEFAULT_FLAG_WC);
        assertThat(testTrail.getFlagCamping()).isEqualTo(DEFAULT_FLAG_CAMPING);
        assertThat(testTrail.getFlagPicnic()).isEqualTo(DEFAULT_FLAG_PICNIC);
        assertThat(testTrail.getFlagStreetfood()).isEqualTo(DEFAULT_FLAG_STREETFOOD);
        assertThat(testTrail.getSource()).isEqualTo(DEFAULT_SOURCE);
        assertThat(testTrail.getAdminComment()).isEqualTo(DEFAULT_ADMIN_COMMENT);
    }

    @Test
    @Transactional
    void createTrailWithExistingId() throws Exception {
        // Create the Trail with an existing ID
        trail.setId(1L);

        int databaseSizeBeforeCreate = trailRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrailMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trail)))
            .andExpect(status().isBadRequest());

        // Validate the Trail in the database
        List<Trail> trailList = trailRepository.findAll();
        assertThat(trailList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = trailRepository.findAll().size();
        // set the field null
        trail.setTitle(null);

        // Create the Trail, which fails.

        restTrailMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trail)))
            .andExpect(status().isBadRequest());

        List<Trail> trailList = trailRepository.findAll();
        assertThat(trailList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = trailRepository.findAll().size();
        // set the field null
        trail.setType(null);

        // Create the Trail, which fails.

        restTrailMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trail)))
            .andExpect(status().isBadRequest());

        List<Trail> trailList = trailRepository.findAll();
        assertThat(trailList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTrails() throws Exception {
        // Initialize the database
        trailRepository.saveAndFlush(trail);

        // Get all the trailList
        restTrailMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trail.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].shortDescription").value(hasItem(DEFAULT_SHORT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].specialRules").value(hasItem(DEFAULT_SPECIAL_RULES)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].enterLat").value(hasItem(DEFAULT_ENTER_LAT.intValue())))
            .andExpect(jsonPath("$.[*].enterLong").value(hasItem(DEFAULT_ENTER_LONG.intValue())))
            .andExpect(jsonPath("$.[*].flagUnavailable").value(hasItem(DEFAULT_FLAG_UNAVAILABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].flagWater").value(hasItem(DEFAULT_FLAG_WATER.booleanValue())))
            .andExpect(jsonPath("$.[*].flagBirdwatching").value(hasItem(DEFAULT_FLAG_BIRDWATCHING.booleanValue())))
            .andExpect(jsonPath("$.[*].flagReligious").value(hasItem(DEFAULT_FLAG_RELIGIOUS.booleanValue())))
            .andExpect(jsonPath("$.[*].flagFishing").value(hasItem(DEFAULT_FLAG_FISHING.booleanValue())))
            .andExpect(jsonPath("$.[*].flagParking").value(hasItem(DEFAULT_FLAG_PARKING.booleanValue())))
            .andExpect(jsonPath("$.[*].flagWC").value(hasItem(DEFAULT_FLAG_WC.booleanValue())))
            .andExpect(jsonPath("$.[*].flagCamping").value(hasItem(DEFAULT_FLAG_CAMPING.booleanValue())))
            .andExpect(jsonPath("$.[*].flagPicnic").value(hasItem(DEFAULT_FLAG_PICNIC.booleanValue())))
            .andExpect(jsonPath("$.[*].flagStreetfood").value(hasItem(DEFAULT_FLAG_STREETFOOD.booleanValue())))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE)))
            .andExpect(jsonPath("$.[*].adminComment").value(hasItem(DEFAULT_ADMIN_COMMENT)));
    }

    @Test
    @Transactional
    void getTrail() throws Exception {
        // Initialize the database
        trailRepository.saveAndFlush(trail);

        // Get the trail
        restTrailMockMvc
            .perform(get(ENTITY_API_URL_ID, trail.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(trail.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.shortDescription").value(DEFAULT_SHORT_DESCRIPTION))
            .andExpect(jsonPath("$.specialRules").value(DEFAULT_SPECIAL_RULES))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.enterLat").value(DEFAULT_ENTER_LAT.intValue()))
            .andExpect(jsonPath("$.enterLong").value(DEFAULT_ENTER_LONG.intValue()))
            .andExpect(jsonPath("$.flagUnavailable").value(DEFAULT_FLAG_UNAVAILABLE.booleanValue()))
            .andExpect(jsonPath("$.flagWater").value(DEFAULT_FLAG_WATER.booleanValue()))
            .andExpect(jsonPath("$.flagBirdwatching").value(DEFAULT_FLAG_BIRDWATCHING.booleanValue()))
            .andExpect(jsonPath("$.flagReligious").value(DEFAULT_FLAG_RELIGIOUS.booleanValue()))
            .andExpect(jsonPath("$.flagFishing").value(DEFAULT_FLAG_FISHING.booleanValue()))
            .andExpect(jsonPath("$.flagParking").value(DEFAULT_FLAG_PARKING.booleanValue()))
            .andExpect(jsonPath("$.flagWC").value(DEFAULT_FLAG_WC.booleanValue()))
            .andExpect(jsonPath("$.flagCamping").value(DEFAULT_FLAG_CAMPING.booleanValue()))
            .andExpect(jsonPath("$.flagPicnic").value(DEFAULT_FLAG_PICNIC.booleanValue()))
            .andExpect(jsonPath("$.flagStreetfood").value(DEFAULT_FLAG_STREETFOOD.booleanValue()))
            .andExpect(jsonPath("$.source").value(DEFAULT_SOURCE))
            .andExpect(jsonPath("$.adminComment").value(DEFAULT_ADMIN_COMMENT));
    }

    @Test
    @Transactional
    void getNonExistingTrail() throws Exception {
        // Get the trail
        restTrailMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTrail() throws Exception {
        // Initialize the database
        trailRepository.saveAndFlush(trail);

        int databaseSizeBeforeUpdate = trailRepository.findAll().size();

        // Update the trail
        Trail updatedTrail = trailRepository.findById(trail.getId()).get();
        // Disconnect from session so that the updates on updatedTrail are not directly saved in db
        em.detach(updatedTrail);
        updatedTrail
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .shortDescription(UPDATED_SHORT_DESCRIPTION)
            .specialRules(UPDATED_SPECIAL_RULES)
            .type(UPDATED_TYPE)
            .price(UPDATED_PRICE)
            .enterLat(UPDATED_ENTER_LAT)
            .enterLong(UPDATED_ENTER_LONG)
            .flagUnavailable(UPDATED_FLAG_UNAVAILABLE)
            .flagWater(UPDATED_FLAG_WATER)
            .flagBirdwatching(UPDATED_FLAG_BIRDWATCHING)
            .flagReligious(UPDATED_FLAG_RELIGIOUS)
            .flagFishing(UPDATED_FLAG_FISHING)
            .flagParking(UPDATED_FLAG_PARKING)
            .flagWC(UPDATED_FLAG_WC)
            .flagCamping(UPDATED_FLAG_CAMPING)
            .flagPicnic(UPDATED_FLAG_PICNIC)
            .flagStreetfood(UPDATED_FLAG_STREETFOOD)
            .source(UPDATED_SOURCE)
            .adminComment(UPDATED_ADMIN_COMMENT);

        restTrailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTrail.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTrail))
            )
            .andExpect(status().isOk());

        // Validate the Trail in the database
        List<Trail> trailList = trailRepository.findAll();
        assertThat(trailList).hasSize(databaseSizeBeforeUpdate);
        Trail testTrail = trailList.get(trailList.size() - 1);
        assertThat(testTrail.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTrail.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTrail.getShortDescription()).isEqualTo(UPDATED_SHORT_DESCRIPTION);
        assertThat(testTrail.getSpecialRules()).isEqualTo(UPDATED_SPECIAL_RULES);
        assertThat(testTrail.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTrail.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testTrail.getEnterLat()).isEqualTo(UPDATED_ENTER_LAT);
        assertThat(testTrail.getEnterLong()).isEqualTo(UPDATED_ENTER_LONG);
        assertThat(testTrail.getFlagUnavailable()).isEqualTo(UPDATED_FLAG_UNAVAILABLE);
        assertThat(testTrail.getFlagWater()).isEqualTo(UPDATED_FLAG_WATER);
        assertThat(testTrail.getFlagBirdwatching()).isEqualTo(UPDATED_FLAG_BIRDWATCHING);
        assertThat(testTrail.getFlagReligious()).isEqualTo(UPDATED_FLAG_RELIGIOUS);
        assertThat(testTrail.getFlagFishing()).isEqualTo(UPDATED_FLAG_FISHING);
        assertThat(testTrail.getFlagParking()).isEqualTo(UPDATED_FLAG_PARKING);
        assertThat(testTrail.getFlagWC()).isEqualTo(UPDATED_FLAG_WC);
        assertThat(testTrail.getFlagCamping()).isEqualTo(UPDATED_FLAG_CAMPING);
        assertThat(testTrail.getFlagPicnic()).isEqualTo(UPDATED_FLAG_PICNIC);
        assertThat(testTrail.getFlagStreetfood()).isEqualTo(UPDATED_FLAG_STREETFOOD);
        assertThat(testTrail.getSource()).isEqualTo(UPDATED_SOURCE);
        assertThat(testTrail.getAdminComment()).isEqualTo(UPDATED_ADMIN_COMMENT);
    }

    @Test
    @Transactional
    void putNonExistingTrail() throws Exception {
        int databaseSizeBeforeUpdate = trailRepository.findAll().size();
        trail.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trail.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trail))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trail in the database
        List<Trail> trailList = trailRepository.findAll();
        assertThat(trailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTrail() throws Exception {
        int databaseSizeBeforeUpdate = trailRepository.findAll().size();
        trail.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrailMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trail))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trail in the database
        List<Trail> trailList = trailRepository.findAll();
        assertThat(trailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTrail() throws Exception {
        int databaseSizeBeforeUpdate = trailRepository.findAll().size();
        trail.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrailMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trail)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Trail in the database
        List<Trail> trailList = trailRepository.findAll();
        assertThat(trailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTrailWithPatch() throws Exception {
        // Initialize the database
        trailRepository.saveAndFlush(trail);

        int databaseSizeBeforeUpdate = trailRepository.findAll().size();

        // Update the trail using partial update
        Trail partialUpdatedTrail = new Trail();
        partialUpdatedTrail.setId(trail.getId());

        partialUpdatedTrail
            .title(UPDATED_TITLE)
            .specialRules(UPDATED_SPECIAL_RULES)
            .enterLat(UPDATED_ENTER_LAT)
            .enterLong(UPDATED_ENTER_LONG)
            .flagUnavailable(UPDATED_FLAG_UNAVAILABLE)
            .flagWater(UPDATED_FLAG_WATER)
            .flagBirdwatching(UPDATED_FLAG_BIRDWATCHING)
            .flagFishing(UPDATED_FLAG_FISHING)
            .flagParking(UPDATED_FLAG_PARKING)
            .flagCamping(UPDATED_FLAG_CAMPING)
            .flagStreetfood(UPDATED_FLAG_STREETFOOD)
            .adminComment(UPDATED_ADMIN_COMMENT);

        restTrailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrail.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrail))
            )
            .andExpect(status().isOk());

        // Validate the Trail in the database
        List<Trail> trailList = trailRepository.findAll();
        assertThat(trailList).hasSize(databaseSizeBeforeUpdate);
        Trail testTrail = trailList.get(trailList.size() - 1);
        assertThat(testTrail.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTrail.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testTrail.getShortDescription()).isEqualTo(DEFAULT_SHORT_DESCRIPTION);
        assertThat(testTrail.getSpecialRules()).isEqualTo(UPDATED_SPECIAL_RULES);
        assertThat(testTrail.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testTrail.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testTrail.getEnterLat()).isEqualTo(UPDATED_ENTER_LAT);
        assertThat(testTrail.getEnterLong()).isEqualTo(UPDATED_ENTER_LONG);
        assertThat(testTrail.getFlagUnavailable()).isEqualTo(UPDATED_FLAG_UNAVAILABLE);
        assertThat(testTrail.getFlagWater()).isEqualTo(UPDATED_FLAG_WATER);
        assertThat(testTrail.getFlagBirdwatching()).isEqualTo(UPDATED_FLAG_BIRDWATCHING);
        assertThat(testTrail.getFlagReligious()).isEqualTo(DEFAULT_FLAG_RELIGIOUS);
        assertThat(testTrail.getFlagFishing()).isEqualTo(UPDATED_FLAG_FISHING);
        assertThat(testTrail.getFlagParking()).isEqualTo(UPDATED_FLAG_PARKING);
        assertThat(testTrail.getFlagWC()).isEqualTo(DEFAULT_FLAG_WC);
        assertThat(testTrail.getFlagCamping()).isEqualTo(UPDATED_FLAG_CAMPING);
        assertThat(testTrail.getFlagPicnic()).isEqualTo(DEFAULT_FLAG_PICNIC);
        assertThat(testTrail.getFlagStreetfood()).isEqualTo(UPDATED_FLAG_STREETFOOD);
        assertThat(testTrail.getSource()).isEqualTo(DEFAULT_SOURCE);
        assertThat(testTrail.getAdminComment()).isEqualTo(UPDATED_ADMIN_COMMENT);
    }

    @Test
    @Transactional
    void fullUpdateTrailWithPatch() throws Exception {
        // Initialize the database
        trailRepository.saveAndFlush(trail);

        int databaseSizeBeforeUpdate = trailRepository.findAll().size();

        // Update the trail using partial update
        Trail partialUpdatedTrail = new Trail();
        partialUpdatedTrail.setId(trail.getId());

        partialUpdatedTrail
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .shortDescription(UPDATED_SHORT_DESCRIPTION)
            .specialRules(UPDATED_SPECIAL_RULES)
            .type(UPDATED_TYPE)
            .price(UPDATED_PRICE)
            .enterLat(UPDATED_ENTER_LAT)
            .enterLong(UPDATED_ENTER_LONG)
            .flagUnavailable(UPDATED_FLAG_UNAVAILABLE)
            .flagWater(UPDATED_FLAG_WATER)
            .flagBirdwatching(UPDATED_FLAG_BIRDWATCHING)
            .flagReligious(UPDATED_FLAG_RELIGIOUS)
            .flagFishing(UPDATED_FLAG_FISHING)
            .flagParking(UPDATED_FLAG_PARKING)
            .flagWC(UPDATED_FLAG_WC)
            .flagCamping(UPDATED_FLAG_CAMPING)
            .flagPicnic(UPDATED_FLAG_PICNIC)
            .flagStreetfood(UPDATED_FLAG_STREETFOOD)
            .source(UPDATED_SOURCE)
            .adminComment(UPDATED_ADMIN_COMMENT);

        restTrailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrail.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrail))
            )
            .andExpect(status().isOk());

        // Validate the Trail in the database
        List<Trail> trailList = trailRepository.findAll();
        assertThat(trailList).hasSize(databaseSizeBeforeUpdate);
        Trail testTrail = trailList.get(trailList.size() - 1);
        assertThat(testTrail.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testTrail.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testTrail.getShortDescription()).isEqualTo(UPDATED_SHORT_DESCRIPTION);
        assertThat(testTrail.getSpecialRules()).isEqualTo(UPDATED_SPECIAL_RULES);
        assertThat(testTrail.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTrail.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testTrail.getEnterLat()).isEqualTo(UPDATED_ENTER_LAT);
        assertThat(testTrail.getEnterLong()).isEqualTo(UPDATED_ENTER_LONG);
        assertThat(testTrail.getFlagUnavailable()).isEqualTo(UPDATED_FLAG_UNAVAILABLE);
        assertThat(testTrail.getFlagWater()).isEqualTo(UPDATED_FLAG_WATER);
        assertThat(testTrail.getFlagBirdwatching()).isEqualTo(UPDATED_FLAG_BIRDWATCHING);
        assertThat(testTrail.getFlagReligious()).isEqualTo(UPDATED_FLAG_RELIGIOUS);
        assertThat(testTrail.getFlagFishing()).isEqualTo(UPDATED_FLAG_FISHING);
        assertThat(testTrail.getFlagParking()).isEqualTo(UPDATED_FLAG_PARKING);
        assertThat(testTrail.getFlagWC()).isEqualTo(UPDATED_FLAG_WC);
        assertThat(testTrail.getFlagCamping()).isEqualTo(UPDATED_FLAG_CAMPING);
        assertThat(testTrail.getFlagPicnic()).isEqualTo(UPDATED_FLAG_PICNIC);
        assertThat(testTrail.getFlagStreetfood()).isEqualTo(UPDATED_FLAG_STREETFOOD);
        assertThat(testTrail.getSource()).isEqualTo(UPDATED_SOURCE);
        assertThat(testTrail.getAdminComment()).isEqualTo(UPDATED_ADMIN_COMMENT);
    }

    @Test
    @Transactional
    void patchNonExistingTrail() throws Exception {
        int databaseSizeBeforeUpdate = trailRepository.findAll().size();
        trail.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, trail.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trail))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trail in the database
        List<Trail> trailList = trailRepository.findAll();
        assertThat(trailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTrail() throws Exception {
        int databaseSizeBeforeUpdate = trailRepository.findAll().size();
        trail.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrailMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trail))
            )
            .andExpect(status().isBadRequest());

        // Validate the Trail in the database
        List<Trail> trailList = trailRepository.findAll();
        assertThat(trailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTrail() throws Exception {
        int databaseSizeBeforeUpdate = trailRepository.findAll().size();
        trail.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrailMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(trail)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Trail in the database
        List<Trail> trailList = trailRepository.findAll();
        assertThat(trailList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTrail() throws Exception {
        // Initialize the database
        trailRepository.saveAndFlush(trail);

        int databaseSizeBeforeDelete = trailRepository.findAll().size();

        // Delete the trail
        restTrailMockMvc
            .perform(delete(ENTITY_API_URL_ID, trail.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Trail> trailList = trailRepository.findAll();
        assertThat(trailList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
