import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITrail, getTrailIdentifier } from '../trail.model';

export type EntityResponseType = HttpResponse<ITrail>;
export type EntityArrayResponseType = HttpResponse<ITrail[]>;

@Injectable({ providedIn: 'root' })
export class TrailService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/trails');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(trail: ITrail): Observable<EntityResponseType> {
    return this.http.post<ITrail>(this.resourceUrl, trail, { observe: 'response' });
  }

  update(trail: ITrail): Observable<EntityResponseType> {
    return this.http.put<ITrail>(`${this.resourceUrl}/${getTrailIdentifier(trail) as number}`, trail, { observe: 'response' });
  }

  partialUpdate(trail: ITrail): Observable<EntityResponseType> {
    return this.http.patch<ITrail>(`${this.resourceUrl}/${getTrailIdentifier(trail) as number}`, trail, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITrail>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITrail[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTrailToCollectionIfMissing(trailCollection: ITrail[], ...trailsToCheck: (ITrail | null | undefined)[]): ITrail[] {
    const trails: ITrail[] = trailsToCheck.filter(isPresent);
    if (trails.length > 0) {
      const trailCollectionIdentifiers = trailCollection.map(trailItem => getTrailIdentifier(trailItem)!);
      const trailsToAdd = trails.filter(trailItem => {
        const trailIdentifier = getTrailIdentifier(trailItem);
        if (trailIdentifier == null || trailCollectionIdentifiers.includes(trailIdentifier)) {
          return false;
        }
        trailCollectionIdentifiers.push(trailIdentifier);
        return true;
      });
      return [...trailsToAdd, ...trailCollection];
    }
    return trailCollection;
  }
}
