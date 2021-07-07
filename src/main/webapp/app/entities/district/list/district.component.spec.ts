import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DistrictService } from '../service/district.service';

import { DistrictComponent } from './district.component';

describe('Component Tests', () => {
  describe('District Management Component', () => {
    let comp: DistrictComponent;
    let fixture: ComponentFixture<DistrictComponent>;
    let service: DistrictService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DistrictComponent],
      })
        .overrideTemplate(DistrictComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DistrictComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(DistrictService);

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
      expect(comp.districts?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
