import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ITrail, Trail } from '../trail.model';
import { TrailService } from '../service/trail.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { ICountry } from 'app/entities/country/country.model';
import { CountryService } from 'app/entities/country/service/country.service';
import { IRegion } from 'app/entities/region/region.model';
import { RegionService } from 'app/entities/region/service/region.service';
import { IDistrict } from 'app/entities/district/district.model';
import { DistrictService } from 'app/entities/district/service/district.service';

@Component({
  selector: 'jhi-trail-update',
  templateUrl: './trail-update.component.html',
})
export class TrailUpdateComponent implements OnInit {
  isSaving = false;

  countriesSharedCollection: ICountry[] = [];
  regionsSharedCollection: IRegion[] = [];
  districtsSharedCollection: IDistrict[] = [];

  editForm = this.fb.group({
    id: [],
    title: [null, [Validators.required]],
    description: [],
    shortDescription: [],
    specialRules: [],
    type: [null, [Validators.required]],
    coverPhoto: [],
    coverPhotoContentType: [],
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
    region: [],
    district: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected trailService: TrailService,
    protected countryService: CountryService,
    protected regionService: RegionService,
    protected districtService: DistrictService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ trail }) => {
      this.updateForm(trail);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('hopitAdminApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
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

  trackRegionById(index: number, item: IRegion): number {
    return item.id!;
  }

  trackDistrictById(index: number, item: IDistrict): number {
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
      coverPhoto: trail.coverPhoto,
      coverPhotoContentType: trail.coverPhotoContentType,
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
      region: trail.region,
      district: trail.district,
    });

    this.countriesSharedCollection = this.countryService.addCountryToCollectionIfMissing(this.countriesSharedCollection, trail.country);
    this.regionsSharedCollection = this.regionService.addRegionToCollectionIfMissing(this.regionsSharedCollection, trail.region);
    this.districtsSharedCollection = this.districtService.addDistrictToCollectionIfMissing(this.districtsSharedCollection, trail.district);
  }

  protected loadRelationshipsOptions(): void {
    this.countryService
      .query()
      .pipe(map((res: HttpResponse<ICountry[]>) => res.body ?? []))
      .pipe(
        map((countries: ICountry[]) => this.countryService.addCountryToCollectionIfMissing(countries, this.editForm.get('country')!.value))
      )
      .subscribe((countries: ICountry[]) => (this.countriesSharedCollection = countries));

    this.regionService
      .query()
      .pipe(map((res: HttpResponse<IRegion[]>) => res.body ?? []))
      .pipe(map((regions: IRegion[]) => this.regionService.addRegionToCollectionIfMissing(regions, this.editForm.get('region')!.value)))
      .subscribe((regions: IRegion[]) => (this.regionsSharedCollection = regions));

    this.districtService
      .query()
      .pipe(map((res: HttpResponse<IDistrict[]>) => res.body ?? []))
      .pipe(
        map((districts: IDistrict[]) =>
          this.districtService.addDistrictToCollectionIfMissing(districts, this.editForm.get('district')!.value)
        )
      )
      .subscribe((districts: IDistrict[]) => (this.districtsSharedCollection = districts));
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
      coverPhotoContentType: this.editForm.get(['coverPhotoContentType'])!.value,
      coverPhoto: this.editForm.get(['coverPhoto'])!.value,
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
      region: this.editForm.get(['region'])!.value,
      district: this.editForm.get(['district'])!.value,
    };
  }
}
