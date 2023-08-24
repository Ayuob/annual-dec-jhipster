import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SocialSecurityPensionerDetailComponent } from './social-security-pensioner-detail.component';

describe('SocialSecurityPensioner Management Detail Component', () => {
  let comp: SocialSecurityPensionerDetailComponent;
  let fixture: ComponentFixture<SocialSecurityPensionerDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SocialSecurityPensionerDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ socialSecurityPensioner: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SocialSecurityPensionerDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SocialSecurityPensionerDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load socialSecurityPensioner on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.socialSecurityPensioner).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
