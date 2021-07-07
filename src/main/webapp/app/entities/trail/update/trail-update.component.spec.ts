jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TrailService } from '../service/trail.service';
import { ITrail, Trail } from '../trail.model';
import { ICountry } from 'app/entities/country/country.model';
import { CountryService } from 'app/entities/country/service/country.service';

import { TrailUpdateComponent } from './trail-update.component';

describe('Component Tests', () => {
  describe('Trail Management Update Component', () => {
    let comp: TrailUpdateComponent;
    let fixture: ComponentFixture<TrailUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let trailService: TrailService;
    let countryService: CountryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TrailUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TrailUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TrailUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      trailService = TestBed.inject(TrailService);
      countryService = TestBed.inject(CountryService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call country query and add missing value', () => {
        const trail: ITrail = { id: 456 };
        const country: ICountry = { id: 56507 };
        trail.country = country;

        const countryCollection: ICountry[] = [{ id: 11875 }];
        jest.spyOn(countryService, 'query').mockReturnValue(of(new HttpResponse({ body: countryCollection })));
        const expectedCollection: ICountry[] = [country, ...countryCollection];
        jest.spyOn(countryService, 'addCountryToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ trail });
        comp.ngOnInit();

        expect(countryService.query).toHaveBeenCalled();
        expect(countryService.addCountryToCollectionIfMissing).toHaveBeenCalledWith(countryCollection, country);
        expect(comp.countriesCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const trail: ITrail = { id: 456 };
        const country: ICountry = { id: 97719 };
        trail.country = country;

        activatedRoute.data = of({ trail });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(trail));
        expect(comp.countriesCollection).toContain(country);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Trail>>();
        const trail = { id: 123 };
        jest.spyOn(trailService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ trail });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: trail }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(trailService.update).toHaveBeenCalledWith(trail);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Trail>>();
        const trail = new Trail();
        jest.spyOn(trailService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ trail });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: trail }));
        saveSubject.complete();

        // THEN
        expect(trailService.create).toHaveBeenCalledWith(trail);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Trail>>();
        const trail = { id: 123 };
        jest.spyOn(trailService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ trail });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(trailService.update).toHaveBeenCalledWith(trail);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCountryById', () => {
        it('Should return tracked Country primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCountryById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
