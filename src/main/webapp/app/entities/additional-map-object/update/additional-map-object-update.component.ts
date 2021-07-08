import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAdditionalMapObject, AdditionalMapObject } from '../additional-map-object.model';
import { AdditionalMapObjectService } from '../service/additional-map-object.service';
import { ITrail } from 'app/entities/trail/trail.model';
import { TrailService } from 'app/entities/trail/service/trail.service';

@Component({
  selector: 'jhi-additional-map-object-update',
  templateUrl: './additional-map-object-update.component.html',
})
export class AdditionalMapObjectUpdateComponent implements OnInit {
  isSaving = false;

  trailsSharedCollection: ITrail[] = [];

  editForm = this.fb.group({
    id: [],
    title: [null, [Validators.required]],
    objectLat: [],
    objectLong: [],
    type: [],
    description: [],
    trail: [],
  });

  constructor(
    protected additionalMapObjectService: AdditionalMapObjectService,
    protected trailService: TrailService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ additionalMapObject }) => {
      this.updateForm(additionalMapObject);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const additionalMapObject = this.createFromForm();
    if (additionalMapObject.id !== undefined) {
      this.subscribeToSaveResponse(this.additionalMapObjectService.update(additionalMapObject));
    } else {
      this.subscribeToSaveResponse(this.additionalMapObjectService.create(additionalMapObject));
    }
  }

  trackTrailById(index: number, item: ITrail): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAdditionalMapObject>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(additionalMapObject: IAdditionalMapObject): void {
    this.editForm.patchValue({
      id: additionalMapObject.id,
      title: additionalMapObject.title,
      objectLat: additionalMapObject.objectLat,
      objectLong: additionalMapObject.objectLong,
      type: additionalMapObject.type,
      description: additionalMapObject.description,
      trail: additionalMapObject.trail,
    });

    this.trailsSharedCollection = this.trailService.addTrailToCollectionIfMissing(this.trailsSharedCollection, additionalMapObject.trail);
  }

  protected loadRelationshipsOptions(): void {
    this.trailService
      .query()
      .pipe(map((res: HttpResponse<ITrail[]>) => res.body ?? []))
      .pipe(map((trails: ITrail[]) => this.trailService.addTrailToCollectionIfMissing(trails, this.editForm.get('trail')!.value)))
      .subscribe((trails: ITrail[]) => (this.trailsSharedCollection = trails));
  }

  protected createFromForm(): IAdditionalMapObject {
    return {
      ...new AdditionalMapObject(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      objectLat: this.editForm.get(['objectLat'])!.value,
      objectLong: this.editForm.get(['objectLong'])!.value,
      type: this.editForm.get(['type'])!.value,
      description: this.editForm.get(['description'])!.value,
      trail: this.editForm.get(['trail'])!.value,
    };
  }
}
