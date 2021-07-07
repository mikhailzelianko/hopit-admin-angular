import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITrailPathWaypoint, TrailPathWaypoint } from '../trail-path-waypoint.model';
import { TrailPathWaypointService } from '../service/trail-path-waypoint.service';

@Injectable({ providedIn: 'root' })
export class TrailPathWaypointRoutingResolveService implements Resolve<ITrailPathWaypoint> {
  constructor(protected service: TrailPathWaypointService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITrailPathWaypoint> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((trailPathWaypoint: HttpResponse<TrailPathWaypoint>) => {
          if (trailPathWaypoint.body) {
            return of(trailPathWaypoint.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TrailPathWaypoint());
  }
}
