jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { AdditionalMapObjectService } from '../service/additional-map-object.service';
import { IAdditionalMapObject, AdditionalMapObject } from '../additional-map-object.model';
import { ITrail } from 'app/entities/trail/trail.model';
import { TrailService } from 'app/entities/trail/service/trail.service';

import { AdditionalMapObjectUpdateComponent } from './additional-map-object-update.component';

describe('Component Tests', () => {
  describe('AdditionalMapObject Management Update Component', () => {
    let comp: AdditionalMapObjectUpdateComponent;
    let fixture: ComponentFixture<AdditionalMapObjectUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let additionalMapObjectService: AdditionalMapObjectService;
    let trailService: TrailService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AdditionalMapObjectUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(AdditionalMapObjectUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AdditionalMapObjectUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      additionalMapObjectService = TestBed.inject(AdditionalMapObjectService);
      trailService = TestBed.inject(TrailService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Trail query and add missing value', () => {
        const additionalMapObject: IAdditionalMapObject = { id: 456 };
        const trail: ITrail = { id: 75394 };
        additionalMapObject.trail = trail;

        const trailCollection: ITrail[] = [{ id: 69636 }];
        jest.spyOn(trailService, 'query').mockReturnValue(of(new HttpResponse({ body: trailCollection })));
        const additionalTrails = [trail];
        const expectedCollection: ITrail[] = [...additionalTrails, ...trailCollection];
        jest.spyOn(trailService, 'addTrailToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ additionalMapObject });
        comp.ngOnInit();

        expect(trailService.query).toHaveBeenCalled();
        expect(trailService.addTrailToCollectionIfMissing).toHaveBeenCalledWith(trailCollection, ...additionalTrails);
        expect(comp.trailsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const additionalMapObject: IAdditionalMapObject = { id: 456 };
        const trail: ITrail = { id: 96641 };
        additionalMapObject.trail = trail;

        activatedRoute.data = of({ additionalMapObject });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(additionalMapObject));
        expect(comp.trailsSharedCollection).toContain(trail);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<AdditionalMapObject>>();
        const additionalMapObject = { id: 123 };
        jest.spyOn(additionalMapObjectService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ additionalMapObject });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: additionalMapObject }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(additionalMapObjectService.update).toHaveBeenCalledWith(additionalMapObject);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<AdditionalMapObject>>();
        const additionalMapObject = new AdditionalMapObject();
        jest.spyOn(additionalMapObjectService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ additionalMapObject });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: additionalMapObject }));
        saveSubject.complete();

        // THEN
        expect(additionalMapObjectService.create).toHaveBeenCalledWith(additionalMapObject);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<AdditionalMapObject>>();
        const additionalMapObject = { id: 123 };
        jest.spyOn(additionalMapObjectService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ additionalMapObject });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(additionalMapObjectService.update).toHaveBeenCalledWith(additionalMapObject);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackTrailById', () => {
        it('Should return tracked Trail primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackTrailById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
