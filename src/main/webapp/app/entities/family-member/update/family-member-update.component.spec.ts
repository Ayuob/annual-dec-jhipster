import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FamilyMemberFormService } from './family-member-form.service';
import { FamilyMemberService } from '../service/family-member.service';
import { IFamilyMember } from '../family-member.model';
import { ISocialSecurityPensioner } from 'app/entities/social-security-pensioner/social-security-pensioner.model';
import { SocialSecurityPensionerService } from 'app/entities/social-security-pensioner/service/social-security-pensioner.service';

import { FamilyMemberUpdateComponent } from './family-member-update.component';

describe('FamilyMember Management Update Component', () => {
  let comp: FamilyMemberUpdateComponent;
  let fixture: ComponentFixture<FamilyMemberUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let familyMemberFormService: FamilyMemberFormService;
  let familyMemberService: FamilyMemberService;
  let socialSecurityPensionerService: SocialSecurityPensionerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FamilyMemberUpdateComponent],
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
      .overrideTemplate(FamilyMemberUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FamilyMemberUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    familyMemberFormService = TestBed.inject(FamilyMemberFormService);
    familyMemberService = TestBed.inject(FamilyMemberService);
    socialSecurityPensionerService = TestBed.inject(SocialSecurityPensionerService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call SocialSecurityPensioner query and add missing value', () => {
      const familyMember: IFamilyMember = { id: 456 };
      const pensioner: ISocialSecurityPensioner = { id: 48664 };
      familyMember.pensioner = pensioner;

      const socialSecurityPensionerCollection: ISocialSecurityPensioner[] = [{ id: 66086 }];
      jest
        .spyOn(socialSecurityPensionerService, 'query')
        .mockReturnValue(of(new HttpResponse({ body: socialSecurityPensionerCollection })));
      const additionalSocialSecurityPensioners = [pensioner];
      const expectedCollection: ISocialSecurityPensioner[] = [...additionalSocialSecurityPensioners, ...socialSecurityPensionerCollection];
      jest.spyOn(socialSecurityPensionerService, 'addSocialSecurityPensionerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ familyMember });
      comp.ngOnInit();

      expect(socialSecurityPensionerService.query).toHaveBeenCalled();
      expect(socialSecurityPensionerService.addSocialSecurityPensionerToCollectionIfMissing).toHaveBeenCalledWith(
        socialSecurityPensionerCollection,
        ...additionalSocialSecurityPensioners.map(expect.objectContaining)
      );
      expect(comp.socialSecurityPensionersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const familyMember: IFamilyMember = { id: 456 };
      const pensioner: ISocialSecurityPensioner = { id: 18422 };
      familyMember.pensioner = pensioner;

      activatedRoute.data = of({ familyMember });
      comp.ngOnInit();

      expect(comp.socialSecurityPensionersSharedCollection).toContain(pensioner);
      expect(comp.familyMember).toEqual(familyMember);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFamilyMember>>();
      const familyMember = { id: 123 };
      jest.spyOn(familyMemberFormService, 'getFamilyMember').mockReturnValue(familyMember);
      jest.spyOn(familyMemberService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ familyMember });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: familyMember }));
      saveSubject.complete();

      // THEN
      expect(familyMemberFormService.getFamilyMember).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(familyMemberService.update).toHaveBeenCalledWith(expect.objectContaining(familyMember));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFamilyMember>>();
      const familyMember = { id: 123 };
      jest.spyOn(familyMemberFormService, 'getFamilyMember').mockReturnValue({ id: null });
      jest.spyOn(familyMemberService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ familyMember: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: familyMember }));
      saveSubject.complete();

      // THEN
      expect(familyMemberFormService.getFamilyMember).toHaveBeenCalled();
      expect(familyMemberService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFamilyMember>>();
      const familyMember = { id: 123 };
      jest.spyOn(familyMemberService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ familyMember });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(familyMemberService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareSocialSecurityPensioner', () => {
      it('Should forward to socialSecurityPensionerService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(socialSecurityPensionerService, 'compareSocialSecurityPensioner');
        comp.compareSocialSecurityPensioner(entity, entity2);
        expect(socialSecurityPensionerService.compareSocialSecurityPensioner).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
