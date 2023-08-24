import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SocialSecurityPensionerFormService } from './social-security-pensioner-form.service';
import { SocialSecurityPensionerService } from '../service/social-security-pensioner.service';
import { ISocialSecurityPensioner } from '../social-security-pensioner.model';

import { SocialSecurityPensionerUpdateComponent } from './social-security-pensioner-update.component';

describe('SocialSecurityPensioner Management Update Component', () => {
  let comp: SocialSecurityPensionerUpdateComponent;
  let fixture: ComponentFixture<SocialSecurityPensionerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let socialSecurityPensionerFormService: SocialSecurityPensionerFormService;
  let socialSecurityPensionerService: SocialSecurityPensionerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SocialSecurityPensionerUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(SocialSecurityPensionerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SocialSecurityPensionerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    socialSecurityPensionerFormService = TestBed.inject(SocialSecurityPensionerFormService);
    socialSecurityPensionerService = TestBed.inject(SocialSecurityPensionerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const socialSecurityPensioner: ISocialSecurityPensioner = { id: 456 };

      activatedRoute.data = of({ socialSecurityPensioner });
      comp.ngOnInit();

      expect(comp.socialSecurityPensioner).toEqual(socialSecurityPensioner);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISocialSecurityPensioner>>();
      const socialSecurityPensioner = { id: 123 };
      jest.spyOn(socialSecurityPensionerFormService, 'getSocialSecurityPensioner').mockReturnValue(socialSecurityPensioner);
      jest.spyOn(socialSecurityPensionerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ socialSecurityPensioner });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: socialSecurityPensioner }));
      saveSubject.complete();

      // THEN
      expect(socialSecurityPensionerFormService.getSocialSecurityPensioner).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(socialSecurityPensionerService.update).toHaveBeenCalledWith(expect.objectContaining(socialSecurityPensioner));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISocialSecurityPensioner>>();
      const socialSecurityPensioner = { id: 123 };
      jest.spyOn(socialSecurityPensionerFormService, 'getSocialSecurityPensioner').mockReturnValue({ id: null });
      jest.spyOn(socialSecurityPensionerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ socialSecurityPensioner: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: socialSecurityPensioner }));
      saveSubject.complete();

      // THEN
      expect(socialSecurityPensionerFormService.getSocialSecurityPensioner).toHaveBeenCalled();
      expect(socialSecurityPensionerService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISocialSecurityPensioner>>();
      const socialSecurityPensioner = { id: 123 };
      jest.spyOn(socialSecurityPensionerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ socialSecurityPensioner });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(socialSecurityPensionerService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
