package com.brodyagi.hopit.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TrailPathWaypoint.
 */
@Entity
@Table(name = "trail_path_waypoint")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TrailPathWaypoint implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "waypoint_lat")
    private Long waypointLat;

    @Column(name = "waypoint_long")
    private Long waypointLong;

    @ManyToOne
    @JsonIgnoreProperties(value = { "trailPathWaypoints", "trail" }, allowSetters = true)
    private TrailPath trailpath;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TrailPathWaypoint id(Long id) {
        this.id = id;
        return this;
    }

    public Long getWaypointLat() {
        return this.waypointLat;
    }

    public TrailPathWaypoint waypointLat(Long waypointLat) {
        this.waypointLat = waypointLat;
        return this;
    }

    public void setWaypointLat(Long waypointLat) {
        this.waypointLat = waypointLat;
    }

    public Long getWaypointLong() {
        return this.waypointLong;
    }

    public TrailPathWaypoint waypointLong(Long waypointLong) {
        this.waypointLong = waypointLong;
        return this;
    }

    public void setWaypointLong(Long waypointLong) {
        this.waypointLong = waypointLong;
    }

    public TrailPath getTrailpath() {
        return this.trailpath;
    }

    public TrailPathWaypoint trailpath(TrailPath trailPath) {
        this.setTrailpath(trailPath);
        return this;
    }

    public void setTrailpath(TrailPath trailPath) {
        this.trailpath = trailPath;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TrailPathWaypoint)) {
            return false;
        }
        return id != null && id.equals(((TrailPathWaypoint) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TrailPathWaypoint{" +
            "id=" + getId() +
            ", waypointLat=" + getWaypointLat() +
            ", waypointLong=" + getWaypointLong() +
            "}";
    }
}
