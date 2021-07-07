import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITrailPathWaypoint, getTrailPathWaypointIdentifier } from '../trail-path-waypoint.model';

export type EntityResponseType = HttpResponse<ITrailPathWaypoint>;
export type EntityArrayResponseType = HttpResponse<ITrailPathWaypoint[]>;

@Injectable({ providedIn: 'root' })
export class TrailPathWaypointService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/trail-path-waypoints');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(trailPathWaypoint: ITrailPathWaypoint): Observable<EntityResponseType> {
    return this.http.post<ITrailPathWaypoint>(this.resourceUrl, trailPathWaypoint, { observe: 'response' });
  }

  update(trailPathWaypoint: ITrailPathWaypoint): Observable<EntityResponseType> {
    return this.http.put<ITrailPathWaypoint>(
      `${this.resourceUrl}/${getTrailPathWaypointIdentifier(trailPathWaypoint) as number}`,
      trailPathWaypoint,
      { observe: 'response' }
    );
  }

  partialUpdate(trailPathWaypoint: ITrailPathWaypoint): Observable<EntityResponseType> {
    return this.http.patch<ITrailPathWaypoint>(
      `${this.resourceUrl}/${getTrailPathWaypointIdentifier(trailPathWaypoint) as number}`,
      trailPathWaypoint,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITrailPathWaypoint>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITrailPathWaypoint[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addTrailPathWaypointToCollectionIfMissing(
    trailPathWaypointCollection: ITrailPathWaypoint[],
    ...trailPathWaypointsToCheck: (ITrailPathWaypoint | null | undefined)[]
  ): ITrailPathWaypoint[] {
    const trailPathWaypoints: ITrailPathWaypoint[] = trailPathWaypointsToCheck.filter(isPresent);
    if (trailPathWaypoints.length > 0) {
      const trailPathWaypointCollectionIdentifiers = trailPathWaypointCollection.map(
        trailPathWaypointItem => getTrailPathWaypointIdentifier(trailPathWaypointItem)!
      );
      const trailPathWaypointsToAdd = trailPathWaypoints.filter(trailPathWaypointItem => {
        const trailPathWaypointIdentifier = getTrailPathWaypointIdentifier(trailPathWaypointItem);
        if (trailPathWaypointIdentifier == null || trailPathWaypointCollectionIdentifiers.includes(trailPathWaypointIdentifier)) {
          return false;
        }
        trailPathWaypointCollectionIdentifiers.push(trailPathWaypointIdentifier);
        return true;
      });
      return [...trailPathWaypointsToAdd, ...trailPathWaypointCollection];
    }
    return trailPathWaypointCollection;
  }
}
