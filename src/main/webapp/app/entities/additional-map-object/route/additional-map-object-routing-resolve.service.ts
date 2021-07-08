import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAdditionalMapObject, AdditionalMapObject } from '../additional-map-object.model';
import { AdditionalMapObjectService } from '../service/additional-map-object.service';

@Injectable({ providedIn: 'root' })
export class AdditionalMapObjectRoutingResolveService implements Resolve<IAdditionalMapObject> {
  constructor(protected service: AdditionalMapObjectService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAdditionalMapObject> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((additionalMapObject: HttpResponse<AdditionalMapObject>) => {
          if (additionalMapObject.body) {
            return of(additionalMapObject.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new AdditionalMapObject());
  }
}
