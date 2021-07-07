jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITrailPathWaypoint, TrailPathWaypoint } from '../trail-path-waypoint.model';
import { TrailPathWaypointService } from '../service/trail-path-waypoint.service';

import { TrailPathWaypointRoutingResolveService } from './trail-path-waypoint-routing-resolve.service';

describe('Service Tests', () => {
  describe('TrailPathWaypoint routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: TrailPathWaypointRoutingResolveService;
    let service: TrailPathWaypointService;
    let resultTrailPathWaypoint: ITrailPathWaypoint | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(TrailPathWaypointRoutingResolveService);
      service = TestBed.inject(TrailPathWaypointService);
      resultTrailPathWaypoint = undefined;
    });

    describe('resolve', () => {
      it('should return ITrailPathWaypoint returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTrailPathWaypoint = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTrailPathWaypoint).toEqual({ id: 123 });
      });

      it('should return new ITrailPathWaypoint if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTrailPathWaypoint = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultTrailPathWaypoint).toEqual(new TrailPathWaypoint());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as TrailPathWaypoint })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTrailPathWaypoint = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTrailPathWaypoint).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
