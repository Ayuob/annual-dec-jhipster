import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAnnualDeclaration, NewAnnualDeclaration } from '../annual-declaration.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAnnualDeclaration for edit and NewAnnualDeclarationFormGroupInput for create.
 */
type AnnualDeclarationFormGroupInput = IAnnualDeclaration | PartialWithRequiredKeyOf<NewAnnualDeclaration>;

type AnnualDeclarationFormDefaults = Pick<NewAnnualDeclaration, 'id' | 'familyMembers'>;

type AnnualDeclarationFormGroupContent = {
  id: FormControl<IAnnualDeclaration['id'] | NewAnnualDeclaration['id']>;
  submissionDate: FormControl<IAnnualDeclaration['submissionDate']>;
  status: FormControl<IAnnualDeclaration['status']>;
  pensioner: FormControl<IAnnualDeclaration['pensioner']>;
  familyMembers: FormControl<IAnnualDeclaration['familyMembers']>;
};

export type AnnualDeclarationFormGroup = FormGroup<AnnualDeclarationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AnnualDeclarationFormService {
  createAnnualDeclarationFormGroup(annualDeclaration: AnnualDeclarationFormGroupInput = { id: null }): AnnualDeclarationFormGroup {
    const annualDeclarationRawValue = {
      ...this.getFormDefaults(),
      ...annualDeclaration,
    };
    return new FormGroup<AnnualDeclarationFormGroupContent>({
      id: new FormControl(
        { value: annualDeclarationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      submissionDate: new FormControl(annualDeclarationRawValue.submissionDate, {
        validators: [Validators.required],
      }),
      status: new FormControl(annualDeclarationRawValue.status, {
        validators: [Validators.required],
      }),
      pensioner: new FormControl(annualDeclarationRawValue.pensioner),
      familyMembers: new FormControl(annualDeclarationRawValue.familyMembers ?? []),
    });
  }

  getAnnualDeclaration(form: AnnualDeclarationFormGroup): IAnnualDeclaration | NewAnnualDeclaration {
    return form.getRawValue() as IAnnualDeclaration | NewAnnualDeclaration;
  }

  resetForm(form: AnnualDeclarationFormGroup, annualDeclaration: AnnualDeclarationFormGroupInput): void {
    const annualDeclarationRawValue = { ...this.getFormDefaults(), ...annualDeclaration };
    form.reset(
      {
        ...annualDeclarationRawValue,
        id: { value: annualDeclarationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AnnualDeclarationFormDefaults {
    return {
      id: null,
      familyMembers: [],
    };
  }
}
