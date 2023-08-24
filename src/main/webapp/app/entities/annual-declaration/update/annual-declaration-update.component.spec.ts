import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AnnualDeclarationFormService } from './annual-declaration-form.service';
import { AnnualDeclarationService } from '../service/annual-declaration.service';
import { IAnnualDeclaration } from '../annual-declaration.model';
import { ISocialSecurityPensioner } from 'app/entities/social-security-pensioner/social-security-pensioner.model';
import { SocialSecurityPensionerService } from 'app/entities/social-security-pensioner/service/social-security-pensioner.service';
import { IFamilyMember } from 'app/entities/family-member/family-member.model';
import { FamilyMemberService } from 'app/entities/family-member/service/family-member.service';

import { AnnualDeclarationUpdateComponent } from './annual-declaration-update.component';

describe('AnnualDeclaration Management Update Component', () => {
  let comp: AnnualDeclarationUpdateComponent;
  let fixture: ComponentFixture<AnnualDeclarationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let annualDeclarationFormService: AnnualDeclarationFormService;
  let annualDeclarationService: AnnualDeclarationService;
  let socialSecurityPensionerService: SocialSecurityPensionerService;
  let familyMemberService: FamilyMemberService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AnnualDeclarationUpdateComponent],
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
      .overrideTemplate(AnnualDeclarationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AnnualDeclarationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    annualDeclarationFormService = TestBed.inject(AnnualDeclarationFormService);
    annualDeclarationService = TestBed.inject(AnnualDeclarationService);
    socialSecurityPensionerService = TestBed.inject(SocialSecurityPensionerService);
    familyMemberService = TestBed.inject(FamilyMemberService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call SocialSecurityPensioner query and add missing value', () => {
      const annualDeclaration: IAnnualDeclaration = { id: 456 };
      const pensioner: ISocialSecurityPensioner = { id: 33593 };
      annualDeclaration.pensioner = pensioner;

      const socialSecurityPensionerCollection: ISocialSecurityPensioner[] = [{ id: 70971 }];
      jest
        .spyOn(socialSecurityPensionerService, 'query')
        .mockReturnValue(of(new HttpResponse({ body: socialSecurityPensionerCollection })));
      const additionalSocialSecurityPensioners = [pensioner];
      const expectedCollection: ISocialSecurityPensioner[] = [...additionalSocialSecurityPensioners, ...socialSecurityPensionerCollection];
      jest.spyOn(socialSecurityPensionerService, 'addSocialSecurityPensionerToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ annualDeclaration });
      comp.ngOnInit();

      expect(socialSecurityPensionerService.query).toHaveBeenCalled();
      expect(socialSecurityPensionerService.addSocialSecurityPensionerToCollectionIfMissing).toHaveBeenCalledWith(
        socialSecurityPensionerCollection,
        ...additionalSocialSecurityPensioners.map(expect.objectContaining)
      );
      expect(comp.socialSecurityPensionersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call FamilyMember query and add missing value', () => {
      const annualDeclaration: IAnnualDeclaration = { id: 456 };
      const familyMembers: IFamilyMember[] = [{ id: 89143 }];
      annualDeclaration.familyMembers = familyMembers;

      const familyMemberCollection: IFamilyMember[] = [{ id: 86828 }];
      jest.spyOn(familyMemberService, 'query').mockReturnValue(of(new HttpResponse({ body: familyMemberCollection })));
      const additionalFamilyMembers = [...familyMembers];
      const expectedCollection: IFamilyMember[] = [...additionalFamilyMembers, ...familyMemberCollection];
      jest.spyOn(familyMemberService, 'addFamilyMemberToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ annualDeclaration });
      comp.ngOnInit();

      expect(familyMemberService.query).toHaveBeenCalled();
      expect(familyMemberService.addFamilyMemberToCollectionIfMissing).toHaveBeenCalledWith(
        familyMemberCollection,
        ...additionalFamilyMembers.map(expect.objectContaining)
      );
      expect(comp.familyMembersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const annualDeclaration: IAnnualDeclaration = { id: 456 };
      const pensioner: ISocialSecurityPensioner = { id: 80103 };
      annualDeclaration.pensioner = pensioner;
      const familyMembers: IFamilyMember = { id: 29377 };
      annualDeclaration.familyMembers = [familyMembers];

      activatedRoute.data = of({ annualDeclaration });
      comp.ngOnInit();

      expect(comp.socialSecurityPensionersSharedCollection).toContain(pensioner);
      expect(comp.familyMembersSharedCollection).toContain(familyMembers);
      expect(comp.annualDeclaration).toEqual(annualDeclaration);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAnnualDeclaration>>();
      const annualDeclaration = { id: 123 };
      jest.spyOn(annualDeclarationFormService, 'getAnnualDeclaration').mockReturnValue(annualDeclaration);
      jest.spyOn(annualDeclarationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ annualDeclaration });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: annualDeclaration }));
      saveSubject.complete();

      // THEN
      expect(annualDeclarationFormService.getAnnualDeclaration).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(annualDeclarationService.update).toHaveBeenCalledWith(expect.objectContaining(annualDeclaration));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAnnualDeclaration>>();
      const annualDeclaration = { id: 123 };
      jest.spyOn(annualDeclarationFormService, 'getAnnualDeclaration').mockReturnValue({ id: null });
      jest.spyOn(annualDeclarationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ annualDeclaration: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: annualDeclaration }));
      saveSubject.complete();

      // THEN
      expect(annualDeclarationFormService.getAnnualDeclaration).toHaveBeenCalled();
      expect(annualDeclarationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAnnualDeclaration>>();
      const annualDeclaration = { id: 123 };
      jest.spyOn(annualDeclarationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ annualDeclaration });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(annualDeclarationService.update).toHaveBeenCalled();
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

    describe('compareFamilyMember', () => {
      it('Should forward to familyMemberService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(familyMemberService, 'compareFamilyMember');
        comp.compareFamilyMember(entity, entity2);
        expect(familyMemberService.compareFamilyMember).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
