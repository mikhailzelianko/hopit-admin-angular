import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITrail } from '../trail.model';
import { TrailService } from '../service/trail.service';
import { TrailDeleteDialogComponent } from '../delete/trail-delete-dialog.component';

@Component({
  selector: 'jhi-trail',
  templateUrl: './trail.component.html',
})
export class TrailComponent implements OnInit {
  trails?: ITrail[];
  isLoading = false;

  constructor(protected trailService: TrailService, protected modalService: NgbModal) {}

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
