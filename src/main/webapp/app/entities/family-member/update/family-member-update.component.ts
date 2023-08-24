import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { FamilyMemberFormService, FamilyMemberFormGroup } from './family-member-form.service';
import { IFamilyMember } from '../family-member.model';
import { FamilyMemberService } from '../service/family-member.service';
import { ISocialSecurityPensioner } from 'app/entities/social-security-pensioner/social-security-pensioner.model';
import { SocialSecurityPensionerService } from 'app/entities/social-security-pensioner/service/social-security-pensioner.service';

@Component({
  selector: 'jhi-family-member-update',
  templateUrl: './family-member-update.component.html',
})
export class FamilyMemberUpdateComponent implements OnInit {
  isSaving = false;
  familyMember: IFamilyMember | null = null;

  socialSecurityPensionersSharedCollection: ISocialSecurityPensioner[] = [];

  editForm: FamilyMemberFormGroup = this.familyMemberFormService.createFamilyMemberFormGroup();

  constructor(
    protected familyMemberService: FamilyMemberService,
    protected familyMemberFormService: FamilyMemberFormService,
    protected socialSecurityPensionerService: SocialSecurityPensionerService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareSocialSecurityPensioner = (o1: ISocialSecurityPensioner | null, o2: ISocialSecurityPensioner | null): boolean =>
    this.socialSecurityPensionerService.compareSocialSecurityPensioner(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ familyMember }) => {
      this.familyMember = familyMember;
      if (familyMember) {
        this.updateForm(familyMember);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const familyMember = this.familyMemberFormService.getFamilyMember(this.editForm);
    if (familyMember.id !== null) {
      this.subscribeToSaveResponse(this.familyMemberService.update(familyMember));
    } else {
      this.subscribeToSaveResponse(this.familyMemberService.create(familyMember));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFamilyMember>>): void {
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

  protected updateForm(familyMember: IFamilyMember): void {
    this.familyMember = familyMember;
    this.familyMemberFormService.resetForm(this.editForm, familyMember);

    this.socialSecurityPensionersSharedCollection =
      this.socialSecurityPensionerService.addSocialSecurityPensionerToCollectionIfMissing<ISocialSecurityPensioner>(
        this.socialSecurityPensionersSharedCollection,
        familyMember.pensioner
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
            this.familyMember?.pensioner
          )
        )
      )
      .subscribe(
        (socialSecurityPensioners: ISocialSecurityPensioner[]) => (this.socialSecurityPensionersSharedCollection = socialSecurityPensioners)
      );
  }
}
