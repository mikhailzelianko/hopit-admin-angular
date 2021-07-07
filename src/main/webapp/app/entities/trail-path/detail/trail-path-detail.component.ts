import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITrailPath } from '../trail-path.model';

@Component({
  selector: 'jhi-trail-path-detail',
  templateUrl: './trail-path-detail.component.html',
})
export class TrailPathDetailComponent implements OnInit {
  trailPath: ITrailPath | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ trailPath }) => {
      this.trailPath = trailPath;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
