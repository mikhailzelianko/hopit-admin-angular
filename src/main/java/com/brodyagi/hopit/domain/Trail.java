package com.brodyagi.hopit.domain;

import com.brodyagi.hopit.domain.enumeration.TrailType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Trail.
 */
@Entity
@Table(name = "trail")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Trail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "short_description")
    private String shortDescription;

    @Column(name = "special_rules")
    private String specialRules;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TrailType type;

    @Column(name = "price")
    private Double price;

    @Column(name = "enter_lat")
    private Long enterLat;

    @Column(name = "enter_long")
    private Long enterLong;

    @Column(name = "flag_unavailable")
    private Boolean flagUnavailable;

    @Column(name = "flag_water")
    private Boolean flagWater;

    @Column(name = "flag_birdwatching")
    private Boolean flagBirdwatching;

    @Column(name = "flag_religious")
    private Boolean flagReligious;

    @Column(name = "flag_fishing")
    private Boolean flagFishing;

    @Column(name = "flag_parking")
    private Boolean flagParking;

    @Column(name = "flag_wc")
    private Boolean flagWC;

    @Column(name = "flag_camping")
    private Boolean flagCamping;

    @Column(name = "flag_picnic")
    private Boolean flagPicnic;

    @Column(name = "flag_streetfood")
    private Boolean flagStreetfood;

    @Column(name = "source")
    private String source;

    @Column(name = "admin_comment")
    private String adminComment;

    @OneToOne
    @JoinColumn(unique = true)
    private Country country;

    @OneToMany(mappedBy = "trail")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "trailPathWaypoints", "trail" }, allowSetters = true)
    private Set<TrailPath> trailPaths = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Trail id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public Trail title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public Trail description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortDescription() {
        return this.shortDescription;
    }

    public Trail shortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
        return this;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getSpecialRules() {
        return this.specialRules;
    }

    public Trail specialRules(String specialRules) {
        this.specialRules = specialRules;
        return this;
    }

    public void setSpecialRules(String specialRules) {
        this.specialRules = specialRules;
    }

    public TrailType getType() {
        return this.type;
    }

    public Trail type(TrailType type) {
        this.type = type;
        return this;
    }

    public void setType(TrailType type) {
        this.type = type;
    }

    public Double getPrice() {
        return this.price;
    }

    public Trail price(Double price) {
        this.price = price;
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getEnterLat() {
        return this.enterLat;
    }

    public Trail enterLat(Long enterLat) {
        this.enterLat = enterLat;
        return this;
    }

    public void setEnterLat(Long enterLat) {
        this.enterLat = enterLat;
    }

    public Long getEnterLong() {
        return this.enterLong;
    }

    public Trail enterLong(Long enterLong) {
        this.enterLong = enterLong;
        return this;
    }

    public void setEnterLong(Long enterLong) {
        this.enterLong = enterLong;
    }

    public Boolean getFlagUnavailable() {
        return this.flagUnavailable;
    }

    public Trail flagUnavailable(Boolean flagUnavailable) {
        this.flagUnavailable = flagUnavailable;
        return this;
    }

    public void setFlagUnavailable(Boolean flagUnavailable) {
        this.flagUnavailable = flagUnavailable;
    }

    public Boolean getFlagWater() {
        return this.flagWater;
    }

    public Trail flagWater(Boolean flagWater) {
        this.flagWater = flagWater;
        return this;
    }

    public void setFlagWater(Boolean flagWater) {
        this.flagWater = flagWater;
    }

    public Boolean getFlagBirdwatching() {
        return this.flagBirdwatching;
    }

    public Trail flagBirdwatching(Boolean flagBirdwatching) {
        this.flagBirdwatching = flagBirdwatching;
        return this;
    }

    public void setFlagBirdwatching(Boolean flagBirdwatching) {
        this.flagBirdwatching = flagBirdwatching;
    }

    public Boolean getFlagReligious() {
        return this.flagReligious;
    }

    public Trail flagReligious(Boolean flagReligious) {
        this.flagReligious = flagReligious;
        return this;
    }

    public void setFlagReligious(Boolean flagReligious) {
        this.flagReligious = flagReligious;
    }

    public Boolean getFlagFishing() {
        return this.flagFishing;
    }

    public Trail flagFishing(Boolean flagFishing) {
        this.flagFishing = flagFishing;
        return this;
    }

    public void setFlagFishing(Boolean flagFishing) {
        this.flagFishing = flagFishing;
    }

    public Boolean getFlagParking() {
        return this.flagParking;
    }

    public Trail flagParking(Boolean flagParking) {
        this.flagParking = flagParking;
        return this;
    }

    public void setFlagParking(Boolean flagParking) {
        this.flagParking = flagParking;
    }

    public Boolean getFlagWC() {
        return this.flagWC;
    }

    public Trail flagWC(Boolean flagWC) {
        this.flagWC = flagWC;
        return this;
    }

    public void setFlagWC(Boolean flagWC) {
        this.flagWC = flagWC;
    }

    public Boolean getFlagCamping() {
        return this.flagCamping;
    }

    public Trail flagCamping(Boolean flagCamping) {
        this.flagCamping = flagCamping;
        return this;
    }

    public void setFlagCamping(Boolean flagCamping) {
        this.flagCamping = flagCamping;
    }

    public Boolean getFlagPicnic() {
        return this.flagPicnic;
    }

    public Trail flagPicnic(Boolean flagPicnic) {
        this.flagPicnic = flagPicnic;
        return this;
    }

    public void setFlagPicnic(Boolean flagPicnic) {
        this.flagPicnic = flagPicnic;
    }

    public Boolean getFlagStreetfood() {
        return this.flagStreetfood;
    }

    public Trail flagStreetfood(Boolean flagStreetfood) {
        this.flagStreetfood = flagStreetfood;
        return this;
    }

    public void setFlagStreetfood(Boolean flagStreetfood) {
        this.flagStreetfood = flagStreetfood;
    }

    public String getSource() {
        return this.source;
    }

    public Trail source(String source) {
        this.source = source;
        return this;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAdminComment() {
        return this.adminComment;
    }

    public Trail adminComment(String adminComment) {
        this.adminComment = adminComment;
        return this;
    }

    public void setAdminComment(String adminComment) {
        this.adminComment = adminComment;
    }

    public Country getCountry() {
        return this.country;
    }

    public Trail country(Country country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Set<TrailPath> getTrailPaths() {
        return this.trailPaths;
    }

    public Trail trailPaths(Set<TrailPath> trailPaths) {
        this.setTrailPaths(trailPaths);
        return this;
    }

    public Trail addTrailPath(TrailPath trailPath) {
        this.trailPaths.add(trailPath);
        trailPath.setTrail(this);
        return this;
    }

    public Trail removeTrailPath(TrailPath trailPath) {
        this.trailPaths.remove(trailPath);
        trailPath.setTrail(null);
        return this;
    }

    public void setTrailPaths(Set<TrailPath> trailPaths) {
        if (this.trailPaths != null) {
            this.trailPaths.forEach(i -> i.setTrail(null));
        }
        if (trailPaths != null) {
            trailPaths.forEach(i -> i.setTrail(this));
        }
        this.trailPaths = trailPaths;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Trail)) {
            return false;
        }
        return id != null && id.equals(((Trail) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Trail{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", shortDescription='" + getShortDescription() + "'" +
            ", specialRules='" + getSpecialRules() + "'" +
            ", type='" + getType() + "'" +
            ", price=" + getPrice() +
            ", enterLat=" + getEnterLat() +
            ", enterLong=" + getEnterLong() +
            ", flagUnavailable='" + getFlagUnavailable() + "'" +
            ", flagWater='" + getFlagWater() + "'" +
            ", flagBirdwatching='" + getFlagBirdwatching() + "'" +
            ", flagReligious='" + getFlagReligious() + "'" +
            ", flagFishing='" + getFlagFishing() + "'" +
            ", flagParking='" + getFlagParking() + "'" +
            ", flagWC='" + getFlagWC() + "'" +
            ", flagCamping='" + getFlagCamping() + "'" +
            ", flagPicnic='" + getFlagPicnic() + "'" +
            ", flagStreetfood='" + getFlagStreetfood() + "'" +
            ", source='" + getSource() + "'" +
            ", adminComment='" + getAdminComment() + "'" +
            "}";
    }
}
