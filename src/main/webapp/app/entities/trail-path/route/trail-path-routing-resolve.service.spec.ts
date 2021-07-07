jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITrailPath, TrailPath } from '../trail-path.model';
import { TrailPathService } from '../service/trail-path.service';

import { TrailPathRoutingResolveService } from './trail-path-routing-resolve.service';

describe('Service Tests', () => {
  describe('TrailPath routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: TrailPathRoutingResolveService;
    let service: TrailPathService;
    let resultTrailPath: ITrailPath | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(TrailPathRoutingResolveService);
      service = TestBed.inject(TrailPathService);
      resultTrailPath = undefined;
    });

    describe('resolve', () => {
      it('should return ITrailPath returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTrailPath = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTrailPath).toEqual({ id: 123 });
      });

      it('should return new ITrailPath if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTrailPath = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultTrailPath).toEqual(new TrailPath());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as TrailPath })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTrailPath = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTrailPath).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
