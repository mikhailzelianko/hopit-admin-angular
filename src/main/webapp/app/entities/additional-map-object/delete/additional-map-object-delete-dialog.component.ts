import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAdditionalMapObject } from '../additional-map-object.model';
import { AdditionalMapObjectService } from '../service/additional-map-object.service';

@Component({
  templateUrl: './additional-map-object-delete-dialog.component.html',
})
export class AdditionalMapObjectDeleteDialogComponent {
  additionalMapObject?: IAdditionalMapObject;

  constructor(protected additionalMapObjectService: AdditionalMapObjectService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.additionalMapObjectService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
