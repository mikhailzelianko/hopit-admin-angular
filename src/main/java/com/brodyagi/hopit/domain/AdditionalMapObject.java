package com.brodyagi.hopit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AdditionalMapObject.
 */
@Entity
@Table(name = "additional_map_object")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class AdditionalMapObject implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "object_lat")
    private Long objectLat;

    @Column(name = "object_long")
    private Long objectLong;

    @Column(name = "type")
    private String type;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JsonIgnoreProperties(value = { "trailPaths", "additionalMapObjects", "country", "region", "district" }, allowSetters = true)
    private Trail trail;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AdditionalMapObject id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public AdditionalMapObject title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getObjectLat() {
        return this.objectLat;
    }

    public AdditionalMapObject objectLat(Long objectLat) {
        this.objectLat = objectLat;
        return this;
    }

    public void setObjectLat(Long objectLat) {
        this.objectLat = objectLat;
    }

    public Long getObjectLong() {
        return this.objectLong;
    }

    public AdditionalMapObject objectLong(Long objectLong) {
        this.objectLong = objectLong;
        return this;
    }

    public void setObjectLong(Long objectLong) {
        this.objectLong = objectLong;
    }

    public String getType() {
        return this.type;
    }

    public AdditionalMapObject type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return this.description;
    }

    public AdditionalMapObject description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Trail getTrail() {
        return this.trail;
    }

    public AdditionalMapObject trail(Trail trail) {
        this.setTrail(trail);
        return this;
    }

    public void setTrail(Trail trail) {
        this.trail = trail;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AdditionalMapObject)) {
            return false;
        }
        return id != null && id.equals(((AdditionalMapObject) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AdditionalMapObject{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", objectLat=" + getObjectLat() +
            ", objectLong=" + getObjectLong() +
            ", type='" + getType() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
