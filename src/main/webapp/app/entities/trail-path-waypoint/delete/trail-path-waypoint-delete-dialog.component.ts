import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITrailPathWaypoint } from '../trail-path-waypoint.model';
import { TrailPathWaypointService } from '../service/trail-path-waypoint.service';

@Component({
  templateUrl: './trail-path-waypoint-delete-dialog.component.html',
})
export class TrailPathWaypointDeleteDialogComponent {
  trailPathWaypoint?: ITrailPathWaypoint;

  constructor(protected trailPathWaypointService: TrailPathWaypointService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.trailPathWaypointService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
