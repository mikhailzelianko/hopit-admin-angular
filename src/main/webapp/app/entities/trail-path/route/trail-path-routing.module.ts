import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TrailPathComponent } from '../list/trail-path.component';
import { TrailPathDetailComponent } from '../detail/trail-path-detail.component';
import { TrailPathUpdateComponent } from '../update/trail-path-update.component';
import { TrailPathRoutingResolveService } from './trail-path-routing-resolve.service';

const trailPathRoute: Routes = [
  {
    path: '',
    component: TrailPathComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TrailPathDetailComponent,
    resolve: {
      trailPath: TrailPathRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TrailPathUpdateComponent,
    resolve: {
      trailPath: TrailPathRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TrailPathUpdateComponent,
    resolve: {
      trailPath: TrailPathRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(trailPathRoute)],
  exports: [RouterModule],
})
export class TrailPathRoutingModule {}
