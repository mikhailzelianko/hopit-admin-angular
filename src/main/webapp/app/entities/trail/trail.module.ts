import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TrailComponent } from './list/trail.component';
import { TrailDetailComponent } from './detail/trail-detail.component';
import { TrailUpdateComponent } from './update/trail-update.component';
import { TrailDeleteDialogComponent } from './delete/trail-delete-dialog.component';
import { TrailRoutingModule } from './route/trail-routing.module';

@NgModule({
  imports: [SharedModule, TrailRoutingModule],
  declarations: [TrailComponent, TrailDetailComponent, TrailUpdateComponent, TrailDeleteDialogComponent],
  entryComponents: [TrailDeleteDialogComponent],
})
export class TrailModule {}
