import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AdditionalMapObjectDetailComponent } from './additional-map-object-detail.component';

describe('Component Tests', () => {
  describe('AdditionalMapObject Management Detail Component', () => {
    let comp: AdditionalMapObjectDetailComponent;
    let fixture: ComponentFixture<AdditionalMapObjectDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [AdditionalMapObjectDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ additionalMapObject: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(AdditionalMapObjectDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(AdditionalMapObjectDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load additionalMapObject on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.additionalMapObject).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
