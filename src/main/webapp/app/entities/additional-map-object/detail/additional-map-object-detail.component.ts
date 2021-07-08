import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAdditionalMapObject } from '../additional-map-object.model';

@Component({
  selector: 'jhi-additional-map-object-detail',
  templateUrl: './additional-map-object-detail.component.html',
})
export class AdditionalMapObjectDetailComponent implements OnInit {
  additionalMapObject: IAdditionalMapObject | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ additionalMapObject }) => {
      this.additionalMapObject = additionalMapObject;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
