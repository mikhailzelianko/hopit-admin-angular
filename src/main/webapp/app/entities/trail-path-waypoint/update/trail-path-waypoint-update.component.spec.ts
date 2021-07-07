jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TrailPathWaypointService } from '../service/trail-path-waypoint.service';
import { ITrailPathWaypoint, TrailPathWaypoint } from '../trail-path-waypoint.model';
import { ITrailPath } from 'app/entities/trail-path/trail-path.model';
import { TrailPathService } from 'app/entities/trail-path/service/trail-path.service';

import { TrailPathWaypointUpdateComponent } from './trail-path-waypoint-update.component';

describe('Component Tests', () => {
  describe('TrailPathWaypoint Management Update Component', () => {
    let comp: TrailPathWaypointUpdateComponent;
    let fixture: ComponentFixture<TrailPathWaypointUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let trailPathWaypointService: TrailPathWaypointService;
    let trailPathService: TrailPathService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TrailPathWaypointUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TrailPathWaypointUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TrailPathWaypointUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      trailPathWaypointService = TestBed.inject(TrailPathWaypointService);
      trailPathService = TestBed.inject(TrailPathService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call TrailPath query and add missing value', () => {
        const trailPathWaypoint: ITrailPathWaypoint = { id: 456 };
        const trailpath: ITrailPath = { id: 24970 };
        trailPathWaypoint.trailpath = trailpath;

        const trailPathCollection: ITrailPath[] = [{ id: 2472 }];
        jest.spyOn(trailPathService, 'query').mockReturnValue(of(new HttpResponse({ body: trailPathCollection })));
        const additionalTrailPaths = [trailpath];
        const expectedCollection: ITrailPath[] = [...additionalTrailPaths, ...trailPathCollection];
        jest.spyOn(trailPathService, 'addTrailPathToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ trailPathWaypoint });
        comp.ngOnInit();

        expect(trailPathService.query).toHaveBeenCalled();
        expect(trailPathService.addTrailPathToCollectionIfMissing).toHaveBeenCalledWith(trailPathCollection, ...additionalTrailPaths);
        expect(comp.trailPathsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const trailPathWaypoint: ITrailPathWaypoint = { id: 456 };
        const trailpath: ITrailPath = { id: 9192 };
        trailPathWaypoint.trailpath = trailpath;

        activatedRoute.data = of({ trailPathWaypoint });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(trailPathWaypoint));
        expect(comp.trailPathsSharedCollection).toContain(trailpath);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TrailPathWaypoint>>();
        const trailPathWaypoint = { id: 123 };
        jest.spyOn(trailPathWaypointService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ trailPathWaypoint });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: trailPathWaypoint }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(trailPathWaypointService.update).toHaveBeenCalledWith(trailPathWaypoint);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TrailPathWaypoint>>();
        const trailPathWaypoint = new TrailPathWaypoint();
        jest.spyOn(trailPathWaypointService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ trailPathWaypoint });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: trailPathWaypoint }));
        saveSubject.complete();

        // THEN
        expect(trailPathWaypointService.create).toHaveBeenCalledWith(trailPathWaypoint);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<TrailPathWaypoint>>();
        const trailPathWaypoint = { id: 123 };
        jest.spyOn(trailPathWaypointService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ trailPathWaypoint });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(trailPathWaypointService.update).toHaveBeenCalledWith(trailPathWaypoint);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackTrailPathById', () => {
        it('Should return tracked TrailPath primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackTrailPathById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
