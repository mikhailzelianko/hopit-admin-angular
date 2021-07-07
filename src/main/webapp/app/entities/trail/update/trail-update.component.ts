import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITrail, Trail } from '../trail.model';
import { TrailService } from '../service/trail.service';
import { ICountry } from 'app/entities/country/country.model';
import { CountryService } from 'app/entities/country/service/country.service';

@Component({
  selector: 'jhi-trail-update',
  templateUrl: './trail-update.component.html',
})
export class TrailUpdateComponent implements OnInit {
  isSaving = false;

  countriesCollection: ICountry[] = [];

  editForm = this.fb.group({
    id: [],
    title: [null, [Validators.required]],
    description: [],
    shortDescription: [],
    specialRules: [],
    type: [null, [Validators.required]],
    price: [],
    enterLat: [],
    enterLong: [],
    flagUnavailable: [],
    flagWater: [],
    flagBirdwatching: [],
    flagReligious: [],
    flagFishing: [],
    flagParking: [],
    flagWC: [],
    flagCamping: [],
    flagPicnic: [],
    flagStreetfood: [],
    source: [],
    adminComment: [],
    country: [],
  });

  constructor(
    protected trailService: TrailService,
    protected countryService: CountryService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ trail }) => {
      this.updateForm(trail);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const trail = this.createFromForm();
    if (trail.id !== undefined) {
      this.subscribeToSaveResponse(this.trailService.update(trail));
    } else {
      this.subscribeToSaveResponse(this.trailService.create(trail));
    }
  }

  trackCountryById(index: number, item: ICountry): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITrail>>): void {
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

  protected updateForm(trail: ITrail): void {
    this.editForm.patchValue({
      id: trail.id,
      title: trail.title,
      description: trail.description,
      shortDescription: trail.shortDescription,
      specialRules: trail.specialRules,
      type: trail.type,
      price: trail.price,
      enterLat: trail.enterLat,
      enterLong: trail.enterLong,
      flagUnavailable: trail.flagUnavailable,
      flagWater: trail.flagWater,
      flagBirdwatching: trail.flagBirdwatching,
      flagReligious: trail.flagReligious,
      flagFishing: trail.flagFishing,
      flagParking: trail.flagParking,
      flagWC: trail.flagWC,
      flagCamping: trail.flagCamping,
      flagPicnic: trail.flagPicnic,
      flagStreetfood: trail.flagStreetfood,
      source: trail.source,
      adminComment: trail.adminComment,
      country: trail.country,
    });

    this.countriesCollection = this.countryService.addCountryToCollectionIfMissing(this.countriesCollection, trail.country);
  }

  protected loadRelationshipsOptions(): void {
    this.countryService
      .query({ filter: 'trail-is-null' })
      .pipe(map((res: HttpResponse<ICountry[]>) => res.body ?? []))
      .pipe(
        map((countries: ICountry[]) => this.countryService.addCountryToCollectionIfMissing(countries, this.editForm.get('country')!.value))
      )
      .subscribe((countries: ICountry[]) => (this.countriesCollection = countries));
  }

  protected createFromForm(): ITrail {
    return {
      ...new Trail(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      description: this.editForm.get(['description'])!.value,
      shortDescription: this.editForm.get(['shortDescription'])!.value,
      specialRules: this.editForm.get(['specialRules'])!.value,
      type: this.editForm.get(['type'])!.value,
      price: this.editForm.get(['price'])!.value,
      enterLat: this.editForm.get(['enterLat'])!.value,
      enterLong: this.editForm.get(['enterLong'])!.value,
      flagUnavailable: this.editForm.get(['flagUnavailable'])!.value,
      flagWater: this.editForm.get(['flagWater'])!.value,
      flagBirdwatching: this.editForm.get(['flagBirdwatching'])!.value,
      flagReligious: this.editForm.get(['flagReligious'])!.value,
      flagFishing: this.editForm.get(['flagFishing'])!.value,
      flagParking: this.editForm.get(['flagParking'])!.value,
      flagWC: this.editForm.get(['flagWC'])!.value,
      flagCamping: this.editForm.get(['flagCamping'])!.value,
      flagPicnic: this.editForm.get(['flagPicnic'])!.value,
      flagStreetfood: this.editForm.get(['flagStreetfood'])!.value,
      source: this.editForm.get(['source'])!.value,
      adminComment: this.editForm.get(['adminComment'])!.value,
      country: this.editForm.get(['country'])!.value,
    };
  }
}
