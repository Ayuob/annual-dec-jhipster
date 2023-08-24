import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAnnualDeclaration, NewAnnualDeclaration } from '../annual-declaration.model';

export type PartialUpdateAnnualDeclaration = Partial<IAnnualDeclaration> & Pick<IAnnualDeclaration, 'id'>;

type RestOf<T extends IAnnualDeclaration | NewAnnualDeclaration> = Omit<T, 'submissionDate'> & {
  submissionDate?: string | null;
};

export type RestAnnualDeclaration = RestOf<IAnnualDeclaration>;

export type NewRestAnnualDeclaration = RestOf<NewAnnualDeclaration>;

export type PartialUpdateRestAnnualDeclaration = RestOf<PartialUpdateAnnualDeclaration>;

export type EntityResponseType = HttpResponse<IAnnualDeclaration>;
export type EntityArrayResponseType = HttpResponse<IAnnualDeclaration[]>;

@Injectable({ providedIn: 'root' })
export class AnnualDeclarationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/annual-declarations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(annualDeclaration: NewAnnualDeclaration): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(annualDeclaration);
    return this.http
      .post<RestAnnualDeclaration>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(annualDeclaration: IAnnualDeclaration): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(annualDeclaration);
    return this.http
      .put<RestAnnualDeclaration>(`${this.resourceUrl}/${this.getAnnualDeclarationIdentifier(annualDeclaration)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(annualDeclaration: PartialUpdateAnnualDeclaration): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(annualDeclaration);
    return this.http
      .patch<RestAnnualDeclaration>(`${this.resourceUrl}/${this.getAnnualDeclarationIdentifier(annualDeclaration)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAnnualDeclaration>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAnnualDeclaration[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAnnualDeclarationIdentifier(annualDeclaration: Pick<IAnnualDeclaration, 'id'>): number {
    return annualDeclaration.id;
  }

  compareAnnualDeclaration(o1: Pick<IAnnualDeclaration, 'id'> | null, o2: Pick<IAnnualDeclaration, 'id'> | null): boolean {
    return o1 && o2 ? this.getAnnualDeclarationIdentifier(o1) === this.getAnnualDeclarationIdentifier(o2) : o1 === o2;
  }

  addAnnualDeclarationToCollectionIfMissing<Type extends Pick<IAnnualDeclaration, 'id'>>(
    annualDeclarationCollection: Type[],
    ...annualDeclarationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const annualDeclarations: Type[] = annualDeclarationsToCheck.filter(isPresent);
    if (annualDeclarations.length > 0) {
      const annualDeclarationCollectionIdentifiers = annualDeclarationCollection.map(
        annualDeclarationItem => this.getAnnualDeclarationIdentifier(annualDeclarationItem)!
      );
      const annualDeclarationsToAdd = annualDeclarations.filter(annualDeclarationItem => {
        const annualDeclarationIdentifier = this.getAnnualDeclarationIdentifier(annualDeclarationItem);
        if (annualDeclarationCollectionIdentifiers.includes(annualDeclarationIdentifier)) {
          return false;
        }
        annualDeclarationCollectionIdentifiers.push(annualDeclarationIdentifier);
        return true;
      });
      return [...annualDeclarationsToAdd, ...annualDeclarationCollection];
    }
    return annualDeclarationCollection;
  }

  protected convertDateFromClient<T extends IAnnualDeclaration | NewAnnualDeclaration | PartialUpdateAnnualDeclaration>(
    annualDeclaration: T
  ): RestOf<T> {
    return {
      ...annualDeclaration,
      submissionDate: annualDeclaration.submissionDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restAnnualDeclaration: RestAnnualDeclaration): IAnnualDeclaration {
    return {
      ...restAnnualDeclaration,
      submissionDate: restAnnualDeclaration.submissionDate ? dayjs(restAnnualDeclaration.submissionDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAnnualDeclaration>): HttpResponse<IAnnualDeclaration> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAnnualDeclaration[]>): HttpResponse<IAnnualDeclaration[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
