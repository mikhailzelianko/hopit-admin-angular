jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TrailPathService } from '../service/trail-path.service';
import { ITrailPath, TrailPath } from '../trail-path.model';
import { ITrail } from 'app/entities/trail/trail.model';
import { TrailService } from 'app/entities/trail/service/trail.service';

import { TrailPathUpdateComponent } from './trail-path-update.component';

describe('Component Tests', () => {
  describe('TrailPath Management Update Component', () => {
    let comp: TrailPathUpdateComponent;
    let fixture: ComponentFixture<TrailPathUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let trailPathService: TrailPathService;
    let trailService: TrailService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TrailPathUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TrailPathUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TrailPathUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      trailPathService = TestBed.inject(TrailPathService);
      trailService = TestBed.inject(TrailService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Trail query and add missing value', () => {
        const trailPath: ITrailPath = { id: 456 };
        const trail: ITrail = { id: 66431 };
        trailPath.trail = trail;

        const trailCollection: ITrail[] = [{ id: 82676 }];
        jest.spyOn(trailService, 'query').mockReturnValue(of(new HttpResponse({ body: trailCollection })));
        const additionalTrails = [trail];
        const expectedCollection: ITrail[] = [...additionalTrails, ...trailCollection];
        jest.spyOn(trailService, 'addTrailToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ trailPath });
        comp.ngOnInit();

        expect(trailService.query).toHaveBeenCalled();
        expect(trailService.addTrailToCollectionIfMissing).toHaveBeenCalledWith(trailCollection, ...additionalTrails);
        expect(comp.trailsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const trailPath: ITrailPath = { id: 456 };
        const trail: ITrail = { id: 78658 };
        trailPath.trail = trail;

        activatedRoute.data = of({ trailPath });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(trailPath));
        expect(comp.trailsSharedCollection).toContain(trail);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TrailPath>>();
        const trailPath = { id: 123 };
        jest.spyOn(trailPathService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ trailPath });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: trailPath }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(trailPathService.update).toHaveBeenCalledWith(trailPath);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TrailPath>>();
        const trailPath = new TrailPath();
        jest.spyOn(trailPathService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ trailPath });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: trailPath }));
        saveSubject.complete();

        // THEN
        expect(trailPathService.create).toHaveBeenCalledWith(trailPath);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TrailPath>>();
        const trailPath = { id: 123 };
        jest.spyOn(trailPathService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ trailPath });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(trailPathService.update).toHaveBeenCalledWith(trailPath);
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
