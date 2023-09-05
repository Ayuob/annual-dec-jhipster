import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAnnualDeclaration } from '../annual-declaration.model';
import { DeclarationStatus } from '../../enumerations/declaration-status.model';

@Component({
  selector: 'jhi-annual-declaration-detail',
  templateUrl: './annual-declaration-detail.component.html',
})
export class AnnualDeclarationDetailComponent implements OnInit {
  annualDeclaration: IAnnualDeclaration | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ annualDeclaration }) => {
      this.annualDeclaration = annualDeclaration;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
