import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITrail } from '../trail.model';
import { TrailService } from '../service/trail.service';
import { TrailDeleteDialogComponent } from '../delete/trail-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-trail',
  templateUrl: './trail.component.html',
})
export class TrailComponent implements OnInit {
  trails?: ITrail[];
  isLoading = false;

  constructor(protected trailService: TrailService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.trailService.query().subscribe(
      (res: HttpResponse<ITrail[]>) => {
        this.isLoading = false;
        this.trails = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ITrail): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(trail: ITrail): void {
    const modalRef = this.modalService.open(TrailDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.trail = trail;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
