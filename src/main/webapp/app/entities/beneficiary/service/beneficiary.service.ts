import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBeneficiary, NewBeneficiary } from '../beneficiary.model';

export type PartialUpdateBeneficiary = Partial<IBeneficiary> & Pick<IBeneficiary, 'id'>;

export type EntityResponseType = HttpResponse<IBeneficiary>;
export type EntityArrayResponseType = HttpResponse<IBeneficiary[]>;

@Injectable({ providedIn: 'root' })
export class BeneficiaryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/beneficiaries');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(beneficiary: NewBeneficiary): Observable<EntityResponseType> {
    return this.http.post<IBeneficiary>(this.resourceUrl, beneficiary, { observe: 'response' });
  }

  update(beneficiary: IBeneficiary): Observable<EntityResponseType> {
    return this.http.put<IBeneficiary>(`${this.resourceUrl}/${this.getBeneficiaryIdentifier(beneficiary)}`, beneficiary, {
      observe: 'response',
    });
  }

  partialUpdate(beneficiary: PartialUpdateBeneficiary): Observable<EntityResponseType> {
    return this.http.patch<IBeneficiary>(`${this.resourceUrl}/${this.getBeneficiaryIdentifier(beneficiary)}`, beneficiary, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBeneficiary>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBeneficiary[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBeneficiaryIdentifier(beneficiary: Pick<IBeneficiary, 'id'>): number {
    return beneficiary.id;
  }

  compareBeneficiary(o1: Pick<IBeneficiary, 'id'> | null, o2: Pick<IBeneficiary, 'id'> | null): boolean {
    return o1 && o2 ? this.getBeneficiaryIdentifier(o1) === this.getBeneficiaryIdentifier(o2) : o1 === o2;
  }

  addBeneficiaryToCollectionIfMissing<Type extends Pick<IBeneficiary, 'id'>>(
    beneficiaryCollection: Type[],
    ...beneficiariesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const beneficiaries: Type[] = beneficiariesToCheck.filter(isPresent);
    if (beneficiaries.length > 0) {
      const beneficiaryCollectionIdentifiers = beneficiaryCollection.map(
        beneficiaryItem => this.getBeneficiaryIdentifier(beneficiaryItem)!
      );
      const beneficiariesToAdd = beneficiaries.filter(beneficiaryItem => {
        const beneficiaryIdentifier = this.getBeneficiaryIdentifier(beneficiaryItem);
        if (beneficiaryCollectionIdentifiers.includes(beneficiaryIdentifier)) {
          return false;
        }
        beneficiaryCollectionIdentifiers.push(beneficiaryIdentifier);
        return true;
      });
      return [...beneficiariesToAdd, ...beneficiaryCollection];
    }
    return beneficiaryCollection;
  }
}
