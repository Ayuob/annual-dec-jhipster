import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IBeneficiary, NewBeneficiary } from '../beneficiary.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IBeneficiary for edit and NewBeneficiaryFormGroupInput for create.
 */
type BeneficiaryFormGroupInput = IBeneficiary | PartialWithRequiredKeyOf<NewBeneficiary>;

type BeneficiaryFormDefaults = Pick<NewBeneficiary, 'id'>;

type BeneficiaryFormGroupContent = {
  id: FormControl<IBeneficiary['id'] | NewBeneficiary['id']>;
  entitlementType: FormControl<IBeneficiary['entitlementType']>;
  entitlementDetails: FormControl<IBeneficiary['entitlementDetails']>;
  familyMembers: FormControl<IBeneficiary['familyMembers']>;
  annualDeclaration: FormControl<IBeneficiary['annualDeclaration']>;
};

export type BeneficiaryFormGroup = FormGroup<BeneficiaryFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class BeneficiaryFormService {
  createBeneficiaryFormGroup(beneficiary: BeneficiaryFormGroupInput = { id: null }): BeneficiaryFormGroup {
    const beneficiaryRawValue = {
      ...this.getFormDefaults(),
      ...beneficiary,
    };
    return new FormGroup<BeneficiaryFormGroupContent>({
      id: new FormControl(
        { value: beneficiaryRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      entitlementType: new FormControl(beneficiaryRawValue.entitlementType, {
        validators: [Validators.required],
      }),
      entitlementDetails: new FormControl(beneficiaryRawValue.entitlementDetails),
      familyMembers: new FormControl(beneficiaryRawValue.familyMembers),
      annualDeclaration: new FormControl(beneficiaryRawValue.annualDeclaration),
    });
  }

  getBeneficiary(form: BeneficiaryFormGroup): IBeneficiary | NewBeneficiary {
    return form.getRawValue() as IBeneficiary | NewBeneficiary;
  }

  resetForm(form: BeneficiaryFormGroup, beneficiary: BeneficiaryFormGroupInput): void {
    const beneficiaryRawValue = { ...this.getFormDefaults(), ...beneficiary };
    form.reset(
      {
        ...beneficiaryRawValue,
        id: { value: beneficiaryRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): BeneficiaryFormDefaults {
    return {
      id: null,
    };
  }
}