import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ITrail } from '../trail.model';

@Component({
  selector: 'jhi-trail-detail',
  templateUrl: './trail-detail.component.html',
})
export class TrailDetailComponent implements OnInit {
  trail: ITrail | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ trail }) => {
      this.trail = trail;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
