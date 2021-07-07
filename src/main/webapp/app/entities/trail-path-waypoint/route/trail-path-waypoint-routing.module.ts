import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TrailPathWaypointComponent } from '../list/trail-path-waypoint.component';
import { TrailPathWaypointDetailComponent } from '../detail/trail-path-waypoint-detail.component';
import { TrailPathWaypointUpdateComponent } from '../update/trail-path-waypoint-update.component';
import { TrailPathWaypointRoutingResolveService } from './trail-path-waypoint-routing-resolve.service';

const trailPathWaypointRoute: Routes = [
  {
    path: '',
    component: TrailPathWaypointComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TrailPathWaypointDetailComponent,
    resolve: {
      trailPathWaypoint: TrailPathWaypointRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TrailPathWaypointUpdateComponent,
    resolve: {
      trailPathWaypoint: TrailPathWaypointRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TrailPathWaypointUpdateComponent,
    resolve: {
      trailPathWaypoint: TrailPathWaypointRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(trailPathWaypointRoute)],
  exports: [RouterModule],
})
export class TrailPathWaypointRoutingModule {}
