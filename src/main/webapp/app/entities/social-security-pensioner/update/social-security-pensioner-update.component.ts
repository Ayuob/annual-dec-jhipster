import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { SocialSecurityPensionerFormService, SocialSecurityPensionerFormGroup } from './social-security-pensioner-form.service';
import { ISocialSecurityPensioner } from '../social-security-pensioner.model';
import { SocialSecurityPensionerService } from '../service/social-security-pensioner.service';

@Component({
  selector: 'jhi-social-security-pensioner-update',
  templateUrl: './social-security-pensioner-update.component.html',
})
export class SocialSecurityPensionerUpdateComponent implements OnInit {
  isSaving = false;
  socialSecurityPensioner: ISocialSecurityPensioner | null = null;

  editForm: SocialSecurityPensionerFormGroup = this.socialSecurityPensionerFormService.createSocialSecurityPensionerFormGroup();

  constructor(
    protected socialSecurityPensionerService: SocialSecurityPensionerService,
    protected socialSecurityPensionerFormService: SocialSecurityPensionerFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ socialSecurityPensioner }) => {
      this.socialSecurityPensioner = socialSecurityPensioner;
      if (socialSecurityPensioner) {
        this.updateForm(socialSecurityPensioner);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const socialSecurityPensioner = this.socialSecurityPensionerFormService.getSocialSecurityPensioner(this.editForm);
    if (socialSecurityPensioner.id !== null) {
      this.subscribeToSaveResponse(this.socialSecurityPensionerService.update(socialSecurityPensioner));
    } else {
      this.subscribeToSaveResponse(this.socialSecurityPensionerService.create(socialSecurityPensioner));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISocialSecurityPensioner>>): void {
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

  protected updateForm(socialSecurityPensioner: ISocialSecurityPensioner): void {
    this.socialSecurityPensioner = socialSecurityPensioner;
    this.socialSecurityPensionerFormService.resetForm(this.editForm, socialSecurityPensioner);
  }
}
