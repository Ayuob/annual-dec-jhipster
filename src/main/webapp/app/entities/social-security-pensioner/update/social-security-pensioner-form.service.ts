import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISocialSecurityPensioner, NewSocialSecurityPensioner } from '../social-security-pensioner.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISocialSecurityPensioner for edit and NewSocialSecurityPensionerFormGroupInput for create.
 */
type SocialSecurityPensionerFormGroupInput = ISocialSecurityPensioner | PartialWithRequiredKeyOf<NewSocialSecurityPensioner>;

type SocialSecurityPensionerFormDefaults = Pick<NewSocialSecurityPensioner, 'id'>;

type SocialSecurityPensionerFormGroupContent = {
  id: FormControl<ISocialSecurityPensioner['id'] | NewSocialSecurityPensioner['id']>;
  nationalNumber: FormControl<ISocialSecurityPensioner['nationalNumber']>;
  pensionNumber: FormControl<ISocialSecurityPensioner['pensionNumber']>;
  dateOfBirth: FormControl<ISocialSecurityPensioner['dateOfBirth']>;
  address: FormControl<ISocialSecurityPensioner['address']>;
};

export type SocialSecurityPensionerFormGroup = FormGroup<SocialSecurityPensionerFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SocialSecurityPensionerFormService {
  createSocialSecurityPensionerFormGroup(
    socialSecurityPensioner: SocialSecurityPensionerFormGroupInput = { id: null }
  ): SocialSecurityPensionerFormGroup {
    const socialSecurityPensionerRawValue = {
      ...this.getFormDefaults(),
      ...socialSecurityPensioner,
    };
    return new FormGroup<SocialSecurityPensionerFormGroupContent>({
      id: new FormControl(
        { value: socialSecurityPensionerRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nationalNumber: new FormControl(socialSecurityPensionerRawValue.nationalNumber, {
        validators: [Validators.required, Validators.minLength(12), Validators.maxLength(12)],
      }),
      pensionNumber: new FormControl(socialSecurityPensionerRawValue.pensionNumber, {
        validators: [Validators.required],
      }),
      dateOfBirth: new FormControl(socialSecurityPensionerRawValue.dateOfBirth, {
        validators: [Validators.required],
      }),
      address: new FormControl(socialSecurityPensionerRawValue.address, {
        validators: [Validators.required],
      }),
    });
  }

  getSocialSecurityPensioner(form: SocialSecurityPensionerFormGroup): ISocialSecurityPensioner | NewSocialSecurityPensioner {
    return form.getRawValue() as ISocialSecurityPensioner | NewSocialSecurityPensioner;
  }

  resetForm(form: SocialSecurityPensionerFormGroup, socialSecurityPensioner: SocialSecurityPensionerFormGroupInput): void {
    const socialSecurityPensionerRawValue = { ...this.getFormDefaults(), ...socialSecurityPensioner };
    form.reset(
      {
        ...socialSecurityPensionerRawValue,
        id: { value: socialSecurityPensionerRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): SocialSecurityPensionerFormDefaults {
    return {
      id: null,
    };
  }
}
