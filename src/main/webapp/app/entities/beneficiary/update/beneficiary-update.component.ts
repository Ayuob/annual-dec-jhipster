import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { BeneficiaryFormService, BeneficiaryFormGroup } from './beneficiary-form.service';
import { IBeneficiary } from '../beneficiary.model';
import { BeneficiaryService } from '../service/beneficiary.service';
import { IFamilyMember } from 'app/entities/family-member/family-member.model';
import { FamilyMemberService } from 'app/entities/family-member/service/family-member.service';
import { IAnnualDeclaration } from 'app/entities/annual-declaration/annual-declaration.model';
import { AnnualDeclarationService } from 'app/entities/annual-declaration/service/annual-declaration.service';
import { EntitlementType } from 'app/entities/enumerations/entitlement-type.model';

@Component({
  selector: 'jhi-beneficiary-update',
  templateUrl: './beneficiary-update.component.html',
})
export class BeneficiaryUpdateComponent implements OnInit {
  isSaving = false;
  beneficiary: IBeneficiary | null = null;
  entitlementTypeValues = Object.keys(EntitlementType);

  familyMembersSharedCollection: IFamilyMember[] = [];
  annualDeclarationsSharedCollection: IAnnualDeclaration[] = [];

  editForm: BeneficiaryFormGroup = this.beneficiaryFormService.createBeneficiaryFormGroup();

  constructor(
    protected beneficiaryService: BeneficiaryService,
    protected beneficiaryFormService: BeneficiaryFormService,
    protected familyMemberService: FamilyMemberService,
    protected annualDeclarationService: AnnualDeclarationService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareFamilyMember = (o1: IFamilyMember | null, o2: IFamilyMember | null): boolean =>
    this.familyMemberService.compareFamilyMember(o1, o2);

  compareAnnualDeclaration = (o1: IAnnualDeclaration | null, o2: IAnnualDeclaration | null): boolean =>
    this.annualDeclarationService.compareAnnualDeclaration(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ beneficiary }) => {
      this.beneficiary = beneficiary;
      if (beneficiary) {
        this.updateForm(beneficiary);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const beneficiary = this.beneficiaryFormService.getBeneficiary(this.editForm);
    if (beneficiary.id !== null) {
      this.subscribeToSaveResponse(this.beneficiaryService.update(beneficiary));
    } else {
      this.subscribeToSaveResponse(this.beneficiaryService.create(beneficiary));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBeneficiary>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(beneficiary: IBeneficiary): void {
    this.beneficiary = beneficiary;
    this.beneficiaryFormService.resetForm(this.editForm, beneficiary);

    this.familyMembersSharedCollection = this.familyMemberService.addFamilyMemberToCollectionIfMissing<IFamilyMember>(
      this.familyMembersSharedCollection,
      beneficiary.familyMembers
    );
    this.annualDeclarationsSharedCollection = this.annualDeclarationService.addAnnualDeclarationToCollectionIfMissing<IAnnualDeclaration>(
      this.annualDeclarationsSharedCollection,
      beneficiary.annualDeclaration
    );
  }

  protected loadRelationshipsOptions(): void {
    this.familyMemberService
      .query()
      .pipe(map((res: HttpResponse<IFamilyMember[]>) => res.body ?? []))
      .pipe(
        map((familyMembers: IFamilyMember[]) =>
          this.familyMemberService.addFamilyMemberToCollectionIfMissing<IFamilyMember>(familyMembers, this.beneficiary?.familyMembers)
        )
      )
      .subscribe((familyMembers: IFamilyMember[]) => (this.familyMembersSharedCollection = familyMembers));

    this.annualDeclarationService
      .query()
      .pipe(map((res: HttpResponse<IAnnualDeclaration[]>) => res.body ?? []))
      .pipe(
        map((annualDeclarations: IAnnualDeclaration[]) =>
          this.annualDeclarationService.addAnnualDeclarationToCollectionIfMissing<IAnnualDeclaration>(
            annualDeclarations,
            this.beneficiary?.annualDeclaration
          )
        )
      )
      .subscribe((annualDeclarations: IAnnualDeclaration[]) => (this.annualDeclarationsSharedCollection = annualDeclarations));
  }
}
