import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITrailPathWaypoint } from '../trail-path-waypoint.model';

@Component({
  selector: 'jhi-trail-path-waypoint-detail',
  templateUrl: './trail-path-waypoint-detail.component.html',
})
export class TrailPathWaypointDetailComponent implements OnInit {
  trailPathWaypoint: ITrailPathWaypoint | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ trailPathWaypoint }) => {
      this.trailPathWaypoint = trailPathWaypoint;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
