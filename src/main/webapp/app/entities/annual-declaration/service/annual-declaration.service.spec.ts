import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IAnnualDeclaration } from '../annual-declaration.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../annual-declaration.test-samples';

import { AnnualDeclarationService, RestAnnualDeclaration } from './annual-declaration.service';

const requireRestSample: RestAnnualDeclaration = {
  ...sampleWithRequiredData,
  submissionDate: sampleWithRequiredData.submissionDate?.format(DATE_FORMAT),
};

describe('AnnualDeclaration Service', () => {
  let service: AnnualDeclarationService;
  let httpMock: HttpTestingController;
  let expectedResult: IAnnualDeclaration | IAnnualDeclaration[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AnnualDeclarationService);
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

    it('should create a AnnualDeclaration', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const annualDeclaration = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(annualDeclaration).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a AnnualDeclaration', () => {
      const annualDeclaration = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(annualDeclaration).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a AnnualDeclaration', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of AnnualDeclaration', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a AnnualDeclaration', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAnnualDeclarationToCollectionIfMissing', () => {
      it('should add a AnnualDeclaration to an empty array', () => {
        const annualDeclaration: IAnnualDeclaration = sampleWithRequiredData;
        expectedResult = service.addAnnualDeclarationToCollectionIfMissing([], annualDeclaration);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(annualDeclaration);
      });

      it('should not add a AnnualDeclaration to an array that contains it', () => {
        const annualDeclaration: IAnnualDeclaration = sampleWithRequiredData;
        const annualDeclarationCollection: IAnnualDeclaration[] = [
          {
            ...annualDeclaration,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAnnualDeclarationToCollectionIfMissing(annualDeclarationCollection, annualDeclaration);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a AnnualDeclaration to an array that doesn't contain it", () => {
        const annualDeclaration: IAnnualDeclaration = sampleWithRequiredData;
        const annualDeclarationCollection: IAnnualDeclaration[] = [sampleWithPartialData];
        expectedResult = service.addAnnualDeclarationToCollectionIfMissing(annualDeclarationCollection, annualDeclaration);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(annualDeclaration);
      });

      it('should add only unique AnnualDeclaration to an array', () => {
        const annualDeclarationArray: IAnnualDeclaration[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const annualDeclarationCollection: IAnnualDeclaration[] = [sampleWithRequiredData];
        expectedResult = service.addAnnualDeclarationToCollectionIfMissing(annualDeclarationCollection, ...annualDeclarationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const annualDeclaration: IAnnualDeclaration = sampleWithRequiredData;
        const annualDeclaration2: IAnnualDeclaration = sampleWithPartialData;
        expectedResult = service.addAnnualDeclarationToCollectionIfMissing([], annualDeclaration, annualDeclaration2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(annualDeclaration);
        expect(expectedResult).toContain(annualDeclaration2);
      });

      it('should accept null and undefined values', () => {
        const annualDeclaration: IAnnualDeclaration = sampleWithRequiredData;
        expectedResult = service.addAnnualDeclarationToCollectionIfMissing([], null, annualDeclaration, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(annualDeclaration);
      });

      it('should return initial array if no AnnualDeclaration is added', () => {
        const annualDeclarationCollection: IAnnualDeclaration[] = [sampleWithRequiredData];
        expectedResult = service.addAnnualDeclarationToCollectionIfMissing(annualDeclarationCollection, undefined, null);
        expect(expectedResult).toEqual(annualDeclarationCollection);
      });
    });

    describe('compareAnnualDeclaration', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAnnualDeclaration(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAnnualDeclaration(entity1, entity2);
        const compareResult2 = service.compareAnnualDeclaration(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAnnualDeclaration(entity1, entity2);
        const compareResult2 = service.compareAnnualDeclaration(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAnnualDeclaration(entity1, entity2);
        const compareResult2 = service.compareAnnualDeclaration(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
