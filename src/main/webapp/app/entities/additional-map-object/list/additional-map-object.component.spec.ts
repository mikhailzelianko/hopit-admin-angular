import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { AdditionalMapObjectService } from '../service/additional-map-object.service';

import { AdditionalMapObjectComponent } from './additional-map-object.component';

describe('Component Tests', () => {
  describe('AdditionalMapObject Management Component', () => {
    let comp: AdditionalMapObjectComponent;
    let fixture: ComponentFixture<AdditionalMapObjectComponent>;
    let service: AdditionalMapObjectService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [AdditionalMapObjectComponent],
      })
        .overrideTemplate(AdditionalMapObjectComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(AdditionalMapObjectComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(AdditionalMapObjectService);

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
      expect(comp.additionalMapObjects?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
