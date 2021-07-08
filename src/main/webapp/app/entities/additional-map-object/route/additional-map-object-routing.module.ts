import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AdditionalMapObjectComponent } from '../list/additional-map-object.component';
import { AdditionalMapObjectDetailComponent } from '../detail/additional-map-object-detail.component';
import { AdditionalMapObjectUpdateComponent } from '../update/additional-map-object-update.component';
import { AdditionalMapObjectRoutingResolveService } from './additional-map-object-routing-resolve.service';

const additionalMapObjectRoute: Routes = [
  {
    path: '',
    component: AdditionalMapObjectComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AdditionalMapObjectDetailComponent,
    resolve: {
      additionalMapObject: AdditionalMapObjectRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AdditionalMapObjectUpdateComponent,
    resolve: {
      additionalMapObject: AdditionalMapObjectRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AdditionalMapObjectUpdateComponent,
    resolve: {
      additionalMapObject: AdditionalMapObjectRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(additionalMapObjectRoute)],
  exports: [RouterModule],
})
export class AdditionalMapObjectRoutingModule {}
