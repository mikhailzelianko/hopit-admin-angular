import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITrailPath, TrailPath } from '../trail-path.model';
import { TrailPathService } from '../service/trail-path.service';
import { ITrail } from 'app/entities/trail/trail.model';
import { TrailService } from 'app/entities/trail/service/trail.service';

@Component({
  selector: 'jhi-trail-path-update',
  templateUrl: './trail-path-update.component.html',
})
export class TrailPathUpdateComponent implements OnInit {
  isSaving = false;

  trailsSharedCollection: ITrail[] = [];

  editForm = this.fb.group({
    id: [],
    title: [null, [Validators.required]],
    description: [],
    trail: [],
  });

  constructor(
    protected trailPathService: TrailPathService,
    protected trailService: TrailService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ trailPath }) => {
      this.updateForm(trailPath);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const trailPath = this.createFromForm();
    if (trailPath.id !== undefined) {
      this.subscribeToSaveResponse(this.trailPathService.update(trailPath));
    } else {
      this.subscribeToSaveResponse(this.trailPathService.create(trailPath));
    }
  }

  trackTrailById(index: number, item: ITrail): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITrailPath>>): void {
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

  protected updateForm(trailPath: ITrailPath): void {
    this.editForm.patchValue({
      id: trailPath.id,
      title: trailPath.title,
      description: trailPath.description,
      trail: trailPath.trail,
    });

    this.trailsSharedCollection = this.trailService.addTrailToCollectionIfMissing(this.trailsSharedCollection, trailPath.trail);
  }

  protected loadRelationshipsOptions(): void {
    this.trailService
      .query()
      .pipe(map((res: HttpResponse<ITrail[]>) => res.body ?? []))
      .pipe(map((trails: ITrail[]) => this.trailService.addTrailToCollectionIfMissing(trails, this.editForm.get('trail')!.value)))
      .subscribe((trails: ITrail[]) => (this.trailsSharedCollection = trails));
  }

  protected createFromForm(): ITrailPath {
    return {
      ...new TrailPath(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      description: this.editForm.get(['description'])!.value,
      trail: this.editForm.get(['trail'])!.value,
    };
  }
}
