import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISocialSecurityPensioner, NewSocialSecurityPensioner } from '../social-security-pensioner.model';

export type PartialUpdateSocialSecurityPensioner = Partial<ISocialSecurityPensioner> & Pick<ISocialSecurityPensioner, 'id'>;

type RestOf<T extends ISocialSecurityPensioner | NewSocialSecurityPensioner> = Omit<T, 'dateOfBirth'> & {
  dateOfBirth?: string | null;
};

export type RestSocialSecurityPensioner = RestOf<ISocialSecurityPensioner>;

export type NewRestSocialSecurityPensioner = RestOf<NewSocialSecurityPensioner>;

export type PartialUpdateRestSocialSecurityPensioner = RestOf<PartialUpdateSocialSecurityPensioner>;

export type EntityResponseType = HttpResponse<ISocialSecurityPensioner>;
export type EntityArrayResponseType = HttpResponse<ISocialSecurityPensioner[]>;

@Injectable({ providedIn: 'root' })
export class SocialSecurityPensionerService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/social-security-pensioners');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(socialSecurityPensioner: NewSocialSecurityPensioner): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(socialSecurityPensioner);
    return this.http
      .post<RestSocialSecurityPensioner>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(socialSecurityPensioner: ISocialSecurityPensioner): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(socialSecurityPensioner);
    return this.http
      .put<RestSocialSecurityPensioner>(`${this.resourceUrl}/${this.getSocialSecurityPensionerIdentifier(socialSecurityPensioner)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(socialSecurityPensioner: PartialUpdateSocialSecurityPensioner): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(socialSecurityPensioner);
    return this.http
      .patch<RestSocialSecurityPensioner>(
        `${this.resourceUrl}/${this.getSocialSecurityPensionerIdentifier(socialSecurityPensioner)}`,
        copy,
        { observe: 'response' }
      )
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSocialSecurityPensioner>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSocialSecurityPensioner[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSocialSecurityPensionerIdentifier(socialSecurityPensioner: Pick<ISocialSecurityPensioner, 'id'>): number {
    return socialSecurityPensioner.id;
  }

  compareSocialSecurityPensioner(
    o1: Pick<ISocialSecurityPensioner, 'id'> | null,
    o2: Pick<ISocialSecurityPensioner, 'id'> | null
  ): boolean {
    return o1 && o2 ? this.getSocialSecurityPensionerIdentifier(o1) === this.getSocialSecurityPensionerIdentifier(o2) : o1 === o2;
  }

  addSocialSecurityPensionerToCollectionIfMissing<Type extends Pick<ISocialSecurityPensioner, 'id'>>(
    socialSecurityPensionerCollection: Type[],
    ...socialSecurityPensionersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const socialSecurityPensioners: Type[] = socialSecurityPensionersToCheck.filter(isPresent);
    if (socialSecurityPensioners.length > 0) {
      const socialSecurityPensionerCollectionIdentifiers = socialSecurityPensionerCollection.map(
        socialSecurityPensionerItem => this.getSocialSecurityPensionerIdentifier(socialSecurityPensionerItem)!
      );
      const socialSecurityPensionersToAdd = socialSecurityPensioners.filter(socialSecurityPensionerItem => {
        const socialSecurityPensionerIdentifier = this.getSocialSecurityPensionerIdentifier(socialSecurityPensionerItem);
        if (socialSecurityPensionerCollectionIdentifiers.includes(socialSecurityPensionerIdentifier)) {
          return false;
        }
        socialSecurityPensionerCollectionIdentifiers.push(socialSecurityPensionerIdentifier);
        return true;
      });
      return [...socialSecurityPensionersToAdd, ...socialSecurityPensionerCollection];
    }
    return socialSecurityPensionerCollection;
  }

  protected convertDateFromClient<T extends ISocialSecurityPensioner | NewSocialSecurityPensioner | PartialUpdateSocialSecurityPensioner>(
    socialSecurityPensioner: T
  ): RestOf<T> {
    return {
      ...socialSecurityPensioner,
      dateOfBirth: socialSecurityPensioner.dateOfBirth?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restSocialSecurityPensioner: RestSocialSecurityPensioner): ISocialSecurityPensioner {
    return {
      ...restSocialSecurityPensioner,
      dateOfBirth: restSocialSecurityPensioner.dateOfBirth ? dayjs(restSocialSecurityPensioner.dateOfBirth) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSocialSecurityPensioner>): HttpResponse<ISocialSecurityPensioner> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSocialSecurityPensioner[]>): HttpResponse<ISocialSecurityPensioner[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
