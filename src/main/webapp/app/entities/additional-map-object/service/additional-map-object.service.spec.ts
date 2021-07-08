import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAdditionalMapObject, AdditionalMapObject } from '../additional-map-object.model';

import { AdditionalMapObjectService } from './additional-map-object.service';

describe('Service Tests', () => {
  describe('AdditionalMapObject Service', () => {
    let service: AdditionalMapObjectService;
    let httpMock: HttpTestingController;
    let elemDefault: IAdditionalMapObject;
    let expectedResult: IAdditionalMapObject | IAdditionalMapObject[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AdditionalMapObjectService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        title: 'AAAAAAA',
        objectLat: 0,
        objectLong: 0,
        type: 'AAAAAAA',
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

      it('should create a AdditionalMapObject', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new AdditionalMapObject()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a AdditionalMapObject', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
            objectLat: 1,
            objectLong: 1,
            type: 'BBBBBB',
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

      it('should partial update a AdditionalMapObject', () => {
        const patchObject = Object.assign(
          {
            title: 'BBBBBB',
            objectLong: 1,
            type: 'BBBBBB',
          },
          new AdditionalMapObject()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of AdditionalMapObject', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
            objectLat: 1,
            objectLong: 1,
            type: 'BBBBBB',
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

      it('should delete a AdditionalMapObject', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAdditionalMapObjectToCollectionIfMissing', () => {
        it('should add a AdditionalMapObject to an empty array', () => {
          const additionalMapObject: IAdditionalMapObject = { id: 123 };
          expectedResult = service.addAdditionalMapObjectToCollectionIfMissing([], additionalMapObject);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(additionalMapObject);
        });

        it('should not add a AdditionalMapObject to an array that contains it', () => {
          const additionalMapObject: IAdditionalMapObject = { id: 123 };
          const additionalMapObjectCollection: IAdditionalMapObject[] = [
            {
              ...additionalMapObject,
            },
            { id: 456 },
          ];
          expectedResult = service.addAdditionalMapObjectToCollectionIfMissing(additionalMapObjectCollection, additionalMapObject);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a AdditionalMapObject to an array that doesn't contain it", () => {
          const additionalMapObject: IAdditionalMapObject = { id: 123 };
          const additionalMapObjectCollection: IAdditionalMapObject[] = [{ id: 456 }];
          expectedResult = service.addAdditionalMapObjectToCollectionIfMissing(additionalMapObjectCollection, additionalMapObject);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(additionalMapObject);
        });

        it('should add only unique AdditionalMapObject to an array', () => {
          const additionalMapObjectArray: IAdditionalMapObject[] = [{ id: 123 }, { id: 456 }, { id: 26101 }];
          const additionalMapObjectCollection: IAdditionalMapObject[] = [{ id: 123 }];
          expectedResult = service.addAdditionalMapObjectToCollectionIfMissing(additionalMapObjectCollection, ...additionalMapObjectArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const additionalMapObject: IAdditionalMapObject = { id: 123 };
          const additionalMapObject2: IAdditionalMapObject = { id: 456 };
          expectedResult = service.addAdditionalMapObjectToCollectionIfMissing([], additionalMapObject, additionalMapObject2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(additionalMapObject);
          expect(expectedResult).toContain(additionalMapObject2);
        });

        it('should accept null and undefined values', () => {
          const additionalMapObject: IAdditionalMapObject = { id: 123 };
          expectedResult = service.addAdditionalMapObjectToCollectionIfMissing([], null, additionalMapObject, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(additionalMapObject);
        });

        it('should return initial array if no AdditionalMapObject is added', () => {
          const additionalMapObjectCollection: IAdditionalMapObject[] = [{ id: 123 }];
          expectedResult = service.addAdditionalMapObjectToCollectionIfMissing(additionalMapObjectCollection, undefined, null);
          expect(expectedResult).toEqual(additionalMapObjectCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
