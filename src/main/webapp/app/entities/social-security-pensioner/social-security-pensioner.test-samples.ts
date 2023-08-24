import dayjs from 'dayjs/esm';

import { ISocialSecurityPensioner, NewSocialSecurityPensioner } from './social-security-pensioner.model';

export const sampleWithRequiredData: ISocialSecurityPensioner = {
  id: 61894,
  nationalNumber: 'Pizza Falkla',
  pensionNumber: 'SQL Unbranded',
  dateOfBirth: dayjs('2023-08-23'),
  address: 'Rubber',
};

export const sampleWithPartialData: ISocialSecurityPensioner = {
  id: 71468,
  nationalNumber: 'Sausages pin',
  pensionNumber: 'Rubber Investment solid',
  dateOfBirth: dayjs('2023-08-23'),
  address: 'Officer Bedfordshire',
};

export const sampleWithFullData: ISocialSecurityPensioner = {
  id: 3991,
  nationalNumber: 'analyzer Arm',
  pensionNumber: 'Quality',
  dateOfBirth: dayjs('2023-08-22'),
  address: 'Forward',
};

export const sampleWithNewData: NewSocialSecurityPensioner = {
  nationalNumber: 'bypass Trini',
  pensionNumber: 'payment Optimized modular',
  dateOfBirth: dayjs('2023-08-22'),
  address: 'Bacon',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
