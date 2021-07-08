import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IAdditionalMapObject } from '../additional-map-object.model';
import { AdditionalMapObjectService } from '../service/additional-map-object.service';
import { AdditionalMapObjectDeleteDialogComponent } from '../delete/additional-map-object-delete-dialog.component';

@Component({
  selector: 'jhi-additional-map-object',
  templateUrl: './additional-map-object.component.html',
})
export class AdditionalMapObjectComponent implements OnInit {
  additionalMapObjects?: IAdditionalMapObject[];
  isLoading = false;

  constructor(protected additionalMapObjectService: AdditionalMapObjectService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.additionalMapObjectService.query().subscribe(
      (res: HttpResponse<IAdditionalMapObject[]>) => {
        this.isLoading = false;
        this.additionalMapObjects = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IAdditionalMapObject): number {
    return item.id!;
  }

  delete(additionalMapObject: IAdditionalMapObject): void {
    const modalRef = this.modalService.open(AdditionalMapObjectDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.additionalMapObject = additionalMapObject;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
