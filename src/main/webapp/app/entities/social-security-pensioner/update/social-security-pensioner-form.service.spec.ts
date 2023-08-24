import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../social-security-pensioner.test-samples';

import { SocialSecurityPensionerFormService } from './social-security-pensioner-form.service';

describe('SocialSecurityPensioner Form Service', () => {
  let service: SocialSecurityPensionerFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SocialSecurityPensionerFormService);
  });

  describe('Service methods', () => {
    describe('createSocialSecurityPensionerFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSocialSecurityPensionerFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nationalNumber: expect.any(Object),
            pensionNumber: expect.any(Object),
            dateOfBirth: expect.any(Object),
            address: expect.any(Object),
          })
        );
      });

      it('passing ISocialSecurityPensioner should create a new form with FormGroup', () => {
        const formGroup = service.createSocialSecurityPensionerFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nationalNumber: expect.any(Object),
            pensionNumber: expect.any(Object),
            dateOfBirth: expect.any(Object),
            address: expect.any(Object),
          })
        );
      });
    });

    describe('getSocialSecurityPensioner', () => {
      it('should return NewSocialSecurityPensioner for default SocialSecurityPensioner initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createSocialSecurityPensionerFormGroup(sampleWithNewData);

        const socialSecurityPensioner = service.getSocialSecurityPensioner(formGroup) as any;

        expect(socialSecurityPensioner).toMatchObject(sampleWithNewData);
      });

      it('should return NewSocialSecurityPensioner for empty SocialSecurityPensioner initial value', () => {
        const formGroup = service.createSocialSecurityPensionerFormGroup();

        const socialSecurityPensioner = service.getSocialSecurityPensioner(formGroup) as any;

        expect(socialSecurityPensioner).toMatchObject({});
      });

      it('should return ISocialSecurityPensioner', () => {
        const formGroup = service.createSocialSecurityPensionerFormGroup(sampleWithRequiredData);

        const socialSecurityPensioner = service.getSocialSecurityPensioner(formGroup) as any;

        expect(socialSecurityPensioner).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISocialSecurityPensioner should not enable id FormControl', () => {
        const formGroup = service.createSocialSecurityPensionerFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSocialSecurityPensioner should disable id FormControl', () => {
        const formGroup = service.createSocialSecurityPensionerFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
