import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITrailPath } from '../trail-path.model';
import { TrailPathService } from '../service/trail-path.service';
import { TrailPathDeleteDialogComponent } from '../delete/trail-path-delete-dialog.component';

@Component({
  selector: 'jhi-trail-path',
  templateUrl: './trail-path.component.html',
})
export class TrailPathComponent implements OnInit {
  trailPaths?: ITrailPath[];
  isLoading = false;

  constructor(protected trailPathService: TrailPathService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.trailPathService.query().subscribe(
      (res: HttpResponse<ITrailPath[]>) => {
        this.isLoading = false;
        this.trailPaths = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ITrailPath): number {
    return item.id!;
  }

  delete(trailPath: ITrailPath): void {
    const modalRef = this.modalService.open(TrailPathDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.trailPath = trailPath;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
