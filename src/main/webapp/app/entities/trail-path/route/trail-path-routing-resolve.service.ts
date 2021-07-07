import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITrailPath, TrailPath } from '../trail-path.model';
import { TrailPathService } from '../service/trail-path.service';

@Injectable({ providedIn: 'root' })
export class TrailPathRoutingResolveService implements Resolve<ITrailPath> {
  constructor(protected service: TrailPathService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITrailPath> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((trailPath: HttpResponse<TrailPath>) => {
          if (trailPath.body) {
            return of(trailPath.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new TrailPath());
  }
}
