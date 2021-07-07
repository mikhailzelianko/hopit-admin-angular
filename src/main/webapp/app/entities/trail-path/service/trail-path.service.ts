import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITrailPath, getTrailPathIdentifier } from '../trail-path.model';

export type EntityResponseType = HttpResponse<ITrailPath>;
export type EntityArrayResponseType = HttpResponse<ITrailPath[]>;

@Injectable({ providedIn: 'root' })
export class TrailPathService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/trail-paths');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(trailPath: ITrailPath): Observable<EntityResponseType> {
    return this.http.post<ITrailPath>(this.resourceUrl, trailPath, { observe: 'response' });
  }

  update(trailPath: ITrailPath): Observable<EntityResponseType> {
    return this.http.put<ITrailPath>(`${this.resourceUrl}/${getTrailPathIdentifier(trailPath) as number}`, trailPath, {
      observe: 'response',
    });
  }

  partialUpdate(trailPath: ITrailPath): Observable<EntityResponseType> {
    return this.http.patch<ITrailPath>(`${this.resourceUrl}/${getTrailPathIdentifier(trailPath) as number}`, trailPath, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITrailPath>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITrailPath[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTrailPathToCollectionIfMissing(
    trailPathCollection: ITrailPath[],
    ...trailPathsToCheck: (ITrailPath | null | undefined)[]
  ): ITrailPath[] {
    const trailPaths: ITrailPath[] = trailPathsToCheck.filter(isPresent);
    if (trailPaths.length > 0) {
      const trailPathCollectionIdentifiers = trailPathCollection.map(trailPathItem => getTrailPathIdentifier(trailPathItem)!);
      const trailPathsToAdd = trailPaths.filter(trailPathItem => {
        const trailPathIdentifier = getTrailPathIdentifier(trailPathItem);
        if (trailPathIdentifier == null || trailPathCollectionIdentifiers.includes(trailPathIdentifier)) {
          return false;
        }
        trailPathCollectionIdentifiers.push(trailPathIdentifier);
        return true;
      });
      return [...trailPathsToAdd, ...trailPathCollection];
    }
    return trailPathCollection;
  }
}
