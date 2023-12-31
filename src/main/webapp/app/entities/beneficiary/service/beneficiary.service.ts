import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { BeneficiaryId, IBeneficiary, NewBeneficiary } from '../beneficiary.model';

export type PartialUpdateBeneficiary = Partial<IBeneficiary> & Pick<IBeneficiary, 'id'>;
// export type PartialUpdateBeneficiary = Partial<IBeneficiary> & Pick<IBeneficiary, 'familyMembersId' | 'annualDeclarationId'>;

export type EntityResponseType = HttpResponse<IBeneficiary>;
export type EntityArrayResponseType = HttpResponse<IBeneficiary[]>;

@Injectable({ providedIn: 'root' })
export class BeneficiaryService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/beneficiaries');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(beneficiary: NewBeneficiary): Observable<EntityResponseType> {
    return this.http.post<IBeneficiary>(this.resourceUrl, beneficiary, { observe: 'response' });
  }

  // update(beneficiary: IBeneficiary): Observable<EntityResponseType> {
  //   return this.http.put<IBeneficiary>(`${this.resourceUrl}/${this.getBeneficiaryUriIdentifier(beneficiary)}`, beneficiary, {
  //     //Todo Debug getBeneficiaryIdentifier() to see how id is represented within the url
  //     observe: 'response',
  //   });
  // }
  update(beneficiary: IBeneficiary): Observable<EntityResponseType> {
    console.log('Beneficiary ID:', beneficiary.id); // Add this line for debugging
    const { familyMemberId, annualDeclarationId } = beneficiary.id;

    const url = `${this.resourceUrl}/${familyMemberId}/${annualDeclarationId}`;

    return this.http.put<IBeneficiary>(url, beneficiary, {
      observe: 'response',
    });
  }

  partialUpdate(beneficiary: PartialUpdateBeneficiary): Observable<EntityResponseType> {
    return this.http.patch<IBeneficiary>(`${this.resourceUrl}/${this.getBeneficiaryUriIdentifier(beneficiary)}`, beneficiary, {
      observe: 'response',
    });
  }

  // find(id: number): Observable<EntityResponseType> {
  //   return this.http.get<IBeneficiary>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  // }
  find(familyMembersId: number, annualDeclarationId: number): Observable<EntityResponseType> {
    return this.http.get<IBeneficiary>(`${this.resourceUrl}/${familyMembersId}/${annualDeclarationId}`, {
      observe: 'response',
    });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBeneficiary[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  // delete(id: number): Observable<HttpResponse<{}>> {
  //   return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  // }
  delete(familyMembersId: number, annualDeclarationId: number): Observable<HttpResponse<{}>> {
    const url = `${this.resourceUrl}/${familyMembersId}/${annualDeclarationId}`;
    return this.http.delete(url, { observe: 'response' });
  }

  getBeneficiaryIdentifier(beneficiary: Pick<IBeneficiary, 'id'>): BeneficiaryId {
    return beneficiary.id;
  }
  getBeneficiaryUriIdentifier(beneficiary: Pick<IBeneficiary, 'id'>): string {
    const { familyMemberId, annualDeclarationId } = beneficiary.id;
    return familyMemberId + '/' + annualDeclarationId;
  }
  // getBeneficiaryIdentifier(beneficiary: Pick<IBeneficiary, 'familyMembersId' | 'annualDeclarationId'>): string {
  //   return `${beneficiary.familyMembersId}-${beneficiary.annualDeclarationId}`;
  // }

  compareBeneficiary(o1: Pick<IBeneficiary, 'id'> | null, o2: Pick<IBeneficiary, 'id'> | null): boolean {
    return o1 && o2
      ? this.getBeneficiaryIdentifier(o1).familyMemberId === this.getBeneficiaryIdentifier(o2).familyMemberId
      : o1 === o2 && o1 && o2
      ? this.getBeneficiaryIdentifier(o1).annualDeclarationId === this.getBeneficiaryIdentifier(o2).annualDeclarationId
      : o1 === o2;
  }
  // compareBeneficiary(o1: Pick<IBeneficiary, 'familyMembersId' | 'annualDeclarationId'> | null, o2: Pick<IBeneficiary, 'familyMembersId' | 'annualDeclarationId'> | null): boolean {
  //   return o1 && o2 ? (
  //     o1.familyMembersId === o2.familyMembersId &&
  //     o1.annualDeclarationId === o2.annualDeclarationId
  //   ) : o1 === o2;
  // }

  addBeneficiaryToCollectionIfMissing<Type extends Pick<IBeneficiary, 'id'>>(
    beneficiaryCollection: Type[],
    ...beneficiariesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const beneficiaries: Type[] = beneficiariesToCheck.filter(isPresent);
    if (beneficiaries.length > 0) {
      const beneficiaryCollectionIdentifiers = beneficiaryCollection.map(
        beneficiaryItem => `${beneficiaryItem.id.familyMemberId}-${beneficiaryItem.id.annualDeclarationId}`
      );
      const beneficiariesToAdd = beneficiaries.filter(beneficiaryItem => {
        const beneficiaryIdentifier = `${beneficiaryItem.id.familyMemberId}-${beneficiaryItem.id.annualDeclarationId}`;
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
