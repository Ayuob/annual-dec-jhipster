import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBeneficiary } from '../beneficiary.model';

@Component({
  selector: 'jhi-beneficiary-detail',
  templateUrl: './beneficiary-detail.component.html',
})
export class BeneficiaryDetailComponent implements OnInit {
  beneficiary: IBeneficiary | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ beneficiary }) => {
      this.beneficiary = beneficiary;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
