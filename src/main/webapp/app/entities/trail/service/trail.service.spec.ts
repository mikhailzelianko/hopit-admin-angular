import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { TrailType } from 'app/entities/enumerations/trail-type.model';
import { ITrail, Trail } from '../trail.model';

import { TrailService } from './trail.service';

describe('Service Tests', () => {
  describe('Trail Service', () => {
    let service: TrailService;
    let httpMock: HttpTestingController;
    let elemDefault: ITrail;
    let expectedResult: ITrail | ITrail[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(TrailService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        title: 'AAAAAAA',
        description: 'AAAAAAA',
        shortDescription: 'AAAAAAA',
        specialRules: 'AAAAAAA',
        type: TrailType.HIKING,
        price: 0,
        enterLat: 0,
        enterLong: 0,
        flagUnavailable: false,
        flagWater: false,
        flagBirdwatching: false,
        flagReligious: false,
        flagFishing: false,
        flagParking: false,
        flagWC: false,
        flagCamping: false,
        flagPicnic: false,
        flagStreetfood: false,
        source: 'AAAAAAA',
        adminComment: 'AAAAAAA',
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

      it('should create a Trail', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Trail()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Trail', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
            description: 'BBBBBB',
            shortDescription: 'BBBBBB',
            specialRules: 'BBBBBB',
            type: 'BBBBBB',
            price: 1,
            enterLat: 1,
            enterLong: 1,
            flagUnavailable: true,
            flagWater: true,
            flagBirdwatching: true,
            flagReligious: true,
            flagFishing: true,
            flagParking: true,
            flagWC: true,
            flagCamping: true,
            flagPicnic: true,
            flagStreetfood: true,
            source: 'BBBBBB',
            adminComment: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Trail', () => {
        const patchObject = Object.assign(
          {
            title: 'BBBBBB',
            description: 'BBBBBB',
            enterLong: 1,
            flagReligious: true,
            flagParking: true,
            flagWC: true,
            flagCamping: true,
            flagStreetfood: true,
            source: 'BBBBBB',
          },
          new Trail()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Trail', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
            description: 'BBBBBB',
            shortDescription: 'BBBBBB',
            specialRules: 'BBBBBB',
            type: 'BBBBBB',
            price: 1,
            enterLat: 1,
            enterLong: 1,
            flagUnavailable: true,
            flagWater: true,
            flagBirdwatching: true,
            flagReligious: true,
            flagFishing: true,
            flagParking: true,
            flagWC: true,
            flagCamping: true,
            flagPicnic: true,
            flagStreetfood: true,
            source: 'BBBBBB',
            adminComment: 'BBBBBB',
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

      it('should delete a Trail', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addTrailToCollectionIfMissing', () => {
        it('should add a Trail to an empty array', () => {
          const trail: ITrail = { id: 123 };
          expectedResult = service.addTrailToCollectionIfMissing([], trail);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(trail);
        });

        it('should not add a Trail to an array that contains it', () => {
          const trail: ITrail = { id: 123 };
          const trailCollection: ITrail[] = [
            {
              ...trail,
            },
            { id: 456 },
          ];
          expectedResult = service.addTrailToCollectionIfMissing(trailCollection, trail);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Trail to an array that doesn't contain it", () => {
          const trail: ITrail = { id: 123 };
          const trailCollection: ITrail[] = [{ id: 456 }];
          expectedResult = service.addTrailToCollectionIfMissing(trailCollection, trail);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(trail);
        });

        it('should add only unique Trail to an array', () => {
          const trailArray: ITrail[] = [{ id: 123 }, { id: 456 }, { id: 43633 }];
          const trailCollection: ITrail[] = [{ id: 123 }];
          expectedResult = service.addTrailToCollectionIfMissing(trailCollection, ...trailArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const trail: ITrail = { id: 123 };
          const trail2: ITrail = { id: 456 };
          expectedResult = service.addTrailToCollectionIfMissing([], trail, trail2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(trail);
          expect(expectedResult).toContain(trail2);
        });

        it('should accept null and undefined values', () => {
          const trail: ITrail = { id: 123 };
          expectedResult = service.addTrailToCollectionIfMissing([], null, trail, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(trail);
        });

        it('should return initial array if no Trail is added', () => {
          const trailCollection: ITrail[] = [{ id: 123 }];
          expectedResult = service.addTrailToCollectionIfMissing(trailCollection, undefined, null);
          expect(expectedResult).toEqual(trailCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
