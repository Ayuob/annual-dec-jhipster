import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAnnualDeclaration } from '../annual-declaration.model';
import { AnnualDeclarationService } from '../service/annual-declaration.service';

@Injectable({ providedIn: 'root' })
export class AnnualDeclarationRoutingResolveService implements Resolve<IAnnualDeclaration | null> {
  constructor(protected service: AnnualDeclarationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAnnualDeclaration | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((annualDeclaration: HttpResponse<IAnnualDeclaration>) => {
          if (annualDeclaration.body) {
            return of(annualDeclaration.body);
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
