import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'social-security-pensioner',
        data: { pageTitle: 'annualDecJhApp.socialSecurityPensioner.home.title' },
        loadChildren: () =>
          import('./social-security-pensioner/social-security-pensioner.module').then(m => m.SocialSecurityPensionerModule),
      },
      {
        path: 'family-member',
        data: { pageTitle: 'annualDecJhApp.familyMember.home.title' },
        loadChildren: () => import('./family-member/family-member.module').then(m => m.FamilyMemberModule),
      },
      {
        path: 'beneficiary',
        data: { pageTitle: 'annualDecJhApp.beneficiary.home.title' },
        loadChildren: () => import('./beneficiary/beneficiary.module').then(m => m.BeneficiaryModule),
      },
      {
        path: 'annual-declaration',
        data: { pageTitle: 'annualDecJhApp.annualDeclaration.home.title' },
        loadChildren: () => import('./annual-declaration/annual-declaration.module').then(m => m.AnnualDeclarationModule),
      },
      {
        path: 'document',
        data: { pageTitle: 'annualDecJhApp.document.home.title' },
        loadChildren: () => import('./document/document.module').then(m => m.DocumentModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
