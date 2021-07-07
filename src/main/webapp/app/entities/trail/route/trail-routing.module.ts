import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TrailComponent } from '../list/trail.component';
import { TrailDetailComponent } from '../detail/trail-detail.component';
import { TrailUpdateComponent } from '../update/trail-update.component';
import { TrailRoutingResolveService } from './trail-routing-resolve.service';

const trailRoute: Routes = [
  {
    path: '',
    component: TrailComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TrailDetailComponent,
    resolve: {
      trail: TrailRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TrailUpdateComponent,
    resolve: {
      trail: TrailRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TrailUpdateComponent,
    resolve: {
      trail: TrailRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(trailRoute)],
  exports: [RouterModule],
})
export class TrailRoutingModule {}
