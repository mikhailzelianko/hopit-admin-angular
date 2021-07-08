import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITrailPath, TrailPath } from '../trail-path.model';

import { TrailPathService } from './trail-path.service';

describe('Service Tests', () => {
  describe('TrailPath Service', () => {
    let service: TrailPathService;
    let httpMock: HttpTestingController;
    let elemDefault: ITrailPath;
    let expectedResult: ITrailPath | ITrailPath[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(TrailPathService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        title: 'AAAAAAA',
        distance: 0,
        description: 'AAAAAAA',
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

      it('should create a TrailPath', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new TrailPath()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a TrailPath', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
            distance: 1,
            description: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a TrailPath', () => {
        const patchObject = Object.assign(
          {
            title: 'BBBBBB',
            distance: 1,
            description: 'BBBBBB',
          },
          new TrailPath()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of TrailPath', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
            distance: 1,
            description: 'BBBBBB',
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

      it('should delete a TrailPath', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addTrailPathToCollectionIfMissing', () => {
        it('should add a TrailPath to an empty array', () => {
          const trailPath: ITrailPath = { id: 123 };
          expectedResult = service.addTrailPathToCollectionIfMissing([], trailPath);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(trailPath);
        });

        it('should not add a TrailPath to an array that contains it', () => {
          const trailPath: ITrailPath = { id: 123 };
          const trailPathCollection: ITrailPath[] = [
            {
              ...trailPath,
            },
            { id: 456 },
          ];
          expectedResult = service.addTrailPathToCollectionIfMissing(trailPathCollection, trailPath);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a TrailPath to an array that doesn't contain it", () => {
          const trailPath: ITrailPath = { id: 123 };
          const trailPathCollection: ITrailPath[] = [{ id: 456 }];
          expectedResult = service.addTrailPathToCollectionIfMissing(trailPathCollection, trailPath);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(trailPath);
        });

        it('should add only unique TrailPath to an array', () => {
          const trailPathArray: ITrailPath[] = [{ id: 123 }, { id: 456 }, { id: 4591 }];
          const trailPathCollection: ITrailPath[] = [{ id: 123 }];
          expectedResult = service.addTrailPathToCollectionIfMissing(trailPathCollection, ...trailPathArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const trailPath: ITrailPath = { id: 123 };
          const trailPath2: ITrailPath = { id: 456 };
          expectedResult = service.addTrailPathToCollectionIfMissing([], trailPath, trailPath2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(trailPath);
          expect(expectedResult).toContain(trailPath2);
        });

        it('should accept null and undefined values', () => {
          const trailPath: ITrailPath = { id: 123 };
          expectedResult = service.addTrailPathToCollectionIfMissing([], null, trailPath, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(trailPath);
        });

        it('should return initial array if no TrailPath is added', () => {
          const trailPathCollection: ITrailPath[] = [{ id: 123 }];
          expectedResult = service.addTrailPathToCollectionIfMissing(trailPathCollection, undefined, null);
          expect(expectedResult).toEqual(trailPathCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
