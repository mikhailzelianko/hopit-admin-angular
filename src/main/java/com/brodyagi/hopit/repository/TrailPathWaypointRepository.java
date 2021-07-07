package com.brodyagi.hopit.repository;

import com.brodyagi.hopit.domain.TrailPathWaypoint;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TrailPathWaypoint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrailPathWaypointRepository extends JpaRepository<TrailPathWaypoint, Long> {}
