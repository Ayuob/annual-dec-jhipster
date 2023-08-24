import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ISocialSecurityPensioner } from '../social-security-pensioner.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../social-security-pensioner.test-samples';

import { SocialSecurityPensionerService, RestSocialSecurityPensioner } from './social-security-pensioner.service';

const requireRestSample: RestSocialSecurityPensioner = {
  ...sampleWithRequiredData,
  dateOfBirth: sampleWithRequiredData.dateOfBirth?.format(DATE_FORMAT),
};

describe('SocialSecurityPensioner Service', () => {
  let service: SocialSecurityPensionerService;
  let httpMock: HttpTestingController;
  let expectedResult: ISocialSecurityPensioner | ISocialSecurityPensioner[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SocialSecurityPensionerService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a SocialSecurityPensioner', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const socialSecurityPensioner = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(socialSecurityPensioner).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SocialSecurityPensioner', () => {
      const socialSecurityPensioner = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(socialSecurityPensioner).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SocialSecurityPensioner', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SocialSecurityPensioner', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SocialSecurityPensioner', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSocialSecurityPensionerToCollectionIfMissing', () => {
      it('should add a SocialSecurityPensioner to an empty array', () => {
        const socialSecurityPensioner: ISocialSecurityPensioner = sampleWithRequiredData;
        expectedResult = service.addSocialSecurityPensionerToCollectionIfMissing([], socialSecurityPensioner);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(socialSecurityPensioner);
      });

      it('should not add a SocialSecurityPensioner to an array that contains it', () => {
        const socialSecurityPensioner: ISocialSecurityPensioner = sampleWithRequiredData;
        const socialSecurityPensionerCollection: ISocialSecurityPensioner[] = [
          {
            ...socialSecurityPensioner,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSocialSecurityPensionerToCollectionIfMissing(
          socialSecurityPensionerCollection,
          socialSecurityPensioner
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SocialSecurityPensioner to an array that doesn't contain it", () => {
        const socialSecurityPensioner: ISocialSecurityPensioner = sampleWithRequiredData;
        const socialSecurityPensionerCollection: ISocialSecurityPensioner[] = [sampleWithPartialData];
        expectedResult = service.addSocialSecurityPensionerToCollectionIfMissing(
          socialSecurityPensionerCollection,
          socialSecurityPensioner
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(socialSecurityPensioner);
      });

      it('should add only unique SocialSecurityPensioner to an array', () => {
        const socialSecurityPensionerArray: ISocialSecurityPensioner[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const socialSecurityPensionerCollection: ISocialSecurityPensioner[] = [sampleWithRequiredData];
        expectedResult = service.addSocialSecurityPensionerToCollectionIfMissing(
          socialSecurityPensionerCollection,
          ...socialSecurityPensionerArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const socialSecurityPensioner: ISocialSecurityPensioner = sampleWithRequiredData;
        const socialSecurityPensioner2: ISocialSecurityPensioner = sampleWithPartialData;
        expectedResult = service.addSocialSecurityPensionerToCollectionIfMissing([], socialSecurityPensioner, socialSecurityPensioner2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(socialSecurityPensioner);
        expect(expectedResult).toContain(socialSecurityPensioner2);
      });

      it('should accept null and undefined values', () => {
        const socialSecurityPensioner: ISocialSecurityPensioner = sampleWithRequiredData;
        expectedResult = service.addSocialSecurityPensionerToCollectionIfMissing([], null, socialSecurityPensioner, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(socialSecurityPensioner);
      });

      it('should return initial array if no SocialSecurityPensioner is added', () => {
        const socialSecurityPensionerCollection: ISocialSecurityPensioner[] = [sampleWithRequiredData];
        expectedResult = service.addSocialSecurityPensionerToCollectionIfMissing(socialSecurityPensionerCollection, undefined, null);
        expect(expectedResult).toEqual(socialSecurityPensionerCollection);
      });
    });

    describe('compareSocialSecurityPensioner', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSocialSecurityPensioner(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSocialSecurityPensioner(entity1, entity2);
        const compareResult2 = service.compareSocialSecurityPensioner(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSocialSecurityPensioner(entity1, entity2);
        const compareResult2 = service.compareSocialSecurityPensioner(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSocialSecurityPensioner(entity1, entity2);
        const compareResult2 = service.compareSocialSecurityPensioner(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
