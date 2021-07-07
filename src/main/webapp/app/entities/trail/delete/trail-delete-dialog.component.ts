import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITrail } from '../trail.model';
import { TrailService } from '../service/trail.service';

@Component({
  templateUrl: './trail-delete-dialog.component.html',
})
export class TrailDeleteDialogComponent {
  trail?: ITrail;

  constructor(protected trailService: TrailService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.trailService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
