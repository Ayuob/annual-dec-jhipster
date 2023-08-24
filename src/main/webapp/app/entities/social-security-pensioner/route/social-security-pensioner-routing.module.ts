import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SocialSecurityPensionerComponent } from '../list/social-security-pensioner.component';
import { SocialSecurityPensionerDetailComponent } from '../detail/social-security-pensioner-detail.component';
import { SocialSecurityPensionerUpdateComponent } from '../update/social-security-pensioner-update.component';
import { SocialSecurityPensionerRoutingResolveService } from './social-security-pensioner-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const socialSecurityPensionerRoute: Routes = [
  {
    path: '',
    component: SocialSecurityPensionerComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SocialSecurityPensionerDetailComponent,
    resolve: {
      socialSecurityPensioner: SocialSecurityPensionerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SocialSecurityPensionerUpdateComponent,
    resolve: {
      socialSecurityPensioner: SocialSecurityPensionerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SocialSecurityPensionerUpdateComponent,
    resolve: {
      socialSecurityPensioner: SocialSecurityPensionerRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(socialSecurityPensionerRoute)],
  exports: [RouterModule],
})
export class SocialSecurityPensionerRoutingModule {}
