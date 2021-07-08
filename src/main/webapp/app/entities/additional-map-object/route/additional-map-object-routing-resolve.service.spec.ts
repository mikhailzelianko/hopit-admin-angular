jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAdditionalMapObject, AdditionalMapObject } from '../additional-map-object.model';
import { AdditionalMapObjectService } from '../service/additional-map-object.service';

import { AdditionalMapObjectRoutingResolveService } from './additional-map-object-routing-resolve.service';

describe('Service Tests', () => {
  describe('AdditionalMapObject routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: AdditionalMapObjectRoutingResolveService;
    let service: AdditionalMapObjectService;
    let resultAdditionalMapObject: IAdditionalMapObject | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(AdditionalMapObjectRoutingResolveService);
      service = TestBed.inject(AdditionalMapObjectService);
      resultAdditionalMapObject = undefined;
    });

    describe('resolve', () => {
      it('should return IAdditionalMapObject returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAdditionalMapObject = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAdditionalMapObject).toEqual({ id: 123 });
      });

      it('should return new IAdditionalMapObject if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAdditionalMapObject = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultAdditionalMapObject).toEqual(new AdditionalMapObject());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as AdditionalMapObject })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAdditionalMapObject = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAdditionalMapObject).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
