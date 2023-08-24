import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISocialSecurityPensioner } from '../social-security-pensioner.model';
import { SocialSecurityPensionerService } from '../service/social-security-pensioner.service';

@Injectable({ providedIn: 'root' })
export class SocialSecurityPensionerRoutingResolveService implements Resolve<ISocialSecurityPensioner | null> {
  constructor(protected service: SocialSecurityPensionerService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISocialSecurityPensioner | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((socialSecurityPensioner: HttpResponse<ISocialSecurityPensioner>) => {
          if (socialSecurityPensioner.body) {
            return of(socialSecurityPensioner.body);
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
