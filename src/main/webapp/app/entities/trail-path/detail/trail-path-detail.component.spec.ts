import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TrailPathDetailComponent } from './trail-path-detail.component';

describe('Component Tests', () => {
  describe('TrailPath Management Detail Component', () => {
    let comp: TrailPathDetailComponent;
    let fixture: ComponentFixture<TrailPathDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [TrailPathDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ trailPath: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(TrailPathDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TrailPathDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load trailPath on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.trailPath).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
