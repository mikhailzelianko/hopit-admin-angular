jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITrail, Trail } from '../trail.model';
import { TrailService } from '../service/trail.service';

import { TrailRoutingResolveService } from './trail-routing-resolve.service';

describe('Service Tests', () => {
  describe('Trail routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: TrailRoutingResolveService;
    let service: TrailService;
    let resultTrail: ITrail | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(TrailRoutingResolveService);
      service = TestBed.inject(TrailService);
      resultTrail = undefined;
    });

    describe('resolve', () => {
      it('should return ITrail returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTrail = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTrail).toEqual({ id: 123 });
      });

      it('should return new ITrail if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTrail = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultTrail).toEqual(new Trail());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Trail })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTrail = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTrail).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
