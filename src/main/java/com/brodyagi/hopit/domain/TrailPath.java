package com.brodyagi.hopit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TrailPath.
 */
@Entity
@Table(name = "trail_path")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TrailPath implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "trailpath")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "trailpath" }, allowSetters = true)
    private Set<TrailPathWaypoint> trailPathWaypoints = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "country", "trailPaths" }, allowSetters = true)
    private Trail trail;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TrailPath id(Long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public TrailPath title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public TrailPath description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<TrailPathWaypoint> getTrailPathWaypoints() {
        return this.trailPathWaypoints;
    }

    public TrailPath trailPathWaypoints(Set<TrailPathWaypoint> trailPathWaypoints) {
        this.setTrailPathWaypoints(trailPathWaypoints);
        return this;
    }

    public TrailPath addTrailPathWaypoint(TrailPathWaypoint trailPathWaypoint) {
        this.trailPathWaypoints.add(trailPathWaypoint);
        trailPathWaypoint.setTrailpath(this);
        return this;
    }

    public TrailPath removeTrailPathWaypoint(TrailPathWaypoint trailPathWaypoint) {
        this.trailPathWaypoints.remove(trailPathWaypoint);
        trailPathWaypoint.setTrailpath(null);
        return this;
    }

    public void setTrailPathWaypoints(Set<TrailPathWaypoint> trailPathWaypoints) {
        if (this.trailPathWaypoints != null) {
            this.trailPathWaypoints.forEach(i -> i.setTrailpath(null));
        }
        if (trailPathWaypoints != null) {
            trailPathWaypoints.forEach(i -> i.setTrailpath(this));
        }
        this.trailPathWaypoints = trailPathWaypoints;
    }

    public Trail getTrail() {
        return this.trail;
    }

    public TrailPath trail(Trail trail) {
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
        if (!(o instanceof TrailPath)) {
            return false;
        }
        return id != null && id.equals(((TrailPath) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrailPath{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
