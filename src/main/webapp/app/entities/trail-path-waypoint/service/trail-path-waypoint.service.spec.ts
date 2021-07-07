import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITrailPathWaypoint, TrailPathWaypoint } from '../trail-path-waypoint.model';

import { TrailPathWaypointService } from './trail-path-waypoint.service';

describe('Service Tests', () => {
  describe('TrailPathWaypoint Service', () => {
    let service: TrailPathWaypointService;
    let httpMock: HttpTestingController;
    let elemDefault: ITrailPathWaypoint;
    let expectedResult: ITrailPathWaypoint | ITrailPathWaypoint[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(TrailPathWaypointService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        waypointLat: 0,
        waypointLong: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a TrailPathWaypoint', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new TrailPathWaypoint()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a TrailPathWaypoint', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            waypointLat: 1,
            waypointLong: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a TrailPathWaypoint', () => {
        const patchObject = Object.assign({}, new TrailPathWaypoint());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of TrailPathWaypoint', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            waypointLat: 1,
            waypointLong: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a TrailPathWaypoint', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addTrailPathWaypointToCollectionIfMissing', () => {
        it('should add a TrailPathWaypoint to an empty array', () => {
          const trailPathWaypoint: ITrailPathWaypoint = { id: 123 };
          expectedResult = service.addTrailPathWaypointToCollectionIfMissing([], trailPathWaypoint);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(trailPathWaypoint);
        });

        it('should not add a TrailPathWaypoint to an array that contains it', () => {
          const trailPathWaypoint: ITrailPathWaypoint = { id: 123 };
          const trailPathWaypointCollection: ITrailPathWaypoint[] = [
            {
              ...trailPathWaypoint,
            },
            { id: 456 },
          ];
          expectedResult = service.addTrailPathWaypointToCollectionIfMissing(trailPathWaypointCollection, trailPathWaypoint);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a TrailPathWaypoint to an array that doesn't contain it", () => {
          const trailPathWaypoint: ITrailPathWaypoint = { id: 123 };
          const trailPathWaypointCollection: ITrailPathWaypoint[] = [{ id: 456 }];
          expectedResult = service.addTrailPathWaypointToCollectionIfMissing(trailPathWaypointCollection, trailPathWaypoint);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(trailPathWaypoint);
        });

        it('should add only unique TrailPathWaypoint to an array', () => {
          const trailPathWaypointArray: ITrailPathWaypoint[] = [{ id: 123 }, { id: 456 }, { id: 22065 }];
          const trailPathWaypointCollection: ITrailPathWaypoint[] = [{ id: 123 }];
          expectedResult = service.addTrailPathWaypointToCollectionIfMissing(trailPathWaypointCollection, ...trailPathWaypointArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const trailPathWaypoint: ITrailPathWaypoint = { id: 123 };
          const trailPathWaypoint2: ITrailPathWaypoint = { id: 456 };
          expectedResult = service.addTrailPathWaypointToCollectionIfMissing([], trailPathWaypoint, trailPathWaypoint2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(trailPathWaypoint);
          expect(expectedResult).toContain(trailPathWaypoint2);
        });

        it('should accept null and undefined values', () => {
          const trailPathWaypoint: ITrailPathWaypoint = { id: 123 };
          expectedResult = service.addTrailPathWaypointToCollectionIfMissing([], null, trailPathWaypoint, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(trailPathWaypoint);
        });

        it('should return initial array if no TrailPathWaypoint is added', () => {
          const trailPathWaypointCollection: ITrailPathWaypoint[] = [{ id: 123 }];
          expectedResult = service.addTrailPathWaypointToCollectionIfMissing(trailPathWaypointCollection, undefined, null);
          expect(expectedResult).toEqual(trailPathWaypointCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
