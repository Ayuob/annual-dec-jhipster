import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISocialSecurityPensioner } from '../social-security-pensioner.model';

@Component({
  selector: 'jhi-social-security-pensioner-detail',
  templateUrl: './social-security-pensioner-detail.component.html',
})
export class SocialSecurityPensionerDetailComponent implements OnInit {
  socialSecurityPensioner: ISocialSecurityPensioner | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ socialSecurityPensioner }) => {
      this.socialSecurityPensioner = socialSecurityPensioner;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
