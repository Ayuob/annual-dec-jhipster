import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IBeneficiary } from '../beneficiary.model';
import { BeneficiaryService } from '../service/beneficiary.service';

@Injectable({ providedIn: 'root' })
export class BeneficiaryRoutingResolveService implements Resolve<IBeneficiary | null> {
  constructor(protected service: BeneficiaryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBeneficiary | null | never> {
    const familyMembersId = route.params['familyMembersId'];
    const annualDeclarationId = route.params['annualDeclarationId'];

    if (familyMembersId && annualDeclarationId) {
      return this.service.find(familyMembersId, annualDeclarationId).pipe(
        mergeMap((beneficiary: HttpResponse<IBeneficiary>) => {
          if (beneficiary.body) {
            return of(beneficiary.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
