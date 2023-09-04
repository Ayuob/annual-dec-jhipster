import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IFamilyMember, NewFamilyMember } from '../family-member.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFamilyMember for edit and NewFamilyMemberFormGroupInput for create.
 */
type FamilyMemberFormGroupInput = IFamilyMember | PartialWithRequiredKeyOf<NewFamilyMember>;

type FamilyMemberFormDefaults = Pick<NewFamilyMember, 'id' | 'annualDeclarations'>;

type FamilyMemberFormGroupContent = {
  id: FormControl<IFamilyMember['id'] | NewFamilyMember['id']>;
  nationalNumber: FormControl<IFamilyMember['nationalNumber']>;
  name: FormControl<IFamilyMember['name']>;
  dateOfBirth: FormControl<IFamilyMember['dateOfBirth']>;
  gender: FormControl<IFamilyMember['gender']>;
  pensioner: FormControl<IFamilyMember['pensioner']>;
  annualDeclarations: FormControl<IFamilyMember['annualDeclarations']>;
};

export type FamilyMemberFormGroup = FormGroup<FamilyMemberFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FamilyMemberFormService {
  createFamilyMemberFormGroup(familyMember: FamilyMemberFormGroupInput = { id: null }): FamilyMemberFormGroup {
    const familyMemberRawValue = {
      ...this.getFormDefaults(),
      ...familyMember,
    };
    return new FormGroup<FamilyMemberFormGroupContent>({
      id: new FormControl(
        { value: familyMemberRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nationalNumber: new FormControl(familyMemberRawValue.nationalNumber, {
        validators: [Validators.required, Validators.minLength(12), Validators.maxLength(12)],
      }),
      name: new FormControl(familyMemberRawValue.name, {
        validators: [Validators.required],
      }),
      dateOfBirth: new FormControl(familyMemberRawValue.dateOfBirth, {
        validators: [Validators.required],
      }),
      gender: new FormControl(familyMemberRawValue.gender, {
        validators: [],
      }),
      pensioner: new FormControl(familyMemberRawValue.pensioner),
      annualDeclarations: new FormControl(familyMemberRawValue.annualDeclarations ?? []),
    });
  }

  getFamilyMember(form: FamilyMemberFormGroup): IFamilyMember | NewFamilyMember {
    return form.getRawValue() as IFamilyMember | NewFamilyMember;
  }

  resetForm(form: FamilyMemberFormGroup, familyMember: FamilyMemberFormGroupInput): void {
    const familyMemberRawValue = { ...this.getFormDefaults(), ...familyMember };
    form.reset(
      {
        ...familyMemberRawValue,
        id: { value: familyMemberRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FamilyMemberFormDefaults {
    return {
      id: null,
      annualDeclarations: [],
    };
  }
}
