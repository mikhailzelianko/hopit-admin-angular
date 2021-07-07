import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TrailPathWaypointDetailComponent } from './trail-path-waypoint-detail.component';

describe('Component Tests', () => {
  describe('TrailPathWaypoint Management Detail Component', () => {
    let comp: TrailPathWaypointDetailComponent;
    let fixture: ComponentFixture<TrailPathWaypointDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [TrailPathWaypointDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ trailPathWaypoint: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(TrailPathWaypointDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TrailPathWaypointDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load trailPathWaypoint on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.trailPathWaypoint).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
