import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITrail, Trail } from '../trail.model';
import { TrailService } from '../service/trail.service';

@Injectable({ providedIn: 'root' })
export class TrailRoutingResolveService implements Resolve<ITrail> {
  constructor(protected service: TrailService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITrail> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((trail: HttpResponse<Trail>) => {
          if (trail.body) {
            return of(trail.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Trail());
  }
}
