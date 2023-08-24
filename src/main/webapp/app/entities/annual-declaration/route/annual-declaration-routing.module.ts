import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AnnualDeclarationComponent } from '../list/annual-declaration.component';
import { AnnualDeclarationDetailComponent } from '../detail/annual-declaration-detail.component';
import { AnnualDeclarationUpdateComponent } from '../update/annual-declaration-update.component';
import { AnnualDeclarationRoutingResolveService } from './annual-declaration-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const annualDeclarationRoute: Routes = [
  {
    path: '',
    component: AnnualDeclarationComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AnnualDeclarationDetailComponent,
    resolve: {
      annualDeclaration: AnnualDeclarationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AnnualDeclarationUpdateComponent,
    resolve: {
      annualDeclaration: AnnualDeclarationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AnnualDeclarationUpdateComponent,
    resolve: {
      annualDeclaration: AnnualDeclarationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(annualDeclarationRoute)],
  exports: [RouterModule],
})
export class AnnualDeclarationRoutingModule {}
