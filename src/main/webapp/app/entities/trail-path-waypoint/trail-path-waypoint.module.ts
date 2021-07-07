import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TrailPathWaypointComponent } from './list/trail-path-waypoint.component';
import { TrailPathWaypointDetailComponent } from './detail/trail-path-waypoint-detail.component';
import { TrailPathWaypointUpdateComponent } from './update/trail-path-waypoint-update.component';
import { TrailPathWaypointDeleteDialogComponent } from './delete/trail-path-waypoint-delete-dialog.component';
import { TrailPathWaypointRoutingModule } from './route/trail-path-waypoint-routing.module';

@NgModule({
  imports: [SharedModule, TrailPathWaypointRoutingModule],
  declarations: [
    TrailPathWaypointComponent,
    TrailPathWaypointDetailComponent,
    TrailPathWaypointUpdateComponent,
    TrailPathWaypointDeleteDialogComponent,
  ],
  entryComponents: [TrailPathWaypointDeleteDialogComponent],
})
export class TrailPathWaypointModule {}
