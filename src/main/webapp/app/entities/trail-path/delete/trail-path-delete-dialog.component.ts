import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITrailPath } from '../trail-path.model';
import { TrailPathService } from '../service/trail-path.service';

@Component({
  templateUrl: './trail-path-delete-dialog.component.html',
})
export class TrailPathDeleteDialogComponent {
  trailPath?: ITrailPath;

  constructor(protected trailPathService: TrailPathService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.trailPathService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
