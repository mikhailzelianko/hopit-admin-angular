import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAdditionalMapObject, getAdditionalMapObjectIdentifier } from '../additional-map-object.model';

export type EntityResponseType = HttpResponse<IAdditionalMapObject>;
export type EntityArrayResponseType = HttpResponse<IAdditionalMapObject[]>;

@Injectable({ providedIn: 'root' })
export class AdditionalMapObjectService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/additional-map-objects');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(additionalMapObject: IAdditionalMapObject): Observable<EntityResponseType> {
    return this.http.post<IAdditionalMapObject>(this.resourceUrl, additionalMapObject, { observe: 'response' });
  }

  update(additionalMapObject: IAdditionalMapObject): Observable<EntityResponseType> {
    return this.http.put<IAdditionalMapObject>(
      `${this.resourceUrl}/${getAdditionalMapObjectIdentifier(additionalMapObject) as number}`,
      additionalMapObject,
      { observe: 'response' }
    );
  }

  partialUpdate(additionalMapObject: IAdditionalMapObject): Observable<EntityResponseType> {
    return this.http.patch<IAdditionalMapObject>(
      `${this.resourceUrl}/${getAdditionalMapObjectIdentifier(additionalMapObject) as number}`,
      additionalMapObject,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAdditionalMapObject>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAdditionalMapObject[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAdditionalMapObjectToCollectionIfMissing(
    additionalMapObjectCollection: IAdditionalMapObject[],
    ...additionalMapObjectsToCheck: (IAdditionalMapObject | null | undefined)[]
  ): IAdditionalMapObject[] {
    const additionalMapObjects: IAdditionalMapObject[] = additionalMapObjectsToCheck.filter(isPresent);
    if (additionalMapObjects.length > 0) {
      const additionalMapObjectCollectionIdentifiers = additionalMapObjectCollection.map(
        additionalMapObjectItem => getAdditionalMapObjectIdentifier(additionalMapObjectItem)!
      );
      const additionalMapObjectsToAdd = additionalMapObjects.filter(additionalMapObjectItem => {
        const additionalMapObjectIdentifier = getAdditionalMapObjectIdentifier(additionalMapObjectItem);
        if (additionalMapObjectIdentifier == null || additionalMapObjectCollectionIdentifiers.includes(additionalMapObjectIdentifier)) {
          return false;
        }
        additionalMapObjectCollectionIdentifiers.push(additionalMapObjectIdentifier);
        return true;
      });
      return [...additionalMapObjectsToAdd, ...additionalMapObjectCollection];
    }
    return additionalMapObjectCollection;
  }
}
