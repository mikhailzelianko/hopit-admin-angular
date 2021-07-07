import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDistrict } from '../district.model';
import { DistrictService } from '../service/district.service';
import { DistrictDeleteDialogComponent } from '../delete/district-delete-dialog.component';

@Component({
  selector: 'jhi-district',
  templateUrl: './district.component.html',
})
export class DistrictComponent implements OnInit {
  districts?: IDistrict[];
  isLoading = false;

  constructor(protected districtService: DistrictService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.districtService.query().subscribe(
      (res: HttpResponse<IDistrict[]>) => {
        this.isLoading = false;
        this.districts = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IDistrict): number {
    return item.id!;
  }

  delete(district: IDistrict): void {
    const modalRef = this.modalService.open(DistrictDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.district = district;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
