import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BeneficiaryDetailComponent } from './beneficiary-detail.component';

describe('Beneficiary Management Detail Component', () => {
  let comp: BeneficiaryDetailComponent;
  let fixture: ComponentFixture<BeneficiaryDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BeneficiaryDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ beneficiary: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(BeneficiaryDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(BeneficiaryDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load beneficiary on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.beneficiary).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
