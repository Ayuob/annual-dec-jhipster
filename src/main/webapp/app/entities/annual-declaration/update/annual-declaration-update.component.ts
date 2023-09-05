import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { AnnualDeclarationFormGroup, AnnualDeclarationFormService } from './annual-declaration-form.service';
import { IAnnualDeclaration } from '../annual-declaration.model';
import { AnnualDeclarationService } from '../service/annual-declaration.service';
import { ISocialSecurityPensioner } from 'app/entities/social-security-pensioner/social-security-pensioner.model';
import { SocialSecurityPensionerService } from 'app/entities/social-security-pensioner/service/social-security-pensioner.service';
import { IFamilyMember } from 'app/entities/family-member/family-member.model';
import { FamilyMemberService } from 'app/entities/family-member/service/family-member.service';
import { DeclarationStatus } from 'app/entities/enumerations/declaration-status.model';
import dayjs from 'dayjs/esm';
import { translate } from '@angular/localize/tools';
import { TranslatePipe } from '@ngx-translate/core/lib/translate.pipe';

@Component({
  selector: 'jhi-annual-declaration-update',
  templateUrl: './annual-declaration-update.component.html',
})
export class AnnualDeclarationUpdateComponent implements OnInit {
  isSaving = false;
  annualDeclaration: IAnnualDeclaration | null = null;
  declarationStatusValues = Object.keys(DeclarationStatus);

  socialSecurityPensionersSharedCollection: ISocialSecurityPensioner[] = [];
  familyMembersSharedCollection: IFamilyMember[] = [];

  editForm: AnnualDeclarationFormGroup = this.annualDeclarationFormService.createAnnualDeclarationFormGroup();

  constructor(
    protected annualDeclarationService: AnnualDeclarationService,
    protected annualDeclarationFormService: AnnualDeclarationFormService,
    protected socialSecurityPensionerService: SocialSecurityPensionerService,
    protected familyMemberService: FamilyMemberService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareSocialSecurityPensioner = (o1: ISocialSecurityPensioner | null, o2: ISocialSecurityPensioner | null): boolean =>
    this.socialSecurityPensionerService.compareSocialSecurityPensioner(o1, o2);

  compareFamilyMember = (o1: IFamilyMember | null, o2: IFamilyMember | null): boolean =>
    this.familyMemberService.compareFamilyMember(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ annualDeclaration }) => {
      this.annualDeclaration = annualDeclaration;
      if (annualDeclaration) {
        this.updateForm(annualDeclaration);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const annualDeclaration = this.annualDeclarationFormService.getAnnualDeclaration(this.editForm);
    annualDeclaration.submissionDate = dayjs(new Date());
    annualDeclaration.status = DeclarationStatus.SUBMITTED;
    if (annualDeclaration.id !== null) {
      this.subscribeToSaveResponse(this.annualDeclarationService.update(annualDeclaration));
    } else {
      this.subscribeToSaveResponse(this.annualDeclarationService.create(annualDeclaration));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAnnualDeclaration>>): void {
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

  protected updateForm(annualDeclaration: IAnnualDeclaration): void {
    this.annualDeclaration = annualDeclaration;
    this.annualDeclarationFormService.resetForm(this.editForm, annualDeclaration);

    this.socialSecurityPensionersSharedCollection =
      this.socialSecurityPensionerService.addSocialSecurityPensionerToCollectionIfMissing<ISocialSecurityPensioner>(
        this.socialSecurityPensionersSharedCollection,
        annualDeclaration.pensioner
      );
    this.familyMembersSharedCollection = this.familyMemberService.addFamilyMemberToCollectionIfMissing<IFamilyMember>(
      this.familyMembersSharedCollection,
      ...(annualDeclaration.familyMembers ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.socialSecurityPensionerService
      .query()
      .pipe(map((res: HttpResponse<ISocialSecurityPensioner[]>) => res.body ?? []))
      .pipe(
        map((socialSecurityPensioners: ISocialSecurityPensioner[]) =>
          this.socialSecurityPensionerService.addSocialSecurityPensionerToCollectionIfMissing<ISocialSecurityPensioner>(
            socialSecurityPensioners,
            this.annualDeclaration?.pensioner
          )
        )
      )
      .subscribe(
        (socialSecurityPensioners: ISocialSecurityPensioner[]) => (this.socialSecurityPensionersSharedCollection = socialSecurityPensioners)
      );

    this.familyMemberService
      .query()
      .pipe(map((res: HttpResponse<IFamilyMember[]>) => res.body ?? []))
      .pipe(
        map((familyMembers: IFamilyMember[]) =>
          this.familyMemberService.addFamilyMemberToCollectionIfMissing<IFamilyMember>(
            familyMembers,
            ...(this.annualDeclaration?.familyMembers ?? [])
          )
        )
      )
      .subscribe((familyMembers: IFamilyMember[]) => (this.familyMembersSharedCollection = familyMembers));
  }
}
