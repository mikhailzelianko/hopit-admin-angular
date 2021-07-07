import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITrailPathWaypoint } from '../trail-path-waypoint.model';
import { TrailPathWaypointService } from '../service/trail-path-waypoint.service';
import { TrailPathWaypointDeleteDialogComponent } from '../delete/trail-path-waypoint-delete-dialog.component';

@Component({
  selector: 'jhi-trail-path-waypoint',
  templateUrl: './trail-path-waypoint.component.html',
})
export class TrailPathWaypointComponent implements OnInit {
  trailPathWaypoints?: ITrailPathWaypoint[];
  isLoading = false;

  constructor(protected trailPathWaypointService: TrailPathWaypointService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.trailPathWaypointService.query().subscribe(
      (res: HttpResponse<ITrailPathWaypoint[]>) => {
        this.isLoading = false;
        this.trailPathWaypoints = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ITrailPathWaypoint): number {
    return item.id!;
  }

  delete(trailPathWaypoint: ITrailPathWaypoint): void {
    const modalRef = this.modalService.open(TrailPathWaypointDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.trailPathWaypoint = trailPathWaypoint;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
