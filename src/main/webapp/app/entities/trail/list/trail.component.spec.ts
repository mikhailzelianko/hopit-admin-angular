import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { TrailService } from '../service/trail.service';

import { TrailComponent } from './trail.component';

describe('Component Tests', () => {
  describe('Trail Management Component', () => {
    let comp: TrailComponent;
    let fixture: ComponentFixture<TrailComponent>;
    let service: TrailService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TrailComponent],
      })
        .overrideTemplate(TrailComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TrailComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(TrailService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.trails?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
