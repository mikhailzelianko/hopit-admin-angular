import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { TrailPathWaypointService } from '../service/trail-path-waypoint.service';

import { TrailPathWaypointComponent } from './trail-path-waypoint.component';

describe('Component Tests', () => {
  describe('TrailPathWaypoint Management Component', () => {
    let comp: TrailPathWaypointComponent;
    let fixture: ComponentFixture<TrailPathWaypointComponent>;
    let service: TrailPathWaypointService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TrailPathWaypointComponent],
      })
        .overrideTemplate(TrailPathWaypointComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TrailPathWaypointComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(TrailPathWaypointService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.trailPathWaypoints?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
