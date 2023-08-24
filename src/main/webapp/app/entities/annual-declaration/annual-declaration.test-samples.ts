import dayjs from 'dayjs/esm';

import { DeclarationStatus } from 'app/entities/enumerations/declaration-status.model';

import { IAnnualDeclaration, NewAnnualDeclaration } from './annual-declaration.model';

export const sampleWithRequiredData: IAnnualDeclaration = {
  id: 96800,
  submissionDate: dayjs('2023-08-22'),
  status: DeclarationStatus['REJECTED'],
};

export const sampleWithPartialData: IAnnualDeclaration = {
  id: 99997,
  submissionDate: dayjs('2023-08-23'),
  status: DeclarationStatus['SUBMITTED'],
};

export const sampleWithFullData: IAnnualDeclaration = {
  id: 53060,
  submissionDate: dayjs('2023-08-23'),
  status: DeclarationStatus['APPROVED'],
};

export const sampleWithNewData: NewAnnualDeclaration = {
  submissionDate: dayjs('2023-08-23'),
  status: DeclarationStatus['REJECTED'],
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
