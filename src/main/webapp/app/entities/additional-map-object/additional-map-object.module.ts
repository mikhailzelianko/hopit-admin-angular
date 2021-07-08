import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AdditionalMapObjectComponent } from './list/additional-map-object.component';
import { AdditionalMapObjectDetailComponent } from './detail/additional-map-object-detail.component';
import { AdditionalMapObjectUpdateComponent } from './update/additional-map-object-update.component';
import { AdditionalMapObjectDeleteDialogComponent } from './delete/additional-map-object-delete-dialog.component';
import { AdditionalMapObjectRoutingModule } from './route/additional-map-object-routing.module';

@NgModule({
  imports: [SharedModule, AdditionalMapObjectRoutingModule],
  declarations: [
    AdditionalMapObjectComponent,
    AdditionalMapObjectDetailComponent,
    AdditionalMapObjectUpdateComponent,
    AdditionalMapObjectDeleteDialogComponent,
  ],
  entryComponents: [AdditionalMapObjectDeleteDialogComponent],
})
export class AdditionalMapObjectModule {}
