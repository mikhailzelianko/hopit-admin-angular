import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { TrailPathService } from '../service/trail-path.service';

import { TrailPathComponent } from './trail-path.component';

describe('Component Tests', () => {
  describe('TrailPath Management Component', () => {
    let comp: TrailPathComponent;
    let fixture: ComponentFixture<TrailPathComponent>;
    let service: TrailPathService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TrailPathComponent],
      })
        .overrideTemplate(TrailPathComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TrailPathComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(TrailPathService);

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
      expect(comp.trailPaths?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
