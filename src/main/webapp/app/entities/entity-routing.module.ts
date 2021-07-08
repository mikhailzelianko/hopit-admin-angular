import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'district',
        data: { pageTitle: 'hopitAdminApp.district.home.title' },
        loadChildren: () => import('./district/district.module').then(m => m.DistrictModule),
      },
      {
        path: 'region',
        data: { pageTitle: 'hopitAdminApp.region.home.title' },
        loadChildren: () => import('./region/region.module').then(m => m.RegionModule),
      },
      {
        path: 'country',
        data: { pageTitle: 'hopitAdminApp.country.home.title' },
        loadChildren: () => import('./country/country.module').then(m => m.CountryModule),
      },
      {
        path: 'trail',
        data: { pageTitle: 'hopitAdminApp.trail.home.title' },
        loadChildren: () => import('./trail/trail.module').then(m => m.TrailModule),
      },
      {
        path: 'trail-path',
        data: { pageTitle: 'hopitAdminApp.trailPath.home.title' },
        loadChildren: () => import('./trail-path/trail-path.module').then(m => m.TrailPathModule),
      },
      {
        path: 'trail-path-waypoint',
        data: { pageTitle: 'hopitAdminApp.trailPathWaypoint.home.title' },
        loadChildren: () => import('./trail-path-waypoint/trail-path-waypoint.module').then(m => m.TrailPathWaypointModule),
      },
      {
        path: 'additional-map-object',
        data: { pageTitle: 'hopitAdminApp.additionalMapObject.home.title' },
        loadChildren: () => import('./additional-map-object/additional-map-object.module').then(m => m.AdditionalMapObjectModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
