import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TrailDetailComponent } from './trail-detail.component';

describe('Component Tests', () => {
  describe('Trail Management Detail Component', () => {
    let comp: TrailDetailComponent;
    let fixture: ComponentFixture<TrailDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [TrailDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ trail: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(TrailDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TrailDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load trail on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.trail).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
