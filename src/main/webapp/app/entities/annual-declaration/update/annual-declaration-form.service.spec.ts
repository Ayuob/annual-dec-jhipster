import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../annual-declaration.test-samples';

import { AnnualDeclarationFormService } from './annual-declaration-form.service';

describe('AnnualDeclaration Form Service', () => {
  let service: AnnualDeclarationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AnnualDeclarationFormService);
  });

  describe('Service methods', () => {
    describe('createAnnualDeclarationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAnnualDeclarationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            submissionDate: expect.any(Object),
            status: expect.any(Object),
            pensioner: expect.any(Object),
            familyMembers: expect.any(Object),
          })
        );
      });

      it('passing IAnnualDeclaration should create a new form with FormGroup', () => {
        const formGroup = service.createAnnualDeclarationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            submissionDate: expect.any(Object),
            status: expect.any(Object),
            pensioner: expect.any(Object),
            familyMembers: expect.any(Object),
          })
        );
      });
    });

    describe('getAnnualDeclaration', () => {
      it('should return NewAnnualDeclaration for default AnnualDeclaration initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createAnnualDeclarationFormGroup(sampleWithNewData);

        const annualDeclaration = service.getAnnualDeclaration(formGroup) as any;

        expect(annualDeclaration).toMatchObject(sampleWithNewData);
      });

      it('should return NewAnnualDeclaration for empty AnnualDeclaration initial value', () => {
        const formGroup = service.createAnnualDeclarationFormGroup();

        const annualDeclaration = service.getAnnualDeclaration(formGroup) as any;

        expect(annualDeclaration).toMatchObject({});
      });

      it('should return IAnnualDeclaration', () => {
        const formGroup = service.createAnnualDeclarationFormGroup(sampleWithRequiredData);

        const annualDeclaration = service.getAnnualDeclaration(formGroup) as any;

        expect(annualDeclaration).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAnnualDeclaration should not enable id FormControl', () => {
        const formGroup = service.createAnnualDeclarationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAnnualDeclaration should disable id FormControl', () => {
        const formGroup = service.createAnnualDeclarationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
