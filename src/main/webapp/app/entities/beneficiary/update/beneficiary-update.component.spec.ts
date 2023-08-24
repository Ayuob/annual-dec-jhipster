import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BeneficiaryFormService } from './beneficiary-form.service';
import { BeneficiaryService } from '../service/beneficiary.service';
import { IBeneficiary } from '../beneficiary.model';
import { IFamilyMember } from 'app/entities/family-member/family-member.model';
import { FamilyMemberService } from 'app/entities/family-member/service/family-member.service';

import { BeneficiaryUpdateComponent } from './beneficiary-update.component';

describe('Beneficiary Management Update Component', () => {
  let comp: BeneficiaryUpdateComponent;
  let fixture: ComponentFixture<BeneficiaryUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let beneficiaryFormService: BeneficiaryFormService;
  let beneficiaryService: BeneficiaryService;
  let familyMemberService: FamilyMemberService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BeneficiaryUpdateComponent],
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
      .overrideTemplate(BeneficiaryUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BeneficiaryUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    beneficiaryFormService = TestBed.inject(BeneficiaryFormService);
    beneficiaryService = TestBed.inject(BeneficiaryService);
    familyMemberService = TestBed.inject(FamilyMemberService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call familyMembers query and add missing value', () => {
      const beneficiary: IBeneficiary = { id: 456 };
      const familyMembers: IFamilyMember = { id: 42767 };
      beneficiary.familyMembers = familyMembers;

      const familyMembersCollection: IFamilyMember[] = [{ id: 7022 }];
      jest.spyOn(familyMemberService, 'query').mockReturnValue(of(new HttpResponse({ body: familyMembersCollection })));
      const expectedCollection: IFamilyMember[] = [familyMembers, ...familyMembersCollection];
      jest.spyOn(familyMemberService, 'addFamilyMemberToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ beneficiary });
      comp.ngOnInit();

      expect(familyMemberService.query).toHaveBeenCalled();
      expect(familyMemberService.addFamilyMemberToCollectionIfMissing).toHaveBeenCalledWith(familyMembersCollection, familyMembers);
      expect(comp.familyMembersCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const beneficiary: IBeneficiary = { id: 456 };
      const familyMembers: IFamilyMember = { id: 3690 };
      beneficiary.familyMembers = familyMembers;

      activatedRoute.data = of({ beneficiary });
      comp.ngOnInit();

      expect(comp.familyMembersCollection).toContain(familyMembers);
      expect(comp.beneficiary).toEqual(beneficiary);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBeneficiary>>();
      const beneficiary = { id: 123 };
      jest.spyOn(beneficiaryFormService, 'getBeneficiary').mockReturnValue(beneficiary);
      jest.spyOn(beneficiaryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ beneficiary });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: beneficiary }));
      saveSubject.complete();

      // THEN
      expect(beneficiaryFormService.getBeneficiary).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(beneficiaryService.update).toHaveBeenCalledWith(expect.objectContaining(beneficiary));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBeneficiary>>();
      const beneficiary = { id: 123 };
      jest.spyOn(beneficiaryFormService, 'getBeneficiary').mockReturnValue({ id: null });
      jest.spyOn(beneficiaryService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ beneficiary: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: beneficiary }));
      saveSubject.complete();

      // THEN
      expect(beneficiaryFormService.getBeneficiary).toHaveBeenCalled();
      expect(beneficiaryService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IBeneficiary>>();
      const beneficiary = { id: 123 };
      jest.spyOn(beneficiaryService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ beneficiary });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(beneficiaryService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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
