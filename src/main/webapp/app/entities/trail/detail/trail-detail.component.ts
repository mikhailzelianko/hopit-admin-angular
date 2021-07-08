import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITrail } from '../trail.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-trail-detail',
  templateUrl: './trail-detail.component.html',
})
export class TrailDetailComponent implements OnInit {
  trail: ITrail | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ trail }) => {
      this.trail = trail;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
