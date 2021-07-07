import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITrailPathWaypoint, TrailPathWaypoint } from '../trail-path-waypoint.model';
import { TrailPathWaypointService } from '../service/trail-path-waypoint.service';
import { ITrailPath } from 'app/entities/trail-path/trail-path.model';
import { TrailPathService } from 'app/entities/trail-path/service/trail-path.service';

@Component({
  selector: 'jhi-trail-path-waypoint-update',
  templateUrl: './trail-path-waypoint-update.component.html',
})
export class TrailPathWaypointUpdateComponent implements OnInit {
  isSaving = false;

  trailPathsSharedCollection: ITrailPath[] = [];

  editForm = this.fb.group({
    id: [],
    waypointLat: [],
    waypointLong: [],
    trailpath: [],
  });

  constructor(
    protected trailPathWaypointService: TrailPathWaypointService,
    protected trailPathService: TrailPathService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ trailPathWaypoint }) => {
      this.updateForm(trailPathWaypoint);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const trailPathWaypoint = this.createFromForm();
    if (trailPathWaypoint.id !== undefined) {
      this.subscribeToSaveResponse(this.trailPathWaypointService.update(trailPathWaypoint));
    } else {
      this.subscribeToSaveResponse(this.trailPathWaypointService.create(trailPathWaypoint));
    }
  }

  trackTrailPathById(index: number, item: ITrailPath): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITrailPathWaypoint>>): void {
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

  protected updateForm(trailPathWaypoint: ITrailPathWaypoint): void {
    this.editForm.patchValue({
      id: trailPathWaypoint.id,
      waypointLat: trailPathWaypoint.waypointLat,
      waypointLong: trailPathWaypoint.waypointLong,
      trailpath: trailPathWaypoint.trailpath,
    });

    this.trailPathsSharedCollection = this.trailPathService.addTrailPathToCollectionIfMissing(
      this.trailPathsSharedCollection,
      trailPathWaypoint.trailpath
    );
  }

  protected loadRelationshipsOptions(): void {
    this.trailPathService
      .query()
      .pipe(map((res: HttpResponse<ITrailPath[]>) => res.body ?? []))
      .pipe(
        map((trailPaths: ITrailPath[]) =>
          this.trailPathService.addTrailPathToCollectionIfMissing(trailPaths, this.editForm.get('trailpath')!.value)
        )
      )
      .subscribe((trailPaths: ITrailPath[]) => (this.trailPathsSharedCollection = trailPaths));
  }

  protected createFromForm(): ITrailPathWaypoint {
    return {
      ...new TrailPathWaypoint(),
      id: this.editForm.get(['id'])!.value,
      waypointLat: this.editForm.get(['waypointLat'])!.value,
      waypointLong: this.editForm.get(['waypointLong'])!.value,
      trailpath: this.editForm.get(['trailpath'])!.value,
    };
  }
}
