import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TrailPathComponent } from './list/trail-path.component';
import { TrailPathDetailComponent } from './detail/trail-path-detail.component';
import { TrailPathUpdateComponent } from './update/trail-path-update.component';
import { TrailPathDeleteDialogComponent } from './delete/trail-path-delete-dialog.component';
import { TrailPathRoutingModule } from './route/trail-path-routing.module';

@NgModule({
  imports: [SharedModule, TrailPathRoutingModule],
  declarations: [TrailPathComponent, TrailPathDetailComponent, TrailPathUpdateComponent, TrailPathDeleteDialogComponent],
  entryComponents: [TrailPathDeleteDialogComponent],
})
export class TrailPathModule {}
