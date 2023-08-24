import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AnnualDeclarationDetailComponent } from './annual-declaration-detail.component';

describe('AnnualDeclaration Management Detail Component', () => {
  let comp: AnnualDeclarationDetailComponent;
  let fixture: ComponentFixture<AnnualDeclarationDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AnnualDeclarationDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ annualDeclaration: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AnnualDeclarationDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AnnualDeclarationDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load annualDeclaration on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.annualDeclaration).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
